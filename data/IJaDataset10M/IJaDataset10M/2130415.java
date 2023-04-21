package com.ecmdeveloper.plugin.search.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author ricardo.belfor
 *
 */
public abstract class ValueWizardPage extends WizardPage {

    private Object value;

    protected ValueWizardPage(String pageName) {
        super(pageName);
    }

    @Override
    public final void createControl(Composite parent) {
        Composite container = createContainer(parent);
        createInput(container);
    }

    protected abstract void createInput(Composite container);

    private Composite createContainer(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        container.setLayout(gridLayout);
        setControl(container);
        return container;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    protected void setDirty() {
        getWizard().getContainer().updateButtons();
    }

    protected GridData getFullRowGridData() {
        GridData gd = new GridData();
        gd.horizontalSpan = 2;
        return gd;
    }
}
