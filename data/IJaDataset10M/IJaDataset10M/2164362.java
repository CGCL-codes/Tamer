package calclipse.mcomp.cntxt;

/**
 * Used by the {@link calclipse.mcomp.cntxt.tokens.Exit} token
 * to handle the exit operation.
 * @author T. Sommerland
 */
public interface ExitPoint {

    ExitPoint DEFAULT = new DefaultExitPoint();

    /**
     * Implementations may choose not to exit.
     */
    void exit(int status);
}
