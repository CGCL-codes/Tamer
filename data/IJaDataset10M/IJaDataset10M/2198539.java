package display;

import java.awt.*;
import listener.KeyAction;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import world.*;
import world.shot.Shot;
import javax.swing.*;
import graphics.Camera;

public class WorldCanvas extends Canvas implements Runnable, FocusListener {

    private static final long serialVersionUID = 1L;

    World w;

    JFrame owner;

    double fps = 0;

    BufferStrategy bs;

    Camera c = new Camera(0, 0);

    KeyAction ka = new KeyAction();

    public WorldCanvas(JFrame owner, World w, int width, int height) {
        this.w = w;
        this.owner = owner;
        setSize(width, height);
        owner.addKeyListener(ka);
    }

    public void setBufferStrategy(BufferStrategy bs) {
        this.bs = bs;
    }

    public void run() {
        createBufferStrategy(3);
        bs = getBufferStrategy();
        double total = 0;
        int fpsCount = 0;
        int maxfpsCount = 150;
        for (; ; ) {
            double start = System.currentTimeMillis();
            c.setWidth(getWidth());
            c.setHeight(getHeight());
            ka.updateCamera(c);
            if (bs != null) {
                drawWorld(c);
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
            double end = System.currentTimeMillis();
            fpsCount++;
            total += (int) (1 / ((end - start) / 1000));
            fps = total / fpsCount;
            if (fpsCount == maxfpsCount) {
                fpsCount = 0;
                total = 0;
            }
        }
    }

    private void drawWorld(Camera c) {
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.fillRect(0, 0, getWidth(), getHeight());
        ArrayList<Shot> s = w.getShotEngine().getShots();
        for (int i = 0; i < s.size(); i++) {
            s.get(i).drawShot(g, c);
        }
        g.setColor(Color.cyan);
        Point p = c.getScreenLocation(new Point(w.getWidth() / 2 - 10, w.getHeight() / 2 - 10));
        g.fillRect(p.x, p.y, 20, 20);
        w.drawWorld(g, c);
        g.setColor(Color.black);
        g.drawRect(0 - c.getxover(), 0 - c.getyover(), w.getWidth(), w.getHeight());
        g.drawString("FPS: " + fps, 3, 16);
        g.dispose();
        bs.show();
    }

    public void focusGained(FocusEvent e) {
        owner.requestFocus();
    }

    public void focusLost(FocusEvent e) {
    }
}
