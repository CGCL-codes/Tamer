package com.iv.flash.fop;

import com.iv.flash.api.*;
import com.iv.flash.api.action.Program;
import com.iv.flash.api.button.*;
import com.iv.flash.api.shape.*;
import com.iv.flash.api.text.*;
import com.iv.flash.util.GeomHelper;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * This is a helper class for the SWFRenderer, this does all
 * JGen specific calls, like drawing lines etc etc.
 *
 * @author Johan "Spocke" S�rlin
 * @author James Taylor
 */
public class FOPScriptBuilder {

    private Script script;

    private Frame currentFrame;

    private String linkHandler = null;

    private Font currentFont;

    private int layerCount = 2;

    private int pageWidth = 0;

    private int pageHeight = 0;

    private int maxPageWidth = 0;

    private int maxPageHeight = 0;

    /**
     * Constructs a new FOPScriptBuilder
     *
     */
    public FOPScriptBuilder() {
        script = new Script(0);
    }

    /**
     * Sets the name of the function used to handle links. This should be a
     * function defined on the timeline containing this script.
     *
     * FIXME: Need to evaluate other ways to do this, to allow the most
     *        flexibility
     */
    public void setLinkHandler(String methodName) {
        linkHandler = methodName;
    }

    public void startPage(int width, int height) {
        pageWidth = millipointsToTwixels(width);
        pageHeight = millipointsToTwixels(height);
        maxPageWidth = (pageWidth > maxPageWidth) ? pageWidth : maxPageWidth;
        maxPageHeight = (pageHeight > maxPageHeight) ? pageHeight : maxPageHeight;
        currentFrame = script.newFrame();
        script.removeAllInstances(currentFrame);
    }

    public Script getScript() {
        return script;
    }

    public Rectangle2D getMaxPageBounds() {
        return GeomHelper.newRectangle(0, 0, maxPageWidth, maxPageHeight);
    }

    /**
     * Adds a new line shape to the FlashMovie.
     *
     * @param x1 x1 position in FOP points
     * @param y1 y1 position in FOP points
     * @param x2 x2 position in FOP points
     * @param y1 y1 position in FOP points
     * @param th thickness in FOP points
     * @param r red color channel
     * @param g green color channel
     * @param b blue color channel
     */
    public void addLine(int x1, int y1, int x2, int y2, int th, float r, float g, float b) {
        x1 = millipointsToTwixels(x1);
        y1 = millipointsToTwixels(y1);
        x2 = millipointsToTwixels(x2);
        y2 = millipointsToTwixels(y2);
        th = millipointsToTwixels(th);
        y1 = pageHeight - y1;
        y2 = pageHeight - y2;
        Shape shape = new Shape();
        shape.setLineStyle(new LineStyle(th, new AlphaColor(r, g, b)));
        shape.drawLine(x1, y2, x2, y2);
        shape.setBounds(x1, y1, x2 - x1, y2 - y1);
        currentFrame.addInstance(shape, layerCount++, new AffineTransform(), null);
    }

    /**
     * Adds a new filled rectangle shape to the FlashMovie.
     *
     * @param x x position in FOP points
     * @param y y position in FOP points
     * @param w width in FOP points
     * @param h height in FOP points
     * @param r red color channel
     * @param g green color channel
     * @param b blue color channel
     */
    public void addRect(int x, int y, int w, int h, float r, float g, float b) {
        x = millipointsToTwixels(x);
        y = millipointsToTwixels(y);
        w = millipointsToTwixels(w);
        h = millipointsToTwixels(h);
        h = -h;
        y = pageHeight - y;
        Shape shape = new Shape();
        shape.setFillStyle0(FillStyle.newSolid(new AlphaColor(r, g, b)));
        Rectangle2D movieRect = GeomHelper.newRectangle(x, y, w, h);
        shape.drawRectangle(movieRect);
        shape.setBounds(movieRect);
        currentFrame.addInstance(shape, layerCount++, new AffineTransform(), null);
    }

    public void addLink(int x, int y, int w, int h, String destination) {
        x = millipointsToTwixels(x);
        y = millipointsToTwixels(y);
        w = millipointsToTwixels(w);
        h = millipointsToTwixels(h);
        y = pageHeight - y;
        Button2 button = new Button2();
        Shape shape = new Shape();
        shape.setFillStyle0(FillStyle.newSolid(new AlphaColor(0x31, 0x63, 0x9c, 0x60)));
        Rectangle2D r = GeomHelper.newRectangle(0, 0, w, h);
        shape.drawRectangle(r);
        shape.setBounds(r);
        AffineTransform shapeMatrix = new AffineTransform();
        ButtonRecord hitState = new ButtonRecord(ButtonRecord.HitTest, shape, 1, shapeMatrix, CXForm.newIdentity(true));
        button.addButtonRecord(hitState);
        Program p = new Program();
        if (linkHandler != null) {
            p.push(destination);
            p.push(1);
            p.push("_parent");
            p.eval();
            p.push(linkHandler);
            p.callMethod();
        } else {
            p.getURL(destination, "_blank");
        }
        ActionCondition onRelease = new ActionCondition(ActionCondition.OverDownToOverUp, p);
        button.addActionCondition(onRelease);
        AffineTransform instMatrix = AffineTransform.getTranslateInstance(x, y);
        currentFrame.addInstance(button, layerCount++, instMatrix, null);
    }

    /**
     * Adds a new text element to the FlashMovie.
     *
     * @param string String to place in text element
     * @param pos_x x position in FOP points
     * @param pos_y y position in FOP points
     * @param r red color channel
     * @param g green color channel
     * @param b blue color channel
     * @param fontMetric font metric
     * @param size font size in FOP points
     * @param underlined underlined text element
     */
    public void addText(String string, int x, int y, float r, float g, float b, SWFFontMetric fontMetric, int size, int width) {
        x = millipointsToTwixels(x);
        y = millipointsToTwixels(y);
        size = millipointsToTwixels(size);
        width = millipointsToTwixels(width);
        y = pageHeight - y;
        currentFont = fontMetric.getFont();
        y += (currentFont.descent - currentFont.leading) / 5;
        Text text = Text.newText();
        TextItem item = new TextItem(string, currentFont, size, new AlphaColor(r, g, b));
        text.addTextItem(item);
        text.setBounds(0, 0, width + 10, size);
        AffineTransform position = AffineTransform.getTranslateInstance(x, y - size);
        currentFrame.addInstance(text, layerCount++, position, null);
    }

    /**
     * Convert millipoints ( unit of formatting used by most parts of FOP ) to
     * twixels ( unit used by flash ).
     *
     * 1000 points = 1 pixel = 20 twixels, thus 1 point = .02 twixels
     */
    private static int millipointsToTwixels(int points) {
        return Math.round(points * 0.02f);
    }
}
