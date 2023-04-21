package mipt.gui.choice;

import mipt.common.Named;

public class ComboBox extends javax.swing.JComboBox implements ConvertingCellEditor.EditorConverter {

    /**
 * ComboBox constructor comment.
 */
    public ComboBox() {
        super();
    }

    /**
 * ComboBox constructor comment.
 * @param items java.lang.Object[]
 */
    public ComboBox(java.lang.Object[] items) {
        super(items);
    }

    /**
 * ComboBox constructor comment.
 * @param items java.util.Vector
 */
    public ComboBox(java.util.Vector items) {
        super(items);
    }

    /**
 * ComboBox constructor comment.
 * @param aModel javax.swing.ComboBoxModel
 */
    public ComboBox(javax.swing.ComboBoxModel aModel) {
        super(aModel);
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        stopEditing();
    }

    public void configureEditor(javax.swing.ComboBoxEditor editor, Object item) {
        editor.setItem(convertValueBeforeEdit(item, null));
    }

    /**
 * 
 * @return java.lang.Object
 * @param item java.lang.Object - can't be null
 */
    public Object convertValueAfterEdit(Object item, ConvertingCellEditor editor) {
        int n = getItemCount();
        for (int i = 0; i < n; i++) {
            Object current = getItemAt(i);
            if (current instanceof Named) {
                if (item.equals(((Named) current).getName())) return current;
            }
        }
        return item;
    }

    public Object convertValueBeforeEdit(Object item, ConvertingCellEditor editor) {
        return DefaultCellRenderer.convertName(item);
    }

    /**
 * 
 * @return javax.swing.JTextField
 */
    public final javax.swing.JTextField getField() {
        if (getEditor() instanceof DefaultComboBoxEditor) {
            return ((DefaultComboBoxEditor) getEditor()).getField();
        }
        return null;
    }

    /**
 * 
 * @return int
 * @param item java.lang.Object
 */
    public final int getIndexOf(Object item) {
        int n = getItemCount();
        for (int i = 0; i < n; i++) {
            if (item == getItemAt(i)) return i;
        }
        return -1;
    }

    /**
 * 
 * @return boolean
 * @param obj java.lang.Object
 */
    public boolean isAmongItems(Object obj) {
        int n = getItemCount();
        for (int i = 0; i < n; i++) {
            if (getItemAt(i) == obj) return true;
        }
        return false;
    }

    /**
 * 
 * @param item java.lang.Object
 */
    public void itemUpdated(Object item) {
        int index = getIndexOf(item);
        if (index < 0) return;
        itemUpdatedAt(index);
    }

    /**
 * 
 * @param index int
 */
    public void itemUpdatedAt(int index) {
        contentsChanged(new javax.swing.event.ListDataEvent(getModel(), javax.swing.event.ListDataEvent.CONTENTS_CHANGED, index, index));
    }

    /**
 * 
 * @return java.util.Iterator
 */
    public java.util.Iterator iterator() {
        return new ListModelIterator(getModel());
    }

    /**
 * 
 * @param index int
 * @param object java.lang.Object
 */
    public void replaceItem(Object object, int index) {
        removeItemAt(index);
        insertItemAt(object, index);
    }

    /**
 * 
 * @param field javax.swing.JTextField
 */
    public final void setField(javax.swing.JTextField field) {
        if (getEditor() instanceof DefaultComboBoxEditor) {
            ((DefaultComboBoxEditor) getEditor()).setField(field);
        }
    }

    /**
 * 
 */
    public void stopEditing() {
        Object newItem = getEditor().getItem();
        getModel().setSelectedItem(convertValueAfterEdit(newItem, null));
        getUI().setPopupVisible(this, false);
    }

    /**
 * Overriden to install renderer and editor because this is the only method called always
 *   from all constructors
 * @param model javax.swing.ComboBoxModel
 */
    public void updateUI() {
        setEditor(new DefaultComboBoxEditor());
        setRenderer(new DefaultComboBoxRenderer());
        super.updateUI();
    }
}
