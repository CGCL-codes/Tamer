package org.geoforge.guitlcolg.frame.secrun.button;

import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import org.geoforge.guillc.button.BIcnHlpOfflineOnthisAbs;
import org.geoforge.guihlpolg.property.oxp.PrpMgrPrivateHelpClass2idOxp;

/**
 *
 * 
 */
public class BIcnHlpOfflineOnthisWinViewSection extends BIcnHlpOfflineOnthisAbs {

    private static final long serialVersionUID = 1L;

    public BIcnHlpOfflineOnthisWinViewSection(ImageIcon iin, int intSizeIcon, MouseListener mlr) throws Exception {
        super(iin, intSizeIcon, mlr);
        super._setPropertyValueHelp(PrpMgrPrivateHelpClass2idOxp.getPrpHlpWinViewOlgSectionExp());
    }
}
