package org.opensih.gdq.Modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.hibernate.validator.NotNull;

@Entity
public class Paciente implements Serializable {

    private static final long serialVersionUID = 7285681594797359088L;

    private String root_extension;

    private Date fnac;

    private String sexo;

    private String nombre, apellido;

    private String infoContacto;

    private List<String> ids;

    private String calle, ciudad, departamento, pais, telefono;

    @NotNull
    @Id
    public String getRoot_extension() {
        return root_extension;
    }

    public void setRoot_extension(String root_extension) {
        this.root_extension = root_extension;
    }

    public Date getFnac() {
        return fnac;
    }

    public void setFnac(Date fnac) {
        this.fnac = fnac;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

    public String getInfoContacto() {
        return infoContacto;
    }

    public void setInfoContacto(String infoContacto) {
        this.infoContacto = infoContacto;
    }

    @Transient
    public String getRoot() {
        String root = "";
        if (root_extension != null && root_extension.contains("_")) root = this.root_extension.split("_")[0];
        return root;
    }

    @Transient
    public String getExtension() {
        String extension = "";
        if (root_extension != null && root_extension.contains("_")) extension = this.root_extension.split("_")[1];
        return extension;
    }

    @Transient
    public String getCalle() {
        return calle;
    }

    @Transient
    public void setCalle(String calle) {
        this.calle = calle;
    }

    @Transient
    public String getCiudad() {
        return ciudad;
    }

    @Transient
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Transient
    public String getDepartamento() {
        return departamento;
    }

    @Transient
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Transient
    public String getPais() {
        return pais;
    }

    @Transient
    public void setPais(String pais) {
        this.pais = pais;
    }

    @Transient
    public String getTelefono() {
        return telefono;
    }

    @Transient
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Transient
    public List<String> getIds() {
        return ids;
    }

    @Transient
    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return (((this.nombre != null) ? this.nombre : "") + " " + ((this.apellido != null) ? this.apellido : "")).trim();
    }

    @Transient
    public String getDireccion() {
        return " " + ((this.calle != null) ? this.calle : "") + "/" + ((this.ciudad != null) ? this.ciudad : "") + "/" + ((this.departamento != null) ? this.departamento : "") + "/" + ((this.pais != null) ? this.pais : "");
    }

    @Transient
    public String getNacimiento() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (this.fnac != null) return sdf.format(this.fnac); else return "";
    }

    @Transient
    public String getApellidoNombre() {
        return (((this.apellido != null) ? this.apellido : "") + " " + ((this.nombre != null) ? this.nombre : "")).trim();
    }

    @Transient
    public String getApellidoNombreCORTO() {
        String ape = "";
        String nom = "";
        if (this.apellido != null) {
            if (this.apellido.length() > 10) {
                ape = this.apellido.substring(0, 10) + "..";
            } else {
                ape = this.apellido;
            }
            ape = ape + ", ";
        }
        if (this.nombre != null) {
            if (this.nombre.length() > 10) {
                nom = this.nombre.substring(0, 10) + "..";
                nom = nom + "..";
            } else nom = this.nombre;
        }
        return ape + nom;
    }

    @Transient
    public String getEdad() {
        if (this.getFnac() != null) {
            int factor = 0;
            Calendar birth = new GregorianCalendar();
            Calendar today = new GregorianCalendar();
            birth.setTime(this.getFnac());
            today.setTime(new Date());
            if (today.get(Calendar.MONTH) <= birth.get(Calendar.MONTH)) {
                if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)) {
                    if (today.get(Calendar.DATE) < birth.get(Calendar.DATE)) {
                        factor = -1;
                    }
                } else {
                    factor = -1;
                }
            }
            int edad = (today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)) + factor;
            return String.valueOf(edad);
        }
        return "";
    }
}
