package alefpp.core.exceptions;

/**
 * Undefined Type Exception for non-existens modules (classes)
 * @author Adrabi Abderrahim
 * @version 1.1
 */
public class UndefinedTypeException extends Exception {

    private static final long serialVersionUID = 1L;

    public UndefinedTypeException(String $1, long[] $2) {
        super($1 + " cannot be resolved to a type @[" + $2[0] + "," + $2[1] + "]");
    }
}
