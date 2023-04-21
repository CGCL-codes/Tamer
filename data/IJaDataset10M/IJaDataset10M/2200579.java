package planning.editor.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import planning.model.PreDefinedEntities.ModelEntityId;

public class RobotPropPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel jPanel = null;

    private JLabel jLabelStatus = null;

    private JPanel jPanel1 = null;

    private JLabel jLabel = null;

    private JPanel jPanel2 = null;

    private JTextField jTextFieldName = null;

    private JPanel jPanel3 = null;

    private JButton jButtonOk = null;

    private JButton jButtonCancel = null;

    private JLabel jLabel4 = null;

    private JLabel jLabel5 = null;

    private JTextField jTextFieldScale = null;

    private JComboBox jComboBoxType = null;

    private JCheckBox jCheckBoxCommunicating = null;

    private JLabel jLabel1 = null;

    /**
	 * This is the default constructor
	 */
    public RobotPropPanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(240, 125);
        this.setPreferredSize(new Dimension(240, 125));
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
            jLabel1 = new JLabel();
            jLabel1.setText("Communication : ");
            jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel5 = new JLabel();
            jLabel5.setText("Scale : ");
            jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel4 = new JLabel();
            jLabel4.setText("Type : ");
            jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel = new JLabel();
            jLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            jLabel.setText("Name : ");
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(4);
            jPanel1 = new JPanel();
            jPanel1.setLayout(gridLayout);
            jPanel1.add(jLabel, null);
            jPanel1.add(jLabel4, null);
            jPanel1.add(jLabel5, null);
            jPanel1.add(jLabel1, null);
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
            gridLayout1.setRows(4);
            jPanel2 = new JPanel();
            jPanel2.setLayout(gridLayout1);
            jPanel2.add(getJTextFieldName(), null);
            jPanel2.add(getJComboBoxType(), null);
            jPanel2.add(getJTextFieldScale(), null);
            jPanel2.add(getJCheckBoxCommunicating(), null);
        }
        return jPanel2;
    }

    /**
	 * This method initializes jTextFieldName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldName() {
        if (jTextFieldName == null) {
            jTextFieldName = new JTextField();
        }
        return jTextFieldName;
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
	 * This method initializes jTextFieldScale	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldScale() {
        if (jTextFieldScale == null) {
            jTextFieldScale = new JTextField();
        }
        return jTextFieldScale;
    }

    /**
	 * This method initializes jComboBoxType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    public JComboBox getJComboBoxType() {
        if (jComboBoxType == null) {
            jComboBoxType = new JComboBox(ModelEntityId.values());
            jComboBoxType.setSelectedItem(ModelEntityId.NOENTITY);
        }
        return jComboBoxType;
    }

    /**
	 * This method initializes jCheckBoxCommunicating	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    public JCheckBox getJCheckBoxCommunicating() {
        if (jCheckBoxCommunicating == null) {
            jCheckBoxCommunicating = new JCheckBox();
            jCheckBoxCommunicating.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return jCheckBoxCommunicating;
    }
}
