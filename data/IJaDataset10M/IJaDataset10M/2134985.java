package freeguide.plugins.reminder.alarm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ResourceBundle;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class AlarmUIPanel extends JPanel {

    private JCheckBox cbRemind = null;

    private JLabel jLabel = null;

    private JTextField textWarning = null;

    private JLabel jLabel1 = null;

    private JTextField textGiveup = null;

    private final ResourceBundle i18n;

    /**
     * This is the default constructor
     *
     * @param localizer DOCUMENT ME!
     */
    public AlarmUIPanel(final ResourceBundle i18n) {
        super();
        this.i18n = i18n;
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
        gridBagConstraints41.gridx = 2;
        gridBagConstraints41.gridy = 1;
        gridBagConstraints41.insets = new java.awt.Insets(5, 5, 0, 5);
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.gridx = 0;
        gridBagConstraints21.gridy = 1;
        gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints21.insets = new java.awt.Insets(5, 5, 0, 0);
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.gridy = 4;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 4;
        gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 0);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jLabel1 = new JLabel();
        jLabel1.setText("Give up after");
        jLabel1.setText(i18n.getString("options.give_up_after_secs"));
        jLabel1.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_G);
        jLabel1.setLabelFor(getTextGiveup());
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.insets = new java.awt.Insets(5, 5, 0, 5);
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jLabel = new JLabel();
        jLabel.setText("Seconds warning");
        jLabel.setText(i18n.getString("options.seconds_warning"));
        jLabel.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_W);
        jLabel.setLabelFor(getTextWarning());
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new java.awt.Insets(5, 5, 0, 5);
        this.setLayout(new GridBagLayout());
        this.setSize(300, 200);
        this.add(getCbRemind(), gridBagConstraints1);
        this.add(jLabel, gridBagConstraints2);
        this.add(getTextWarning(), gridBagConstraints3);
        this.add(jLabel1, gridBagConstraints4);
        this.add(getTextGiveup(), gridBagConstraints5);
    }

    /**
     * This method initializes jCheckBox
     *
     * @return javax.swing.JCheckBox
     */
    public JCheckBox getCbRemind() {
        if (cbRemind == null) {
            cbRemind = new JCheckBox();
            cbRemind.setText("Remind me of progs");
            cbRemind.setText(i18n.getString("options.remind_me_of_progs"));
            cbRemind.setMnemonic(java.awt.event.KeyEvent.VK_R);
        }
        return cbRemind;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    public JTextField getTextWarning() {
        if (textWarning == null) {
            textWarning = new JTextField();
        }
        return textWarning;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    public JTextField getTextGiveup() {
        if (textGiveup == null) {
            textGiveup = new JTextField();
        }
        return textGiveup;
    }
}
