package org.DFish.tools.calculator.plugin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.DFish.tools.calculator.JCalculatorPanel;
import org.DFish.tools.calculator.helper.*;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.DockableWindowManager;

/**
 *
 * @author yuyiwei
 */
public class JCalculatorPluginPanel extends JPanel {

    private View view;

    private boolean floating;

    private JCalculatorPanel cp;

    public JCalculatorPluginPanel(View view, String position) {
        super(new BorderLayout());
        this.view = view;
        floating = position.equals(DockableWindowManager.FLOATING);
        cp = new JCalculatorPanel();
        cp.setVisible(true);
        add(BorderLayout.CENTER, cp);
        setVisible(true);
        cp.focuseOnDefaultComponent();
        if (floating) {
            this.setPreferredSize(new Dimension(440, 100));
        }
    }

    public JCalculatorPanel getPanel() {
        return cp;
    }
}
