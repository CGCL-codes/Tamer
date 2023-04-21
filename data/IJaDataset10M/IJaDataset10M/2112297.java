package redora.exceptions;

/**
 * Exception occurring when setting values.
 * 
 * @author Nanjing RedOrange (http://www.red-orange.cn)
 */
public class FieldException extends RedoraException {

    private static final long serialVersionUID = 1L;

    public FieldException(String s, Exception e) {
        super(s, e);
    }
}
