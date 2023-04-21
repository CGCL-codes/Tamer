package org.javenue.util.process;

/**
 * Callback interface for business operations that are to be executed in a transactional fashion and
 * which alter a process's state.
 * <p>
 * A <tt>Transition</tt>'s <code>doTransition()</code> method can be annotated <tt>@Idempotent</tt> which
 * means the code within the method will be automatically reattempted in the case of failure.
 * <p>
 * A <tt>Process</tt> that executes <tt>Transition</tt> objects via a <tt>TransitionManager</tt> can optionally
 * have its <code>processState</code> managed by the <tt>TransitionManager</tt>. When a <tt>Process</tt>'s
 * <code>stateManaged</code> flag is set to <code>true</code> then the <tt>TransitionManager</tt> will
 * automatically load the <code>processState</code> for the process from the persistence store,
 * inject it into the <tt>Process</tt> instance before the <code>doTransition()</code> method is invoked.
 * If the <code>doTransition()</code> method completes without throwing a runtime exception, then
 * the <tt>TransitionManager</tt> will automatically persist/commit the <code>processState</code> object
 * back to the persistence store.
 * <p>
 * If a process's <code>stateManaged</code> flag is set to <code>false</code>, then the code within
 * the <code>doTransition()</code> method will simply be executed by the <tt>TransitionManager</tt>
 * in a transactional fashion.
 * 
 * @see TransitionManager
 * @see TransitionWithResult
 * @author Benjamin Possolo
 * <p>Created on Jun 28, 2008
 */
public interface Transition {

    /**
	 * The designer of a <tt>Process</tt> should place transactional business operations and any
	 * operations that alter the <code>processState</code> object within an implementation
	 * of this method. Runtime exceptions thrown within this method will be caught by the
	 * <tt>TransitionManager</tt> and wrapped in a <tt>TransitionException</tt> before being
	 * passed up to the executing process.
	 * <p>
	 * This method is invoked by a <tt>TransitionManager</tt>.
	 * <p>
	 * If user code must return a value from within the <code>doTransition()</code> method,
	 * then the <tt>TransitionWithResult</tt> interface should be used instead.
	 * <p>
	 * This method can be annotated <tt>@Idempotent</tt> which
	 * means the code within the method will be automatically reattempted 
	 * in the case of failure.
	 */
    void doTransition();
}
