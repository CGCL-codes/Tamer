package com.ibm.tuningfork.infra.data;

import java.nio.ByteBuffer;
import com.ibm.tuningfork.infra.event.AttributeType;
import com.ibm.tuningfork.infra.event.EventAttribute;
import com.ibm.tuningfork.infra.event.TupleIndexOutOfBoundsException;

public class SliceInfo extends Tuple {

    /** This class's TupleType */
    public static final SliceInfoTupleType eventType = new SliceInfoTupleType();

    public final int address;

    public final int color;

    public final int intensity;

    public final Annotation annotation;

    public SliceInfo(int a, int c, int i) {
        this(a, c, i, null);
    }

    public SliceInfo(int a, int c, int i, Annotation annotation) {
        this.address = a;
        this.color = c;
        this.intensity = i;
        this.annotation = annotation;
    }

    public int getInt(int attribute) {
        if (attribute == 0) {
            return address;
        } else if (attribute == 1) {
            return color;
        } else if (attribute == 2) {
            return intensity;
        } else {
            throw new TupleIndexOutOfBoundsException(attribute, AttributeType.DOUBLE);
        }
    }

    public int numIntegers() {
        return 3;
    }

    public Annotation getRelation(int attribute) {
        if (attribute == 0) {
            return annotation;
        } else {
            throw new TupleIndexOutOfBoundsException(attribute, AttributeType.RELATION);
        }
    }

    public int numRelations() {
        return 1;
    }

    /**
     * The TupleType for this class
     */
    public static class SliceInfoTupleType extends StaticTupleType {

        /**
         * Create a new SliceInfoTupleType
         */
        public SliceInfoTupleType() {
            super("SliceInfo", "SliceInfo", new EventAttribute[] { new EventAttribute("address", "address", AttributeType.INT), new EventAttribute("color", "color", AttributeType.INT), new EventAttribute("intensity", "intensity", AttributeType.INT), new EventAttribute("annotation", "annotation", Annotation.eventType) });
        }

        public ITuple newInstance(Long time, int[] ints, long[] longs, double[] doubles, String[] strings, boolean[] booleans, IRelation[] relations) {
            return new SliceInfo(ints[0], ints[1], ints[2], (Annotation) relations[0]);
        }

        public ITuple read(ByteBuffer buffer) {
            return new SliceInfo(buffer.getInt(), buffer.getInt(), buffer.getInt(), new Annotation(buffer));
        }
    }
}
