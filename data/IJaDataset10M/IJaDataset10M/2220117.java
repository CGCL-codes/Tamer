package org.mersys.sagi.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Franky Villadiego 
 */
@Entity
@Table(name = "inmuebles_edificacion")
@SequenceGenerator(name = "secuencia", sequenceName = "inmuebles_edificacion_id_seq", allocationSize = 1)
public class TipoEdificacion implements Serializable {

    private long id;

    private String nombre;

    private boolean seleccionado;

    public TipoEdificacion() {
    }

    public TipoEdificacion(String nombre) {
        this.nombre = nombre;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Transient
    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    /** Valida que el nombre no exceda el limite
     * del tamaño del campo en la base de datos, si es asi hace el corte. Ademas
     * elimina espacios en los extremos y pasa a mayusculas dicha propiedad.
     *
     */
    public void ValidarAtributos() {
        if (this.nombre.trim().length() > 50) {
            this.nombre = this.nombre.trim().substring(0, 49);
        }
        this.nombre = this.nombre.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
