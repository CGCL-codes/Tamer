package com.comarch.depth.project;

import java.util.Iterator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;
import de.tu.depth.fragments.Chapter;
import de.tu.depth.fragments.Fragment;
import de.tu.depth.fragments.Tutorial;
import depth.IRepository;
import depth.RepositoryFactory;
import depth.exceptions.DepthCrossProjectRelationException;
import depth.exceptions.DepthFragmentAlreadyExistsException;

/**
 * This class is used for D&D whenever something is dragged upon a Tutorial
 * element in the project view.
 */
public class TutorialDropAssistant extends CommonDropAdapterAssistant {

    private IRepository repository = RepositoryFactory.getRepository();

    /**
	 * This method is called whenever the user drags an object over this project
	 * view. The mouse cursor will adapt to the return value of this method.
	 */
    @Override
    public IStatus validateDrop(Object target, int operation, TransferData transferType) {
        if (!(target instanceof Tutorial)) {
            return Status.CANCEL_STATUS;
        }
        TreeSelection selection = (TreeSelection) LocalSelectionTransfer.getInstance().nativeToJava(transferType);
        Iterator it = selection.iterator();
        while (it.hasNext()) {
            Object source = it.next();
            if (!(source instanceof Chapter)) {
                return Status.CANCEL_STATUS;
            }
            if (repository.canContain((Fragment) target, (Fragment) source)) {
                return Status.CANCEL_STATUS;
            }
        }
        return Status.OK_STATUS;
    }

    /**
	 * This method is called, when the user drops an element. If the user drops
	 * a Chapter node on a Tutorial node, append this as child of the Tutorial.
	 */
    @Override
    public IStatus handleDrop(CommonDropAdapter aDropAdapter, DropTargetEvent aDropTargetEvent, Object aTarget) {
        IRepository repository = RepositoryFactory.getRepository();
        if (aDropTargetEvent.data instanceof TreeSelection) {
            TreeSelection treeSelection = (TreeSelection) aDropTargetEvent.data;
            if (aTarget instanceof Tutorial) {
                Tutorial tutorial = (Tutorial) aTarget;
                Object[] selection = treeSelection.toArray();
                for (int i = 0; i < selection.length; i++) {
                    if (selection[i] instanceof Chapter) {
                        Chapter dragSource = (Chapter) selection[i];
                        if (!tutorial.getParent().getParent().equals(dragSource.getParent().getParent())) {
                            continue;
                        }
                        try {
                            repository.addChapterToTutorial(tutorial, dragSource);
                        } catch (DepthFragmentAlreadyExistsException e) {
                            MessageDialog.openError(Display.getDefault().getActiveShell(), "Chapter already exists", "A chapter with the same name is already part of this tutorial.");
                        } catch (DepthCrossProjectRelationException e) {
                            MessageDialog.openError(Display.getDefault().getActiveShell(), "Chapter of different project", "It's not possible to append a chapter of a different project to this tutorial.");
                        }
                    }
                }
                return Status.OK_STATUS;
            }
        }
        return Status.CANCEL_STATUS;
    }
}
