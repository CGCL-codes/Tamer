package planning.editor.panels;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridBagConstraints;

public class CollisionWorldPropPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel jPanel = null;

    private JLabel jLabelStatus = null;

    private JPanel jPanel1 = null;

    private JLabel jLabel = null;

    private JLabel jLabel1 = null;

    private JLabel jLabel2 = null;

    private JPanel jPanel2 = null;

    private JTextField jTextFieldXStart = null;

    private JTextField jTextFieldXEnd = null;

    private JPanel jPanel3 = null;

    private JButton jButtonOk = null;

    private JButton jButtonCancel = null;

    private JLabel jLabel3 = null;

    private JLabel jLabel4 = null;

    private JLabel jLabel5 = null;

    private JTextField jTextFieldYStart = null;

    private JTextField jTextFieldYEnd = null;

    private JTextField jTextFieldOrientationStart = null;

    private JTextField jTextFieldOrientationEnd = null;

    /**
	 * This is the default constructor
	 */
    public CollisionWorldPropPanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(234, 190);
        this.setPreferredSize(new Dimension(234, 190));
        this.setLayout(new BorderLayout());
        this.add(getJPanel(), BorderLayout.NORTH);
        this.add(getJPanel1(), BorderLayout.WEST);
        this.add(getJPanel2(), BorderLayout.CENTER);
        this.add(getJPanel3(), BorderLayout.SOUTH);
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            jPanel.add(getJLabelStatus(), java.awt.BorderLayout.CENTER);
        }
        return jPanel;
    }

    public JLabel getJLabelStatus() {
        if (jLabelStatus == null) {
            jLabelStatus = new JLabel();
            jLabelStatus.setText(" ");
        }
        return jLabelStatus;
    }

    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jLabel5 = new JLabel();
            jLabel5.setText("Orientation End : ");
            jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel4 = new JLabel();
            jLabel4.setText("Orientation Start : ");
            jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel3 = new JLabel();
            jLabel3.setText("Y End : ");
            jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel2 = new JLabel();
            jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel2.setText("Y Start : ");
            jLabel1 = new JLabel();
            jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel1.setText("X End : ");
            jLabel = new JLabel();
            jLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel.setText("X Start : ");
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(6);
            jPanel1 = new JPanel();
            jPanel1.setLayout(gridLayout);
            jPanel1.add(jLabel, null);
            jPanel1.add(jLabel1, null);
            jPanel1.add(jLabel2, null);
            jPanel1.add(jLabel3, null);
            jPanel1.add(jLabel4, null);
            jPanel1.add(jLabel5, null);
        }
        return jPanel1;
    }

    /**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            GridLayout gridLayout1 = new GridLayout();
            gridLayout1.setRows(6);
            jPanel2 = new JPanel();
            jPanel2.setLayout(gridLayout1);
            jPanel2.add(getJTextFieldXStart(), null);
            jPanel2.add(getJTextFieldXEnd(), null);
            jPanel2.add(getJTextFieldYStart(), null);
            jPanel2.add(getJTextFieldYEnd(), null);
            jPanel2.add(getJTextFieldOrientationStart(), null);
            jPanel2.add(getJTextFieldOrientationEnd(), null);
        }
        return jPanel2;
    }

    /**
	 * This method initializes jTextFieldXStart	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldXStart() {
        if (jTextFieldXStart == null) {
            jTextFieldXStart = new JTextField();
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
        }
        return jTextFieldXEnd;
    }

    /**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            jPanel3 = new JPanel();
            jPanel3.setLayout(new GridBagLayout());
            jPanel3.add(getJButtonOk(), new GridBagConstraints());
            jPanel3.add(getJButtonCancel(), new GridBagConstraints());
        }
        return jPanel3;
    }

    /**
	 * This method initializes jButtonOk	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getJButtonOk() {
        if (jButtonOk == null) {
            jButtonOk = new JButton();
            jButtonOk.setText("   SET   ");
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

    /**
	 * This method initializes jTextFieldYStart	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldYStart() {
        if (jTextFieldYStart == null) {
            jTextFieldYStart = new JTextField();
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
        }
        return jTextFieldYEnd;
    }

    /**
	 * This method initializes jTextFieldOrientationStart	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldOrientationStart() {
        if (jTextFieldOrientationStart == null) {
            jTextFieldOrientationStart = new JTextField();
        }
        return jTextFieldOrientationStart;
    }

    /**
	 * This method initializes jTextFieldOrientationEnd	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldOrientationEnd() {
        if (jTextFieldOrientationEnd == null) {
            jTextFieldOrientationEnd = new JTextField();
        }
        return jTextFieldOrientationEnd;
    }
}
