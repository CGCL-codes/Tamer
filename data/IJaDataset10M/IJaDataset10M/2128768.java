package org.stateengine.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.stateengine.entity.IStateEngineUser;
import org.stateengine.state.IStateContainer;
import org.stateengine.state.State;
import org.stateengine.storage.IDBConnection;

public class ChainedAction extends AActionOnState {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private List listActions;

    public ChainedAction(String label) {
        super(label);
        listActions = new ArrayList();
    }

    public void addAction(IActionOnState action) {
        listActions.add(action);
    }

    public void perform(IDBConnection connection, IStateEngineUser loggedUser, HttpSession sess, HttpServletRequest request, IStateContainer parent, State state) {
        Iterator itActions = listActions.iterator();
        while (itActions.hasNext()) {
            IActionOnState action = (IActionOnState) itActions.next();
            action.perform(connection, loggedUser, sess, request, parent, state);
            if ((state.getErrorMessage() != null) && (!state.getErrorMessage().trim().equals(""))) {
                return;
            }
        }
    }
}
