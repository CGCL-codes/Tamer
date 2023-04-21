package org.maveryx.utils.xml.filter.exception;

/**
 * Classe per la gestione di errori generati a seguito di errori durante
 * la trasformazione di un elemento xml.
 * @author Riccardo Costa
 */
public class FilterException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * Costruttore per la classe.
	 *
	 */
    public FilterException() {
        super();
    }

    /**
	 * Costruisce l'oggetto impostando il messaggio di errore da visualizzare. 
	 * @param message Messaggio dell'errore generato.
	 */
    public FilterException(String message) {
        super(message);
    }
}
