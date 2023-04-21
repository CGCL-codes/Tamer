package com.panomedic.gui;

import com.panomedic.Constants;
import com.panomedic.utils.Utils;
import com.panomedic.colors.ColorConstants;
import com.panomedic.kernel.VisualHandler;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 *
 * @author Yare
 */
public class OptionsDialog extends JDialog implements ActionListener {

    GridBagConstraints constraints = new GridBagConstraints();

    JPanel contentPane = new JPanel(new GridBagLayout());

    JComboBox methodCombo;

    JLabel lutAccLabel;

    JSpinner lutAccSpin;

    JSpinner blurRadSpin;

    JComboBox csCombo;

    JCheckBox[] compCheck;

    JCheckBox saveEnd;

    JCheckBox saveStep;

    JButton saveButton;

    JButton cancelButton;

    private Preferences prefs;

    public OptionsDialog(Frame owner, String title) {
        super(owner, title, true);
        prefs = Preferences.userRoot().node(Constants.defNodePath);
        String[] methodStrings = { "Histogram Statistics", "Level Mapping" };
        JLabel methodLabel = new JLabel("Used method");
        methodCombo = new JComboBox(methodStrings);
        methodCombo.setSelectedIndex(prefs.getInt(Constants.METHOD_CHOICE, Constants.METHOD_LUT));
        methodCombo.addActionListener(this);
        SpinnerModel blurRadModel = new SpinnerNumberModel(prefs.getInt(Constants.BLUR_RADIUS, 10), 0, 200, 1);
        blurRadSpin = new JSpinner(blurRadModel);
        constraints.weightx = 0;
        JLabel saveEndLabel = new JLabel("Save result automatically after processing");
        constraints.weightx = 0;
        saveEnd = new JCheckBox();
        saveEnd.setSelected(prefs.getInt(Constants.SAVE_END, 0) > 0 ? true : false);
        constraints.weightx = 0;
        JLabel saveStepLabel = new JLabel("Save result after every step");
        constraints.weightx = 0;
        saveStep = new JCheckBox();
        saveStep.setSelected(prefs.getInt(Constants.SAVE_END, 0) > 1 ? true : false);
        constraints.gridwidth = 1;
        contentPane.setOpaque(true);
        this.setContentPane(contentPane);
        setResizable(true);
        this.setLocation(300, 300);
        this.setSize(new Dimension(500, 500));
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
    }

    private void addGB(Container cont, JComponent comp, int x, int y) {
        if ((cont.getLayout() instanceof GridBagLayout) == false) {
            cont.setLayout(new GridBagLayout());
        }
        constraints.gridx = x;
        constraints.gridy = y;
        contentPane.add(comp, constraints);
    }

    private void setLutAcc(boolean value) {
        lutAccSpin.setEnabled(value);
        lutAccLabel.setEnabled(value);
    }

    private void setCompLabels(int csType) {
        for (int i = 0; i < ColorConstants.NUM_COMP; i++) {
            compCheck[i].setText(ColorConstants.getCompName(csType, i));
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == methodCombo) {
        } else if (e.getSource() == csCombo) {
            setCompLabels(csCombo.getSelectedIndex());
        } else if (e.getSource() == saveButton) {
            prefs.putInt(Constants.CS_TYPE, csCombo.getSelectedIndex());
            prefs.putInt(Constants.COMP_PROC, Utils.getCompProc(new boolean[] { compCheck[0].isSelected(), compCheck[1].isSelected(), compCheck[2].isSelected() }));
            prefs.putInt(Constants.METHOD_CHOICE, methodCombo.getSelectedIndex());
            prefs.putInt(Constants.LUT_ACCURACY, (Integer) lutAccSpin.getValue());
            prefs.putInt(Constants.BLUR_RADIUS, (Integer) blurRadSpin.getValue());
            int save = 0;
            if (saveEnd.isSelected()) {
                save++;
                if (saveStep.isSelected()) {
                    save++;
                }
            }
            prefs.putInt(Constants.SAVE_END, save);
            try {
                prefs.flush();
            } catch (BackingStoreException ex) {
                Logger.getLogger(OptionsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (methodCombo.getSelectedIndex() == Constants.METHOD_LUT) {
                VisualHandler.getInstance().getFrame().getToolBar().setShowLutEnabled(true);
            } else {
                VisualHandler.getInstance().getFrame().getToolBar().setShowLutEnabled(false);
            }
            this.setVisible(false);
        } else if (e.getSource() == cancelButton) {
            this.setVisible(false);
        }
    }

    public static void main(String[] args) {
        final JFrame f = new JFrame("Options dialog Frame");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton closeButton = new JButton("Show");
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OptionsDialog dialog = new OptionsDialog(f, "Options dialog");
            }
        });
        f.add(closeButton);
        f.pack();
        f.setVisible(true);
    }
}
