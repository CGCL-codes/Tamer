package org.jdesktop.swingx.autocomplete;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxEditor;

/**
 * <p>
 * Wrapper around the combobox editor that translates combobox items into
 * strings. The methods <tt>setItem</tt> and <tt>getItem</tt> are modified
 * to account for the string conversion.
 * </p><p>
 * This is necessary for those cases where the combobox items have no useful
 * <tt>toString()</tt> method and a custom <tt>ObjectToStringConverter</tt> is
 * used.
 * </p><p>
 * If we do not do this, the interaction between ComboBoxEditor and JComboBox
 * will result in firing ActionListener events with the string value of
 * ComboBoxEditor as the currently selected value.
 * </p>
 * @author Noel Grandin noelgrandin@gmail.com
 * @author Thomas Bierhance
 */
public class AutoCompleteComboBoxEditor implements ComboBoxEditor {

    /** the original combo box editor*/
    private final ComboBoxEditor wrapped;

    /** the converter used to convert items into their string representation */
    private final ObjectToStringConverter stringConverter;

    /** last selected item */
    private Object oldItem;

    /**
     * Creates a new <tt>AutoCompleteComboBoxEditor</tt>.
     *
     * @param wrapped the original <tt>ComboBoxEditor</tt> to be wrapped
     * @param stringConverter the converter to use to convert items into their
     * string representation.
     */
    public AutoCompleteComboBoxEditor(ComboBoxEditor wrapped, ObjectToStringConverter stringConverter) {
        this.wrapped = wrapped;
        this.stringConverter = stringConverter;
    }

    public Component getEditorComponent() {
        return wrapped.getEditorComponent();
    }

    public void setItem(Object anObject) {
        this.oldItem = anObject;
        wrapped.setItem(stringConverter.getPreferredStringForItem(anObject));
    }

    public Object getItem() {
        final Object wrappedItem = wrapped.getItem();
        String[] oldAsStrings = stringConverter.getPossibleStringsForItem(oldItem);
        for (int i = 0, n = oldAsStrings.length; i < n; i++) {
            String oldAsString = oldAsStrings[i];
            if (oldAsString.equals(wrappedItem)) {
                return oldItem;
            }
        }
        return null;
    }

    public void selectAll() {
        wrapped.selectAll();
    }

    public void addActionListener(ActionListener l) {
        wrapped.addActionListener(l);
    }

    public void removeActionListener(ActionListener l) {
        wrapped.removeActionListener(l);
    }
}
