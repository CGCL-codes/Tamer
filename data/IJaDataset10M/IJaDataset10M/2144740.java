package org.paja.group.logica.alfa.util;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.paja.group.logica.alfa.usuario.Usuario;

/**
 *
 * @author necronet
 */
@Entity
@Table(name = "tbl_historial")
@SequenceGenerator(name = "SEQ_HISTORIAL", sequenceName = "tbl_historial_id_historial_seq")
@NamedQueries({ @NamedQuery(name = "Historial.findByIdHistorial", query = "SELECT l FROM Historial l WHERE l.idHistorial = :idHistorial"), @NamedQuery(name = "Historial.findByTabla", query = "SELECT l FROM Historial l WHERE l.tabla = :tabla"), @NamedQuery(name = "Historial.findByTipo", query = "SELECT l FROM Historial l WHERE l.tipo = :tipo"), @NamedQuery(name = "Historial.findByExito", query = "SELECT l FROM Historial l WHERE l.exito = :exito"), @NamedQuery(name = "Historial.findByFechaHistorial", query = "SELECT l FROM Historial l WHERE l.fechaHistorial = :fechaHistorial") })
public class Historial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_historial", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HISTORIAL")
    private Integer idHistorial;

    @Column(name = "tabla", nullable = false)
    private String tabla;

    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoHistorial tipo;

    @Column(name = "exito", nullable = false)
    private boolean exito;

    @Column(name = "fecha_historial", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaHistorial;

    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario usuario;

    public Historial() {
    }

    public Historial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Historial(Integer idHistorial, String tabla, TipoHistorial tipo, boolean exito, Date fechaHistorial) {
        this.idHistorial = idHistorial;
        this.tabla = tabla;
        this.tipo = tipo;
        this.exito = exito;
        this.fechaHistorial = fechaHistorial;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public TipoHistorial getTipo() {
        return tipo;
    }

    public void setTipo(TipoHistorial tipo) {
        this.tipo = tipo;
    }

    public boolean getExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public Date getFechaHistorial() {
        return fechaHistorial;
    }

    public void setFechaHistorial(Date fechaHistorial) {
        this.fechaHistorial = fechaHistorial;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario idUsuario) {
        this.usuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorial != null ? idHistorial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Historial)) {
            return false;
        }
        Historial other = (Historial) object;
        if ((this.idHistorial == null && other.idHistorial != null) || (this.idHistorial != null && !this.idHistorial.equals(other.idHistorial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.paja.group.capa.logica.alfa.util.Historial[idHistorial=" + idHistorial + "]";
    }
}
