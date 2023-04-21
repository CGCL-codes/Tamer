package com.ivis.xprocess.ui.wizards;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.properties.DialogMessages;
import com.ivis.xprocess.ui.properties.WizardMessages;
import com.ivis.xprocess.ui.util.FontAndColorManager;
import com.ivis.xprocess.ui.view.providers.ElementContentProvider;
import com.ivis.xprocess.ui.view.providers.ElementLabelProvider;
import com.ivis.xprocess.ui.views.sorters.ElementSorter;

public class ElementDeletionWizardPage extends XProcessWizardPage {

    private TableViewer elementListViewer;

    private IElementWrapper[] elementWrappers;

    public ElementDeletionWizardPage(String pageName, IElementWrapper[] elementWrappers) {
        super(pageName);
        this.elementWrappers = elementWrappers;
        this.setTitle(WizardMessages.deletion_wizard_title);
        this.setDescription(WizardMessages.deletion_wizard_description);
    }

    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout();
        container.setLayout(gridLayout);
        Label messageLabel = new Label(container, SWT.NONE);
        messageLabel.setText(DialogMessages.delete_message);
        new Label(container, SWT.NONE);
        elementListViewer = new TableViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gridData = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
        elementListViewer.getTable().setLayoutData(gridData);
        elementListViewer.setContentProvider(new ElementContentProvider());
        ILabelProvider labelProvider = new ElementLabelProvider();
        elementListViewer.setLabelProvider(labelProvider);
        elementListViewer.getTable().setFont(FontAndColorManager.getInstance().getDefaultFont());
        elementListViewer.setSorter(new ElementSorter());
        Set<IElementWrapper> elementWrapperSet = new HashSet<IElementWrapper>();
        for (int i = 0; i < elementWrappers.length; i++) {
            elementWrapperSet.add(elementWrappers[i]);
        }
        elementListViewer.setInput(elementWrapperSet.toArray(new IElementWrapper[] {}));
        setControl(container);
        setupData();
        setupTestHarness();
    }

    @Override
    protected void setupData() {
        if ((getPreviousPage() != null) && getPreviousPage() instanceof XProcessWizardPage) {
            ((XProcessWizardPage) getPreviousPage()).setPageComplete(true);
        }
        checkData();
    }

    @Override
    public void checkData() {
        setPageComplete(true);
    }

    /**
     * Setting up the wizard page for Abbot
     */
    private void setupTestHarness() {
    }

    @Override
    public boolean save() {
        return true;
    }

    @Override
    public void cancel() {
    }
}
