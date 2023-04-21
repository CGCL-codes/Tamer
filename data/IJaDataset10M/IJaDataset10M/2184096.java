package edu.rice.cs.drjava.model.repl;

/**
 * Interface for any listener to a SimpleInteractionsDocument,
 * which can be used in a standalone interactions window.
 * @version $Id: SimpleInteractionsListener.java 1179 2003-02-06 06:18:14Z csreis $
 */
public interface SimpleInteractionsListener {

    /**
   * Called when an interaction has started.
   */
    public void interactionStarted();

    /**
   * Called when an interaction has ended.
   */
    public void interactionEnded();
}
