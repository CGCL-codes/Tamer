package org.maveryx.utils.xml.filter.exception;

/**
 * Classe per la gestione di errori generati a seguito di errori durante
 * il salvataggio di un documento xml.
 * @author Riccardo Costa
 */
public class SaveXmlException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * Costruttore per la classe.
	 *
	 */
    public SaveXmlException() {
        super();
    }

    /**
	 * Costruisce l'oggetto impostando il messaggio di errore da visualizzare. 
	 * @param message Messaggio dell'errore generato.
	 */
    public SaveXmlException(String message) {
        super(message);
    }
}
