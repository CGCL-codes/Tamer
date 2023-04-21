package org.fpdev.apps.rtemaster.actions;

import org.fpdev.core.data.LegacyDB;
import org.fpdev.apps.rtemaster.RouteMaster;

/**
 *
 * @author demory
 */
public class CreateLandmarkAction implements ACAction {

    private String name_, loc_;

    /** Creates a new instance of CreateLandmarkAction */
    public CreateLandmarkAction(String name, String loc) {
        name_ = name;
        loc_ = loc;
    }

    public String getName() {
        return "Create Landmark";
    }

    public void updateDB(LegacyDB db) {
        db.initLandmark(name_, loc_);
    }

    public boolean doAction(RouteMaster av) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean undoAction(RouteMaster av) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
