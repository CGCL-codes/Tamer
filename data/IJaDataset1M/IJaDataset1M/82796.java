package ca.gc.drdc_rddc.atlantic.rpr;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import ca.gc.drdc_rddc.atlantic.dmso.TimeFactory;
import ca.gc.drdc_rddc.atlantic.dmso.TimeIntervalFactory;
import ca.gc.drdc_rddc.atlantic.hla.HLAArrayCodec;
import ca.gc.drdc_rddc.atlantic.hla.HLACodec;
import ca.gc.drdc_rddc.atlantic.hla.HLACodecBase;
import ca.gc.drdc_rddc.atlantic.hla.HLACodecFactory;
import ca.gc.drdc_rddc.atlantic.hla.HLAModel;
import ca.gc.drdc_rddc.atlantic.hla.HLATimeManager;
import ca.gc.drdc_rddc.atlantic.hla.TimeManager;
import ca.gc.drdc_rddc.atlantic.hla.UserCodecs;

/**
 * Codecs and array encodings specific to RPR2.
 * 
 * @author dillman
 * 
 */
public class RPR2Codecs implements UserCodecs {

    public class UnsignedInteger16BE extends HLACodecBase {

        public boolean isValid(Object p_obj) {
            if (p_obj == null) {
                throw new IllegalArgumentException("null value");
            }
            if (!(p_obj instanceof Number)) {
                throw new IllegalArgumentException("not a subclass of Number");
            }
            return true;
        }

        public HLACodec resolve() {
            return this;
        }

        public Object unmarshall(ByteBuffer bb) {
            bb.order(ByteOrder.BIG_ENDIAN);
            return new Short(bb.getShort());
        }

        public void marshall(Object obj, ByteBuffer bb) {
            bb.order(ByteOrder.BIG_ENDIAN);
            bb.putShort(((Number) obj).shortValue());
        }

        public int getRawLength(Object obj) {
            return 2;
        }

        public Class getResultClass() {
            return Short.class;
        }

        public Object read(StreamTokenizer tok) throws IOException {
            int tt = tok.nextToken();
            return new Short((short) tok.nval);
        }
    }

    public class UnsignedInteger16LE extends HLACodecBase {

        public boolean isValid(Object p_obj) {
            if (p_obj == null) {
                throw new IllegalArgumentException("null value");
            }
            if (!(p_obj instanceof Number)) {
                throw new IllegalArgumentException("not a subclass of Number");
            }
            return true;
        }

        public HLACodec resolve() {
            return this;
        }

        public Object unmarshall(ByteBuffer bb) {
            bb.order(ByteOrder.LITTLE_ENDIAN);
            return new Short(bb.getShort());
        }

        public void marshall(Object obj, ByteBuffer bb) {
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putShort(((Number) obj).shortValue());
        }

        public int getRawLength(Object obj) {
            return 2;
        }

        public Class getResultClass() {
            return Short.class;
        }

        public Object read(StreamTokenizer tok) throws IOException {
            int tt = tok.nextToken();
            return new Short((short) tok.nval);
        }
    }

    public class UnsignedInteger32BE extends HLACodecBase {

        public boolean isValid(Object p_obj) {
            if (p_obj == null) {
                throw new IllegalArgumentException("null value");
            }
            if (!(p_obj instanceof Number)) {
                throw new IllegalArgumentException("not a subclass of Number");
            }
            return true;
        }

        public HLACodec resolve() {
            return this;
        }

        public Object unmarshall(ByteBuffer bb) {
            bb.order(ByteOrder.BIG_ENDIAN);
            return new Integer(bb.getInt());
        }

        public void marshall(Object obj, ByteBuffer bb) {
            bb.order(ByteOrder.BIG_ENDIAN);
            bb.putInt(((Number) obj).intValue());
        }

        public int getRawLength(Object obj) {
            return 4;
        }

        public Class getResultClass() {
            return Integer.class;
        }

        public Object read(StreamTokenizer tok) throws IOException {
            int tt = tok.nextToken();
            return new Integer((int) tok.nval);
        }
    }

    public class UnsignedInteger32LE extends HLACodecBase {

        public boolean isValid(Object p_obj) {
            if (p_obj == null) {
                throw new IllegalArgumentException("null value");
            }
            if (!(p_obj instanceof Number)) {
                throw new IllegalArgumentException("not a subclass of Number");
            }
            return true;
        }

        public HLACodec resolve() {
            return this;
        }

        public Object unmarshall(ByteBuffer bb) {
            bb.order(ByteOrder.LITTLE_ENDIAN);
            return new Integer(bb.getInt());
        }

        public void marshall(Object obj, ByteBuffer bb) {
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putInt(((Number) obj).intValue());
        }

        public int getRawLength(Object obj) {
            return 4;
        }

        public Class getResultClass() {
            return Integer.class;
        }

        public Object read(StreamTokenizer tok) throws IOException {
            int tt = tok.nextToken();
            return new Integer((int) tok.nval);
        }
    }

    public class UnsignedInteger64BE extends HLACodecBase {

        public boolean isValid(Object p_obj) {
            if (p_obj == null) {
                throw new IllegalArgumentException("null value");
            }
            if (!(p_obj instanceof Number)) {
                throw new IllegalArgumentException("not a subclass of Number");
            }
            return true;
        }

        public HLACodec resolve() {
            return this;
        }

        public Object unmarshall(ByteBuffer bb) {
            bb.order(ByteOrder.BIG_ENDIAN);
            return new Long(bb.getLong());
        }

        public void marshall(Object obj, ByteBuffer bb) {
            bb.order(ByteOrder.BIG_ENDIAN);
            bb.putLong(((Number) obj).longValue());
        }

        public int getRawLength(Object obj) {
            return 8;
        }

        public Class getResultClass() {
            return Long.class;
        }

        public Object read(StreamTokenizer tok) throws IOException {
            int tt = tok.nextToken();
            return new Long((long) tok.nval);
        }
    }

    public class UnsignedInteger64LE extends HLACodecBase {

        public boolean isValid(Object p_obj) {
            if (p_obj == null) {
                throw new IllegalArgumentException("null value");
            }
            if (!(p_obj instanceof Number)) {
                throw new IllegalArgumentException("not a subclass of Number");
            }
            return true;
        }

        public HLACodec resolve() {
            return this;
        }

        public Object unmarshall(ByteBuffer bb) {
            bb.order(ByteOrder.LITTLE_ENDIAN);
            return new Long(bb.getLong());
        }

        public void marshall(Object obj, ByteBuffer bb) {
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putLong(((Number) obj).longValue());
        }

        public int getRawLength(Object obj) {
            return 8;
        }

        public Class getResultClass() {
            return Long.class;
        }

        public Object read(StreamTokenizer tok) throws IOException {
            int tt = tok.nextToken();
            return new Integer((int) tok.nval);
        }
    }

    /**
	 * HLAbitArray.
	 * 
	 * @author dillman
	 */
    public static class HLAbitArrayCodec extends HLAArrayCodec implements Cloneable {

        static final byte[] bitMask = { 1, 2, 4, 8, 16, 32, 64, -128 };

        /**
		 * Read and decode array data to the end of bb.
		 * 
		 * @param bb
		 *            the bytebuffer to decode.
		 * @return the decoded array.
		 */
        public Object unmarshall(ByteBuffer bb) {
            int len = ((Integer) HLACodecFactory.hlaInteger32BE.unmarshall(bb)).intValue();
            BitSet bs = new BitSet(len);
            int index = 0;
            byte b = 0;
            while (index < len) {
                int pos = index % 8;
                if (pos == 0) {
                    b = bb.get();
                }
                if ((bitMask[pos] & b) == bitMask[pos]) {
                    bs.set(index);
                }
                index++;
            }
            return bs;
        }

        /**
		 * Encode an object into a bytebuffer.
		 * 
		 * @param obj
		 *            the source object to encode.
		 * @param bb
		 *            the bytebuffer to be written.
		 */
        public void marshall(Object obj, ByteBuffer bb) {
            BitSet bs = null;
            int len;
            if (obj instanceof BitSet) {
                bs = (BitSet) obj;
            } else {
                Object[] a = getArray(obj);
                bs = new BitSet(a.length);
                for (int i = 0; i < a.length; i++) {
                    bs.set(i, ((Boolean) a[i]).booleanValue());
                }
            }
            len = bs.size();
            HLACodecFactory.hlaInteger32BE.marshall(new Integer(len), bb);
            byte b = 0;
            boolean flush = false;
            for (int i = 0; i < len; i++) {
                int pos = i % 8;
                if (bs.get(i)) {
                    b |= bitMask[pos];
                    flush = true;
                }
                if (pos == 7) {
                    bb.put(b);
                    flush = false;
                    b = 0;
                }
            }
            if (flush) {
                bb.put(b);
            }
        }

        /**
		 * Find the size of the bytebuffer required to encode a given object.
		 * 
		 * @param obj
		 * @return length of the bytebuffer required to encode the given object.
		 */
        public int getRawLength(Object obj) {
            int rawLen = 4;
            int len;
            if (obj instanceof BitSet) {
                len = ((BitSet) obj).size();
            } else {
                Object[] a = getArray(obj);
                len = a.length;
            }
            rawLen = len / 8;
            if (len % 8 > 0) rawLen++;
            return rawLen;
        }
    }

    /**
	 * An array terminated by a null value. Note that this array could contain
	 * any element type which has a (conceptual?) null value, such as double,
	 * int, etc. Note that if the element type is HLAOctet then this codec
	 * converts between String, not array (i.e. null terminated arrays of
	 * HLAOctet are treated as Strings, not arrays) for simplicity.
	 * 
	 * Null terminated arrays of non-numeric items are not handled (i.e. element
	 * values must derrive from the Number class in java.lang).
	 * 
	 * Mar 16, 2005 1:12:54 PM
	 * 
	 * @author Dillman
	 */
    public static class HLAnullTerminatedArrayCodec extends HLAArrayCodec implements Cloneable {

        /**
		 * Read and decode a null terminated array.
		 * 
		 * @param bb
		 *            the bytebuffer to decode.
		 * @return the decoded array or String.
		 */
        public Object unmarshall(ByteBuffer bb) {
            if (dataType.getResultClass() == Character.class) {
                StringBuffer sb = new StringBuffer();
                while (bb.remaining() > 0) {
                    Character c = (Character) dataType.unmarshall(bb);
                    if (c.charValue() == 0) break;
                    sb.append(c);
                }
                return sb.toString();
            }
            Vector v = new Vector();
            while (bb.remaining() > 0) {
                Number n = (Number) dataType.unmarshall(bb);
                if (n instanceof Double && n.doubleValue() != 0.0) v.add(n);
                if (n instanceof Float && n.floatValue() != 0.0f) v.add(n); else if (n.longValue() != 0) v.add(n); else break;
            }
            return makeNewArray(v);
        }

        /**
		 * Encode the object to a bytebuffer.
		 * 
		 * @param obj
		 *            the object to encode.
		 * @param bb
		 *            bytebuffer to be written.
		 */
        public void marshall(Object obj, ByteBuffer bb) {
            Object[] a = getArray(obj);
            int len = a.length;
            for (int i = 0; i < len; i++) {
                dataType.marshall(a[i], bb);
            }
            if (dataType.getResultClass() == Character.class) {
                dataType.marshall(new Character((char) 0), bb);
            } else {
                dataType.marshall(new Integer(0), bb);
            }
        }

        /**
		 * Get the number of bytes of an encoded object.
		 * 
		 * @param obj
		 *            the object to encode.
		 * @return the number of bytes required to encode obj.
		 */
        public int getRawLength(Object obj) {
            int rawLen = 0;
            Object[] a = getArray(obj);
            int len = a.length;
            for (int i = 0; i < len; i++) {
                rawLen += dataType.getRawLength(a[i]);
            }
            if (dataType.getResultClass() == Character.class) {
                rawLen += dataType.getRawLength(new Character((char) 0));
            } else {
                rawLen += dataType.getRawLength(new Integer(0));
            }
            return rawLen;
        }
    }

    /**
	 * A variable length array terminated by the end of the buffer. Mar 16, 2005
	 * 1:07:46 PM
	 * 
	 * @author Dillman
	 */
    public static class HLAlengthlessVarArrayCodec extends HLAArrayCodec {

        /**
		 * Read and decode array data to the end of bb.
		 * 
		 * @param bb
		 *            the bytebuffer to decode.
		 * @return the decoded array.
		 */
        public Object unmarshall(ByteBuffer bb) {
            if (dataType.getResultClass() == Character.class) {
                StringBuffer sb = new StringBuffer();
                while (bb.remaining() > 0) {
                    sb.append(((Character) dataType.unmarshall(bb)).charValue());
                }
                return sb.toString();
            }
            List l = new LinkedList();
            while (bb.remaining() > 0) {
                l.add(dataType.unmarshall(bb));
            }
            return makeNewArray(l);
        }

        /**
		 * Encode an object into a bytebuffer.
		 * 
		 * @param obj
		 *            the source object to encode.
		 * @param bb
		 *            the bytebuffer to be written.
		 */
        public void marshall(Object obj, ByteBuffer bb) {
            Object[] a = getArray(obj);
            int len = a.length;
            for (int i = 0; i < len; i++) {
                dataType.marshall(a[i], bb);
            }
        }

        /**
		 * Find the size of the bytebuffer required to encode a given object.
		 * 
		 * @param obj
		 * @return length of the bytebuffer required to encode the given object.
		 */
        public int getRawLength(Object obj) {
            int rawLen = 0;
            Object[] a = getArray(obj);
            int len = a.length;
            for (int i = 0; i < len; i++) {
                rawLen += dataType.getRawLength(a[i]);
            }
            return rawLen;
        }
    }

    /**
	 * A variable length array terminated by the end of the buffer. Mar 16, 2005
	 * 1:07:46 PM
	 * 
	 * @author Dillman
	 */
    public static class HLAunpaddedLengthlessVarArrayCodec extends HLAArrayCodec {

        /**
		 * Read and decode array data to the end of bb.
		 * 
		 * @param bb
		 *            the bytebuffer to decode.
		 * @return the decoded array.
		 */
        public Object unmarshall(ByteBuffer bb) {
            if (dataType.getResultClass() == Character.class) {
                StringBuffer sb = new StringBuffer();
                while (bb.remaining() > 0) {
                    sb.append(((Character) dataType.unmarshall(bb)).charValue());
                }
                return sb.toString();
            }
            List l = new LinkedList();
            while (bb.remaining() > 0) {
                l.add(dataType.unmarshall(bb));
            }
            return makeNewArray(l);
        }

        /**
		 * Encode an object into a bytebuffer.
		 * 
		 * @param obj
		 *            the source object to encode.
		 * @param bb
		 *            the bytebuffer to be written.
		 */
        public void marshall(Object obj, ByteBuffer bb) {
            Object[] a = getArray(obj);
            int len = a.length;
            for (int i = 0; i < len; i++) {
                dataType.marshall(a[i], bb);
            }
        }

        /**
		 * Find the size of the bytebuffer required to encode a given object.
		 * 
		 * @param obj
		 * @return length of the bytebuffer required to encode the given object.
		 */
        public int getRawLength(Object obj) {
            int rawLen = 0;
            Object[] a = getArray(obj);
            int len = a.length;
            for (int i = 0; i < len; i++) {
                rawLen += dataType.getRawLength(a[i]);
            }
            return rawLen;
        }
    }

    public static UnsignedInteger16BE unsignedInteger16BE;

    public static UnsignedInteger32BE unsignedInteger32BE;

    public static UnsignedInteger64BE unsignedInteger64BE;

    public static UnsignedInteger16LE unsignedInteger16LE;

    public static UnsignedInteger32LE unsignedInteger32LE;

    public static UnsignedInteger64LE unsignedInteger64LE;

    public RPR2Codecs() {
        unsignedInteger16BE = new UnsignedInteger16BE();
        unsignedInteger32BE = new UnsignedInteger32BE();
        unsignedInteger64BE = new UnsignedInteger64BE();
        unsignedInteger16LE = new UnsignedInteger16LE();
        unsignedInteger32LE = new UnsignedInteger32LE();
        unsignedInteger64LE = new UnsignedInteger64LE();
    }

    /**
	 * Register the specific types into the codec factory.
	 * 
	 * @param factory
	 *            the HLACodecFactory in which to register the specific types.
	 */
    public void registerCodecs(HLAModel model) {
        HLACodecFactory factory = model.getCodecFactory();
        factory.registerCodec("Unsignedinteger16BE", unsignedInteger16BE);
        factory.registerCodec("Unsignedinteger32BE", unsignedInteger32BE);
        factory.registerCodec("Unsignedinteger64BE", unsignedInteger64BE);
        factory.registerCodec("Unsignedinteger16LE", unsignedInteger16LE);
        factory.registerCodec("Unsignedinteger32LE", unsignedInteger32LE);
        factory.registerCodec("Unsignedinteger64LE", unsignedInteger64LE);
        factory.registerEncoding("HLAbitArray", new HLAbitArrayCodec());
        factory.registerEncoding("HLAnullTerminatedArray", new HLAnullTerminatedArrayCodec());
        factory.registerEncoding("HLAlengthlessVarArray", new HLAlengthlessVarArrayCodec());
        factory.registerEncoding("HLAunpaddedLengthlessVarArray", new HLAunpaddedLengthlessVarArrayCodec());
        factory.registerEncoding("HLApaddingTo16Array", new HLAlengthlessVarArrayCodec());
        factory.registerEncoding("HLApaddingTo32Array", new HLAlengthlessVarArrayCodec());
        factory.registerEncoding("HLApaddingTo64Array", new HLAlengthlessVarArrayCodec());
        HLATimeManager tm = new TimeManager(model);
        tm.setTimeFactory(new TimeFactory(0.0, Double.MAX_VALUE));
        tm.setIntervalFactory(new TimeIntervalFactory(1.0e-9));
        model.setTimeManager(tm);
    }
}
