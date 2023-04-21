package coffeeviewer.gui.about;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import coffeeviewer.source.main.Main;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;

public class InfoPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField InfoTextField = null;

    /**
	 * This is the default constructor
	 */
    public InfoPanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(300, 200);
        this.setLayout(new BorderLayout());
        this.add(getInfoTextField(), BorderLayout.CENTER);
    }

    /**
	 * This method initializes InfoTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getInfoTextField() {
        if (InfoTextField == null) {
            InfoTextField = new JTextField();
            InfoTextField.setEditable(false);
            InfoTextField.setText(Main.getInstance().giveSystemInfo());
        }
        return InfoTextField;
    }
}
