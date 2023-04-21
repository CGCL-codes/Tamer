package com.volantis.mcs.dom2theme;

import com.volantis.mcs.dom.Document;

/**
 * Optimizes the styles in the document.
 */
public interface StyledDocumentOptimizer {

    /**
     * Optimize the document.
     *
     * <p>Any property values that can be inferred (inherited, initial) and
     * that have not been explicitly specified are cleared.</p>
     *
     * @param document The document whose styles should be normalized.
     */
    public void optimizeDocument(Document document);
}
