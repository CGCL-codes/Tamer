package be.belgium.eid.eidcommon;

import java.util.AbstractMap;
import java.util.HashMap;

/**
 * The TLV performs operations according to the <b>Tag-Length-Value protocol</b>.
 * This is the way how the data is stored on the eID smart card. Files are
 * represented as sequences of TLV elements that consist of a tag name to
 * uniquely identify the data that is represented. This tag is followed by the
 * length of the data represented by the tag. Finally the data itself is stored
 * as a sequence of bytes. <br />
 * <b>E.g.:</b> imagine we want to store the first name and the last name of a
 * person on a smart card or any other low-level device. Then we store the name
 * 'Will Hunting' as a sequence of bytes (delimited by ;'s) as follows:
 * '0;4;87;105;108;108;1;7;72;117;110;116;105;110;103'. Here the first byte is
 * the tag for the first name, the next byte is the length of the first name and
 * the next 4 bytes are the decimal representations of the ASCII characters of
 * which the first name exists. After these bytes the same thing is done for the
 * last name which has tag 1, length 7 and 7 decimal numbers representing the
 * data. <br />
 * For more information click <a
 * href="http://en.wikipedia.org/wiki/Type-length-value" target="_blank">here</a>.
 * 
 * @author Kristof Overdulve
 * @version 1.0.0 25 Nov 2007
 */
public class TLV {

    /**
	 * contains the mapping of the TLV element tags to the data that is
	 * represented by those tags
	 */
    private final transient AbstractMap<Byte, byte[]> fTLVElements;

    /**
	 * Initializes the TLV with its default values. To input data in the TLV we
	 * have to perform {@link be.belgium.eid.eidcommon.TLV#parse(byte[])}. Do
	 * this before trying to fetch data from the buffer.
	 */
    public TLV() {
        fTLVElements = new HashMap<Byte, byte[]>();
    }

    /**
	 * Initializes the TLV by parsing the given character string that represents
	 * the data.
	 * 
	 * @param characterStream
	 *            is the stream of characters to parse into a TLV map structure
	 */
    public TLV(final byte[] characterStream) {
        fTLVElements = new HashMap<Byte, byte[]>();
        parse(characterStream);
    }

    /**
	 * Parses the input stream into a map structure to make the processing of
	 * the data easier. This method can be used multiple times with different
	 * byteacterStreams to combine multiple input byteacter streams. Watch out
	 * not to redefine keys that you don't want to redefine.
	 * 
	 * @param byteacterStream
	 *            is the stream of byteacters to parse into a TLV map structure
	 */
    public void parse(final byte[] byteacterStream) {
        int i = 0;
        while (i < byteacterStream.length) {
            final byte tag = byteacterStream[i++];
            int lengthData = 0;
            if (i == byteacterStream.length) {
                break;
            } else {
                lengthData = byteacterStream[i];
            }
            if ((tagData(tag) == null) || (lengthData > 0)) {
                byte[] data;
                while (byteacterStream[i] == 0xFF) {
                    lengthData += byteacterStream[++i];
                }
                i++;
                data = new byte[lengthData];
                for (int j = 0; j < lengthData; j++) {
                    data[j] = byteacterStream[i + j];
                }
                fTLVElements.put(tag, data);
                i += lengthData;
            } else {
                i++;
            }
        }
    }

    /**
	 * Returns the data of the given tag. Returns null if there is no data
	 * associated with this tag.
	 * 
	 * @param tag
	 *            is the tag to identify the data to return
	 * @return the data associated with the given tag
	 */
    public byte[] tagData(final byte tag) {
        return fTLVElements.get(tag);
    }

    /**
	 * Returns the map structure of the TLV input stream. The key is the so
	 * called 'tag' uniquely defining the associated data.
	 * 
	 * @return the map representing the input stream in a more structured way.
	 */
    public AbstractMap<Byte, byte[]> getTLVElements() {
        return fTLVElements;
    }
}
