package org.geoforge.guitlcecl.dialog.tabs.settings;

import java.awt.Frame;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import org.geoforge.guitlcecl.dialog.tabs.settings.tabbedpane.TabCntSettingsInfDspTopGlbAres;
import org.geoforge.guitlc.dialog.tabs.settings.GfrDlgTabsSettingsDspGlbDftActionAbs;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class GfrDlgTabsSettingsDspGlbDftActionAres extends GfrDlgTabsSettingsDspGlbDftActionAbs {

    public GfrDlgTabsSettingsDspGlbDftActionAres(Frame frmOwner) throws Exception {
        super(frmOwner, "Areas");
        super._tabContents_ = new TabCntSettingsInfDspTopGlbAres((ActionListener) this, (ChangeListener) this);
    }
}
