package org.nodal.util;

import java.util.Vector;
import org.nodal.Types;
import org.nodal.model.*;

/**
 * A utility class for providing the ShortSeq interface to a variety of
 * sources.
 * @author Lee Iverson <leei@ece.ubc.ca>
 */
public final class ShortSeqUtil {

    /**
   * Create an ShortSeq interface for an arbitrary Object.
   */
    public static final ShortSeq create(Object v) throws ConstraintFailure {
        if (v == null || v instanceof ShortSeq) {
            return (ShortSeq) v;
        } else if (v instanceof short[]) {
            return create((short[]) v);
        } else if (v instanceof Vector) {
            return create((Vector) v);
        } else if (v instanceof Number) {
            return create((Number) v);
        }
        throw new ConstraintFailure("not usable as ShortSeq");
    }

    /**
   * Create an ShortSeq interface for an short[] array.
   */
    public static final ShortSeq create(short[] a) {
        return new ArraySeq(a);
    }

    public static final ShortSeq create(short[] a, int start) {
        return new ArraySeq(a, start);
    }

    public static final ShortSeq create(short[] a, int start, int end) {
        return new ArraySeq(a, start, end);
    }

    abstract static class NumSeq extends IntSeqUtil.NumSeq implements ShortSeq {

        public final short[] asShortArray() {
            short[] arr = new short[size()];
            for (int i = 0; i < size(); ++i) {
                arr[i] = shortAt(i);
            }
            return arr;
        }
    }

    /**
   * Class implementing the conversion of shortAt output to Object.
   */
    private abstract static class BaseSeq extends NumSeq {

        public final Seq.SeqType type() {
            return Seq.SeqType.SHORT;
        }

        public final Object elementAt(int i) {
            return new Short(shortAt(i));
        }

        public final void appendElementAt(StringBuffer str, int i) {
            str.append(shortAt(i));
        }

        public abstract short shortAt(int i);

        public final int intAt(int i) {
            return shortAt(i);
        }

        public final long longAt(int i) {
            return shortAt(i);
        }

        public final float floatAt(int i) {
            return shortAt(i);
        }

        public final double doubleAt(int i) {
            return shortAt(i);
        }
    }

    ;

    /**
   * Class implementing ShortSeq wrapper around short[] array.
   */
    private static final class ArraySeq extends BaseSeq implements ShortSeq {

        private final int st;

        private final int len;

        private final short[] arr;

        ArraySeq(short[] a) {
            arr = a;
            st = 0;
            len = a.length;
        }

        ArraySeq(short[] a, int start) {
            arr = a;
            st = start;
            len = a.length - st;
        }

        ArraySeq(short[] a, int start, int end) {
            arr = a;
            st = start;
            len = end - st + 1;
        }

        public final int size() {
            return len;
        }

        public final short shortAt(int i) {
            return arr[st + i];
        }
    }

    /**
   * Create an ShortSeq interface for a Vector.
   * <p>Tested on creation for validity.</p>
   */
    public static final ShortSeq create(Vector v) throws TypeConstraintFailure {
        for (int i = 0; i < v.size(); ++i) {
            Object ch = v.elementAt(i);
            checkType(ch);
        }
        return new VectorSeq(v);
    }

    /**
   * Class implementing ShortSeq wrapper around Vector.
   */
    private static final class VectorSeq extends BaseSeq implements ShortSeq {

        private final Vector vec;

        VectorSeq(Vector v) {
            vec = v;
        }

        public final int size() {
            return vec.size();
        }

        public final short shortAt(int i) {
            Object ch = vec.elementAt(i);
            return ((Number) ch).shortValue();
        }
    }

    /**
   * Create a ShortSeq interface for a single short..
   */
    public static final ShortSeq create(short i) {
        return new BareShortSeq(i);
    }

    /**
   * Class implementing ShortSeq wrapper around bare short (sequence of one).
   */
    private static final class BareShortSeq extends BaseSeq implements ShortSeq {

        private final short val;

        BareShortSeq(short a) {
            val = a;
        }

        public final int size() {
            return 1;
        }

        public final short shortAt(int i) {
            if (i != 0) {
                throw new ArrayIndexOutOfBoundsException(i);
            }
            return val;
        }
    }

    /**
   * Create a ShortSeq interface wrapper around Number (sequence of one).
   */
    public static final ShortSeq create(Number n) throws TypeConstraintFailure {
        checkType(n);
        return new BareShortSeq(n.shortValue());
    }

    /**
   * Test whether Object can be used as a Short.
   * @throws TypeConstraintFailure
   */
    private static final void checkType(Object v) throws TypeConstraintFailure {
        if (Types.SHORT.accepts(v)) {
            return;
        }
        throw new TypeConstraintFailure(v, Types.SHORT, "not usable as short");
    }
}

;
