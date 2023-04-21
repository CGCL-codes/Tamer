package malictus.klang.primitives;

import java.io.*;
import malictus.klang.KlangConstants;

/**
 * Primitive representing a signed 4-byte (32 bit) little-endian integer value.
 * 
 * @author Jim Halliday
 */
public class Int4ByteSignedLE implements Primitive, PrimitiveInt, PrimitiveFixedByte {

    private Integer value;

    /**
	 * Empty constructor. The actual value must be initialized later, 
	 * before the class will return a valid value.
	 */
    public Int4ByteSignedLE() {
    }

    /**
	 * Constructor from an integer value.
	 * @param value the Integer that this object should represent
	 */
    public Int4ByteSignedLE(Integer value) {
        this.value = value;
    }

    /**
	 * Constructor from a file reference. The value will be read from the file at
	 * the current file pointer, and the file pointer will be advanced to 
	 * just beyond the value.
	 * @param raf a RandomAccessFile object that points to a valid file, queued for reading
	 * @throws IOException if the file cannot be read properly
	 * @throws BadValueException if the value read from file is incorrect
	 */
    public Int4ByteSignedLE(RandomAccessFile raf) throws IOException, BadValueException {
        setValueFromFile(raf);
    }

    /**
	 * Constructor from a string that represents the integer value.
	 * @param val the string to read from
	 * @throws BadValueException if the string cannot be converted properly into a matching integer
	 */
    public Int4ByteSignedLE(String val) throws BadValueException {
        setValueFromString(val);
    }

    /**
	 * Get the number of bytes that this value (always) occupies
	 * @return the fixed length in bytes
	 */
    public int getFixedLength() {
        return 4;
    }

    /**
	 * Set the current value to the specified integer object.
	 * @param value the new Integer value
	 * @throws BadValueException if the Integer is out of range
	 */
    public void setValue(Integer value) throws BadValueException {
        if (!testCandidate(value)) {
            throw new BadValueException(value.toString());
        }
        this.value = value;
    }

    /**
	 * Set the value for this object from the specified file pointer.
	 * @param raf the file object to read the value from
	 * @throws IOException if the file cannot be read properly
	 * @throws BadValueException if the value is out of range
	 */
    public void setValueFromFile(RandomAccessFile raf) throws IOException, BadValueException {
        int input = raf.readInt();
        input = Integer.reverseBytes(input);
        Integer val = new Integer(input);
        if (!testCandidate(val)) {
            throw new BadValueException(val.toString());
        }
        value = val;
    }

    /**
	 * Set the value for this object from the specified string.
	 * @param s the string to read
	 * @throws BadValueException if the string cannot be parsed into a integer that is within range
	 */
    public void setValueFromString(String s) throws BadValueException {
        if (s == null) {
            value = null;
            return;
        }
        s = s.trim();
        try {
            Integer x = Integer.decode(s);
            if (!testCandidate(x)) {
                throw new BadValueException(x.toString() + " is not within the correct limits for this value");
            }
            value = x;
        } catch (Exception err) {
            throw new BadValueException(s);
        }
    }

    /**
	 * Get this object's integer value.
	 * @return the current value; note that this will return null if the value was never initialized
	 */
    public Integer getValue() {
        return value;
    }

    /**
	 * Get this object's value as a long.
	 * @return the current value; note that this will return null if the value was never initialized
	 */
    public Long getValueAsLong() {
        if (value == null) {
            return null;
        } else {
            return value.longValue();
        }
    }

    /**
	 * Write this value to the specified file, at the current file pointer.
	 * @param raf file object to write the value to
	 * @throws IOException if the file cannot be written to properly
	 * @throws BadValueException if the value has not been initialized
	 */
    public void writeValueToFile(RandomAccessFile raf) throws IOException, BadValueException {
        if (value == null) {
            throw new BadValueException();
        }
        int output = Integer.reverseBytes(value);
        raf.writeInt(output);
    }

    /**
	 * Write the current value of this object to a string.
	 * @throws BadValueException if the value has not been initialized
	 * @return a string representation of this integer
	 */
    public String writeValueToString() throws BadValueException {
        if (value == null) {
            throw new BadValueException();
        }
        return value.toString();
    }

    /**
	 * Retrieve a text-based description of this primitive.
	 * @return a text-based description of this primitive
	 */
    public String getPrimitiveDescription() {
        return KlangConstants.getPrimitiveDescriptionFor(this.getClass().getName());
    }

    /**
	 * Tell if the primitive object has an initialized value or not.
	 * @return true if value has been initialized, and false otherwise
	 */
    public boolean valueExists() {
        if (value == null) {
            return false;
        }
        return true;
    }

    /**
	 * Used internally to see if a given int value is within range for this object
	 * @param value the Integer object to test
	 * @return true if candidate is in range, and false otherwise
	 */
    private boolean testCandidate(Integer value) {
        return true;
    }
}
