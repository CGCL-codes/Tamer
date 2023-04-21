package org.geoforge.guitlc.frame.main.prsshared.menuitem;

import org.geoforge.guitlc.frame.main.shared.menuitem.MimCloseThisAbs;
import org.geoforge.guitlc.frame.main.prsshared.action.ActionCloseChildSpaceProject;
import org.geoforge.io.finder.GfrFactoryIconShared;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class MimCloseThisSpaceProject extends MimCloseThisAbs {

    public MimCloseThisSpaceProject() {
        super(GfrFactoryIconShared.s_getCloseSpaceProject(GfrFactoryIconShared.INT_SIZE_SMALL));
    }

    @Override
    public void loadTransient() throws Exception {
        super._act = ActionCloseChildSpaceProject.s_getInstance();
        super.loadTransient();
    }
}
