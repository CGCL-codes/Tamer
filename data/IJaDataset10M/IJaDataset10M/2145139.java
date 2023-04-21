package org.hmaciel.rph.ejb.session;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Entity;
import org.hibernate.validator.NotNull;

@Entity
public class Query implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    String QueryRoot;

    String QueryExtension;

    String nombre;

    String apellido;

    String cedula;

    String root;

    String sexo;

    Date fecha;

    int id;

    public Query() {
    }

    public String getQueryRoot() {
        return QueryRoot;
    }

    public void setQueryRoot(String queryRoot) {
        QueryRoot = queryRoot;
    }

    @NotNull
    @Id
    public String getQueryExtension() {
        return QueryExtension;
    }

    public void setQueryExtension(String queryExtension) {
        QueryExtension = queryExtension;
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
