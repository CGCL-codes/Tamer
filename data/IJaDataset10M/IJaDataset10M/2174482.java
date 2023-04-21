package edu.kgi.biobridge.sbwdriver;

import java.lang.reflect.Array;
import java.util.*;
import edu.kgi.biobridge.gum.*;
import edu.caltech.sbw.*;

/**
 * <p>SBWData is the SBW driver's implementation of the Data interface. It is
 * rather complicated, so consider yourself warned. </p>
 * 
 * <p>The class is intended to be used in either a standalone value mode, or as 
 * a cursor on lists and arrays. Because of this, its backing store 
 * is <b>always</b> an array of some kind, even if the array is just a single 
 * element. A good deal of logic is used to hide this fact.</p>
 *  
 * @author Cameron Wellock
 */
class SBWData implements Data, BlockWriteable {

    /**
 * Data, which must be an array of some kind.
 */
    protected Object data;

    /**
 * Indices for accessing the data.
 */
    protected int indices[] = { 0 };

    /**
 * Parameter type of this Data object.
 */
    protected ParameterType type;

    /**
 * SBW type of this Data object.
 */
    protected byte sbwType;

    /**
 * Create a new SBWData with a byte value.
 * @param value Value to create object with.
 */
    public SBWData(byte value) {
        byte v[] = new byte[1];
        v[0] = value;
        data = v;
        type = ParameterType.BYTE;
        sbwType = DataBlockWriter.BYTE_TYPE;
    }

    /**
 * Create a new SBWData with an integer value.
 * @param value Value to create object with.
 */
    public SBWData(int value) {
        int v[] = new int[1];
        v[0] = value;
        data = v;
        type = ParameterType.INTEGER;
        sbwType = DataBlockWriter.INTEGER_TYPE;
    }

    /**
 * Create a new SBWData with a double value.
 * @param value Value to create object with.
 */
    public SBWData(double value) {
        double v[] = new double[1];
        v[0] = value;
        data = v;
        type = ParameterType.DOUBLE;
        sbwType = DataBlockWriter.DOUBLE_TYPE;
    }

    /**
 * Create a new SBWData with a string value.
 * @param value Value to create object with.
 */
    public SBWData(String value) {
        String v[] = new String[1];
        v[0] = value;
        data = v;
        type = ParameterType.STRING;
        sbwType = DataBlockWriter.STRING_TYPE;
    }

    /**
 * Create a new SBWData with a boolean value.
 * @param value Value to create object with.
 */
    public SBWData(boolean value) {
        boolean v[] = new boolean[1];
        v[0] = value;
        data = v;
        type = ParameterType.BOOLEAN;
        sbwType = DataBlockWriter.BOOLEAN_TYPE;
    }

    /**
 * Create a new SBWData as a list.
 * @param value Collection of items to keep in the SBWData object.
 */
    public SBWData(Collection value) {
        Collection v[] = new Collection[1];
        v[0] = value;
        data = v;
        type = ParameterType.LIST;
        sbwType = DataBlockWriter.LIST_TYPE;
    }

    /**
 * Create a new SBWData as an array.
 * @param value Array to create object with.
 */
    public SBWData(Object value) {
        data = value;
        indices = (int[]) Array.newInstance(Integer.TYPE, arrayDimensions(value));
        type = ParameterType.ARRAY;
        sbwType = DataBlockWriter.ARRAY_TYPE;
    }

    /**
 * Create a new SBWData as a "cursor" on an array.
 * @param array Underlying array to create cursor on.
 * @param type Component type of the array.
 * @param sbwType SBW type of the array.
 */
    public SBWData(Object array, ParameterType type, byte sbwType) {
        data = array;
        indices = (int[]) Array.newInstance(Integer.TYPE, arrayDimensions(array));
        this.type = type;
        this.sbwType = sbwType;
    }

    /**
 * Dereference the array to get to the last, single-dimensional component of the array.
 * @return A 1-dimensional array.
 */
    private Object arrayDeref() {
        Object arr = data;
        for (int i = 0; i < indices.length - 1; i++) {
            arr = Array.get(arr, indices[i]);
        }
        return arr;
    }

    /**
 * Get the number of dimensions of an array.
 * @param array Array to get the dimensions of.
 * @return Number of dimensions in the array.
 */
    private int arrayDimensions(Object array) {
        int dims = 1;
        while (array.getClass().getComponentType().isArray()) {
            dims++;
            array = Array.get(array, 0);
        }
        return dims;
    }

    /**
 * Attempt to set a value by demotion. Callers must manually cast their value into all potential
 * input types; this method will choose the correct one for use.
 * @param bo Boolean value, if available.
 * @param by Byte value.
 * @param in Integer value.
 * @param db Double value.
 * @throws ETypeConversionFailure
 */
    private void attemptDemotedSet(boolean bo, byte by, int in, double db) throws ETypeConversionFailure {
        if (type.equals(ParameterType.BOOLEAN)) Array.setBoolean(arrayDeref(), lastIndex(), bo); else if (type.equals(ParameterType.BYTE)) Array.setByte(arrayDeref(), lastIndex(), by); else if (type.equals(ParameterType.INTEGER)) Array.setInt(arrayDeref(), lastIndex(), in); else if (type.equals(ParameterType.DOUBLE)) Array.setDouble(arrayDeref(), lastIndex(), db); else throw new ETypeConversionFailure(type);
    }

    /**
 * Convert a boolean to an integer value.
 * @param b Boolean to convert.
 * @return Integer value.
 */
    private static int booleanToInt(boolean b) {
        if (b) return 1; else return 0;
    }

    public DataFactory dataFactory() {
        return SBWDataFactory.factory;
    }

    /**
 * Fail the operation if the underlying data is not of a simple type 
 * (not a list or array).
 * @throws Exception
 */
    private void failIfNotSimple() throws Exception {
        if (type.equals(ParameterType.LIST) || type.equals(ParameterType.ARRAY)) throw new Exception();
    }

    public Object get() throws ETypeConversionFailure {
        return Array.get(arrayDeref(), lastIndex());
    }

    public byte getByte() throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            return Array.getByte(arrayDeref(), lastIndex());
        } catch (Exception e) {
            throw new ETypeConversionFailure(data, type);
        }
    }

    public int getInteger() throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            return Array.getInt(arrayDeref(), lastIndex());
        } catch (Exception e) {
            throw new ETypeConversionFailure(data, type);
        }
    }

    public float getFloat() throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            return Array.getFloat(arrayDeref(), lastIndex());
        } catch (Exception e) {
            throw new ETypeConversionFailure(data, type);
        }
    }

    public double getDouble() throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            return Array.getDouble(arrayDeref(), lastIndex());
        } catch (Exception e) {
            throw new ETypeConversionFailure(data, type);
        }
    }

    public boolean getBoolean() throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            return Array.getBoolean(arrayDeref(), lastIndex());
        } catch (Exception e) {
            throw new ETypeConversionFailure(data, type);
        }
    }

    public String getString() throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            return Array.get(arrayDeref(), lastIndex()).toString();
        } catch (Exception e) {
            throw new ETypeConversionFailure(data, type);
        }
    }

    public DataArray getArray() throws ETypeConversionFailure {
        if (type.equals(ParameterType.ARRAY)) return new SBWDataArray(data); else throw new ETypeConversionFailure(data, type);
    }

    public DataList getList() throws ETypeConversionFailure {
        if (type.equals(ParameterType.LIST)) {
            if (Array.get(arrayDeref(), lastIndex()) == null) Array.set(arrayDeref(), lastIndex(), new Vector());
            return new SBWDataList((Collection) Array.get(arrayDeref(), lastIndex()));
        } else {
            throw new ETypeConversionFailure(data, type);
        }
    }

    /**
 * Get the last index value of the indices.
 */
    public int lastIndex() {
        return indices[indices.length - 1];
    }

    /**
 * Move the index pointer to a new position in the array.
 * @param indices New indices to move to.
 */
    protected void move(int indices[]) {
        this.indices = indices;
    }

    public void set(byte value) throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            Array.setByte(arrayDeref(), lastIndex(), value);
        } catch (IllegalArgumentException e) {
            attemptDemotedSet((value != 0), value, (int) value, (double) value);
        } catch (Exception e) {
            throw new ETypeConversionFailure(new Byte(value), type);
        }
    }

    public void set(int value) throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            Array.setInt(arrayDeref(), lastIndex(), value);
        } catch (IllegalArgumentException e) {
            attemptDemotedSet((value != 0), (byte) value, value, (double) value);
        } catch (Exception e) {
            throw new ETypeConversionFailure(new Integer(value), type);
        }
    }

    public void set(float value) throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            Array.setFloat(arrayDeref(), lastIndex(), value);
        } catch (IllegalArgumentException e) {
            attemptDemotedSet((value != 0f), (byte) value, (int) value, (double) value);
        } catch (Exception e) {
            throw new ETypeConversionFailure(new Float(value), type);
        }
    }

    public void set(double value) throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            Array.setDouble(arrayDeref(), lastIndex(), value);
        } catch (IllegalArgumentException e) {
            attemptDemotedSet((value != 0d), (byte) value, (int) value, (double) value);
        } catch (Exception e) {
            throw new ETypeConversionFailure(new Double(value), type);
        }
    }

    public void set(boolean value) throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            Array.setBoolean(arrayDeref(), lastIndex(), value);
        } catch (IllegalArgumentException e) {
            int iv = booleanToInt(value);
            attemptDemotedSet(value, (byte) iv, (int) iv, (double) iv);
        } catch (Exception e) {
            throw new ETypeConversionFailure(new Boolean(value), type);
        }
    }

    public void set(String value) throws ETypeConversionFailure {
        try {
            failIfNotSimple();
            if (type.equals(ParameterType.BOOLEAN)) {
                Array.set(arrayDeref(), lastIndex(), Boolean.valueOf(value));
            } else if (type.equals(ParameterType.BYTE)) {
                Array.set(arrayDeref(), lastIndex(), Byte.valueOf(value));
            } else if (type.equals(ParameterType.INTEGER)) {
                Array.set(arrayDeref(), lastIndex(), Integer.valueOf(value));
            } else if (type.equals(ParameterType.DOUBLE)) {
                Array.set(arrayDeref(), lastIndex(), Double.valueOf(value));
            } else if (type.equals(ParameterType.STRING)) {
                Array.set(arrayDeref(), lastIndex(), value);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ETypeConversionFailure(value, type);
        }
    }

    public void set(DataArray value) throws ETypeConversionFailure {
        throw new ETypeConversionFailure(value, type);
    }

    public void set(DataList value) throws ETypeConversionFailure {
        try {
            SBWDataList list = (SBWDataList) value;
            Array.set(arrayDeref(), lastIndex(), list.items);
        } catch (Exception e) {
            throw new ETypeConversionFailure(value, type);
        }
    }

    public ParameterType type() {
        return type;
    }

    public void writeTo(DataBlockWriter writer) throws ECallFailure {
        try {
            if (type.equals(ParameterType.BOOLEAN)) writer.add(getBoolean()); else if (type.equals(ParameterType.BYTE)) writer.add(getByte()); else if (type.equals(ParameterType.INTEGER)) writer.add(getInteger()); else if (type.equals(ParameterType.DOUBLE)) writer.add(getDouble()); else if (type.equals(ParameterType.STRING)) writer.add(getString()); else if (type.equals(ParameterType.LIST)) writer.add(Array.get(arrayDeref(), lastIndex())); else if (type.equals(ParameterType.ARRAY)) writer.add(data); else throw new ECallFailure("Unwriteable value");
        } catch (ETypeConversionFailure e) {
            throw new ECallFailure(e.toString());
        } catch (SBWUnsupportedObjectTypeException e) {
            throw new ECallFailure(e.toString());
        }
    }
}
