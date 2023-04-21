package org.nightlabs.jfire.base.ui.prop.structedit;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.nightlabs.base.ui.composite.ListComposite;
import org.nightlabs.base.ui.wizard.DynamicPathWizard;
import org.nightlabs.base.ui.wizard.DynamicPathWizardPage;
import org.nightlabs.jfire.base.ui.resource.Messages;

public class StructFieldCreationWizard extends DynamicPathWizard {

    private class StructFieldTypePage extends DynamicPathWizardPage {

        private ListComposite<StructFieldMetaData> fieldList;

        private Map<StructFieldMetaData, DynamicPathWizardPage> fieldCreationWizardPages;

        private Label description;

        public StructFieldTypePage() {
            super(StructFieldTypePage.class.getName(), Messages.getString("org.nightlabs.jfire.base.ui.prop.structedit.StructFieldCreationWizard.StructFieldTypePage.title"));
            fieldCreationWizardPages = new HashMap<StructFieldMetaData, DynamicPathWizardPage>();
        }

        public StructFieldMetaData getSelectedFieldMetaData() {
            return fieldList.getSelectedElement();
        }

        @Override
        public Control createPageContents(Composite parent) {
            Composite comp = new Composite(parent, SWT.NONE);
            comp.setLayout(new GridLayout(1, false));
            new Label(comp, SWT.NONE).setText(Messages.getString("org.nightlabs.jfire.base.ui.prop.structedit.StructFieldCreationWizard.StructFieldTypePage.availableTypesLabel.text"));
            Collection<StructFieldMetaData> fields = StructFieldFactoryRegistry.sharedInstance().getFieldMetaDataMap().values();
            fieldList = new ListComposite<StructFieldMetaData>(comp, SWT.NONE);
            fieldList.setInput(new LinkedList<StructFieldMetaData>(fields));
            description = new Label(comp, SWT.NONE);
            description.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            fieldList.getList().addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                    widgetSelected(e);
                    if (canFinish()) finish();
                }

                public void widgetSelected(SelectionEvent e) {
                    StructFieldMetaData selected = fieldList.getSelectedElement();
                    description.setText(selected.getFieldDescription());
                    StructFieldCreationWizard wizard = (StructFieldCreationWizard) getWizard();
                    if (!fieldCreationWizardPages.containsKey(selected)) {
                        DynamicPathWizardPage page = selected.getFieldFactory().createWizardPage();
                        fieldCreationWizardPages.put(selected, page);
                    }
                    DynamicPathWizardPage page = fieldCreationWizardPages.get(selected);
                    wizard.replaceDetailsWizardPage(page);
                    wizard.setSelectedFieldMetaData(selected);
                }
            });
            return comp;
        }
    }

    private DynamicPathWizardPage detailsPage = null;

    private StructFieldMetaData selectedFieldMetaData = null;

    public StructFieldCreationWizard() {
        addPage(new StructFieldTypePage());
    }

    public void replaceDetailsWizardPage(DynamicPathWizardPage newDetailsPage) {
        if (detailsPage != null) removeDynamicWizardPage(detailsPage);
        if (newDetailsPage == null) return;
        detailsPage = newDetailsPage;
        addDynamicWizardPage(detailsPage);
    }

    public DynamicPathWizardPage getDetailsWizardPage() {
        return detailsPage;
    }

    public void setSelectedFieldMetaData(StructFieldMetaData metaData) {
        selectedFieldMetaData = metaData;
    }

    public StructFieldMetaData getSelectedFieldMetaData() {
        return selectedFieldMetaData;
    }

    @Override
    public boolean performFinish() {
        return true;
    }
}
