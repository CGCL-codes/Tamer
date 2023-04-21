package org.gaea.ui.lookandfeel;

import java.awt.Graphics;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * Divider Normal. No bump for splitpane
 * 
 * @author jsgoupil
 */
class GaeaSplitPaneDivider extends BasicSplitPaneDivider {

    /**
	 * Auto Generated
	 */
    private static final long serialVersionUID = -1377287925089197753L;

    public GaeaSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
    }

    @Override
    public void paint(Graphics g) {
    }
}
