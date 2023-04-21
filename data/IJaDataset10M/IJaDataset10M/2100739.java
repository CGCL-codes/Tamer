package net.sourceforge.eclipseopengl.wizard;

import net.sourceforge.eclipseopengl.OpenGLMessages;
import net.sourceforge.eclipseopengl.extensionpoint.*;
import net.sourceforge.eclipseopengl.viewers.ExampleContentProvider;
import net.sourceforge.eclipseopengl.viewers.ExampleLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class ExamplePage extends WizardPage implements IPlatformChangeListener, IBindingChangeListener {

    private PluginFieldData fieldData;

    private TreeViewer exampleList;

    private DescriptionContentPart contentPart;

    private Label exampleHeading;

    private Button exampleButton;

    protected ExamplePage(PluginFieldData fieldData) {
        super(OpenGLMessages.ExamplePage_Title);
        setPageComplete(false);
        setTitle(OpenGLMessages.ExamplePage_Title);
        setDescription(OpenGLMessages.ExamplePage_Description);
        this.fieldData = fieldData;
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setLayout(new GridLayout());
        createExampleButton(composite);
        exampleHeading = new Label(composite, SWT.NONE);
        exampleHeading.setText(" ");
        exampleHeading.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        SashForm sashForm = new SashForm(composite, SWT.HORIZONTAL);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 300;
        sashForm.setLayoutData(gd);
        createExampleList(sashForm);
        createDescriptionPart(sashForm);
        setPageComplete(validatePage());
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
        Dialog.applyDialogFont(composite);
    }

    private IExampleEntry getSelectedExample() {
        IExampleEntry result = null;
        IStructuredSelection selection = (IStructuredSelection) exampleList.getSelection();
        if (!selection.isEmpty()) {
            result = (IExampleEntry) selection.getFirstElement();
        }
        return result;
    }

    private void updateDescriptionText() {
        IExampleEntry example = getSelectedExample();
        if (example != null) {
            contentPart.setText(example.getDescription());
        } else {
            contentPart.setText("");
        }
    }

    private void createExampleList(Composite parent) {
        exampleList = new TreeViewer(parent, SWT.SINGLE | SWT.BORDER);
        exampleList.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
        exampleList.setLabelProvider(new ExampleLabelProvider());
        exampleList.setContentProvider(new ExampleContentProvider());
        exampleList.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                updateDescriptionText();
                setPageComplete(validatePage());
            }
        });
        exampleList.addTreeListener(new ITreeViewerListener() {

            public void treeCollapsed(TreeExpansionEvent event) {
                updateDescriptionText();
                setPageComplete(validatePage());
            }

            public void treeExpanded(TreeExpansionEvent event) {
                updateDescriptionText();
                setPageComplete(validatePage());
            }
        });
        exampleList.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                IExampleEntry selection = getSelectedExample();
                if (selection instanceof IXMLExample) {
                    if (validatePage()) {
                        advanceToNextPageOrFinish();
                    }
                }
                if (selection instanceof IXMLCategory) {
                    TreeItem[] items = exampleList.getTree().getSelection();
                    if (items.length > 0) {
                        TreeItem item = items[0];
                        item.setExpanded(!item.getExpanded());
                    }
                }
            }
        });
    }

    private void advanceToNextPageOrFinish() {
        if (canFlipToNextPage()) {
            getContainer().showPage(getNextPage());
        } else if (validatePage()) {
            if (getWizard().performFinish()) {
                ((WizardDialog) getContainer()).close();
            }
        }
    }

    private void createDescriptionPart(Composite parent) {
        contentPart = new DescriptionContentPart(parent);
    }

    private void createExampleButton(Composite parent) {
        exampleButton = new Button(parent, SWT.CHECK);
        exampleButton.setText(OpenGLMessages.ExamplePage_EnableExamples);
        exampleButton.setSelection(true);
        exampleButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                boolean enabled = exampleButton.getSelection();
                setExampleEnabled(enabled);
            }
        });
    }

    private void setExampleEnabled(boolean enabled) {
        exampleHeading.setEnabled(enabled);
        exampleList.getControl().setEnabled(enabled);
        contentPart.setEnabled(enabled);
        setPageComplete(validatePage());
    }

    public void platformChanged() {
        if (fieldData.getBinding() != null && fieldData.getPlatform() != null) {
            String bindingId = fieldData.getBinding().getId();
            IXMLOSEntry osEntry = fieldData.getPlatform().getOSEntry();
            Object[] items = ExampleUtil.getExamples(bindingId, osEntry).getItems();
            exampleList.setInput(items);
            exampleList.expandAll();
            exampleList.collapseAll();
            if (items.length == 0) {
                exampleButton.setSelection(false);
                setExampleEnabled(false);
            } else {
                exampleButton.setSelection(true);
                setExampleEnabled(true);
            }
        }
    }

    public void bindingChanged() {
        if (fieldData.getBinding() != null) {
            String bindingName = fieldData.getBinding().getName();
            exampleHeading.setText(OpenGLMessages.ExamplePage_AvailableExamples + bindingName);
        }
    }

    protected boolean validatePage() {
        IExampleEntry exampleEntry = getSelectedExample();
        IXMLExample example = null;
        if (exampleEntry instanceof IXMLExample) {
            example = (IXMLExample) exampleEntry;
        }
        fieldData.setExample(example);
        if (!exampleButton.getSelection()) {
            fieldData.setExample(null);
        }
        return !exampleButton.getSelection() || fieldData.getExample() != null;
    }
}
