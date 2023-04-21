package org.jmol.awt;

import javax.vecmath.Point3f;
import netscape.javascript.JSObject;
import org.jmol.api.ApiPlatform;
import org.jmol.api.Interface;
import org.jmol.api.JmolPopupInterface;
import org.jmol.api.JmolViewer;
import org.jmol.g3d.Font3D;
import org.jmol.viewer.ActionManager;
import org.jmol.viewer.Viewer;

public class Platform implements ApiPlatform {

    private Mouse mouse;

    public void setViewer(JmolViewer viewer, Object display) {
    }

    public void convertPointFromScreen(Object display, Point3f ptTemp) {
        Display.convertPointFromScreen(display, ptTemp);
    }

    public void getFullScreenDimensions(Object display, int[] widthHeight) {
        Display.getFullScreenDimensions(display, widthHeight);
    }

    public JmolPopupInterface getMenuPopup(Viewer viewer, String menuStructure, char type) {
        JmolPopupInterface jmolpopup = (JmolPopupInterface) Interface.getOptionInterface(type == 'j' ? "popup.JmolPopup" : "modelkit.ModelKitPopup");
        if (jmolpopup != null) jmolpopup.initialize(viewer, menuStructure);
        return jmolpopup;
    }

    public boolean hasFocus(Object display) {
        return Display.hasFocus(display);
    }

    public String prompt(String label, String data, String[] list, boolean asButtons) {
        return Display.prompt(label, data, list, asButtons);
    }

    /**
   * legacy apps will use this
   * 
   * @param viewer
   * @param g
   * @param size
   */
    public void renderScreenImage(JmolViewer viewer, Object g, Object size) {
        Display.renderScreenImage(viewer, g, size);
    }

    public void requestFocusInWindow(Object display) {
        Display.requestFocusInWindow(display);
    }

    public void repaint(Object display) {
        Display.repaint(display);
    }

    public void setTransparentCursor(Object display) {
        Display.setTransparentCursor(display);
    }

    public void setCursor(int c, Object display) {
        Display.setCursor(c, display);
    }

    public void getMouseManager(Viewer viewer, ActionManager actionManager) {
        mouse = new Mouse(viewer, actionManager);
    }

    public boolean handleOldJvm10Event(int id, int x, int y, int modifiers, long time) {
        return mouse.handleOldJvm10Event(id, x, y, modifiers, time);
    }

    public void clearMouse() {
        mouse.clear();
    }

    public void disposeMouse() {
        mouse.dispose();
        mouse = null;
    }

    public Object allocateRgbImage(int windowWidth, int windowHeight, int[] pBuffer, int windowSize, boolean backgroundTransparent) {
        return Image.allocateRgbImage(windowWidth, windowHeight, pBuffer, windowSize, backgroundTransparent);
    }

    public Object createImage(Object data) {
        return Image.createImage(data);
    }

    public void disposeGraphics(Object gOffscreen) {
        Image.disposeGraphics(gOffscreen);
    }

    public void drawImage(Object g, Object img, int x, int y, int width, int height) {
        Image.drawImage(g, img, x, y, width, height);
    }

    public int[] grabPixels(Object imageobj, int width, int height) {
        return Image.grabPixels(imageobj, width, height);
    }

    public int[] drawImageToBuffer(Object gOffscreen, Object imageOffscreen, Object imageobj, int width, int height, int bgcolor) {
        return Image.drawImageToBuffer(gOffscreen, imageOffscreen, imageobj, width, height, bgcolor);
    }

    public int[] getTextPixels(String text, Font3D font3d, Object gObj, Object image, int width, int height, int ascent) {
        return Image.getTextPixels(text, font3d, gObj, image, width, height, ascent);
    }

    public void flushImage(Object imagePixelBuffer) {
        Image.flush(imagePixelBuffer);
    }

    public Object getGraphics(Object image) {
        return Image.getGraphics(image);
    }

    public int getImageHeight(Object image) {
        return Image.getHeight(image);
    }

    public int getImageWidth(Object image) {
        return Image.getWidth(image);
    }

    public Object getJpgImage(Viewer viewer, int quality, String comment) {
        return Image.getJpgImage(this, viewer, quality, comment);
    }

    public Object getStaticGraphics(Object image, boolean backgroundTransparent) {
        return Image.getStaticGraphics(image, backgroundTransparent);
    }

    public Object newBufferedImage(Object image, int w, int h) {
        return Image.newBufferedImage(image, w, h);
    }

    public Object newBufferedRgbImage(int w, int h) {
        return Image.newBufferedImage(w, h);
    }

    public boolean waitForDisplay(Object display, Object image) throws InterruptedException {
        Image.waitForDisplay(display, image);
        return true;
    }

    public int fontStringWidth(Object fontMetrics, String text) {
        return Font.stringWidth(fontMetrics, text);
    }

    public int getFontAscent(Object fontMetrics) {
        return Font.getAscent(fontMetrics);
    }

    public int getFontDescent(Object fontMetrics) {
        return Font.getDescent(fontMetrics);
    }

    public Object getFontMetrics(Object graphics, Object font) {
        return Font.getFontMetrics(graphics, font);
    }

    public Object newFont(String fontFace, boolean isBold, boolean isItalic, float fontSize) {
        return Font.newFont(fontFace, isBold, isItalic, fontSize);
    }

    public Object getJsObjectInfo(Object jsObject, String method, Object[] args) {
        JSObject DOMNode = (JSObject) jsObject;
        if (method == null) {
            String namespaceURI = (String) DOMNode.getMember("namespaceURI");
            String localName = (String) DOMNode.getMember("localName");
            return "namespaceURI=\"" + namespaceURI + "\" localName=\"" + localName + "\"";
        }
        return (args == null ? DOMNode.getMember(method) : DOMNode.call(method, args));
    }
}
