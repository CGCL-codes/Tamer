package net.sf.jsfcomp.hibernatetrace.domain.xml;

import java.util.LinkedList;
import java.util.List;
import net.sf.jsfcomp.hibernatetrace.domain.State;

/**
 * @author Mert Caliskan
 * 
 */
public class States {

    private List stateList = new LinkedList();

    public List getStateList() {
        return stateList;
    }

    public void setStateList(List stateList) {
        this.stateList = stateList;
    }

    public void addState(State state) {
        getStateList().add(state);
    }
}
