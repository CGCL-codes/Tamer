package org.geoforge.guillcolg.treenode.prd;

import java.awt.event.ActionListener;
import java.util.Observer;
import java.util.logging.Logger;
import org.geoforge.guillc.tree.TreAbs;
import org.geoforge.guillcolg.popupmenu.prd.PmuCtlCtrLeafTloDskSbsMan;
import org.geoforge.guillcolg.treenode.GfrNodCtrMovFolderLblLclOlgTloAbs;
import org.geoforge.ioolg.finder.GfrFactoryIconOpr;
import org.geoforge.io.finder.GfrFactoryIconAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.mdldatolg.opr.GfrMdlDtaIdObjTloLclSbs;
import org.geoforge.wrpbasprssynolg.opr.GfrWrpBasTloSynOlgSbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class GfrNodCtrMovFolderLblDskTloSbs extends GfrNodCtrMovFolderLblLclOlgTloAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrNodCtrMovFolderLblDskTloSbs.class.getName());

    static {
        GfrNodCtrMovFolderLblDskTloSbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public GfrNodCtrMovFolderLblDskTloSbs(ActionListener alrController, String strIdUnique, TreAbs tre, ActionListener alrParentPanelMvc) throws Exception {
        super(alrController, alrParentPanelMvc, strIdUnique, GfrWrpBasTloSynOlgSbs.getInstance().getName(strIdUnique), GfrFactoryIconOpr.s_getFacFixSbs(GfrFactoryIconAbs.INT_SIZE_SMALL), tre);
        GfrMdlDtaIdObjTloLclSbs.getInstance().addObserver((Observer) this);
        super._pop_ = new PmuCtlCtrLeafTloDskSbsMan(alrController, strIdUnique, tre, alrParentPanelMvc);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        return true;
    }

    @Override
    public void destroy() {
        GfrMdlDtaIdObjTloLclSbs.getInstance().deleteObserver((Observer) this);
        super.destroy();
    }
}
