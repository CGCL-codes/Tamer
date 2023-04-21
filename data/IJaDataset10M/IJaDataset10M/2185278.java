package com.ciberiasoluciones.lidia.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author osalcedo
 */
@Embeddable
public class TelefonosPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id")
    private int id;

    @Basic(optional = false)
    @Column(name = "cod_tipo_telefono")
    private int codTipoTelefono;

    public TelefonosPK() {
    }

    public TelefonosPK(int id, int codTipoTelefono) {
        this.id = id;
        this.codTipoTelefono = codTipoTelefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodTipoTelefono() {
        return codTipoTelefono;
    }

    public void setCodTipoTelefono(int codTipoTelefono) {
        this.codTipoTelefono = codTipoTelefono;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) codTipoTelefono;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TelefonosPK)) {
            return false;
        }
        TelefonosPK other = (TelefonosPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.codTipoTelefono != other.codTipoTelefono) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ciberiasoluciones.lidia.modelo.TelefonosPK[ id=" + id + ", codTipoTelefono=" + codTipoTelefono + " ]";
    }
}
