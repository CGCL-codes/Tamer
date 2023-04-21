package com.enjava.discografica.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "discos")
public class Disco implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5645107845662132870L;

    Long id;

    String nombre;

    Cantante cantante;

    Calendar fecha;

    Set<Cancion> canciones;

    Set<LineaPedido> lineas;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @ManyToOne
    public Cantante getCantante() {
        return cantante;
    }

    public void setCantante(Cantante cantante) {
        this.cantante = cantante;
    }

    @OneToMany(mappedBy = "disco", cascade = CascadeType.ALL)
    public Set<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(Set<Cancion> canciones) {
        this.canciones = canciones;
    }

    @OneToMany(mappedBy = "disco", cascade = CascadeType.ALL)
    public Set<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(Set<LineaPedido> lineas) {
        this.lineas = lineas;
    }

    @Column
    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Disco other = (Disco) obj;
        if (nombre == null) {
            if (other.nombre != null) return false;
        } else if (!nombre.equals(other.nombre)) return false;
        return true;
    }
}
