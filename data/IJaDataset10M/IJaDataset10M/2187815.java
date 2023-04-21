package medieveniti.data;

import javax.servlet.ServletException;

public class InvalidSessionException extends ServletException {

    private static final long serialVersionUID = 2710924851964608445L;

    public InvalidSessionException() {
        super("session is expired or was deleted.");
    }
}
