package com.volantis.mcs.dom2theme.impl;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.debug.StyledDocumentLogger;
import com.volantis.mcs.dom2theme.StyledDOMStyleAttributeRenderer;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.optimizer.StyledDOMOptimizer;

/**
 * Default implementation of {@link StyledDOMStyleAttributeRenderer}
 * Responsible for converting styles to 'style' attribute of element 
 */
public class DefaultStyledDOMStyleAttributeRenderer implements StyledDOMStyleAttributeRenderer {

    /**
     * Optimize styled document
     */
    private StyledDOMOptimizer optimizer;

    /**
     * Rewrite style as 'style' attribute value
     */
    private OutputStyledElementIteratee rewriter;

    /**
     * Default constructor with rewriter and optimizer
     * @param rewriter
     * @param optimizer
     */
    public DefaultStyledDOMStyleAttributeRenderer(OutputStyledElementIteratee rewriter, StyledDOMOptimizer optimizer) {
        this.optimizer = optimizer;
        this.rewriter = rewriter;
    }

    public void renderStyleAttributes(Document styledDom) {
        StyledDocumentLogger.logDocument(styledDom);
        OutputStyledElementList elementList = optimizer.optimize(styledDom);
        elementList.iterate(this.rewriter);
    }
}
