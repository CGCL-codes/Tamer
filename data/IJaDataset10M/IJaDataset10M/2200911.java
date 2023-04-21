package br.ufal.graw.exception;

/**
	PersistenceInformationException is thrown by either a document, link or message.
*/
public class PersistentInformationException extends GrawException {

    /** Constructs a PersistenceInformationException with the specified detail message. */
    public PersistentInformationException(String message) {
        super(message);
    }
}
