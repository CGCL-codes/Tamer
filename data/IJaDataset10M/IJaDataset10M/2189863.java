package net.sourceforge.rconx.view.panel;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import net.sourceforge.rconx.view.component.menubar.RconXMenuBar;

/**
 * @author RconX Project
 */
public class RconXPanel extends JPanel {

    private static final RconXMenuBar menuBar = new RconXMenuBar();

    private static final MainPanel mainPanel = new MainPanel();

    private static final StatusBar statusBar = new StatusBar();

    /**
	 * 
	 */
    public RconXPanel() {
        this.setLayout(new BorderLayout());
        this.add(menuBar, BorderLayout.PAGE_START);
        this.add(mainPanel, BorderLayout.LINE_START);
        this.add(statusBar, BorderLayout.PAGE_END);
    }
}
