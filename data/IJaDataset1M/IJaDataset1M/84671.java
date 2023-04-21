package com.volantis.mcs.dom.debug;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.output.AbstractCharacterEncoder;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Output debugging information for a "plain" document, including whitespace
 * but not including styles.
 */
public class DebugDocument {

    /**
     * A trivial character encoder.
     */
    private static final AbstractCharacterEncoder encoder = new AbstractCharacterEncoder() {

        public void encode(int c, Writer out) throws IOException {
            out.write(c);
        }
    };

    public String debug(final Document document) {
        return debug(new Executor() {

            public void execute(DOMDocumentOutputter outputter) throws IOException {
                outputter.output(document);
            }
        });
    }

    public String debug(final Element element) {
        return debug(new Executor() {

            public void execute(DOMDocumentOutputter outputter) throws IOException {
                outputter.output(element);
            }
        });
    }

    private String debug(Executor executor) {
        StringWriter writer = new StringWriter();
        DOMDocumentOutputter outputter = new DOMDocumentOutputter(new XMLDocumentWriter(writer), encoder);
        try {
            executor.execute(outputter);
        } catch (IOException e) {
            throw new ExtendedRuntimeException(e);
        }
        return writer.toString();
    }

    private interface Executor {

        void execute(DOMDocumentOutputter outputter) throws IOException;
    }
}
