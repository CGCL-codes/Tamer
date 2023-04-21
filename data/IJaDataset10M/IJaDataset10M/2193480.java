package tit.observation.behaviour;

import tit.observation.Observation;
import tit.observation.ObservationInIllegalStateException;
import tit.observation.ObservationsInWrongOrderException;

/**
 * Wall3-class.
 * @author Bart Sas
 */
public class Wall3 extends Behaviour {

    /**
     * This behaviour's name.
     */
    public static final String NAME = "Wall3";

    /**
     * Constructs a new <code>Wall3</code>-object.
     * @param time The time on wich this behaviour is recorded.
     */
    public Wall3(long time) {
        super(time);
    }

    /**
     * Constructs a new <code>Wall3</code>-object.
     * @param taketimefrom The <code>Bahaviour</code>-object from which this behaviour's time is taken.
     */
    public Wall3(Behaviour taketimefrom) {
        super(taketimefrom);
    }

    /**
     * Reproduces this behaviour on <code>observation</code>.
     * @param observation The <code>Observation</code> on which this behaviour is reproduced.
     * @throws ObservationInIllegalStateException Can be thrown by the observation.
     * @throws ObservationsInWrongOrderException Can be thrown by the observation.
     */
    public void reproduceBehaviour(Observation observation) throws ObservationInIllegalStateException, ObservationsInWrongOrderException {
        observation.recordWall3(getTime());
    }

    /**
     * Checks whether this <code>Wall3</code> equals another <code>Object</code>.
     * @param other The <code>Objsect</code> to which this object is compared to.
     * @return <code>true</code> if this <code>Behaviour</code> equals <code>other</code>, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        return other instanceof Wall3 && super.equals(other);
    }

    /**
     * Returns this behaviour's name.
     * @return This behaviour's name.
     */
    public String toString() {
        return NAME;
    }
}
