package ircam.jmax.editors.sequence;

import ircam.jmax.editors.sequence.track.*;

/**
 * a mapper into the velocity value of the SequenceEvents
 */
public class VelocityMapper extends DoubleMapper {

    /**
   * set the given velocity in the given event
   */
    public void set(Event e, double value) {
        e.setProperty("velocity", new Double(value));
    }

    /**
   * get the velocity of the given event
   */
    public double get(Event e) {
        return ((Double) e.getProperty("velocity")).doubleValue();
    }

    public String getName() {
        return "velocity";
    }

    public static DoubleMapper getMapper() {
        return itsVelocityMapper;
    }

    static VelocityMapper itsVelocityMapper = new VelocityMapper();
}
