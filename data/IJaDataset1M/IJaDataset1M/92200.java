package org.exolab.jms.message;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotWriteableException;

/**
 * This class implements the {@link javax.jms.MapMessage} interface.
 * <p>
 * A MapMessage is used to send a set of name-value pairs where names are
 * Strings and values are Java primitive types. The entries can be accessed
 * sequentially or randomly by name. The order of the entries is undefined.
 * It inherits from <code>Message</code> and adds a map message body.
 * <p>
 * The primitive types can be read or written explicitly using methods
 * for each type. They may also be read or written generically as objects.
 * For instance, a call to <code>MapMessage.setInt("foo", 6)</code> is
 * equivalent to <code>MapMessage.setObject("foo", new Integer(6))</code>.
 * Both forms are provided because the explicit form is convenient for
 * static programming and the object form is needed when types are not known
 * at compile time.
 * <p>
 * When a client receives a MapMessage, it is in read-only mode. If a
 * client attempts to write to the message at this point, a
 * MessageNotWriteableException is thrown. If {@link #clearBody} is
 * called, the message can now be both read from and written to.
 * <p>
 * Map messages support the following conversion table. The marked cases
 * must be supported. The unmarked cases must throw a JMSException. The
 * String to primitive conversions may throw a runtime exception if the
 * primitives <code>valueOf()</code> method does not accept it as a valid
 * String representation of the primitive.
 * <p>
 * A value written as the row type can be read as the column type.
 *
 * <pre>
 * |        | boolean byte short char int long float double String byte[]
 * |----------------------------------------------------------------------
 * |boolean |    X                                            X
 * |byte    |          X     X         X   X                  X
 * |short   |                X         X   X                  X
 * |char    |                     X                           X
 * |int     |                          X   X                  X
 * |long    |                              X                  X
 * |float   |                                    X     X      X
 * |double  |                                          X      X
 * |String  |    X     X     X         X   X     X     X      X
 * |byte[]  |                                                        X
 * |----------------------------------------------------------------------
 * </pre>
 *
 * <p>
 * Attempting to read a null value as a Java primitive type must be treated
 * as calling the primitive's corresponding <code>valueOf(String)</code>
 * conversion method with a null value. Since char does not support a
 * String conversion, attempting to read a null value as a char must
 * throw NullPointerException.
 *
 * @version     $Revision: 1.1 $ $Date: 2004/11/26 01:50:43 $
 * @author      <a href="mailto:mourikis@exolab.org">Jim Mourikis</a>
 * @see         javax.jms.MapMessage
 */
public class MapMessageImpl extends MessageImpl implements MapMessage {

    /**
     * Object version no. for serialization
     */
    static final long serialVersionUID = 2;

    /**
     * The initial size of the map
     */
    private static final int INITIAL_SIZE = 20;

    /**
     * The container for all message data
     */
    private HashMap _map = new HashMap(INITIAL_SIZE);

    /**
     * Construct a new MapMessage
     *
     * @throws JMSException if the message type can't be set
     */
    public MapMessageImpl() throws JMSException {
        setJMSType("MapMessage");
    }

    /**
     * Clone an instance of this object
     *
     * @return a copy of this object
     * @throws CloneNotSupportedException if object or attributes aren't
     * cloneable
     */
    public final Object clone() throws CloneNotSupportedException {
        MapMessageImpl result = (MapMessageImpl) super.clone();
        result._map = (HashMap) _map.clone();
        return result;
    }

    /**
     * Serialize out this message's data
     *
     * @param out the stream to serialize out to
     * @throws IOException if any I/O exceptions occurr
     */
    public final void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeLong(serialVersionUID);
        out.writeObject(_map);
    }

    /**
     * Serialize in this message's data
     *
     * @param in the stream to serialize in from
     * @throws ClassNotFoundException if the class for an object being
     * restored cannot be found.
     * @throws IOException if any I/O exceptions occur
     */
    public final void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
        super.readExternal(in);
        long version = in.readLong();
        if (version == serialVersionUID) {
            _map = (HashMap) in.readObject();
        } else {
            throw new IOException("Incorrect version enountered: " + version + ". This version = " + serialVersionUID);
        }
    }

    /**
     * Return the boolean value with the given name
     *
     * @param name the name of the boolean
     * @return the boolean value with the given name
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     * @throws MessageFormatException if this type conversion is invalid
     */
    public final boolean getBoolean(String name) throws JMSException, MessageFormatException {
        return FormatConverter.getBoolean(_map.get(name));
    }

    /**
     * Return the byte value with the given name
     *
     * @param name the name of the byte
     * @return the byte value with the given name
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     * @throws MessageFormatException if this type conversion is invalid
     */
    public final byte getByte(String name) throws JMSException, MessageFormatException {
        return FormatConverter.getByte(_map.get(name));
    }

    /**
     * Return the short value with the given name
     *
     * @param name the name of the short
     * @return the short value with the given name
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     * @throws MessageFormatException if this type conversion is invalid
     */
    public final short getShort(String name) throws JMSException, MessageFormatException {
        return FormatConverter.getShort(_map.get(name));
    }

    /**
     * Return the Unicode character value with the given name
     *
     * @param name the name of the Unicode character
     * @return the Unicode character value with the given name
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     * @throws MessageFormatException if this type conversion is invalid
     */
    public final char getChar(String name) throws JMSException, MessageFormatException {
        return FormatConverter.getChar(_map.get(name));
    }

    /**
     * Return the integer value with the given name
     *
     * @param name the name of the integer
     * @return the integer value with the given name
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     * @throws MessageFormatException if this type conversion is invalid
     */
    public final int getInt(String name) throws JMSException, MessageFormatException {
        return FormatConverter.getInt(_map.get(name));
    }

    /**
     * Return the long value with the given name
     *
     * @param name the name of the long
     * @return the long value with the given name
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     * @throws MessageFormatException if this type conversion is invalid
     */
    public final long getLong(String name) throws JMSException, MessageFormatException {
        return FormatConverter.getLong(_map.get(name));
    }

    /**
     * Return the float value with the given name
     *
     * @param name the name of the float
     * @return the float value with the given name
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     * @throws MessageFormatException if this type conversion is invalid
     */
    public final float getFloat(String name) throws JMSException, MessageFormatException {
        return FormatConverter.getFloat(_map.get(name));
    }

    /**
     * Return the double value with the given name
     *
     * @param name the name of the double
     * @return the double value with the given name
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     * @throws MessageFormatException if this type conversion is invalid
     */
    public final double getDouble(String name) throws JMSException, MessageFormatException {
        return FormatConverter.getDouble(_map.get(name));
    }

    /**
     * Return the String value with the given name
     *
     * @param name the name of the String
     * @return the String value with the given name. If there is no item
     * by this name, a null value is returned.
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     * @throws MessageFormatException if this type conversion is invalid
     */
    public final String getString(String name) throws JMSException, MessageFormatException {
        return FormatConverter.getString(_map.get(name));
    }

    /**
     * Return the byte array value with the given name
     *
     * @param name the name of the byte array
     * @return a copy of the byte array value with the given name.
     * If there is no item by this name, a null value is returned.
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     * @throws MessageFormatException if this type conversion is invalid
     */
    public final byte[] getBytes(String name) throws JMSException, MessageFormatException {
        return FormatConverter.getBytes(_map.get(name));
    }

    /**
     * Return the Java object value with the given name
     * <p>
     * Note that this method can be used to return in objectified format,
     * an object that had been stored in the Map with the equivalent
     * <code>setObject</code> method call, or it's equivalent primitive
     * set<type> method.
     *
     * @param name the name of the Java object
     * @return a copy of the Java object value with the given name, in
     * objectified format (eg. if it set as an int, then an Integer is
     * returned).
     * Note that byte values are returned as byte[], not Byte[].
     * If there is no item by this name, a null value is returned.
     * @throws JMSException if JMS fails to read the message due to some
     * internal JMS error
     */
    public final Object getObject(String name) throws JMSException {
        Object result = null;
        Object value = _map.get(name);
        if (value != null) {
            if (value instanceof Boolean) {
                result = new Boolean(((Boolean) value).booleanValue());
            } else if (value instanceof Byte) {
                result = new Byte(((Byte) value).byteValue());
            } else if (value instanceof Short) {
                result = new Short(((Short) value).shortValue());
            } else if (value instanceof Character) {
                result = new Character(((Character) value).charValue());
            } else if (value instanceof Integer) {
                result = new Integer(((Integer) value).intValue());
            } else if (value instanceof Long) {
                result = new Long(((Long) value).longValue());
            } else if (value instanceof Float) {
                result = new Float(((Float) value).floatValue());
            } else if (value instanceof Double) {
                result = new Double(((Double) value).doubleValue());
            } else if (value instanceof String) {
                result = (String) value;
            } else if (value instanceof byte[]) {
                result = getBytes(name);
            } else {
                throw new MessageFormatException("MapMessage contains an unsupported object of type=" + value.getClass().getName());
            }
        }
        return result;
    }

    /**
     * Return an Enumeration of all the Map message's names.
     *
     * @return an enumeration of all the names in this Map message.
     */
    public final Enumeration getMapNames() {
        return Collections.enumeration(_map.keySet());
    }

    /**
     * Set a boolean value with the given name, into the Map
     *
     * @param name the name of the boolean
     * @param value the boolean value to set in the Map
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setBoolean(String name, boolean value) throws MessageNotWriteableException {
        checkWrite();
        _map.put(name, new Boolean(value));
    }

    /**
     * Set a byte value with the given name, into the Map
     *
     * @param name the name of the byte
     * @param value the byte value to set in the Map
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setByte(String name, byte value) throws MessageNotWriteableException {
        checkWrite();
        _map.put(name, new Byte(value));
    }

    /**
     * Set a short value with the given name, into the Map
     *
     * @param name the name of the short
     * @param value the short value to set in the Map
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setShort(String name, short value) throws MessageNotWriteableException {
        checkWrite();
        _map.put(name, new Short(value));
    }

    /**
     * Set a Unicode character value with the given name, into the Map
     *
     * @param name the name of the Unicode character
     * @param value the Unicode character value to set in the Map
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setChar(String name, char value) throws MessageNotWriteableException {
        checkWrite();
        _map.put(name, new Character(value));
    }

    /**
     * Set an integer value with the given name, into the Map
     *
     * @param name the name of the integer
     * @param value the integer value to set in the Map
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setInt(String name, int value) throws MessageNotWriteableException {
        checkWrite();
        _map.put(name, new Integer(value));
    }

    /**
     * Set a long value with the given name, into the Map
     *
     * @param name the name of the long
     * @param value the long value to set in the Map
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setLong(String name, long value) throws MessageNotWriteableException {
        checkWrite();
        _map.put(name, new Long(value));
    }

    /**
     * Set a float value with the given name, into the Map
     *
     * @param name the name of the float
     * @param value the float value to set in the Map
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setFloat(String name, float value) throws MessageNotWriteableException {
        checkWrite();
        _map.put(name, new Float(value));
    }

    /**
     * Set a double value with the given name, into the Map
     *
     * @param name the name of the double
     * @param value the double value to set in the Map
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setDouble(String name, double value) throws MessageNotWriteableException {
        checkWrite();
        _map.put(name, new Double(value));
    }

    /**
     * Set a String value with the given name, into the Map
     *
     * @param name the name of the String
     * @param value the String value to set in the Map
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setString(String name, String value) throws MessageNotWriteableException {
        checkWrite();
        _map.put(name, value);
    }

    /**
     * Set a byte array value with the given name, into the Map
     *
     * @param name the name of the byte array
     * @param value the byte array value to set in the Map. The array is
     * copied so the value for name will not be altered by future
     * modifications.
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setBytes(String name, byte[] value) throws MessageNotWriteableException {
        checkWrite();
        byte[] bytes = null;
        if (value != null) {
            bytes = new byte[value.length];
            System.arraycopy(value, 0, bytes, 0, bytes.length);
        }
        _map.put(name, bytes);
    }

    /**
     * Set a portion of the byte array value with the given name, into the Map
     *
     * @param name the name of the byte array
     * @param value the byte array value to set in the Map.
     * @param offset the initial offset within the byte array.
     * @param length the number of bytes to use.
     * @throws MessageNotWriteableException if the message is in read-only mode
     */
    public final void setBytes(String name, byte[] value, int offset, int length) throws MessageNotWriteableException {
        checkWrite();
        byte[] bytes = null;
        if (value != null) {
            bytes = new byte[length];
            System.arraycopy(value, offset, bytes, 0, length);
        }
        _map.put(name, bytes);
    }

    /**
     * Set a Java object value with the given name, into the Map
     * <p>
     * Note that this method only works for the objectified primitive
     * object types (Integer, Double, Long ...), String's and byte arrays.
     *
     * @param name the name of the Java object
     * @param value the Java object value to set in the Map
     * @throws MessageFormatException if object is invalid
     * @throws MessageNotWriteableException if message in read-only mode.
     */
    public final void setObject(String name, Object value) throws MessageFormatException, MessageNotWriteableException {
        checkWrite();
        if (value == null) {
            _map.put(name, null);
        } else if (value instanceof Boolean) {
            setBoolean(name, ((Boolean) value).booleanValue());
        } else if (value instanceof Byte) {
            setByte(name, ((Byte) value).byteValue());
        } else if (value instanceof Short) {
            setShort(name, ((Short) value).shortValue());
        } else if (value instanceof Character) {
            setChar(name, ((Character) value).charValue());
        } else if (value instanceof Integer) {
            setInt(name, ((Integer) value).intValue());
        } else if (value instanceof Long) {
            setLong(name, ((Long) value).longValue());
        } else if (value instanceof Float) {
            setFloat(name, ((Float) value).floatValue());
        } else if (value instanceof Double) {
            setDouble(name, ((Double) value).doubleValue());
        } else if (value instanceof String) {
            setString(name, (String) value);
        } else if (value instanceof byte[]) {
            setBytes(name, (byte[]) value);
        } else {
            throw new MessageFormatException("MapMessage does not support objects of type=" + value.getClass().getName());
        }
    }

    /**
     * Check if an item exists in this MapMessage
     *
     * @param name the name of the item to test
     * @return true if the item exists
     */
    public final boolean itemExists(String name) {
        return _map.containsKey(name);
    }

    /**
     * Clear out the message body. Clearing a message's body does not clear
     * its header values or property entries.
     * If this message body was read-only, calling this method leaves the
     * message body is in the same state as an empty body in a newly created
     * message
     */
    public final void clearBody() throws JMSException {
        super.clearBody();
        _map = new HashMap(INITIAL_SIZE);
    }
}
