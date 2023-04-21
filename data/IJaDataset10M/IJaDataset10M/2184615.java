package net.benojt.iterator;

import net.benojt.FractalPanel;
import net.benojt.tools.Complex;

/**
 * a template without complex numbers
 * @author frank
 *
 */
public class SimpleTemplate extends AbstractParameterIterator implements IteratorTemplate {

    public static final String templateName = "SimpleTemplate";

    public static final String version = "version";

    public SimpleTemplate() {
        super();
        ;
    }

    public int iterPoint(double[] coords) {
        double re = coords[0];
        double im = coords[1];
        z.re = 0;
        z.im = 0;
        ;
        value = 0d;
        iter = 0;
        while (iter++ <= maxIter && value <= maxValue) {
            double _re = z.re * z.re - z.im * z.im + re;
            double _im = 2 * z.re * z.im + im;
            z.re = _re;
            z.im = _im;
            ;
            value = z.mod();
        }
        if (iter > maxIter) return -1; else return iter;
    }

    public String getInfoMessage() {
        return "This is a template for complex plane fractals without complex numbers.";
    }

    public static final String[][] templateItems = new String[][] { { "Constructor" }, { "Initialisation" }, { "Polynom" }, { "Extracode" }, { "Info" } };
}
