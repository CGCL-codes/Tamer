package preprocessing.methods.FeatureSelection.FrFsMethods;

import preprocessing.methods.FeatureSelection.StatMeasures.Measure;
import preprocessing.methods.FeatureSelection.StatMeasures.MyHashBasedMIMeasure;
import preprocessing.methods.FeatureSelection.StatMeasures.MIMeasureOK;
import java.util.Arrays;

/**
 *
 * @author pilnya1
 */
public class XyMIMethod_MI_OK extends Method {

    public static String getName() {
        return "XyMI";
    }

    double[][] miMatrix;

    private static final int MI_MEASURE = 0;

    public XyMIMethod_MI_OK() {
        methodDescription = "Compute ranks as a sorted order of sums of MI (ok) between specific attribute and  the rest of attributes. ";
    }

    @Override
    @SuppressWarnings("static-access")
    public boolean computeRanks() {
        try {
            MIMeasureOK mim = ((MIMeasureOK) measures[MI_MEASURE]);
            int numAttributes = mim.NUM_ATTRIBUTES - 1;
            System.out.println("ranks in Xy MI OK Method()");
            proportionalImportance = new double[numAttributes];
            double[] attClassMI;
            attClassMI = mim.getClassAttMIarray();
            miMatrix = mim.getMImatrix();
            for (int i = 0; i < miMatrix.length; i++) {
                for (int j = 0; j < miMatrix[i].length; j++) {
                    if (i != j) {
                        double miIJ = miMatrix[i][j];
                        proportionalImportance[i] += miIJ;
                        System.out.printf("%1.3f\t", miIJ);
                    } else System.out.printf("%5d\t", 0);
                }
                System.out.println();
            }
            ranks = computeRanksFromProportionalImportance(proportionalImportance);
            setNormalisedProportionalImportance();
            System.out.println("prop. importance: " + Arrays.toString(proportionalImportance));
            System.out.println("ranks           : " + Arrays.toString(ranks));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("exception in XyMIMethod - computeRanks()...");
            return false;
        }
        return true;
    }

    @Override
    public Class[] getMethodMeasureClasses() {
        if (methodMeasureClasses == null) {
            Class[] classes = { MIMeasureOK.class };
            methodMeasureClasses = classes;
            measures = new Measure[methodMeasureClasses.length];
        }
        return methodMeasureClasses;
    }
}
