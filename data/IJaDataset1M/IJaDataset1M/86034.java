package org.plazmaforge.bsolution.document.server.services;

import org.plazmaforge.bsolution.document.common.beans.Document;
import org.plazmaforge.bsolution.document.common.services.DocumentService;

/**
 * @author hapon
 *
 */
public class DocumentServiceImpl extends AbstractDocumentService implements DocumentService {

    protected Class getEntityClass() {
        return Document.class;
    }
}
