package net.ar.webonswing.toolkit.peers;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.peer.*;
import net.ar.webonswing.toolkit.*;

public class WosComponentPeer implements ComponentPeer {

    public void destroyBuffers() {
    }

    public void disable() {
    }

    public void dispose() {
    }

    public void enable() {
    }

    public void hide() {
    }

    public void show() {
    }

    public void updateCursorImmediately() {
    }

    public boolean canDetermineObscurity() {
        return false;
    }

    public boolean handlesWheelScrolling() {
        return false;
    }

    public boolean isFocusable() {
        return false;
    }

    public boolean isObscured() {
        return false;
    }

    public void reshape(int x, int y, int width, int height) {
    }

    public void setBounds(int x, int y, int width, int height) {
    }

    public void repaint(long tm, int x, int y, int width, int height) {
    }

    public void setEnabled(boolean b) {
    }

    public void setVisible(boolean b) {
    }

    public void handleEvent(AWTEvent e) {
    }

    public void createBuffers(int numBuffers, java.awt.BufferCapabilities caps) throws AWTException {
    }

    public void flip(java.awt.BufferCapabilities.FlipContents flipAction) {
    }

    public void setBackground(Color c) {
    }

    public void setForeground(Color c) {
    }

    public boolean requestFocus(Component lightweightChild, boolean temporary, boolean focusedWindowChangeAllowed, long time) {
        return false;
    }

    public Dimension getMinimumSize() {
        return null;
    }

    public Dimension getPreferredSize() {
        return null;
    }

    public Dimension minimumSize() {
        return null;
    }

    public Dimension preferredSize() {
        return null;
    }

    public void setFont(Font f) {
    }

    public Graphics getGraphics() {
        return null;
    }

    public void paint(Graphics g) {
    }

    public void print(Graphics g) {
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return null;
    }

    public Image getBackBuffer() {
        return null;
    }

    public Image createImage(int width, int height) {
        return null;
    }

    public Point getLocationOnScreen() {
        return null;
    }

    public Toolkit getToolkit() {
        return new WosToolkit();
    }

    public void coalescePaintEvent(PaintEvent e) {
    }

    public ColorModel getColorModel() {
        return null;
    }

    public VolatileImage createVolatileImage(int width, int height) {
        return null;
    }

    public FontMetrics getFontMetrics(Font font) {
        return null;
    }

    public Image createImage(ImageProducer producer) {
        return null;
    }

    public int checkImage(Image img, int w, int h, ImageObserver o) {
        return 0;
    }

    public boolean prepareImage(Image img, int w, int h, ImageObserver o) {
        return false;
    }

    public void setCursor(Cursor cursor) {
    }

    public void requestFocus() {
    }

    public boolean isFocusTraversable() {
        return false;
    }

    public void setBounds(int x, int y, int width, int height, int op) {
    }

    public void reparent(ContainerPeer newContainer) {
    }

    public boolean isReparentSupported() {
        return false;
    }

    public void layout() {
    }

    public Rectangle getBounds() {
        return new Rectangle(0, 0, 640, 480);
    }
}
