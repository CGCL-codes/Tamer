package com.fh.auge.preferences;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.fh.auge.Activator;

public class CurrencyPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private SimpleComboFieldEditor simpleComboFieldEditor;

    public CurrencyPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    public void createFieldEditors() {
        String value = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.CURRENCIES);
        simpleComboFieldEditor = new SimpleComboFieldEditor(PreferenceConstants.PORTFOLIO_CURRENCY, "Portfolio Currency:", createItems(value.split(";")), getFieldEditorParent());
        addField(simpleComboFieldEditor);
        Composite c = new Composite(getFieldEditorParent(), SWT.NONE);
        GridData d = new GridData();
        d.grabExcessHorizontalSpace = true;
        d.horizontalAlignment = GridData.FILL;
        d.horizontalSpan = 2;
        c.setLayoutData(d);
        addField(new ListEditor(PreferenceConstants.CURRENCIES, "Available Currencies:", c) {

            @Override
            protected String createList(String[] items) {
                StringBuffer buf = new StringBuffer();
                for (String string : items) {
                    buf.append(string);
                    buf.append(";");
                }
                return buf.toString();
            }

            @Override
            protected String getNewInputObject() {
                InputDialog c = new InputDialog(getShell(), "Add Currency", "Currency Code", null, null);
                if (InputDialog.OK == c.open()) {
                    simpleComboFieldEditor.addValue(c.getValue());
                    return c.getValue();
                }
                return null;
            }

            @Override
            protected String[] parseString(String stringList) {
                return stringList.split(";");
            }
        });
    }

    private String[][] createItems(String[] items) {
        String[][] res = new String[items.length][2];
        for (int i = 0; i < items.length; i++) {
            res[i][0] = items[i];
            res[i][1] = items[i];
        }
        return res;
    }

    public void init(IWorkbench workbench) {
    }
}
