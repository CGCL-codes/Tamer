package jarcade.graphics;

import jarcade.utilities.ShapeUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Owner
 */
public class PaintedShapeSprite extends Sprite {

    private Paint fillPaint;

    private Stroke stroke;

    private Paint strokePaint;

    public static PaintedShapeSprite createRectangleSprite(Color fill, Color border, double x, double y, double width, double height) {
        Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
        PaintedShapeSprite ps = new PaintedShapeSprite(rect, fill, new BasicStroke(1.0f), border);
        return ps;
    }

    public static PaintedShapeSprite createStarSprite(Color fill, Color border, double x, double y, int points, int outerRadius, int innerRadius) {
        Shape starShape = ShapeUtil.createStarPolygon(x, y, points, innerRadius, outerRadius);
        PaintedShapeSprite ps = new PaintedShapeSprite(starShape, fill, new BasicStroke(1.0f), border);
        return ps;
    }

    public static PaintedShapeSprite createRegularPolygonSprite(Color fill, Color border, double x, double y, int sides, double radius) {
        Shape polyShape = ShapeUtil.createRegularPolygon(x, y, sides, radius);
        PaintedShapeSprite ps = new PaintedShapeSprite(polyShape, fill, new BasicStroke(1.0f), border);
        return ps;
    }

    public static PaintedShapeSprite createCircleSprite(Color fill, Color border, double x, double y, double radius) {
        return createEllipseSprite(fill, border, x, y, 2 * radius, 2 * radius);
    }

    public static PaintedShapeSprite createEllipseSprite(Color fill, Color border, double x, double y, double width, double height) {
        Ellipse2D shape = new Ellipse2D.Double(x - (width / 2), y - (height / 2), width, height);
        PaintedShapeSprite ps = new PaintedShapeSprite(shape, fill, new BasicStroke(1.0f), border);
        return ps;
    }

    public PaintedShapeSprite(Shape shape, Paint fillPaint) {
        super(shape);
        setFillPaint(fillPaint);
    }

    public PaintedShapeSprite(Shape shape, Stroke stroke, Paint strokePaint) {
        super(shape);
        setStroke(stroke);
        setStrokePaint(strokePaint);
    }

    public PaintedShapeSprite(Shape shape, Paint fillPaint, Stroke stroke, Paint strokePaint) {
        super(shape);
        setFillPaint(fillPaint);
        setStroke(stroke);
        setStrokePaint(strokePaint);
    }

    public void paint(Graphics2D gfx) {
        if (!this.isPaintActive()) {
            return;
        }
        if (fillPaint != null) {
            gfx.setPaint(fillPaint);
            gfx.fill(getShape());
        }
        if (stroke != null) {
            if (strokePaint != null) {
                gfx.setPaint(strokePaint);
            }
            gfx.draw(getShape());
        }
    }

    public Paint getFillPaint() {
        return fillPaint;
    }

    public void setFillPaint(Paint fillPaint) {
        this.fillPaint = fillPaint;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public Paint getStrokePaint() {
        return strokePaint;
    }

    public void setStrokePaint(Paint strokePaint) {
        this.strokePaint = strokePaint;
    }

    @Override
    public void update(long elapsedMilliseconds) {
    }
}
