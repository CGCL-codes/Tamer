package com.tangledcode.mtm_game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.Random;
import javax.swing.JFrame;
import com.tangledcode.mtm_game.input.InputManager;
import com.tangledcode.mtm_game.util.Settings;

public class GameController {

    public static final int GS_NULL = 0;

    public static final int GS_GAME = 1;

    public static final int GS_PAUSED = 2;

    public static final int GS_MENU = 3;

    public static final int GS_INTRO = 4;

    private static int gameState;

    public static final int SCREEN_WIDTH = 1280;

    public static final int SCREEN_HEIGHT = 800;

    public static final boolean FULLSCREEN = false;

    public static final String TITLE = new String("Minutes to Midnigth");

    public static final DisplayMode[] BEST_DISPLAY_MODES = new DisplayMode[] { new DisplayMode(GameController.SCREEN_WIDTH, GameController.SCREEN_HEIGHT, 32, 0), new DisplayMode(GameController.SCREEN_WIDTH, GameController.SCREEN_HEIGHT, 16, 0), new DisplayMode(GameController.SCREEN_WIDTH, GameController.SCREEN_HEIGHT, 8, 0) };

    private JFrame frame;

    private BufferStrategy buffer;

    private BufferedImage bi;

    private GraphicsDevice gd;

    public static final Random random = new Random();

    private boolean running;

    private boolean paused;

    private int fps;

    public GameController() {
        this.running = true;
        this.paused = false;
        this.fps = 0;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        this.frame = new JFrame(GameController.TITLE, gc);
        if (GameController.FULLSCREEN) {
            this.frame.setUndecorated(true);
        } else {
            this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        this.frame.setIgnoreRepaint(true);
        if (GameController.FULLSCREEN) {
            if (gd.isFullScreenSupported()) {
                gd.setFullScreenWindow(this.frame);
                if (gd.isDisplayChangeSupported()) {
                    DisplayMode best = this.getBestDisplayMode();
                    if (best != null) {
                        gd.setDisplayMode(best);
                    } else {
                        System.out.println("Display Mode could not be found");
                        System.exit(0);
                    }
                } else {
                    System.out.println("Cannot change into fullscreen mode");
                    System.exit(0);
                }
            } else {
                System.out.println("Fullscreen mode not supported");
                System.exit(0);
            }
        } else {
            this.frame.setPreferredSize(new Dimension(GameController.SCREEN_WIDTH, GameController.SCREEN_HEIGHT));
            this.frame.pack();
            this.frame.setVisible(true);
            this.frame.setLocationRelativeTo(null);
        }
        this.frame.createBufferStrategy(2);
        this.buffer = this.frame.getBufferStrategy();
        this.bi = gc.createCompatibleImage(GameController.SCREEN_WIDTH, GameController.SCREEN_HEIGHT);
        if (GameController.FULLSCREEN) {
            Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, new int[16 * 16], 0, 16));
            Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisibleCursor");
            this.frame.setCursor(transparentCursor);
        }
    }

    public static int getGameState() {
        return GameController.gameState;
    }

    public static void setGameState(int gameState) {
        GameController.gameState = gameState;
    }

    public void run() {
        int frames = 0;
        long totalTime = 0L;
        long timeDiff = 0L;
        long currentTime = System.currentTimeMillis();
        long lastTime = currentTime;
        while (this.running) {
            lastTime = currentTime;
            currentTime = System.currentTimeMillis();
            timeDiff = currentTime - lastTime;
            totalTime += currentTime - lastTime;
            if (totalTime > 1000L) {
                totalTime -= 1000L;
                this.fps = frames;
                frames = 0;
            }
            ++frames;
            this.input(timeDiff);
            this.update(timeDiff);
            this.draw();
        }
    }

    private void input(long timeDiff) {
        InputManager.pullInput(timeDiff);
    }

    private void update(long timeDiff) {
        if (GameController.getGameState() != GameController.GS_PAUSED) {
            LayerManager.getLayer(LayerManager.BACKGROUND).update(timeDiff);
            LayerManager.getLayer(LayerManager.ENTITIES).update(timeDiff);
            LayerManager.getLayer(LayerManager.EFFECTS).update(timeDiff);
            LayerManager.getLayer(LayerManager.LABELS).update(timeDiff);
            LayerManager.getLayer(LayerManager.PROJECTILES).update(timeDiff);
        }
        LayerManager.getLayer(LayerManager.FOREGROUND).update(timeDiff);
    }

    private void draw() {
        Graphics2D g2d = this.bi.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, GameController.SCREEN_WIDTH, GameController.SCREEN_HEIGHT);
        if (GameController.getGameState() != GameController.GS_PAUSED) {
            LayerManager.getLayer(LayerManager.ENTITIES).draw(g2d);
            LayerManager.getLayer(LayerManager.EFFECTS).draw(g2d);
            LayerManager.getLayer(LayerManager.LABELS).draw(g2d);
            LayerManager.getLayer(LayerManager.PROJECTILES).draw(g2d);
        }
        LayerManager.getLayer(LayerManager.FOREGROUND).draw(g2d);
        if (Settings.getSetting("dev.statistics").equals("true")) {
            g2d.setFont(new Font("Helvetica", Font.BOLD, 14));
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.format("FPS: %s", this.fps), 20, 40);
            g2d.drawString(String.format("Objects: %s (%s)", LayerManager.size(), LayerManager.visibleSize()), 20, 54);
        }
        Graphics g = this.buffer.getDrawGraphics();
        g.drawImage(this.bi, 0, 0, null);
        if (!this.buffer.contentsLost()) {
            this.buffer.show();
        }
        if (g != null) {
            g.dispose();
        }
        if (g2d != null) {
            g2d.dispose();
        }
    }

    private DisplayMode getBestDisplayMode() {
        for (int i = 0; i < GameController.BEST_DISPLAY_MODES.length; i++) {
            DisplayMode[] modes = this.gd.getDisplayModes();
            for (int j = 0; j < modes.length; j++) {
                if (modes[j].getWidth() == GameController.BEST_DISPLAY_MODES[i].getWidth() && modes[j].getHeight() == GameController.BEST_DISPLAY_MODES[i].getHeight() && modes[j].getBitDepth() == GameController.BEST_DISPLAY_MODES[i].getBitDepth()) {
                    return GameController.BEST_DISPLAY_MODES[i];
                }
            }
        }
        return null;
    }
}
