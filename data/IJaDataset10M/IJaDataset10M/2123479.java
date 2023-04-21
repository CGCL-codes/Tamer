package org.geoforge.guitlc.frame.main.prsshared.action;

/**
 *
 * 
 */
public class ActionSwitchPerspManage extends ActionSwitchPerspAbs {

    public static final String STR_ID_SUFFIX = "Manage";

    private static ActionSwitchPerspManage _INSTANCE = null;

    public static ActionSwitchPerspManage s_getInstance() {
        if (ActionSwitchPerspManage._INSTANCE == null) ActionSwitchPerspManage._INSTANCE = new ActionSwitchPerspManage();
        return ActionSwitchPerspManage._INSTANCE;
    }

    private ActionSwitchPerspManage() {
        super(ActionSwitchPerspManage.STR_ID_SUFFIX, ActionSwitchPerspManage.STR_ID_SUFFIX);
    }
}
