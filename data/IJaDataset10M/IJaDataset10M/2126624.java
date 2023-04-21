package com.datagenic.fourthdimension.adapters;

import com.datagenic.fourthdimension.ListAdapter;
import org.apache.commons.collections.primitives.*;
import java.util.*;

/**
 * The ByteListAdapter is an implementation of the ListAdapter storing only primitive byte data
 */
public class ByteListAdapter extends ListAdapter implements java.io.Serializable {

    private static final long serialVersionUID = 1002;

    /**
     * The commons primitive datatype which this class is providing an adapter to
     */
    private ByteList list;

    /** Creates a new instance of ByteListAdapter with an empty internal byte array*/
    public ByteListAdapter() {
        super();
        list = new ArrayByteList();
    }

    /** Creates a new instance of ByteListAdapter with an initial internal short array
     * @param data intial data to populate the internal long array
     */
    public ByteListAdapter(byte[] data) {
        super();
        list = new ArrayByteList(data);
    }

    /**
     * Appends the specified double value to the end of the list.<p>
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(double value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add((byte) value);
    }

    /**
     * Appends the specified double value at the specified location.  Shifts the element at that position ( if any ) and any subsequent elements to the right increasing their indicies.<p>
     * @param index the index at which to insert the element
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(int index, double value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add(index, (byte) value);
    }

    /**
     * Replaces the double value at the specified location.  <p>
     * @param index the index at which to element is to be replaced
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void set(int index, double value) throws UnsupportedOperationException, IllegalArgumentException {
        list.set(index, (byte) value);
    }

    /**
     * Returns the value of the element at the specified position.  <p>
     * @param index the index at which to element is to be replaced
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     * @return the stored value
     */
    public double getDouble(int index) throws UnsupportedOperationException, IllegalArgumentException {
        return list.get(index);
    }

    /**
     * Appends the specified float value to the end of the list.<p>
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(float value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add((byte) value);
    }

    /**
     * Appends the specified float value at the specified location.  Shifts the element at that position ( if any ) and any subsequent elements to the right increasing their indicies.<p>
     * @param index the index at which to insert the element
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(int index, float value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add(index, (byte) value);
    }

    /**
     * Replaces the float value at the specified location.  <p>
     * @param index the index at which to element is to be replaced
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void set(int index, float value) throws UnsupportedOperationException, IllegalArgumentException {
        list.set(index, (byte) value);
    }

    /**
     * Returns the value of the element at the specified position.  <p>
     * @param index the index at which to element is to be replaced
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     * @return the stored value
     */
    public float getFloat(int index) throws UnsupportedOperationException, IllegalArgumentException {
        return list.get(index);
    }

    /**
     * Appends the specified long value to the end of the list.<p>
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(long value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add((byte) value);
    }

    /**
     * Appends the specified long value at the specified location.  Shifts the element at that position ( if any ) and any subsequent elements to the right increasing their indicies.<p>
     * @param index the index at which to insert the element
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(int index, long value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add(index, (byte) value);
    }

    /**
     * Replaces the long value at the specified location.  <p>
     * @param index the index at which to element is to be replaced
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void set(int index, long value) throws UnsupportedOperationException, IllegalArgumentException {
        list.set(index, (byte) value);
    }

    /**
     * Returns the value of the element at the specified position.  <p>
     * @param index the index at which to element is to be replaced
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     * @return the stored value
     */
    public long getLong(int index) throws UnsupportedOperationException, IllegalArgumentException {
        return list.get(index);
    }

    /**
     * Appends the specified int value to the end of the list.<p>
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(int value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add((byte) value);
    }

    /**
     * Appends the specified int value at the specified location.  Shifts the element at that position ( if any ) and any subsequent elements to the right increasing their indicies.<p>
     * @param index the index at which to insert the element
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(int index, int value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add(index, (byte) value);
    }

    /**
     * Replaces the int value at the specified location.  <p>
     * @param index the index at which to element is to be replaced
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void set(int index, int value) throws UnsupportedOperationException, IllegalArgumentException {
        list.set(index, (byte) value);
    }

    /**
     * Returns the value of the element at the specified position.  <p>
     * @param index the index at which to element is to be replaced
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     * @return the stored value
     */
    public int getInt(int index) throws UnsupportedOperationException, IllegalArgumentException {
        return list.get(index);
    }

    /**
     * Appends the specified short value to the end of the list.<p>
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(short value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add((byte) value);
    }

    /**
     * Appends the specified short value at the specified location.  Shifts the element at that position ( if any ) and any subsequent elements to the right increasing their indicies.<p>
     * @param index the index at which to insert the element
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(int index, short value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add(index, (byte) value);
    }

    /**
     * Replaces the short value at the specified location.  <p>
     * @param index the index at which to element is to be replaced
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void set(int index, short value) throws UnsupportedOperationException, IllegalArgumentException {
        list.set(index, (byte) value);
    }

    /**
     * Returns the value of the element at the specified position.  <p>
     * @param index the index at which to element is to be replaced
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     * @return the stored value
     */
    public short getShort(int index) throws UnsupportedOperationException, IllegalArgumentException {
        return list.get(index);
    }

    /**
     * Appends the specified byte value to the end of the list.<p>
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(byte value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add(value);
    }

    /**
     * Appends the specified byte value at the specified location.  Shifts the element at that position ( if any ) and any subsequent elements to the right increasing their indicies.<p>
     * @param index the index at which to insert the element
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(int index, byte value) throws UnsupportedOperationException, IllegalArgumentException {
        list.add(index, value);
    }

    /**
     * Replaces the byte value at the specified location.  <p>
     * @param index the index at which to element is to be replaced
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void set(int index, byte value) throws UnsupportedOperationException, IllegalArgumentException {
        list.set(index, value);
    }

    /**
     * Returns the value of the element at the specified position.  <p>
     * @param index the index at which to element is to be replaced
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     * @return the stored value
     */
    public byte getByte(int index) throws UnsupportedOperationException, IllegalArgumentException {
        return list.get(index);
    }

    /**
     * Appends the specified Object value to the end of the list.<p>
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(Object value) throws UnsupportedOperationException, IllegalArgumentException {
        if (value == null) {
            list.add((byte) 0);
        } else if (value instanceof Number) {
            list.add(((Byte) value).byteValue());
        } else {
            throw new UnsupportedOperationException("Data type not supported. [" + value.getClass().getName());
        }
    }

    /**
     * Appends the specified Object value at the specified location.  Shifts the element at that position ( if any ) and any subsequent elements to the right increasing their indicies.<p>
     * @param index the index at which to insert the element
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(int index, Object value) throws UnsupportedOperationException, IllegalArgumentException {
        if (value == null) {
            list.add(index, (byte) 0);
        } else if (value instanceof Number) {
            list.add(index, ((Byte) value).byteValue());
        } else {
            throw new UnsupportedOperationException("Data type not supported. [" + value.getClass().getName());
        }
    }

    /**
     * Replaces the Object value at the specified location.  <p>
     * @param index the index at which to element is to be replaced
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void set(int index, Object value) throws UnsupportedOperationException, IllegalArgumentException {
        if (value == null) {
            list.set(index, (byte) 0);
        } else if (value instanceof Number) {
            list.set(index, ((Byte) value).byteValue());
        } else {
            throw new UnsupportedOperationException("Data type not supported. [" + value.getClass().getName());
        }
    }

    /**
     * Returns the value of the element at the specified position.  <p>
     * @param index the index at which to element is to be replaced
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     * @return the stored value
     */
    public Object getObject(int index) throws UnsupportedOperationException, IllegalArgumentException {
        return new Byte(list.get(index));
    }

    /**
     * Returns the value of the element at the specified position.  <p>
     * @param index the index at which to element is to be replaced
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     * @return the stored value
     */
    public String getString(int index) throws UnsupportedOperationException, IllegalArgumentException {
        return String.valueOf(list.get(index));
    }

    /**
     * Appends the specified boolean value to the end of the list.<p>
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(boolean value) throws UnsupportedOperationException, IllegalArgumentException {
        if (value) {
            list.add((byte) 1);
        } else {
            list.add((byte) 0);
        }
    }

    /**
     * Appends the specified boolean value at the specified location.  Shifts the element at that position ( if any ) and any subsequent elements to the right increasing their indicies.<p>
     * @param index the index at which to insert the element
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void add(int index, boolean value) throws UnsupportedOperationException, IllegalArgumentException {
        if (value) {
            list.add(index, (byte) 1);
        } else {
            list.add(index, (byte) 0);
        }
    }

    /**
     * Replaces the boolean value at the specified location.  <p>
     * @param index the index at which to element is to be replaced
     * @param value the element that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void set(int index, boolean value) throws UnsupportedOperationException, IllegalArgumentException {
        if (value) {
            list.set(index, (byte) 1);
        } else {
            list.set(index, (byte) 0);
        }
    }

    /**
     * Returns the value of the element at the specified position.  <p>
     * @param index the index at which to element is to be replaced
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     * @return the stored value
     */
    public boolean getBoolean(int index) throws UnsupportedOperationException, IllegalArgumentException {
        float value = list.get(index);
        return (value == 1) ? true : false;
    }

    /**
     * Inserts all the elements of the ListAdapter starting at the specified location. Shifts the element at that position ( if any ) and any subsequent elements to the right increasing their indicies.<p>
     * @param index the index at which to insert the element
     * @param list the ListAdapter that is to be stored
     * @throws java.lang.UnsupportedOperationException - when this operation is not supported.
     * @throws java.lang.IllegalArgumentException - may be thrown if some aspect of the specified element prevents it from being added.
     */
    public void addAll(int index, ListAdapter list) throws UnsupportedOperationException, IllegalArgumentException {
        this.list.addAll(index, list.getByteList());
    }

    /**
     * Provides access to the underlying List that this adapter is using
     * @return the underlying primitive array implementation
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public DoubleList getDoubleList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Data type not supported. [" + DoubleList.class.getName());
    }

    /**
     * Provides access to the underlying List that this adapter is using
     * @return the underlying primitive array implementation
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public ByteList getByteList() throws UnsupportedOperationException {
        return list;
    }

    /**
     * Provides access to the underlying List that this adapter is using
     * @return the underlying primitive array implementation
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public BooleanList getBooleanList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Data type not supported. [" + BooleanList.class.getName());
    }

    /**
     * Provides access to the underlying List that this adapter is using
     * @return the underlying primitive array implementation
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public FloatList getFloatList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Data type not supported. [" + BooleanList.class.getName());
    }

    /**
     * Provides access to the underlying List that this adapter is using
     * @return the underlying primitive array implementation
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public IntList getIntList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Data type not supported. [" + ShortList.class.getName());
    }

    /**
     * Provides access to the underlying List that this adapter is using
     * @return the underlying primitive array implementation
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public LongList getLongList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Data type not supported. [" + IntList.class.getName());
    }

    /**
     * Provides access to the underlying List that this adapter is using
     * @return the underlying primitive array implementation
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public ShortList getShortList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Data type not supported. [" + ByteList.class.getName());
    }

    /**
     * Provides access to the underlying List that this adapter is using
     * @return the underlying primitive array implementation
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public java.util.ArrayList getStringList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Data type not supported. [" + ArrayList.class.getName());
    }

    /**
     * Returns the number of elements in the list
     * @return int the size of the list
     */
    public int size() {
        return list.size();
    }

    /**
     * Returns a ListAdapter of the elements between the specified fromIndex and toIndex exclusive.
     * @param fromIndex the starting index inclusive
     * @param toIndex the end index exclusive
     * @return A ListAdapter of the same type containing elements between the start and end indexes
     */
    public ListAdapter subList(int fromIndex, int toIndex) {
        return new ByteListAdapter(list.subList(fromIndex, toIndex).toArray());
    }

    /**
     * Converts the contents of the ListAdapter into the corresponding primitive array
     * @return double[] containing the contents of the ListAdapter
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public double[] toDoubleArray() throws UnsupportedOperationException {
        byte[] tmp = list.toArray();
        double[] d = new double[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            d[i] = (double) tmp[i];
        }
        return d;
    }

    /**
     * Converts the contents of the ListAdapter into the corresponding primitive array
     * @return float[] containing the contents of the ListAdapter
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public float[] toFloatArray() throws UnsupportedOperationException {
        byte[] tmp = list.toArray();
        float[] d = new float[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            d[i] = (float) tmp[i];
        }
        return d;
    }

    /**
     * Converts the contents of the ListAdapter into the corresponding primitive array
     * @return long[] containing the contents of the ListAdapter
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public long[] toLongArray() throws UnsupportedOperationException {
        byte[] tmp = list.toArray();
        long[] d = new long[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            d[i] = (long) tmp[i];
        }
        return d;
    }

    /**
     * Converts the contents of the ListAdapter into the corresponding primitive array
     * @return int[] containing the contents of the ListAdapter
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public int[] toIntArray() throws UnsupportedOperationException {
        byte[] tmp = list.toArray();
        int[] d = new int[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            d[i] = (int) tmp[i];
        }
        return d;
    }

    /**
     * Converts the contents of the ListAdapter into the corresponding primitive array
     * @return short[] containing the contents of the ListAdapter
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public short[] toShortArray() throws UnsupportedOperationException {
        byte[] tmp = list.toArray();
        short[] d = new short[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            d[i] = (short) tmp[i];
        }
        return d;
    }

    /**
     * Converts the contents of the ListAdapter into the corresponding primitive array
     * @return byte[] containing the contents of the ListAdapter
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public byte[] toByteArray() throws UnsupportedOperationException {
        return list.toArray();
    }

    /**
     * Converts the contents of the ListAdapter into the corresponding primitive array
     * @return String[] containing the contents of the ListAdapter
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public String[] toStringArray() throws UnsupportedOperationException {
        byte[] tmp = list.toArray();
        String[] d = new String[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            d[i] = String.valueOf(tmp[i]);
        }
        return d;
    }

    /**
     * Converts the contents of the ListAdapter into the corresponding primitive array
     * @return boolean[] containing the contents of the ListAdapter
     * @throws java.lang.UnsupportedOperationException if this operation is not supported
     */
    public boolean[] toBooleanArray() throws UnsupportedOperationException {
        byte[] tmp = list.toArray();
        boolean[] d = new boolean[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            d[i] = (tmp[i] == 1) ? true : false;
        }
        return d;
    }

    /**
     * Creates and returns a copy of this object
     * @return a clone of this instance
     */
    public Object clone() {
        ByteListAdapter adapter = new ByteListAdapter(list.toArray());
        return adapter;
    }

    /**
     * Produces a ListAdapter of the correct type with a defined size.  All the values are set to
     * default primitive values.
     * @param size defines the length of the internal storage array
     */
    public ListAdapter makeAdapter(int size) {
        byte[] tmp = new byte[size];
        return new ByteListAdapter(tmp);
    }
}
