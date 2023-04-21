package javagie.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author eduardo
 */
@Embeddable
public class Direccion implements Serializable {

    @Column(name = "direccion_calle")
    private String calle;

    @ManyToOne
    @JoinColumn(name = "direccion_id_comuna", referencedColumnName = "id_comuna")
    private Comuna comuna;

    public Direccion() {
    }

    public Direccion(String calle, Comuna comuna) {
        this.calle = calle;
        this.comuna = comuna;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Direccion other = (Direccion) obj;
        if ((this.calle == null) ? (other.calle != null) : !this.calle.equals(other.calle)) {
            return false;
        }
        if (this.comuna != other.comuna && (this.comuna == null || !this.comuna.equals(other.comuna))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.calle != null ? this.calle.hashCode() : 0);
        hash = 67 * hash + (this.comuna != null ? this.comuna.hashCode() : 0);
        return hash;
    }
}
