package com.gestioni.adoc.aps.system.services.protocollo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;

public class ProtocolloRecord extends ApsEntityRecord {

    public String getEnteId() {
        return _enteId;
    }

    public void setEnteId(String enteId) {
        this._enteId = enteId;
    }

    public String getAooId() {
        return _aooId;
    }

    public void setAooId(String aooId) {
        this._aooId = aooId;
    }

    public String getAnno() {
        return _anno;
    }

    public void setAnno(String anno) {
        this._anno = anno;
    }

    public long getProgressivo() {
        return _progressivo;
    }

    public void setProgressivo(long progressivo) {
        this._progressivo = progressivo;
    }

    public String getSenso() {
        return _senso;
    }

    public void setSenso(String senso) {
        this._senso = senso;
    }

    public Date getDataProtocollazione() {
        return _dataProtocollazione;
    }

    public void setDataProtocollazione(Date dataProtocollazione) {
        this._dataProtocollazione = dataProtocollazione;
    }

    public Date getDataArrivo() {
        return _dataArrivo;
    }

    public void setDataArrivo(Date dataArrivo) {
        this._dataArrivo = dataArrivo;
    }

    public int getIdProtocollatore() {
        return _idProtocollatore;
    }

    public void setIdProtocollatore(int idProtocollatore) {
        this._idProtocollatore = idProtocollatore;
    }

    public String getOggetto() {
        return _oggetto;
    }

    public void setOggetto(String oggetto) {
        this._oggetto = oggetto;
    }

    public SmallContatto getMittente() {
        return _mittente;
    }

    public void setMittente(SmallContatto mittente) {
        this._mittente = mittente;
    }

    public int getStatoProtocollo() {
        return _statoProtocollo;
    }

    public void setStatoProtocollo(int statoProtocollo) {
        this._statoProtocollo = statoProtocollo;
    }

    public Date getDataAnnullamento() {
        return _dataAnnullamento;
    }

    public void setDataAnnullamento(Date dataAnnullamento) {
        this._dataAnnullamento = dataAnnullamento;
    }

    public String getMotivazioneAnnullamento() {
        return _motivazioneAnnullamento;
    }

    public void setMotivazioneAnnullamento(String motivazioneAnnullamento) {
        this._motivazioneAnnullamento = motivazioneAnnullamento;
    }

    public void setRiservato(Boolean riservato) {
        this._riservato = riservato;
    }

    public Boolean getRiservato() {
        return _riservato;
    }

    public void setDestinatari(List<SmallContatto> destinatari) {
        this._destinatari = destinatari;
    }

    public List<SmallContatto> getDestinatari() {
        return _destinatari;
    }

    private String _enteId;

    private String _aooId;

    private String _anno;

    private long _progressivo;

    private String _senso;

    private Date _dataProtocollazione;

    private Date _dataArrivo;

    private int _idProtocollatore;

    private Boolean _riservato;

    private String _oggetto;

    private SmallContatto _mittente = new SmallContatto();

    private List<SmallContatto> _destinatari = new ArrayList<SmallContatto>();

    private int _statoProtocollo;

    private Date _dataAnnullamento;

    private String _motivazioneAnnullamento;
}
