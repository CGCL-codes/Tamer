package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Represents a single byte as a number
 * <p/>
 * <p>Usually single byte fields are used as a boolean field, but not always so we dont do this conversion
 */
public class Mp4TagByteField extends Mp4TagTextField {

    public static String TRUE_VALUE = "1";

    private int realDataLength;

    private byte[] bytedata;

    /**
     * Create new field
     * <p/>
     * Assume length of 1 which is correct for most but not all byte fields
     *
     * @param id
     * @param value is a String representation of a number
     * @throws org.jaudiotagger.tag.FieldDataInvalidException
     */
    public Mp4TagByteField(Mp4FieldKey id, String value) throws FieldDataInvalidException {
        this(id, value, 1);
    }

    /**
     * Create new field with known length
     *
     * @param id
     * @param value is a String representation of a number
     * @param realDataLength
     * @throws org.jaudiotagger.tag.FieldDataInvalidException
     */
    public Mp4TagByteField(Mp4FieldKey id, String value, int realDataLength) throws FieldDataInvalidException {
        super(id.getFieldName(), value);
        this.realDataLength = realDataLength;
        try {
            Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            throw new FieldDataInvalidException("Value of:" + value + " is invalid for field:" + id);
        }
    }

    /**
     * Construct from rawdata from audio file
     *
     * @param id
     * @param raw
     * @throws UnsupportedEncodingException
     */
    public Mp4TagByteField(String id, ByteBuffer raw) throws UnsupportedEncodingException {
        super(id, raw);
    }

    public Mp4FieldType getFieldType() {
        return Mp4FieldType.INTEGER;
    }

    /**
     * Return raw data bytes
     * <p/>
     * TODO this code should be done better so generalised to any length
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    protected byte[] getDataBytes() throws UnsupportedEncodingException {
        if (bytedata != null) {
            return bytedata;
        }
        switch(realDataLength) {
            case 2:
                {
                    Short shortValue = new Short(content);
                    byte rawData[] = Utils.getSizeBEInt16(shortValue);
                    return rawData;
                }
            case 1:
                {
                    Short shortValue = new Short(content);
                    byte rawData[] = new byte[1];
                    rawData[0] = shortValue.byteValue();
                    return rawData;
                }
            case 4:
                {
                    Integer intValue = new Integer(content);
                    byte rawData[] = Utils.getSizeBEInt32(intValue);
                    return rawData;
                }
            default:
                {
                    throw new RuntimeException(id + ":" + realDataLength + ":" + "Dont know how to write byte fields of this length");
                }
        }
    }

    protected void build(ByteBuffer data) throws UnsupportedEncodingException {
        Mp4BoxHeader header = new Mp4BoxHeader(data);
        Mp4DataBox databox = new Mp4DataBox(header, data);
        dataSize = header.getDataLength();
        realDataLength = dataSize - Mp4DataBox.PRE_DATA_LENGTH;
        bytedata = databox.getByteData();
        content = databox.getContent();
    }
}
