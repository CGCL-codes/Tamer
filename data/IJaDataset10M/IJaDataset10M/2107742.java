package org.geoforge.guillc.button;

import java.awt.event.ActionListener;
import org.geoforge.guillc.menuitem.MimFolderAllCollapse;
import org.geoforge.guillc.button.BtnTransientAbs;
import org.geoforge.io.finder.GfrFactoryIconShared;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class BtnAllCollapse extends BtnTransientAbs {

    public BtnAllCollapse(ActionListener alr) {
        super(alr, GfrFactoryIconShared.s_getCollapseAllFolderTree(GfrFactoryIconShared.INT_SIZE_SMALL), GfrFactoryIconShared.INT_SIZE_SMALL, MimFolderAllCollapse.STR_TIP);
        super.setEnabled(false);
    }
}
