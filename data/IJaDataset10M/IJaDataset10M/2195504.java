package artofillusion.procedural;

import artofillusion.*;
import artofillusion.math.*;
import java.awt.*;

/** This is a Module which outputs the square root of a number. */
public class SqrtModule extends Module {

    boolean valueOk, errorOk, gradOk;

    double value, error, valueIn, errorIn, lastBlur;

    Vec3 gradient;

    public SqrtModule(Point position) {
        super("Sqrt", new IOPort[] { new IOPort(IOPort.NUMBER, IOPort.INPUT, IOPort.LEFT, new String[] { "Value", "(0)" }) }, new IOPort[] { new IOPort(IOPort.NUMBER, IOPort.OUTPUT, IOPort.RIGHT, new String[] { "Root" }) }, position);
        gradient = new Vec3();
    }

    public void init(PointInfo p) {
        valueOk = errorOk = gradOk = false;
    }

    public double getAverageValue(int which, double blur) {
        if (valueOk && blur == lastBlur) return value;
        valueOk = true;
        lastBlur = blur;
        if (linkFrom[0] == null) {
            value = error = 0.0;
            errorOk = true;
            return 0.0;
        }
        valueIn = linkFrom[0].getAverageValue(linkFromIndex[0], blur);
        errorIn = linkFrom[0].getValueError(linkFromIndex[0], blur);
        if (errorIn == 0.0) {
            if (valueIn < 0.0) value = -Math.sqrt(-valueIn); else value = Math.sqrt(valueIn);
            error = 0.0;
            errorOk = true;
            return value;
        }
        value = (integral(valueIn + errorIn) - integral(valueIn - errorIn)) / (2.0 * errorIn);
        return value;
    }

    private double integral(double x) {
        if (x > 0.0) return (2.0 / 3.0) * Math.pow(x, 1.5); else return (2.0 / 3.0) * Math.pow(-x, 1.5);
    }

    public double getValueError(int which, double blur) {
        if (!valueOk || blur != lastBlur) getAverageValue(which, blur);
        if (errorOk) return error;
        errorOk = true;
        if (errorIn == 0.0) {
            error = 0.0;
            return 0.0;
        }
        error /= 2.0 * Math.sqrt(Math.abs(valueIn));
        return error;
    }

    public void getValueGradient(int which, Vec3 grad, double blur) {
        if (gradOk && blur == lastBlur) {
            grad.set(gradient);
            return;
        }
        if (linkFrom[0] == null) {
            grad.set(0.0, 0.0, 0.0);
            return;
        }
        if (!valueOk || blur != lastBlur) getAverageValue(which, blur);
        gradOk = true;
        linkFrom[0].getValueGradient(linkFromIndex[0], gradient, blur);
        gradient.scale(0.5 / value);
        grad.set(gradient);
    }
}
