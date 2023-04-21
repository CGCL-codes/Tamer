package com.touchgraph.graphlayout.interaction;

/**
 * TGUserInterface. A user interface that can be activated or deactivated, much like a listener can be added or removed.
 * 
 * If a parent UI is specified as a parameter to activate() then the parent UI is temporarily disabled while the current UI is active. Classes that extend TGUserInterface must call super.deactivate() if they override this method.
 * 
 * @author Alexander Shapiro
 * @version 1.22-jre1.1 $Id: TGUserInterface.java,v 1.1 2003/05/05 01:25:43 sauerf Exp $
 */
public abstract class TGUserInterface {

    private TGUserInterface parentUI;

    public abstract void activate();

    /** Each user interface is responsible for properly setting this variable. */
    boolean active;

    public boolean isActive() {
        return active;
    }

    public void activate(TGUserInterface parent) {
        parentUI = parent;
        parentUI.deactivate();
        activate();
    }

    public void deactivate() {
        if (parentUI != null) {
            parentUI.activate();
        }
        parentUI = null;
    }
}
