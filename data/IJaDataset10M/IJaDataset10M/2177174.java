package com.cusp.pt.service.model.impl;

import java.util.List;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import com.cusp.pt.helper.SpringHelper;
import com.cusp.pt.model.Document;
import com.cusp.pt.model.eao.DocumentEAO;
import com.cusp.pt.service.model.DocumentService;

@Name("documentService")
@Scope(ScopeType.APPLICATION)
public class DefaultDocumentService implements DocumentService {

    private DocumentEAO documentEAO;

    public List<Document> getDocumentList() {
        return getDocumentEAO().findAllDocuments();
    }

    public DocumentEAO getDocumentEAO() {
        if (documentEAO == null) {
            documentEAO = (DocumentEAO) SpringHelper.getBean("documentEAO");
        }
        return documentEAO;
    }

    public void setDocumentEAO(DocumentEAO documentEAO) {
        this.documentEAO = documentEAO;
    }
}
