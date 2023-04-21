package org.japura.examples.controller.modal;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class OtherFrame extends JFrame {

    public static int count = 1;

    private JLabel sameModalGroupNameLabel;

    private JLabel childControllerLabel;

    public OtherFrame() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle(Integer.toString(count++));
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("wrap 1"));
        panel.add(getChildControllerLabel());
        panel.add(getSameModalGroupNameLabel());
        add(panel);
    }

    protected JLabel getSameModalGroupNameLabel() {
        if (sameModalGroupNameLabel == null) {
            sameModalGroupNameLabel = new JLabel();
        }
        return sameModalGroupNameLabel;
    }

    protected JLabel getChildControllerLabel() {
        if (childControllerLabel == null) {
            childControllerLabel = new JLabel();
        }
        return childControllerLabel;
    }
}
