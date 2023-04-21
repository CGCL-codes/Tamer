package net.derquinse.common.util.concurrent;

import net.derquinse.common.base.NotInstantiable;

/**
 * Interruptions support class.
 * @author Andres Rodriguez
 */
public final class Interruptions extends NotInstantiable {

    /** Not instantiable. */
    private Interruptions() {
    }

    /**
	 * Throws {@link InterruptedException} if the current thread has been interrupted. If so, the
	 * interrupted flag is cleared.
	 */
    public static void throwIfInterrupted() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }
}
