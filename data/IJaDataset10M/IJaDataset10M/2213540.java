package org.cleartk.util.cr.linereader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.util.ViewURIUtil;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * <p>
 */
public class DefaultLineHandler implements LineHandler {

    int count;

    public void initialize(UimaContext context) throws ResourceInitializationException {
        count = 1;
    }

    public void handleLine(JCas jCas, File rootFile, File file, String line) throws IOException, CollectionException {
        jCas.setSofaDataString(line, "text/plain");
        URI uri;
        try {
            uri = new URI(String.format("%s#%d", file.toURI().toString(), this.count));
        } catch (URISyntaxException e) {
            throw new CollectionException(e);
        }
        ViewURIUtil.setURI(jCas, uri);
        ++count;
    }
}
