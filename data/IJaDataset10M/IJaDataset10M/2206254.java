package com.teknokala.xtempore.xml.util;

import java.util.List;
import com.teknokala.xtempore.xml.BufferedXMLOutput;
import com.teknokala.xtempore.xml.Mark;
import com.teknokala.xtempore.xml.XMLEvent;
import com.teknokala.xtempore.xml.XMLOutput;
import com.teknokala.xtempore.xml.XMLStreamException;

/**
 * Wrapper class which provides output buffering to output streams which doesn't support it.
 *
 * @author Timo Santasalo <timo.santasalo@teknokala.com>
 */
public class FakeBuffer implements BufferedXMLOutput {

    private final XMLOutput out;

    private boolean failOnBufferOperation = false;

    public FakeBuffer(final XMLOutput out) {
        super();
        this.out = out;
    }

    public void setFailOnBufferOperation(boolean failOnBufferOperation) {
        this.failOnBufferOperation = failOnBufferOperation;
    }

    public boolean isFailOnBufferOperation() {
        return failOnBufferOperation;
    }

    public String getSystemId() {
        return out.getSystemId();
    }

    public void write(List<XMLEvent> events) throws XMLStreamException {
        out.write(events);
    }

    public void write(XMLEvent[] events) throws XMLStreamException {
        out.write(events);
    }

    public void write(XMLEvent ev) throws XMLStreamException {
        out.write(ev);
    }

    public void close() {
        out.close();
    }

    private void onBufferOperation() throws XMLStreamException {
        if (failOnBufferOperation) {
            throw new XMLStreamException("Buffer operation encountered!");
        }
    }

    public Mark mark() throws XMLStreamException {
        return new Mark() {

            public void release() throws XMLStreamException {
                onBufferOperation();
            }

            public void reset() throws XMLStreamException {
                onBufferOperation();
            }

            public void resetAndRelease() throws XMLStreamException {
                onBufferOperation();
            }

            public void update() throws XMLStreamException {
                onBufferOperation();
            }
        };
    }
}
