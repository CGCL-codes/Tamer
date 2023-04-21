package org.humboldt.cassia.core.jdo;

import java.io.Serializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Clase que representa los parametros de la aplicaci�n  
 * 
 * @author Benjamin A. Rodriguez R. benjamin.a.rodriguez@gmail.com
 * 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Parametros implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    Long id;

    @Persistent
    String tipo;

    @Persistent
    String nombre;

    @Persistent
    String valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Parametros(String nombre) {
        super();
        this.nombre = nombre;
    }

    /**
	 * @return the tipo
	 */
    public String getTipo() {
        return tipo;
    }

    /**
	 * @param tipo the tipo to set
	 */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
	 * @return the valor
	 */
    public String getValor() {
        return valor;
    }

    /**
	 * @param valor the valor to set
	 */
    public void setValor(String valor) {
        this.valor = valor;
    }
}
