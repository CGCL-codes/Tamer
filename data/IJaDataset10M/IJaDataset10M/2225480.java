package net.sf.depcon.core.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.SharedHeaderFormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class ContactEditor extends SharedHeaderFormEditor {

    public static final String ID = "net.sf.depcon.core.ui.editors.ContactEditor";

    public ContactEditor() {
    }

    @Override
    protected void addPages() {
        try {
            addPage(new ContactViewPage(getEditorInput(), this));
            addPage(new ContactEditPage(getEditorInput(), this));
            addPage(new ContactNotesPage(getEditorInput(), this));
        } catch (final PartInitException e) {
            e.printStackTrace();
            System.err.println("Failed to create editor");
        }
    }

    @Override
    protected void createHeaderContents(final IManagedForm headerForm) {
        final ScrolledForm form = headerForm.getForm();
        final FormToolkit toolkit = headerForm.getToolkit();
        toolkit.decorateFormHeading(form.getForm());
        form.setText("Header");
    }

    @Override
    public void doSave(final IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }
}
