package edu.gsbme.wasabi.UI.Dialog.FML.Mesh;

import ncsa.hdf.object.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.FML.Search.FMLSearch.FMLSource;
import edu.gsbme.MMLParser2.FML.VirtualTree.cTreeNodes;
import edu.gsbme.MMLParser2.Factory.FMLFactory;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.wasabi.UI.UItext;
import edu.gsbme.wasabi.UI.UItype;
import edu.gsbme.wasabi.UI.Algorithm.FML.searchMeshElemList;
import edu.gsbme.wasabi.UI.Algorithm.data.GenericSearchResult;
import edu.gsbme.wasabi.UI.Dialog.MFormDialog;
import edu.gsbme.wasabi.UI.Dialog.HDF5.HDF5ViewerDialog;
import edu.gsbme.wasabi.UI.Dialog.Imports.ImportBrowserDialog;
import edu.gsbme.wasabi.UI.Forms.FML.MeshElementForm;

/**
 * Mesh element list dialog
 * @author David
 *
 */
public class MeshElemListDialog extends MFormDialog {

    cTreeNodes node;

    public MeshElemListDialog(Shell shell) {
        super(shell);
    }

    public MeshElemListDialog(Shell shell, Element element, UItype type) {
        super(shell, element, type);
        x = 600;
        y = 500;
        FMLFactory fmlFactory = new FMLFactory("", returnActiveEditorModel());
        if (type == UItype.NEW) {
            Element elem = fmlFactory.getFrameWorker().insertElementListTag(returnElement());
            modTracker.insertNewModification(returnElement(), elem);
            setElement(elem);
            insertObserveElement(elem);
        }
    }

    public MeshElemListDialog(Shell shell, cTreeNodes node, UItype type) {
        super(shell, null, type);
        x = 600;
        y = 500;
        this.node = node;
        if (node.getSource() == FMLSource.XML) {
            setElement((Element) node.getReferneceObject());
            insertObserveElement(returnElement());
            if (type == UItype.NEW) {
                FMLFactory fmlFactory = new FMLFactory("", returnActiveEditorModel());
                Element elem = fmlFactory.getFrameWorker().insertEdgeListTag(returnElement());
                modTracker.insertNewModification(returnElement(), elem);
                setElement(elem);
                insertObserveElement(elem);
            }
        }
    }

    protected void createFormContent(IManagedForm mform) {
        FormToolkit toolkit = mform.getToolkit();
        ScrolledForm scForm = mform.getForm();
        skeleton = new MeshElementForm();
        skeleton.construct_layout(toolkit, scForm);
    }

    private void setupActions() {
        final MeshElementForm form = (MeshElementForm) skeleton;
        form.hdf5_enable.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (form.hdf5_enable.getSelection()) {
                    setEditable(false);
                    form.hdf5.setEnabled(true);
                    form.browse.setEnabled(true);
                } else {
                    setEditable(true);
                    form.hdf5.setEnabled(false);
                    form.browse.setEnabled(false);
                }
            }
        });
        form.browse.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ImportBrowserDialog browser = new ImportBrowserDialog(getParentShell(), SWT.SINGLE, ImportBrowserDialog.HDF5);
                browser.OpenDialog();
                if (browser.results.length > 0) {
                    Element import_id = ((FMLFactory) returnActiveEditorModel().getFactory()).getImportWorker().returnImportTag(browser.results[0]);
                    HDF5ViewerDialog viewer = new HDF5ViewerDialog(getParentShell(), import_id, UItype.OVERVIEW);
                    viewer.OpenDialog();
                    form.hdf5.setText(viewer.getPathSelection());
                    refresh_UI();
                }
            }
        });
    }

    private void createTables() {
        final MeshElementForm form = (MeshElementForm) skeleton;
        form.listTable.listTable.setLinesVisible(true);
        form.listTable.listTable.setHeaderVisible(true);
        TableColumn col = new TableColumn(form.listTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText("Element type");
        col.setWidth((int) (form.listTable.listTable.getSize().x * 0.3));
        form.listTable.listTable.showColumn(col);
        col = new TableColumn(form.listTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText(UItext.pt1);
        col.setWidth((int) (form.listTable.listTable.getSize().x * 0.1));
        form.listTable.listTable.showColumn(col);
        col = new TableColumn(form.listTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText(UItext.pt2);
        col.setWidth((int) (form.listTable.listTable.getSize().x * 0.1));
        form.listTable.listTable.showColumn(col);
        col = new TableColumn(form.listTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText(UItext.pt3);
        col.setWidth((int) (form.listTable.listTable.getSize().x * 0.1));
        form.listTable.listTable.showColumn(col);
        col = new TableColumn(form.listTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText(UItext.pt4);
        col.setWidth((int) (form.listTable.listTable.getSize().x * 0.1));
        form.listTable.listTable.showColumn(col);
        col = new TableColumn(form.listTable.listTable, SWT.LEFT);
        col.setResizable(true);
        col.setText(UItext.domain);
        col.setWidth((int) (form.listTable.listTable.getSize().x * 0.25));
        form.listTable.listTable.showColumn(col);
    }

    searchMeshElemList loadTable;

    @Override
    public boolean commit_change() {
        MeshElementForm form = (MeshElementForm) skeleton;
        if (isEditable()) {
            if (form.hdf5_enable.getSelection()) {
                if (returnElement() != null) {
                    returnElement().setAttribute(Attributes.ext_src.toString(), form.hdf5.getText().trim());
                }
            }
        }
        return true;
    }

    @Override
    public void initial_load_UI() {
        MeshElementForm form = (MeshElementForm) skeleton;
        createTables();
        setupActions();
        if (node != null) {
            if (node.getSource() == FMLSource.XML) {
                loadTable = new searchMeshElemList((Element) node.getReferneceObject());
                GenericSearchResult r = (GenericSearchResult) loadTable.run();
                r.populateDefaultTable(form.listTable.listTable);
                r.dispose();
                loadTable.dispose();
            } else if (node.getSource() == FMLSource.HDF5) {
                setHDF5Input(false);
                setEditable(false);
                loadTable = new searchMeshElemList(node.h5parser, (Group) node.getReferneceObject());
                GenericSearchResult r = (GenericSearchResult) loadTable.run();
                r.populateDefaultTable(form.listTable.listTable);
                r.dispose();
                loadTable.dispose();
            }
        } else {
            loadTable = new searchMeshElemList(returnElement());
            GenericSearchResult r = (GenericSearchResult) loadTable.run();
            r.populateDefaultTable(form.listTable.listTable);
            r.dispose();
            loadTable.dispose();
        }
    }

    @Override
    public void refresh_UI() {
        MeshElementForm form = (MeshElementForm) skeleton;
        form.listTable.listTable.removeAll();
        if (node != null) {
            if (node.getSource() == FMLSource.XML) {
                loadTable = new searchMeshElemList((Element) node.getReferneceObject());
                GenericSearchResult r = (GenericSearchResult) loadTable.run();
                r.populateDefaultTable(form.listTable.listTable);
                r.dispose();
                loadTable.dispose();
            } else if (node.getSource() == FMLSource.HDF5) {
                loadTable = new searchMeshElemList(node.h5parser, (Group) node.getReferneceObject());
                GenericSearchResult r = (GenericSearchResult) loadTable.run();
                r.populateDefaultTable(form.listTable.listTable);
                r.dispose();
                loadTable.dispose();
            }
        } else {
            loadTable = new searchMeshElemList(returnElement());
            GenericSearchResult r = (GenericSearchResult) loadTable.run();
            r.populateDefaultTable(form.listTable.listTable);
            r.dispose();
            loadTable.dispose();
        }
    }

    private void setHDF5Input(boolean editable) {
        MeshElementForm form = (MeshElementForm) skeleton;
        form.hdf5.setEnabled(editable);
        form.hdf5_enable.setEnabled(editable);
        form.browse.setEnabled(editable);
    }

    private void setEditable(boolean editable) {
        final MeshElementForm form = (MeshElementForm) skeleton;
        super.setEditableFlag(editable);
        if (editable) {
            form.listTable.listTable.addSelectionListener(form.listTable.tableBehavior);
            form.listTable.list_new.setEnabled(true);
            if (form.listTable.listTable.getSelectionCount() > 0) form.listTable.list_edit.setEnabled(true); else form.listTable.list_edit.setEnabled(false);
            if (form.listTable.listTable.getSelectionCount() > 0) form.listTable.list_delete.setEnabled(true); else form.listTable.list_delete.setEnabled(false);
        } else {
            form.listTable.listTable.removeSelectionListener(form.listTable.tableBehavior);
            form.listTable.list_new.setEnabled(false);
            form.listTable.list_edit.setEnabled(false);
            form.listTable.list_delete.setEnabled(false);
        }
    }
}
