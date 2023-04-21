package org.restlet.client.representation;

import java.io.IOException;
import java.io.Reader;
import org.restlet.client.data.MediaType;
import org.restlet.client.engine.io.BioUtils;

/**
 * Representation based on a BIO stream.
 * 
 * @author Jerome Louvel
 */
public abstract class StreamRepresentation extends Representation {

    /**
     * Constructor.
     * 
     * @param mediaType
     *            The media type.
     */
    public StreamRepresentation(MediaType mediaType) {
        super(mediaType);
    }

    @Override
    public Reader getReader() throws IOException {
        return BioUtils.getReader(getStream(), getCharacterSet());
    }
}
