package unbbayes.io.exception;

import java.io.IOException;
import javax.xml.bind.JAXBException;

/**
 * Exception class when loading a network.
 * @author Rommel N. Carvalho
 * @author Michael S. Onishi
 * @version 1.0
 */
public class LoadException extends UBIOException {

    /** Serialization runtime version number */
    private static final long serialVersionUID = 0;

    public LoadException(Throwable t) {
        super(t);
    }

    public LoadException() {
        this("");
    }

    ;

    public LoadException(String msg) {
        super(msg);
    }

    ;
}
