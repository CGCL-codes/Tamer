package edu.url.lasalle.campus.scorm2004rte.util.exception;

/**
* $Id: NotFoundPropertiesException.java,v 1.3 2007/12/03 14:55:18 toroleo Exp $
* 
*  NotFoundPropertiesException.
*  Excepci� que indica que no se ha trobat
*  la propietat buscada en el fitxer indicat.
*
* @author M.Reyes La Salle mtoro@salle.url.edu
* @version 1.0 05/09/2007 $Revision: 1.3 $ $Date: 2007/12/03 14:55:18 $
* $Log: NotFoundPropertiesException.java,v $
* Revision 1.3  2007/12/03 14:55:18  toroleo
* S'ha afegit el meu e-mail a la capcelera de la classe.
*
* Revision 1.2  2007/11/09 15:36:16  toroleo
* Canvi de la capcelera.
*
*
*/
public class NotFoundPropertiesException extends Exception {

    /** */
    private static final long serialVersionUID = 1L;

    /** Nom del par�metre al que es vol accedir. */
    private String nameParameter;

    /**
 	* Constructor.
 	*
 	* @param aNameParameter nom del par�metre que causa l'excepci�.
 	*/
    public NotFoundPropertiesException(final String aNameParameter) {
        super("Falta el par�metre de configuraci�: '" + aNameParameter + "'");
        this.nameParameter = aNameParameter;
    }

    /**
	 * Retorna el nom del par�metre que ha causat l'excepci�.
	 * 
	 * @return Un String amb el nom del par�metre que ha causat l'excepci�.
	 */
    public final String getNombreParametro() {
        return nameParameter;
    }
}
