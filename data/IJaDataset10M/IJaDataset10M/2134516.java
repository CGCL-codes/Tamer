package org.mxeclipse.business.table.user;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.mxeclipse.model.MxTreeBusiness;
import org.mxeclipse.model.MxTreePerson;
import org.mxeclipse.model.MxTreeTrigger;
import org.mxeclipse.model.MxTreeStateUserAccess;
import org.mxeclipse.model.MxTreeUser;
import org.mxeclipse.model.MxTreeWeb;
import org.mxeclipse.views.MxEclipseBusinessView;

/**
 * This class implements an ICellModifier
 * An ICellModifier is called when the user modifes a cell in the
 * tableViewer
 */
public class MxUserCellModifier implements ICellModifier {

    MxUserComposite composite;

    /**
    * Constructor
    * @param TableViewerExample an instance of a TableViewerExample
    */
    public MxUserCellModifier(MxUserComposite composite) {
        super();
        this.composite = composite;
    }

    /**
    * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
    */
    public boolean canModify(Object element, String property) {
        if (element instanceof MxTreeStateUserAccess) {
            MxTreeStateUserAccess userAccess = (MxTreeStateUserAccess) element;
            return userAccess.getUserBasicType().equals(MxTreeStateUserAccess.ACCESS_USER);
        } else if (element instanceof MxTreeUser) {
            return true;
        } else {
            return false;
        }
    }

    /**
    * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
    */
    public Object getValue(Object element, String property) {
        Object result = "";
        String userType = null;
        String userName = null;
        if (element == null) return -1;
        if (element instanceof MxTreeStateUserAccess) {
            MxTreeStateUserAccess userAccess = (MxTreeStateUserAccess) element;
            if (userAccess.getUser() == null) return -1;
            userType = userAccess.getUserType();
            userName = userAccess.getName();
        } else if (element instanceof MxTreeUser) {
            MxTreeUser user = (MxTreeUser) element;
            userType = user.getType();
            userName = user.getName();
        } else {
            return -1;
        }
        try {
            if (property.equals(MxUserComposite.COLUMN_TYPE)) {
                result = -1;
                for (int i = 0; i < MxTreeUser.ALL_USER_TYPES.length; i++) {
                    if (MxTreeUser.ALL_USER_TYPES[i].equals(userType)) {
                        result = i;
                        break;
                    }
                }
            } else if (property.equals(MxUserComposite.COLUMN_NAME)) {
                result = -1;
                ComboBoxCellEditor ed = (ComboBoxCellEditor) composite.editors[MxUserComposite.COL_INDEX_NAME];
                ed.setItems(MxTreeUser.getAllUserNames(false, userType));
                for (int i = 0; i < MxTreeUser.getAllUserNames(false, userType).length; i++) {
                    if (MxTreeUser.getAllUserNames(false, userType)[i].equals(userName)) {
                        result = i;
                        break;
                    }
                }
            } else if (element instanceof MxTreeUser) {
                if (userName.length() > 0) {
                    IWorkbench workbench = PlatformUI.getWorkbench();
                    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
                    IWorkbenchPage page = window.getActivePage();
                    try {
                        MxEclipseBusinessView view = (MxEclipseBusinessView) page.showView("org.mxeclipse.views.MxEclipseBusinessView");
                        TreeItem[] selected = view.treObjects.getSelection();
                        if (selected.length > 0) {
                            view.expandItem(selected[0]);
                            for (TreeItem subItem : selected[0].getItems()) {
                                if (subItem.getData() instanceof MxTreeUser) {
                                    MxTreeUser subPerson = (MxTreeUser) subItem.getData();
                                    if (subPerson.getName().equals(userName)) {
                                        view.nodeSelected(subItem);
                                        view.treObjects.setSelection(subItem);
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (PartInitException e) {
                        MessageDialog.openError(composite.getShell(), "User selection", "Error when trying to select a user! " + e.getMessage());
                    }
                }
            } else {
                String triggerObjectNames = userName;
                result = "";
            }
        } catch (Exception ex) {
            MessageDialog.openError(composite.getShell(), "Trigger retrieval", "Error when retrieving a trigger properties for editing!");
        }
        return result;
    }

    /**
    * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
    */
    public void modify(Object element, String property, Object value) {
        TableItem item = (TableItem) element;
        boolean bChanged = false;
        if (item.getData() instanceof MxTreeStateUserAccess) {
            MxTreeStateUserAccess userAccess = (MxTreeStateUserAccess) item.getData();
            try {
                if (property.equals(MxUserComposite.COLUMN_TYPE)) {
                    int nValue = ((Integer) value).intValue();
                    if (nValue >= 0) {
                        userAccess.setUserType(MxTreeUser.ALL_USER_TYPES[nValue]);
                        bChanged = true;
                    }
                } else if (property.equals(MxUserComposite.COLUMN_NAME)) {
                    int nValue = ((Integer) value).intValue();
                    if (nValue >= 0) {
                        userAccess.setName(MxTreeUser.getAllUserNames(false, userAccess.getUserType())[nValue]);
                        if (userAccess.getUserType().equals("")) {
                            userAccess.setUserBasicType(userAccess.getName());
                        }
                        bChanged = true;
                    }
                }
            } catch (Exception ex) {
                MessageDialog.openInformation(composite.getShell(), "State User Access Configuration", "Error when editing value. Please give a correct value!");
            }
        } else if (item.getData() instanceof MxTreeUser) {
            MxTreeUser user = (MxTreeUser) item.getData();
            try {
                if (property.equals(MxUserComposite.COLUMN_TYPE)) {
                    int nValue = ((Integer) value).intValue();
                    if (nValue >= 0) {
                        user.setType(MxTreeUser.ALL_USER_TYPES[nValue]);
                        bChanged = true;
                    }
                } else if (property.equals(MxUserComposite.COLUMN_NAME)) {
                    int nValue = ((Integer) value).intValue();
                    if (nValue >= 0) {
                        user.setName(MxTreeUser.getAllUserNames(false, user.getType())[nValue]);
                        bChanged = true;
                    }
                } else if (property.equals(MxUserComposite.COLUMN_SELECT)) {
                }
            } catch (Exception ex) {
                MessageDialog.openInformation(composite.getShell(), "User Access Configuration", "Error when editing value. Please give a correct value!");
            }
        }
        if (bChanged) {
            composite.getBusiness().propertyChanged((MxTreeBusiness) item.getData());
        }
    }
}
