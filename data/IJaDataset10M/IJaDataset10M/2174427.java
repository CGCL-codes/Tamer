package com.ivis.xprocess.ui.datawrappers.process;

import java.util.ArrayList;
import com.ivis.xprocess.core.Xprocess;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.framework.XchangeElementContainer;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.ParentWrapper;

public class PrototypeWrapper extends ParentWrapper {

    public PrototypeWrapper(IElementWrapper parent, Xelement xelement) {
        super(parent, xelement);
    }

    @Override
    public Object[] getChildren() {
        return new ArrayList<IElementWrapper>().toArray();
    }

    /**
     * A default implementation that delegates to getChildren().
     *
     * @return all the Tasks
     */
    public Object[] getTasks() {
        return getChildren();
    }

    /**
     * @return true if the Prototype is not contained in a Process
     */
    public boolean isInAProject() {
        XchangeElementContainer container = getElement().getXchangeElementContainer();
        if (container instanceof Xprocess) {
            return false;
        }
        return true;
    }

    /**
     * @return the Process that this Prototype resides in
     */
    public Xprocess getProcess() {
        XchangeElementContainer container = getElement().getXchangeElementContainer();
        if (container instanceof Xprocess) {
            return (Xprocess) container;
        }
        return (Xprocess) container.getContainedIn();
    }

    /**
     * @return the Project that this Prototype is in, otherwise null
     */
    public Xproject getProject() {
        if (!isInAProject()) {
            return null;
        }
        return (Xproject) getElement().getXchangeElementContainer();
    }
}
