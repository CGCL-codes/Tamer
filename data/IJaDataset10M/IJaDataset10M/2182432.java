package org.geoforge.guitlc.dialog.tabs.settings.tabbedpane;

import java.awt.event.ActionListener;
import org.geoforge.guitlc.dialog.tabs.settings.panel.PnlTabsSettingsInfObjNamMlo;

/**
 *
 * @author bantchao
 */
public abstract class TabCntSettingsInfDspObjNamGlbMloAbs extends TabCntSettingsInfDspObjNamGlbAbs {

    protected TabCntSettingsInfDspObjNamGlbMloAbs(ActionListener alrParent, String strNameObject, String strCreationDateObject, int intW, int intH) {
        super(intW, intH);
        super._pnlInformation = new PnlTabsSettingsInfObjNamMlo(alrParent, strNameObject, strCreationDateObject);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        return true;
    }
}
