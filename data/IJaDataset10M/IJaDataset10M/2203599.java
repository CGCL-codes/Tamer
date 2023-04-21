package openfield.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author shader
 */
@Entity
@Table(name = "TIPOS_MAGNITUD", catalog = "", schema = "APP", uniqueConstraints = { @UniqueConstraint(columnNames = { "DESCRIPCION" }) })
@NamedQueries({ @NamedQuery(name = "TipoMagnitud.findAll", query = "SELECT t FROM TipoMagnitud t"), @NamedQuery(name = "TipoMagnitud.findById", query = "SELECT t FROM TipoMagnitud t WHERE t.id = :id"), @NamedQuery(name = "TipoMagnitud.findByDescripcion", query = "SELECT t FROM TipoMagnitud t WHERE t.descripcion LIKE :descripcion") })
public class TipoMagnitud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Short id;

    @Basic(optional = false)
    @Column(name = "DESCRIPCION", nullable = false, length = 100)
    private String descripcion;

    public TipoMagnitud() {
    }

    public TipoMagnitud(String descripcion) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoMagnitud)) {
            return false;
        }
        TipoMagnitud other = (TipoMagnitud) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "openfield.ado.TipoMagnitud[id=" + id + "]";
    }
}
