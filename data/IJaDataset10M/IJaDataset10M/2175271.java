package de.intarsys.pdf.font;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import de.intarsys.pdf.cos.COSBasedObject;
import de.intarsys.pdf.cos.COSName;
import de.intarsys.pdf.cos.COSObject;
import de.intarsys.pdf.cos.COSStream;

/**
 * A character map. This object can map from codepoints to CID's which can be
 * used to select glyphs in a CID keyed font object.
 * 
 */
public abstract class CMap extends COSBasedObject {

    /**
	 * The meta class implementation
	 */
    public static class MetaClass extends COSBasedObject.MetaClass {

        protected MetaClass(Class instanceClass) {
            super(instanceClass);
        }

        @Override
        protected COSBasedObject doCreateCOSBasedObject(COSObject object) {
            if (object instanceof COSName) {
                CMap result = null;
                result = IdentityCMap.getSingleton((COSName) object);
                if (result == null) {
                    result = NamedCMap.loadCMap((COSName) object);
                }
                if (result == null) {
                    result = IdentityCMap.SINGLETON;
                }
                return result;
            } else if (object instanceof COSStream) {
                return new InternalCMap(object);
            } else {
                throw new IllegalArgumentException("CMap must be defined using COSStream or COSName");
            }
        }
    }

    /** The meta class instance */
    public static final MetaClass META = new MetaClass(MetaClass.class.getDeclaringClass());

    public static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result = (result << 8) + (bytes[i] & 0xFF);
        }
        return result;
    }

    public static int toInt(byte[] bytes, int offset, int length) {
        int result = 0;
        int end = offset + length;
        for (int i = offset; i < end; i++) {
            result = (result << 8) + (bytes[i] & 0xFF);
        }
        return result;
    }

    protected CMap(COSObject object) {
        super(object);
    }

    /**
	 * Get the character for the codepoint or -1 if not available.
	 * 
	 * @param codepoint
	 *            The codepoint
	 * 
	 * @return Get the character for the codepoint or -1 if not available.
	 */
    public abstract int getDecoded(int codepoint);

    /**
	 * Get the codepoint for the the character or -1 if invalid.
	 * 
	 * @param character
	 *            The character to look up.
	 * 
	 * @return Get the codepoint for the the character or -1 if invalid.
	 */
    public abstract int getEncoded(int character);

    /**
	 * Get the next decoded character from the input stream. This method reads
	 * as much bytes as needed by the encoding and returns the decoded
	 * character.
	 * 
	 * @param is
	 *            The input stream with encoded data.
	 * @return The next decoded character from the input stream.
	 * @throws IOException
	 */
    public abstract int getNextDecoded(InputStream is) throws IOException;

    /**
	 * The next codepoint from the input stream. This method reads as much bytes
	 * as needed by the encoding and returns the complete multibyte codepoint.
	 * 
	 * @param is
	 *            The input stream with encoded data.
	 * @return The next codepoint from the input stream.
	 * @throws IOException
	 */
    public abstract int getNextEncoded(InputStream is) throws IOException;

    /**
	 * Put the next character onto the input stream after encoding. This method
	 * writes as much bytes as needed by the encoding.
	 * 
	 * @param os
	 *            The stream to write the bytes.
	 * 
	 * @param character
	 *            The character to be encoded.
	 * 
	 * @throws IOException
	 */
    public abstract void putNextDecoded(OutputStream os, int character) throws IOException;

    /**
	 * Put the next codepoint onto the input stream. This method writes as much
	 * bytes as needed by the encoding.
	 * 
	 * @param os
	 *            The stream to write the bytes.
	 * 
	 * @param codepoint
	 *            The codepoint.
	 * @throws IOException
	 */
    public abstract void putNextEncoded(OutputStream os, int codepoint) throws IOException;
}
