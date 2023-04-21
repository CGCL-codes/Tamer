package edu.gsbme.wasabi.UI.Dialog.Metadata;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.CommonParserLib.MMLMetaData;
import edu.gsbme.MMLParser2.Vocabulary.Metadata;
import edu.gsbme.wasabi.UI.UItype;
import edu.gsbme.wasabi.UI.Algorithm.DeleteConfirmDialog;
import edu.gsbme.wasabi.UI.DataStruct.ModificationTracker;
import edu.gsbme.wasabi.UI.Dialog.MFormDialog;
import edu.gsbme.wasabi.UI.Forms.Metadata.DOAPProjectForm;

/**
 * FIXME : DOAP is under review, might be replaced with a simpler document description sytnax
 * @author David
 *
 */
public class ProjectDialog extends MFormDialog {

    public static void main(String[] arg) {
        ProjectDialog test = new ProjectDialog(null, null, null);
        test.OpenDialog();
    }

    public ProjectDialog(Shell shell, Element element, UItype type) {
        super(shell, element, type);
        x = 800;
        y = 600;
        if (type == UItype.NEW) {
            if (!returnElement().getTagName().equals(Metadata.annotation.toString())) {
                Element anno = returnActiveEditorModel().getFactory().getMetadataWorker().insertAnnotaitonTag(returnElement());
                modTracker.insertNewModification(returnElement(), anno);
                setElement(anno);
            }
            Element proj = returnActiveEditorModel().getFactory().getMetadataWorker().insertProjectTag(returnElement());
            modTracker.insertNewModification(returnElement(), proj);
            setElement(proj);
            insertObserveElement(proj);
        }
    }

    protected void createFormContent(IManagedForm mform) {
        FormToolkit toolkit = mform.getToolkit();
        ScrolledForm scForm = mform.getForm();
        skeleton = new DOAPProjectForm();
        skeleton.construct_layout(toolkit, scForm);
    }

    @Override
    public boolean commit_change() {
        commitTextFields();
        return true;
    }

    @Override
    public void initial_load_UI() {
        DOAPProjectForm form = (DOAPProjectForm) skeleton;
        setupDeveloperTable();
        setupDeveloperTableButtons();
        setupMaintainerTable();
        setupMaintainerTableButton();
        refreshTextFields();
    }

    private void setupDeveloperTable() {
        DOAPProjectForm form = (DOAPProjectForm) skeleton;
        form.developer.listTable.setLinesVisible(true);
        form.developer.listTable.setHeaderVisible(true);
        TableColumn col = new TableColumn(form.developer.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText("Surname");
        col.setWidth((int) (form.developer.listTable.getSize().x * 0.4));
        col = new TableColumn(form.developer.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText("Firstname");
        col.setWidth((int) (form.developer.listTable.getSize().x * 0.4));
        form.developer.listTable.showColumn(col);
        form.developer.list_edit.setEnabled(false);
        form.developer.list_delete.setEnabled(false);
    }

    private void setupMaintainerTable() {
        DOAPProjectForm form = (DOAPProjectForm) skeleton;
        form.maintainers.listTable.setLinesVisible(true);
        form.maintainers.listTable.setHeaderVisible(true);
        TableColumn col = new TableColumn(form.maintainers.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText("Surname");
        col.setWidth((int) (form.maintainers.listTable.getSize().x * 0.4));
        col = new TableColumn(form.maintainers.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText("Firstname");
        col.setWidth((int) (form.maintainers.listTable.getSize().x * 0.4));
        form.maintainers.listTable.showColumn(col);
        form.maintainers.list_edit.setEnabled(false);
        form.maintainers.list_delete.setEnabled(false);
    }

    private void setupDeveloperTableButtons() {
        final DOAPProjectForm form = (DOAPProjectForm) skeleton;
        form.developer.list_new.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Element dev = MMLMetaData.returnCreatorTag(returnElement());
                if (dev == null) {
                    dev = returnActiveEditorModel().getFactory().getMetadataWorker().insertCreatorTag(returnElement());
                    ModificationTracker tracker = new ModificationTracker();
                    tracker.insertNewModification(returnElement(), dev);
                    new vCardDialog(getShell(), dev, UItype.NEW, tracker).OpenDialog();
                    return;
                }
                new vCardDialog(getShell(), dev, UItype.NEW).OpenDialog();
            }
        });
        form.developer.list_edit.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (form.developer.listTable.getSelectionCount() > 0) {
                    Element selected = (Element) form.developer.listTable.getSelection()[0].getData();
                    new vCardDialog(getShell(), selected, UItype.EDIT).OpenDialog();
                }
            }
        });
        form.developer.list_delete.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (form.developer.listTable.getSelectionCount() > 0) {
                    Element selected = (Element) form.developer.listTable.getSelection()[0].getData();
                    boolean delete = DeleteConfirmDialog.deleteConfirmation(getParentShell(), returnElement(), selected);
                    if (delete) {
                        modTracker.insertDeleteModification(returnElement(), selected);
                    }
                }
            }
        });
    }

    private void setupMaintainerTableButton() {
        final DOAPProjectForm form = (DOAPProjectForm) skeleton;
        form.maintainers.list_new.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Element dev = MMLMetaData.returnMaintainerTag(returnElement());
                if (dev == null) {
                    dev = returnActiveEditorModel().getFactory().getMetadataWorker().insertMaintainerTag(returnElement());
                    ModificationTracker tracker = new ModificationTracker();
                    tracker.insertNewModification(returnElement(), dev);
                    new vCardDialog(getShell(), dev, UItype.NEW, tracker).OpenDialog();
                    return;
                }
                new vCardDialog(getShell(), dev, UItype.NEW).OpenDialog();
            }
        });
        form.maintainers.list_edit.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (form.maintainers.listTable.getSelectionCount() > 0) {
                    Element selected = (Element) form.maintainers.listTable.getSelection()[0].getData();
                    new vCardDialog(getShell(), selected, UItype.EDIT).OpenDialog();
                }
            }
        });
        form.maintainers.list_delete.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (form.maintainers.listTable.getSelectionCount() > 0) {
                    Element selected = (Element) form.maintainers.listTable.getSelection()[0].getData();
                    boolean delete = DeleteConfirmDialog.deleteConfirmation(getParentShell(), returnElement(), selected);
                    if (delete) {
                        modTracker.insertDeleteModification(returnElement(), selected);
                    }
                }
            }
        });
    }

    private void refreshTextFields() {
        DOAPProjectForm form = (DOAPProjectForm) skeleton;
    }

    private void commitTextFields() {
        DOAPProjectForm form = (DOAPProjectForm) skeleton;
    }

    @Override
    public void refresh_UI() {
        refreshTextFields();
    }
}
