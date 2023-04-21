package com.ivis.xprocess.ui.perspectives.manager;

import com.ivis.xprocess.ui.UIConstants;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;

public class SimplerProjectManagerPerspectiveManager extends ProjectManagerPerspectiveManager {

    public SimplerProjectManagerPerspectiveManager(IElementWrapper currentElementWrapper) {
        super(currentElementWrapper);
        this.perspectiveId = UIConstants.projectmanager_perspective_id;
    }

    public SimplerProjectManagerPerspectiveManager(String perspectiveId) {
        super(perspectiveId);
    }

    public Object[] filterAlerts(Object[] objects) {
        return objects;
    }
}
