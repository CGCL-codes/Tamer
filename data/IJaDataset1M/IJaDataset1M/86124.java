package net.conselldemallorca.ad.sistra.backoffice;

import java.util.List;

/**
 * Fila de resultats d'una consulta de domini
 * 
 * @author Limit Tecnologies
 */
public class DominiFila {

    private List<DominiColumna> columnes;

    public List<DominiColumna> getColumnes() {
        return columnes;
    }

    public void setColumnes(List<DominiColumna> columnes) {
        this.columnes = columnes;
    }
}
