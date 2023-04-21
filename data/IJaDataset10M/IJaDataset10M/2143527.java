package openfield.entities.explotacion;

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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author shader
 */
@Entity
@Table(name = "EX_TIPOS_APERO_EQUIPO", catalog = "", schema = "APP", uniqueConstraints = { @UniqueConstraint(columnNames = { "DESCRIPCION" }) })
@NamedQueries({ @NamedQuery(name = "TipoAperoEquipo.findAll", query = "SELECT e FROM TipoAperoEquipo e"), @NamedQuery(name = "TipoAperoEquipo.findById", query = "SELECT e FROM TipoAperoEquipo e WHERE e.id = :id"), @NamedQuery(name = "TipoAperoEquipo.findByDescripcion", query = "SELECT e FROM TipoAperoEquipo e WHERE e.descripcion LIKE :descripcion") })
public class TipoAperoEquipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Short id;

    @Basic(optional = false)
    @Column(name = "DESCRIPCION", nullable = false, length = 100)
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private Collection<AperoEquipo> aperosEquiposCollection;

    public TipoAperoEquipo() {
    }

    public TipoAperoEquipo(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<AperoEquipo> getAperosEquiposCollection() {
        return aperosEquiposCollection;
    }

    public void setAperosEquiposCollection(Collection<AperoEquipo> exAperosEquiposCollection) {
        this.aperosEquiposCollection = exAperosEquiposCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoAperoEquipo)) {
            return false;
        }
        TipoAperoEquipo other = (TipoAperoEquipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "openfield.ado.TipoAperoEquipo[id=" + id + "]";
    }
}
