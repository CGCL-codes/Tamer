package org.geoforge.guitlc.frame.main.spcprtroot.button;

import org.geoforge.guillc.button.BtnTransIcnOpenAbs;
import org.geoforge.guitlc.frame.main.spcprtroot.action.ActionOpenChildFromListSpaceWork;
import org.geoforge.io.finder.GfrFactoryIconShared;

/**
 *
 * 
 */
public class BtnTransIcnOpenChildFromListSpaceWork extends BtnTransIcnOpenAbs {

    public BtnTransIcnOpenChildFromListSpaceWork() {
        super(GfrFactoryIconShared.s_getOpenSpaceWork(GfrFactoryIconShared.INT_SIZE_LARGE));
    }

    @Override
    public void loadTransient() throws Exception {
        super._act = ActionOpenChildFromListSpaceWork.s_getInstance();
        super.loadTransient();
    }
}
