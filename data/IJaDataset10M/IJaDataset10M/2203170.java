package org.leviatan.dataharbour.gui.swingcomponents.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.leviatan.dataharbour.gui.swingcomponents.views.EditDataSourcePanel;

public class EditDataSourceFrame extends JFrame {

    private static final long serialVersionUID = 8843651180736139240L;

    private JPanel jContentPane = null;

    public EditDataSourceFrame() {
        super();
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(400, 200));
        this.pack();
        this.setContentPane(new EditDataSourcePanel());
        this.setTitle("Edit datasource");
    }
}
