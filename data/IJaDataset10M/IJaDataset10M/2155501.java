package org.geoforge.guillcolg.menuitem.exp;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.geoforge.guillc.frame.FrmGfrAbs;
import org.geoforge.guillc.menuitem.MimTrsIdTloDescAbs;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.wrpbasprssynolg.oxp.GfrWrpBasTloSynOlgWll;

/**
 *
 * @author bantchao
 * 
 * TODO: listen to model in case of modified contents
 */
public class MimTrsIdTloDescWll extends MimTrsIdTloDescAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(MimTrsIdTloDescWll.class.getName());

    static {
        MimTrsIdTloDescWll._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public MimTrsIdTloDescWll(String strId) {
        super(strId);
    }

    @Override
    public void loadTransient() throws Exception {
        super.loadTransient();
        String strValue = GfrWrpBasTloSynOlgWll.getInstance().getDescription(super.getId());
        if (strValue == null || strValue.length() < 1) super.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            String strValue = GfrWrpBasTloSynOlgWll.getInstance().getDescription(super.getId());
            JFrame frmOwner = FrmGfrAbs.s_getFrameOwner(this);
            GfrOptionPaneAbs.s_showDialogInfo(frmOwner, strValue);
        } catch (Exception exc) {
            exc.printStackTrace();
            MimTrsIdTloDescWll._LOGGER_.severe(exc.getMessage());
            GfrOptionPaneAbs.s_showDialogError((Component) null, exc.getMessage());
        }
    }
}
