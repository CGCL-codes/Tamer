package org.paja.group.logica.alfa.usuario;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.paja.group.logica.alfa.comunicacion.casos.MensajeTematica;
import org.paja.group.logica.alfa.comunicacion.casos.Tematica;
import org.paja.group.logica.alfa.comunicacion.casos.UsuarioTematica;
import org.paja.group.logica.alfa.config.Sucursal;
import org.paja.group.logica.alfa.usuario.nota.MensajeInstantaneo;
import org.paja.group.logica.alfa.usuario.nota.Nota;
import org.paja.group.logica.alfa.util.Historial;

/**
 *
 * @author necronet
 */
@Entity
@Table(name = "tbl_usuario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.INTEGER)
@NamedQueries({ @NamedQuery(name = "Usuario.findByIdUsuario", query = "SELECT t FROM Usuario t WHERE t.idUsuario = :idUsuario"), @NamedQuery(name = "Usuario.findByNombre", query = "SELECT t FROM Usuario t WHERE t.nombre = :nombre"), @NamedQuery(name = "Usuario.findByContrasenna", query = "SELECT t FROM Usuario t WHERE t.contrasenna = :contrasenna"), @NamedQuery(name = "Usuario.findByConectado", query = "SELECT t FROM Usuario t WHERE t.conectado = :conectado"), @NamedQuery(name = "Usuario.findByUltimaSesion", query = "SELECT t FROM Usuario t WHERE t.ultimaSesion = :ultimaSesion"), @NamedQuery(name = "Usuario.findByNoIpSesion", query = "SELECT t FROM Usuario t WHERE t.noIpSesion = :noIpSesion"), @NamedQuery(name = "Usuario.findByActivo", query = "SELECT t FROM Usuario t WHERE t.activo = :activo") })
@SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "tbl_usuario_id_usuario_seq", allocationSize = 1)
public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_usuario", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO")
    protected Integer idUsuario;

    @Column(name = "nombre", nullable = false)
    protected String nombre;

    @Column(name = "contrasenna", nullable = false)
    protected String contrasenna;

    @Column(name = "conectado", nullable = false)
    protected int conectado;

    @Column(name = "ultima_sesion")
    @Temporal(TemporalType.DATE)
    protected Date ultimaSesion;

    @Column(name = "no_ip_sesion")
    protected String noIpSesion;

    @Column(name = "activo", nullable = false)
    protected int activo;

    @JoinColumn(name = "id_sucursal", referencedColumnName = "id_sucursal")
    @ManyToOne(fetch = FetchType.EAGER)
    protected Sucursal sucursal;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private Collection<MensajeInstantaneo> mensajesInstantaneos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private Collection<Nota> notas;

    @ManyToMany(mappedBy = "usuarios")
    private Collection<GrupoTrabajo> idGrupoTrabajoCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administrador")
    private Collection<GrupoTrabajo> grupoTrabajoCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "duenno")
    private Collection<Tematica> tematicas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<UsuarioTematica> usuariosTematicas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<Historial> HistorialCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<MensajeTematica> mensajeTematicaCollection;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public int getConectado() {
        return conectado;
    }

    public void setConectado(int conectado) {
        this.conectado = conectado;
    }

    public Date getUltimaSesion() {
        return ultimaSesion;
    }

    public void setUltimaSesion(Date ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
    }

    public String getNoIpSesion() {
        return noIpSesion;
    }

    public void setNoIpSesion(String noIpSesion) {
        this.noIpSesion = noIpSesion;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombre();
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Collection<MensajeInstantaneo> getMensajesInstantaneos() {
        return mensajesInstantaneos;
    }

    public void setMensajesInstantaneos(Collection<MensajeInstantaneo> mensajesInstantaneos) {
        this.mensajesInstantaneos = mensajesInstantaneos;
    }

    public Collection<Nota> getNotas() {
        return notas;
    }

    public void setNotas(Collection<Nota> notas) {
        this.notas = notas;
    }

    public Collection<Historial> getHistorialCollection() {
        return HistorialCollection;
    }

    public void setHistorialCollection(Collection<Historial> HistorialCollection) {
        this.HistorialCollection = HistorialCollection;
    }

    public Collection<MensajeTematica> getMensajeTematicaCollection() {
        return mensajeTematicaCollection;
    }

    public void setMensajeTematicaCollection(Collection<MensajeTematica> mensajeTematicaCollection) {
        this.mensajeTematicaCollection = mensajeTematicaCollection;
    }
}
