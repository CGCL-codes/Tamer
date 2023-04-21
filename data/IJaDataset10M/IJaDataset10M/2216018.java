package tp2.modelo.excepciones;

public class ComposicionIncompleta extends RuntimeException {

    public ComposicionIncompleta() {
        super();
    }

    public ComposicionIncompleta(String message, Throwable cause) {
        super(message, cause);
    }

    public ComposicionIncompleta(String message) {
        super(message);
    }

    public ComposicionIncompleta(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 1L;
}
