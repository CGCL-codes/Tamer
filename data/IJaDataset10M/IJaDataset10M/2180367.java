package org.t2framework.t2.format.amf3.io.writer.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import org.t2framework.t2.format.amf3.Amf3Constants;
import org.t2framework.t2.format.amf3.type.Amf3TypeSpecification;

public class Amf3ArrayWriterImpl extends AbstractAmf3TypedObjectWriterImpl {

    public final int getDataTypeValue() {
        return Amf3TypeSpecification.ARRAY;
    }

    public boolean isWritableValue(final Object value) {
        return (value instanceof Object[]);
    }

    protected void writeInlineObject(final Object object, final DataOutputStream outputStream) throws IOException {
        writeArrayElements((Object[]) object, outputStream);
    }

    private final void writeArrayElements(final Object[] array, final DataOutputStream outputStream) throws IOException {
        addObjectReference(array);
        final int arrayDef = array.length << 1 | Amf3Constants.OBJECT_INLINE;
        writeIntData(arrayDef, outputStream);
        outputStream.writeByte(Amf3Constants.EMPTY_STRING_DATA);
        for (int i = 0; i < array.length; i++) {
            writeObjectElement(array[i], outputStream);
        }
    }
}
