package mvc.model;

import java.util.Date;

/**
 *
 * @author RUFERSIC
 */
public class Persona {

    private String nombre;

    private String apellido;

    private String cedula;

    private String e_mail;

    private String telefono;

    private Date Fecha_de_nacimiento;

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido the apellido to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @return the cedula
     */
    public String getCedula() {
        return cedula;
    }

    /**
     * @param cedula the cedula to set
     */
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /**
     * @return the e_mail
     */
    public String getE_mail() {
        return e_mail;
    }

    /**
     * @param e_mail the e_mail to set
     */
    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the Fecha_de_nacimiento
     */
    public Date getFecha_de_nacimiento() {
        return Fecha_de_nacimiento;
    }

    /**
     * @param Fecha_de_nacimiento the Fecha_de_nacimiento to set
     */
    public void setFecha_de_nacimiento(Date Fecha_de_nacimiento) {
        this.Fecha_de_nacimiento = Fecha_de_nacimiento;
    }
}
