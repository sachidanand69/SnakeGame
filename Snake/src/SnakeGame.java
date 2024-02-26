import java.awt.*;
import java.awt.event.*;

//ArrayList is used for storing the segment of the snake body
import java.util.ArrayList;

//This is used for getting the random x and y values
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    //step 1: Creating the Tile class for fixing the co-ordinates for the snake and food inside the SnakeGame class
    private class Tile {

        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
    
    //frame width
    int boardWidth;
    
    //frame height
    int boardheight;

    //tilesize used to set the height and width of the snake and food
    int tilesize = 25;

    //It is the reference of the tile class, by using the reference 
    //we can call the x and y co-ordinate of the tile class for adjusting the co-ordinate of snake or food
    Tile snakeHead;
    
    //Snake Food
    Tile food;

    //Randomly placing the food
    Random random;

    //Game Logic
    Timer gameLoop;

    //By using the arraylist we can create snakebody
    ArrayList<Tile> snakeBody;

    //Game Over
    boolean gameOver=false;

    int velocityX;
    int velocityY;

    SnakeGame(int boardWidth, int boardheight) {
        this.boardWidth = boardWidth;
        this.boardheight = boardheight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardheight));

        //setting the background color of the frame
        setBackground(Color.BLACK);

        //Listening the keyboard keys
        addKeyListener(this);
        setFocusable(true);

        //declaring the co-ordinate of the snake
        snakeHead=new Tile(5,5);
        
        //declaring the food co-ordinate for the snake
        food=new Tile(10,10);


        snakeBody=new ArrayList<Tile>();

        random=new Random();
        placeFood();

        //we can set the movement of the snake in horizontal direction
        velocityX=0;
        //we can set the movement of the snake in vertical direction
        velocityY=1;

        gameLoop=new Timer(300, this);
        gameLoop.start();
    }

    public boolean colision(Tile tile1, Tile tile2){
        return tile1.x==tile2.x && tile1.y==tile2.y;
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //Grid
        for(int i=0; i<boardWidth/tilesize; i++){

            g.drawLine(i*tilesize, 0, i*tilesize, boardheight);
            g.drawLine(0, i*tilesize, boardWidth, i*tilesize);
        }
        // setting the size and color of food
        g.setColor(Color.red);
        g.fillRect(food.x*tilesize, food.y*tilesize, tilesize, tilesize);

        //snake
            g.setColor(Color.green);
            g.fillRect(snakeHead.x*tilesize, snakeHead.y*tilesize, tilesize, tilesize);

        //snakebody
        for(int i=0;i<snakeBody.size();i++){
            Tile snakePart=snakeBody.get(i);
            g.fillRect(snakePart.x * tilesize, snakePart.y*tilesize, tilesize, tilesize);
        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN,16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: "+String.valueOf(snakeBody.size()), tilesize-16, tilesize);
        }else{
            g.drawString("Score: "+ String.valueOf(snakeBody.size()), tilesize-16, tilesize);
        }
    }

    public void placeFood(){
        food.x=random.nextInt(boardWidth/tilesize);
        food.y=random.nextInt(boardheight/tilesize);
    }

    public void move(){
        // eat snake
        if(colision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //snakebody moving with head
        for(int i=snakeBody.size()-1; i>=0; i--){
            Tile snakePart=snakeBody.get(i);
            if(i==0){
                snakePart.x=snakeHead.x;
                snakePart.y=snakeHead.y;
            }else{
                Tile prevSnakePart= snakeBody.get(i-1);
                snakePart.x=prevSnakePart.x;
                snakePart.y=prevSnakePart.y;
            }
        }

        snakeHead.x+=velocityX;
        snakeHead.y+=velocityY;

        //GameOver Logic
        for(int i=0; i<snakeBody.size(); i++){
            Tile snakePart=snakeBody.get(i);
            //collision condition with thier own part
            if(colision(snakeHead, snakePart)){
                 gameOver=true;
            }
        }
        //collision condition with the wall
        if(snakeHead.x*tilesize<0 || snakeHead.x*tilesize> boardWidth 
           || snakeHead.y*tilesize<0 || snakeHead.y*tilesize>boardheight){
                gameOver=true;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_UP && velocityY!=1){
            velocityX=0;
            velocityY=-1;
        }else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY !=-1){
            velocityX=0;
            velocityY=1;
        }else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1){
            velocityX=-1;
            velocityY=0;
        }else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1){
            velocityX=1;
            velocityY=0;
        }
    }

    //No Need
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
