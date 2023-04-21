package testconf.framework.engine;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * StateInfo eXtended with Transitions
 *
 * @author  mutilin
 */
public class StateInfo {

    public int id;

    public List transitionList = new Vector();

    public boolean isUntested = true;

    public Iterator last;

    /** Creates a new instance of StateInfo */
    public StateInfo() {
    }

    /** Creates a new instance of StateInfo */
    public StateInfo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List getTransitions() {
        return transitionList;
    }

    public boolean isUntested() {
        return isUntested;
    }

    public void setUntested(boolean b) {
        isUntested = b;
    }

    public Iterator getLast() {
        return last;
    }

    public void setLast(Iterator test) {
        last = test;
    }
}
