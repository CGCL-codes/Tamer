package com.g2d;

import java.awt.image.BufferedImage;
import java.text.AttributedString;
import com.cell.gfx.IGraphics;
import com.cell.gfx.IImage;
import com.g2d.geom.Ellipse2D;
import com.g2d.geom.Line2D;
import com.g2d.geom.Path2D;
import com.g2d.geom.Polygon;
import com.g2d.geom.Rectangle;
import com.g2d.geom.Rectangle2D;
import com.g2d.geom.Shape;

public abstract class Graphics2D implements IGraphics {

    @Override
    public final void drawString(String str, int x, int y, int shandowColor, int shandowX, int shandowY) {
        Color c = getColor();
        drawString(str, x, y);
        setColor(new Color(shandowColor));
        drawString(str, x + shandowX, y + shandowY);
        setColor(c);
    }

    @Override
    public final void fillRectAlpha(int argb, int x, int y, int width, int height) {
        Color c = getColor();
        setColor(new Color(argb));
        fillRect(x, y, width, height);
        setColor(c);
    }

    @Override
    public final void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        fillPolygon(new int[] { x1, x2, x3 }, new int[] { y1, y2, y3 }, 3);
    }

    @Override
    public final String getFontName() {
        return getFont().getName();
    }

    @Override
    public final int getFontSize() {
        return getFont().getSize();
    }

    @Override
    public final void setColor(int red, int green, int blue) {
        setColor(new Color(red, green, blue));
    }

    @Override
    public final void setColor(int RGB) {
        setColor(new Color(RGB));
    }

    @Override
    public final void setStringAntiAllias(boolean antiallias) {
        this.setFontAntialiasing(antiallias);
    }

    public abstract Color getColor();

    public abstract void setColor(Color c);

    public abstract Font getFont();

    public abstract void setFont(Font font);

    public final void clip(Rectangle rect) {
        clipRect(rect.x, rect.y, rect.width, rect.height);
    }

    public final void setClip(Rectangle rect) {
        setClip(rect.x, rect.y, rect.width, rect.height);
    }

    public abstract void setClip(int x, int y, int width, int height);

    public abstract void clipRect(int x, int y, int width, int height);

    public abstract boolean hitClip(int x, int y, int width, int height);

    public abstract void pushComposite();

    public abstract void popComposite();

    public abstract void setPaint(Paint paint);

    public abstract void pushPaint();

    public abstract void popPaint();

    public abstract void setStroke(Stroke s);

    public abstract void pushStroke();

    public abstract void popStroke();

    public void fill(Shape shape) {
        if (shape instanceof Line2D) {
            Line2D line = (Line2D) shape;
            drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
        } else if (shape instanceof Rectangle2D) {
            Rectangle2D rect = (Rectangle2D) shape;
            fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
        } else if (shape instanceof Ellipse2D) {
            Ellipse2D arc = (Ellipse2D) shape;
            fillArc((int) arc.getX(), (int) arc.getY(), (int) arc.getWidth(), (int) arc.getHeight(), 0, 360);
        } else if (shape instanceof Polygon) {
            Polygon poly = (Polygon) shape;
            fillPolygon(poly.xpoints, poly.ypoints, poly.npoints);
        } else if (shape instanceof Path2D) {
            Path2D path = (Path2D) shape;
            fillPath(path);
        }
    }

    public void draw(Shape shape) {
        if (shape instanceof Line2D) {
            Line2D line = (Line2D) shape;
            drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
        } else if (shape instanceof Rectangle2D) {
            Rectangle2D rect = (Rectangle2D) shape;
            drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
        } else if (shape instanceof Ellipse2D) {
            Ellipse2D arc = (Ellipse2D) shape;
            drawArc((int) arc.getX(), (int) arc.getY(), (int) arc.getWidth(), (int) arc.getHeight(), 0, 360);
        } else if (shape instanceof Polygon) {
            Polygon poly = (Polygon) shape;
            drawPolygon(poly.xpoints, poly.ypoints, poly.npoints);
        } else if (shape instanceof Path2D) {
            Path2D path = (Path2D) shape;
            drawPath(path);
        }
    }

    public abstract void drawLine(int x1, int y1, int x2, int y2);

    public abstract void drawPath(Path2D path);

    public abstract void fillPath(Path2D path);

    public abstract void fillRect(int x, int y, int width, int height);

    public abstract void fillRect(Rectangle2D rect);

    public abstract void drawRect(int x, int y, int width, int height);

    public abstract void drawRect(Rectangle2D rect);

    public abstract void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    public abstract void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    public abstract void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);

    public abstract void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle);

    public final void drawOval(int x, int y, int width, int height) {
        drawArc(x, y, width, height, 0, 360);
    }

    public final void fillOval(int x, int y, int width, int height) {
        fillArc(x, y, width, height, 0, 360);
    }

    public abstract void drawPolyline(int xPoints[], int yPoints[], int nPoints);

    public abstract void drawPolygon(int xPoints[], int yPoints[], int nPoints);

    public abstract void drawPolygon(Polygon p);

    public abstract void fillPolygon(int xPoints[], int yPoints[], int nPoints);

    public abstract void fillPolygon(Polygon p);

    /**
	 * 绘制指定图像中当前可用的图像。图像的左上角位于该图形上下文坐标空间的 (x, y)。
	 * @param img 要绘制的指定图像。
	 * @param x x 坐标。
	 * @param y y 坐标。
	 */
    public abstract boolean drawImage(Image img, int x, int y);

    /**
	 * 绘制指定图像中已缩放到适合指定矩形内部的图像。 
	 * 图像绘制在此图形上下文坐标空间的指定矩形内部，如果需要，则进行缩放。
	 * @param img 要绘制的指定图像。
	 * @param x x 坐标。
	 * @param y y 坐标。
	 * @param w 矩形的宽度。
	 * @param h 矩形的高度。
	 */
    public abstract boolean drawImage(Image img, int x, int y, int width, int height);

    /**
	 * 绘制当前可用的指定图像的指定区域，动态地缩放图像使其符合目标绘制表面的指定区域。
	 * 此方法总是用非缩放的图像来呈现缩放的矩形，并且动态地执行所需的缩放。此操作不使用缓存的缩放图像。
	 * 执行图像从源到目标的缩放：源矩形的第一个坐标被映射到目标矩形的第一个坐标，第二个源坐标被映射到第二个目标坐标。
	 * 按需要缩放和翻转子图像以保持这些映射关系。 
	 * @param img 要绘制的指定图像。
	 * @param dx1 目标矩形第一个角的 x 坐标。
	 * @param dy1 目标矩形第一个角的 y 坐标。
	 * @param dx2 目标矩形第二个角的 x 坐标。
	 * @param dy2 目标矩形第二个角的 y 坐标。
	 * @param sx1 源矩形第一个角的 x 坐标。
	 * @param sy1 源矩形第一个角的 y 坐标。
	 * @param sx2 源矩形第二个角的 x 坐标。
	 * @param sy2 源矩形第二个角的 y 坐标。
	 * @return
	 */
    public abstract boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2);

    @Override
    public final void drawRoundImage(IImage img, int x, int y, int width, int height, int transform) {
        pushClip();
        clipRect(x, y, width, height);
        int w = img.getWidth();
        int h = img.getHeight();
        for (int dx = 0; dx < width; ) {
            for (int dy = 0; dy < height; ) {
                drawImage(img, x + dx, y + dy, 0);
                dy += h;
            }
            dx += w;
        }
        popClip();
    }

    public abstract void drawChars(char data[], int offset, int length, int x, int y);

    public abstract void drawString(String str, int x, int y);

    public abstract void drawString(String str, float x, float y);

    public abstract void drawString(AttributedString atext, int x, int y);

    public abstract void drawString(AttributedString atext, float x, float y);

    public abstract void translate(int x, int y);

    public abstract void translate(double tx, double ty);

    public abstract void rotate(double theta);

    public abstract void rotate(double theta, double x, double y);

    public abstract void scale(double sx, double sy);

    public abstract void shear(double shx, double shy);

    public abstract void popTransform();

    public abstract void pushTransform();

    public abstract float setAlpha(float alpha);

    public abstract float getAlpha();

    public abstract boolean setFontAntialiasing(boolean enable);

    public abstract boolean getFontAntialiasing();

    protected final void transform(int transform, int width, int height) {
        switch(transform) {
            case TRANS_ROT90:
                {
                    translate(height, 0);
                    rotate(ANGLE_90);
                    break;
                }
            case TRANS_ROT180:
                {
                    translate(width, height);
                    rotate(Math.PI);
                    break;
                }
            case TRANS_ROT270:
                {
                    translate(0, width);
                    rotate(ANGLE_270);
                    break;
                }
            case TRANS_MIRROR:
                {
                    translate(width, 0);
                    scale(-1, 1);
                    break;
                }
            case TRANS_MIRROR_ROT90:
                {
                    translate(height, 0);
                    rotate(ANGLE_90);
                    translate(width, 0);
                    scale(-1, 1);
                    break;
                }
            case TRANS_MIRROR_ROT180:
                {
                    translate(width, 0);
                    scale(-1, 1);
                    translate(width, height);
                    rotate(Math.PI);
                    break;
                }
            case TRANS_MIRROR_ROT270:
                {
                    rotate(ANGLE_270);
                    scale(-1, 1);
                    break;
                }
        }
    }
}
