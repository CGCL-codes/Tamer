package org.t2framework.t2.format.amf3.io.writer.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import org.t2framework.t2.format.amf3.io.writer.Amf3DataWriter;
import org.t2framework.t2.format.amf3.io.writer.Amf3DataWriterFactory;

public abstract class AbstractAmf3TypedObjectWriterImpl extends AbstractAmf3ObjectWriterImpl {

    protected Amf3DataWriterFactory writerFactory;

    public void setWriterFactory(final Amf3DataWriterFactory writerFactory) {
        this.writerFactory = writerFactory;
    }

    protected void writeObjectElement(final Object value, final DataOutputStream outputStream) throws IOException {
        final Amf3DataWriter dataWriter = writerFactory.createAmf3DataWriter(value);
        dataWriter.writeAmf3Data(value, outputStream);
    }
}
