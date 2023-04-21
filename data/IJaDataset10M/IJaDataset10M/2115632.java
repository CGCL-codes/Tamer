package com.daveoxley.cnery.actions;

import com.daveoxley.cbus.CGateException;
import com.daveoxley.cbus.CGateSession;
import com.daveoxley.cbus.Group;
import java.io.Serializable;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 *
 * @author Dave Oxley <dave@daveoxley.co.uk>
 */
@Name("customAction")
@Scope(ScopeType.EVENT)
public class CustomAction implements Serializable {

    @In
    private CGateSession cGateSession;

    private Group group;

    private int value = -1;

    private String action;

    public void setAddress(String address) throws CGateException {
        group = (Group) cGateSession.getCGateObject(address);
        doAction();
    }

    public void setValue(int value) throws CGateException {
        this.value = value;
        doAction();
    }

    public void setAction(String action) throws CGateException {
        this.action = action;
        doAction();
    }

    private void doAction() throws CGateException {
        if (action == null) return;
        if (action.equals("ramp")) {
            if (group == null || value == -1) return;
            group.ramp(value, 0);
        } else if (action.equals("toggle")) {
            if (group == null) return;
            if (group.getLevel() > 0) group.off(); else group.on();
        }
    }
}
