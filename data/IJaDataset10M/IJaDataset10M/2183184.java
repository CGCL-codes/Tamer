package org.geoforge.guitlcolg.panel.geostat.toolbar;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JToolBar;
import org.geoforge.guillc.toolbar.TbrSubAbs;
import org.geoforge.ioolg.util.property.oxp.PrpNamingOxp;
import org.geoforge.io.finder.GfrFactoryIconShared;

/**
 *
 * 
 */
public abstract class TbrSubDtaGstAbs extends TbrSubAbs {

    private static final String _F_STR_TITLE_FLOATABLE_SUFFIX = "data";

    protected TbrSubDtaGstAbs(MouseListener mlrEffectsBorder, ActionListener alrParentComponent, String strWhat) {
        super(mlrEffectsBorder, alrParentComponent, PrpNamingOxp.STR_NAME_DISPLAY_GST + " " + strWhat + " " + TbrSubDtaGstAbs._F_STR_TITLE_FLOATABLE_SUFFIX, JToolBar.VERTICAL, GfrFactoryIconShared.s_getPrint(GfrFactoryIconShared.INT_SIZE_MEDIUM), GfrFactoryIconShared.INT_SIZE_MEDIUM);
    }

    @Override
    public void loadTransient() throws Exception {
    }

    @Override
    public void releaseTransient() throws Exception {
    }
}
