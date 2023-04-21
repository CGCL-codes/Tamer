package org.geoforge.guitlcolg.frame.main.prscpdman.toolbar;

import java.awt.event.MouseListener;
import org.geoforge.guillcolg.button.exp.BtnTransientDelAllObjOxp;
import org.geoforge.guillcolg.button.exp.BtnTransientExpObjOxp;
import org.geoforge.guillcolg.button.exp.BtnTransientImpObjOxp;
import org.geoforge.guillcolg.button.exp.BtnTransientNewObjOxp;
import org.geoforge.guitlc.frame.main.prscpdman.button.BIcnHlpOfflineOnthisSpaceProjectMan;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class TbrSpcClsPrjWwdManOlgExp extends TbrSpcClsPrjWwdManAbs {

    public TbrSpcClsPrjWwdManOlgExp(MouseListener mlrEffectsBorder) throws Exception {
        super(mlrEffectsBorder);
        super._btnHelpThisFrame = new BIcnHlpOfflineOnthisSpaceProjectMan(mlrEffectsBorder);
        super._btnNewObject = new BtnTransientNewObjOxp();
        super._btnDeleteAll = new BtnTransientDelAllObjOxp();
        super._btnImportObject = new BtnTransientImpObjOxp();
        super._btnExportObject = new BtnTransientExpObjOxp();
    }
}
