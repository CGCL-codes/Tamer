package org.phymote.gui;

import java.awt.BorderLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.phymote.control.exceptions.Messages;

/**
 * Dialogframe to copy Semicolon-seperated data for use in any
 * Spreadsheet-program
 * 
 * @author Christoph Krichenbauer
 * 
 */
public class CopyPasteDialog extends javax.swing.JDialog implements ClipboardOwner {

    private static final long serialVersionUID = 3560131815031454182L;

    private JTextArea textArea;

    private JPanel buttonBar;

    private JButton copyButton;

    private JButton closeButton;

    private CopyPasteDialog mySelf = this;

    public CopyPasteDialog(JFrame frame, String scsl) {
        super(frame);
        initGUI(scsl);
    }

    private void initGUI(String myText) {
        textArea = new JTextArea();
        getContentPane().add(textArea, BorderLayout.CENTER);
        textArea.setText(myText);
        textArea.setEditable(false);
        buttonBar = new JPanel();
        getContentPane().add(buttonBar, BorderLayout.SOUTH);
        copyButton = new JButton();
        buttonBar.add(copyButton);
        copyButton.setText(Messages.getString("CopyPasteDialog.Copy"));
        copyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(textArea.getText()), mySelf);
            }
        });
        closeButton = new JButton();
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mySelf.dispose();
            }
        });
        buttonBar.add(closeButton);
        closeButton.setText(Messages.getString("CopyPasteDialog.Close"));
        setSize(400, 300);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
