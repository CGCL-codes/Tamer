package org.geoforge.guillcolg.popupmenu.exp;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.geoforge.guillcolg.menuitem.exp.MimTrsIdCopMloPrf;
import org.geoforge.guillcolg.menuitem.exp.MimTrsIdDelMloPrf;
import org.geoforge.guillcolg.menuitem.exp.MimTrsIdRenMloPrf;
import org.geoforge.guillcolg.menuitem.exp.MimTrsIdSetMloPrfGlobe;
import org.geoforge.guillcolg.popupmenu.PmuCtlCtrLeafMloLclOlgAbs;
import org.geoforge.guillc.tree.TreAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class PmuCtlCtrLeafMloDskPrfAbs extends PmuCtlCtrLeafMloLclOlgAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PmuCtlCtrLeafMloDskPrfAbs.class.getName());

    static {
        PmuCtlCtrLeafMloDskPrfAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected PmuCtlCtrLeafMloDskPrfAbs(ActionListener alrController, ActionListener alrParentPanelMvc, String strUniqueId, TreAbs tree, String strIdParent) {
        super(strUniqueId, tree);
        super._mimCopy = new MimTrsIdCopMloPrf(alrController, strUniqueId, strIdParent);
        super._mimRename = new MimTrsIdRenMloPrf(alrController, strUniqueId, strIdParent);
        super._mimDelete = new MimTrsIdDelMloPrf(alrController, strUniqueId, strIdParent);
        super._mimSettings = new MimTrsIdSetMloPrfGlobe(alrController, strUniqueId, strIdParent);
    }
}
