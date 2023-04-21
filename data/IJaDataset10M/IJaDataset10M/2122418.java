package exceptions;

@SuppressWarnings("serial")
public class MessageChatInvalide extends Exception {

    /**
	 * Appel du constructeur simple.
	 * @param cause Un bref message d'erreur sur la cause de l'exception
	 */
    public MessageChatInvalide(String cause) {
        super(cause);
    }
}
