package de.exilab.pixmgr.dialog.lnf;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * Dialog for selecting a look and feel and a theme.
 * @author <a href="andreas@exilab.de">Andreas Heidt</a>
 * @version $Revision: 1.2 $ - $Date: 2004/07/30 16:30:37 $
 */
public class LookAndFeelDialog extends JDialog implements ActionListener {

    /**
     * Combo box with look and feels
     */
    private JComboBox m_comboLnf;

    /**
     * Combo bx with themes
     */
    private JComboBox m_comboTheme;

    /**
     * OK button
     */
    private JButton m_buttonOk;

    /**
     * CANCEL button
     */
    private JButton m_buttonCancel;

    /**
     * <code>true</code>, if the dialog was cancelled
     */
    private boolean m_bWasCancelled;

    /**
     * Constructor of the class <code>LookAndFeelDialog</code>
     * @param parent Reference to the parent frame
     */
    public LookAndFeelDialog(Frame parent) {
        super(parent, "Select LookAndFeel", true);
        initGUI();
        setResizable(false);
    }

    /**
     * Initializes the GUI.     
     */
    private void initGUI() {
        GridBagConstraints gbc;
        JPanel cp = new JPanel();
        cp.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 10);
        cp.add(new JLabel("Look and Feel:"), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 10, 5, 10);
        cp.add(getComboLnf(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 0, 10);
        cp.add(new JLabel("Theme:"), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 10, 10, 10);
        cp.add(getComboTheme(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 10, 5, 10);
        cp.add(new JSeparator(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(5, 10, 10, 10);
        cp.add(getButtonOk(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(5, 10, 10, 10);
        cp.add(getButtonCancel(), gbc);
        setContentPane(cp);
        pack();
    }

    /**
     * Returns the combo box with the look and feels
     * @return A <code>JComboBox</code>
     */
    public JComboBox getComboLnf() {
        if (m_comboLnf == null) {
            m_comboLnf = new JComboBox();
            m_comboLnf.setPreferredSize(new Dimension(250, m_comboLnf.getPreferredSize().height));
            m_comboLnf.addItem("Metal");
            m_comboLnf.addItem("Motif");
            m_comboLnf.addItem("Plastic3D");
            m_comboLnf.addItem("Plastic");
            m_comboLnf.addItem("PlasticXP");
        }
        return m_comboLnf;
    }

    /**
     * Returns the combo box with the themes
     * @return A <code>JComboxBox</code>
     */
    public JComboBox getComboTheme() {
        if (m_comboTheme == null) {
            m_comboTheme = new JComboBox();
            m_comboTheme.setPreferredSize(new Dimension(250, m_comboTheme.getPreferredSize().height));
            m_comboTheme.addItem("Brown Sugar");
            m_comboTheme.addItem("Dark Star");
            m_comboTheme.addItem("Desert Blue");
            m_comboTheme.addItem("Desert Bluer");
            m_comboTheme.addItem("Desert Green");
            m_comboTheme.addItem("Desert Red");
            m_comboTheme.addItem("Desert Yellow");
            m_comboTheme.addItem("Experience Blue");
            m_comboTheme.addItem("Experience Green");
            m_comboTheme.addItem("Silver");
            m_comboTheme.addItem("Sky Blue");
            m_comboTheme.addItem("Sky Bluer");
            m_comboTheme.addItem("Sky Bluer Tahoma");
            m_comboTheme.addItem("Sky Green");
            m_comboTheme.addItem("Sky Krupp");
            m_comboTheme.addItem("Sky Pink");
            m_comboTheme.addItem("Sky Red");
            m_comboTheme.addItem("Sky Yellow");
            m_comboTheme.setEnabled(false);
        }
        return m_comboTheme;
    }

    /**
     * Returns the OK button
     * @return A <code>Jbutton</code>
     */
    public JButton getButtonOk() {
        if (m_buttonOk == null) {
            m_buttonOk = new JButton("Ok");
            m_buttonOk.addActionListener(this);
        }
        return m_buttonOk;
    }

    public JButton getButtonCancel() {
        if (m_buttonCancel == null) {
            m_buttonCancel = new JButton("Cancel");
            m_buttonCancel.addActionListener(this);
        }
        return m_buttonCancel;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed
     * (java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == getButtonOk() || source == getButtonCancel()) {
            if (source == getButtonOk()) {
                m_bWasCancelled = false;
            }
            setVisible(false);
        }
    }

    /**
     * @see java.awt.Component#setVisible(boolean)
     */
    public void setVisible(boolean bVisible) {
        if (bVisible) {
            m_bWasCancelled = true;
        }
        super.setVisible(bVisible);
    }

    /**
     * Determines if this dialog was cancelled by the user
     * @return <code>true</code> if the dialog was cancelled
     */
    public boolean wasCancelled() {
        return m_bWasCancelled;
    }
}
