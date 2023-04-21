package br.com.arsmachina.authorization.exception;

/**
 * Class that represents an authorized attempt to read objects from a type.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ReadTypeAuthorizationException extends TypeAuthorizationException {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructor that receives a message and a type.
	 * 
	 * @param message a {@link String}.
	 * @param type a {@link Class} instance. It cannot be null.
	 */
    public ReadTypeAuthorizationException(String message, Class<?> type) {
        super(message, type);
    }

    /**
	 * Constructor that receives a type.
	 * 
	 * @param type a {@link Class} instance. It cannot be null.
	 */
    public ReadTypeAuthorizationException(Class<?> type) {
        super(String.format("Unauthorized %s read attempt", type.getName()), type);
    }
}
