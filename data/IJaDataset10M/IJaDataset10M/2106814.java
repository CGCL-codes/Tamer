package net.zeminvaders.lang;

/**
 * The operator is invalid.
 *
 * @author <a href="mailto:grom@zeminvaders.net">Cameron Zemek</a>
 */
public class InvalidOperatorException extends ZemException {

    private static final long serialVersionUID = -57261291654807212L;

    public InvalidOperatorException(SourcePosition pos) {
        super("Invalid operator", pos);
    }
}
