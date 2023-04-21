package tit.observation.behaviour;

import tit.configuration.Configurations;
import tit.observation.Observation;
import tit.observation.ObservationInIllegalStateException;
import tit.observation.ObservationsInWrongOrderException;

/**
 * Base-class for all kinds of behaviour.
 * @author Bart Sas
 */
public abstract class Behaviour implements Comparable<Behaviour> {

    /**
     * The time on which this behaviour was recorded.
     */
    private long time;

    /**
     * Reproduces this behaviour on <code>observation</code>.
     * @param observation The <code>Observation</code> on which this behaviour is reproduced.
     * @throws ObservationInIllegalStateException Can be thrown by the observation.
     * @throws ObservationsInWrongOrderException Can be thrown by the observation.
     */
    public abstract void reproduceBehaviour(Observation observation) throws ObservationInIllegalStateException, ObservationsInWrongOrderException;

    /**
     * Constructs a new <code>Behaviour</code>-object.
     * @param timeinit The time on wich this behaviour is recorded.
     */
    public Behaviour(long timeinit) {
        time = timeinit;
    }

    /**
     * Constructs a new <code>Behaviour</code>-object.
     * @param taketimefrom The <code>Bahaviour</code>-object from which this behaviour's time is taken.
     */
    public Behaviour(Behaviour taketimefrom) {
        time = taketimefrom.time;
    }

    /**
     * Sets the time;
     * @param newtime The new time.
     */
    public void setTime(long newtime) {
        time = newtime;
    }

    /**
     * Gets the time.
     * @return The time.
     */
    public long getTime() {
        return time;
    }

    /**
     * Compares this object with <code>other</code>.
     * @param other The <code>Behaviour</code> to which this <code>Behaviour</code> is compared. 
     * @return -1 if <code>this.time</code> &lt; <code>other.time</code>, +1 if <code>this.time</code> &gt; <code>other.time</code> and 0 if <code>this.time</code> == <code>other.time</code>.
     */
    public int compareTo(Behaviour other) {
        return time < other.time ? -1 : time > other.time ? +1 : 0;
    }

    /**
     * Checks whether this <code>Behaviour</code> equals another.
     * @param other The <code>Objsect</code> to which this object is compared to.
     * @return <code>true</code> if this <code>Behaviour</code> equals <code>other</code>, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        try {
            return time == ((Behaviour) other).time;
        } catch (ClassCastException exception) {
            return false;
        }
    }

    /**
     * Forces this class's subclasses to overrhide the <code>toString()</code>-method of <code>Object</code>.
     * @return The name of this behaviour.
     */
    public abstract String toString();

    /**
     * Creates a new behaviour.
     * @param nameorcode The name or code of the behaviour.
     * @param taketimefrom The <code>Behaviour</code> from which the new behaviour's name is taken.
     * @return The new behaviour.
     */
    public static Behaviour createBehaviour(String nameorcode, Behaviour taketimefrom) {
        BehaviourCodes codes = Configurations.getBehaviourCodes();
        if (nameorcode.equalsIgnoreCase(Character.toString(codes.getFlyCode())) || nameorcode.equalsIgnoreCase(Fly.NAME)) {
            return new Fly(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getHopCode())) || nameorcode.equalsIgnoreCase(Hop.NAME)) {
            return new Hop(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getPreenCode())) || nameorcode.equalsIgnoreCase(Preen.NAME)) {
            return new Preen(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getBillwipeCode())) || nameorcode.equalsIgnoreCase(Billwipe.NAME)) {
            return new Billwipe(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getBoinkCode())) || nameorcode.equalsIgnoreCase(Boink.NAME)) {
            return new Boink(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getPeckRingCode())) || nameorcode.equalsIgnoreCase(PeckRing.NAME)) {
            return new PeckRing(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getTree1Code())) || nameorcode.equalsIgnoreCase(Tree1.NAME)) {
            return new Tree1(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getTree2Code())) || nameorcode.equalsIgnoreCase(Tree2.NAME)) {
            return new Tree2(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getTree3Code())) || nameorcode.equalsIgnoreCase(Tree3.NAME)) {
            return new Tree3(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getTree4Code())) || nameorcode.equalsIgnoreCase(Tree4.NAME)) {
            return new Tree4(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getTree5Code())) || nameorcode.equalsIgnoreCase(Tree5.NAME)) {
            return new Tree5(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getWall1Code())) || nameorcode.equalsIgnoreCase(Wall1.NAME)) {
            return new Wall1(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getWall2Code())) || nameorcode.equalsIgnoreCase(Wall2.NAME)) {
            return new Wall2(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getWall3Code())) || nameorcode.equalsIgnoreCase(Wall3.NAME)) {
            return new Wall3(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getWall4Code())) || nameorcode.equalsIgnoreCase(Wall4.NAME)) {
            return new Wall4(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getFloorCode())) || nameorcode.equalsIgnoreCase(Floor.NAME)) {
            return new Floor(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getDoorOrWindowCode())) || nameorcode.equalsIgnoreCase(DoorOrWindow.NAME)) {
            return new DoorOrWindow(taketimefrom);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getOtherLocationCode())) || nameorcode.equalsIgnoreCase(OtherLocation.NAME)) {
            return new OtherLocation(taketimefrom);
        }
        throw new IllegalArgumentException("Invalid name or code");
    }

    /**
     * Creates a new behaviour.
     * @param nameorcode The name or code of the behaviour.
     * @param time The time on which the behaviour is recorded.
     * @return The new behaviour.
     */
    public static Behaviour createBehaviour(String nameorcode, long time) {
        BehaviourCodes codes = Configurations.getBehaviourCodes();
        if (nameorcode.equalsIgnoreCase(Character.toString(codes.getFlyCode())) || nameorcode.equalsIgnoreCase(Fly.NAME)) {
            return new Fly(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getHopCode())) || nameorcode.equalsIgnoreCase(Hop.NAME)) {
            return new Hop(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getPreenCode())) || nameorcode.equalsIgnoreCase(Preen.NAME)) {
            return new Preen(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getBillwipeCode())) || nameorcode.equalsIgnoreCase(Billwipe.NAME)) {
            return new Billwipe(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getBoinkCode())) || nameorcode.equalsIgnoreCase(Boink.NAME)) {
            return new Boink(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getPeckRingCode())) || nameorcode.equalsIgnoreCase(PeckRing.NAME)) {
            return new PeckRing(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getTree1Code())) || nameorcode.equalsIgnoreCase(Tree1.NAME)) {
            return new Tree1(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getTree2Code())) || nameorcode.equalsIgnoreCase(Tree2.NAME)) {
            return new Tree2(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getTree3Code())) || nameorcode.equalsIgnoreCase(Tree3.NAME)) {
            return new Tree3(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getTree4Code())) || nameorcode.equalsIgnoreCase(Tree4.NAME)) {
            return new Tree4(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getTree5Code())) || nameorcode.equalsIgnoreCase(Tree5.NAME)) {
            return new Tree5(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getWall1Code())) || nameorcode.equalsIgnoreCase(Wall1.NAME)) {
            return new Wall1(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getWall2Code())) || nameorcode.equalsIgnoreCase(Wall2.NAME)) {
            return new Wall2(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getWall3Code())) || nameorcode.equalsIgnoreCase(Wall3.NAME)) {
            return new Wall3(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getWall4Code())) || nameorcode.equalsIgnoreCase(Wall4.NAME)) {
            return new Wall4(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getFloorCode())) || nameorcode.equalsIgnoreCase(Floor.NAME)) {
            return new Floor(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getDoorOrWindowCode())) || nameorcode.equalsIgnoreCase(DoorOrWindow.NAME)) {
            return new DoorOrWindow(time);
        } else if (nameorcode.equalsIgnoreCase(Character.toString(codes.getOtherLocationCode())) || nameorcode.equalsIgnoreCase(OtherLocation.NAME)) {
            return new OtherLocation(time);
        }
        throw new IllegalArgumentException("Invalid name or code");
    }
}
