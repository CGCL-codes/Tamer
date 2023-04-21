package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.LocationEditor;
import at.rc.tacos.client.ui.admin.editors.LocationEditorInput;
import at.rc.tacos.platform.model.Location;

/**
 * Opens an empty editor to create a new location
 * 
 * @author Michael
 */
public class EditorNewLocationAction extends Action {

    private Logger log = LoggerFactory.getLogger(EditorNewLocationAction.class);

    private IWorkbenchWindow window;

    /**
	 * Default class constructor for creating the editor
	 */
    public EditorNewLocationAction(IWorkbenchWindow window) {
        this.window = window;
    }

    /**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
    @Override
    public String getToolTipText() {
        return "�ffnen ein neues Fenster um eine Dienststelle anzulegen.";
    }

    /**
	 * Returns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
    @Override
    public String getText() {
        return "Dienststelle hinzuf�gen";
    }

    /**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
    @Override
    public ImageDescriptor getImageDescriptor() {
        return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.locationAdd");
    }

    /**
	 * Opens the editor to create the location
	 */
    @Override
    public void run() {
        IWorkbenchPage page = window.getActivePage();
        try {
            Location newLocation = new Location();
            LocationEditorInput input = new LocationEditorInput(newLocation, true);
            page.openEditor(input, LocationEditor.ID);
        } catch (PartInitException e) {
            log.error("Failed to create a new location editor", e);
        }
    }
}
