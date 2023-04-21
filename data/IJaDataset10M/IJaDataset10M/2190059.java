package test.jaco.swing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class AutoCompleteJComboBoxer extends PlainDocument {

    private final JComboBox comboBox;

    private ComboBoxModel model;

    private JTextComponent editor;

    public AutoCompleteJComboBoxer(JComboBox comboBox) {
        this.comboBox = comboBox;
        comboBox.setEditable(true);
        model = comboBox.getModel();
        editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        editor.setDocument(this);
        editor.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    highlightCompletedText(0);
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    AutoCompleteJComboBoxer.this.comboBox.setSelectedIndex(0);
                    AutoCompleteJComboBoxer.this.editor.setText(AutoCompleteJComboBoxer.this.comboBox.getSelectedItem().toString());
                    highlightCompletedText(0);
                }
            }
        });
        Object selected = comboBox.getSelectedItem();
        if (selected != null) editor.setText(selected.toString());
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offs);
        String afterOffset = currentText.substring(offs, currentText.length());
        String futureText = beforeOffset + str + afterOffset;
        Object item = lookupItem(futureText);
        if (item != null) {
            comboBox.setSelectedItem(item);
        } else {
            item = comboBox.getSelectedItem();
            offs = offs - str.length();
            comboBox.getToolkit().beep();
        }
        super.remove(0, getLength());
        super.insertString(0, item.toString(), a);
        if (item.toString().equals(str) && offs == 0) {
            highlightCompletedText(0);
        } else {
            highlightCompletedText(offs + str.length());
            comboBox.setPopupVisible(true);
        }
    }

    private void highlightCompletedText(int start) {
        editor.setCaretPosition(getLength());
        editor.moveCaretPosition(start);
    }

    private Object lookupItem(String pattern) {
        Object selectedItem = model.getSelectedItem();
        if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
            return selectedItem;
        } else {
            for (int i = 0, n = model.getSize(); i < n; i++) {
                Object currentItem = model.getElementAt(i);
                if (startsWithIgnoreCase(currentItem.toString(), pattern)) {
                    return currentItem;
                }
            }
        }
        return null;
    }

    private boolean startsWithIgnoreCase(String str1, String str2) {
        return str1.toUpperCase().startsWith(str2.toUpperCase());
    }
}
