package org.geoforge.guitlcolg.panel.cpd.wwd.oxp.toolbar;

import org.geoforge.guitlc.panel.cpd.wwd.toolbar.TbrSubCadWwdObjSelAbs;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.logging.Logger;
import org.geoforge.guitlcolg.panel.cpd.wwd.opr.button.BIcnHlpOfflineOnthisSelOlgPrd;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class TbrSubCadWwdObjSelOlgPrd extends TbrSubCadWwdObjSelAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(TbrSubCadWwdObjSelOlgPrd.class.getName());

    static {
        TbrSubCadWwdObjSelOlgPrd._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public TbrSubCadWwdObjSelOlgPrd(ActionListener alrParentComponent, MouseListener mlrEffectsBorder) throws Exception {
        super(mlrEffectsBorder, alrParentComponent);
        super._btnHelpThisFrame = new BIcnHlpOfflineOnthisSelOlgPrd(mlrEffectsBorder);
    }
}
