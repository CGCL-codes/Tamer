package org.fenggui.decorator.border;

import java.io.IOException;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.decorator.IDecorator;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Spacing;
import org.fenggui.util.Span;

/**
 * Solid line border type. The border can have arbitrary line
 * width (bottom, top, left, right) but is drawn in only one color.  
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 *
 */
public class PlainBorder extends Border {

    private Color color = Color.BLUE;

    /**
   * Creates a Simple Border.
   * @param c
   *          The color that in whicht the line shall appear
   * @param lineWidth
   *          Initiates all line widths with this thickness
   */
    public PlainBorder(Color c, int lineWidth) {
        this(lineWidth, lineWidth, lineWidth, lineWidth, c, true, Span.BORDER);
    }

    /**
   * Creates a Simple Border. The default color is blue. The default
   * line width is 1.
   *
   */
    public PlainBorder() {
        setSpacing(1, 1, 1, 1);
    }

    public PlainBorder(InputOnlyStream stream) throws IOException, IXMLStreamableException {
        process(stream);
    }

    public PlainBorder(int top, int left, int right, int bottom) {
        this(top, left, right, bottom, Color.BLACK, true, Span.BORDER);
    }

    public PlainBorder(int top, int left, int right, int bottom, Color color, boolean enabled, Span span) {
        setSpacing(top, left, right, bottom);
        this.color = color;
        setEnabled(enabled);
        setSpan(span);
    }

    public PlainBorder(Spacing s) {
        setSpacing(s);
    }

    public PlainBorder(Spacing s, Color c) {
        color = c;
        setSpacing(s);
    }

    /**
   * Creates a Simple Border. The default border width is 1.
   * @param c
   *          The color of the border
   */
    public PlainBorder(Color c) {
        this(1, 1, 1, 1, c, true, Span.BORDER);
    }

    public PlainBorder(Color c, boolean enabled) {
        this(1, 1, 1, 1, c, enabled, Span.BORDER);
    }

    public PlainBorder(Color c, Span span) {
        this(1, 1, 1, 1, c, true, span);
    }

    /**
   * Returns the line color.
   * @return the line color
   * 
   */
    public Color getColor() {
        return color;
    }

    /**
   * Sets the line color.
   * @param color line color
   */
    public void setColor(Color color) {
        this.color = color;
    }

    private void changeLineWidth(IOpenGL gl, int width) {
        gl.end();
        gl.lineWidth(width);
        gl.startLines();
    }

    @Override
    public void paint(Graphics g, int localX, int localY, int width, int height) {
        IOpenGL gl = g.getOpenGL();
        g.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        int globalX = localX + g.getTranslation().getX();
        int globalY = localY + g.getTranslation().getY();
        if (getLeft() != 1 && getLeft() > 0) gl.lineWidth(getLeft());
        gl.startLines();
        if (getLeft() > 0) {
            gl.vertex(globalX + getLeft() / 2, globalY + getBottom());
            gl.vertex(globalX + getLeft() / 2, globalY + height - getTop());
        }
        if (getBottom() > 0) {
            if (getBottom() != getLeft()) changeLineWidth(gl, getBottom());
            gl.vertex(globalX, globalY + getBottom() / 2);
            gl.vertex(globalX + width, globalY + getBottom() / 2);
        }
        if (getRight() > 0) {
            if (getRight() != getBottom()) changeLineWidth(gl, getRight());
            gl.vertex(globalX + width - (getRight() + 1) / 2, globalY + getBottom());
            gl.vertex(globalX + width - (getRight() + 1) / 2, globalY + height - getTop());
        }
        if (getTop() > 0) {
            if (getTop() != getRight()) changeLineWidth(gl, getTop());
            gl.vertex(globalX, globalY + height - (getTop() + 1) / 2);
            gl.vertex(globalX + width, globalY + height - (getTop() + 1) / 2);
        }
        gl.end();
        gl.lineWidth(1);
    }

    @Override
    public void process(InputOutputStream stream) throws IOException, IXMLStreamableException {
        super.process(stream);
        if (getTop() == 0 && getLeft() == 0 && getBottom() == 0 && getRight() == 0) {
            setSpacing(1, 1);
        }
        color = stream.processChild("Color", color, Color.BLACK, Color.class);
    }

    @Override
    public IDecorator copy() {
        PlainBorder result = new PlainBorder(this, color);
        super.copy(result);
        return result;
    }
}
