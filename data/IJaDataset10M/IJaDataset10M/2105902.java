package de.freeradical.jpackrat2.toolbox;

public interface ByteArrayList {

    /**
	 * writes some chars into the array.
	 * @throws ByteArrayListException 
	 */
    void append(char[] newBytes, int length) throws ByteArrayListException;

    /**
	 * @return the entire length of the array in bytes
	 * @throws ByteArrayListException 
	 */
    long getLength() throws ByteArrayListException;

    /**
	 * this method marks the position, where the current part
	 * ends and a new one starts
	 * @throws ByteArrayListException 
	 */
    void markNextPart() throws ByteArrayListException;

    /**
	 * this method sets the read position to zero
	 * @throws ByteArrayListException 
	 */
    void resetReading() throws ByteArrayListException;

    /**
	 * fetches 'i' bytes into the 'buffer' and thereby
	 * increments the read position by 'i'
	 * @throws ByteArrayListException 
	 */
    int fetch(byte[] buffer, int i) throws ByteArrayListException;

    /**
	 * @return the number of parts
	 */
    int getNumberParts();

    /**
	 * @return the length of a part in bytes 
	 * @throws ByteArrayListException 
	 */
    long getPartLength(int partNr) throws ByteArrayListException;

    /**
	 * @return the start position of a part
	 */
    long getPartBeginPosition(int partNr);

    /**
	 * sets the read position to an new absolute value
	 * @throws ByteArrayListException 
	 */
    void setPosition(long partBeginPosition) throws ByteArrayListException;

    /**
	 * fetches a line from the current reading position. the
	 * line will stop with the given delimiter. the returned value
	 * will not contain this delimiter, but the new read position
	 * is set to next next character after the delimiter. 
	 * @throws ByteArrayListException 
	 */
    String fetchLine(byte delimiter) throws ByteArrayListException;

    /**
	 * reads the byte on the actual reading position and increments
	 * the reading position by one
	 * @throws ByteArrayListException 
	 */
    byte fetch() throws ByteArrayListException;

    /**
	 * @return the current reading position
	 * @throws ByteArrayListException 
	 */
    long getPosition() throws ByteArrayListException;

    /**
	 * decrements the current reading position by one
	 * @throws ByteArrayListException 
	 */
    void prev() throws ByteArrayListException;

    /**
	 * reads the byte on the actual reading position without setting
	 * a new position
	 * @throws ByteArrayListException 
	 */
    byte getActualByte() throws ByteArrayListException;

    /**
	 * appends one byte to the arrays end.
	 * @throws ByteArrayListException 
	 */
    void append(byte b) throws ByteArrayListException;

    /**
	 * after usage, you should call that in order to avoid
	 * memory leaks. after calling this method, the instance
	 * is unusable!
	 * @throws ByteArrayListException 
	 */
    void dispose() throws ByteArrayListException;

    void writeToDisk(String file) throws ByteArrayListException;
}
