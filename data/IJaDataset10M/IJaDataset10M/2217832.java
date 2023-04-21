package net.sf.refactorit.metrics;

import net.sf.refactorit.classmodel.BinMethod;

/**
 * Number of parameters (NP) metric.

 */
public class NumberOfParametersMetric {

    /** Hidden constructor. */
    private NumberOfParametersMetric() {
    }

    /**
   * Calculates number of parameters metric for the method.

   *

   * @param method method.

   *

   * @return number of parameters metric for the <code>method</code>.

   */
    public static int calculate(BinMethod method) {
        return method.getParameters().length;
    }
}
