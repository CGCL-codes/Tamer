package expenser;

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.text.*;

/** Klasa obs�uguj�ca uzupe�nianie do wpisywanego 
 * tekstu w JComboBox.
 */
public class AutoCompletion extends PlainDocument {

    JComboBox comboBox;

    ComboBoxModel model;

    JTextComponent editor;

    boolean selecting = false;

    boolean hidePopupOnFocusLoss;

    boolean hitBackspace = false;

    boolean hitBackspaceOnSelection;

    KeyListener editorKeyListener;

    FocusListener editorFocusListener;

    public AutoCompletion(final JComboBox comboBox) {
        this.comboBox = comboBox;
        model = comboBox.getModel();
        comboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!selecting) highlightCompletedText(0);
            }
        });
        comboBox.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("editor")) configureEditor((ComboBoxEditor) e.getNewValue());
                if (e.getPropertyName().equals("model")) model = (ComboBoxModel) e.getNewValue();
            }
        });
        editorKeyListener = new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (comboBox.isDisplayable()) comboBox.setPopupVisible(true);
                hitBackspace = false;
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_BACK_SPACE:
                        hitBackspace = true;
                        hitBackspaceOnSelection = editor.getSelectionStart() != editor.getSelectionEnd();
                        break;
                    case KeyEvent.VK_DELETE:
                        e.consume();
                        comboBox.getToolkit().beep();
                        break;
                }
            }
        };
        hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");
        editorFocusListener = new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                highlightCompletedText(0);
            }

            public void focusLost(FocusEvent e) {
                if (hidePopupOnFocusLoss) comboBox.setPopupVisible(false);
            }
        };
        configureEditor(comboBox.getEditor());
        Object selected = comboBox.getSelectedItem();
        if (selected != null) setText(selected.toString());
        highlightCompletedText(0);
    }

    public static void enable(JComboBox comboBox) {
        comboBox.setEditable(true);
        new AutoCompletion(comboBox);
    }

    void configureEditor(ComboBoxEditor newEditor) {
        if (editor != null) {
            editor.removeKeyListener(editorKeyListener);
            editor.removeFocusListener(editorFocusListener);
        }
        if (newEditor != null) {
            editor = (JTextComponent) newEditor.getEditorComponent();
            editor.addKeyListener(editorKeyListener);
            editor.addFocusListener(editorFocusListener);
            editor.setDocument(this);
        }
    }

    public void remove(int offs, int len) throws BadLocationException {
        if (selecting) return;
        if (hitBackspace) {
            if (offs > 0) {
                if (hitBackspaceOnSelection) offs--;
            } else {
                comboBox.getToolkit().beep();
            }
            highlightCompletedText(offs);
        } else {
            super.remove(offs, len);
        }
    }

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (selecting) return;
        super.insertString(offs, str, a);
        Object item = lookupItem(getText(0, getLength()));
        if (item != null) {
            setSelectedItem(item);
        } else {
            item = comboBox.getSelectedItem();
            offs = offs - str.length();
            comboBox.getToolkit().beep();
        }
        setText(item.toString());
        highlightCompletedText(offs + str.length());
    }

    private void setText(String text) {
        try {
            super.remove(0, getLength());
            super.insertString(0, text, null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e.toString());
        }
    }

    private void highlightCompletedText(int start) {
        editor.setCaretPosition(getLength());
        editor.moveCaretPosition(start);
    }

    private void setSelectedItem(Object item) {
        selecting = true;
        model.setSelectedItem(item);
        selecting = false;
    }

    private Object lookupItem(String pattern) {
        Object selectedItem = model.getSelectedItem();
        if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
            return selectedItem;
        } else {
            for (int i = 0, n = model.getSize(); i < n; i++) {
                Object currentItem = model.getElementAt(i);
                if (currentItem != null && startsWithIgnoreCase(currentItem.toString(), pattern)) {
                    return currentItem;
                }
            }
        }
        return null;
    }

    private boolean startsWithIgnoreCase(String str1, String str2) {
        return str1.toUpperCase().startsWith(str2.toUpperCase());
    }

    private static void createAndShowGUI() {
        final JComboBox comboBox = new JComboBox(new Object[] { "Ester", "Jordi", "Jordina", "Jorge", "Sergi" });
        enable(comboBox);
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.getContentPane().add(comboBox);
        frame.pack();
        frame.setVisible(true);
    }
}
