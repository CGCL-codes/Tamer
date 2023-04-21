package com.hadeslee.audiotag.tag.datatype;

import com.hadeslee.audiotag.tag.id3.AbstractTagFrameBody;
import com.hadeslee.audiotag.tag.id3.valuepair.TextEncoding;

/**
 * Represents a String which is not delimited by null character with fixed text encoding.
 * 
 * This type of String will usually only be used when it is the last field within a frame, when reading the remainder of the byte array will
 * be read, when writing the frame will accomodate the required size for the String. The String will be encoded
 * using the default encoding regardless of what encoding may be specified in the framebody
 */
public class StringSizeTerminated extends TextEncodedStringSizeTerminated {

    /**
     * Creates a new ObjectStringSizeTerminated datatype.
     *
     * @param identifier  identifies the frame type
     */
    public StringSizeTerminated(String identifier, AbstractTagFrameBody frameBody) {
        super(identifier, frameBody);
    }

    public StringSizeTerminated(StringSizeTerminated object) {
        super(object);
    }

    public boolean equals(Object obj) {
        if (obj instanceof StringSizeTerminated == false) {
            return false;
        }
        return super.equals(obj);
    }

    protected String getTextEncodingCharSet() {
        return TextEncoding.CHARSET_ISO_8859_1;
    }
}
