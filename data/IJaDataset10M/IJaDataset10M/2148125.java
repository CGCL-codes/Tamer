package at.rc.tacos.client.controller;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import at.rc.tacos.factory.ImageFactory;

public class EditorSaveAction extends Action implements IWorkbenchWindowActionDelegate {

    /**
	 * Default class constructor for saving changes
	 */
    public EditorSaveAction() {
    }

    /**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
    @Override
    public String getToolTipText() {
        return "Speichert alle �nderungen die vorgenommen worden sind.";
    }

    /**
	 * Returns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
    @Override
    public String getText() {
        return "�nderungen speichern";
    }

    /**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
    @Override
    public ImageDescriptor getImageDescriptor() {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("admin.save");
    }

    /**
	 * Shows the view to edit a vehicle
	 */
    @Override
    public void run() {
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        editor.doSave(Job.getJobManager().createProgressGroup());
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
