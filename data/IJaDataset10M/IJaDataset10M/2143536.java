package com.jgoodies.looks.plastic;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * The JGoodies Plastic L&amp;F implementation of <code>SplitPaneUI</code>.
 * Uses a special divider that paints modified one-touch buttons.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 *
 * @see com.jgoodies.looks.plastic.PlasticSplitPaneDivider
 */
public final class PlasticSplitPaneUI extends BasicSplitPaneUI {

    public static ComponentUI createUI(JComponent x) {
        return new PlasticSplitPaneUI();
    }

    /**
     * Creates and returns the modified default divider.
     */
    public BasicSplitPaneDivider createDefaultDivider() {
        return new PlasticSplitPaneDivider(this);
    }
}
