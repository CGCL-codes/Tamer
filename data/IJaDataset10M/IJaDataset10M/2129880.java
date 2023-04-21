package org.ibit.avanthotel.persistence.logic.data.allotjament;

/**
 * @created 7 de octubre de 2003
 */
public class DistanciesData implements java.io.Serializable {

    private String nomReferencia;

    private Integer distancia;

    private String unitat;

    /**
    *Construeix una nova instancia de DistanciesData
    *
    * @param nomReferencia
    * @param distancia
    * @param unitat
    */
    public DistanciesData(String nomReferencia, Integer distancia, String unitat) {
        this.nomReferencia = nomReferencia;
        this.distancia = distancia;
        this.unitat = unitat;
    }

    /**
    * retorna el valor per la propietat nomReferencia
    *
    * @return valor de nomReferencia
    */
    public String getNomReferencia() {
        return nomReferencia;
    }

    /**
    * fitxa el valor de distancia
    *
    * @param distancia nou valor per distancia
    */
    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }

    /**
    * retorna el valor per la propietat distancia
    *
    * @return valor de distancia
    */
    public Integer getDistancia() {
        return distancia;
    }

    /**
    * fitxa el valor de unitat
    *
    * @param unitat nou valor per unitat
    */
    public void setUnitat(String unitat) {
        this.unitat = unitat;
    }

    /**
    * retorna el valor per la propietat unitat
    *
    * @return valor de unitat
    */
    public String getUnitat() {
        return unitat;
    }

    /**
    * fitxa el valor de nomReferencia
    *
    * @param nomReferencia nou valor per nomReferencia
    */
    public void setNomReferencia(String nomReferencia) {
        this.nomReferencia = nomReferencia;
    }

    /**
    * @return
    */
    public String toString() {
        return "{ nomReferencia=" + nomReferencia + ", distancia=" + distancia + ", unitat=" + unitat + "}";
    }
}
