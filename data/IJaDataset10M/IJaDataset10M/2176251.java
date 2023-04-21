package dream.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/** A startup window is used for displaying a splash screen during
 * startup process.
 *
 * @author mike
 * @version 0.1
 */
public class StartupWindow extends JWindow implements ImageObserver {

    /** This image should fill the whole window. */
    private Image image = null;

    /** Is true if the image has been loaded from a resource path. */
    private boolean imageIsResource = false;

    /** Is true if the image is ready for drawing. */
    private boolean imageReady = false;

    /** Image width. */
    private int sizeX = 0;

    /** Image height. */
    private int sizeY = 0;

    /** Toolkit needed for image operations. */
    private Toolkit tool = Toolkit.getDefaultToolkit();

    /** Text position X. */
    private int textX = 0;

    /** Text position Y. */
    private int textY = 0;

    /** The actual displayed message. */
    private String actualMessage = null;

    /** If true, the window should dispose itself. */
    private boolean toDispose = false;

    /** Creates new StartupWindow */
    public StartupWindow() {
    }

    /** Creates new StartupWindow
     * @param pictureResourcePath the ressource path to the image that is used for the splash screen
     */
    public StartupWindow(String pictureResourcePath) {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        importImage(pictureResourcePath);
        tool.prepareImage(image, -1, -1, this);
    }

    /** Tells the window to dispose itself as soon as possible. */
    public void dispose() {
        toDispose = true;
        super.dispose();
    }

    /** Fixes the positon the output will be printed to.
     * @param x new x position in pixels inside the startup window
     * @param y new y position in pixels inside the startup window
     */
    public void setTextPosition(int x, int y) {
        textX = x;
        textY = y;
    }

    /** Prints the given string onto the screen instantly. The text position can be
     * modified using setTextPosition.
     * @param message the string to be printed
     */
    public void printMessage(String message) {
        final StartupWindow thisWindow = this;
        this.toFront();
        actualMessage = message;
        if (SwingUtilities.isEventDispatchThread()) {
            repaint();
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        thisWindow.repaint();
                    }
                });
            } catch (Exception ex) {
                System.err.println(ex);
            }
            ;
        }
    }

    /** Is called if the image is finally ready, displays it on the screen.
     * @param availableImage the image which should be ready for displaying
     * @param imageWidth the image width
     * @param imageHeight the image height
     */
    public void displayImage(Image availableImage, int imageWidth, int imageHeight) {
        Dimension screenDim = null;
        final StartupWindow thisWindow = this;
        super.show();
        if ((imageWidth > 0) && (imageHeight > 0)) {
            screenDim = tool.getScreenSize();
            setLocation((screenDim.width / 2) - (imageWidth / 2), (screenDim.height / 2) - (imageHeight / 2));
            setSize(imageWidth, imageHeight);
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    thisWindow.repaint();
                }
            });
        } catch (Exception ex) {
            System.err.println(ex);
        }
        ;
        if (toDispose) dispose();
    }

    /** Overrides default to prevent background clearing. */
    public void repaint() {
        paint(this.getGraphics());
    }

    /** Overrides default paint.
     * @param g graphics object which is used to paint onto
     */
    public void paint(Graphics g) {
        if ((imageReady) && (g != null)) {
            g.drawImage(image, 0, 0, this);
            if (actualMessage != null) {
                g.setColor(Color.black);
                g.drawString(actualMessage, textX, textY);
            }
        }
    }

    /** Loads the given image from a resource path or alternatively as file.
     * @param pictureResourcePath the resource/file path of the image
     */
    public void importImage(String pictureResourcePath) {
        boolean printErrors = true;
        try {
            image = tool.createImage(getClass().getResource(pictureResourcePath));
            imageIsResource = true;
        } catch (NullPointerException nex) {
            if (printErrors) {
                System.out.println("warning: image file " + pictureResourcePath + " not in classpath, loading as file.");
            }
            image = tool.createImage(pictureResourcePath);
            imageIsResource = false;
        }
    }

    /** Called by the AWT when the image loading progresses or the
     * image is ready.
     * @param p1 the image itself
     * @param p2 the flags (see ImageObserver.imageUpdate documentation)
     * @param p3 see ImageObserver.imageUpdate documentation
     * @param p4 see ImageObserver.imageUpdate documentation
     * @param p5 x size of the image
     * @param p6 y size of the image
     * @return true if further update (loading) is needed, false otherwise
     */
    public boolean imageUpdate(Image p1, int p2, int p3, int p4, int p5, int p6) {
        boolean answer = true;
        if ((p2 & ImageObserver.ALLBITS) == ImageObserver.ALLBITS) {
            answer = false;
            imageReady = true;
            sizeX = p5;
            sizeY = p6;
            this.displayImage(p1, p5, p6);
        }
        return answer;
    }

    /** test main program
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        StartupWindow win = new StartupWindow("/dream/resources/icons/console-startup.gif");
        win.setTextPosition(10, 140);
        win.printMessage("Hallo, hier eine Nachricht!");
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException ex) {
            ;
        }
        win.printMessage("Und noch eine!");
        try {
            Thread.currentThread().sleep(6000);
        } catch (InterruptedException ex) {
            ;
        }
        System.exit(0);
    }
}
