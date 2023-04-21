package org.paja.group.logica.alfa.comunicacion.casos;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "tbl_mensaje_tematica")
@SequenceGenerator(name = "SEQ_MENSAJE_TEMATICA", sequenceName = "tbl_mensaje_tematica_id_mensaje_tematica_seq", initialValue = 1, allocationSize = 1)
@NamedQueries({ @NamedQuery(name = "MensajeTematica.findByIdMensajeTematica", query = "SELECT m FROM MensajeTematica m WHERE m.idMensajeTematica = :idMensajeTematica"), @NamedQuery(name = "MensajeTematica.findByTitulo", query = "SELECT m FROM MensajeTematica m WHERE m.titulo = :titulo"), @NamedQuery(name = "MensajeTematica.findByTexto", query = "SELECT m FROM MensajeTematica m WHERE m.texto = :texto"), @NamedQuery(name = "MensajeTematica.findByFecha", query = "SELECT m FROM MensajeTematica m WHERE m.fecha = :fecha") })
public class MensajeTematica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_mensaje_tematica", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MENSAJE_TEMATICA")
    private Integer idMensajeTematica;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "texto", nullable = false)
    private String texto;

    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMensajeTematica", fetch = FetchType.EAGER)
    private Collection<DatoAdjunto> tblDatoAdjuntoCollection;

    @OneToMany(mappedBy = "mensajeIdMensajeTematica", fetch = FetchType.EAGER)
    private Collection<MensajeTematica> mensajeTematicaCollection;

    @JoinColumn(name = "mensaje_id_mensaje_tematica", referencedColumnName = "id_mensaje_tematica")
    @ManyToOne
    private MensajeTematica mensajeIdMensajeTematica;

    @JoinColumn(name = "id_tematica", referencedColumnName = "id_tematica")
    @ManyToOne
    private Tematica idTematica;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    public MensajeTematica() {
    }

    public MensajeTematica(Integer idMensajeTematica) {
        this.idMensajeTematica = idMensajeTematica;
    }

    public MensajeTematica(Integer idMensajeTematica, String texto, Date fecha) {
        this.idMensajeTematica = idMensajeTematica;
        this.texto = texto;
        this.fecha = fecha;
    }

    public Integer getIdMensajeTematica() {
        return idMensajeTematica;
    }

    public void setIdMensajeTematica(Integer idMensajeTematica) {
        this.idMensajeTematica = idMensajeTematica;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Collection<DatoAdjunto> getTblDatoAdjuntoCollection() {
        return tblDatoAdjuntoCollection;
    }

    public void setTblDatoAdjuntoCollection(Collection<DatoAdjunto> tblDatoAdjuntoCollection) {
        this.tblDatoAdjuntoCollection = tblDatoAdjuntoCollection;
    }

    public Collection<MensajeTematica> getMensajeTematicaCollection() {
        return mensajeTematicaCollection;
    }

    public void setMensajeTematicaCollection(Collection<MensajeTematica> mensajeTematicaCollection) {
        this.mensajeTematicaCollection = mensajeTematicaCollection;
    }

    public MensajeTematica getMensajeIdMensajeTematica() {
        return mensajeIdMensajeTematica;
    }

    public void setMensajeIdMensajeTematica(MensajeTematica mensajeIdMensajeTematica) {
        this.mensajeIdMensajeTematica = mensajeIdMensajeTematica;
    }

    public Tematica getIdTematica() {
        return idTematica;
    }

    public void setIdTematica(Tematica idTematica) {
        this.idTematica = idTematica;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMensajeTematica != null ? idMensajeTematica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MensajeTematica)) {
            return false;
        }
        MensajeTematica other = (MensajeTematica) object;
        if ((this.idMensajeTematica == null && other.idMensajeTematica != null) || (this.idMensajeTematica != null && !this.idMensajeTematica.equals(other.idMensajeTematica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.paja.group.logica.MensajeTematica[idMensajeTematica=" + idMensajeTematica + "]";
    }
}
