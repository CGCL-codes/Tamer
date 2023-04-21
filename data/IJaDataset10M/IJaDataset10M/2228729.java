package gate.compound.impl;

import gate.ProcessingResource;
import gate.Resource;
import gate.compound.CompoundDocument;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;

/**
 * This PR deletes a member document from the compound document.
 * 
 * @author niraj
 */
public class DeleteMemberPR extends AbstractLanguageAnalyser implements ProcessingResource {

    private static final long serialVersionUID = 2001251690576486313L;

    private String documentID;

    public Resource init() throws ResourceInstantiationException {
        return this;
    }

    public void reInit() throws ResourceInstantiationException {
        init();
    }

    public void execute() throws ExecutionException {
        if (document == null) {
            throw new ExecutionException("Document is null!");
        }
        if (document instanceof CompoundDocument) ((CompoundDocument) document).removeDocument(documentID); else System.err.println("Since the document is not an instance of CompoundDocument, No changes made for the document :" + document.getName());
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
