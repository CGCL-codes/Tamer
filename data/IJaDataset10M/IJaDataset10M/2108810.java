package org.geoforge.guitlc.frame.main.spcprtroot.action;

import org.geoforge.guitlc.frame.main.spcprtwork.action.ActionCloseChildSpaceWork;
import org.geoforge.guitlc.frame.main.shared.action.ActionManageChildrenAbs;

/**
 *
 * 
 */
public class ActionManageChildrenSpaceWorks extends ActionManageChildrenAbs {

    private static ActionManageChildrenSpaceWorks _INSTANCE = null;

    public static ActionManageChildrenSpaceWorks s_getInstance() {
        if (ActionManageChildrenSpaceWorks._INSTANCE == null) ActionManageChildrenSpaceWorks._INSTANCE = new ActionManageChildrenSpaceWorks();
        return ActionManageChildrenSpaceWorks._INSTANCE;
    }

    private ActionManageChildrenSpaceWorks() {
        super(ActionCloseChildSpaceWork.STR_ID_SUFFIX + "s", ActionCloseChildSpaceWork.STR_ID_SUFFIX.toLowerCase() + "s");
    }
}
