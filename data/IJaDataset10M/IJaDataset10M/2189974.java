package org.plazmaforge.bsolution.document.common;

import org.plazmaforge.bsolution.document.common.beans.Document;
import org.plazmaforge.bsolution.document.common.beans.DocumentType;
import org.plazmaforge.framework.core.exception.ApplicationException;

/** 
 * @author Oleh Hapon
 * $Id: IDocumentCreator.java,v 1.3 2010/12/05 07:56:03 ohapon Exp $
 */
public interface IDocumentCreator {

    String getId();

    Class getDocumentClass();

    void setDocumentClass(Class documentClass);

    DocumentType getDocumentType();

    void setDocumentType(DocumentType documentType);

    Document createDocument(Document parentDocument) throws ApplicationException;
}
