package src.projects.findPeaks.tests;

import java.util.Vector;
import src.lib.FDR.LinearRegression;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.objects.PeakPairIdx;

/**
 * Test for the Linear Regression module
 * @author afejes
 * @version $Revision: 2935 $
 *
 */
public class test_linear_regression {

    private test_linear_regression() {
    }

    public static void main(String[] args) {
        float[][] oxygen_data = { { 0.99f, 90.1f }, { 1.02f, 89.05f }, { 1.15f, 91.43f }, { 1.29f, 93.74f }, { 1.46f, 96.73f }, { 1.36f, 94.45f }, { 0.87f, 87.59f }, { 1.23f, 91.77f }, { 1.55f, 99.42f }, { 1.40f, 93.65f }, { 1.19f, 93.54f }, { 1.15f, 92.52f }, { 0.98f, 90.56f }, { 1.01f, 89.54f }, { 1.11f, 89.85f }, { 1.20f, 90.39f }, { 1.26f, 93.25f }, { 1.32f, 93.41f }, { 1.43f, 94.98f }, { 0.95f, 87.33f } };
        Log_Buffer LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        Thread th = new Thread(LB);
        th.start();
        LB.notice("This example comes from Chapter 10 - Simple Linear Regression and Correlation");
        LB.notice("TODO: get name of textbook from Inanc.");
        Vector<PeakPairIdx> store = new Vector<PeakPairIdx>();
        for (float[] a : oxygen_data) {
            PeakPairIdx z = new PeakPairIdx(1, 1, a[0], a[1]);
            store.add(z);
        }
        PeakPairIdx[] pairs = store.toArray(new PeakPairIdx[store.size()]);
        LinearRegression n = new LinearRegression(LB, pairs);
        n.filter(0.05, 0);
        LB.notice("Expected values: (note: rounding errors are present.)");
        LB.notice("Sxy:       10.18");
        LB.notice("Sx2:       29.29");
        LB.notice("Sy2:       170044.53");
        LB.notice("Sxx:       0.68");
        LB.notice("Syy:       173.37");
        LB.notice("slope:     14.97");
        LB.notice("intercept: 74.20");
        LB.notice("Calculated values: ");
        n.write_data();
        LB.close();
    }
}
