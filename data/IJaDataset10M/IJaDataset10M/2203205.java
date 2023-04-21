package extraTools;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "t_provincias")
@NamedQueries({ @NamedQuery(name = "Provincia.findAll", query = "SELECT p FROM Provincia p"), @NamedQuery(name = "Provincia.findByCodProv", query = "SELECT p FROM Provincia p WHERE p.codProv = :codProv"), @NamedQuery(name = "Provincia.findByProvincia", query = "SELECT p FROM Provincia p WHERE p.provincia = :provincia") })
public class Provincia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "CodProv")
    private String codProv;

    @Column(name = "Provincia")
    private String provincia;

    public Provincia() {
    }

    public Provincia(String codProv) {
        this.codProv = codProv;
    }

    public String getCodProv() {
        return codProv;
    }

    public void setCodProv(String codProv) {
        this.codProv = codProv;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codProv != null ? codProv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Provincia)) {
            return false;
        }
        Provincia other = (Provincia) object;
        if ((this.codProv == null && other.codProv != null) || (this.codProv != null && !this.codProv.equals(other.codProv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "extraTools.Provincia[codProv=" + codProv + "]";
    }
}
