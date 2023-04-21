package machine;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import machine.JmlTemporalState;
import runtime.JMLTemporalSpecificationError;
import java.util.Observable;
import java.util.Map;

public class JmlRuntimeTemporalStateMachine extends Observable {

    private int initStateNumber;

    private List<JmlTemporalState> listOfStates;

    private Map<JmlTemporalState, Map<String, JmlTemporalState>> transitionTable;

    public Map<JmlTemporalState, Map<String, JmlTemporalState>> getTransitionTable() {
        return transitionTable;
    }

    private int currentStateNumber;

    public int getCurrentStateNumber() {
        return currentStateNumber;
    }

    public void setCurrentStateNumber(int currentStateNumber) {
        this.currentStateNumber = currentStateNumber;
    }

    private JmlTemporalState lastStateAdded;

    private int temporalFormulaNumber;

    public JmlRuntimeTemporalStateMachine() {
        listOfStates = new ArrayList<JmlTemporalState>();
        initStateNumber = -1;
        transitionTable = new HashMap<JmlTemporalState, Map<String, JmlTemporalState>>();
        currentStateNumber = -1;
        lastStateAdded = null;
    }

    public JmlRuntimeTemporalStateMachine(JmlRuntimeTemporalStateMachine inp) {
        this.initStateNumber = inp.initStateNumber;
        this.listOfStates = inp.listOfStates;
        this.transitionTable = inp.transitionTable;
        this.currentStateNumber = inp.initStateNumber;
        this.lastStateAdded = inp.lastStateAdded;
    }

    public void addTransition(int srcStateNum, String ev, int destStateNum) {
        JmlTemporalState srcState = getState(srcStateNum);
        JmlTemporalState destState = getState(destStateNum);
        Map<String, JmlTemporalState> transitionsFromSrc = transitionTable.get(srcState);
        if (transitionsFromSrc != null) transitionsFromSrc.put(ev.toString(), destState); else {
            transitionsFromSrc = new HashMap<String, JmlTemporalState>();
            transitionsFromSrc.put(ev.toString(), destState);
            transitionTable.put(srcState, transitionsFromSrc);
        }
    }

    public int getTemporalFormulaNumber() {
        return temporalFormulaNumber;
    }

    public void setTemporalFormulaNumber(int temporalFormulaNumber) {
        this.temporalFormulaNumber = temporalFormulaNumber;
    }

    public JmlTemporalState getState(int stateNumber) {
        for (JmlTemporalState s : listOfStates) {
            if (s.getStateNumber() == stateNumber) return s;
        }
        return null;
    }

    public void setInitState(int statenum) {
        this.initStateNumber = statenum;
    }

    public void setAsNonAcceptingState(int statenum) {
        for (JmlTemporalState s : listOfStates) {
            if (s.getStateNumber() == statenum) s.setAsNonAcceptingState();
        }
    }

    public int getInitStateNumber() {
        return initStateNumber;
    }

    public void addState(JmlTemporalState s) {
        listOfStates.add(s);
        lastStateAdded = s;
    }

    public int getNumberOfLastStateAdded() {
        return (lastStateAdded == null) ? -1 : lastStateAdded.getStateNumber();
    }

    public JmlTemporalState getLastStateAdded() {
        return lastStateAdded;
    }

    public int numberOfStates() {
        return listOfStates.size();
    }

    public List<JmlTemporalState> getListOfStates() {
        return listOfStates;
    }

    public void setListOfStates(List<JmlTemporalState> listOfStates) {
        this.listOfStates = listOfStates;
    }

    public void consume(String ev) throws JMLTemporalSpecificationError {
        Map<String, JmlTemporalState> transitionsFromCurrentState = transitionTable.get(getState(currentStateNumber));
        if (transitionsFromCurrentState != null) {
            JmlTemporalState newState = transitionsFromCurrentState.get(ev.toString());
            if (newState != null) currentStateNumber = newState.getStateNumber();
        }
        if (getState(currentStateNumber).isTracePropCheckingState()) {
            setChanged();
            notifyObservers();
            clearChanged();
            if (getState(currentStateNumber).isTransienceState()) getState(currentStateNumber).setAsNonTracePropertyChecking();
        }
    }

    public boolean inFinalState() {
        return getState(currentStateNumber).isAcceptingState();
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        toString(s);
        return s.toString();
    }

    public void toString(StringBuffer s) {
        s.append("machine{");
        for (Iterator<JmlTemporalState> it = listOfStates.iterator(); it.hasNext(); ) {
            JmlTemporalState ts = it.next();
            ts.toString(s);
            if (it.hasNext()) s.append(",");
        }
        s.append(";");
        Set<JmlTemporalState> setofstates = transitionTable.keySet();
        for (Iterator<JmlTemporalState> tsit = setofstates.iterator(); tsit.hasNext(); ) {
            JmlTemporalState tempstate = tsit.next();
            Map<String, JmlTemporalState> transitions_from_tempstate = transitionTable.get(tempstate);
            Set<String> setofevents = transitions_from_tempstate.keySet();
            for (Iterator<String> eit = setofevents.iterator(); eit.hasNext(); ) {
                s.append("s" + tempstate.getStateNumber() + "");
                s.append("[");
                String e = eit.next();
                s.append(e);
                s.append("]");
                s.append("-->");
                s.append("s(" + transitions_from_tempstate.get(e).getStateNumber() + ")");
                if (eit.hasNext()) s.append(",");
            }
            if (tsit.hasNext()) s.append(",");
        }
        s.append("}");
    }
}
