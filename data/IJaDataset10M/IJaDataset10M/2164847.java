package planning.editor.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RRTConnectRePlanAlgorithmPropPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel jPanel = null;

    private JLabel jLabelStatus = null;

    private JPanel jPanel1 = null;

    private JPanel jPanel2 = null;

    private JPanel jPanel3 = null;

    private JButton jButtonOk = null;

    private JButton jButtonCancel = null;

    private JLabel jLabel1 = null;

    private JTextField jTextFieldName = null;

    private JLabel jLabel = null;

    private JLabel jLabel2 = null;

    private JTextField jTextFieldStepCount = null;

    private JTextField jTextFieldThreshold = null;

    private JLabel jLabel4 = null;

    private JTextField jTextFieldEnlargement = null;

    private JLabel jLabel3 = null;

    private JLabel jLabel5 = null;

    private JLabel jLabel6 = null;

    private JLabel jLabel7 = null;

    private JLabel jLabel8 = null;

    private JLabel jLabel9 = null;

    private JTextField jTextFieldCollisionSaveEnlargement = null;

    private JTextField jTextFieldEscapeTreeSize = null;

    private JTextField jTextFieldMaxEscapeTrial = null;

    private JTextField jTextFieldMaxSimpleEscapeTrial = null;

    private JLabel jLabel10 = null;

    private JTextField jTextFieldSavedCollisionEnlargement = null;

    /**
	 * This is the default constructor
	 */
    public RRTConnectRePlanAlgorithmPropPanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(290, 280);
        this.setPreferredSize(new Dimension(290, 280));
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
            jLabel10 = new JLabel();
            jLabel10.setText("Saved Collision Enlargement : ");
            jLabel8 = new JLabel();
            jLabel8.setText("Max Simple Escape Trial : ");
            jLabel8.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel7 = new JLabel();
            jLabel7.setText("Max Escape Trial : ");
            jLabel7.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel6 = new JLabel();
            jLabel6.setText("Escape Tree Size : ");
            jLabel6.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel5 = new JLabel();
            jLabel5.setText("Collision Save Enlargement : ");
            jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel3 = new JLabel();
            jLabel3.setText("-----");
            jLabel3.setHorizontalTextPosition(SwingConstants.CENTER);
            jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
            jLabel4 = new JLabel();
            jLabel4.setText("Collision Enlargement : ");
            jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel2 = new JLabel();
            jLabel2.setText("Threshold : ");
            jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel = new JLabel();
            jLabel.setText("Step Count : ");
            jLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel1 = new JLabel();
            jLabel1.setText("Step Size : ");
            jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(10);
            jPanel1 = new JPanel();
            jPanel1.setLayout(gridLayout);
            jPanel1.add(jLabel1, null);
            jPanel1.add(jLabel, null);
            jPanel1.add(jLabel2, null);
            jPanel1.add(jLabel4, null);
            jPanel1.add(jLabel3, null);
            jPanel1.add(jLabel5, null);
            jPanel1.add(jLabel10, null);
            jPanel1.add(jLabel6, null);
            jPanel1.add(jLabel7, null);
            jPanel1.add(jLabel8, null);
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
            jLabel9 = new JLabel();
            jLabel9.setText("-----");
            jLabel9.setHorizontalTextPosition(SwingConstants.CENTER);
            jLabel9.setHorizontalAlignment(SwingConstants.CENTER);
            GridLayout gridLayout1 = new GridLayout();
            gridLayout1.setRows(10);
            jPanel2 = new JPanel();
            jPanel2.setLayout(gridLayout1);
            jPanel2.add(getJTextFieldStepSize(), null);
            jPanel2.add(getJTextFieldStepCount(), null);
            jPanel2.add(getJTextFieldThreshold(), null);
            jPanel2.add(getJTextFieldEnlargement(), null);
            jPanel2.add(jLabel9, null);
            jPanel2.add(getJTextFieldCollisionSaveEnlargement(), null);
            jPanel2.add(getJTextFieldSavedCollisionEnlargement(), null);
            jPanel2.add(getJTextFieldEscapeTreeSize(), null);
            jPanel2.add(getJTextFieldMaxEscapeTrial(), null);
            jPanel2.add(getJTextFieldMaxSimpleEscapeTrial(), null);
        }
        return jPanel2;
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
	 * This method initializes jTextFieldName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldStepSize() {
        if (jTextFieldName == null) {
            jTextFieldName = new JTextField();
            jTextFieldName.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldName;
    }

    /**
	 * This method initializes jTextFieldStepCount	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldStepCount() {
        if (jTextFieldStepCount == null) {
            jTextFieldStepCount = new JTextField();
            jTextFieldStepCount.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldStepCount;
    }

    /**
	 * This method initializes jTextFieldThreshold	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldThreshold() {
        if (jTextFieldThreshold == null) {
            jTextFieldThreshold = new JTextField();
            jTextFieldThreshold.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldThreshold;
    }

    /**
	 * This method initializes jTextFieldEnlargement	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldEnlargement() {
        if (jTextFieldEnlargement == null) {
            jTextFieldEnlargement = new JTextField();
            jTextFieldEnlargement.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldEnlargement;
    }

    /**
	 * This method initializes jTextFieldCollisionSaveEnlargement	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldCollisionSaveEnlargement() {
        if (jTextFieldCollisionSaveEnlargement == null) {
            jTextFieldCollisionSaveEnlargement = new JTextField();
            jTextFieldCollisionSaveEnlargement.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldCollisionSaveEnlargement;
    }

    /**
	 * This method initializes jTextFieldEscapeTreeSize	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldEscapeTreeSize() {
        if (jTextFieldEscapeTreeSize == null) {
            jTextFieldEscapeTreeSize = new JTextField();
            jTextFieldEscapeTreeSize.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldEscapeTreeSize;
    }

    /**
	 * This method initializes jTextFieldMaxEscapeTrial	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldMaxEscapeTrial() {
        if (jTextFieldMaxEscapeTrial == null) {
            jTextFieldMaxEscapeTrial = new JTextField();
            jTextFieldMaxEscapeTrial.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldMaxEscapeTrial;
    }

    /**
	 * This method initializes jTextFieldMaxSimpleEscapeTrial	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldMaxSimpleEscapeTrial() {
        if (jTextFieldMaxSimpleEscapeTrial == null) {
            jTextFieldMaxSimpleEscapeTrial = new JTextField();
            jTextFieldMaxSimpleEscapeTrial.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldMaxSimpleEscapeTrial;
    }

    /**
	 * This method initializes jTextFieldSavedCollisionEnlargement	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldSavedCollisionEnlargement() {
        if (jTextFieldSavedCollisionEnlargement == null) {
            jTextFieldSavedCollisionEnlargement = new JTextField();
            jTextFieldSavedCollisionEnlargement.setHorizontalAlignment(JTextField.TRAILING);
        }
        return jTextFieldSavedCollisionEnlargement;
    }
}
