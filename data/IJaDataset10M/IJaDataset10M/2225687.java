package com.avaje.ebeaninternal.server.lucene;

import org.apache.lucene.document.Document;

/**
 * Safe for a single thread to write to the document and index.
 * <p>
 * This can cache/reuse Fields knowing that it will only be used in a single
 * thread (not concurrently).
 * </p>
 * 
 * @author rbygrave
 */
public interface DocFieldWriter {

    /**
     * Write the bean properties value(s) to the document (via fields).
     */
    public void writeValue(Object bean, Document document);
}
