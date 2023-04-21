package org.jamiga.intuition.peer;

import org.jamiga.intuition.*;
import org.jamiga.msg.*;
import java.awt.*;
import java.awt.BufferCapabilities.FlipContents;
import java.awt.peer.*;
import java.awt.Rectangle;
import java.awt.peer.ContainerPeer;
import java.awt.Component;
import sun.awt.CausedFocusEvent;
import sun.java2d.pipe.Region;

/**
 *
 * @author  peter
 */
public class IntuiComponentPeer implements ComponentPeer {

    /** Stores the real life related component */
    protected Component component;

    /** Stores the coordinates and size, and stacking level */
    protected int x, y, width, height, z;

    /** Stores a flag, if this component is enabled */
    protected boolean enabled = true;

    /** Stores the foreground color */
    protected Color foreGround = null;

    /** Stores the background color */
    protected Color backGround = null;

    /** Stores the object's visibility */
    protected boolean visible = false;

    protected Rectangle rect = null;

    /**
	 * Creates a new instance of IntuiComponentPeer
	 *
	 * @param component (Component) the "real life" component
	 */
    public IntuiComponentPeer(Component component) {
        System.out.println("ComponentPeer.<init>");
        this.component = component;
        this.rect = component.getBounds();
        if (rect != null) {
            this.x = (int) rect.getX();
            this.y = (int) rect.getY();
        }
        this.width = component.getWidth();
        this.height = component.getHeight();
    }

    public boolean canDetermineObscurity() {
        System.out.println("ComponenetPeer.canDetermineObscurity");
        return false;
    }

    public int checkImage(Image img, int width, int height, java.awt.image.ImageObserver ob) {
        System.out.println("ComponentPeer.checkImage");
        return 0;
    }

    public void coalescePaintEvent(java.awt.event.PaintEvent e) {
        System.out.println("ComponentPeer.coalescePaintEvent");
    }

    public void createBuffers(int x, BufferCapabilities capabilities) throws AWTException {
        System.out.println("ComponentPeer.createBuffers");
    }

    public Image createImage(java.awt.image.ImageProducer prod) {
        System.out.println("ComponentPeer.createImage");
        return null;
    }

    public Image createImage(int width, int height) {
        System.out.println("ComponentPeer.createImage(II)");
        return null;
    }

    public java.awt.image.VolatileImage createVolatileImage(int width, int height) {
        System.out.println("ComponentPeer.createVolatieImage");
        return null;
    }

    public void destroyBuffers() {
        System.out.println("ComponentPeer.destroyBuffers");
    }

    /**
	 * This method disables the component
	 */
    public void disable() {
        System.out.println("ComponentPeer.disable");
        setEnabled(false);
    }

    public void dispose() {
        System.out.println("ComponentPeer.dispose");
    }

    /**
	 * This method enables the component
	 */
    public void enable() {
        System.out.println("ComponentPeer.enable");
        setEnabled(true);
    }

    public void flip(java.awt.BufferCapabilities.FlipContents contents) {
        System.out.println("ComponentPeer.flip");
    }

    public Image getBackBuffer() {
        System.out.println("ComponentPeer.getBackBuffer");
        return null;
    }

    public java.awt.image.ColorModel getColorModel() {
        System.out.println("ComponentPeer.getColorModel");
        return null;
    }

    public FontMetrics getFontMetrics(java.awt.Font f) {
        System.out.println("ComponentPeer.getFontMetrics");
        return null;
    }

    public Graphics getGraphics() {
        System.out.println("ComponentPeer.getGraphics");
        return null;
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        System.out.println("ComponentPeer.getGraphicsConfiguration");
        return null;
    }

    public Point getLocationOnScreen() {
        System.out.println("ComponentPeer.getLocationOnScreen");
        return new Point(x, y);
    }

    /**
	 * Returns the minimum size that this object takes. This defaults
	 * to 1x1 pixels
	 *
	 * @return the minimum size (Dimension)
	 */
    public Dimension getMinimumSize() {
        System.out.println("ComponentPeer.getMinimumSize");
        return new Dimension(1, 1);
    }

    /**
	 * returns the preferred dimension for this object
	 *
	 * @return the preferred dimension (Dimension)
	 */
    public Dimension getPreferredSize() {
        System.out.println("ComponentPeer.getPreferredSize");
        return getMinimumSize();
    }

    /**
	 * Returns the toolkit that generates IntuiPeers
	 *
	 * @return the toolkit (Toolkit)
	 */
    public Toolkit getToolkit() {
        System.out.println("ComponentPeer.getToolkit");
        return IntuiToolkit.baseKit;
    }

    public void handleEvent(AWTEvent e) {
    }

    public boolean handlesWheelScrolling() {
        System.out.println("ComponentPeer.handlesWheelScrolling");
        return false;
    }

    public void hide() {
        System.out.println("ComponentPeer.hide");
    }

    public boolean isFocusTraversable() {
        System.out.println("ComponentPeer.isFocusTraversable");
        return false;
    }

    public boolean isFocusable() {
        System.out.println("ComponentPeer.isFocusable");
        return false;
    }

    public boolean isObscured() {
        System.out.println("ComponentPeer.isObscured");
        return false;
    }

    /**
	 * Returns the minimum size required.
	 * This is a deprecated method, therefore I just call the
	 * "getMinimumSize method here.
	 *
	 * @return the minimum size (Dimension)
	 */
    public Dimension minimumSize() {
        System.out.println("ComponentPeer.minimumSize");
        return getMinimumSize();
    }

    public void paint(Graphics graphics) {
        System.out.println("ComponentPeer.paint");
    }

    /**
	 * Returns the preferred size for this component
	 *
	 * @return the preferred size (Dimension)
	 */
    public Dimension preferredSize() {
        System.out.println("ComponentPeer.preferredSize");
        return getPreferredSize();
    }

    public boolean prepareImage(Image img, int width, int height, java.awt.image.ImageObserver ob) {
        System.out.println("ComponentPeer.prepareImage");
        return false;
    }

    public void print(Graphics graphics) {
        System.out.println("ComponentPeer.print");
    }

    public void repaint(long tm, int x, int y, int width, int height) {
        System.out.println("ComponentPeer.repaint");
    }

    public void requestFocus() {
        System.out.println("ComponentPeer.requestFocus");
    }

    public boolean requestFocus(Component source, boolean bool1, boolean bool2, long x) {
        System.out.println("ComponentPeer.requestFocus(LZZJ)");
        return false;
    }

    /**
	 * Changes the shape/size of the object
	 *
	 * @param x (int) the x coordinate
	 * @param y (int) the y coordinate
	 * @param width (int) the width
	 * @param height (int) the height
	 */
    public void reshape(int x, int y, int width, int height) {
        System.out.println("ComponentPeer.reshape");
        setBounds(x, y, width, height);
    }

    /**
	 * Changes the object's background color
	 *
	 * @param color (Color) the new background color
	 */
    public void setBackground(Color color) {
        this.backGround = color;
    }

    /**
	 * Changes the shape/size of the object
	 *
	 * @param x (int) the x coordinate
	 * @param y (int) the y coordinate
	 * @param width (int) the width
	 * @param height (int) the height
	 */
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setCursor(Cursor cursor) {
        System.out.println("ComponentPeer.setCursor");
    }

    /**
	 * Changes the enabled-status of the object
	 *
	 * @param enabled (boolean) the new status
	 */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEventMask(long mask) {
        System.out.println("ComponentPeer.setEventMask");
    }

    public void setFont(java.awt.Font font) {
        System.out.println("ComponentPeer.setFont");
    }

    /**
	 * Changes the object's foreground color
	 *
	 * @param color (Color) the new foreground color
	 */
    public void setForeground(Color color) {
        this.foreGround = color;
    }

    /**
	 * Changes the visibility setting
	 *
	 * @param visible (boolean)
	 */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
	 * Turns an object into the visible mode
	 */
    public void show() {
        this.setVisible(true);
    }

    public void updateCursorImmediately() {
        System.out.println("ComponentPeer.updateCursorImmediately");
    }

    /**
	 * Returns the underlying component
	 *
	 * @return the component (Component)
	 */
    public Component getComponent() {
        System.out.println("ComponentPeer.getComponent");
        return this.component;
    }

    /**
   * Get the bounds of this component peer.
   *
   * @return component peer bounds
   * @since 1.5
   */
    public Rectangle getBounds() {
        System.out.println("ComponentPeer.getBounds");
        return this.rect;
    }

    /**
   * Reparent this component under another container.
   *
   * @param parent
   * @since 1.5
   */
    public void reparent(ContainerPeer parent) {
        return;
    }

    /**
   * Set the bounds of this component peer.
   *
   * @param x the new x co-ordinate
   * @param y the new y co-ordinate
   * @param width the new width
   * @param height the new height
   * @param z the new stacking level
   * @since 1.5
   */
    public void setBounds(int x, int y, int width, int height, int z) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.z = z;
    }

    /**
   * Check if this component supports being reparented.
   *
   * @return true if this component can be reparented,
   * false otherwise.
   * @since 1.5
   */
    public boolean isReparentSupported() {
        return false;
    }

    /**
   * Layout this component peer.
   *
   * @since 1.5
   */
    public void layout() {
        System.out.println("ComponentPeer.layout");
        return;
    }

    /**
   * Requests the focus on the component.
   */
    public boolean requestFocus(Component lightweightChild, boolean temporary, boolean focusedWindowChangeAllowed, long time, CausedFocusEvent.Cause cause) {
        return false;
    }

    @Override
    public void applyShape(Region arg0) {
    }

    @Override
    public void flip(int arg0, int arg1, int arg2, int arg3, FlipContents arg4) {
    }
}
