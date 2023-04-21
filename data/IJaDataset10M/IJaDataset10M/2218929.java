package net.conselldemallorca.ad.sistra.backoffice;

/**
 * Columna amb el valor d'un camp a una consulta de domini
 * 
 * @author Limit Tecnologies
 */
public class DominiColumna {

    private String codi;

    private String valor;

    public DominiColumna(String codi, String valor) {
        this.codi = codi;
        this.valor = valor;
    }

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
