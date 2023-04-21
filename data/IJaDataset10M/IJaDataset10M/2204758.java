package com.gestioni.adoc.aps.system.services.fascicolo.events;

import com.agiletec.aps.system.services.IManager;
import com.agiletec.aps.system.services.notify.ApsEvent;
import com.gestioni.adoc.aps.system.services.documento.model.Documento;
import com.gestioni.adoc.aps.system.services.fascicolo.Fascicolo;

public class FascicoloChangedEvent extends ApsEvent {

    public void notify(IManager srv) {
        ((FascicoloChangedObserver) srv).updateFromFascicoloChanged(this);
    }

    @SuppressWarnings("unchecked")
    public Class getObserverInterface() {
        return FascicoloChangedObserver.class;
    }

    public String getOggetto() {
        return OGGETTO;
    }

    public void setAzioneDescr(String azioneDescr) {
        this._azioneDescr = azioneDescr;
    }

    public String getAzioneDescr() {
        return _azioneDescr;
    }

    public void setProfiloRecipient(Integer profiloRecipient) {
        this._profiloRecipient = profiloRecipient;
    }

    public Integer getProfiloRecipient() {
        return _profiloRecipient;
    }

    public void setUoRecipient(Integer uoRecipient) {
        this._uoRecipient = uoRecipient;
    }

    public Integer getUoRecipient() {
        return _uoRecipient;
    }

    public void setFascicolo(Fascicolo fascicolo) {
        this._fascicolo = fascicolo;
    }

    public Fascicolo getFascicolo() {
        return _fascicolo;
    }

    /**
	 * Setta il documento. Utilizzato per la notifica dei cambiamenti in fascicolazione documenti
	 * @param documento
	 */
    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    /**
	 * Utilizzato per la propagazione dell'evento di fascicolazione dei documenti
	 * @return
	 */
    public Documento getDocumento() {
        return documento;
    }

    /**
	 * Esecutore
	 * @param idProfilo
	 */
    public void setIdProfilo(Integer idProfilo) {
        this._idProfilo = idProfilo;
    }

    public Integer getIdProfilo() {
        return _idProfilo;
    }

    private String _azioneDescr;

    private Integer _idProfilo;

    private Fascicolo _fascicolo;

    private Integer _profiloRecipient;

    private Integer _uoRecipient;

    private Documento documento;

    private static final String OGGETTO = "fascicolo";
}
