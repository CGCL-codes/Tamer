package org.geoforge.guillcolg.treenode;

import java.awt.event.ActionListener;
import java.util.Observable;
import org.geoforge.guillcolg.treenode.GfrNodCtrMovLeafChkSelTloLclOlgAbs;
import java.util.logging.Logger;
import org.geoforge.guillc.tree.TreAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.mdldat.event.GfrEvtMdlIdDtaAddedMlo;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class GfrNodCtrMovLeafChkSelTloLclOlgLineAbs extends GfrNodCtrMovLeafChkSelTloLclOlgAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrNodCtrMovLeafChkSelTloLclOlgLineAbs.class.getName());

    static {
        GfrNodCtrMovLeafChkSelTloLclOlgLineAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected GfrNodCtrMovLeafChkSelTloLclOlgLineAbs(ActionListener alrController, String strIdUnique, String strName, TreAbs tre) throws Exception {
        super(alrController, strIdUnique, strName, tre);
    }

    @Override
    public void update(Observable obs, Object objEvt) {
        if (objEvt instanceof GfrEvtMdlIdDtaAddedMlo) return;
        super.update(obs, objEvt);
    }
}
