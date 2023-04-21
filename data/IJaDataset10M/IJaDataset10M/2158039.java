package tk.srmi;

/**
 * Represents data about invocation of a method of the Remote interface.
 * @version 0.0.2, 04 Jan 2008
 * @author Andrew Kartashev
 * @param <Remote> interface whose methods may be invoked from a non-local
 * virtual machine.
 */
public interface StubHandler<Remote> {

    /**
     * Called when method of connected Remote is invoked.
     * @param stubId object identifying Remote method of whih was invoked.
     * @param invocation information about invoked method and passed
     * parameters.
     */
    void invoked(Object stubId, Invocation<Remote> invocation);
}
