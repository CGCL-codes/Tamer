package org.icepdf.core.pobjects.functions;

import org.icepdf.core.pobjects.Dictionary;
import java.util.Vector;

/**
 * <p>Type 3 Function (PDF 1.3) defines a stitching of the subdomains of
 * serveral 1-input functionsto produce a single new 1-input function.</p>
 *
 * @author ICEsoft Technologies Inc.
 * @since 3.0
 */
public class Function_3 extends Function {

    private float bounds[];

    private float encode[];

    private Function functions[];

    /**
     * Creates a new instance of a type 2 function.
     *
     * @param d function's dictionary.
     */
    Function_3(Dictionary d) {
        super(d);
        Vector boundTemp = (Vector) d.getObject("Bounds");
        if (boundTemp != null) {
            bounds = new float[boundTemp.size()];
            for (int i = 0; i < boundTemp.size(); i++) {
                bounds[i] = ((Number) boundTemp.elementAt(i)).floatValue();
            }
        }
        Vector encodeTemp = (Vector) d.getObject("Encode");
        if (encodeTemp != null) {
            encode = new float[encodeTemp.size()];
            for (int i = 0; i < encodeTemp.size(); i++) {
                encode[i] = ((Number) encodeTemp.elementAt(i)).floatValue();
            }
        }
        Vector functionTemp = (Vector) d.getObject("Functions");
        if (encodeTemp != null) {
            functions = new Function[functionTemp.size()];
            for (int i = 0; i < functionTemp.size(); i++) {
                functions[i] = Function.getFunction(d.getLibrary(), functionTemp.get(i));
            }
        }
    }

    /**
     * <p>Puts the value x thought the function type 3 algorithm.
     *
     * @param x input values m
     * @return output values n
     */
    public float[] calculate(float[] x) {
        int k = functions.length;
        if (k == 1 && bounds.length == 0) {
            if (domain[0] <= x[0] && x[0] <= domain[1]) {
                return encode(x, functions[0], 0);
            }
        }
        for (int b = 0; b < bounds.length; b++) {
            if (b == 0) {
                if (domain[0] <= x[0] && x[0] < bounds[b]) {
                    return encode(x, functions[b], b);
                }
            }
            if (b == k - 2) {
                if (bounds[b] <= x[0] && x[0] <= domain[1]) {
                    return encode(x, functions[k - 1], k - 1);
                }
            }
            if (bounds[b] <= x[0] && x[0] < bounds[b + 1]) {
                return encode(x, functions[b], b);
            }
        }
        return null;
    }

    /**
     * Utility method to apply the interpolation rules and finally calculate
     * the return value using the selected function.  The method also checks
     * to see if the values fall in the specified range and makes the
     * appropriate adjustments, if range is present.
     *
     * @param x        one element array,
     * @param function function to be applied can be of any type.
     * @param i        i th subdomain, selected subdomain.
     * @return n length array of calculated values.  n length is defined by the
     *         colour space component count.
     */
    private float[] encode(float[] x, Function function, int i) {
        int k = functions.length;
        if (i <= 0 && i < k && bounds.length > 0) {
            float b1;
            float b2;
            if (i - 1 == -1) {
                b1 = domain[0];
            } else {
                b1 = bounds[i - 1];
            }
            if (i == k - 1) {
                b2 = domain[1];
            } else {
                b2 = bounds[i];
            }
            if (k - 2 < bounds.length && bounds[k - 2] == domain[1]) {
                x[0] = encode[2 * i];
            }
            x[0] = interpolate(x[0], b1, b2, encode[2 * i], encode[2 * i + 1]);
            x = function.calculate(x);
        } else {
            x[0] = interpolate(x[0], domain[0], domain[1], encode[2 * i], encode[2 * i + 1]);
            x = function.calculate(x);
        }
        return validateAgainstRange(x);
    }

    /**
     * Utility method to check if the values fall within the functions range.
     *
     * @param values values to test against range.
     * @return correct values that fall within the functions range.
     */
    private float[] validateAgainstRange(float[] values) {
        for (int j = 0, max = values.length; j < max; j++) {
            if (range != null && values[j] < range[2 * j]) {
                values[j] = range[2 * j];
            } else if (range != null && values[j] > range[(2 * j) + 1]) {
                values[j] = range[(2 * j) + 1];
            } else if (values[j] < 0) {
                values[j] = 0.0f;
            }
        }
        return values;
    }
}
