package org.compiere.plaf;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/**
 *  Adempiere Scroll Pane UI
 *
 *  @author     Jorg Janke
 *  @version    $Id: AdempiereScrollPaneUI.java,v 1.2 2006/07/30 00:52:23 jjanke Exp $
 */
public class CompiereScrollPaneUI extends MetalScrollPaneUI {

    /**
	 * Create UI (not shared)
	 * @param x
	 * @return UI
	 */
    public static ComponentUI createUI(JComponent x) {
        return new CompiereScrollPaneUI();
    }

    /**
	 *  Install UI
	 *  @param c
	 */
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
        ((JScrollPane) c).getViewport().setOpaque(false);
    }
}
