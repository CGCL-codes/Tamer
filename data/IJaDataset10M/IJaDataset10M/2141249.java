package org.plotnikov.pricemaker;

import javax.swing.*;
import javax.swing.text.*;

public class S03FixedAutoSelection extends PlainDocument {

    ComboBoxModel model;

    boolean selecting = false;

    public S03FixedAutoSelection(ComboBoxModel model) {
        this.model = model;
    }

    public void remove(int offs, int len) throws BadLocationException {
        if (selecting) return;
        super.remove(offs, len);
    }

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (selecting) return;
        System.out.println("insert " + str + " at " + offs);
        super.insertString(offs, str, a);
        String content = getText(0, getLength());
        Object item = lookupItem(content);
        if (item != model.getSelectedItem()) System.out.println("Selecting '" + item + "'");
        selecting = true;
        model.setSelectedItem(item);
        selecting = false;
    }

    private Object lookupItem(String pattern) {
        for (int i = 0, n = model.getSize(); i < n; i++) {
            Object currentItem = model.getElementAt(i);
            if (currentItem.toString().startsWith(pattern)) {
                return currentItem;
            }
        }
        return null;
    }

    private static void createAndShowGUI() {
        JComboBox comboBox = new JComboBox(new Object[] { "Ester", "Jordi", "Jordina", "Jorge", "Sergi" });
        comboBox.setEditable(true);
        JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        editor.setDocument(new S03FixedAutoSelection(comboBox.getModel()));
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.getContentPane().add(comboBox);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
