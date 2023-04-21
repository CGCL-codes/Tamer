package org.geoforge.guillc.wwd.toolbar;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.JToolBar;
import org.geoforge.guillc.wwd.buttongroup.BgpKindView;
import org.geoforge.guillc.wwd.toolbar.TbrSubDisplayWwdAbs;
import org.geoforge.io.finder.GfrFactoryIconAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class TbrSubDisplayWwdPicksManAbs extends TbrSubDisplayWwdAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(TbrSubDisplayWwdPicksManAbs.class.getName());

    static {
        TbrSubDisplayWwdPicksManAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected TbrSubDisplayWwdPicksManAbs(MouseListener mlrEffectsBorder, ActionListener alrParentComponent) throws Exception {
        super(GfrFactoryIconAbs.INT_SIZE_MEDIUM, 3, AbstractButton.RIGHT, mlrEffectsBorder, alrParentComponent, JToolBar.HORIZONTAL);
        ((BgpKindView) super._bgpKindView).setVisibleButtons(false);
    }

    @Override
    public void setEnabledButtons(boolean bln) {
        super.setEnabledButtons(bln);
        ((BgpKindView) super._bgpKindView).setVisibleButtons(false);
    }
}
