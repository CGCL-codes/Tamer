package org.stateengine.sample;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.stateengine.entity.IStateEngineUser;
import org.stateengine.state.State;
import org.stateengine.storage.IDBConnection;

public class IntroductionSampleState extends State {

    private static final long serialVersionUID = 1L;

    protected Map initActionsMap() {
        Map actions = super.initActionsMap();
        addActionToMap(actions, new GoToSecondSampleAction("second"));
        return actions;
    }

    public String forwardResponse(IDBConnection connection, IStateEngineUser loggedUser, HttpSession sess, HttpServletRequest request) {
        return "/sample/intro.jsp";
    }
}
