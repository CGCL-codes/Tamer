package org.sbugs.logic.state;

import java.sql.*;
import java.util.*;
import org.sbugs.model.defect.*;
import org.sbugs.exceptions.*;
import org.sbugs.dao.state.StateMachineDAO;
import org.sbugs.logic.attributes.AttributeLogic;

public class DefectStateMachine {

    protected static DefectStateMachine instance;

    private Transition[][] transitions;

    protected DefectStateMachine(Connection connection) throws SQLException {
        initialize(connection);
    }

    protected void initialize(Connection connection) throws SQLException {
        List allStateList = AttributeLogic.getInstance().getAllStateList();
        StateMachineDAO.getInstance().loadStateMachine(this, allStateList, connection);
    }

    /**
	As a side effect of calling this method, all current transitions will be
	erased.
	*/
    public void setMaxStateId(int maxId) {
        transitions = new Transition[maxId + 1][maxId + 1];
    }

    public static DefectStateMachine getInstance(Connection connection) throws SQLException {
        if (instance == null) {
            synchronized (DefectStateMachine.class) {
                if (instance == null) {
                    instance = new DefectStateMachine(connection);
                }
            }
        }
        return instance;
    }

    public void updateState(Defect defect, int oldState, int newState, Map params, Connection connection) throws InvalidTransitionException, StateChangeException {
        Iterator transitionIterator = getHandlers(oldState, newState);
        defect.setStateId(newState);
        System.out.println("updating defect state - calling handlers");
        while (transitionIterator.hasNext()) {
            TransitionHandler handler = (TransitionHandler) transitionIterator.next();
            handler.handleTransition(defect, params, connection);
        }
    }

    /**
	@return an iterator for a list of {@link TransitionHandler}s.  The iterator
	will return the handlers in the order in which they should handle the state
	transitions.  Will never return null, just returns an empty iterator if no
	handlers are registered for this transition.
	@exception InvalidTransitionException if no transition exists from
	startState to endState
	*/
    protected Iterator getHandlers(int startState, int endState) throws InvalidTransitionException {
        int startIndex = startState;
        int endIndex = endState;
        if ((startIndex > transitions.length) || (endIndex > transitions[startIndex].length)) {
            throw new InvalidTransitionException(startState, endState, "Invalid state id range");
        }
        if (transitions[startIndex][endIndex] == null) {
            throw new InvalidTransitionException(startState, endState, "No transition defined");
        }
        return transitions[startIndex][endIndex].getHandlerList().iterator();
    }

    public void addTransition(int beginStateId, int endStateId, Transition transition) {
        if ((beginStateId > transitions.length) || (endStateId > transitions.length)) {
            throw new RuntimeException("Invalid state ids.  transitions.length:" + transitions.length + " begin state id: " + beginStateId + " end state id: " + endStateId);
        }
        transitions[beginStateId][endStateId] = transition;
    }

    public List getAllowedTransitions(int currState) {
        List transitionList = new ArrayList(transitions.length);
        for (int i = 0; i < transitions.length; i++) {
            if (transitions[currState][i] != null) {
                transitionList.add(transitions[currState][i]);
            }
        }
        return transitionList;
    }
}
