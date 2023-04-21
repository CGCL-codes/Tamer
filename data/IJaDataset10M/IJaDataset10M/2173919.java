package org.ugc.cnel.manabi.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "usuario", catalog = "", schema = "public")
@NamedQueries({ @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"), @NamedQuery(name = "Usuario.findById", query = "SELECT u FROM Usuario u WHERE u.id = :id"), @NamedQuery(name = "Usuario.findByCedula", query = "SELECT u FROM Usuario u WHERE u.cedula = :cedula"), @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre"), @NamedQuery(name = "Usuario.findByApellido", query = "SELECT u FROM Usuario u WHERE u.apellido = :apellido"), @NamedQuery(name = "Usuario.findByCorreo", query = "SELECT u FROM Usuario u WHERE u.correo = :correo"), @NamedQuery(name = "Usuario.findByFechaRegistro", query = "SELECT u FROM Usuario u WHERE u.fechaRegistro = :fechaRegistro"), @NamedQuery(name = "Usuario.findByFechaEliminacion", query = "SELECT u FROM Usuario u WHERE u.fechaEliminacion = :fechaEliminacion"), @NamedQuery(name = "Usuario.findByIdUsuarioModifica", query = "SELECT u FROM Usuario u WHERE u.idUsuarioModifica = :idUsuarioModifica"), @NamedQuery(name = "Usuario.findByEstado", query = "SELECT u FROM Usuario u WHERE u.estado = :estado"), @NamedQuery(name = "Usuario.findByClave", query = "SELECT u FROM Usuario u WHERE u.clave = :clave"), @NamedQuery(name = "Usuario.findByEsadmin", query = "SELECT u FROM Usuario u WHERE u.esadmin = :esadmin") })
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "cedula", nullable = false, length = 10)
    private String cedula;

    @Basic(optional = false)
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(name = "correo", length = 150)
    private String correo;

    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @Column(name = "fecha_eliminacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEliminacion;

    @Column(name = "id_usuario_modifica")
    private Integer idUsuarioModifica;

    @Basic(optional = false)
    @Column(name = "estado", nullable = false)
    private boolean estado;

    @Basic(optional = false)
    @Column(name = "clave", nullable = false, length = 50)
    private String clave;

    @Basic(optional = false)
    @Column(name = "esadmin", nullable = false)
    private boolean esadmin;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private Collection<Inspeccion> inspeccionCollection;

    @OneToMany(mappedBy = "idUsuario")
    private Collection<RolUsuario> rolUsuarioCollection;

    @OneToMany(mappedBy = "idUsuario")
    private Collection<Audit> AuditCollection;

    public Usuario() {
    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario(Integer id, String cedula, String nombre, String apellido, boolean estado, String clave, boolean esadmin) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estado = estado;
        this.clave = clave;
        this.esadmin = esadmin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(Date fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public Integer getIdUsuarioModifica() {
        return idUsuarioModifica;
    }

    public void setIdUsuarioModifica(Integer idUsuarioModifica) {
        this.idUsuarioModifica = idUsuarioModifica;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean getEsadmin() {
        return esadmin;
    }

    public void setEsadmin(boolean esadmin) {
        this.esadmin = esadmin;
    }

    public Collection<Inspeccion> getInspeccionCollection() {
        return inspeccionCollection;
    }

    public void setInspeccionCollection(Collection<Inspeccion> inspeccionCollection) {
        this.inspeccionCollection = inspeccionCollection;
    }

    public Collection<RolUsuario> getRolUsuarioCollection() {
        return rolUsuarioCollection;
    }

    public void setRolUsuarioCollection(Collection<RolUsuario> rolUsuarioCollection) {
        this.rolUsuarioCollection = rolUsuarioCollection;
    }

    public Collection<Audit> getAuditCollection() {
        return AuditCollection;
    }

    public void setAuditCollection(Collection<Audit> AuditCollection) {
        this.AuditCollection = AuditCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ugc.cnel.manabi.entidades.Usuario[id=" + id + "]";
    }
}
