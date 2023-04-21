package org.openfast.template.type.codec;

import org.openfast.ScalarValue;

public abstract class NotStopBitEncodedTypeCodec extends TypeCodec {

    private static final long serialVersionUID = 1L;

    public byte[] encode(ScalarValue value) {
        return encodeValue(value);
    }

    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass();
    }
}
