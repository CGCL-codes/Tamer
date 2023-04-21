package org.mitre.rt.client.ui;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 *
 * @author BWORRELL
 */
public class RTSplitPaneDivider extends BasicSplitPaneDivider {

    public RTSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
    }

    public JButton getLeftButton() {
        return super.leftButton;
    }

    public JButton getRightButton() {
        return super.rightButton;
    }
}
