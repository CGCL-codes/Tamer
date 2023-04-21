package com.tabuto.test.j2dgf;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import com.tabuto.j2dgf.gui.J2DControlPanel;

public class MyControlLeftPanel extends J2DControlPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4975971744365752259L;

    public MyControlLeftPanel(Dimension d) {
        super(d);
        this.setLayout(new FlowLayout());
        addContent();
    }

    protected void addContent() {
        JButton Test = new JButton("Test");
        this.add(Test);
        JButton Left = new JButton("Left");
        this.add(Left);
    }
}
