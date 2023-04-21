package uk.ac.imperial.ma.metric.plotting;

/**
 * This class extends CoordTransformer. It generates coordinates
 * for polar curves. Its sole public method is setPoints(). This
 * should be called form outside the paint() method.
 *
 * @author Phil Ramsden
 * @version 0.1
 */
public class PolarCoordGenerator extends CoordTransformer {

    public PolarCoordGenerator() {
    }

    public PolarCoordGenerator(MathPainter mp) {
        this.mathPainter = mp;
    }

    public void setPoints(String funcString, String variable, double thetaMin, double thetaRange, double thetaJump) throws Exception {
        setPoints("r*cos(" + variable + ")", "r*sin(" + variable + ")", variable, "r", funcString, thetaMin, thetaRange, thetaJump);
    }

    public void setPoints(String funcString, String variable, double thetaMin, double thetaRange, int np) throws Exception {
        setPoints("r*cos(" + variable + ")", "r*sin(" + variable + ")", variable, "r", funcString, thetaMin, thetaRange, np);
    }

    public void setPoints(String funcString, String variable, double thetaMin, double thetaRange) throws Exception {
        setPoints(funcString, variable, thetaMin, thetaRange, 40);
    }
}
