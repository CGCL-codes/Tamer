package org.wikipediacleaner.gui.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

/**
 * An action listener for marking a link to a disambiguation page as normal.
 */
public class MarkLinkAction implements ActionListener {

    private final String oldTitle;

    private final String text;

    private final String template;

    private final Element element;

    private final JTextPane textPane;

    private final JCheckBox checkBox;

    public MarkLinkAction(String oldTitle, String text, String template, Element element, JTextPane textPane, JCheckBox checkBox) {
        this.oldTitle = oldTitle;
        this.text = text;
        this.template = template;
        this.element = element;
        this.textPane = textPane;
        this.checkBox = checkBox;
    }

    public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
        if ((element != null) && (textPane != null) && (oldTitle != null) && (oldTitle.length() > 0)) {
            int startOffset = element.getStartOffset();
            int endOffset = element.getEndOffset();
            String newText = "{{" + template + "|" + oldTitle;
            if ((text != null) && (text.length() > 0) && (!text.equals(oldTitle))) {
                newText += "|" + text;
            }
            newText += "}}";
            try {
                textPane.getDocument().remove(startOffset, endOffset - startOffset);
                textPane.getDocument().insertString(startOffset, newText, element.getAttributes());
            } catch (BadLocationException e1) {
            }
            if ((checkBox != null) && (checkBox.isEnabled())) {
                checkBox.setSelected(true);
            }
        }
    }
}
