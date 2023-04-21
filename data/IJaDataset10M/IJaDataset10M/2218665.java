package net.sf.depcon.core.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ContactViewPage extends FormPage implements IFormPage {

    public static final String ID = "net.sf.depcon.core.ui.editors.ContactViewPage";

    public ContactViewPage(final IEditorInput input, final FormEditor parent) {
        super(parent, ID, "View");
    }

    @Override
    protected void createFormContent(final IManagedForm managedForm) {
        final FormToolkit toolkit = managedForm.getToolkit();
        final ScrolledForm form = managedForm.getForm();
        final Composite body = form.getBody();
        final TableWrapLayout layout = new TableWrapLayout();
        layout.makeColumnsEqualWidth = false;
        layout.numColumns = 2;
        body.setLayout(layout);
        Section section = toolkit.createSection(body, Section.TITLE_BAR);
        section.setText("Image");
        TableWrapData twData = new TableWrapData();
        twData.rowspan = 3;
        section.setLayoutData(twData);
        section = toolkit.createSection(body, Section.TITLE_BAR);
        section.setText("Identification");
        twData = new TableWrapData();
        twData.grabHorizontal = true;
        section.setLayoutData(twData);
        section = toolkit.createSection(body, SWT.NONE);
        section.setText("Address");
        twData = new TableWrapData();
        twData.grabHorizontal = true;
        section.setLayoutData(twData);
        section = toolkit.createSection(body, SWT.NONE);
        section.setText("Contact Information");
        twData = new TableWrapData();
        twData.grabHorizontal = true;
        section.setLayoutData(twData);
    }
}
