package org.ugc.cnel.manabi.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "audit", catalog = "", schema = "public")
@NamedQueries({ @NamedQuery(name = "Audit.findAll", query = "SELECT a FROM Audit a"), @NamedQuery(name = "Audit.findById", query = "SELECT a FROM Audit a WHERE a.id = :id"), @NamedQuery(name = "Audit.findByFechahora", query = "SELECT a FROM Audit a WHERE a.fechahora = :fechahora"), @NamedQuery(name = "Audit.findByDetalle", query = "SELECT a FROM Audit a WHERE a.detalle = :detalle") })
public class Audit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "fechahora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahora;

    @Column(name = "detalle", length = 2147483647)
    private String detalle;

    @JoinColumn(name = "idusuario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario idUsuario;

    public Audit() {
    }

    public Audit(Integer id) {
        this.id = id;
    }

    public Audit(Integer id, String detalle) {
        this.id = id;
        this.detalle = detalle;
        this.fechahora = new Date();
    }

    public Audit(int i, Usuario usu_login, String string) {
        this.id = i;
        this.detalle = string;
        this.setIdUsuario(usu_login);
        this.fechahora = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechahora() {
        return fechahora;
    }

    public void setFechahora(Date fechahora) {
        this.fechahora = fechahora;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Audit)) {
            return false;
        }
        Audit other = (Audit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ugc.cnel.manabi.entidades.Audit[id=" + id + "]";
    }
}
