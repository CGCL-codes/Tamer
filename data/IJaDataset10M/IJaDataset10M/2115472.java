package aino.arch.P14;

public class Literal extends Constant implements aino.arch.Literal {

    public static final byte[] Init(int init, int size) {
        switch(size) {
            case 1:
                return new byte[] { (byte) init };
            case 2:
                return new byte[] { (byte) init };
            default:
                throw new Error(String.valueOf(size));
        }
    }

    public static final byte[] Init(Object value) {
        if (null == value) return new byte[] { 0 }; else if (value instanceof String) try {
            return ((String) value).getBytes("US-ASCII");
        } catch (java.io.UnsupportedEncodingException exc) {
            throw new InternalError(exc.toString());
        } else if (value instanceof Number) {
            if (value instanceof Byte) {
                return Bits1(((Long) value).intValue());
            } else if (value instanceof Short) {
                return Bits1(((Long) value).intValue());
            } else if (value instanceof Integer) {
                return Bits1(((Long) value).intValue());
            } else if (value instanceof Long) {
                return Bits2(((Long) value).intValue());
            } else if (value instanceof Float) return BitsFp4(Float.floatToIntBits(((Float) value).floatValue())); else return BitsFp8(Double.doubleToLongBits(((Double) value).doubleValue()));
        } else throw new Error(value.getClass().getName());
    }

    public static final String Label(int init, int size) {
        return String.format("Literal_%d_0x%x", size, init);
    }

    public static final String Label(Object init) {
        return Label(Init(init));
    }

    public static final String Label(byte[] init) {
        if (null != init) return Label(ID(init), init.length); else throw new Error();
    }

    public Literal(int init) {
        this(init, 1);
    }

    public Literal(int init, int size) {
        super(Label(init, size), Init(init, size));
    }

    public Literal(String label, int init, int size) {
        super(label, Init(init, size));
    }

    public Literal(String label, Object value) {
        this(label, Init(value));
    }

    private Literal(String label, byte[] init) {
        super(label, init);
    }

    public int literalValue() {
        if (1 == this.size && null != this.init) return (this.init[0] & 0xFF); else return -1;
    }

    public static final Literal[] Add(Literal[] list, Literal item) {
        if (null == item) return list; else if (null == list) return new Literal[] { item }; else {
            final int count = list.length;
            Literal[] copier = new Literal[count + 1];
            System.arraycopy(list, 0, copier, 0, count);
            copier[count] = item;
            return copier;
        }
    }

    public static final int IndexOf(Literal[] list, String label) {
        if (null == list) return -1; else {
            final int count = list.length;
            for (int cc = 0; cc < count; cc++) {
                Literal c = list[cc];
                if (label.equals(c.label)) return cc;
            }
            return -1;
        }
    }
}
