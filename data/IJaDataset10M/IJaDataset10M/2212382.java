package com.memoire.vainstall.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import com.memoire.vainstall.VAUpgradeStep;
import com.memoire.vainstall.VAGlobals;

/**
 * @version      $Id: VAUpgradePanel.java,v 1.5 2004/02/02 20:57:59 deniger Exp $
 * @author       Axel von Arnim
 */
public class VAUpgradePanel extends VAPanel implements VAUpgradeStep {

    JLabel lbVersion_, lbStatus_;

    JTextField tfDirectory_;

    JRadioButton rdYes_, rdNo_;

    JPanel pnChoice_;

    public VAUpgradePanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JPanel pnMain = new JPanel();
        pnMain.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(new Insets(5, 5, 5, 5))));
        pnMain.setLayout(new BoxLayout(pnMain, BoxLayout.Y_AXIS));
        JPanel pnHaut = new JPanel();
        pnHaut.setLayout(new BorderLayout());
        JLabel lbTitle = new JLabel(VAGlobals.i18n("UI_Upgrade"));
        lbTitle.setFont(lbTitle.getFont().deriveFont(Font.BOLD, 20));
        lbTitle.setOpaque(true);
        lbTitle.setBorder(new EmptyBorder(new Insets(5, 0, 5, 0)));
        lbTitle.setBackground(pnMain.getBackground().darker());
        lbTitle.setForeground(Color.white);
        pnHaut.add(BorderLayout.NORTH, lbTitle);
        JPanel pnDetails = new JPanel();
        pnDetails.setLayout(new GridLayout(4, 1));
        lbVersion_ = new JLabel();
        lbVersion_.setForeground(pnDetails.getBackground());
        Font f = lbVersion_.getFont().deriveFont(Font.BOLD);
        lbVersion_.setFont(f);
        lbVersion_.setText(VAGlobals.i18n("UI_Invisible"));
        pnDetails.add(new JLabel(VAGlobals.i18n("UI_PreviousVersionFound")));
        pnDetails.add(lbVersion_);
        tfDirectory_ = new JTextField();
        tfDirectory_.setEditable(false);
        tfDirectory_.setText("");
        pnDetails.add(new JLabel(VAGlobals.i18n("UI_InstallationDirectory")));
        pnDetails.add(tfDirectory_);
        pnHaut.add(BorderLayout.SOUTH, pnDetails);
        JPanel pnBas = new JPanel();
        pnBas.setLayout(new BorderLayout());
        pnChoice_ = new JPanel();
        pnChoice_.setBorder(new EmptyBorder(new Insets(20, 0, 0, 0)));
        pnChoice_.setLayout(new BorderLayout());
        pnChoice_.add(BorderLayout.CENTER, new JLabel(VAGlobals.i18n("UI_WantToUpgrade")));
        JPanel pnRadios = new JPanel();
        rdYes_ = new JRadioButton(VAGlobals.i18n("Common_Yes"));
        rdYes_.setEnabled(true);
        rdYes_.setSelected(false);
        rdNo_ = new JRadioButton(VAGlobals.i18n("Common_No"));
        rdNo_.setEnabled(true);
        rdNo_.setSelected(true);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdYes_);
        bg.add(rdNo_);
        pnRadios.add(rdYes_);
        pnRadios.add(rdNo_);
        pnChoice_.add(BorderLayout.SOUTH, pnRadios);
        setChoiceEnabled(false);
        pnBas.add(BorderLayout.NORTH, pnChoice_);
        lbStatus_ = new JLabel();
        lbStatus_.setOpaque(true);
        lbStatus_.setText(VAGlobals.i18n("UI_ClickNextToContinue"));
        lbStatus_.setBackground(pnMain.getBackground().brighter());
        pnBas.add(BorderLayout.SOUTH, lbStatus_);
        pnMain.add(pnHaut);
        pnMain.add(pnBas);
        JComponent pnImage = VAImagePanel.IMAGE_PANEL;
        add(pnImage);
        add(pnMain);
    }

    public void version(String msg) {
        lbVersion_.setForeground(Color.red);
        lbVersion_.setText(msg);
    }

    public void setChoiceEnabled(boolean b) {
        pnChoice_.setVisible(b);
    }

    public void status(String msg) {
        lbStatus_.setText(msg);
    }

    public void directory(String msg) {
        tfDirectory_.setText(msg);
    }

    public boolean isConfirmUpgrade() {
        return rdYes_.isSelected();
    }
}
