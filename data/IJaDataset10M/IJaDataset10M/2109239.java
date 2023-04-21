package ramon.impl;

import java.io.Serializable;
import java.util.Date;

/**
 * Contiene la informaci�n de control de la acci�n vinculada. 
 * A diferencia de ramon.impl.Accion que adem�s contiene los valores temporales.
 */
public class AccionInfo implements Serializable {

    private String estado;

    private boolean estaExpirada;

    private Date ultimoAcceso;

    public AccionInfo(String estado, boolean estaExpirada, Date ultimoAcceso) {
        this.estado = estado;
        this.estaExpirada = estaExpirada;
        this.ultimoAcceso = ultimoAcceso;
    }

    public String getEstado() {
        return estado;
    }

    public boolean estaExpirada() {
        return estaExpirada;
    }

    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }
}
