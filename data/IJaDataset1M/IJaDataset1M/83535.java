package com.ibm.tuningfork.infra.event;

import java.nio.ByteBuffer;
import com.ibm.tuningfork.infra.data.IRelation;
import com.ibm.tuningfork.infra.data.ITuple;
import com.ibm.tuningfork.infra.data.TupleFactory;

public class Event_Double extends Event {

    /** This class's TupleFactory */
    static final TupleFactory factory = new Factory();

    protected double value;

    public Event_Double(long time, double value) {
        super(time);
        this.value = value;
    }

    public double getDouble(final int attribute) {
        if (attribute == 0) {
            return value;
        } else {
            throw new TupleIndexOutOfBoundsException(attribute, AttributeType.DOUBLE);
        }
    }

    public int numDoubles() {
        return 1;
    }

    public void write(ByteBuffer buf) {
        buf.putLong(time);
        buf.putDouble(value);
    }

    /**
     * The EventFactory for this kind of Event
     */
    private static final class Factory implements TupleFactory {

        public IEvent newInstance(Long time, int[] ints, long[] longs, double[] doubles, String[] strings, boolean[] booleans, IRelation[] relations) {
            return new Event_Double(time, doubles[0]);
        }

        public ITuple read(ByteBuffer buffer) {
            return new Event_Double(buffer.getLong(), buffer.getDouble());
        }
    }
}
