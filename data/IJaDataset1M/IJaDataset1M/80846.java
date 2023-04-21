package org.geoforge.guitlcolg.frame.main.prsrun.action;

import org.geoforge.guitlc.frame.main.prsrun.action.ActionWindowViewerAbs;

/**
 *
 * 
 */
public class ActionWindowViewerSection extends ActionWindowViewerAbs {

    private static final String _STR_ID_SHORT_SUFFIX = "Section";

    private static ActionWindowViewerSection _INSTANCE = null;

    public static ActionWindowViewerSection s_getInstance() {
        if (ActionWindowViewerSection._INSTANCE == null) ActionWindowViewerSection._INSTANCE = new ActionWindowViewerSection();
        return ActionWindowViewerSection._INSTANCE;
    }

    private ActionWindowViewerSection() {
        super(ActionWindowViewerSection._STR_ID_SHORT_SUFFIX, "3D seismic section viewer");
    }
}
