package it.cilea.osd.jdyna.dto;

import it.cilea.osd.jdyna.model.Soggettario;
import java.io.Serializable;

public class DTOSoggetto implements Serializable {

    private Soggettario soggettario;

    private String voce;

    public Soggettario getSoggettario() {
        return soggettario;
    }

    public void setSoggettario(Soggettario soggettario) {
        this.soggettario = soggettario;
    }

    public String getVoce() {
        return voce;
    }

    public void setVoce(String voce) {
        this.voce = voce;
    }
}
