package moa.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import weka.core.Utils;

/**
 * Class implementing some utility methods.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Bernhard Pfahringer (bernhard@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public class MiscUtils {

    public static int chooseRandomIndexBasedOnWeights(double[] weights, Random random) {
        double probSum = Utils.sum(weights);
        double val = random.nextDouble() * probSum;
        int index = 0;
        double sum = 0.0;
        while ((sum <= val) && (index < weights.length)) {
            sum += weights[index++];
        }
        return index - 1;
    }

    public static int poisson(double lambda, Random r) {
        if (lambda < 100.0) {
            double product = 1.0;
            double sum = 1.0;
            double threshold = r.nextDouble() * Math.exp(lambda);
            int i = 1;
            int max = Math.max(100, 10 * (int) Math.ceil(lambda));
            while ((i < max) && (sum <= threshold)) {
                product *= (lambda / i);
                sum += product;
                i++;
            }
            return i - 1;
        }
        double x = lambda + Math.sqrt(lambda) * r.nextGaussian();
        if (x < 0.0) {
            return 0;
        }
        return (int) Math.floor(x);
    }

    public static String getStackTraceString(Exception ex) {
        StringWriter stackTraceWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTraceWriter));
        return "*** STACK TRACE ***\n" + stackTraceWriter.toString();
    }
}
