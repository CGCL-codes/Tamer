package view;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * @author   Fabrizio Pastore [ fabrizio.pastore@gmail.com ]
 */
public abstract class ConfigPanel extends JPanel {

    /**
	 * @uml.property   name="config"
	 */
    private Object config;

    public ConfigPanel(LayoutManager layout) {
        super(layout);
    }

    public ConfigPanel() {
        super();
    }

    /**
	 * @return     Returns the config.
	 * @uml.property   name="config"
	 */
    public abstract Object getConfig();
}
