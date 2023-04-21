package org.exist.indexing.bobo;

import org.exist.dom.QName;
import org.exist.util.XMLString;

/**
 * Extract text from an XML fragment to be indexed with Lucene.
 * This interface provides an additional abstraction to handle whitespace
 * between elements or ignore certain elements.
 */
public interface TextExtractor {

    public void configure(BoboConfig config, BoboIndexConfig idxConfig);

    public int startElement(QName name);

    public int endElement(QName name);

    public int characters(XMLString value);

    public float getBoost();

    public XMLString getText();
}
