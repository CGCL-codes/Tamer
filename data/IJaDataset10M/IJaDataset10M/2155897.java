package promas10.tools;

import cartago.*;

public class Calc extends Artifact {

    @OPERATION
    void compute(double x, OpFeedbackParam<Double> res) {
        double result = longTermCalc(x);
        res.set(result);
    }

    private double longTermCalc(double x) {
        try {
            Thread.sleep(2000);
        } catch (Exception ex) {
        }
        return 1.0;
    }
}
