package org.lineman.xml;

import java.awt.datatransfer.DataFlavor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.ActivationDataFlavor;
import javax.activation.MimeType;
import org.lineman.activation.AbstractClassDataContentHandler;

/**
 * A data content handler for beans generated using {@link XMLEncoder} and
 * {@link XMLDecoder}.
 */
public class BeanDataContentHandler extends AbstractClassDataContentHandler<Object> {

    /**
     * The description used for {@link DataFlavor}s handled by this.
     */
    public static final String DESCRIPTION = "XML-encoded Java Bean";

    /**
     * Create a new data content handler for beans.
     */
    public BeanDataContentHandler() {
        super(Object.class, new DataFlavor[] { new ActivationDataFlavor(Object.class, "application/xml", DESCRIPTION), new ActivationDataFlavor(Object.class, "text/xml", DESCRIPTION) });
    }

    /**
     * Reads a bean using {@link XMLDecoder}.
     * @param input {@inheritDoc}
     * @param mime Ignored.
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    protected Object readRepresentation(final InputStream input, final MimeType mime) throws IOException {
        final XMLDecoder decoder = new XMLDecoder(input);
        try {
            return decoder.readObject();
        } finally {
            decoder.close();
        }
    }

    /**
     * Writes a bean using {@link XMLEncoder}.
     * @param source {@inheritDoc}
     * @param mime Ignored
     * @param output {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    protected void writeRepresentation(final Object source, final MimeType mime, final OutputStream output) throws IOException {
        final XMLEncoder encoder = new XMLEncoder(output);
        try {
            encoder.writeObject(source);
        } finally {
            encoder.close();
        }
    }
}
