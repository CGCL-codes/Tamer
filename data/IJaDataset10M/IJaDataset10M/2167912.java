package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.MobilePhoneEditor;
import at.rc.tacos.client.ui.admin.editors.MobilePhoneEditorInput;
import at.rc.tacos.platform.model.MobilePhoneDetail;

/**
 * Opens a new editor to create a new mobile phone
 * 
 * @author Michael
 */
public class EditorNewMobilePhoneAction extends Action {

    private Logger log = LoggerFactory.getLogger(EditorNewMobilePhoneAction.class);

    private IWorkbenchWindow window;

    /**
	 * Default class constructor for creating the editor
	 */
    public EditorNewMobilePhoneAction(IWorkbenchWindow window) {
        this.window = window;
    }

    /**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
    @Override
    public String getToolTipText() {
        return "�ffnen ein neues Fenster um ein Mobiltelefon anzulegen.";
    }

    /**
	 * Returns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
    @Override
    public String getText() {
        return "Mobiltelefon hinzuf�gen";
    }

    /**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
    @Override
    public ImageDescriptor getImageDescriptor() {
        return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.mobilePhoneAdd");
    }

    /**
	 * Opens the editor to create the mobile phone
	 */
    @Override
    public void run() {
        IWorkbenchPage page = window.getActivePage();
        try {
            MobilePhoneDetail newPhone = new MobilePhoneDetail();
            MobilePhoneEditorInput input = new MobilePhoneEditorInput(newPhone, true);
            page.openEditor(input, MobilePhoneEditor.ID);
        } catch (PartInitException e) {
            log.error("Failed to create a new phone editor", e);
        }
    }
}
