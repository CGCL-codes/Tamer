package gov.sns.apps.jeri.tools.swing.autocompletecombobox;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

/**
 * Provides a combo box that does a lookup within the list when the user types 
 * in it. When the user types in the combo box, it looks for an item that starts 
 * with what the user has typed and finishes it for them. The text that was 
 * added is then selected so that if the user keeps typing, the text that they
 * type is added to their text.
 * 
 * @author Chris Fowlkes
 */
public class AutoCompleteComboBox extends JComboBox {

    /**
   * Creates a new <CODE>AutoCompleteComboBox</CODE>.
   */
    public AutoCompleteComboBox() {
        setUI(new WideComboUI());
        getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                editor_keyTyped(e);
            }
        });
    }

    /**
   * Called when a key is typed in the editor. This method searches through the 
   * list for something that matches the text typed. The first item encountered 
   * is selected and the text is completed. The text that was added is then 
   * selected, so that if the user keeps typing they are only adding to what 
   * they typed.
   * 
   * @param e The <CODE>KeyEvent</CODE> that caused the invocatin of this method.
   */
    void editor_keyTyped(final KeyEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JTextField editor = (JTextField) getEditor().getEditorComponent();
                String editorText = editor.getText();
                int itemCount, editorTextLength = editorText.length();
                while (editorTextLength > 0) {
                    itemCount = getItemCount();
                    for (int i = 0; i < itemCount; i++) {
                        Object currentItem = getItemAt(i);
                        String currentString;
                        if (currentItem == null) currentString = ""; else currentString = currentItem.toString().toUpperCase();
                        if (currentString.startsWith(editorText.toUpperCase())) {
                            final int selectedIndex = i;
                            String newEditorText = getItemAt(selectedIndex).toString();
                            editor.setText(newEditorText);
                            Caret editorCaret = editor.getCaret();
                            editorCaret.setDot(newEditorText.length());
                            editorCaret.moveDot(editorText.length());
                            return;
                        }
                    }
                    editorText = editorText.substring(0, editorText.length() - 1);
                }
            }
        });
    }

    /**
   * Gets the item selected in the combo box.
   * 
   * @return The item selected in the combo.
   */
    @Override
    public Object getSelectedItem() {
        Object selectedObject = super.getSelectedItem();
        if (selectedObject == null || !(selectedObject instanceof String)) return selectedObject;
        String selectedItem = selectedObject.toString();
        if (isEditable()) {
            String editorItem = getEditor().getItem().toString();
            if (editorItem != null && !selectedItem.equals(editorItem)) selectedItem = editorItem;
        }
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            Object currentItem = getItemAt(i);
            String stringValue;
            if (currentItem == null) stringValue = ""; else stringValue = currentItem.toString();
            if (selectedItem.equals(stringValue)) return getItemAt(i);
        }
        return selectedObject;
    }
}
