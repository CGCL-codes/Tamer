package org.jnf.persistence;

/**
 * 
 * @author Pablo Krause, powered by GUCOBA Systems S.C.
 * 
 */
public interface GenericDAO {

    /**
	 * Limpia las operaciones pendientes de ejecutar contra la base de datos. 
	 */
    void clear();

    /**
	 * Manda ejecutar las operaciones pendientes de ejecutar contra la base de datos.
	 */
    void flush();
}
