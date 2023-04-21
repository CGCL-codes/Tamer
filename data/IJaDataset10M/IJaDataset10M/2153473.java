package ar.com.khronos.core.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Representa un proyecto (por ejemplo de desarrollo)
 * en el que trabajan empleados de una empresa.
 * <br>
 * Los empleados son asignados a uno o mas proyectos,
 * para cumplir con determinadas tareas estipuladas por
 * el mismo proyecto.
 * 
 * @author <a href="mailto:tezequiel@gmail.com">Ezequiel Turovetzky</a>
 *
 */
@Entity
@Table(name = "proyecto")
@AttributeOverride(name = "id", column = @Column(name = "proyecto_id"))
public class Proyecto extends PersistibleNombrableObject {

    /** Empleados asignados a este proyecto */
    private Collection<Usuario> usuariosAsignados;

    /** Categorias de tarea realizables en este proyecto */
    private Collection<CategoriaTarea> categoriasAsignadas;

    /** Cliente due�o del proyecto */
    private Cliente owner;

    /** Si el proyecto se encuentra actualmente activo */
    private boolean activo;

    /**
	 * Crea una nueva instancia de esta clase.
	 */
    public Proyecto() {
        usuariosAsignados = new ArrayList<Usuario>();
        categoriasAsignadas = new ArrayList<CategoriaTarea>();
        activo = true;
    }

    /**
	 * Crea una nueva instancia de esta clase.
	 * 
	 * @param name Nombre de este proyecto
	 */
    public Proyecto(String name) {
        this();
        setName(name);
    }

    /**
	 * Devuelve todos los usuarios asignados a este proyecto.
	 * 
	 * @return Todos los usuarios asignados a este proyecto 
	 */
    @ManyToMany
    @Cascade(CascadeType.MERGE)
    @JoinTable(joinColumns = { @JoinColumn(name = "proyecto_id") }, inverseJoinColumns = { @JoinColumn(name = "usuario_id") })
    public Collection<Usuario> getUsuariosAsignados() {
        return usuariosAsignados;
    }

    /**
	 * Establece todos los usuarios asignados a este proyecto.
	 * 
	 * @param usuariosAsignados Los usuarios asignados a este proyecto
	 * 
	 * @see #addUsuario(Usuario)
	 */
    public void setUsuariosAsignados(Collection<Usuario> usuariosAsignados) {
        this.usuariosAsignados = usuariosAsignados;
    }

    /**
	 * Devuelve todas las categorias de tareas
	 * realizables en este proyecto.
	 * <br>
	 * Ninguna tarea que no pertenezca a alguna de estas
	 * categorias puede agregarse como tarea realizada
	 * en un dia de trabajo en este proyecto.
	 * 
	 * @return Las categorias de tareas realizables en este proyecto
	 */
    @ManyToMany
    @OrderBy("name")
    @Cascade(CascadeType.MERGE)
    @JoinTable(joinColumns = { @JoinColumn(name = "proyecto_id") }, inverseJoinColumns = { @JoinColumn(name = "categoria_id") })
    public Collection<CategoriaTarea> getCategoriasAsignadas() {
        return categoriasAsignadas;
    }

    /**
	 * Establece las categorias de tareas
	 * realizables en este proyecto.
	 * <br>
	 * Ninguna tarea que no pertenezca a alguna de estas
	 * categorias puede agregarse como tarea realizada
	 * en un dia de trabajo en este proyecto.
	 * 
	 * @param categoriasAsignadas Las categorias de tareas realizables 
	 * 		  en este proyecto
	 */
    public void setCategoriasAsignadas(Collection<CategoriaTarea> categoriasAsignadas) {
        this.categoriasAsignadas = categoriasAsignadas;
    }

    /**
	 * Devuelve el cliente al cual pertenece este
	 * proyecto.
	 * 
	 * @return El cliente al cual pertence el proyecto
	 */
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    public Cliente getOwner() {
        return owner;
    }

    /**
	 * Establece el cliente al cual pertenece este
	 * proyecto.
	 * 
	 * @param owner El cliente al cual pertence el proyecto
	 */
    public void setOwner(Cliente owner) {
        this.owner = owner;
    }

    /**
	 * Informa si este proyecto se encuentra 
	 * actualmente activo.
	 * 
	 * @return <code>true</code> si el proyecto se encuentra activo,
	 * 		   <code>false</code> en caso opuesto
	 */
    @Basic
    public boolean isActivo() {
        return activo;
    }

    /**
	 * Establece si este proyecto se encuentra 
	 * actualmente activo.
	 * 
	 * @param activo <code>true</code> si el proyecto se encuentra activo,
	 * 		   <code>false</code> en caso opuesto
	 */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
	 * Asigna a este proyecto a un usuario (empleado)
	 * para trabajar en el.
	 * 
	 * @param usuario Usuario a asignar
	 * 
	 * @throws IllegalStateException Si el usuario ya esta
	 * 		   previamente asignado a este proyecto
	 */
    public void addUsuario(Usuario usuario) {
        if (!usuariosAsignados.contains(usuario)) {
            usuariosAsignados.add(usuario);
        } else {
            throw new IllegalStateException("El usuario " + usuario + " ya se encuentra asignado al proyecto");
        }
    }

    /**
	 * Informa si este proyecto tiene asignado al usuario
	 * especificado.
	 * 
	 * @param usuario Usuario a saber si esta asignado a este proyecto
	 * 
	 * @return <code>true</code> si el usuario esta asignado a este proyecto
	 * 		   <code>false</code> en caso opuesto
	 */
    public boolean hasUsuarioAsignado(Usuario usuario) {
        return usuariosAsignados.contains(usuario);
    }

    /**
	 * Informa si este proyecto tiene asignada a la categoria de tarea
	 * especificada.
	 * 
	 * @param categoriaTarea CategoriaTarea a saber si esta asignada a este proyecto
	 * 
	 * @return <code>true</code> si la categoriaTarea esta asignada a este proyecto
	 * 		   <code>false</code> en caso opuesto
	 */
    public boolean hasCategoriaTareaAsignada(CategoriaTarea categoriaTarea) {
        return categoriasAsignadas.contains(categoriaTarea);
    }
}
