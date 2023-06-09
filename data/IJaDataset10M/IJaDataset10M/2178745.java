package artofillusion.procedural;

import artofillusion.math.*;
import artofillusion.ui.*;
import java.awt.*;

public class BlendModule extends Module {

    RGBColor blendColor;

    boolean colorOk;

    double lastBlur;

    public BlendModule(Point position) {
        super("Blend", new IOPort[] { new IOPort(IOPort.COLOR, IOPort.INPUT, IOPort.TOP, new String[] { "Color 1", '(' + Translate.text("black") + ')' }), new IOPort(IOPort.COLOR, IOPort.INPUT, IOPort.BOTTOM, new String[] { "Color 2", '(' + Translate.text("white") + ')' }), new IOPort(IOPort.NUMBER, IOPort.INPUT, IOPort.LEFT, new String[] { "Fraction", "(0)" }) }, new IOPort[] { new IOPort(IOPort.COLOR, IOPort.OUTPUT, IOPort.RIGHT, new String[] { "Blend" }) }, position);
        blendColor = new RGBColor(0.0f, 0.0f, 0.0f);
    }

    public void init(PointInfo p) {
        colorOk = false;
    }

    public void getColor(int which, RGBColor c, double blur) {
        if (colorOk && blur == lastBlur) {
            c.copy(blendColor);
            return;
        }
        colorOk = true;
        lastBlur = blur;
        double fract = (linkFrom[2] == null) ? 0.0 : linkFrom[2].getAverageValue(linkFromIndex[2], blur);
        double error = (linkFrom[2] == null) ? 0.0 : linkFrom[2].getValueError(linkFromIndex[2], blur);
        double min = fract - error, max = fract + error;
        if (min < 1.0 && max > 0.0) if (min < 0.0 || max > 1.0) {
            fract = 0.0;
            if (min < 0.0) min = 0.0;
            if (max > 1.0) {
                fract = max - 1.0;
                max = 1.0;
            }
            fract += 0.5 * (max + min) * (max - min);
            fract /= 2.0 * error;
        }
        if (fract < 1.0) {
            if (linkFrom[0] == null) blendColor.setRGB(0.0f, 0.0f, 0.0f); else linkFrom[0].getColor(linkFromIndex[0], blendColor, blur);
        }
        if (fract > 0.0) {
            if (linkFrom[1] == null) c.setRGB(1.0f, 1.0f, 1.0f); else linkFrom[1].getColor(linkFromIndex[1], c, blur);
        }
        if (fract <= 0.0) {
            c.copy(blendColor);
            return;
        }
        if (fract >= 1.0) {
            blendColor.copy(c);
            return;
        }
        blendColor.scale((float) (1.0 - fract));
        c.scale((float) fract);
        blendColor.add(c);
        c.copy(blendColor);
    }
}
