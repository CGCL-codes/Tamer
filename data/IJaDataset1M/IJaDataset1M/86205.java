package com.memoire.vainstall.gui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.memoire.vainstall.VADirectoryStep;
import com.memoire.vainstall.VAGlobals;

/**
 * @version      $Id: VADirectoryPanel.java,v 1.6 2005/10/11 09:51:55 deniger Exp $
 * @author       Axel von Arnim
 */
public class VADirectoryPanel extends VAPanel implements VADirectoryStep {

    JTextField tfDir_;

    JButton btBrowse_;

    public VADirectoryPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JPanel pnMain = new JPanel();
        pnMain.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(new Insets(5, 5, 5, 5))));
        pnMain.setLayout(new BoxLayout(pnMain, BoxLayout.Y_AXIS));
        JPanel pnHaut = new JPanel();
        pnHaut.setLayout(new BorderLayout());
        JLabel lbTitle = new JLabel(VAGlobals.i18n("UI_Directory"));
        lbTitle.setFont(lbTitle.getFont().deriveFont(Font.BOLD, 20));
        lbTitle.setOpaque(true);
        lbTitle.setBorder(new EmptyBorder(new Insets(5, 0, 5, 0)));
        lbTitle.setBackground(pnMain.getBackground().darker());
        lbTitle.setForeground(Color.white);
        pnHaut.add(BorderLayout.NORTH, lbTitle);
        tfDir_ = new JTextField();
        pnHaut.add(BorderLayout.SOUTH, tfDir_);
        JPanel pnBas = new JPanel();
        pnBas.setLayout(new BorderLayout());
        btBrowse_ = new JButton(VAGlobals.i18n("UI_Browse"));
        btBrowse_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                VADirectoryPanel.this.browse();
            }
        });
        pnBas.add(BorderLayout.NORTH, btBrowse_);
        pnMain.add(pnHaut);
        pnMain.add(pnBas);
        JComponent pnImage = VAImagePanel.IMAGE_PANEL;
        add(pnImage);
        add(pnMain);
    }

    public void setDirectory(File f) {
        if (f != null) tfDir_.setText(f.getAbsolutePath());
    }

    public File getDirectory() {
        String dirstr = tfDir_.getText().trim();
        if ((dirstr == null) || ("".equals(dirstr))) {
            JOptionPane.showMessageDialog(this, VAGlobals.i18n("UI_MustChoose"), VAGlobals.i18n("UI_Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        }
        File dir = new File(dirstr);
        return dir;
    }

    public void roDirectory(File d) {
        JOptionPane.showMessageDialog(this, VAGlobals.i18n("Setup_NoWritableDirectory") + d.getAbsolutePath() + "\n" + VAGlobals.i18n("Setup_NoWritableDirectoryInfos"), VAGlobals.i18n("UI_Error"), JOptionPane.ERROR_MESSAGE);
    }

    public void rejectDirectory() {
        JOptionPane.showMessageDialog(this, VAGlobals.i18n("UI_NotChooseDirectory"), VAGlobals.i18n("UI_Error"), JOptionPane.ERROR_MESSAGE);
    }

    public boolean acceptDirectory() {
        int res = JOptionPane.showConfirmDialog(this, VAGlobals.i18n("UI_InstallationDirectory") + "\n" + getDirectory() + "\n" + VAGlobals.i18n("UI_IsThatRight"), VAGlobals.i18n("UI_Confirm"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return (res == JOptionPane.YES_OPTION);
    }

    void browse() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int res = fc.showSaveDialog(this);
        File dir = null;
        if (res == JFileChooser.APPROVE_OPTION) {
            dir = fc.getSelectedFile();
            if (dir != null) {
                if (((dir.exists()) && (dir.isDirectory()) && (dir.canWrite())) || (!dir.exists())) {
                    tfDir_.setText(dir.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(this, dir.getName() + VAGlobals.i18n("UI_NotValidDirectory"), VAGlobals.i18n("UI_Error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
