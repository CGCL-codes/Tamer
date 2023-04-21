package pl.adyga.jpop.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GamePanel {

    private static final long ONE_SEC_NS = 1000000000;

    private Line2D line = new Line2D.Float();

    private long lastUpdate;

    private long counter;

    private BufferedImage img;

    private BufferedImage baloonImg;

    private boolean paused;

    private boolean running;

    private int frameCounter;

    private long frameCountingStart;

    private int fps;

    private float alpha, x0 = 640, y0 = 512;

    private final float alphaPerSec = (float) (Math.PI);

    private long currentTime;

    public void init(int width, int heigth, int colorDepth) throws IOException {
        running = true;
        img = ImageIO.read(getClass().getResource("/pl/adyga/jpop/resource/image/bg/wood_stream.png"));
        baloonImg = ImageIO.read(getClass().getResource("/pl/adyga/jpop/resource/image/baloon_red.png"));
    }

    /** Save current time, must be called at the beginning of each game update */
    private void saveCurrentTime() {
        currentTime = System.nanoTime();
    }

    private long getCurrentTime() {
        return currentTime;
    }

    /** Must be called at game update end. */
    private void saveLastUpdateTime() {
        lastUpdate = currentTime;
    }

    /** Time from previous update in ms */
    private long lastUpdateTimeDiffMs() {
        return (currentTime - lastUpdate) / 1000000;
    }

    public void gameUpdate() {
        saveCurrentTime();
        if (lastUpdate > 0) {
            if (getCurrentTime() - frameCountingStart >= ONE_SEC_NS) {
                fps = frameCounter;
                frameCounter = 0;
                frameCountingStart = getCurrentTime();
            }
            if (!paused) {
                alpha += alphaPerSec * lastUpdateTimeDiffMs() / 1000;
                float y = getY(0);
                if (y >= 0) line.setLine(0, y, 1280, 1024 - y); else {
                    line.setLine(getX1(), 0, 1280 - getX1(), 1024);
                }
            }
        }
        saveLastUpdateTime();
    }

    public float getY(float x) {
        return (float) Math.tan(alpha) * (x - x0) + y0;
    }

    public float getX1() {
        return x0 - y0 / (float) Math.tan(alpha);
    }

    public void render(Graphics2D g2) {
        long time = System.nanoTime();
        frameCounter++;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 1280, 1024);
        AffineTransform at = new AffineTransform();
        at.setToScale((1050f + frameCounter) / 1024, 1050f / 1024);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        g2.drawImage(img, at, null);
        g2.setColor(Color.RED);
        g2.draw(line);
        g2.drawString("FPS: " + fps, 10, 10);
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                running = false;
                break;
            case KeyEvent.VK_P:
                paused = !paused;
                break;
        }
    }

    public boolean running() {
        return running;
    }
}
