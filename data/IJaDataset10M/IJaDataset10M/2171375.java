package org.parallelj.mda.controlflow.diagram.extension.edit.parts;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;
import org.parallelj.mda.controlflow.diagram.edit.parts.AtomicTaskNameEditPart;
import org.parallelj.mda.controlflow.diagram.extension.ControlFlowDiagramExtendedEditorPlugin;
import org.parallelj.mda.controlflow.diagram.extension.adapters.TaskNameAdapter;
import org.parallelj.mda.controlflow.diagram.extension.adapters.TaskNameRefreshmentAdapter;
import org.parallelj.mda.controlflow.model.controlflow.Task;
import org.parallelj.mda.controlflow.model.controlflow.TaskVisibility;
import org.parallelj.mda.controlflow.model.edit.ControlflowEditPlugin;

/**
 * Extension of Atomic Task Name EditPart, to add custom adapters.
 * 
 * @author Atos Worldline
 * 
 */
public class ExtendedAtomicTaskNameEditPart extends AtomicTaskNameEditPart {

    public ExtendedAtomicTaskNameEditPart(View view) {
        super(view);
        this.addAdapters(view);
    }

    /**
	 * Adds adapters on view associated with Edit Part
	 * 
	 * @param view
	 */
    private void addAdapters(View view) {
        EObject element = view.getElement();
        if (element != null) {
            element.eAdapters().add(new TaskNameAdapter(this));
            element.eAdapters().add(new TaskNameRefreshmentAdapter(this));
        }
    }

    private static final String AT_PUBLIC_IMAGE_PATH = "/icons/full/obj16/AtomicTaskPUBLIC.gif";

    private static final String AT_PRIVATE_IMAGE_PATH = "/icons/full/obj16/AtomicTaskPRIVATE.gif";

    @Override
    protected Image getLabelIcon() {
        Task task = (Task) ((View) getModel()).getElement();
        Image image = null;
        if (task != null) {
            if (task.getVisibility() == TaskVisibility.PUBLIC) image = ControlFlowDiagramExtendedEditorPlugin.getDefault().getImage(ControlflowEditPlugin.INSTANCE.getSymbolicName(), AT_PUBLIC_IMAGE_PATH); else if (task.getVisibility() == TaskVisibility.PRIVATE) image = ControlFlowDiagramExtendedEditorPlugin.getDefault().getImage(ControlflowEditPlugin.INSTANCE.getSymbolicName(), AT_PRIVATE_IMAGE_PATH);
        }
        return image != null ? image : super.getLabelIcon();
    }
}
