package com.siasal.usuarios.business;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.common.exception.BusinessException;
import com.siasal.usuarios.commons.PersonaTO;
import com.siasal.usuarios.commons.UsuarioConsultaTO;

/**
 * @hibernate.joined-subclass table="usu_usuarioconsulta"
 * @hibernate.joined-subclass-key column="uc_id"
 * 
 * @hibernate.query name="getUsuarioConsultaByCedula" query="from
 *                  UsuarioConsulta as u where u.cedula= :cedula"
 * @hibernate.query name="getUsuariosConsultaByTipo" query="from
 *                  UsuarioConsulta as u where u.tipo.id= :idTipo"
 * @hibernate.query name="usuariosExistentes" query="select count(*) from
 *                  UsuarioConsulta as u "
 * 
 */
public class UsuarioConsulta extends Persona {

    private TipoUC tipo;

    /**
	 * 
	 */
    private static final long serialVersionUID = -8800773755043722862L;

    /** @modelguid {28569FEF-9980-4C19-B53C-8B79D3788E92} */
    protected Integer estadoUC;

    /** @modelguid {9D32574D-0A70-4934-AE88-8107C49F9B78} */
    private java.util.Set multas = new HashSet();

    /** @modelguid {C7ED8702-CA2B-417F-B7AC-D9395D50F735} */
    private java.util.Set suscripciones = new HashSet();

    private String nombreCompleto;

    /** @modelguid {06E69D55-1C06-465B-8B7C-41D58ABA5A71} */
    public void imprimeCarne() {
    }

    /** @modelguid {0F09A2E4-5570-4C11-BBB6-1CE1157529B3} */
    public void imprimeCertificado() {
    }

    /** @modelguid {ACED1506-4E42-4BE2-AF33-5C13D04B24C4} */
    public void puedeLlevar() {
    }

    /** @modelguid {38DF0DC1-2355-4E17-8533-40D07104854B} */
    public void prestamos() {
    }

    /** @modelguid {CC9A94BC-F244-431D-9960-BBAD7105D147} */
    public void habilitado() {
    }

    /** @modelguid {E1AABAC3-C2A0-4E07-8C76-4E5C878F7B02} */
    public void actualizar() {
    }

    /** @modelguid {86B0670D-9540-447E-A434-A87A04AF43C8} */
    public void crear() {
    }

    /**
	 * @hibernate.property type="int" column="uc_estadoUC"
	 * @return
	 */
    public Integer getEstadoUC() {
        return estadoUC;
    }

    /** @modelguid {77981D55-8C12-4AF8-AD95-3BD2DD10BA78} */
    public void setEstadoUC(Integer aEstadoUC) {
        estadoUC = aEstadoUC;
    }

    /**
	 * @hibernate.set
	 * @hibernate.set inverse="true" lazy="true" 
	 * @hibernate.collection-key column="uc_id"
	 * @hibernate.collection-one-to-many class="com.siasal.usuarios.business.Multa"
	 */
    public java.util.Set getMultas() {
        return multas;
    }

    public void setMultas(java.util.Set multas) {
        this.multas = multas;
    }

    /**
	 * 
	 * @return
	 * @hibernate.set cascade="save-update"
	 * @hibernate.collection-key column="uc_id"
	 * @hibernate.collection-one-to-many class="com.siasal.usuarios.business.Suscripcion" 
	 * 
	 */
    public java.util.Set getSuscripciones() {
        return suscripciones;
    }

    public void setSuscripciones(java.util.Set suscripciones) {
        this.suscripciones = suscripciones;
    }

    public UsuarioConsulta(String apellido, String cedula, String direccion, String email, Integer id, String nombre, String telefono, Integer estadouc, Set multas, Set suscripciones) {
        super(apellido, cedula, direccion, email, id, nombre, telefono);
        estadoUC = estadouc;
        this.multas = multas;
        this.suscripciones = suscripciones;
    }

    public UsuarioConsulta(UsuarioConsultaTO usuarioConsulta) {
        super(usuarioConsulta);
        setEstadoUC(usuarioConsulta.getEstadoUC());
        setMultas(usuarioConsulta.getMultasTO());
        setSuscripciones(usuarioConsulta.getSuscripcionesTO());
    }

    public UsuarioConsulta(PersonaTO persona) {
        super(persona);
    }

    public UsuarioConsulta(String apellido, String cedula, String direccion, String email, Integer id, String nombre, String telefono) {
        super(apellido, cedula, direccion, email, id, nombre, telefono);
    }

    /**
	 * @param apellido
	 * @param cedula
	 * @param direccion
	 * @param email
	 * @param id
	 * @param nombre
	 * @param telefono
	 * @param donaciones
	 * @param estadouc
	 * @param multas
	 * @param suscripciones
	 * @param tiposuc
	 */
    public UsuarioConsulta(UsuarioConsulta usuarioConsulta) {
        super(usuarioConsulta.getApellido(), usuarioConsulta.getCedula(), usuarioConsulta.getDireccion(), usuarioConsulta.getEmail(), usuarioConsulta.getId(), usuarioConsulta.getNombre(), usuarioConsulta.getTelefono());
        estadoUC = usuarioConsulta.getEstadoUC();
        this.multas = usuarioConsulta.getMultas();
        this.suscripciones = usuarioConsulta.getSuscripciones();
    }

    public PersonaTO getTO() {
        return new UsuarioConsultaTO(this.getApellido(), this.getCedula(), this.getDireccion(), this.getEmail(), this.getId(), this.getNombre(), this.getTelefono(), this.getEstadoUC(), this.getMultas(), this.getSuscripciones());
    }

    public UsuarioConsulta() {
    }

    /**
	 * @hibernate.many-to-one column="tuc_id"
	 *                        class="com.siasal.usuarios.business.TipoUC"
	 *                        cascade="save-update"
	 * @return
	 */
    public TipoUC getTipo() {
        return tipo;
    }

    public void setTipo(TipoUC tipo) {
        this.tipo = tipo;
    }

    /**
	 * Metodo para crear la suscripcion
	 */
    public void crearSuscripcion() {
        Integer tiempoMeses = getTipo().getTiempoSuscripcion();
        Suscripcion suscripcion = new Suscripcion();
        Calendar calendar = Calendar.getInstance();
        suscripcion.setFecha(new Date(calendar.getTimeInMillis()));
        calendar.add(Calendar.MONTH, tiempoMeses);
        suscripcion.setCaducidad(calendar.getTime());
        suscripcion.setRenovaciones(0);
        getSuscripciones().add(suscripcion);
    }

    /**
	 * Renovar suscripcion
	 * @throws BusinessException 
	 */
    public void renovarSuscripcion() throws BusinessException {
        Integer tiempoMeses = getTipo().getTiempoSuscripcion();
        if (getSuscripciones().isEmpty()) {
            throw new BusinessException("Usuario no tiene suscripciones");
        }
        Iterator it = getSuscripciones().iterator();
        Suscripcion suscripcion = (Suscripcion) it.next();
        Calendar calendar = Calendar.getInstance();
        suscripcion.setFecha(new Date(calendar.getTimeInMillis()));
        calendar.add(Calendar.MONTH, tiempoMeses);
        suscripcion.setCaducidad(calendar.getTime());
        suscripcion.setRenovaciones(suscripcion.getRenovaciones() + 1);
    }

    public String getNombreCompleto() {
        if (nombreCompleto == null) {
            nombreCompleto = nombre + " " + apellido;
        }
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}
