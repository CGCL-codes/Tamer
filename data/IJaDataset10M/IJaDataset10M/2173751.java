package org.amse.bomberman.client.viewmanager;

/**
 * Class that represents state of ViewManager.
 *
 * @author Kirilchuk V.E.
 */
public interface State {

    /**
     * Method that initialize current state. For example subscribe listeners,
     * setting up some constants and so on.
     */
    void init();

    /**
     * Method that used to switch to previous state.
     */
    void previous();

    /**
     * Method that used to switch to next state.
     */
    void next();

    /**
     * Sets previous state.
     *
     * @param previous state to set as previous for this state.
     * @return this state with setted previous state.
     */
    State setPrevious(State previous);

    /**
     * Sets next state.
     *
     * @param next state to set as next for this state.
     * @return this state with setted next state.
     */
    State setNext(State next);

    /**
     * Method that uninitialize current state. For example unsubscribe listeners,
     * resetting some constants and so on.
     */
    void release();
}
