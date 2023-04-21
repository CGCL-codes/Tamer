package markgame2d.engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public abstract class MarkGame {

    private MarkJPanel panel;

    private MarkRunner runner;

    protected final int width;

    protected final int height;

    private boolean running = false;

    private int delay;

    private Color bgColor;

    private MarkScreen screen;

    private boolean[] keys;

    public MarkGame(int screenWidth, int screenHeight, int fps, Color bgColor) {
        this.width = screenWidth;
        this.height = screenHeight;
        this.delay = 1000 / fps;
        this.bgColor = bgColor;
        panel = new MarkJPanel();
        runner = new MarkRunner();
        screen = new MarkScreen() {
        };
        keys = new boolean[0xFFFF + 1];
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public boolean[] getKeys() {
        return keys;
    }

    public MarkJPanel getPanel() {
        return panel;
    }

    private void logic(float time) {
        synchronized (this) {
            screen.update(time);
        }
    }

    public void setScreen(MarkScreen screen) {
        if (screen == null) {
            System.err.print("Screen = null.");
            return;
        }
        this.screen.onExit();
        this.screen = screen;
        this.screen.onEnter();
    }

    private void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        screen.keyPressed(e);
    }

    private void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        screen.keyReleased(e);
    }

    private void keyTyped(KeyEvent e) {
        screen.keyTyped(e);
    }

    public final boolean isRunning() {
        return running;
    }

    public void start() {
        if (!running) {
            running = true;
            new Thread(runner).start();
        }
    }

    public final void stop() {
        running = false;
    }

    private class MarkJPanel extends JPanel {

        public MarkJPanel() {
            setFocusable(true);
            setBackground(bgColor);
            setDoubleBuffered(true);
            addKeyListener(new MarkKeyListener());
        }

        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }

        public Dimension getSize() {
            return new Dimension(width, height);
        }

        public void paint(Graphics g) {
            super.paint(g);
            synchronized (MarkGame.this) {
                MarkGame.this.screen.paint((Graphics2D) g);
            }
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
    }

    private class MarkRunner implements Runnable {

        public void run() {
            long beforeTime = System.currentTimeMillis();
            while (true) {
                long currentTime = System.currentTimeMillis();
                long timeDiff = currentTime - beforeTime;
                long sleep = delay - timeDiff;
                if (timeDiff != 0) {
                    logic(timeDiff / 1000f);
                    panel.repaint();
                }
                if (sleep < 0) sleep = 2;
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    System.err.println("interrupted");
                }
                beforeTime = currentTime;
            }
        }
    }

    private class MarkKeyListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
            synchronized (MarkGame.this) {
                MarkGame.this.keyPressed(e);
            }
        }

        public void keyReleased(KeyEvent e) {
            synchronized (MarkGame.this) {
                MarkGame.this.keyReleased(e);
            }
        }

        public void keyTyped(KeyEvent e) {
            synchronized (MarkGame.this) {
                MarkGame.this.keyTyped(e);
            }
        }
    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(MarkGame.class.getClassLoader().getResource(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
