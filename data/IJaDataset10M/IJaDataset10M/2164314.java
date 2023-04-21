package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.common.Convertors;
import com.volantis.mcs.eclipse.common.NamedColor;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * A StyledGroup emulates the SWT Group (org.eclipse.swt.widgets.Group). The
 * differences are that a StyledGroup has a controllable border width and
 * color.
 *
 * In SWT both focus and selection are delegated to or derived from the
 * native widgets. Since this is an emulation both focus and selection need
 * to be emulated also rather than delegated to the super class.
 */
public class StyledGroup extends Composite {

    /**
     * The resource prefix for this widget.
     */
    private static final String RESOURCE_PREFIX = "StyledGroup.";

    /**
     * The horizontal inset in pixels of the child from the StyledGroup's
     * border.
     */
    private static final int MARGIN = ControlsMessages.getInteger(RESOURCE_PREFIX + "margin").intValue();

    /**
     * The text indentation. This is the gap between the border line and the
     * start of the text.
     */
    private static final int TEXT_INSET = ControlsMessages.getInteger(RESOURCE_PREFIX + "textInset").intValue();

    /**
     * The text indentation. This is the indent from the left side of the
     * border to the start of the text. The border line width will be added
     * to this number to provide the point at which the border line stops
     * to make way for the text.
     */
    private static final int TEXT_INDENT = ControlsMessages.getInteger(RESOURCE_PREFIX + "textIndent").intValue();

    /**
     * Constant for a String extent of zero.
     */
    private static final Point ZERO_POINT = new Point(0, 0);

    /**
     * Constant representing the minumum height of a StyledGroup.
     */
    private static final int MIN_HEIGHT = 15;

    /**
     * The width of the part of the border that is the line.
     * {@link #setBorderLineWidth}.
     */
    private int borderLineWidth = 1;

    /**
     * The actual border width. This is the max of the borderLineWidth and
     * the height of the text (if any) associated with this StyledGroup.
     */
    private int borderWidth = 1;

    /**
     * A ColorRegistry for managing colours created by StyledGroups.
     */
    private static final ColorRegistry COLOR_REGISTRY = new ColorRegistry();

    static {
        COLOR_REGISTRY.put(NamedColor.BLACK.getName(), new RGB(0, 0, 0));
    }

    /**
     * The border color.
     */
    private Color borderColor = COLOR_REGISTRY.get(NamedColor.BLACK.getName());

    /**
     * The text color. Users cannot set this. Instead, the text is set to
     * a contrasting color whenever the StyledGroup's background color is set.
     */
    private Color textColor = borderColor;

    /**
     * The text for the StyledGroup.
     */
    private String text;

    /**
     * Creates a StyledGroup widget.
     * @param parent the parent Composite
     * @param style the StyledGroup's style (scroll bar styles are ignored)
     */
    public StyledGroup(Composite parent, int style) {
        super(parent, checkStyle(style));
        addBorderTextPainter();
        initAccessible();
    }

    /**
     * Removes any scroll bar styles from the style. This was copied from the
     * SWT Group.
     * @param style the style to check
     * @return the new style
     */
    private static int checkStyle(int style) {
        return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
    }

    /**
     * This computes the size of the StyledGroup by starting from its child
     * and growing outwards.
     */
    public Point computeSize(int wHint, int hHint, boolean changed) {
        Point size = super.computeSize(wHint, hHint, changed);
        size.x += (borderWidth + MARGIN) * 2;
        size.y += MIN_HEIGHT;
        return size;
    }

    /**
     * Provide the Rectangle that describes the space available to child
     * controls of this StyledGroup relative to this StyledGroup.
     * @return the Rectangle describing the area available to clients
     */
    public Rectangle getClientArea() {
        updateBorderWidth();
        Rectangle area = super.getClientArea();
        int margin = (borderWidth + MARGIN);
        area.x += margin;
        area.y += margin;
        area.width -= margin * 2;
        area.height -= margin * 2;
        return area;
    }

    /**
     * Computes the extent of a String. If text is an empty
     * String or is null the returned Point has 0 for both x and y.
     * @param s the String
     * @param gc the GC (graphics context) used to obtain the text extent.
     * @return the text's extent
     */
    private Point getTextExtent(String s, GC gc) {
        Point extent = ZERO_POINT;
        if (s != null && s.length() > 0) {
            extent = gc.textExtent(s);
            extent.y -= 4;
        }
        return extent;
    }

    /**
     * Creates and adds a PaintListener to draw the StyledGroup's border and
     * text. Note that no Label widgets are used for the text: the text is
     * rendered directly.
     *
     * This method is called on construction of the StyledGroup.
     */
    private void addBorderTextPainter() {
        this.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent paintEvent) {
                repaint(paintEvent.gc);
            }
        });
    }

    /**
     * Paint the border and border text of the StyledGroup using a GC.
     *
     * Sub-classes should override this method to do different or additional
     * painting.
     *
     * This method is called by the PaintListener that is attacted to this
     * StyledGroup. This PaintListener is called any time the StyleGroup is
     * redrawn (e.g. with the redraw() method).
     *
     * @param gc the GC in which to do the repaint
     */
    protected void repaint(GC gc) {
        gc.setForeground(borderColor);
        gc.setLineWidth(borderLineWidth);
        Point textExtent = getTextExtent(text, gc);
        int lineInset = (borderWidth - borderLineWidth / 2);
        int insetAdjust = borderLineWidth % 2 == 0 ? 1 : 0;
        Rectangle borderRectangle = new Rectangle(lineInset - insetAdjust, lineInset - insetAdjust, getSize().x - lineInset * 2 + insetAdjust, getSize().y - lineInset * 2 + insetAdjust);
        gc.drawRectangle(borderRectangle);
        if (text != null && text.length() > 0) {
            int textX = borderWidth + TEXT_INDENT;
            int textHeight = getTextHeight();
            int textY = (Math.abs(textHeight - borderWidth) / 2) - (textHeight / 3) + 3;
            gc.setForeground(textColor);
            gc.fillRectangle(textX, 0, textExtent.x + 2 * TEXT_INSET, borderWidth + 3);
            gc.drawText(text, textX + TEXT_INSET, textY);
        }
    }

    /**
     * Override setFont() to update the size of the border.
     * @param font the Font.
     */
    public void setFont(Font font) {
        super.setFont(font);
        updateBorderWidth();
        redraw();
        layout(true);
    }

    /**
     * Sets the border color and immediately updates the display.
     * @param color the s border color
     */
    public void setBorderColor(Color color) {
        borderColor = color;
        redraw();
    }

    /**
     * Set the width of the line part of the border of this StyledGroup. The
     * minimum width is 0 which will be drawn as 1 pixel line - i.e. there
     * cannot be no border. The border line that is drawn will always be
     * borderLineWidth + 1 pixels. A borderLineWidth that is < 0 will be
     * treated as 0.
     * @param borderLineWidth the required width of the line part of the border.
     */
    public void setBorderLineWidth(int borderLineWidth) {
        borderLineWidth = borderLineWidth <= 0 ? 1 : borderLineWidth + 1;
        this.borderLineWidth = borderLineWidth;
        updateBorderWidth();
        redraw();
        super.layout(true);
    }

    /**
     * Get the border line width of this StyledGroup.
     */
    public int getBorderLineWidth() {
        return borderLineWidth;
    }

    /**
     * Sets the text of this StyledGroup and immediately updates the display.
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
        updateBorderWidth();
        redraw();
        super.layout(true);
    }

    /**
     * Get the text of this Styled group.
     * @return text the text
     */
    public String getText() {
        return text;
    }

    /**
     * Update the borderWidth variable.
     */
    private void updateBorderWidth() {
        borderWidth = Math.max(borderLineWidth, getTextHeight());
    }

    /**
     * Get the height of the text based on the font data for this StyledGroup.
     * @return the height of the text which will be 0 if text is null or has
     * a length of 0.
     */
    private int getTextHeight() {
        int fontHeight = text != null && text.length() > 0 ? getFont().getFontData()[0].getHeight() : 0;
        return fontHeight;
    }

    /**
     * Overridden to set the background color and then to set the color of the
     * StyledGroup's text to be a contrasting color.
     * @param bgColor
     */
    public void setBackground(Color bgColor) {
        textColor = Convertors.getContrastingColor(bgColor);
        super.setBackground(bgColor);
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        StandardAccessibleListener al = new StandardAccessibleListener() {

            public void getName(AccessibleEvent ae) {
                ae.result = getText();
            }
        };
        al.setControl(this);
        getAccessible().addAccessibleListener(al);
    }
}
