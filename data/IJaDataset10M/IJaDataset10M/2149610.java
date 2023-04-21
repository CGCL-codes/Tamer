package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.AddressEditor;
import at.rc.tacos.client.ui.admin.editors.AddressEditorInput;
import at.rc.tacos.platform.model.Address;

/**
 * Opens a new editor to create a new address record
 * 
 * @author Michael
 */
public class EditorNewAddressAction extends Action {

    private Logger log = LoggerFactory.getLogger(EditorNewAddressAction.class);

    private IWorkbenchWindow window;

    /**
	 * Default class constructor for creating the editor
	 */
    public EditorNewAddressAction(IWorkbenchWindow window) {
        this.window = window;
    }

    /**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
    @Override
    public String getToolTipText() {
        return "�ffnen ein neues Fenster um eine Adresse anzulegen";
    }

    /**
	 * Retruns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
    @Override
    public String getText() {
        return "Adresse hinzuf�gen";
    }

    /**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
    @Override
    public ImageDescriptor getImageDescriptor() {
        return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.addressAdd");
    }

    /**
	 * Opens the editor to create the competence
	 */
    @Override
    public void run() {
        IWorkbenchPage page = window.getActivePage();
        try {
            Address newAddress = new Address();
            AddressEditorInput input = new AddressEditorInput(newAddress, true);
            page.openEditor(input, AddressEditor.ID);
        } catch (PartInitException e) {
            log.error("Failed to create a new address editor", e);
        }
    }
}
