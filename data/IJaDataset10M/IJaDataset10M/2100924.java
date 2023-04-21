package net.sf.jdsc;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public class FullDataStructureException extends DataStructureException {

    private static final long serialVersionUID = 1061221745539419070L;

    public FullDataStructureException() {
        super();
    }

    public FullDataStructureException(String message) {
        super(message);
    }

    public FullDataStructureException(Throwable cause) {
        super(cause);
    }

    public FullDataStructureException(String message, Throwable cause) {
        super(message, cause);
    }
}
