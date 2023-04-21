package org.gvsig.crs;

/**
 * Clase que captura las excepciones cuando se definen los CRS
 * 
 * @author Miguel Garc�a Jim�nez (garciajimenez.miguel@gmail.com)
 *
 */
public class CrsException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public CrsException(Exception e) {
        super(e);
    }
}
