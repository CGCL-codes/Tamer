package edu.udo.scaffoldhunter.view.plot;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Michael Hesse
 * 
 * this class uses BigInteger to get rid of rounding errors
 * (a number formatter might have the same effect..)
 * 
 */
public class SimpleTickStrategy implements TickStrategy {

    @Override
    public double[] getTicks(double min, double max) {
        double[] tickPosition;
        double delta;
        double minTick, maxTick, deltaTick;
        if (min > max) {
            double t = min;
            min = max;
            max = t;
        }
        if (min == max) {
            tickPosition = new double[1];
            tickPosition[0] = min;
            return tickPosition;
        }
        delta = max - min;
        {
            int p = (int) Math.log10(delta);
            deltaTick = Math.pow(10, p);
            while ((delta / deltaTick) < 2) deltaTick /= 10;
        }
        {
            int d = (int) (min / deltaTick);
            BigDecimal _min = BigDecimal.valueOf(d);
            BigDecimal _deltaTick = BigDecimal.valueOf(deltaTick);
            _min = _min.multiply(_deltaTick);
            while (_min.doubleValue() > min) _min = _min.subtract(_deltaTick);
            while (_min.doubleValue() < min) _min = _min.add(_deltaTick);
            minTick = _min.doubleValue();
        }
        {
            int d = (int) (max / deltaTick);
            BigDecimal _max = BigDecimal.valueOf(d);
            BigDecimal _deltaTick = BigDecimal.valueOf(deltaTick);
            _max = _max.multiply(_deltaTick);
            while (_max.doubleValue() < max) _max = _max.add(_deltaTick);
            while (_max.doubleValue() > max) _max = _max.subtract(_deltaTick);
            maxTick = _max.doubleValue();
        }
        {
            BigDecimal _tick = BigDecimal.valueOf(minTick);
            BigDecimal _deltaTick = BigDecimal.valueOf(deltaTick);
            ArrayList<Double> _tickPosition = new ArrayList<Double>();
            do {
                _tickPosition.add(_tick.doubleValue());
                _tick = _tick.add(_deltaTick);
            } while (_tick.doubleValue() <= maxTick);
            tickPosition = new double[_tickPosition.size()];
            for (int i = 0; i < _tickPosition.size(); i++) tickPosition[i] = _tickPosition.get(i);
        }
        return tickPosition;
    }

    @Override
    public double[] suggestedInterval(double min, double max) {
        double[] suggestedMinMax = { 0, 10 };
        double delta = max - min;
        double deltaTick;
        if (delta == 0) {
            min -= 0.05;
            max += 0.05;
            delta = 0.1;
        }
        {
            int p = (int) Math.log10(delta);
            deltaTick = Math.pow(10, p);
            while ((delta / deltaTick) < 2) deltaTick /= 10;
        }
        {
            int d = (int) (min / deltaTick);
            BigDecimal _min = BigDecimal.valueOf(d);
            BigDecimal _deltaTick = BigDecimal.valueOf(deltaTick);
            _min = _min.multiply(_deltaTick);
            while (_min.doubleValue() > min) _min = _min.subtract(_deltaTick);
            suggestedMinMax[0] = _min.doubleValue();
        }
        {
            int d = (int) (max / deltaTick);
            BigDecimal _max = BigDecimal.valueOf(d);
            BigDecimal _deltaTick = BigDecimal.valueOf(deltaTick);
            _max = _max.multiply(_deltaTick);
            while (_max.doubleValue() < max) _max = _max.add(_deltaTick);
            suggestedMinMax[1] = _max.doubleValue();
        }
        return suggestedMinMax;
    }
}
