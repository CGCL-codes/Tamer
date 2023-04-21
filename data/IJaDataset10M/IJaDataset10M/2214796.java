package cn.the.angry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import cn.the.angry.resourcemanager.ResourceManager;
import cn.the.angry.util.AngryPigDebugDraw;
import cn.the.angry.util.GraphicUtils;

public abstract class BaseFrame extends JFrame {

    private final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    private final GraphicsDevice gd = ge.getDefaultScreenDevice();

    protected final BufferedImage backgroundBufferImage;

    protected final BufferedImage foregroundBufferImage;

    private boolean isShowFPS = true;

    private static float targetFPS = 60.0f;

    private int fpsAverageCount = 100;

    private long[] nanos;

    private long nanoStart;

    private long frameCount = 0;

    private long frameRatePeriod;

    protected static int WIDTH;

    protected static int HEIGHT;

    protected AABB worldAABB;

    protected World mWorld;

    protected AngryPigDebugDraw debugDraw;

    private boolean isEnd = false;

    public BaseFrame() {
        super();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        gd.setFullScreenWindow(this);
        WIDTH = getWidth();
        HEIGHT = getHeight();
        backgroundBufferImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        foregroundBufferImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        initCanvas();
        initFPS();
        initWorld();
        initAudio();
        add(panel);
    }

    public void initWorld() {
        worldAABB = new AABB();
        worldAABB.lowerBound.set(-100, -100);
        worldAABB.upperBound.set(1500, 1000);
        debugDraw = new AngryPigDebugDraw(WIDTH, HEIGHT, 1.0f);
        mWorld = new World(worldAABB, new Vec2(0f, 40f), true);
    }

    public void setWorld(Graphics2D g2) {
        debugDraw.setGraphics(g2);
        mWorld.step(1.0f / 60f, 10);
    }

    public abstract void initElements();

    public abstract void drawElements(Graphics2D g2);

    public abstract void initAudio();

    private JPanel panel = new JPanel() {

        private Graphics2D g2;

        @Override
        public void update(Graphics g) {
            paint(g);
        }

        @Override
        public void paint(Graphics g) {
            g2 = (Graphics2D) g;
            g2.drawImage(backgroundBufferImage, 0, 0, null);
            setWorld(g2);
            drawElements(g2);
            drawFPS(g2);
            g2.drawImage(foregroundBufferImage, 0, 0, null);
            g2.dispose();
        }
    };

    private void initCanvas() {
        final Graphics2D backgroundG2d = backgroundBufferImage.createGraphics();
        backgroundG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final Graphics2D foregroundG2d = foregroundBufferImage.createGraphics();
        foregroundG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        backgroundG2d.setColor(new Color(146, 204, 222));
        backgroundG2d.fillRect(0, 0, BaseFrame.WIDTH, BaseFrame.HEIGHT - 318 - 200);
        GraphicUtils.autoFill(backgroundG2d, ResourceManager.getSkyImage(), 0, BaseFrame.HEIGHT - 318 - 200, BaseFrame.WIDTH, BaseFrame.HEIGHT, GraphicUtils.AUTOFILLHORIZONTAL);
        GraphicUtils.autoFill(backgroundG2d, ResourceManager.getParallaxBackgroundImage(), 0, BaseFrame.HEIGHT - 400, BaseFrame.WIDTH, BaseFrame.HEIGHT, GraphicUtils.AUTOFILLHORIZONTAL);
        GraphicUtils.autoFill(backgroundG2d, ResourceManager.getParallaxGrassImage(), 0, BaseFrame.HEIGHT - 260, BaseFrame.WIDTH, BaseFrame.HEIGHT, GraphicUtils.AUTOFILLHORIZONTAL);
        backgroundG2d.drawImage(ResourceManager.getSlingshotRightImage(), 200, BaseFrame.HEIGHT - 200 - ResourceManager.getSlingshotRightImage().getHeight(null), ResourceManager.getSlingshotRightImage().getWidth(null), ResourceManager.getSlingshotRightImage().getHeight(null), null);
        foregroundG2d.drawImage(ResourceManager.getSlingshotLeftImage(), 200 - 15, BaseFrame.HEIGHT - 200 - ResourceManager.getSlingshotRightImage().getHeight(null) - 5, ResourceManager.getSlingshotLeftImage().getWidth(null), ResourceManager.getSlingshotLeftImage().getHeight(null), null);
        GraphicUtils.autoFill(foregroundG2d, ResourceManager.getParallaxGreenGroundImage(), 0, BaseFrame.HEIGHT - 230, BaseFrame.WIDTH, BaseFrame.HEIGHT, GraphicUtils.AUTOFILLHORIZONTAL);
        GraphicUtils.autoFill(foregroundG2d, ResourceManager.getGroundImage(), 0, BaseFrame.HEIGHT - 200, BaseFrame.WIDTH, BaseFrame.HEIGHT, GraphicUtils.AUTOFILLHORIZONTAL);
        backgroundG2d.dispose();
        foregroundG2d.dispose();
    }

    public void initFPS() {
        nanos = new long[fpsAverageCount];
        long nanosPerFrameGuess = (long) (1000000000.0 / targetFPS);
        nanos[fpsAverageCount - 1] = System.nanoTime();
        for (int i = fpsAverageCount - 2; i >= 0; --i) {
            nanos[i] = nanos[i + 1] - nanosPerFrameGuess;
        }
        nanoStart = System.nanoTime();
        frameRatePeriod = (long) (1000000000.0 / targetFPS);
    }

    private final Runnable update = new Runnable() {

        long beforeTime;

        long afterTime;

        long overSleepTime = 0L;

        @Override
        public void run() {
            while (true) {
                long timeDiff = afterTime - beforeTime;
                long sleepTime = (frameRatePeriod - timeDiff) - overSleepTime;
                beforeTime = System.nanoTime();
                overSleepTime = 0L;
                if (!isEnd) {
                    panel.repaint();
                }
                afterTime = System.nanoTime();
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime / 1000000L, (int) (sleepTime % 1000000L));
                    } catch (InterruptedException ex) {
                    }
                    overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
                }
            }
        }
    };

    public void start() {
        isEnd = false;
        new Thread(update).start();
    }

    public void end() {
        isEnd = true;
    }

    public void drawFPS(Graphics2D g2) {
        for (int i = 0; i < fpsAverageCount - 1; ++i) {
            nanos[i] = nanos[i + 1];
        }
        nanos[fpsAverageCount - 1] = System.nanoTime();
        float averagedFPS = (float) ((fpsAverageCount - 1) * 1000000000.0 / (nanos[fpsAverageCount - 1] - nanos[0]));
        ++frameCount;
        float totalFPS = (float) (frameCount * 1000000000 / (1.0 * (System.nanoTime() - nanoStart)));
        g2.drawString("100 Average FPS is " + averagedFPS, 15, 15);
        g2.drawString("Entire FPS is " + totalFPS, 15, 25);
    }

    public void setShowFPS(boolean isShowFPS) {
        this.isShowFPS = isShowFPS;
    }

    public boolean isShowFPS() {
        return isShowFPS;
    }
}
