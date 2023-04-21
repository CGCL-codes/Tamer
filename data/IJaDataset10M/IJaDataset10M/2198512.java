package simulation.editor.panels;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.FlowLayout;

public class BoundsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JTextField jTextFieldXStart = null;

    private JTextField jTextFieldXEnd = null;

    private JTextField jTextFieldYStart = null;

    private JTextField jTextFieldYEnd = null;

    private JLabel jLabel = null;

    private JLabel jLabel1 = null;

    private JLabel jLabel2 = null;

    private JLabel jLabel3 = null;

    private JPanel jPanel2 = null;

    private JButton jButtonOk = null;

    private JButton jButtonCancel = null;

    /**
	 * This is the default constructor
	 */
    public BoundsPanel() {
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
        this.setSize(new Dimension(193, 123));
        this.setPreferredSize(new Dimension(193, 123));
        this.add(getJPanel1(), BorderLayout.WEST);
        this.add(getJPanel(), BorderLayout.CENTER);
        this.add(getJPanel2(), BorderLayout.SOUTH);
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridLayout gridLayout1 = new GridLayout();
            gridLayout1.setRows(4);
            jPanel = new JPanel();
            jPanel.setLayout(gridLayout1);
            jPanel.add(getJTextFieldXStart(), null);
            jPanel.add(getJTextFieldXEnd(), null);
            jPanel.add(getJTextFieldYStart(), null);
            jPanel.add(getJTextFieldYEnd(), null);
        }
        return jPanel;
    }

    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jLabel3 = new JLabel();
            jLabel3.setText("Y End : ");
            jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel2 = new JLabel();
            jLabel2.setText("Y Start : ");
            jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel1 = new JLabel();
            jLabel1.setText("X End : ");
            jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel = new JLabel();
            jLabel.setText("X Start : ");
            jLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(4);
            jPanel1 = new JPanel();
            jPanel1.setLayout(gridLayout);
            jPanel1.add(jLabel, null);
            jPanel1.add(jLabel1, null);
            jPanel1.add(jLabel2, null);
            jPanel1.add(jLabel3, null);
        }
        return jPanel1;
    }

    /**
	 * This method initializes jTextFieldXStart	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldXStart() {
        if (jTextFieldXStart == null) {
            jTextFieldXStart = new JTextField();
            jTextFieldXStart.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldXStart;
    }

    /**
	 * This method initializes jTextFieldXEnd	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldXEnd() {
        if (jTextFieldXEnd == null) {
            jTextFieldXEnd = new JTextField();
            jTextFieldXEnd.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldXEnd;
    }

    /**
	 * This method initializes jTextFieldYStart	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldYStart() {
        if (jTextFieldYStart == null) {
            jTextFieldYStart = new JTextField();
            jTextFieldYStart.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldYStart;
    }

    /**
	 * This method initializes jTextFieldYEnd	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldYEnd() {
        if (jTextFieldYEnd == null) {
            jTextFieldYEnd = new JTextField();
            jTextFieldYEnd.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldYEnd;
    }

    /**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
            jPanel2.setLayout(new FlowLayout());
            jPanel2.add(getJButtonOk(), null);
            jPanel2.add(getJButtonCancel(), null);
        }
        return jPanel2;
    }

    /**
	 * This method initializes jButtonOk	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getJButtonOk() {
        if (jButtonOk == null) {
            jButtonOk = new JButton();
            jButtonOk.setText("    SET    ");
        }
        return jButtonOk;
    }

    /**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getJButtonCancel() {
        if (jButtonCancel == null) {
            jButtonCancel = new JButton();
            jButtonCancel.setText("REVERT");
        }
        return jButtonCancel;
    }
}
