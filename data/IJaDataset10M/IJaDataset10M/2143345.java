package edu.it.contingency.context;

import edu.it.contingency.ContingencyListener;

/**
 * @author bardram
 */
public interface Resource extends ContingencyListener {

    public int getState();

    public void setState(int state);
}
