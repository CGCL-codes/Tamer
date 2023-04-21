package org.openfast.template.type.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.openfast.BitVector;
import org.openfast.BitVectorValue;
import org.openfast.Global;
import org.openfast.ScalarValue;
import org.openfast.error.FastConstants;

public final class BitVectorType extends TypeCodec {

    private static final long serialVersionUID = 1L;

    BitVectorType() {
    }

    /**
     * Takes a ScalarValue object, and converts it to a byte array
     * 
     * @param value
     *            The ScalarValue to be encoded
     * @return Returns a byte array of the passed object
     */
    public byte[] encodeValue(ScalarValue value) {
        return ((BitVectorValue) value).value.getBytes();
    }

    /**
     * Reads in a stream of data and stores it to a BitVector object
     * 
     * @param in
     *            The InputStream to be decoded
     * @return Returns a new BitVector object with the data stream as an array
     */
    public ScalarValue decode(InputStream in) {
        int byt;
        ByteArrayOutputStream buffer = Global.getBuffer();
        do {
            try {
                byt = in.read();
                if (byt < 0) {
                    Global.handleError(FastConstants.END_OF_STREAM, "The end of the input stream has been reached.");
                    return null;
                }
            } catch (IOException e) {
                Global.handleError(FastConstants.IO_ERROR, "A IO error has been encountered while decoding.", e);
                return null;
            }
            buffer.write(byt);
        } while ((byt & 0x80) == 0);
        return new BitVectorValue(new BitVector(buffer.toByteArray()));
    }

    /**
     * 
     * @return Returns null
     */
    public ScalarValue fromString(String value) {
        return null;
    }

    /**
     * 
     * @return Returns a default BitVectorValue object
     */
    public ScalarValue getDefaultValue() {
        return new BitVectorValue(new BitVector(0));
    }

    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass();
    }
}
