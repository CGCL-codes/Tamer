package org.geoforge.guitlc.frame.main.spcprtwork.menuitem;

import org.geoforge.guillc.menuitem.MimTransOpenAbs;
import org.geoforge.guitlc.frame.main.spcprtwork.action.ActionOpenChildFromListSpaceProject;
import org.geoforge.io.finder.GfrFactoryIconShared;

/**
 *
 * 
 */
public class MimOpenChildFromListSpaceProject extends MimTransOpenAbs {

    public MimOpenChildFromListSpaceProject() {
        super(GfrFactoryIconShared.s_getOpenSpaceProject(GfrFactoryIconShared.INT_SIZE_SMALL));
    }

    @Override
    public void loadTransient() throws Exception {
        super._act = ActionOpenChildFromListSpaceProject.s_getInstance();
        super.loadTransient();
    }
}
