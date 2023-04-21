package com.ixora.common.typedproperties.ui;

import java.awt.Color;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import com.ixora.common.typedproperties.PropertyEntry;
import com.ixora.common.typedproperties.PropertyEntryNumber;
import com.ixora.common.ui.UIFactoryMgr;

/**
 * @author Daniel Moraru
 */
public abstract class CellEditorNumber<T extends Comparable<T>> extends CellEditorInPlace<T> {

    /** Compoenent */
    protected JFormattedTextField field;

    protected PropertyEntryNumber<T> property;

    /**
     * Constructor.
     * @param comp
     */
    protected CellEditorNumber(CellComponentNumber<T> comp) {
        super();
        final NumberFormatter formatter = comp.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        formatter.setAllowsInvalid(true);
        field = UIFactoryMgr.createFormattedTextField(formatter);
        field.setBackground(BACKGROUND);
        delegate = new DefaultCellEditor(field);
        field.getDocument().addDocumentListener(new DocumentListener() {

            private Color originalColor = field.getForeground();

            public void insertUpdate(DocumentEvent e) {
                checkIfValid(e);
            }

            @SuppressWarnings("unchecked")
            private void checkIfValid(DocumentEvent e) {
                boolean invalid = false;
                try {
                    if (property != null) {
                        int len = e.getDocument().getLength();
                        if (len == 0 && !property.isRequired()) {
                            return;
                        }
                        Object obj = formatter.stringToValue(e.getDocument().getText(0, len));
                        property.validateValue((T) obj);
                    }
                } catch (Exception ex) {
                    invalid = true;
                } finally {
                    if (invalid) {
                        field.setForeground(Color.RED);
                    } else {
                        field.setForeground(originalColor);
                    }
                }
            }

            public void removeUpdate(DocumentEvent e) {
                checkIfValid(e);
            }

            public void changedUpdate(DocumentEvent e) {
                checkIfValid(e);
            }
        });
    }

    /**
     * @see com.ixora.common.typedproperties.ui.PropertyEntryCellEditor#setPropertyEntry(com.ixora.common.app.typedproperties.PropertyEntry)
     */
    public void setPropertyEntry(PropertyEntry<T> e) {
        property = (PropertyEntryNumber<T>) e;
        NumberFormatter formatter = (NumberFormatter) field.getFormatter();
        formatter.setMinimum(property.getMin());
        formatter.setMaximum(property.getMax());
        Object obj = e.getValue();
        if (obj != null) {
            field.setValue(obj);
        } else {
            field.setValue(null);
        }
    }
}
