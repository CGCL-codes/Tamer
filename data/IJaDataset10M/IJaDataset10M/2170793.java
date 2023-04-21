package preprocessing.methods.OutlierDetection;

import game.utils.Exceptions.InvalidArgument;
import preprocessing.methods.BasePreprocessor;
import preprocessing.storage.PreprocessingStorage;
import weka.core.FastVector;

/**
 * Created by IntelliJ IDEA.
 * User: zamecdus
 * Date: 18.11.2009
 * Time: 22:09:07
 * <p/>
 * The simplest of outlier detection algorithms. By its definition, element is an outlier if less then 'p'-fraction
 * of all elements lie within its 'D'-neighbourhood. Hence, p and D are required as paramaters. For each element,
 * the algorithm calculates distances to all other elements, counting those lower than D and if there are less or equal
 * to (p-fraction of N) D-neighbours, element is marked as an outlier.
 * <p/>
 * Worst case complexity: linear with respect to dimension, quadratic with respect to dataset size.
 * <p/>
 * Euclidean distance is used for measurement, but the algorithm would work for any other distance function.
 */
public class BasicAlgorithm extends OutlierDetector {

    public BasicAlgorithm() {
        super();
        methodName = "Basic Algorithm";
        methodDescription = "Simple, unoptimised distance-based method";
        methodTree = "Outlier Detection.";
        baseConfig = new BasicAlgorithmConfig();
        type = BasePreprocessor.Type.GLOBAL;
    }

    public boolean run() {
        Double p, D;
        try {
            p = (Double) baseConfig.getParameterObjByKey("p").getValue();
            D = (Double) baseConfig.getParameterObjByKey("D").getValue();
        } catch (NoSuchFieldException e) {
            logger.error("No parameter called \"p\" or \"D\" found in " + methodName + ".\n", e);
            return false;
        }
        FastVector output = baseAlgorithm(p, D);
        System.out.println(" DONE");
        try {
            store.addNewAttribute(output, "isOutlier", PreprocessingStorage.DataType.NUMERIC, PreprocessingStorage.AttributeRole.SERVICE);
        } catch (InvalidArgument invalidArgument) {
            logger.error("Exporting failed. Check previous errors.", invalidArgument);
            return false;
        }
        return true;
    }

    @Override
    public void finish() {
    }

    @Override
    public boolean isApplyOnTestingData() {
        return false;
    }

    protected FastVector baseAlgorithm(double p, double D) {
        FastVector output = new FastVector();
        baseAlgorithm(p, D, output, false);
        return output;
    }

    protected int baseAlgorithm(double p, double D, FastVector output, boolean STFU) {
        int[] indices = getNumericInputAttributeIndices();
        int N = 0;
        try {
            if (indices.length < 1) {
                throw new InvalidArgument("Array of indices is of zero length. This can not be.");
            }
            N = store.getAttributeLength(indices[0]);
        } catch (InvalidArgument invalidArgument) {
            logger.error("Invalid something. " + invalidArgument.getMessage());
            invalidArgument.printStackTrace();
            return 0;
        }
        double M = N * (1 - p);
        int outlierCount = 0;
        if (!STFU) System.out.print("Running basic algorithm for outlier detection...");
        for (int i = 0; i < N; i++) {
            int j = 0, neighbours = 0;
            while (j < N && neighbours <= M) {
                if (euclidean(store, indices, i, j) <= D) {
                    neighbours++;
                }
                j++;
            }
            if (neighbours <= M) {
                output.addElement(1.0);
                outlierCount++;
            } else {
                output.addElement(0.0);
            }
        }
        if (!STFU) System.out.print(outlierCount + " outliers found.");
        return outlierCount;
    }
}
