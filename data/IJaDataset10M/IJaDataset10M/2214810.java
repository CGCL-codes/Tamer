package dessin.fabrique.exceptions;

/**
 * Erreur indiquant que le dessin est impossible a construire
 * a cause des relations entre les points.
 */
@SuppressWarnings("serial")
public class DessinImpossibleException extends RuntimeException {

    /**
	 * Constructeur d'initialisation.
	 * @param message message d'erreur.
	 */
    public DessinImpossibleException(final String message) {
        super(message);
    }
}
