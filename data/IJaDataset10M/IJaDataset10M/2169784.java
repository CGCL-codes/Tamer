package net.sf.xml2cb.cobol.engine.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import javax.xml.stream.XMLStreamException;
import net.sf.xml2cb.cobol.engine.CobolArea;
import net.sf.xml2cb.core.BufferContext;
import net.sf.xml2cb.core.io.XmlReader;
import net.sf.xml2cb.core.io.XmlWriter;
import net.sf.xml2cb.util.Stack;
import org.xml.sax.ContentHandler;

class CobolFillerImpl implements CobolArea {

    private final int length;

    public CobolFillerImpl(int length) {
        this.length = length;
    }

    public String getName() {
        return null;
    }

    public int getLength() {
        return length;
    }

    public void unmarshall(BufferContext flat, XmlWriter out) {
        flat.skip(length);
    }

    public void unmarshall(BufferContext flat, ContentHandler contentHandler) {
        flat.skip(length);
    }

    public void marshall(XmlReader in, BufferContext flat) throws XMLStreamException, IOException {
        flat.skip(length);
    }

    public void initialize(ByteBuffer buffer) {
        buffer.position(buffer.position() + length);
    }

    public void startEvent(String name, Stack stack, ByteBuffer byteBuffer) {
        throw new UnsupportedOperationException();
    }

    public void endEvent(String name, Stack stack, ByteBuffer byteBuffer) {
        throw new UnsupportedOperationException();
    }

    public void characterEvent(CharBuffer charBuffer, ByteBuffer byteBuffer) {
        throw new UnsupportedOperationException();
    }
}
