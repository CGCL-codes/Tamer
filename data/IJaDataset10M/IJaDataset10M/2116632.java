package org.sw.wavepane;

import java.awt.Point;
import java.awt.Rectangle;
import org.sw.utils.tools;

/**
 *
 * @author ray
 */
public class WaveGenerator implements WavePaneDataSource {

    double defaultSampleRate = 44100;

    double phase = 1;

    double ph = phase * 2 * Math.PI;

    double A = 0.5;

    float sections;

    /** Creates a new instance of WaveGenerator */
    public WaveGenerator(float sections) {
        this.sections = sections;
    }

    public Points getPoints(Rectangle rect, double time, double time_begin, int amp) {
        int pct = (int) (defaultSampleRate * time);
        Points p = new Points(pct <= rect.width);
        double[] value = new double[pct];
        for (int i = 0; i < pct; i++) {
            value[i] = 1 - A * Math.cos((i / defaultSampleRate + time_begin) * ph);
        }
        if (p.isSparsity()) {
            for (int i = 0; i < pct; i++) p.add(new Point(i, (int) (amp * value[i])));
        } else {
            int len = pct / rect.width;
            for (int i = 0; i < rect.width; i++) {
                Point.Double pd = tools.getMMPoint(value, i * len, len);
                p.add(new Point((int) (pd.x * amp), (int) (pd.y * amp)));
            }
        }
        return p;
    }

    public double getSeconds() {
        return (double) sections;
    }
}
