package net.sf.gps2nmea;

/**
 * Generic NMEA sentence factory.
 * 
 * <p>This class aims to provide NMEA talkers with a fast and efficient way to
 * craft NMEA sentences. It can either be used as one-instance-per-sentence or,
 * preferably, as a reusable sentence factory.<br>
 * In any case, {@link #open(String)} must be called before each new sentence.
 * NMEA fields can then be added, one after another. Finally, {@link #close()}
 * adds checksum and returns the complete sentence.<br>
 * Implementation wise, this class is a fast wrapper around a single
 * {@link StringBuffer} instance.</p>
 * <p>Note: In order to optimize execution speed, no checks are performed; it is
 * perfectly possible to use this class to craft illegal NMEA sentences.</p>
 * 
 * @see    NmeaLocation
 * @author Olivier Cornu <o.cornu@gmail.com>
 */
public final class NmeaSentence {

    /**
	 * NMEA protocol: sentence start character.
	 */
    public static final char START = '$';

    /**
	 * NMEA protocol: field separator character.
	 */
    public static final char SEPARATOR = ',';

    /**
	 * NMEA protocol: checksum separator character.
	 */
    public static final char CHECKSUM = '*';

    /**
	 * NMEA protocol: sentence end string.
	 */
    public static final String END = "\r\n";

    private static final short LONGEST = 256;

    private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private final StringBuffer buffer;

    private final int initlen;

    private final int initchk;

    private int checksum = 0;

    /**
	 * Create a new NMEA sentence factory for the specified talker.
	 * @param talkerId	NMEA talker ID.
	 */
    public NmeaSentence(String talkerId) {
        this(talkerId, LONGEST);
    }

    /**
	 * Create a new NMEA sentence factory of specified <code>length</code>
	 * for the specified talker.
	 * <p>Note: <code>length</code> is used as the initial sentence buffer
	 * length. It is typically equal to the longest sentence this factory may
	 * have to craft, yet it is not a limitation: longer sentences may still be
	 * crafted.</p>
	 * @param talkerId	NMEA talker ID.
	 * @param length	Initial buffer length.
	 */
    public NmeaSentence(String talkerId, int length) {
        buffer = new StringBuffer(length);
        buffer.append(START);
        append(talkerId);
        initlen = buffer.length();
        initchk = checksum;
    }

    private void append(char datum) {
        buffer.append(datum);
        checksum ^= datum;
    }

    private void append(String data) {
        for (int i = 0, len = data.length(); i < len; i++) append(data.charAt(i));
    }

    /**
	 * Open a new sentence of type <code>type</code>.
	 * @param type	Sentence type (for example: GGA).
	 */
    public void open(String type) {
        buffer.setLength(initlen);
        checksum = initchk;
        append(type);
    }

    /**
	 * Add an empty field.
	 * @return		Current sentence factory.
	 */
    public NmeaSentence add() {
        append(SEPARATOR);
        return this;
    }

    /**
	 * Add a single character field.
	 * @param field	Single character field.
	 * @return		Current sentence factory.
	 */
    public NmeaSentence add(char field) {
        append(SEPARATOR);
        append(field);
        return this;
    }

    /**
	 * Add a string field.
	 * @param field	String field.
	 * @return		Current sentence factory.
	 */
    public NmeaSentence add(String field) {
        append(SEPARATOR);
        append(field);
        return this;
    }

    /**
	 * Add an integer field.
	 * @param field Integer field.
	 * @return		Current sentence factory.
	 */
    public NmeaSentence add(int field) {
        return add(Integer.toString(field));
    }

    /**
	 * Close the current sentence, performing any necessary step (like appending
	 * its checksum).
	 * @return		Complete NMEA sentence.
	 */
    public String close() {
        buffer.append(CHECKSUM);
        buffer.append(HEX[checksum >> 4]).append(HEX[checksum & 0xf]);
        buffer.append(END);
        return buffer.toString();
    }

    /**
	 * Return the current content of the sentence buffer.
	 * <p>Note: Unless it has previously been closed, this content is not a
	 * complete NMEA sentence.</p>
	 * @return		Content of the sentence buffer.
	 */
    public String toString() {
        return buffer.toString();
    }
}
