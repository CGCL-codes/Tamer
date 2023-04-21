package org.t2framework.t2.format.amf3.io.writer.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Amf3IteratorWriterImpl extends Amf3ArrayWriterImpl {

    public boolean isWritableValue(final Object value) {
        return Iterator.class.isAssignableFrom(value.getClass());
    }

    @SuppressWarnings("unchecked")
    protected final void writeInlineObject(final Object object, final DataOutputStream outputStream) throws IOException {
        writeIterator((Iterator<Object>) object, outputStream);
    }

    private final void writeIterator(final Iterator<Object> value, final DataOutputStream outputStream) throws IOException {
        final ArrayList<Object> list = new ArrayList<Object>();
        while (value.hasNext()) {
            list.add(value.next());
        }
        super.writeInlineObject(list.toArray(new Object[list.size()]), outputStream);
    }
}
