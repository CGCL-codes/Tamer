package ch.integis.ili2sql.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * This Dialog will show reported errors of the INTERLIS-Compiler
 * @author Roger T&ouml;nz
 * @version 0.1
 * @since 21.10.2002
 */
public class ErrorDialog extends JDialog {

    private JScrollPane jScrollPanel = new JScrollPane();

    private JPanel jPanelSouth = new JPanel();

    private JTextArea jTextAreaMsg = new JTextArea();

    private JButton jButtonOk = new JButton("OK");

    /** Creates new form JDialog
     * @param parent
     * @param modal
     */
    public ErrorDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initGUI();
        pack();
    }

    /** This method is called from within the constructor to initialize the form. */
    private void initGUI() {
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog();
            }
        });
        getContentPane().add(jPanelSouth, java.awt.BorderLayout.SOUTH);
        getContentPane().add(jScrollPanel, java.awt.BorderLayout.CENTER);
        jScrollPanel.getViewport().add(jTextAreaMsg);
        jTextAreaMsg.setSize(300, 200);
        jPanelSouth.add(jButtonOk);
        jButtonOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                closeDialog();
            }
        });
    }

    /** Closes the dialog */
    private void closeDialog() {
        setVisible(false);
        dispose();
    }

    /**
     * @return JTextArea
     */
    public JTextArea getTextArea() {
        return jTextAreaMsg;
    }
}
