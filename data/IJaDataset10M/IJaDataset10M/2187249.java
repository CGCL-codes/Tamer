package com.eteks.tools.awt;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import com.eteks.parser.CompiledFunction;

/** * A canvas able to display a 2D curve defined by the equation y = f(x). * * @version 1.0 * @author  Emmanuel Puybaret * @since   Jeks 1.0 */
public class Curve2DDisplay extends Canvas {

    private double[] values;

    private double minimumX;

    private double maximumX;

    private double minimumY = 0.;

    private double maximumY = 0.;

    private CompiledFunction function = null;

    private static final boolean regular = true;

    public Dimension preferredSize() {
        return new Dimension(300, 200);
    }

    public void computeCurve(CompiledFunction function, double minimumX, double maximumX) {
        this.function = function;
        this.minimumX = minimumX;
        this.maximumX = maximumX;
        values = new double[getSize().width];
        double[] parameters = new double[1];
        double pasCalcul = (maximumX - minimumX) / values.length;
        for (int i = 0; i < values.length; i++) {
            parameters[0] = i * pasCalcul + minimumX;
            values[i] = function.computeFunction(parameters);
            if (!regular) if (i == 0) minimumY = maximumY = values[0]; else if (values[i] < minimumY) minimumY = values[i]; else if (values[i] > maximumY) maximumY = values[i];
        }
        repaint();
    }

    public void reshape(int x, int y, int width, int height) {
        values = null;
        super.reshape(x, y, width, height);
        if (function != null) computeCurve(function, minimumX, maximumX);
    }

    public void paint(Graphics gc) {
        gc.setColor(Color.black);
        if (values != null) {
            Dimension size = size();
            int previousPoint = regular ? size.height / 2 - (int) (values[0] / (maximumX - minimumX) * size.width) : (int) ((maximumY - values[0]) / (maximumY - minimumY) * size.height);
            for (int i = 1; i < values.length; i++) {
                int nextPoint = regular ? size.height / 2 - (int) (values[i] / (maximumX - minimumX) * size.width) : (int) ((maximumY - values[i]) / (maximumY - minimumY) * size.height);
                if (values[i - 1] == values[i - 1] && values[i] == values[i]) gc.drawLine(i - 1, previousPoint, i, nextPoint);
                previousPoint = nextPoint;
            }
            gc.setColor(getBackground());
            gc.draw3DRect(0, 0, size.width - 1, size.height - 1, false);
        }
    }
}
