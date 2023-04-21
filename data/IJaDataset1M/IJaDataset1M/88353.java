package com.gestioni.adoc.apsadmin.documento.collegamento;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsException;
import com.gestioni.adoc.aps.system.AdocSystemConstants;
import com.gestioni.adoc.aps.system.services.documento.inoltro.IInoltroDocumentoManager;
import com.gestioni.adoc.aps.system.services.documento.inoltro.InoltroDocumento;
import com.gestioni.adoc.aps.system.services.documento.model.Documento;
import com.gestioni.adoc.aps.system.services.documento.stato.Stato;
import com.gestioni.adoc.apsadmin.documento.DocumentoFinderAction;
import com.opensymphony.xwork2.Action;

public class CollegaDocumentoAction extends DocumentoFinderAction implements ICollegaDocumentoAction {

    private String checkDocument(Documento documento) throws ApsException {
        String check = null;
        if (null == documento) {
            this.addActionError(this.getText("Message.documento.null"));
            return INPUT;
        }
        if (documento.getStato() == Stato.BLOCCATO) {
            this.addActionError(this.getText("Message.documento.bloccato"));
            return INPUT;
        }
        if (documento.getStato() == Stato.CHECKOUT) {
            this.addActionError(this.getText("Message.documento.checkout"));
            return INPUT;
        }
        if (0 != this.getInoltroId()) {
            InoltroDocumento inoltroDocumento = this.getInoltroDocumentoManager().getInoltro(this.getInoltroId());
            if (!inoltroDocumento.getTipologia().equalsIgnoreCase(AdocSystemConstants.INOLTRO_TIPOLOGIA_COMPETENZA)) {
                this.addActionError(this.getText("Error.documento.inoltro.mustbe.competenza"));
                return INPUT;
            }
        }
        return check;
    }

    public String entryCollegamento() {
        try {
            Documento documento = (Documento) this.getDocumentoManager().getDocument(this.getSelectedDocument());
            String checkDoc = this.checkDocument(documento);
            if (null != checkDoc) {
                return checkDoc;
            }
            this.setSelectedDocuments(new HashSet<String>(documento.getDocumentiCollegati()));
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "entryCollegamento");
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String searchDocumenti() {
        try {
            Documento documento = (Documento) this.getDocumentoManager().getDocument(this.getSelectedDocument());
            String checkDoc = this.checkDocument(documento);
            if (null != checkDoc) {
                return checkDoc;
            }
            EntitySearchFilter[] filters = this.createFilters();
            if (filters.length < 2 && (null == this.getVoceTitolario() || this.getVoceTitolario().trim().length() == 0) && (null == this.getNodofascicolo() || this.getNodofascicolo().trim().length() == 0)) {
                this.addActionError(this.getText("Error.filters.empty"));
                return INPUT;
            } else {
                this.setExecuteSearch(true);
            }
            this.setFilters(filters);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "searchDocumenti");
            return FAILURE;
        }
        return Action.SUCCESS;
    }

    @Override
    public String collegaDocumenti() {
        try {
            Documento documento = (Documento) this.getDocumentoManager().getDocument(this.getSelectedDocument());
            String checkDoc = this.checkDocument(documento);
            if (null != checkDoc) {
                return checkDoc;
            }
            List<String> collegatiList = new ArrayList<String>(this.getSelectedDocuments());
            if (collegatiList.contains(this.getSelectedDocument())) {
                collegatiList.remove(this.getSelectedDocument());
            }
            List<String> documentiCollegati = new ArrayList<String>(collegatiList);
            if (!collegatiList.isEmpty()) {
                Iterator<String> it = collegatiList.iterator();
                while (it.hasNext()) {
                    String docId = it.next();
                    Documento doc = (Documento) this.getDocumentoManager().getDocument(docId);
                    if (doc.getStato() == Stato.BLOCCATO) {
                        documentiCollegati.remove(docId);
                        List<Object> args = new ArrayList<Object>();
                        args.add(doc.getId());
                        args.add(doc.getTitolo());
                        this.addActionError(this.getText("Error.collegamento.documento.bloccato", args));
                    }
                }
            }
            documento.setDocumentiCollegati(documentiCollegati);
            this.getDocumentoManager().updateDocument(documento, this.getProfilo().getId());
            this.addActionMessage(this.getText("Message.documento.collegato.updated"));
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "collegaDocumenti");
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String removeCollegamento() {
        try {
            Documento documento = (Documento) this.getDocumentoManager().getDocument(this.getSelectedDocument());
            String checkDoc = this.checkDocument(documento);
            if (null != checkDoc) {
                return checkDoc;
            }
            List<String> documentiCollegati = new ArrayList<String>(documento.getDocumentiCollegati());
            if (null != this.getDocCollegato() && this.getDocCollegato().trim().length() > 0) {
                if (documentiCollegati.contains(this.getDocCollegato())) {
                    documentiCollegati.remove(this.getDocCollegato());
                }
            } else {
                this.addActionError(this.getText("Error.documento.collegato.null"));
                return INPUT;
            }
            documento.setDocumentiCollegati(documentiCollegati);
            this.getDocumentoManager().updateDocument(documento, this.getProfilo().getId());
            this.addActionMessage(this.getText("Message.documento.collegato.removed"));
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "removeCollegamento");
            return FAILURE;
        }
        return SUCCESS;
    }

    public void setSelectedDocument(String selectedDocument) {
        this._selectedDocument = selectedDocument;
    }

    public String getSelectedDocument() {
        return _selectedDocument;
    }

    public void setSelectedDocuments(Set<String> selectedDocuments) {
        this._selectedDocuments = selectedDocuments;
    }

    public Set<String> getSelectedDocuments() {
        return _selectedDocuments;
    }

    public void setDocCollegato(String docCollegato) {
        this._docCollegato = docCollegato;
    }

    public String getDocCollegato() {
        return _docCollegato;
    }

    public void setInoltroId(int inoltroId) {
        this._inoltroId = inoltroId;
    }

    public int getInoltroId() {
        return _inoltroId;
    }

    public void setInoltroDocumentoManager(IInoltroDocumentoManager inoltroDocumentoManager) {
        this._inoltroDocumentoManager = inoltroDocumentoManager;
    }

    protected IInoltroDocumentoManager getInoltroDocumentoManager() {
        return _inoltroDocumentoManager;
    }

    private String _selectedDocument;

    private Set<String> _selectedDocuments = new HashSet<String>();

    private String _docCollegato;

    private int _inoltroId;

    private IInoltroDocumentoManager _inoltroDocumentoManager;
}
