package com.enjava.discografica.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "cantantes")
public class Cantante implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8565362759006939342L;

    Long id;

    String nombre;

    Set<Disco> discos;

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

    @OneToMany(mappedBy = "cantante", cascade = CascadeType.ALL)
    public Set<Disco> getDiscos() {
        return discos;
    }

    @Transient
    public void setDiscos(Set<Disco> discos) {
        this.discos = discos;
    }
}
