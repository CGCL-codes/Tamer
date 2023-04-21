package org.stars.daostars.conversion;

import java.io.Serializable;

/**
 * Rappresenta l'alias di una classe.
 * 
 * @author Francesco Benincasa (908099)
 *
 */
public class TypeAlias implements Serializable {

    /**
	 * serial id
	 */
    private static final long serialVersionUID = -4384308468287445745L;

    protected String name;

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the clazz
	 */
    public TypeHandler getHandler() {
        return handler;
    }

    /**
	 * @param clazz the clazz to set
	 */
    public void setHandler(TypeHandler value) {
        this.handler = value;
    }

    protected TypeHandler handler;
}
