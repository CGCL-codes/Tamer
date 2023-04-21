package org.humboldt.cassia.core.jdo;

import java.io.Serializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Clase que representa las carpetas tematicas 
 * 
 * @author Benjamin A. Rodriguez R. benjamin.a.rodriguez@gmail.com
 * 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CarpetaTematica implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    Long id;

    @Persistent
    String nombre;

    @Persistent
    CarpetaTematica carpetaPadre;

    public CarpetaTematica() {
        super();
    }

    public CarpetaTematica getCarpetaPadre() {
        return carpetaPadre;
    }

    public void setCarpetaPadre(CarpetaTematica carpetaPadre) {
        this.carpetaPadre = carpetaPadre;
    }

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

    public CarpetaTematica(long id, String nombre) {
        super();
        this.id = id;
        this.nombre = nombre;
    }

    public CarpetaTematica(String nombre) {
        super();
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
