package ircam.jmax.editors.sequence;

import ircam.jmax.editors.sequence.track.*;

/**
 * A simple Mapper that accesses the duration parameter
 */
public class DurationMapper extends DoubleMapper {

    public void set(Event e, double value) {
        e.setProperty("duration", new Double(value));
    }

    public double get(Event e) {
        return ((Double) e.getProperty("duration")).doubleValue();
    }

    public String getName() {
        return "duration";
    }

    /**
     * access the static instance
     */
    static DoubleMapper getMapper() {
        if (itsDurationMapper == null) itsDurationMapper = new DurationMapper();
        return itsDurationMapper;
    }

    static DurationMapper itsDurationMapper;
}
