package views.widgets.chart.tests;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Hashtable;
import org.jfree.chart.plot.DefaultDrawingSupplier;

public class MODrawingSupplier extends DefaultDrawingSupplier {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8501946169726524395L;

    /** The default stroke sequence. */
    public static final Stroke[] DEFAULT_STROKE_SEQUENCE = new Stroke[] { new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL), new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 2f, 2f, 2f }, 0.0f), new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 6.0f }, 0.0f), new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 2f, 3f, 4f, 3f, 8.0f, 3f, 4f, 3f, 2f }, 0.0f), new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 3f, 3f, 6f }, 0.0f) };

    private Hashtable<Shape, Integer> usedShapes = new Hashtable<Shape, Integer>();

    private Hashtable<Paint, Integer> usedPaints = new Hashtable<Paint, Integer>();

    private Hashtable<Stroke, Integer> usedStrokes = new Hashtable<Stroke, Integer>();

    public MODrawingSupplier() {
        super(DEFAULT_PAINT_SEQUENCE, DEFAULT_FILL_PAINT_SEQUENCE, DEFAULT_OUTLINE_PAINT_SEQUENCE, DEFAULT_STROKE_SEQUENCE, DEFAULT_OUTLINE_STROKE_SEQUENCE, DEFAULT_SHAPE_SEQUENCE);
    }

    public Paint getNextPaint() {
        int i = 0;
        Paint result;
        while (i < DEFAULT_PAINT_SEQUENCE.length) {
            result = super.getNextPaint();
            if (!usedPaints.containsKey(result)) {
                usedPaints.put(result, 0);
                return result;
            }
            i++;
        }
        result = super.getNextPaint();
        usedPaints.put(result, usedPaints.get(result).intValue() + 1);
        return result;
    }

    public Stroke getNextStroke() {
        int i = 0;
        System.out.println("getNextStroke()");
        Stroke result;
        while (i < DEFAULT_STROKE_SEQUENCE.length) {
            result = super.getNextStroke();
            if (!usedStrokes.containsKey(result)) {
                usedStrokes.put(result, 0);
                return result;
            }
            i++;
        }
        result = super.getNextStroke();
        usedStrokes.put(result, usedStrokes.get(result).intValue() + 1);
        return result;
    }

    public Shape getNextShape() {
        int i = 0;
        Shape result;
        while (i < DEFAULT_SHAPE_SEQUENCE.length) {
            result = super.getNextShape();
            if (!usedShapes.containsKey(result)) {
                usedShapes.put(result, 0);
                return result;
            }
            System.out.println("used : " + result);
            i++;
        }
        result = super.getNextShape();
        usedShapes.put(result, usedShapes.get(result).intValue() + 1);
        return result;
    }

    public boolean isUsedPaint(Paint paint) {
        return (paint != null && usedPaints.containsKey(paint));
    }

    public boolean isUsedStroke(Stroke stroke) {
        return (stroke != null && usedStrokes.containsKey(stroke));
    }

    public boolean isUsedShape(Shape shape) {
        return (shape != null && usedShapes.containsKey(shape));
    }

    public void removePaint(Paint paint) {
        if (paint == null) return;
        Integer i = usedPaints.get(paint);
        if (i != null) {
            if (i.intValue() == 1) {
                usedPaints.remove(paint);
            } else {
                usedPaints.put(paint, i.intValue() - 1);
            }
        }
    }

    public void removeStroke(Stroke stroke) {
        if (stroke == null) return;
        Integer i = usedStrokes.get(stroke);
        if (i != null) {
            if (i.intValue() == 1) {
                usedStrokes.remove(stroke);
            } else {
                usedStrokes.put(stroke, i.intValue() - 1);
            }
        }
    }

    public void removeShape(Shape shape) {
        if (shape == null) return;
        Integer i = usedShapes.get(shape);
        if (i != null) {
            if (i.intValue() == 1) {
                usedShapes.remove(shape);
            } else {
                usedShapes.put(shape, i.intValue() - 1);
            }
        }
    }

    public void addUsedPaint(Paint p) {
        Integer i = usedPaints.get(p);
        if (i != null) {
            usedPaints.put(p, i.intValue() + 1);
        } else {
            usedPaints.put(p, 0);
        }
    }

    public void addUsedStroke(Stroke s) {
        Integer i = usedStrokes.get(s);
        if (i != null) {
            usedStrokes.put(s, i.intValue() + 1);
        } else {
            usedStrokes.put(s, 0);
        }
    }

    public void addUsedShape(Shape s) {
        Integer i = usedShapes.get(s);
        if (i != null) {
            usedShapes.put(s, i.intValue() + 1);
        } else {
            usedShapes.put(s, 0);
        }
    }
}
