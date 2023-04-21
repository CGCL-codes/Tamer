package com.dgtalize.netc.visual.smiles;

import com.dgtalize.netc.system.ErrorNotifier;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

/**
 *
 * @author  DGtalize
 */
public class SmilesWindowUI extends javax.swing.JFrame {

    private final int SMILES_PER_PANEL = 60;

    private MouseListener labelMouseListener = null;

    private JTextPane editableTextPane = null;

    /** Creates new form SmilesWindowUI */
    public SmilesWindowUI(JTextPane textPane) {
        initComponents();
        setSize(400, 250);
        editableTextPane = textPane;
        labelMouseListener = new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                String smileCode = ((JLabel) e.getSource()).getToolTipText();
                StyledDocument styDoc = editableTextPane.getStyledDocument();
                try {
                    styDoc.insertString(styDoc.getLength(), smileCode, styDoc.getLogicalStyle(styDoc.getLength()));
                } catch (Exception ex) {
                    ErrorNotifier.getInstance().addException(ex);
                    JOptionPane.showMessageDialog((Component) e.getSource(), ex.toString(), "Error", 0);
                }
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        };
    }

    public void loadSmiles() {
        try {
            ArrayList<Smile> smiles = SmilesManager.getInstance().getAllSmiles();
            int smileCount = 0;
            JPanel currentPanel = null;
            for (Smile smile : smiles) {
                smileCount++;
                if ((smileCount % SMILES_PER_PANEL) == 1) {
                    currentPanel = addSmilesPanel();
                }
                ImageIcon smileIcon = new ImageIcon(smile.getPath());
                JLabel smileLabel = new JLabel("", smileIcon, JLabel.LEFT);
                smileLabel.setBorder(null);
                smileLabel.setToolTipText(smile.getCode());
                smileLabel.addMouseListener(labelMouseListener);
                smileLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                currentPanel.add(smileLabel);
            }
        } catch (Exception ex) {
            ErrorNotifier.getInstance().addException(ex);
            JOptionPane.showMessageDialog(this, ex.toString(), "Error", 0);
        }
    }

    private JPanel addSmilesPanel() {
        JPanel newPanel = new JPanel();
        smilesTabbedPane.add(newPanel);
        smilesTabbedPane.setTitleAt(smilesTabbedPane.getTabCount() - 1, String.valueOf(smilesTabbedPane.getTabCount() - 1));
        return newPanel;
    }

    private void initComponents() {
        smilesTabbedPane = new javax.swing.JTabbedPane();
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/dgtalize/netc/visual/TextsUI");
        setTitle(bundle.getString("smiles"));
        setAlwaysOnTop(true);
        getContentPane().setLayout(new java.awt.CardLayout());
        getContentPane().add(smilesTabbedPane, "card3");
        pack();
    }

    private javax.swing.JTabbedPane smilesTabbedPane;
}
