package ai.test;

import ai.backbrop.*;
import ai.test.BackProp;

class NeuralNet {

    public static void main(String arg[]) {
        double target[][] = { { 0 }, { 1 }, { 1 }, { 0 }, { 1 }, { 0 }, { 0 }, { 1 } };
        double testData[][] = { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 1, 0 }, { 0, 1, 1 }, { 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 }, { 1, 1, 1 } };
        int lSz[] = { 3, 6, 1 };
        int nTest = target.length;
        double beta = .01, alpha = 1000., Thresh = 0.00001;
        long num_iter = 2000000;
        BackProp bp = new BackProp(lSz, beta, alpha);
        System.out.println("Now training the network....");
        int i;
        for (i = 0; i < num_iter; i++) {
            double maxError = 0;
            for (int j = 0; j < nTest; j++) {
                bp.bpgt(testData[j], target[j]);
                double err = bp.mse(target[j]);
                maxError = Math.max(err, maxError);
            }
            if (maxError < Thresh) {
                System.out.println("Network Trained. Threshold value achieved in " + i * nTest + " iterations.\n" + "MSE:  " + maxError);
                break;
            }
            System.out.println("MSE:  " + String.format("%f7.5", maxError) + "... Training...");
        }
        if (i == num_iter) {
            System.out.println(i + " iterations completed..." + "MSE: " + bp.mse(target[(i - 1) % 8]));
        }
        System.out.println("Now using the trained network to make predctions on test data....");
        for (i = 0; i < nTest; i++) {
            bp.ffwd(testData[i]);
            System.out.println(testData[i][0] + "  " + testData[i][1] + "  " + testData[i][2] + "  " + target[i][0] + "  " + bp.Out(0));
        }
    }
}
