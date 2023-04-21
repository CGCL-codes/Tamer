package oreactor.video;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import oreactor.core.Logger;
import oreactor.core.Reactor;
import oreactor.core.Settings.VideoMode;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

public class VideoDeviceManager {

    int currentPosition = -1;

    VideoDevice device;

    boolean fullscreenEnabled = false;

    Point[] positions = null;

    public static interface VideoDeviceObserver {

        public void sizeChanged(double width, double height);
    }

    static final Logger logger = Logger.getLogger();

    void handleException(VideoMode vm, Exception e) throws OpenReactorException {
        if (VideoMode.FULL.equals(vm)) {
            String msg = "Failed to go to full screen mode: Quitting.";
            logger.info(msg);
            logger.debug(msg, e);
            ExceptionThrower.throwVideoException(msg, e);
        } else if (VideoMode.FULL_FALLBACK.equals(vm)) {
            String msg = "Failed to go to full screen mode: Falling back.";
            logger.info(msg);
            logger.debug(msg, e);
        } else {
        }
    }

    private boolean closed;

    private double height;

    private List<VideoDeviceObserver> observers = new LinkedList<VideoDeviceObserver>();

    List<Plane> planes;

    Reactor reactor;

    private BufferStrategy strategy;

    private double width;

    public VideoDeviceManager(Reactor reactor) throws OpenReactorException {
        this.reactor = reactor;
        this.planes = new LinkedList<Plane>();
        this.width = reactor.screenWidth();
        this.height = reactor.screenHeight();
        this.closed = false;
        this.device = reactor.videoDevice();
    }

    public void addKeyListener(KeyListener d) {
        this.device.addKeyListener(d);
    }

    public void addObserver(VideoDeviceObserver o) {
        if (!this.observers.contains(o)) {
            this.observers.add(o);
        }
    }

    public void close() {
        this.closed = true;
    }

    public void createPlane(PlaneDesc desc) {
        Plane p = desc.createPlane(new Viewport(this.width, this.height));
        logger.debug("Created plane is:<" + p + ">");
        this.addObserver(p);
        this.planes.add(p);
    }

    public void finish() {
        List<Plane> rev = new LinkedList<Plane>();
        rev.addAll(this.planes);
        Collections.reverse(rev);
        for (Plane p : rev) {
            if (p.isEnabled()) {
                p.finish();
            }
        }
    }

    public double height() {
        return this.height;
    }

    public void initialize() throws OpenReactorException {
        initializePositions(reactor);
        VideoMode vm = reactor.settings().videoMode();
        logger.debug("Video mode is set to <" + vm + ">");
        this.device.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                device.setVisible(false);
                device.dispose();
                close();
            }
        });
        if (VideoMode.FULL.equals(vm) || VideoMode.FULL_FALLBACK.equals(vm)) {
            this.device.init();
            boolean succeeded = false;
            DisplayMode dm = new DisplayMode(reactor.screenWidth(), reactor.screenHeight(), reactor.screenColorDepth(), reactor.screenRefreshRate());
            try {
                this.device.goFullScreenMode(dm);
            } catch (UnsupportedOperationException e) {
                handleException(vm, e);
                throw e;
            } catch (RuntimeException e) {
                handleException(vm, e);
            } finally {
                if (succeeded) {
                    this.fullscreenEnabled = true;
                } else {
                    this.device.leaveFullScreenMode();
                }
            }
        } else {
            logger.debug("Normal screen mode is selected.");
            this.device.moveToCenter(reactor.screenWidth(), reactor.screenHeight());
        }
        this.device.setVisible(true);
        this.device.enableBufferStrategy();
        if (this.device.isBufferStrategyEnabled()) {
            strategy = this.device.createBufferStrategy();
        }
    }

    private void initializePositions(Reactor reactor) {
        int w = reactor.screenWidth();
        int h = reactor.screenHeight();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        List<Point> points = new LinkedList<Point>();
        for (GraphicsDevice gd : ge.getScreenDevices()) {
            Rectangle rect = gd.getDefaultConfiguration().getBounds();
            int x = rect.x + (rect.width - w) / 2;
            int y = rect.y + (rect.height - h) / 2;
            logger.debug("Point:x=<" + x + ">,y=<" + y + "> from:" + rect);
            if (gd.equals(ge.getDefaultScreenDevice())) {
                points.add(0, new Point(x, y));
            } else {
                points.add(new Point(x, y));
            }
        }
        this.positions = points.toArray(new Point[0]);
        this.currentPosition = 0;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public List<Plane> planes() {
        return this.planes;
    }

    public void prepare() {
        Collections.sort(this.planes);
        for (Plane p : this.planes) {
            if (p.isEnabled()) {
                p.prepare();
            }
        }
    }

    public void removeObserver(VideoDeviceObserver o) {
        if (this.observers.contains(o)) {
            this.observers.remove(o);
        }
    }

    public void render() throws OpenReactorException {
        if (this.device.isBufferStrategyEnabled()) {
            do {
                do {
                    Graphics2D graphics = (Graphics2D) strategy.getDrawGraphics();
                    this.renderPlanes(graphics);
                    graphics.dispose();
                } while (strategy.contentsRestored());
                strategy.show();
            } while (strategy.contentsLost());
        } else {
            VolatileImage vImg = this.device.createVolatileImage((int) width, (int) height);
            Graphics2D graphics = vImg.createGraphics();
            do {
                this.renderPlanes(graphics);
                graphics.dispose();
            } while (vImg.contentsLost());
            Graphics2D gg = (Graphics2D) this.device.getGraphics();
            gg.drawImage(vImg, 0, 0, (int) width, (int) height, 0, 0, (int) width, (int) height, null);
            gg.dispose();
        }
    }

    protected void renderPlanes(Graphics2D graphics) throws OpenReactorException {
        for (Plane p : this.planes()) {
            if (p.isEnabled()) {
                p.render(graphics, width, height);
            }
        }
    }

    public void save(File f) throws OpenReactorException {
        BufferedImage bImg = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D graphics = (Graphics2D) bImg.getGraphics();
        this.renderPlanes(graphics);
        graphics.dispose();
        try {
            OutputStream os = new FileOutputStream(f);
            try {
                ImageIO.write(bImg, "png", os);
            } finally {
                os.close();
                bImg = null;
            }
        } catch (IOException e) {
            ExceptionThrower.throwFailedToWriteImage(f, e);
        }
    }

    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
        for (VideoDeviceObserver o : this.observers) {
            o.sizeChanged(width, height);
        }
    }

    public void switchDisplay() {
        if (!fullscreenEnabled) {
            this.currentPosition = (this.currentPosition + 1) % positions.length;
            this.device.setLocation(this.positions[this.currentPosition].x, this.positions[this.currentPosition].y);
            this.device.repaint();
        }
    }

    public void terminate() {
        this.device.leaveFullScreenMode();
    }

    public double width() {
        return this.width;
    }
}
