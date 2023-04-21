package edu.gsbme.wasabi.UI.Dialog.FML.BREP;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.Factory.FMLFactory;
import edu.gsbme.MMLParser2.Vocabulary.AttributesValues;
import edu.gsbme.MMLParser2.Vocabulary.FML;
import edu.gsbme.wasabi.UI.UItype;
import edu.gsbme.wasabi.UI.Algorithm.DeleteConfirmDialog;
import edu.gsbme.wasabi.UI.Algorithm.FML.search2DEdgeAdj;
import edu.gsbme.wasabi.UI.Algorithm.data.GenericSearchResult;
import edu.gsbme.wasabi.UI.DataStruct.ModificationTracker;
import edu.gsbme.wasabi.UI.Dialog.MFormDialog;
import edu.gsbme.wasabi.UI.Forms.FML.BREP.EdgeAdjOverviewForm;
import edu.gsbme.wasabi.UI.Forms.Generics.GenericTableAndButtons;

/**
 * Edge adjacency overview dialog
 * @author David
 *
 */
public class EdgeAdjOverviewDialog extends MFormDialog {

    public EdgeAdjOverviewDialog(Shell shell) {
        super(shell);
    }

    public EdgeAdjOverviewDialog(Shell shell, Element element, UItype type) {
        super(shell, element, type);
        x = 500;
        y = 400;
        if (type == UItype.NEW) {
            FMLFactory fmlFactory = new FMLFactory("", returnActiveEditorModel());
            if (returnElement().getTagName().equals(FML.b_rep.toString())) {
                Element elem = fmlFactory.getFrameWorker().insertTopology(returnElement());
                modTracker.insertNewModification(returnElement(), elem);
                setElement(elem);
            }
            Element elem = fmlFactory.getFrameWorker().insertAdjacency(returnElement(), AttributesValues.edge.toString());
            modTracker.insertNewModification(returnElement(), elem);
            setElement(elem);
            insertObserveElement(elem);
        }
    }

    public EdgeAdjOverviewDialog(Shell shell, Element element, UItype type, ModificationTracker tracker) {
        super(shell, element, type, tracker);
        x = 500;
        y = 400;
        if (type == UItype.NEW) {
            FMLFactory fmlFactory = new FMLFactory("", returnActiveEditorModel());
            if (returnElement().getTagName().equals(FML.b_rep.toString())) {
                Element elem = fmlFactory.getFrameWorker().insertTopology(returnElement());
                modTracker.insertNewModification(returnElement(), elem);
                setElement(elem);
            }
            Element elem = fmlFactory.getFrameWorker().insertAdjacency(returnElement(), AttributesValues.edge.toString());
            modTracker.insertNewModification(returnElement(), elem);
            setElement(elem);
            insertObserveElement(elem);
        }
    }

    protected void createFormContent(IManagedForm mform) {
        FormToolkit toolkit = mform.getToolkit();
        ScrolledForm scForm = mform.getForm();
        skeleton = new EdgeAdjOverviewForm();
        skeleton.construct_layout(toolkit, scForm);
    }

    private void createEdgeTable() {
        final EdgeAdjOverviewForm form = (EdgeAdjOverviewForm) skeleton;
        form.adjTable.listTable.setLinesVisible(true);
        form.adjTable.listTable.setHeaderVisible(true);
        form.adjTable.listTable.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                buttonBehavior(form.adjTable);
            }
        });
        TableColumn col = new TableColumn(form.adjTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText("Name");
        col.setWidth((int) (form.adjTable.listTable.getSize().x * 0.25));
        form.adjTable.listTable.showColumn(col);
        col = new TableColumn(form.adjTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText("v1");
        col.setWidth((int) (form.adjTable.listTable.getSize().x * 0.18));
        form.adjTable.listTable.showColumn(col);
        col = new TableColumn(form.adjTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText("v2");
        col.setWidth((int) (form.adjTable.listTable.getSize().x * 0.18));
        form.adjTable.listTable.showColumn(col);
        col = new TableColumn(form.adjTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText("Up");
        col.setWidth((int) (form.adjTable.listTable.getSize().x * 0.18));
        form.adjTable.listTable.showColumn(col);
        col = new TableColumn(form.adjTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText("down");
        col.setWidth((int) (form.adjTable.listTable.getSize().x * 0.18));
        form.adjTable.listTable.showColumn(col);
    }

    private void buttonBehavior(GenericTableAndButtons buttons) {
        if (buttons.listTable.getSelectionCount() > 0) {
            buttons.list_edit.setEnabled(true);
            buttons.list_delete.setEnabled(true);
        } else {
            buttons.list_edit.setEnabled(false);
            buttons.list_edit.setEnabled(false);
        }
    }

    private void createEdgeButtons() {
        final EdgeAdjOverviewForm form = (EdgeAdjOverviewForm) skeleton;
        form.adjTable.list_new.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                new EdgeAdjacencyDialog(getParentShell(), returnElement(), UItype.NEW).OpenDialog();
            }
        });
        form.adjTable.list_edit.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (form.adjTable.listTable.getSelectionCount() > 0) {
                    Element selected = ((Element) form.adjTable.listTable.getSelection()[0].getData());
                    new EdgeAdjacencyDialog(getParentShell(), selected, UItype.EDIT).OpenDialog();
                }
            }
        });
        form.adjTable.list_delete.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                deleteItem(form.adjTable.listTable);
            }
        });
    }

    private void deleteItem(Table viewer) {
        if (viewer.getSelectionCount() > 0) {
            Element selected = ((Element) viewer.getSelection()[0].getData());
            DeleteConfirmDialog.deleteConfirmation_XMLModelUpdate(getParentShell(), selected.getParentNode(), selected);
        }
    }

    @Override
    public boolean commit_change() {
        return true;
    }

    search2DEdgeAdj loadEdgeTable;

    @Override
    public void initial_load_UI() {
        createEdgeTable();
        createEdgeButtons();
        final EdgeAdjOverviewForm form = (EdgeAdjOverviewForm) skeleton;
        loadEdgeTable = new search2DEdgeAdj(returnElement());
        GenericSearchResult r = (GenericSearchResult) loadEdgeTable.run();
        r.populateDefaultTable(form.adjTable.listTable);
        r.dispose();
    }

    @Override
    public void refresh_UI() {
        final EdgeAdjOverviewForm form = (EdgeAdjOverviewForm) skeleton;
        form.adjTable.listTable.removeAll();
        GenericSearchResult r = (GenericSearchResult) loadEdgeTable.run();
        r.populateDefaultTable(form.adjTable.listTable);
        r.dispose();
    }
}
