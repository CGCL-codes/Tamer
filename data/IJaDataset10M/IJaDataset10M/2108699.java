package gnu.java.awt.peer;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.PaintEvent;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.ContainerPeer;
import java.awt.peer.LightweightPeer;

/**
 * A stub class that implements the ComponentPeer and ContainerPeer
 * interfaces using callbacks into the Component and Container
 * classes.  GLightweightPeer allows the Component and Container
 * classes to treat lightweight and heavyweight peers in the same way.
 *
 * Lightweight components are painted directly onto their parent
 * containers through an Image object provided by the toolkit.
 */
public class GLightweightPeer implements LightweightPeer, ContainerPeer {

    private Component comp;

    private Insets containerInsets;

    public GLightweightPeer(Component comp) {
        this.comp = comp;
    }

    public Insets insets() {
        return getInsets();
    }

    public Insets getInsets() {
        if (containerInsets == null) containerInsets = new Insets(0, 0, 0, 0);
        return containerInsets;
    }

    public void beginValidate() {
    }

    public void endValidate() {
    }

    public void beginLayout() {
    }

    public void endLayout() {
    }

    public boolean isPaintPending() {
        return false;
    }

    public int checkImage(Image img, int width, int height, ImageObserver o) {
        return comp.getToolkit().checkImage(img, width, height, o);
    }

    public Image createImage(ImageProducer prod) {
        return comp.getToolkit().createImage(prod);
    }

    public Image createImage(int width, int height) {
        return null;
    }

    public void disable() {
    }

    public void dispose() {
    }

    public void enable() {
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return null;
    }

    public FontMetrics getFontMetrics(Font f) {
        return comp.getToolkit().getFontMetrics(f);
    }

    public Graphics getGraphics() {
        return null;
    }

    public Point getLocationOnScreen() {
        Point parentLocation = comp.getParent().getLocationOnScreen();
        return new Point(parentLocation.x + comp.getX(), parentLocation.y + comp.getY());
    }

    public Dimension getMinimumSize() {
        return new Dimension(comp.getWidth(), comp.getHeight());
    }

    public Dimension getPreferredSize() {
        return new Dimension(comp.getWidth(), comp.getHeight());
    }

    public Toolkit getToolkit() {
        return null;
    }

    public void handleEvent(AWTEvent e) {
    }

    public void hide() {
    }

    public boolean isFocusable() {
        return false;
    }

    public boolean isFocusTraversable() {
        return false;
    }

    public Dimension minimumSize() {
        return getMinimumSize();
    }

    public Dimension preferredSize() {
        return getPreferredSize();
    }

    public void paint(Graphics graphics) {
    }

    public boolean prepareImage(Image img, int width, int height, ImageObserver o) {
        return comp.getToolkit().prepareImage(img, width, height, o);
    }

    public void print(Graphics graphics) {
    }

    public void repaint(long tm, int x, int y, int width, int height) {
    }

    public void requestFocus() {
    }

    public boolean requestFocus(Component source, boolean bool1, boolean bool2, long x) {
        return false;
    }

    public void reshape(int x, int y, int width, int height) {
    }

    public void setBackground(Color color) {
    }

    public void setBounds(int x, int y, int width, int height) {
    }

    public void setCursor(Cursor cursor) {
    }

    public void setEnabled(boolean enabled) {
    }

    public void setEventMask(long eventMask) {
    }

    public void setFont(Font font) {
    }

    public void setForeground(Color color) {
    }

    public void setVisible(boolean visible) {
    }

    public void show() {
    }

    public ColorModel getColorModel() {
        return comp.getColorModel();
    }

    public boolean isObscured() {
        return false;
    }

    public boolean canDetermineObscurity() {
        return false;
    }

    public void coalescePaintEvent(PaintEvent e) {
    }

    public void updateCursorImmediately() {
    }

    public VolatileImage createVolatileImage(int width, int height) {
        return null;
    }

    public boolean handlesWheelScrolling() {
        return false;
    }

    public void createBuffers(int x, BufferCapabilities capabilities) throws AWTException {
    }

    public Image getBackBuffer() {
        return null;
    }

    public void flip(BufferCapabilities.FlipContents contents) {
    }

    public void destroyBuffers() {
    }

    public boolean isRestackSupported() {
        return false;
    }

    public void cancelPendingPaint(int x, int y, int width, int height) {
    }

    public void restack() {
    }

    public Rectangle getBounds() {
        return null;
    }

    public void reparent(ContainerPeer parent) {
    }

    public void setBounds(int x, int y, int z, int width, int height) {
    }

    public boolean isReparentSupported() {
        return false;
    }

    public void layout() {
    }
}
