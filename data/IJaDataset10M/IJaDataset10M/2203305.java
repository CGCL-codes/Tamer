package ca.gc.drdc_rddc.atlantic.hla;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * A base class for array codecs. Mar 16, 2005 1:07:20 PM
 * 
 * @author Dillman
 */
public abstract class HLAArrayCodec extends HLACodecBase implements Cloneable {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(HLAArrayCodec.class);

    /** Cardinality of this array dimension. */
    protected HLAArrayCardinality card;

    /** dataType of array elements. */
    protected HLACodec dataType;

    /**
	 * Create a clone of an array codec from the encoding registration map, then
	 * set a specific cardinality and dataType in the cloned object.
	 * 
	 * @return a clone of this object.
	 * @throws CloneNotSupportedException
	 */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
	 * Get the number of array elements represented in an object. This function
	 * allows a user to pass in Strings, Lists and Arrays flexibly and the
	 * internal code treats them all as arrays 9as appropriate). A String may be
	 * converted to an array of Character if the array element codec returns
	 * class Character. A List is converted to an array.
	 * 
	 * @param obj
	 *            the obj containing the array elements.
	 * @return an array representing obj.
	 */
    public Object[] getArray(Object obj) {
        Object[] a = null;
        if (obj instanceof String && dataType.getResultClass() == Character.class) {
            String str = (String) obj;
            a = new Character[str.length()];
            for (int i = 0; i < a.length; i++) a[i] = new Character(str.charAt(i));
        } else if (obj instanceof List) {
            List asList = (List) obj;
            a = asList.toArray();
        } else a = (Object[]) obj;
        return a;
    }

    /**
	 * Create a new array typed as per dataType.
	 * 
	 * @param len
	 *            array length.
	 * @return array of length len, typed as dataType.
	 */
    public Object[] makeNewArray(int len) {
        Object[] a = (Object[]) Array.newInstance(dataType.getResultClass(), len);
        return a;
    }

    /**
	 * Create a new array typed as per dataType.
	 * 
	 * @param list
	 *            list to convert to array.
	 * @return array of length len, typed as dataType.
	 */
    public Object[] makeNewArray(List<Object> list) {
        Object[] a = (Object[]) Array.newInstance(dataType.getResultClass(), list.size());
        for (int i = 0; i < a.length; i++) a[i] = list.get(i);
        return a;
    }

    /**
	 * Get the number of dimensions in the array.
	 * 
	 * @return number of dimensions in the array.
	 */
    public int getNumDimensions() {
        if (dataType instanceof HLAArrayCodec) return 1 + ((HLAArrayCodec) dataType).getNumDimensions();
        return 1;
    }

    /**
	 * Resolve the element codec.
	 * 
	 * @return this codec is resolution succeeded.
	 */
    @Override
    public HLACodec resolve() {
        assert dataType != null;
        dataType = dataType.resolve();
        assert dataType != null;
        if (dataType == this) {
            logger.fatal("recursive definition in " + getName());
        }
        return this;
    }

    /**
	 * Get the HLACodec for the array elements.
	 * 
	 * @return the codec used for elements of the array.
	 */
    public HLACodec getElementCodec() {
        return dataType;
    }

    /**
	 * Set the HLACodec for the array elements.
	 * 
	 * @param dataType
	 *            the codec used for elements of the array.
	 */
    public void setElementCodec(HLACodec dataType) {
        this.dataType = dataType;
    }

    /**
	 * Get the Java class of objects decoded by this codec.
	 * 
	 * @return the Class of this array.
	 */
    public Class getResultClass() {
        if (dataType.getResultClass() == Character.class) return String.class;
        return Array.class;
    }

    /**
	 * Get the cardinality of this dimension.
	 * 
	 * @return the cardinality of this dimension.
	 */
    public HLAArrayCardinality getCardinality() {
        return card;
    }

    /**
	 * Set the cardinality of this dimension.
	 * 
	 * @param card
	 *            the cardinality of this dimension.
	 */
    public void setCardinality(HLAArrayCardinality card) {
        this.card = card;
    }

    @Override
    public void write(PrintWriter pw, Object value) {
        Object[] array = getArray(value);
        pw.print("[");
        for (int f = 0; f < array.length; f++) {
            if (f != 0) pw.print(", ");
            dataType.write(pw, array[f]);
        }
        pw.print("]");
    }

    public Object read(StreamTokenizer tok) throws IOException {
        List<Object> array = new LinkedList<Object>();
        tok.nextToken();
        while (tok.nextToken() != ']') if (tok.ttype != ',') {
            tok.pushBack();
            array.add(dataType.read(tok));
        }
        return makeNewArray(array);
    }

    public boolean isValid(Object obj) {
        if (obj == null) throw new IllegalArgumentException("null value");
        if (dataType.getResultClass() == Character.class) {
            if (!(obj instanceof String)) throw new IllegalArgumentException("not an string");
        } else if (!(obj.getClass().isArray() || obj instanceof List)) throw new IllegalArgumentException("not an array or a List");
        Object[] a = getArray(obj);
        for (int i = 0; i < a.length; i++) {
            try {
                dataType.isValid(a[i]);
            } catch (IllegalArgumentException e) {
                String newMsg = "[" + i + "]." + e.getLocalizedMessage();
                throw new IllegalArgumentException(newMsg);
            }
        }
        return true;
    }
}
