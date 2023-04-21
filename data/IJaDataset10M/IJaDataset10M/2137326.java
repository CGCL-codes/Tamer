package org.likken.web.actions;

import org.likken.web.states.*;
import org.likken.util.Recyclable;

/**
 * @author Stephane Boisson
 * @version $Revision: 1.4 $ $Date: 2004/02/22 19:38:10 $
 */
public abstract class AbstractAction implements Action {

    public AbstractAction() {
    }

    public void init(final String[] extraInfo) {
    }

    public void perform(LoginState theCurrentState) throws StateException {
        unexpectedAction(theCurrentState);
    }

    public void perform(EntryDisplayState theCurrentState) throws StateException {
        unexpectedAction(theCurrentState);
    }

    public void perform(EntryEditionState theCurrentState) throws StateException {
        unexpectedAction(theCurrentState);
    }

    public void perform(AttributeDownloadState theCurrentState) throws StateException {
        unexpectedAction(theCurrentState);
    }

    public void perform(BrowsingState theCurrentState) throws StateException {
        unexpectedAction(theCurrentState);
    }

    protected void unexpectedAction(State theCurrentState) throws StateException {
        throw new StateException("Unexpected action " + this + " in state " + theCurrentState);
    }

    protected void changeState(final String aStateName) throws StateException {
        throw new StateTransitionException(StateFactory.getState(aStateName));
    }
}
