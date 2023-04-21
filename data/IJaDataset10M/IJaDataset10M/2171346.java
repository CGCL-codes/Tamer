package edu.ucla.loni.LOVE.colormap.plugins;

import edu.ucla.loni.LOVE.colormap.ColorMap;
import java.lang.String;

/**
 * This is a hotmetal colormap
 */
public class HotmetalColorMap extends ColorMap {

    /**
     * Default constructor
     */
    public HotmetalColorMap() {
    }

    /**
     * Constructor
     */
    public HotmetalColorMap(int size, int bits) {
        super(size, bits);
    }

    /**
     * Get the names of the current map
     */
    public String getName() {
        return ("Hotmetal Colormap");
    }

    protected void _setColorMap() {
        double window = _upperLimit - _lowerLimit;
        int red_start = _lowerLimit;
        int red_end = _lowerLimit + (int) (0.5 * window);
        int green_start = _lowerLimit + (int) (0.25 * window + 0.5);
        int green_end = _lowerLimit + (int) (0.75 * window);
        int blue_start = _lowerLimit + (int) (0.5 * window + 0.5);
        int blue_end = _upperLimit;
        _compute_linear_ramp(_lowerLimit, _upperLimit, red_start, red_end, _rMap);
        _compute_linear_ramp(_lowerLimit, _upperLimit, green_start, green_end, _gMap);
        _compute_linear_ramp(_lowerLimit, _upperLimit, blue_start, blue_end, _bMap);
    }

    private static final void _compute_linear_ramp(final int range_min, final int range_max, final int start, final int end, byte[] destination) {
        int i;
        for (i = range_min; i < start; i++) destination[i] = (byte) 0;
        if (start == end) destination[start] = (byte) 127; else {
            final int end_minus_start = end - start;
            for (i = start; i <= end; i++) destination[i] = (byte) (255 * (i - start) / end_minus_start);
        }
        for (i = end + 1; i <= range_max; i++) {
            destination[i] = (byte) 255;
        }
    }
}
