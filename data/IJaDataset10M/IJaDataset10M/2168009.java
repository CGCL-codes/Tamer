package artofillusion.procedural;

import artofillusion.*;
import artofillusion.math.*;
import java.awt.*;

public class ExpModule extends Module {

    boolean valueOk, errorOk, gradOk;

    double value, error, valueIn, errorIn, lastBlur;

    Vec3 gradient;

    public ExpModule(Point position) {
        super("Exp", new IOPort[] { new IOPort(IOPort.NUMBER, IOPort.INPUT, IOPort.LEFT, new String[] { "Value", "(1)" }) }, new IOPort[] { new IOPort(IOPort.NUMBER, IOPort.OUTPUT, IOPort.RIGHT, new String[] { "Exponential" }) }, position);
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
            return 0.0;
        }
        valueIn = linkFrom[0].getAverageValue(linkFromIndex[0], blur);
        errorIn = linkFrom[0].getValueError(linkFromIndex[0], blur);
        if (errorIn == 0.0) {
            value = Math.exp(valueIn);
            error = 0.0;
            return value;
        }
        value = (Math.exp(valueIn + errorIn) - Math.exp(valueIn - errorIn)) / (2.0 * errorIn);
        error = errorIn * value;
        return value;
    }

    public double getValueError(int which, double blur) {
        if (!valueOk || blur != lastBlur) getAverageValue(which, blur);
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
        gradient.scale(valueIn);
        grad.set(gradient);
    }
}
