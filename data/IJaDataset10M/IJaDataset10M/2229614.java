package com.teaminabox.eclipse.wiki.preferences;

import java.util.Arrays;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ComboListEditor extends FieldEditor {

    private final String[] labels;

    private final String[] choices;

    private Combo combo;

    public ComboListEditor(String preferenceKey, String description, String[] labels, String[] choices, Composite parent) {
        init(preferenceKey, description);
        this.labels = labels;
        this.choices = choices;
        createControl(parent);
    }

    protected void adjustForNumColumns(int numColumns) {
        ((GridData) combo.getLayoutData()).horizontalSpan = numColumns;
    }

    protected void doFillIntoGrid(Composite parent, int numColumns) {
        combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        combo.setItems(labels);
        combo.setText(getLabelText());
    }

    protected void doLoad() {
        combo.select(getIndexOf(getPreferenceStore().getString(getPreferenceName()), choices));
    }

    protected void doLoadDefault() {
        combo.select(getIndexOf(getPreferenceStore().getDefaultString(getPreferenceName()), choices));
    }

    private int getIndexOf(String value, String[] list) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(value)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Value of " + value + " is not in " + Arrays.asList(list));
    }

    protected void doStore() {
        getPreferenceStore().setValue(getPreferenceName(), choices[combo.getSelectionIndex()]);
    }

    public int getNumberOfControls() {
        return 2;
    }
}
