package openfield.ADO;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author shader
 */
@Entity
@Table(name = "TIPOS_APERO_EQUIPO")
@NamedQueries({ @NamedQuery(name = "TiposAperoEquipo.findAll", query = "SELECT t FROM TiposAperoEquipo t"), @NamedQuery(name = "TiposAperoEquipo.findById", query = "SELECT t FROM TiposAperoEquipo t WHERE t.id = :id"), @NamedQuery(name = "TiposAperoEquipo.findByDescripcion", query = "SELECT t FROM TiposAperoEquipo t WHERE t.descripcion = :descripcion") })
public class TiposAperoEquipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Short id;

    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private Collection<AperosEquipos> aperosEquiposCollection;

    public TiposAperoEquipo() {
    }

    public TiposAperoEquipo(Short id) {
        this.id = id;
    }

    public TiposAperoEquipo(Short id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<AperosEquipos> getAperosEquiposCollection() {
        return aperosEquiposCollection;
    }

    public void setAperosEquiposCollection(Collection<AperosEquipos> aperosEquiposCollection) {
        this.aperosEquiposCollection = aperosEquiposCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TiposAperoEquipo)) {
            return false;
        }
        TiposAperoEquipo other = (TiposAperoEquipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "openfield.ADO.TiposAperoEquipo[id=" + id + "]";
    }
}
