package org.modsl.core.render;

import java.awt.Color;
import java.awt.GradientPaint;
import org.modsl.core.agt.model.Pt;

/**
 * Gradient fill
 * @author avishnyakov
 */
public class Gradient {

    Color startColor, endColor;

    Pt start, end;

    public Gradient(int[] parameters) {
        if (parameters.length == 10) {
            start = new Pt(parameters[0], parameters[1]);
            startColor = new Color(parameters[2], parameters[3], parameters[4]);
            end = new Pt(parameters[5], parameters[6]);
            endColor = new Color(parameters[7], parameters[8], parameters[9]);
        } else {
            start = new Pt(parameters[0], parameters[1]);
            startColor = new Color(parameters[2], parameters[3], parameters[4], parameters[5]);
            end = new Pt(parameters[6], parameters[7]);
            endColor = new Color(parameters[8], parameters[9], parameters[10], parameters[11]);
        }
    }

    /**
     * Return gradient paint calculated from (start, end) percentages (0-100%) for the given box of (pos, size)
     * @param pos
     * @param size
     * @return
     */
    public GradientPaint getGradientPaint(Pt pos, Pt size) {
        int x1 = (int) (pos.x + start.x / 100d * size.x);
        int y1 = (int) (pos.y + start.y / 100d * size.y);
        int x2 = (int) (pos.x + end.x / 100d * size.x);
        int y2 = (int) (pos.y + end.y / 100d * size.y);
        return new GradientPaint(x1, y1, startColor, x2, y2, endColor);
    }
}
