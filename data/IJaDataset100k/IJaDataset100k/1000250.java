package org.goniolab.module.cogo;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.goniolab.module.GonioLabPanel;

/**
 *
 * @author Patrik Karlsson
 */
public class CogoPanel extends GonioLabPanel {

    public CogoPanel() {
        init();
    }

    public ButtonPanel getButtonPanel() {
        return buttonPanel;
    }

    public ResultStrip getResultStrip() {
        return resultStrip;
    }

    public void setActiveMod(CogoModule aCogoModule) {
        topPanel.removeAll();
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(aCogoModule.getApplicationPanel(), BorderLayout.CENTER);
        getApplicationPanel().validate();
    }

    private void init() {
        topPanel.setLayout(new BorderLayout());
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        getApplicationPanel().setLayout(new BorderLayout());
        getApplicationPanel().add(topPanel, BorderLayout.NORTH);
        getApplicationPanel().add(resultStrip.getScrollPane(), BorderLayout.CENTER);
    }

    private ButtonPanel buttonPanel = new ButtonPanel();

    private ResultStrip resultStrip = new ResultStrip();

    private JPanel topPanel = new JPanel();
}
