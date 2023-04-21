package org.apache.batik.util.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents an object which decodes ISO-8859-1 characters from
 * a stream of bytes.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: ISO_8859_1Decoder.java,v 1.1 2005/11/21 09:51:37 dev Exp $
 */
public class ISO_8859_1Decoder extends AbstractCharDecoder {

    /**
     * Creates a new ISO_8859_1Decoder.
     */
    public ISO_8859_1Decoder(InputStream is) {
        super(is);
    }

    /**
     * Reads the next character.
     * @return a character or END_OF_STREAM.
     */
    public int readChar() throws IOException {
        if (position == count) {
            fillBuffer();
        }
        if (count == -1) {
            return -1;
        }
        return buffer[position++] & 0xff;
    }
}
