package org.servingMathematics.mathqti.domImpl;

import org.servingMathematics.mathqti.dom.MathQTIDocument;
import org.servingMathematics.qti.domImpl.QTIDocumentImpl;
import org.w3c.dom.Document;

/**
 * An implementation of a MathQTI document (this is analogus to the {@link org.w3c.dom.Document Document}
 * implementation of the W3C DOM. It is built on top of the {@link org.servingMathematics.qti.domImpl.QTIDocumentImpl QTIDocument implementation}.
 * 
 * @author <a href="mailto:d.may@imperial.ac.uk">Daniel J. R. May</a>
 * @version 0.2, 29-Mar-2006
 */
public class MathQTIDocumentImpl extends QTIDocumentImpl implements MathQTIDocument {

    public MathQTIDocumentImpl(Document document) {
        super(document);
    }
}
