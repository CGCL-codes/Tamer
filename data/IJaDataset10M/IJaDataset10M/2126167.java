package org.plazmaforge.studio.reportdesigner.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/** 
 * @author Oleh Hapon
 * $Id: StyleBorder.java,v 1.10 2010/11/12 08:10:36 ohapon Exp $
 */
public class StyleBorder extends LineBorder {

    public static final int NONE_BORDER = 0;

    public static final int CORNERS_BORDER = 1;

    public static final int SOFT_BORDER = 2;

    private static final Color DEFAULT_BORDER_COLOR = ColorConstants.black;

    private static final int DEFAULT_CORNER_WIDTH = 4;

    /**
     * <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>, <code>SWT.LINE_DOT</code>,
     * <code>SWT.LINE_DASHDOT</code> or <code>SWT.LINE_DASHDOTDOT</code>.
     */
    private int topLineStyle;

    private int bottomLineStyle;

    private int leftLineStyle;

    private int rightLineStyle;

    private float topLineWidth;

    private float bottomLineWidth;

    private float leftLineWidth;

    private float rightLineWidth;

    private Color topLineColor;

    private Color bottomLineColor;

    private Color leftLineColor;

    private Color rightLineColor;

    private Insets padding;

    private boolean borderPadding = true;

    /**
     * Type of back border. By default <code>CORNERS_BORDER</code>
     */
    private int backBorderType = CORNERS_BORDER;

    /**
     * Width of corner. Only for corners border
     */
    private int cornerWidth = DEFAULT_CORNER_WIDTH;

    public StyleBorder() {
        super();
    }

    public StyleBorder(Color color, int width) {
        super(color, width);
    }

    public StyleBorder(Color color) {
        super(color);
    }

    public StyleBorder(int width) {
        super(width);
    }

    public static StyleBorder createCellBorder(int width) {
        StyleBorder border = new StyleBorder();
        border.setWidth(width);
        border.setLeftLineStyle(SWT.LINE_SOLID);
        border.setTopLineStyle(SWT.LINE_SOLID);
        border.setRightLineStyle(SWT.LINE_SOLID);
        border.setBottomLineStyle(SWT.LINE_SOLID);
        border.setLeftLineWidth(1);
        border.setTopLineWidth(1);
        border.setRightLineWidth(1);
        border.setBottomLineWidth(1);
        return border;
    }

    public Insets getInsets(IFigure figure) {
        if (borderPadding) {
            int w = getWidth();
            int top = Math.max(w, (int) getTopLineWidth());
            int left = Math.max(w, (int) getLeftLineWidth());
            int bottom = Math.max(w, (int) getBottomLineWidth());
            int right = Math.max(w, (int) getRightLineWidth());
            Insets padding = getPadding();
            top += padding.top;
            left += padding.left;
            bottom += padding.bottom;
            right += padding.right;
            top = top < 0 ? 0 : top;
            left = left < 0 ? 0 : left;
            bottom = bottom < 0 ? 0 : bottom;
            right = right < 0 ? 0 : right;
            return new Insets(top, left, bottom, right);
        }
        return new Insets(getPadding());
    }

    public void paint(IFigure figure, Graphics graphics, Insets insets) {
        if (backBorderType == CORNERS_BORDER) {
            paintCornersBorder(figure, graphics, insets);
        } else if (backBorderType == SOFT_BORDER) {
            paintSoftBorder(figure, graphics, insets);
        }
        final Rectangle rectangle = figure.getBounds().getCropped(insets).crop(new Insets(0, 0, 1, 1));
        int currLineWidth = graphics.getLineWidth();
        int currLineStyle = graphics.getLineStyle();
        Color currLineColor = graphics.getForegroundColor();
        int left = rectangle.x;
        int top = rectangle.y;
        int bottom = rectangle.getBottom().y;
        int right = rectangle.getRight().x;
        float lineWidth = 0;
        int lineDelta = 0;
        int lineStyle = 0;
        if (getTopLineWidth() > 0) {
            lineStyle = getTopLineStyle();
            lineWidth = getTopLineWidth();
            lineDelta = 0;
            graphics.setLineStyle(lineStyle);
            graphics.setLineWidthFloat(lineWidth);
            graphics.setForegroundColor(getTopLineColor());
            graphics.drawLine(left, top + lineDelta, right, top + lineDelta);
        }
        if (getBottomLineWidth() > 0) {
            lineStyle = getBottomLineStyle();
            lineWidth = getBottomLineWidth();
            lineDelta = 0;
            graphics.setLineStyle(lineStyle);
            graphics.setLineWidthFloat(lineWidth);
            graphics.setForegroundColor(getBottomLineColor());
            graphics.drawLine(left, bottom - lineDelta, right, bottom - lineDelta);
        }
        if (getLeftLineWidth() > 0) {
            lineStyle = getLeftLineStyle();
            lineWidth = getLeftLineWidth();
            lineDelta = 0;
            graphics.setLineStyle(lineStyle);
            graphics.setLineWidthFloat(lineWidth);
            graphics.setForegroundColor(getLeftLineColor());
            graphics.drawLine(left + lineDelta, top, left + lineDelta, bottom);
        }
        if (getRightLineWidth() > 0) {
            lineStyle = getRightLineStyle();
            lineWidth = getRightLineWidth();
            lineDelta = 0;
            graphics.setLineStyle(lineStyle);
            graphics.setLineWidthFloat(lineWidth);
            graphics.setForegroundColor(getRightLineColor());
            graphics.drawLine(right - lineDelta, top, right - lineDelta, bottom);
        }
        graphics.setLineWidth(currLineWidth);
        graphics.setLineStyle(currLineStyle);
        graphics.setForegroundColor(currLineColor);
    }

    /**
     * Paint corners border
     * @param figure
     * @param graphics
     * @param insets
     */
    protected void paintCornersBorder(IFigure figure, Graphics graphics, Insets insets) {
        int cornerWidth = getCornerWidth();
        final Rectangle rectangle = figure.getBounds().getCropped(insets).crop(new Insets(0, 0, 1, 1));
        int left = rectangle.x;
        final int top = rectangle.y;
        graphics.drawLine(left, top, left, top + cornerWidth);
        graphics.drawLine(left, top, left + cornerWidth, top);
        final int bottom = rectangle.getBottom().y;
        graphics.drawLine(left, bottom, left, bottom - cornerWidth);
        graphics.drawLine(left, bottom, left + cornerWidth, bottom);
        final int right = rectangle.getRight().x;
        graphics.drawLine(right, top, right, top + cornerWidth);
        graphics.drawLine(right, top, right - cornerWidth, top);
        graphics.drawLine(right, bottom, right, bottom - cornerWidth);
        graphics.drawLine(right, bottom, right - cornerWidth, bottom);
    }

    /**
     * Paint soft border
     * @param figure
     * @param graphics
     * @param insets
     */
    protected void paintSoftBorder(IFigure figure, Graphics graphics, Insets insets) {
        super.paint(figure, graphics, insets);
    }

    public int getBottomLineStyle() {
        return bottomLineStyle;
    }

    public void setBottomLineStyle(int bottomLineStyle) {
        this.bottomLineStyle = bottomLineStyle;
    }

    public int getLeftLineStyle() {
        return leftLineStyle;
    }

    public void setLeftLineStyle(int leftLineStyle) {
        this.leftLineStyle = leftLineStyle;
    }

    public int getRightLineStyle() {
        return rightLineStyle;
    }

    public void setRightLineStyle(int rightLineStyle) {
        this.rightLineStyle = rightLineStyle;
    }

    public int getTopLineStyle() {
        return topLineStyle;
    }

    public void setTopLineStyle(int topLineStyle) {
        this.topLineStyle = topLineStyle;
    }

    public float getBottomLineWidth() {
        return bottomLineWidth;
    }

    public void setBottomLineWidth(float bottomLineWidth) {
        this.bottomLineWidth = bottomLineWidth;
    }

    public float getLeftLineWidth() {
        return leftLineWidth;
    }

    public void setLeftLineWidth(float leftLineWidth) {
        this.leftLineWidth = leftLineWidth;
    }

    public float getRightLineWidth() {
        return rightLineWidth;
    }

    public void setRightLineWidth(float rightLineWidth) {
        this.rightLineWidth = rightLineWidth;
    }

    public float getTopLineWidth() {
        return topLineWidth;
    }

    public void setTopLineWidth(float topLineWidth) {
        this.topLineWidth = topLineWidth;
    }

    public Color getBottomLineColor() {
        if (bottomLineColor == null) {
            bottomLineColor = DEFAULT_BORDER_COLOR;
        }
        return bottomLineColor;
    }

    public void setBottomLineColor(Color bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    public Color getLeftLineColor() {
        if (leftLineColor == null) {
            leftLineColor = DEFAULT_BORDER_COLOR;
        }
        return leftLineColor;
    }

    public void setLeftLineColor(Color leftLineColor) {
        this.leftLineColor = leftLineColor;
    }

    public Color getRightLineColor() {
        if (rightLineColor == null) {
            rightLineColor = DEFAULT_BORDER_COLOR;
        }
        return rightLineColor;
    }

    public void setRightLineColor(Color rightLineColor) {
        this.rightLineColor = rightLineColor;
    }

    public Color getTopLineColor() {
        if (topLineColor == null) {
            topLineColor = DEFAULT_BORDER_COLOR;
        }
        return topLineColor;
    }

    public void setTopLineColor(Color topLineColor) {
        this.topLineColor = topLineColor;
    }

    public Insets getPadding() {
        if (padding == null) {
            padding = new Insets();
        }
        return padding;
    }

    public void setPadding(Insets padding) {
        this.padding = padding;
    }

    public int getBackBorderType() {
        return backBorderType;
    }

    public void setBackBorderType(int backBorderType) {
        this.backBorderType = backBorderType;
    }

    public int getCornerWidth() {
        if (cornerWidth < 1) {
            return 1;
        }
        return cornerWidth;
    }

    public void setCornerWidth(int cornerWidth) {
        this.cornerWidth = cornerWidth;
    }
}
