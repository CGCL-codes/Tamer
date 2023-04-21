package errorStateRecognition.Exception;

/**
 * Classe per la gestione di errori generati a seguito di errori durante la
 * gestione del repository.
 * 
 * @author Riccardo Costa
 * @version 1.0
 */
public class RepositoryException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * Costruttore per la classe.
	 * 
	 */
    public RepositoryException() {
        super();
    }

    /**
	 * Costruisce l'oggetto impostando il messaggio di errore da visualizzare.
	 * 
	 * @param message
	 *            Messaggio dell'errore generato.
	 */
    public RepositoryException(String message) {
        super(message);
    }
}
