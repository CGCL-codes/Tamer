package dessin.fabrique.exceptions;

/**
 * Erreur dans la classe {@code FabriquePoint}.
 */
@SuppressWarnings("serial")
public class FabriquePointException extends RuntimeException {

    /**
	 * Constructeur d'initialisation.
	 * @param message message d'erreur.
	 */
    public FabriquePointException(final String message) {
        super(message);
    }
}
