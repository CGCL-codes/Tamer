package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author javier
 */
@Entity
public class UnidadTransporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String patente;

    @ManyToOne
    private ModeloTransporte modelo;

    public ModeloTransporte getModelo() {
        return modelo;
    }

    public void setModelo(ModeloTransporte modelo) {
        this.modelo = modelo;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (patente != null ? patente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UnidadTransporte)) {
            return false;
        }
        UnidadTransporte other = (UnidadTransporte) object;
        if ((this.patente == null && other.patente != null) || (this.patente != null && !this.patente.equals(other.patente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UnidadTransporte[ patente=" + patente + " ]";
    }
}
