package spinja.util;

public interface DataReader {

    /**
	 * Returns the place where the mark is currently placed. There is no guarantee what kind of
	 * numbers this will return, but when reading an number of bytes, the mark must be increased by
	 * that same number.
	 * @return The place where the mark is currently placed.
	 */
    public int getMark();

    /**
	 * Remembers the current place in the DataReader, so that it can be returned to with the
	 * {@link #resetMark()} method.
	 */
    public void storeMark();

    /**
	 * Resets the mark to the place where the {@link #storeMark()} was called. When this was never
	 * called it is reset to the beginning of the data.
	 */
    public void resetMark();

    /**
	 * Reads a boolean value from the storage and moves the pointer to the next place.
	 * @return The boolean value.
	 */
    public boolean readBoolean();

    /**
	 * Reads a boolean value from the storage, but keeps the pointer in the same place.
	 * @return The boolean value.
	 */
    public boolean peekBoolean();

    /**
	 * Reads a byte value from the storage and moves the pointer 8 bits ahead.
	 * @return The byte value.
	 */
    public int readByte();

    /**
	 * Reads a byte value from the storage, but keeps the pointer in the same place.
	 * @return The byte value.
	 */
    public int peekByte();

    /**
	 * Reads a short value from the storage and moves the pointer 16 bits ahead.
	 * @return The short value.
	 */
    public int readShort();

    /**
	 * Reads a short value from the storage, but keeps the pointer in the same place.
	 * @return The short value.
	 */
    public int peekShort();

    /**
	 * Reads a integer value from the storage and moves the pointer 32 bits ahead.
	 * @return The integer value.
	 */
    public int readInt();

    /**
	 * Reads a integer value from the storage, but keeps the pointer in the same place.
	 * @return The integer value.
	 */
    public int peekInt();
}
