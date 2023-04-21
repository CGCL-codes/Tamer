package com.ivis.xprocess.ui.draganddrop.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.dnd.DND;
import com.ivis.xprocess.core.Folder;
import com.ivis.xprocess.core.WorkPackage;
import com.ivis.xprocess.core.Xprocess;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.IFolderWrapper;
import com.ivis.xprocess.ui.datawrappers.WorkPackageWrapper;
import com.ivis.xprocess.ui.datawrappers.project.ProjectWrapper;
import com.ivis.xprocess.ui.properties.DragAndDropProgressMessages;
import com.ivis.xprocess.ui.properties.PopupMessages;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory.ChangeEvent;
import com.ivis.xprocess.ui.util.ElementUtil;
import com.ivis.xprocess.ui.util.ProgressMonitorUtil;

/**
 * Drag and drop WorkPackages onto Folder.
 * Validation rules:
 *
 * -only allow WorkPackages within the same project as the priortized
 * group to be dropped.
 *
 * - restrict membership only within the same Pattern, if the folder is
 * in a Process.
 */
public class AddMemberToFolderAction extends MultiAction {

    @Override
    public boolean canDrag(Object dragObject) {
        if (dragObject instanceof WorkPackageWrapper) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canDrop(Object dragObject, Object dropObject) {
        if (dragObject instanceof WorkPackageWrapper && dropObject instanceof IFolderWrapper) {
            WorkPackageWrapper workPackageWrapper = (WorkPackageWrapper) dragObject;
            Xelement xelement = workPackageWrapper.getElement();
            if (!(xelement instanceof WorkPackage)) {
                return false;
            }
            IFolderWrapper folderWrapper = (IFolderWrapper) dropObject;
            Folder folder = (Folder) folderWrapper.getElement();
            if (!folderWrapper.hasMember((WorkPackage) xelement)) {
                if (xelement instanceof Xproject) {
                    if (!xelement.equals(folder.getContainedIn())) {
                        return false;
                    }
                } else {
                    if (xelement instanceof Xtask) {
                        if (!xelement.getContainedIn().equals(folder.getContainedIn())) {
                            return false;
                        }
                        if (folder.getContainedIn() instanceof Xprocess) {
                            IElementWrapper sourcePattern = ElementUtil.getPatternFor(workPackageWrapper);
                            IElementWrapper destinationPattern = ElementUtil.getPatternFor(folderWrapper.getElementWrapper());
                            if ((sourcePattern != null) && (destinationPattern != null)) {
                                if (!sourcePattern.getUuid().equals(destinationPattern.getUuid())) {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                        Xtask task = (Xtask) xelement;
                        if (task.getProject() != null) {
                            if (task == task.getProject().getDelegateTask()) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canDrop(Collection<?> dragObjects, Object dropObject) {
        Iterator<?> dragObjectsIterator = dragObjects.iterator();
        boolean allWorkpackageWrappers = true;
        while (dragObjectsIterator.hasNext()) {
            if (!(dragObjectsIterator.next() instanceof WorkPackageWrapper)) {
                allWorkpackageWrappers = false;
                break;
            }
        }
        return allWorkpackageWrappers;
    }

    @Override
    public int lightWeightCanDrop(Object dragObject, Object dropObject) {
        if (dragObject instanceof WorkPackageWrapper && dropObject instanceof IFolderWrapper) {
            WorkPackageWrapper workPackageWrapper = (WorkPackageWrapper) dragObject;
            Xelement draggedXElement = workPackageWrapper.getElement();
            if (!(draggedXElement instanceof WorkPackage)) {
                return DND.DROP_NONE;
            }
            IFolderWrapper folderWrapper = (IFolderWrapper) dropObject;
            Folder folder = folderWrapper.getFolder();
            if (!folderWrapper.hasMember((WorkPackage) draggedXElement)) {
                if (draggedXElement instanceof Xproject) {
                    if (!draggedXElement.equals(folder.getContainedIn())) {
                        return DND.DROP_NONE;
                    }
                } else {
                    if (draggedXElement instanceof Xtask) {
                        if (!draggedXElement.getContainedIn().equals(folder.getContainedIn())) {
                            return DND.DROP_NONE;
                        }
                        if (folder.hasImplicitMembership()) {
                            return DND.DROP_NONE;
                        }
                        if (folder.getContainedIn() instanceof Xprocess) {
                            IElementWrapper sourcePattern = ElementUtil.getPatternFor(workPackageWrapper);
                            IElementWrapper destinationPattern = ElementUtil.getPatternFor(folderWrapper.getElementWrapper());
                            if ((sourcePattern != null) && (destinationPattern != null)) {
                                if (!sourcePattern.getUuid().equals(destinationPattern.getUuid())) {
                                    return DND.DROP_NONE;
                                }
                            } else {
                                return DND.DROP_NONE;
                            }
                        }
                        Xtask task = (Xtask) draggedXElement;
                        if (task.getProject() != null) {
                            if (task == task.getProject().getDelegateTask()) {
                                return DND.DROP_NONE;
                            }
                        }
                        return DND.DROP_LINK;
                    }
                }
            }
            if (dragObject instanceof IElementWrapper && ((IElementWrapper) dragObject).getElement() instanceof Xtask) {
                Xtask task = (Xtask) ((IElementWrapper) dragObject).getElement();
                for (WorkPackage wp : folder.getWorkpackageMembers()) {
                    for (Xtask t : wp.getAllTasks()) {
                        if (t.equals(task)) {
                            return DND.DROP_NONE;
                        }
                    }
                }
                return DND.DROP_LINK;
            }
            if (dragObject instanceof ProjectWrapper) {
                return DND.DROP_LINK;
            }
        }
        return DND.DROP_NONE;
    }

    @Override
    public String getItemText(Object dragObject, Object dropObject) {
        IElementWrapper elementWrapper = (IElementWrapper) dragObject;
        String text = PopupMessages.generic_add_prefix + " " + elementWrapper.getLabel() + " " + PopupMessages.add_member_to_folder_membership_suffix;
        return text;
    }

    @Override
    public void doAction(Object dragObject, Object dropObject) {
        if (dropObject instanceof IFolderWrapper) {
            IFolderWrapper folderWrapper = (IFolderWrapper) dropObject;
            addTaskAsMember(folderWrapper, dragObject);
        }
        ChangeEventFactory.saveChanges();
        ChangeEventFactory.stopChangeRecording();
    }

    private void addTaskAsMember(IFolderWrapper folderWrapper, Object dragObject) {
        Folder folder = folderWrapper.getFolder();
        if (dragObject instanceof WorkPackageWrapper) {
            WorkPackageWrapper workPackageWrapper = (WorkPackageWrapper) dragObject;
            WorkPackage workPackage = (WorkPackage) workPackageWrapper.getElement();
            if (workPackage instanceof Xtask) {
                Xtask task = (Xtask) workPackage;
                if ((task.getParent() != null) && task.getParent().equals(task.getProject())) {
                    workPackage = task.getParent();
                }
            }
            folder.addWorkPackageMember(workPackage);
            IElementWrapper elementWrapper = ChangeEventFactory.startChangeRecording(folderWrapper.getElement());
            ChangeEventFactory.addChange(elementWrapper, ChangeEvent.PRIORITIZEDGROUP_MEMBERSHIP_CHANGE);
            ChangeEventFactory.addChange(elementWrapper, ChangeEvent.NEW_CHILD);
            ChangeEventFactory.setSelectElementWrapper(elementWrapper, false);
        }
    }

    @Override
    public String getItemText(Collection<?> dragObjects, Object dropObject) {
        return PopupMessages.add_multiple_items_to_folder_membership;
    }

    @Override
    public void doAction(final Collection<?> dragObjects, final Object dropObject) {
        if (dropObject instanceof IFolderWrapper) {
            final IFolderWrapper folderWrapper = (IFolderWrapper) dropObject;
            IRunnableWithProgress run = new IRunnableWithProgress() {

                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    for (Object dragObject : dragObjects) {
                        addTaskAsMember(folderWrapper, dragObject);
                        monitor.worked(1);
                    }
                }
            };
            ProgressMonitorUtil.runWithProgressMonitor(DragAndDropProgressMessages.add_members_to_folder_progress_title, dragObjects.size(), run);
        }
    }
}
