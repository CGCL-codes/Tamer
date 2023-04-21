package datos;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

@Entity
@Embeddable
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Persona extends com.jgoodies.binding.beans.Model implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id_persona")
    private java.lang.Integer id;

    @OneToMany
    private Set<Direccion> direcciones = new HashSet<Direccion>();

    @OneToMany
    private Set<Telefono> telefonos = new HashSet<Telefono>();

    private java.lang.String nombre;

    private java.lang.String apellido;

    private java.lang.Integer edad;

    public Persona() {
    }

    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        java.lang.Integer oldValue = this.id;
        this.id = id;
        firePropertyChange("id", oldValue, this.id);
    }

    public java.lang.String getNombre() {
        return nombre;
    }

    public void setNombre(java.lang.String nombre) {
        java.lang.String oldValue = this.nombre;
        this.nombre = nombre;
        firePropertyChange("nombre", oldValue, this.nombre);
    }

    public java.lang.String getApellido() {
        return apellido;
    }

    public void setApellido(java.lang.String apellido) {
        java.lang.String oldValue = this.apellido;
        this.apellido = apellido;
        firePropertyChange("apellido", oldValue, this.apellido);
    }

    public java.lang.Integer getEdad() {
        return edad;
    }

    public void setEdad(java.lang.Integer edad) {
        java.lang.Integer oldValue = this.edad;
        this.edad = edad;
        firePropertyChange("edad", oldValue, this.edad);
    }

    public Set<Direccion> getDirecciones() {
        return direcciones;
    }

    public Set<Telefono> getTelefonos() {
        return telefonos;
    }

    public Object get(int index) {
        switch(index) {
            case 0:
                return id;
            case 1:
                return nombre;
            case 2:
                return apellido;
            case 3:
                return edad;
        }
        return "";
    }
}
