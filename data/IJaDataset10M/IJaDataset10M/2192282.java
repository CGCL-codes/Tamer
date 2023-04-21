package org.humboldt.cassia.core.jdo;

import java.io.Serializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Clase que representa los recursos que tiene asociados un usuario  
 * 
 * @author Benjamin A. Rodriguez R. benjamin.a.rodriguez@gmail.com
 * 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RecursosUsuario implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    Long id;

    @Persistent
    Usuario usuario;

    @Persistent
    CarpetaTematica carpeta;

    @Persistent
    Conjunto conjunto;

    public RecursosUsuario() {
        super();
    }

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the carpeta
	 */
    public CarpetaTematica getCarpeta() {
        return carpeta;
    }

    /**
	 * @param carpeta the carpeta to set
	 */
    public void setCarpeta(CarpetaTematica carpeta) {
        this.carpeta = carpeta;
    }

    /**
	 * @return the conjunto
	 */
    public Conjunto getConjunto() {
        return conjunto;
    }

    /**
	 * @param conjunto the conjunto to set
	 */
    public void setConjunto(Conjunto conjunto) {
        this.conjunto = conjunto;
    }

    /**
	 * @return the usuario
	 */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
	 * @param usuario the usuario to set
	 */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
