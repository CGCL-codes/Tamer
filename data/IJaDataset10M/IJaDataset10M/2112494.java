package net.rptools.maptool.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import net.rptools.maptool.client.swing.SwingUtil;

/**
 * @author trevor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MainMenuDialog extends JDialog {

    private javax.swing.JPanel jContentPane = null;

    private JButton startServerButton = null;

    private JButton connectToServerButton = null;

    private JPanel jPanel = null;

    private JButton exitButton = null;

    /**
	 * This is the default constructor
	 */
    public MainMenuDialog() {
        super(MapToolClient.getInstance(), true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        initialize();
    }

    public void setVisible(boolean b) {
        if (b) {
            SwingUtil.centerOver(this, MapToolClient.getInstance());
        }
        super.setVisible(b);
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setTitle("Main Menu");
        this.setSize(195, 156);
        this.setContentPane(getJContentPane());
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new GridBagLayout());
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.insets = new java.awt.Insets(0, 0, 10, 0);
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
            jContentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 3;
            gridBagConstraints3.weighty = 1.0D;
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 2;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            jContentPane.add(getStartServerButton(), gridBagConstraints1);
            jContentPane.add(getConnectToServerButton(), gridBagConstraints2);
            jContentPane.add(getJPanel(), gridBagConstraints3);
            jContentPane.add(getExitButton(), gridBagConstraints4);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getStartServerButton() {
        if (startServerButton == null) {
            startServerButton = new JButton();
            startServerButton.setAction(ClientActions.START_SERVER);
            startServerButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return startServerButton;
    }

    /**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getConnectToServerButton() {
        if (connectToServerButton == null) {
            connectToServerButton = new JButton();
            connectToServerButton.setAction(ClientActions.CONNECT_TO_SERVER);
            connectToServerButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return connectToServerButton;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
        }
        return jPanel;
    }

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getExitButton() {
        if (exitButton == null) {
            exitButton = new JButton();
            exitButton.setAction(ClientActions.EXIT);
        }
        return exitButton;
    }
}
