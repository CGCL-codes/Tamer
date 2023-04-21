package org.geoforge.guitlc.frame.main.prsrun.menuitem;

import org.geoforge.guitlc.frame.main.prsrun.action.ActionCloseAllViewers;
import org.geoforge.guillc.menuitem.MimCloseAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class MimCloseAllViewers extends MimCloseAbs {

    public MimCloseAllViewers() {
        super();
    }

    @Override
    public void loadTransient() throws Exception {
        super._act = ActionCloseAllViewers.s_getInstance();
        super.loadTransient();
    }
}
