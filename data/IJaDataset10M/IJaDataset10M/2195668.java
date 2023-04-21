package edu.winona.csclub.wars.gui;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class MainFrame extends JFrame implements Runnable {

    public static final String NAME = "CS Wars";

    private BufferStrategy strategy;

    private GraphicsEngine gc;

    public MainFrame(GraphicsEngine gc) {
        super(NAME);
        this.getContentPane().add(gc);
        this.gc = gc;
        setResizable(true);
        setIgnoreRepaint(false);
        int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 300) / 2;
        int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 300) / 2;
        setLocation(x, y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setVisible(true);
    }

    public void run() {
        createBufferStrategy(2);
        strategy = this.getBufferStrategy();
        while (true) {
            try {
                float target = 1000 / 60.0f;
                float frameAverage = target;
                long lastFrame = System.currentTimeMillis();
                float yield = 10000f;
                float damping = 0.1f;
                long renderTime = 0;
                long logicTime = 0;
                while (true) {
                    long timeNow = System.currentTimeMillis();
                    frameAverage = (frameAverage * 10 + (timeNow - lastFrame)) / 11;
                    lastFrame = timeNow;
                    Thread.sleep(1000);
                    yield += yield * ((target / frameAverage) - 1) * damping + 0.05f;
                    for (int i = 0; i < yield; i++) {
                        Thread.yield();
                    }
                    long beforeRender = System.currentTimeMillis();
                    Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                    g = (Graphics2D) g.create(5, 21, 500, 500);
                    gc.paint(g);
                    g.dispose();
                    strategy.show();
                    renderTime = System.currentTimeMillis() - beforeRender;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
