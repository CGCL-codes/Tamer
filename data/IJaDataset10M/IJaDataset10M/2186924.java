package quickfixj.codegenerator;

/**
 * Signals an error in the code generation software.
 */
public class CodeGenerationException extends RuntimeException {

    public CodeGenerationException(Throwable cause) {
        super(cause);
    }
}
