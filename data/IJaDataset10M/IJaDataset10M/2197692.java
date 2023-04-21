package org.gzigzag.module;

import org.gzigzag.*;
import java.util.*;
import java.awt.*;

/** Default metrics: Euclidean, no focus+context (no scaling)
 */
public class AwtMetricsNormal extends AwtMetrics {

    public String getZObName() {
        return "Normal(Euclidean)";
    }

    public double[] transformAtFocusNbhood(double x, double y, double focusCoeff) {
        return new double[] { x, y };
    }

    public double[] inverseTransformAtFocusNbhood(double x, double y, double focusCoeff) {
        return new double[] { x, y };
    }

    public double transformScalingValue(double s) {
        return 1.0;
    }

    public double focusCoefficientMinimum() {
        return 1.0;
    }

    public double focusCoefficientMaximum() {
        return 1.0;
    }

    public AwtMetricsNormal() {
        super();
    }

    public AwtMetricsNormal(ZZCell start, Dimension rv, Dimension vv, double ox, double oy) {
        super(start, rv, vv, ox, oy);
    }
}
