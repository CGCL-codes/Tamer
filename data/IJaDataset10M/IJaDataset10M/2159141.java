package pl.adyga.jpop.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GameTypePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JButton localGameBtn = null;

    private JButton networkGameBtn = null;

    private JButton joinNetworkGameBtn = null;

    /**
	 * This is the default constructor
	 */
    public GameTypePanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new Insets(0, 0, 10, 0);
        gridBagConstraints1.gridy = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, 10, 0);
        gridBagConstraints.gridy = 0;
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
        this.add(getLocalGameBtn(), gridBagConstraints);
        this.add(getNetworkGameBtn(), gridBagConstraints1);
        this.add(getJoinNetworkGameBtn(), gridBagConstraints2);
    }

    /**
	 * This method initializes localGameBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getLocalGameBtn() {
        if (localGameBtn == null) {
            localGameBtn = new JButton();
            localGameBtn.setText("Local Game");
            localGameBtn.setToolTipText("Local two player game");
        }
        return localGameBtn;
    }

    /**
	 * This method initializes networkGameBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getNetworkGameBtn() {
        if (networkGameBtn == null) {
            networkGameBtn = new JButton();
            networkGameBtn.setText("Network Game");
            networkGameBtn.setToolTipText("Start a new network game");
        }
        return networkGameBtn;
    }

    /**
	 * This method initializes joinNetworkGameBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJoinNetworkGameBtn() {
        if (joinNetworkGameBtn == null) {
            joinNetworkGameBtn = new JButton();
            joinNetworkGameBtn.setText("Join Network Game");
            joinNetworkGameBtn.setToolTipText("Join an existing network game");
        }
        return joinNetworkGameBtn;
    }
}
