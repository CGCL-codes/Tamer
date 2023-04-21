package ch.randelshofer.splasher;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * A Splash window.
 *  <p>
 * Usage: MyApplication is your application class. Create a Splasher class which
 * opens the splash window, invokes the main method of your Application class,
 * and disposes the splash window afterwards.
 * Please note that we want to keep the Splasher class and the SplashWindow class
 * as small as possible. The less code and the less classes must be loaded into
 * the JVM to open the splash screen, the faster it will appear.
 * <pre>
 * class Splasher {
 *    public static void main(String[] args) {
 *         SplashWindow.splash(Startup.class.getResource("splash.gif"));
 *         MyApplication.main(args);
 *         SplashWindow.disposeSplash();
 *    }
 * }
 * </pre>
 *
 * @author  Werner Randelshofer
 * @version 2.2.1 2006-05-27 Abort when splash image can not be loaded.
 */
@SuppressWarnings("serial")
public class SplashWindow extends Window {

    /**
     * The current instance of the splash window.
     * (Singleton design pattern).
     */
    private static SplashWindow instance;

    /**
     * The splash image which is displayed on the splash window.
     */
    private Image image;

    /**
     * This attribute indicates whether the method
     * paint(Graphics) has been called at least once since the
     * construction of this window.<br>
     * This attribute is used to notify method splash(Image)
     * that the window has been drawn at least once
     * by the AWT event dispatcher thread.<br>
     * This attribute acts like a latch. Once set to true,
     * it will never be changed back to false again.
     *
     * @see #paint
     * @see #splash
     */
    private boolean paintCalled = false;

    /**
     * Creates a new instance.
     * @param parent the parent of the window.
     * @param image the splash image.
     */
    private SplashWindow(Frame parent, Image image) {
        super(parent);
        this.image = image;
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image, 0);
        try {
            mt.waitForID(0);
        } catch (InterruptedException ie) {
        }
        if (mt.isErrorID(0)) {
            setSize(0, 0);
            System.err.println("Warning: SplashWindow couldn't load splash image.");
            synchronized (this) {
                paintCalled = true;
                notifyAll();
            }
            return;
        }
        int imgWidth = image.getWidth(this);
        int imgHeight = image.getHeight(this);
        setSize(imgWidth, imgHeight);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenDim.width - imgWidth) / 2, (screenDim.height - imgHeight) / 2);
        MouseAdapter disposeOnClick = new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                synchronized (SplashWindow.this) {
                    SplashWindow.this.paintCalled = true;
                    SplashWindow.this.notifyAll();
                }
                dispose();
            }
        };
        addMouseListener(disposeOnClick);
    }

    /**
     * Updates the display area of the window.
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Paints the image on the window.
     */
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
        if (!paintCalled) {
            paintCalled = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Open's a splash window using the specified image.
     * @param image The splash image.
     */
    @SuppressWarnings("deprecation")
    public static void splash(Image image) {
        if (instance == null && image != null) {
            Frame f = new Frame();
            instance = new SplashWindow(f, image);
            instance.show();
            if (!EventQueue.isDispatchThread() && Runtime.getRuntime().availableProcessors() == 1) {
                synchronized (instance) {
                    while (!instance.paintCalled) {
                        try {
                            instance.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
    }

    /**
     * Open's a splash window using the specified image.
     * @param imageURL The url of the splash image.
     */
    public static void splash(URL imageURL) {
        if (imageURL != null) {
            splash(Toolkit.getDefaultToolkit().createImage(imageURL));
        }
    }

    /**
     * Closes the splash window.
     */
    public static void disposeSplash() {
        if (instance != null) {
            instance.getOwner().dispose();
            instance = null;
        }
    }

    /**
     * Invokes the main method of the provided class name.
     * @param args the command line arguments
     */
    public static void invokeMain(String className, String[] args) {
        try {
            Class.forName(className).getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });
        } catch (Exception e) {
            InternalError error = new InternalError("Failed to invoke main method");
            error.initCause(e);
            throw error;
        }
    }
}
