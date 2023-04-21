package biblioteca.dao.exceptions;

public class BibliotecaDAOException extends Exception {

    public BibliotecaDAOException() {
        super();
    }

    public BibliotecaDAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public BibliotecaDAOException(String message) {
        super(message);
    }

    public BibliotecaDAOException(Throwable cause) {
        super(cause);
    }
}
