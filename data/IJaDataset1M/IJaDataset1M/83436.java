package com.gestioni.adoc.aps.system.services.pec.mail.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageIN extends BaseMailMessage {

    public void setRefMessageId(String refMessageId) {
        this._refMessageId = refMessageId;
    }

    public String getRefMessageId() {
        return _refMessageId;
    }

    public void setDocument(MessageAttachment document) {
        this._document = document;
    }

    public MessageAttachment getDocument() {
        return _document;
    }

    public String getEmailMittente() {
        return _emailMittente;
    }

    public void setEmailMittente(String emailMittente) {
        this._emailMittente = emailMittente;
    }

    public String getNomeMittente() {
        return _nomeMittente;
    }

    public void setNomeMittente(String nomeMittente) {
        this._nomeMittente = nomeMittente;
    }

    public Date getDataRicezione() {
        return _dataRicezione;
    }

    public void setDataRicezione(Date dataRicezione) {
        this._dataRicezione = dataRicezione;
    }

    public void setDataSpedizione(Date dataSpedizione) {
        this._dataSpedizione = dataSpedizione;
    }

    public Date getDataSpedizione() {
        return _dataSpedizione;
    }

    public void setAttachs(List<MessageAttachment> attachs) {
        this._attachs = attachs;
    }

    public List<MessageAttachment> getAttachs() {
        return _attachs;
    }

    public String getTipoMail() {
        return tipoMail;
    }

    public void setTipoMail(String tipoMail) {
        this.tipoMail = tipoMail;
    }

    private List<MessageAttachment> _attachs = new ArrayList<MessageAttachment>();

    private MessageAttachment _document;

    private String _refMessageId;

    private String _emailMittente;

    private String _nomeMittente;

    private Date _dataRicezione;

    private Date _dataSpedizione;

    private String tipoMail;

    public static final int STATUS_LETTA = 0;

    public static final int STATUS_LETTA_INTEROPERABIlITA_DA_PROTOCOLLARE = 1;

    public static final int STATUS_LETTA_E_PROTOCOLLATA = 2;

    public static final int STATUS_LETTA_TIPO_RICEVUTA = 3;

    public static final int STATUS_LETTA_POSSIEDE_ERRORE = 4;
}
