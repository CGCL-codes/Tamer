package interfazgrafikoak;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GoikoZatia extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel jLabel = null;

    private JTextField jTextField = null;

    /**
	 * This is the default constructor
	 */
    public GoikoZatia() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        jLabel = new JLabel();
        jLabel.setText("Sartu zure izena:");
        this.setSize(300, 200);
        this.setLayout(new BorderLayout());
        this.add(jLabel, BorderLayout.WEST);
        this.add(getJTextField(), BorderLayout.CENTER);
    }

    /**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.setColumns(30);
        }
        return jTextField;
    }
}
