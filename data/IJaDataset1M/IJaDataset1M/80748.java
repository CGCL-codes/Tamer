package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.VehicleDetailEditor;
import at.rc.tacos.client.ui.admin.editors.VehicleDetailEditorInput;
import at.rc.tacos.platform.model.VehicleDetail;

/**
 * Opens a editor to create a new vehicle
 * 
 * @author Michael
 */
public class EditorNewVehicleAction extends Action {

    private Logger log = LoggerFactory.getLogger(EditorNewVehicleAction.class);

    private IWorkbenchWindow window;

    /**
	 * Default class constructor for creating the editor
	 */
    public EditorNewVehicleAction(IWorkbenchWindow window) {
        this.window = window;
    }

    /**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
    @Override
    public String getToolTipText() {
        return "�ffnen ein neues Fenster um ein Fahrzeug anzulegen.";
    }

    /**
	 * Returns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
    @Override
    public String getText() {
        return "Neues Fahrzeug hinzuf�gen";
    }

    /**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
    @Override
    public ImageDescriptor getImageDescriptor() {
        return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.vehicleAdd");
    }

    /**
	 * Opens the editor to create the vehicle
	 */
    @Override
    public void run() {
        IWorkbenchPage page = window.getActivePage();
        try {
            VehicleDetail newVehicle = new VehicleDetail();
            VehicleDetailEditorInput input = new VehicleDetailEditorInput(newVehicle, true);
            page.openEditor(input, VehicleDetailEditor.ID);
        } catch (PartInitException e) {
            log.error("Failed to create a new vehicle editor", e);
        }
    }
}
