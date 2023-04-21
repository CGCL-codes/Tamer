package com.gestioni.adoc.aps.system.services.documento.azioni;

import com.gestioni.adoc.aps.system.services.azioni.AbstractAzioneOggetto;

public class AzioneDocumento extends AbstractAzioneOggetto {

    private String _versione;

    public void setVersione(String versione) {
        this._versione = versione;
    }

    public String getVersione() {
        return _versione;
    }
}
