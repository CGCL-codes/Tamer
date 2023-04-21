package org.geoforge.guitlcolg.dialog.tabs.settings.oxp;

import java.awt.Frame;
import java.awt.event.ActionListener;
import org.geoforge.guitlcolg.dialog.tabs.settings.oxp.tabbedpane.TabCntSettingsInfDspObjNamTloLogWll;

/**
 *
 * @author bantchao
 */
public class GfrDlgTabsSettingsLogWll extends GfrDlgTabsSettingsDspLogAbs {

    public GfrDlgTabsSettingsLogWll(Frame frmOwner, String strId) throws Exception {
        super(frmOwner, "Well");
        super._tabContents_ = new TabCntSettingsInfDspObjNamTloLogWll((ActionListener) this, strId);
    }
}
