package imi.shader.dynamic;

/**
 * This exception is the base class for all dynamic shader compilation exceptions
 * @author Ronald E Dahlgren
 */
public class GLSLCompileException extends Exception {

    public GLSLCompileException(String message) {
        super("Compilation Error: " + message);
    }
}
