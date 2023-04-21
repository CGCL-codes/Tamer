package org.geoforge.guillcolg.menuitem.exp;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.geoforge.guillc.frame.FrmGfrAbs;
import org.geoforge.guillc.menuitem.MimTrsIdTloDescAbs;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.wrpbasprssynolg.oxp.GfrWrpBasTloSynOlgS2d;

/**
 *
 * @author bantchao
 * 
 * TODO: listen to model in case of modified contents
 */
public class MimTrsIdTloDescS2d extends MimTrsIdTloDescAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(MimTrsIdTloDescS2d.class.getName());

    static {
        MimTrsIdTloDescS2d._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public MimTrsIdTloDescS2d(String strId) {
        super(strId);
    }

    @Override
    public void loadTransient() throws Exception {
        super.loadTransient();
        String strValue = GfrWrpBasTloSynOlgS2d.getInstance().getDescription(super.getId());
        if (strValue == null || strValue.length() < 1) super.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            String strValue = GfrWrpBasTloSynOlgS2d.getInstance().getDescription(super.getId());
            JFrame frmOwner = FrmGfrAbs.s_getFrameOwner(this);
            GfrOptionPaneAbs.s_showDialogInfo(frmOwner, strValue);
        } catch (Exception exc) {
            exc.printStackTrace();
            MimTrsIdTloDescS2d._LOGGER_.severe(exc.getMessage());
            GfrOptionPaneAbs.s_showDialogError((Component) null, exc.getMessage());
        }
    }
}
