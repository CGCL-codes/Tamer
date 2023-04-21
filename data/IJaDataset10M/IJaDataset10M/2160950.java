package com.g2d.util;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

@SuppressWarnings("serial")
public class TextEditor extends JPanel {

    JTextPane text_pane = new JTextPane();

    public TextEditor() {
        super(new BorderLayout());
        super.add(new JScrollPane(text_pane), BorderLayout.CENTER);
    }

    public JTextPane getTextPane() {
        return text_pane;
    }

    public void setText(String text) {
        text_pane.setText(text);
    }

    public void insertText(String text, int index) {
        try {
            text_pane.getStyledDocument().insertString(0, text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public String getText() {
        return text_pane.getText();
    }
}
