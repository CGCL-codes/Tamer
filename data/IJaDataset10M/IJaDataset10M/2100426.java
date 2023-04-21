package org.wijiscommons.ssaf.integration.disclosurecontrol.cdcl;

import org.w3c.dom.Document;
import org.wijiscommons.cdcl.gatepoint.ProblemDocument;
import org.wijiscommons.cdcl.gatepoint.ProblemDocumentMetaData;

/**
 * This class represents a wrapper around the SSAF payload that needs to be redacted.
 * <p/>
 * It's a default implementation of the {@link ProblemDocument}.
 *
 * @author Pattabi Doraiswamy (http://pattabidoraiswamy.com)
 * @since Jan 29, 2009
 */
public class ProblemDocumentImpl implements ProblemDocument {

    private Document document;

    private ProblemDocumentMetaData problemDocumentMetaData;

    /**
     * A default constructor that initializes state with the parameters passed in. 
     * 
     * @param document The actual SSAF document payload that needs to be redacted.
     * @param problemDocumentMetaData Meta data about the message payload.
     */
    ProblemDocumentImpl(Document document, ProblemDocumentMetaData problemDocumentMetaData) {
        super();
        this.document = document;
        this.problemDocumentMetaData = problemDocumentMetaData;
    }

    /**
     * {@inheritDoc}
     */
    public Object getDocument() {
        return document;
    }

    /**
     * {@inheritDoc}
     */
    public ProblemDocumentMetaData getDocumentMetaData() {
        return problemDocumentMetaData;
    }
}
