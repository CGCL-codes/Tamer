package org.mobicents.slee.container.component.test.du3;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.serviceactivity.ServiceStartedEvent;

public abstract class Sbb1 implements Sbb {

    public abstract void setSbb2(Sbb2LocalObject sbbLocalObject);

    public abstract Sbb2LocalObject getSbb2();

    public void sbbActivate() {
    }

    public void sbbCreate() throws CreateException {
    }

    public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) {
    }

    public void sbbLoad() {
    }

    public void sbbPassivate() {
    }

    public void sbbPostCreate() throws CreateException {
    }

    public void sbbRemove() {
    }

    public void sbbRolledBack(RolledBackContext arg0) {
    }

    public void sbbStore() {
    }

    public void setSbbContext(SbbContext arg0) {
    }

    public void unsetSbbContext() {
    }

    public void onServiceStartedEvent(ServiceStartedEvent event, ActivityContextInterface aci) {
    }
}
