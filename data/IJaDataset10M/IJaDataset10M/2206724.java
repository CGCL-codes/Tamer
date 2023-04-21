package com.tcmj.pm.mta.jfree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.ChartColor;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author tcmj
 */
public class MTAColorsAndShapes {

    private static Shape[] createStandardSeriesShapes(double size) {
        Shape[] result = new Shape[10];
        double delta = size / 2.0;
        int[] xpoints = null;
        int[] ypoints = null;
        result[0] = new Ellipse2D.Double(-delta, -delta, size, size);
        result[1] = new Rectangle2D.Double(-delta, -delta, size - 1, size - 1);
        xpoints = intArray(0.0, delta, -delta);
        ypoints = intArray(-delta, delta, delta);
        result[2] = new Polygon(xpoints, ypoints, 3);
        xpoints = intArray(0.0, delta, 0.0, -delta);
        ypoints = intArray(-delta, 0.0, delta, 0.0);
        result[3] = new Polygon(xpoints, ypoints, 4);
        result[4] = ShapeUtilities.createDiagonalCross((float) size / 2.5f, 1.0f);
        xpoints = intArray(-delta, +delta, 0.0);
        ypoints = intArray(-delta, -delta, delta);
        result[5] = new Polygon(xpoints, ypoints, 3);
        result[6] = new Ellipse2D.Double(-delta, -delta / 2, size, size / 2);
        result[6] = ShapeUtilities.createRegularCross((float) size / 2.0f, 2f);
        xpoints = intArray(-delta, delta, -delta);
        ypoints = intArray(-delta, 0.0, delta);
        result[7] = new Polygon(xpoints, ypoints, 3);
        result[8] = new Rectangle2D.Double(-delta / 2, -delta, size / 2, size);
        xpoints = intArray(-delta, delta, delta);
        ypoints = intArray(0.0, -delta, +delta);
        result[9] = new Polygon(xpoints, ypoints, 3);
        return result;
    }

    private static int[] intArray(double a, double b, double c) {
        return new int[] { (int) a, (int) b, (int) c };
    }

    private static int[] intArray(double a, double b, double c, double d) {
        return new int[] { (int) a, (int) b, (int) c, (int) d };
    }

    public static DrawingSupplier createDrawingSupplier(int seriesCount) {
        Paint[] mtapaints = new Paint[] { Color.BLACK, ChartColor.DARK_RED, ChartColor.VERY_DARK_GREEN, ChartColor.DARK_BLUE, ChartColor.VERY_DARK_MAGENTA, ChartColor.GRAY, ChartColor.VERY_DARK_BLUE, ChartColor.VERY_DARK_RED, ChartColor.VERY_DARK_YELLOW, ChartColor.DARK_YELLOW, ChartColor.VERY_DARK_CYAN, ChartColor.LIGHT_MAGENTA, ChartColor.DARK_CYAN, ChartColor.VERY_LIGHT_BLUE, ChartColor.VERY_LIGHT_GREEN, ChartColor.VERY_LIGHT_MAGENTA, ChartColor.VERY_LIGHT_CYAN, ChartColor.ORANGE };
        double shapesize;
        float strokesize;
        if (seriesCount <= 5) {
            shapesize = 8d;
            strokesize = 1.5f;
        } else {
            shapesize = 6d;
            strokesize = 1.0f;
        }
        Stroke[] strokes = new Stroke[] { new BasicStroke(strokesize, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL) };
        DefaultDrawingSupplier supplier = new DefaultDrawingSupplier(mtapaints, DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, strokes, DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, createStandardSeriesShapes(shapesize));
        return supplier;
    }
}
