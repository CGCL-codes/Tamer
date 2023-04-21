package PatchConversion;

/**
 * Exception caused by invalid process while defining a generic patch -
 * a connection without a source or target, module name not found, etc
 *
 * @author Kenneth L. Martinez
 */
public class PatchDefinitionException extends Exception {

    public PatchDefinitionException(String s) {
        super(s);
    }
}
