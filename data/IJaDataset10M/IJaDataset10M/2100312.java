package org.jlense.uiworks.internal;

import org.jlense.uiworks.action.IMenuManager;
import org.jlense.uiworks.action.IToolBarManager;
import org.jlense.uiworks.action.SubMenuManager;
import org.jlense.uiworks.action.SubToolBarManager;
import org.jlense.uiworks.workbench.IActionBars;

/**
 * This class represents the action bars for an action set.
 */
public class ActionSetActionBars extends SubActionBars {

    private String actionSetId;

    /**
 * Constructs a new action bars object
 */
    public ActionSetActionBars(IActionBars parent, String actionSetId) {
        super(parent);
        this.actionSetId = actionSetId;
    }

    protected SubMenuManager createSubMenuManager(IMenuManager parent) {
        return new ActionSetMenuManager(parent, actionSetId);
    }

    protected SubToolBarManager createSubToolBarManager(IToolBarManager parent) {
        return new ActionSetToolBarManager(parent, actionSetId);
    }
}
