package org.vmpc.simant;

import org.vmpc.simant.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author svenni (og Vegard :-])
 */
public class Game extends Canvas {

    /** The stragey that allows us to use accelerate page flipping */
    private BufferStrategy strategy;

    /** True if the game is currently "running", i.e. the game loop is looping */
    private boolean gameRunning = true;

    /** The list of all the entities that exist in our game */
    private ArrayList entities = new ArrayList();

    /** The list of entities that need to be removed from the game this loop */
    private ArrayList removeList = new ArrayList();

    /** The entity representing the player */
    private Entity home;

    /** Test **/
    private Entity food;

    private Entity food2;

    private Entity maursluker;

    /** The speed at which the player's ant should move (pixels/sec) */
    private double moveSpeed = 300;

    /** The time at which last fired a shot */
    private long lastFire = 0;

    /** The interval between our players shot (ms) */
    private long firingInterval = 500;

    /** The number of aliens left on the screen */
    private int alienCount;

    /** The message to display which waiting for a key press */
    private String message = "";

    /** True if we're holding up game play until a key has been pressed */
    private boolean waitingForKeyPress = true;

    /** True if the left cursor key is currently pressed */
    private boolean leftPressed = false;

    /** True if the right cursor key is currently pressed */
    private boolean rightPressed = false;

    /** True if we are firing */
    private boolean firePressed = false;

    /** True if game logic needs to be applied this loop, normally as a result of a game event */
    private boolean logicRequiredThisLoop = false;

    private double test = 0;

    private int canvasWidth = 800;

    private int canvasHeight = 600;

    public int homeX = 50;

    public int homeY = 50;

    public boolean collArray[];

    long lastLoopTime;

    private boolean gamePaused = false;

    public Game() {
        JFrame container = new JFrame("SimAnt");
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        panel.setLayout(null);
        setBounds(0, 0, canvasWidth, canvasHeight);
        panel.add(this);
        setIgnoreRepaint(true);
        container.pack();
        container.setResizable(false);
        container.setVisible(true);
        container.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        addMouseListener(new MouseInputHandler());
        addKeyListener(new KeyInputHandler());
        requestFocus();
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        initEntities();
    }

    public void gameLoop() {
        double frameTimeStart;
        double frameTimeEnd;
        double frameTime = 0;
        int frameTimeCalculateEveryFrame = 5;
        int frameTimeCalculated;
        double lastFrameRateTime = System.currentTimeMillis();
        double lastFrameRate = 0;
        ArrayList<Long> frameRates = new ArrayList();
        double frameRate;
        double checkFrameRateEveryMilli = 250;
        double deltaFrameRate;
        long delta;
        lastLoopTime = System.currentTimeMillis();
        while (gameRunning) {
            frameTimeStart = System.nanoTime();
            if (!gamePaused) {
                delta = System.currentTimeMillis() - lastLoopTime;
                deltaFrameRate = System.currentTimeMillis() - lastFrameRateTime;
                lastLoopTime = System.currentTimeMillis();
                Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setColor(Color.GREEN);
                g.fillRect(0, 0, canvasWidth, canvasHeight);
                if (delta > 0) {
                    frameRates.add(1000 / delta);
                }
                if (deltaFrameRate > checkFrameRateEveryMilli) {
                    double frameRateTotal = 0;
                    for (double aFrameRate : frameRates) {
                        frameRateTotal += aFrameRate;
                    }
                    frameRate = frameRateTotal / frameRates.size();
                    frameRates.clear();
                    lastFrameRate = frameRate;
                    lastFrameRateTime = System.currentTimeMillis();
                } else {
                    frameRate = lastFrameRate;
                }
                int collisionSlot = 0;
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = (Entity) entities.get(i);
                    entity.move(delta);
                    entity.draw(g);
                    for (int s = i + 1; s < entities.size(); s++) {
                        Entity me = (Entity) entities.get(i);
                        Entity him = (Entity) entities.get(s);
                        if (me.collidesWith(him)) {
                            if (!collArray[collisionSlot]) {
                                me.collidedWith(him);
                                him.collidedWith(me);
                                collArray[collisionSlot] = true;
                            }
                        } else {
                            collArray[collisionSlot] = false;
                        }
                        collisionSlot++;
                    }
                    if (logicRequiredThisLoop) {
                        entity.doLogic();
                    }
                }
                logicRequiredThisLoop = false;
                frameTimeEnd = System.nanoTime();
                frameTime = frameTimeEnd - frameTimeStart;
                entities.removeAll(removeList);
                removeList.clear();
                g.setColor(Color.black);
                g.drawString("FPS: " + (int) frameRate, 2, 20);
                g.drawString("FrameTime: " + frameTime, 60, 20);
                g.drawString("Food: " + home.getFoodAmount() + " units.", 2, 40);
                g.drawString("Food left: " + food.getFoodAmount() + " units", food.getX(), food.getY() - 5);
                g.drawString("Food left: " + food2.getFoodAmount() + " units", food2.getX(), food2.getY() - 5);
                g.dispose();
                strategy.show();
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class MouseInputHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent mouse) {
            System.out.println("Mouse pressed at: " + mouse.getX() + " " + mouse.getY());
            if (!gamePaused) {
            }
        }
    }

    private class KeyInputHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent key) {
            if (key.getKeyCode() == KeyEvent.VK_P) {
                gamePaused = !gamePaused;
                lastLoopTime = System.currentTimeMillis();
            }
            if (!gamePaused) {
            }
        }
    }

    private void initEntities() {
        home = new HomeEntity(this, "tue.png", homeX, homeY);
        entities.add(home);
        home.setSpeed(0);
        home.setAngleDegrees(0);
        food = new FoodEntity(this, "food.png", 650, 500);
        entities.add(food);
        food.setSpeed(0);
        food.setAngleDegrees(0);
        food2 = new FoodEntity(this, "food.png", 500, 200);
        entities.add(food2);
        food2.setSpeed(0);
        food2.setAngleDegrees(270);
        for (int x = 1; x < 50; x++) {
            Entity entity = new AntEntity(this, "maur2.png", home.getX() + 20, home.getY() + 20);
            entities.add(entity);
            entity.setSpeed(40 + x);
            entity.setAngleDegrees(1 + x * 7 * Math.random());
        }
        maursluker = new AntEntity(this, "maur.png", home.getX() + 20, home.getY() + 20);
        entities.add(maursluker);
        maursluker.setSpeed(100);
        maursluker.setAngleDegrees(45);
        collArray = new boolean[(int) (0.5 * (entities.size() * entities.size() + entities.size()))];
        for (int a = 0; a < (int) 0.5 * (entities.size() * entities.size() + entities.size()); a++) {
            collArray[a] = true;
        }
    }

    public int getcanvasWidth() {
        return this.canvasWidth;
    }

    public int getcanvasHeight() {
        return this.canvasHeight;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game g = new Game();
        g.gameLoop();
    }
}
