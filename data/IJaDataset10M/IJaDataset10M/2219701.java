package net.hawk.digiextractor.mpeg2.descriptor;

import java.io.UnsupportedEncodingException;
import net.hawk.digiextractor.util.BitBuffer;
import net.hawk.digiextractor.util.Helper;

/**
 * The Class ShortEventDescriptor.
 * 
 * @see EN 300 468 (6.2.36)
 */
public class ShortEventDescriptor extends AbstractMPEG2Descriptor {

    /**
	 * Length of Language Code in Bytes.
	 */
    private static final int LC_LENGTH = 3;

    /** The language code. */
    private final transient String languageCode;

    /** The event name. */
    private final transient String eventName;

    /** The text. */
    private final transient String text;

    /**
     * Instantiates a new short event descriptor.
     * 
     * @param buffer the b
     * 
     * @throws DescriptorParsingException the descriptor parsing exception
     * @throws UnsupportedEncodingException the exception
     */
    public ShortEventDescriptor(final BitBuffer buffer) throws DescriptorParsingException, UnsupportedEncodingException {
        super(buffer);
        byte[] helper = new byte[LC_LENGTH];
        buffer.getBytes(helper);
        languageCode = Helper.parseCharacterArray(helper);
        final int eventNameLength = buffer.getByte();
        helper = new byte[eventNameLength];
        buffer.getBytes(helper);
        eventName = Helper.parseCharacterArray(helper);
        final int textLength = buffer.getByte();
        helper = new byte[textLength];
        buffer.getBytes(helper);
        text = Helper.parseCharacterArray(helper);
    }

    /** 
     * to String.
     * 
     * @return the String
     * @see net.hawk.digiextractor.mpeg2.descriptor.MPEG2Descriptor#toString()
     */
    public final String toString() {
        final StringBuffer sbuf = new StringBuffer("\t\t***ShortEventDescriptor***\n\t\t");
        sbuf.append(getLengthString());
        sbuf.append(String.format("\t\tLanguage: %s\n", languageCode));
        sbuf.append(String.format("\t\tEvent Name: %s\n", eventName));
        sbuf.append(String.format("\t\tEvent Text: %s\n", text));
        return sbuf.toString();
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public final String getName() {
        return eventName;
    }

    /**
     * Gets the text.
     * 
     * @return the text
     */
    public final String getText() {
        return text;
    }
}
