package edu.gsbme.wasabi.UI.Dialog.FML.Cell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.MMLParser2.Vocabulary.FML;
import edu.gsbme.wasabi.UI.UItype;
import edu.gsbme.wasabi.UI.Algorithm.DeleteConfirmDialog;
import edu.gsbme.wasabi.UI.DataStruct.ModificationTracker;
import edu.gsbme.wasabi.UI.Dialog.MFormDialog;
import edu.gsbme.wasabi.UI.Forms.FML.Cells.GenericCellTreeListForm;

/**
 * 
 * This Dialog provides a tree representation of a Cell List XML data structure.
 * It displays the XML structure of the children of the input element
 * 
 * This dialog has manipulation functionality for Cell_List type elements and cell object elements only.
 * Other elements will only have display functionality
 * 
 * The buttons action will be tied to the parent element tag name.
 * 
 * This dialog is of UIType.edit or UIType.overview only
 * 
 * This is an generic viewer, it can't creating elements for <cell_list> etc
 * 
 * 
 * @author David
 *
 */
public class GenericCellTreeListDialog extends MFormDialog {

    public enum ElementStructureType {

        DimStruct {

            @Override
            public int getLevel() {
                return 0;
            }
        }
        , CellListStruct {

            @Override
            public int getLevel() {
                return 1;
            }
        }
        , CellStruct {

            @Override
            public int getLevel() {
                return 2;
            }
        }
        , ParameterStruct {

            @Override
            public int getLevel() {
                return 3;
            }
        }
        ;

        public abstract int getLevel();
    }

    public static void main(String[] arg) {
        new GenericCellTreeListDialog(null, null, null, null).OpenDialog();
    }

    ElementStructureType element_type;

    public static String id = "edu.gsbme.wasabi.UI.Dialog.FML.Cell.GenericCellTreeListDialog";

    public GenericCellTreeListDialog(Shell shell, Element element, UItype type, ElementStructureType elem_type) {
        super(shell, element, type);
        x = 400;
        y = 500;
        this.element_type = elem_type;
    }

    public GenericCellTreeListDialog(Shell shell, Element element, UItype type, ModificationTracker tracker, ElementStructureType elem_type) {
        super(shell, element, type, tracker);
        x = 400;
        y = 500;
        this.element_type = elem_type;
    }

    protected void createFormContent(IManagedForm mform) {
        FormToolkit toolkit = mform.getToolkit();
        ScrolledForm scForm = mform.getForm();
        skeleton = new GenericCellTreeListForm();
        skeleton.construct_layout(toolkit, scForm);
    }

    private void createButtonActions() {
        final GenericCellTreeListForm form = (GenericCellTreeListForm) skeleton;
        form.browse.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
            }
        });
        form.hdf5_enable.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (form.hdf5_enable.getSelection()) {
                    form.treeviewer.list_new.setEnabled(false);
                    form.treeviewer.list_edit.setEnabled(false);
                    form.treeviewer.list_delete.setEnabled(false);
                    form.treeviewer.listTreeviewer.getTree().removeSelectionListener(form.treeviewer.defaultButtonAction);
                    form.hdf5.setEnabled(true);
                    form.browse.setEnabled(true);
                } else {
                    form.treeviewer.list_new.setEnabled(true);
                    form.treeviewer.list_edit.setEnabled(true);
                    form.treeviewer.list_delete.setEnabled(true);
                    form.treeviewer.listTreeviewer.getTree().addSelectionListener(form.treeviewer.defaultButtonAction);
                    form.hdf5.setEnabled(false);
                    form.browse.setEnabled(false);
                }
            }
        });
        form.treeviewer.list_new.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                GenericCellTreeListForm form = (GenericCellTreeListForm) skeleton;
                if (form.treeviewer.listTreeviewer.getTree().getSelectionCount() > 0) {
                    TreeItem[] items = form.treeviewer.listTreeviewer.getTree().getSelection();
                    int relpos = getItemDepth(items[0]);
                    if (element_type == ElementStructureType.CellListStruct) {
                        if (relpos == 1) {
                            Element selected = ((Element) form.treeviewer.listTreeviewer.getTree().getSelection()[0].getData());
                            if (selected.getTagName().equals(FML.bezier_curve_list.toString())) {
                                new BezierCurveDialog(getShell(), returnElement(), UItype.NEW).OpenDialog();
                            } else if (selected.getTagName().equals(FML.bezier_surface_list.toString())) {
                                new BezierSurfaceDialog(getShell(), returnElement(), UItype.NEW).OpenDialog();
                            } else new GenericCellDialog(getShell(), returnElement(), UItype.NEW, "").OpenDialog();
                        }
                    } else if (element_type == ElementStructureType.CellStruct) {
                    }
                } else {
                    if (element_type == ElementStructureType.CellListStruct) {
                        new GenericCellDialog(getShell(), returnElement(), UItype.NEW, "").OpenDialog();
                    } else if (element_type == ElementStructureType.CellStruct) {
                    }
                }
            }
        });
        form.treeviewer.list_edit.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                GenericCellTreeListForm form = (GenericCellTreeListForm) skeleton;
                if (form.treeviewer.listTreeviewer.getTree().getSelectionCount() > 0) {
                    Element selected = ((Element) form.treeviewer.listTreeviewer.getTree().getSelection()[0].getData());
                    if (element_type == ElementStructureType.CellListStruct) {
                        if (selected.getTagName().equals(FML.bezier_curve.toString())) {
                            new BezierCurveDialog(getShell(), selected, UItype.EDIT).OpenDialog();
                        } else if (selected.getTagName().equals(FML.bezier_surface.toString())) {
                            new BezierSurfaceDialog(getShell(), selected, UItype.EDIT).OpenDialog();
                        } else new GenericCellDialog(getShell(), selected, UItype.EDIT, "").OpenDialog();
                    } else if (element_type == ElementStructureType.CellStruct) {
                    }
                }
            }
        });
        form.treeviewer.list_delete.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                GenericCellTreeListForm form = (GenericCellTreeListForm) skeleton;
                if (form.treeviewer.listTreeviewer.getTree().getSelectionCount() > 0) {
                    Element selected = ((Element) form.treeviewer.listTreeviewer.getTree().getSelection()[0].getData());
                    DeleteConfirmDialog.deleteConfirmation(getParentShell(), selected.getParentNode(), selected);
                }
            }
        });
    }

    @Override
    public boolean commit_change() {
        return true;
    }

    @Override
    public void initial_load_UI() {
        createButtonActions();
        GenericCellTreeListForm form = (GenericCellTreeListForm) skeleton;
        form.treeviewer.listTreeviewer.setContentProvider(new CellTreeContentProvider());
        form.treeviewer.listTreeviewer.setLabelProvider(new CellTreeLabelProvider());
        form.treeviewer.listTreeviewer.setInput(returnElement());
        form.treeviewer.listTreeviewer.expandAll();
        if (element_type == ElementStructureType.CellListStruct) {
        } else if (element_type == ElementStructureType.CellStruct) {
            form.treeviewer.list_new.setEnabled(false);
            form.treeviewer.list_edit.setEnabled(false);
        }
        if (returnElement().hasAttribute(Attributes.ext_src.toString())) {
            setEditableFlag(false);
            form.hdf5.setText(returnElement().getAttribute(Attributes.ext_src.toString()));
            form.hdf5_enable.setSelection(true);
        }
    }

    private void setEditale(boolean editable) {
        if (isEditable() != editable) {
            setEditableFlag(editable);
            if (editable) {
                setAllowEdit();
            } else {
                setDisableEdit();
            }
        }
    }

    private void setAllowEdit() {
        GenericCellTreeListForm form = (GenericCellTreeListForm) skeleton;
    }

    private void setDisableEdit() {
        GenericCellTreeListForm form = (GenericCellTreeListForm) skeleton;
    }

    @Override
    public void refresh_UI() {
        GenericCellTreeListForm form = (GenericCellTreeListForm) skeleton;
        form.treeviewer.listTreeviewer.setInput(returnElement());
    }

    private int getItemDepth(TreeItem item) {
        if (item == null) return 0; else {
            TreeItem parent = item.getParentItem();
            if (parent == null) return 1; else {
                return 1 + getItemDepth(parent);
            }
        }
    }
}
