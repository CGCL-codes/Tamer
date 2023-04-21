package org.torweg.pulse.bundle;

/**
 * is the base class for all {@code Controller}s of <em>pulse</em>.
 * 
 * <p>
 * <strong>Attention:</strong> {@code Controller}s are singletons and
 * therefore not thread-safe. Both static and instance fields are shared among
 * different requests.
 * </p>
 * <p>
 * The execution of the {@code Controller}'s methods are controlled by
 * annotations.
 * </p>
 * 
 * @see org.torweg.pulse.annotations.Action
 * @see org.torweg.pulse.annotations.AnyAction
 * @see org.torweg.pulse.annotations.Permission
 * @see org.torweg.pulse.annotations.Groups
 * 
 * @author Thomas Weber
 * @version $Revision: 1383 $
 */
public class Controller {

    /**
	 * flag indicating whether the {@code Controller} is always running.
	 */
    private AlwaysRun alwaysRun;

    /**
	 * is used during {@code Bundle} initialisation, to configure the
	 * always run behaviour of the {@code Controller}.
	 * 
	 * @param ar
	 *            the always run setting
	 */
    public final void setAlwaysRun(final AlwaysRun ar) {
        this.alwaysRun = ar;
    }

    /**
	 * returns whether the {@code Controller} is run for every request, or
	 * if it is run only during request for its {@code Bundle}.
	 * 
	 * @return whether the {@code Controller} is run for every request, or
	 *         if it is run only during request for its {@code Bundle}.
	 */
    public final boolean isAlwaysRun() {
        return (this.alwaysRun != AlwaysRun.FALSE);
    }

    /**
	 * returns the AlwaysRun setting of the {@code Controller}.
	 * 
	 * @return the AlwaysRun setting
	 */
    public final AlwaysRun getAlwaysRun() {
        return this.alwaysRun;
    }

    /**
	 * enumerates the different settings for AlwaysRun.
	 * 
	 * @author Thomas Weber
	 */
    public enum AlwaysRun {

        /**
		 * is not always run.
		 */
        FALSE, /**
		 * always run before the controller with the action.
		 */
        PRE, /**
		 * always run, but after the controller with the action.
		 */
        POST
    }
}
