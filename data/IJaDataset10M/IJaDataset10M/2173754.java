package de.shunny.mp3.gui;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import de.shunny.mp3.MP3TableModel;
import java.awt.BorderLayout;

/**
 * @author Frank Senn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SuchListenPanel extends JPanel {

    private JTable jTable = null;

    private JScrollPane jScrollPane = null;

    /**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTable() {
        if (jTable == null) {
            jTable = new JTable();
            jTable.setBackground(java.awt.Color.yellow);
            jTable.setModel(new MP3TableModel());
        }
        return jTable;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setPreferredSize(new java.awt.Dimension(500, 500));
            jScrollPane.setViewportView(getJTable());
            jScrollPane.setBackground(java.awt.Color.orange);
        }
        return jScrollPane;
    }

    public static void main(String[] args) {
    }

    /**
	 * This is the default constructor
	 */
    public SuchListenPanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.setBackground(java.awt.Color.orange);
        this.setSize(300, 200);
        this.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
    }
}
