package org.eclipse.jface.preference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * A field editor for a boolean type preference.
 */
public class BooleanFieldEditor extends FieldEditor {

    /**
     * Style constant (value <code>0</code>) indicating the default
     * layout where the field editor's check box appears to the left
     * of the label.
     */
    public static final int DEFAULT = 0;

    /**
     * Style constant (value <code>1</code>) indicating a layout 
     * where the field editor's label appears on the left
     * with a check box on the right.
     */
    public static final int SEPARATE_LABEL = 1;

    /**
     * Style bits. Either <code>DEFAULT</code> or
     * <code>SEPARATE_LABEL</code>.
     */
    private int style;

    /**
     * The previously selected, or "before", value.
     */
    private boolean wasSelected;

    /**
     * The checkbox control, or <code>null</code> if none.
     */
    private Button checkBox = null;

    /**
     * Creates a new boolean field editor 
     */
    protected BooleanFieldEditor() {
    }

    /**
     * Creates a boolean field editor in the given style.
     * 
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param style the style, either <code>DEFAULT</code> or
     *   <code>SEPARATE_LABEL</code>
     * @param parent the parent of the field editor's control
     * @see #DEFAULT
     * @see #SEPARATE_LABEL
     */
    public BooleanFieldEditor(String name, String labelText, int style, Composite parent) {
        init(name, labelText);
        this.style = style;
        createControl(parent);
    }

    /**
     * Creates a boolean field editor in the default style.
     * 
     * @param name the name of the preference this field editor works on
     * @param label the label text of the field editor
     * @param parent the parent of the field editor's control
     */
    public BooleanFieldEditor(String name, String label, Composite parent) {
        this(name, label, DEFAULT, parent);
    }

    protected void adjustForNumColumns(int numColumns) {
        if (style == SEPARATE_LABEL) numColumns--;
        ((GridData) checkBox.getLayoutData()).horizontalSpan = numColumns;
    }

    protected void doFillIntoGrid(Composite parent, int numColumns) {
        String text = getLabelText();
        switch(style) {
            case SEPARATE_LABEL:
                getLabelControl(parent);
                numColumns--;
                text = null;
            default:
                checkBox = getChangeControl(parent);
                GridData gd = new GridData();
                gd.horizontalSpan = numColumns;
                checkBox.setLayoutData(gd);
                if (text != null) checkBox.setText(text);
        }
    }

    protected void doLoad() {
        if (checkBox != null) {
            boolean value = getPreferenceStore().getBoolean(getPreferenceName());
            checkBox.setSelection(value);
            wasSelected = value;
        }
    }

    protected void doLoadDefault() {
        if (checkBox != null) {
            boolean value = getPreferenceStore().getDefaultBoolean(getPreferenceName());
            checkBox.setSelection(value);
            wasSelected = value;
        }
    }

    protected void doStore() {
        getPreferenceStore().setValue(getPreferenceName(), checkBox.getSelection());
    }

    /**
     * Returns this field editor's current value.
     *
     * @return the value
     */
    public boolean getBooleanValue() {
        return checkBox.getSelection();
    }

    /**
     * Returns the change button for this field editor.
     * @param parent The Composite to create the receiver in.
     *
     * @return the change button
     */
    protected Button getChangeControl(Composite parent) {
        if (checkBox == null) {
            checkBox = new Button(parent, SWT.CHECK | SWT.LEFT);
            checkBox.setFont(parent.getFont());
            checkBox.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    boolean isSelected = checkBox.getSelection();
                    valueChanged(wasSelected, isSelected);
                    wasSelected = isSelected;
                }
            });
            checkBox.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent event) {
                    checkBox = null;
                }
            });
        } else {
            checkParent(checkBox, parent);
        }
        return checkBox;
    }

    public int getNumberOfControls() {
        switch(style) {
            case SEPARATE_LABEL:
                return 2;
            default:
                return 1;
        }
    }

    public void setFocus() {
        if (checkBox != null) {
            checkBox.setFocus();
        }
    }

    public void setLabelText(String text) {
        super.setLabelText(text);
        Label label = getLabelControl();
        if (label == null && checkBox != null) {
            checkBox.setText(text);
        }
    }

    /**
     * Informs this field editor's listener, if it has one, about a change
     * to the value (<code>VALUE</code> property) provided that the old and
     * new values are different.
     *
     * @param oldValue the old value
     * @param newValue the new value
     */
    protected void valueChanged(boolean oldValue, boolean newValue) {
        setPresentsDefaultValue(false);
        if (oldValue != newValue) fireStateChanged(VALUE, oldValue, newValue);
    }

    public void setEnabled(boolean enabled, Composite parent) {
        if (style == SEPARATE_LABEL) super.setEnabled(enabled, parent);
        getChangeControl(parent).setEnabled(enabled);
    }
}
