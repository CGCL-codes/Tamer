package edu.vub.at.trace;

import java.io.IOException;
import java.io.Serializable;

/**
 * A marker for a point in an event loop turn where an event originated.
 */
public class Anchor implements Comparable, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * event loop turn in which the event originated
     */
    public final Turn turn;

    /**
     * intra-{@linkplain #turn turn} event number
     */
    public final long number;

    /**
     * Constructs an instance.
     * @param turn      {@link #turn}
     * @param number    {@link #number}
     */
    public Anchor(final Turn turn, final long number) {
        this.turn = turn;
        this.number = number;
    }

    public boolean equals(final Object o) {
        return null != o && Anchor.class == o.getClass() && number == ((Anchor) o).number && (null != turn ? turn.equals(((Anchor) o).turn) : null == ((Anchor) o).turn);
    }

    public int hashCode() {
        return (null != turn ? turn.hashCode() : 0) + (int) (number ^ (number >>> 32)) + 0x7C42A2C4;
    }

    public int compareTo(final Object o) {
        if (!(o instanceof Anchor)) {
            throw new IllegalArgumentException();
        }
        ;
        final int major = turn.compareTo(((Anchor) o).turn);
        if (0 != major) {
            return major;
        }
        final long minor = number - ((Anchor) o).number;
        return minor < 0L ? -1 : minor == 0L ? 0 : 1;
    }

    /**
     * "anchor" : {
     *   "number" : n,
     *   "turn" : {
     *     "loop" : "event-loop-name",
     *     "number" : n
     *   }
     * }
     */
    public void toJSON(JSONWriter json) throws IOException {
        JSONWriter.ObjectWriter anchor = json.startObject();
        anchor.startMember("number").writeLong(number);
        turn.toJSON(anchor.startMember("turn"));
        anchor.finish();
    }
}
