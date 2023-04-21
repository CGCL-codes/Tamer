package net.sf.fractopt.ui.Carte;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class JPanelCarte extends JPanel {

    private JScrollPane jScrollPaneCarte = null;

    private JPanel jPanelCarte = null;

    /**
	 * This is the default constructor
	 */
    public JPanelCarte() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.setSize(new java.awt.Dimension(495, 377));
        this.setLayout(new BorderLayout());
        this.add(getJScrollPaneCarte(), null);
    }

    /**
	 * This method initializes jScrollPaneCarte	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPaneCarte() {
        if (jScrollPaneCarte == null) {
            jScrollPaneCarte = new JScrollPane();
            jScrollPaneCarte.setViewportView(getJPanelCarte());
        }
        return jScrollPaneCarte;
    }

    /**
	 * This method initializes jPanelCarte	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelCarte() {
        if (jPanelCarte == null) {
            jPanelCarte = new JPanel();
            jPanelCarte.setBackground(java.awt.Color.white);
        }
        return jPanelCarte;
    }
}
