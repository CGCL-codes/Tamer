package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * A partial implementation for String based ID3 fields
 */
public abstract class AbstractString extends AbstractDataType {

    /**
     * Creates a new  datatype
     *
     * @param identifier
     * @param frameBody
     */
    protected AbstractString(String identifier, AbstractTagFrameBody frameBody) {
        super(identifier, frameBody);
    }

    /**
     * Creates a new  datatype, with value
     *
     * @param identifier
     * @param frameBody
     * @param value
     */
    public AbstractString(String identifier, AbstractTagFrameBody frameBody, String value) {
        super(identifier, frameBody, value);
    }

    /**
     * Copy constructor
     *
     * @param object
     */
    protected AbstractString(AbstractString object) {
        super(object);
    }

    /**
     * Return the size in bytes of this datatype as it was/is held in file this
     * will be effected by the encoding type.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size in bytes of this data type.
     * This is set after writing the data to allow us to recalculate the size for
     * frame header.
     * @param size
     */
    protected void setSize(int size) {
        this.size = size;
    }

    /**
     * Return String representation of data type
     *
     * @return a string representation of the value
     */
    public String toString() {
        return (String) value;
    }

    /**
     * Check the value can be encoded with the specified encoding
     * @return
     */
    public boolean canBeEncoded() {
        byte textEncoding = this.getBody().getTextEncoding();
        String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
        CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();
        if (encoder.canEncode((String) value)) {
            return true;
        } else {
            logger.finest("Failed Trying to decode" + value + "with" + encoder.toString());
            return false;
        }
    }
}
