package edu.cmu.sphinx.decoder.linguist;

import edu.cmu.sphinx.decoder.linguist.SentenceHMMState;
import edu.cmu.sphinx.decoder.linguist.ContextBucket;

/**
 * Represents a path in a SentenceHMM. The path includes the
 * particular state, and the states left context.  This is used while
 * building/compiling a SentenceHMM
 */
public class StatePath {

    SentenceHMMState state;

    ContextBucket context;

    /**
     * Creates a StatePath
     *
     * @param state the state for the StatePath
     * @param context the context bucket
     */
    StatePath(SentenceHMMState state, ContextBucket context) {
        this.state = state;
        this.context = context;
    }

    /**
     * Retrieves the state for the state path
     *
     * @return the state
     */
    SentenceHMMState getState() {
        return state;
    }

    /**
     * Retrieves the context for the state path
     *
     * @return the context
     */
    ContextBucket getContext() {
        return context;
    }

    /**
     * Returns the string representation for this StatePath
     *
     * @return the string representation
     */
    public String toString() {
        return state.toString() + " " + context.toString();
    }
}
