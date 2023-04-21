package edu.cmu.sphinx.linguist.flat;

/**
 * a visitor interface
 */
interface SentenceHMMStateVisitor {

    /**
     * Method called when a state is visited by the vistor
     *
     * @param state the state that is being visited
     *
     * @return true if the visiting should be terminated
     */
    public boolean visit(SentenceHMMState state);
}
