package com.netexplode.jtunes.client.ui.component;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <code>LcdPanel</code> represents .....
 * 
 * @author ykim
 * @version $Revision: 1.6 $
 * @since 0.1
 */
public class LcdPanel extends JPanel {

    private static final JLabel info = new JLabel();

    public LcdPanel() {
        super();
        initialize();
    }

    private void initialize() {
        final JPanel framed = new JPanel();
        framed.add(Box.createRigidArea(new Dimension(10, 10)));
        info.setAlignmentY(CENTER_ALIGNMENT);
        framed.add(info);
        framed.add(Box.createRigidArea(new Dimension(10, 10)));
        framed.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""), BorderFactory.createEmptyBorder(15, 5, 15, 5)));
        framed.setPreferredSize(new Dimension(300, 45));
        super.add(Box.createRigidArea(new Dimension(10, 10)));
        super.add(framed);
        super.add(Box.createRigidArea(new Dimension(10, 10)));
    }

    public void setLcdPanelText(String text) {
        info.setText(text);
    }
}
