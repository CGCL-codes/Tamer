package org.geoforge.guitlcolg.dialog.tabs.settings.opr.tabbedpane;

import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import org.geoforge.guitlcolg.dialog.tabs.settings.opr.panel.PnlTabsSettingsDspGlbSbsTop;
import org.geoforge.guitlc.dialog.tabs.settings.tabbedpane.TabCntSettingsInfDspTopGlbAbs;
import org.geoforge.wrpbasprssynolg.opr.GfrWrpBasSynTopSbss;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class TabCntSettingsInfDspTopGlbSbss extends TabCntSettingsInfDspTopGlbAbs {

    private static final int _F_INT_WIDTH_ = 440;

    private static final int _F_INT_HEIGHT_ = 250;

    public TabCntSettingsInfDspTopGlbSbss(ActionListener alrParent, ChangeListener clrParent) throws Exception {
        super(alrParent, GfrWrpBasSynTopSbss.getInstance().getCount(), TabCntSettingsInfDspTopGlbSbss._F_INT_WIDTH_, TabCntSettingsInfDspTopGlbSbss._F_INT_HEIGHT_);
        super._pnlDisplay_ = new PnlTabsSettingsDspGlbSbsTop(alrParent, clrParent);
    }
}
