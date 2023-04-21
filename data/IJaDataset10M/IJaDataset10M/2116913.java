package org.windu2b.jcaddie.model;

import java.io.*;

/**
 * Content for files, images...
 * 
 * @author Emmanuel Puybaret, windu.2b
 */
public interface Content extends Serializable {

    /**
	 * Returns an input stream to a content.
	 * 
	 * @throws IOException
	 *             If the input stream can't be opened.
	 */
    InputStream openStream() throws IOException;
}
