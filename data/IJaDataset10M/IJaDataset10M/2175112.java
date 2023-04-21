package mx.com.nyak.empresa.hbm;

import mx.com.nyak.base.dto.AbstractBaseDTO;

/** 
 * 
 * Derechos Reservados (c)Jose Carlos Perez Cervantes 2009 
 * 
 * 
 * */
public class Telefono extends AbstractBaseDTO implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Integer idTelefono;

    private Empleado empleado;

    private Integer idTipoTelefono;

    private Integer idLadaInternacional;

    private Integer idLadaNacional;

    private String numeroTelefonico;

    private String extension;

    public Integer getIdTelefono() {
        return this.idTelefono;
    }

    public void setIdTelefono(Integer idTelefono) {
        this.idTelefono = idTelefono;
    }

    public Integer getIdTipoTelefono() {
        return this.idTipoTelefono;
    }

    public void setIdTipoTelefono(Integer idTipoTelefono) {
        this.idTipoTelefono = idTipoTelefono;
    }

    public Integer getIdLadaInternacional() {
        return this.idLadaInternacional;
    }

    public void setIdLadaInternacional(Integer idLadaInternacional) {
        this.idLadaInternacional = idLadaInternacional;
    }

    public Integer getIdLadaNacional() {
        return this.idLadaNacional;
    }

    public void setIdLadaNacional(Integer idLadaNacional) {
        this.idLadaNacional = idLadaNacional;
    }

    public String getNumeroTelefonico() {
        return this.numeroTelefonico;
    }

    public void setNumeroTelefonico(String numeroTelefonico) {
        this.numeroTelefonico = numeroTelefonico;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }
}
