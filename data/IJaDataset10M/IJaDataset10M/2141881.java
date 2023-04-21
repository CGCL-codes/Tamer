package org.jcryptool.core.operations.editors;

import java.io.InputStream;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * The EditorsManager class is a singleton. 
 * It provides a global point of access to the registered 
 * editor services. 
 * 
 * The class has only instance. 
 * 
 * The registered editor services are collected in a hash map. 
 * The keys of the hash map are the IDs of the editors and the appropriate values 
 * are the editor services.
 * 
 * @see org.jcryptool.core.operations.editors.AbstractEditorService
 * 
 * @author amro
 * @version 0.1
 *
 */
public class EditorsManager {

    /** The log4j logger */
    private static final Logger logger = OperationsPlugin.getLogManager().getLogger(EditorsManager.class.getName());

    /**
	 * The singletoon object
	 */
    private static EditorsManager instance = null;

    /**
	 * editor services, editorID presents the key and 
	 * editorService the appropriate value
	 */
    private HashMap<String, AbstractEditorService> editorServices = null;

    /**
	 * The constructor exists only to defeat instantiation
	 *
	 */
    private EditorsManager() {
    }

    /**
	 * Getter for the only EditorManager object
	 * @return the EditorManager object
	 */
    public static EditorsManager getInstance() {
        if (instance == null) {
            instance = new EditorsManager();
        }
        return instance;
    }

    public void openNewHexEditor(IEditorInput input) throws PartInitException {
        PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage().openEditor(input, "net.sourceforge.ehep.editors.HexEditor");
    }

    public boolean isEditorOpen() {
        IEditorPart editorPart = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage().getActiveEditor();
        if (editorPart != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Returns an input stream of the active editor content, or <code>null</code> if no editor is active.
	 * 
	 * @return An input stream of the active editor content, or <code>null</code> if no editor is active
	 */
    public InputStream getActiveEditorContentAsInputStream() {
        IEditorPart editorPart = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage().getActiveEditor();
        if (editorPart != null) {
            return getContentInputStream(editorPart);
        } else {
            return null;
        }
    }

    public InputStream getContentInputStream(IEditorPart editorPart) {
        if (editorServices == null) {
            editorServices = new HashMap<String, AbstractEditorService>();
            retrieveEditorServices();
        }
        final String editorID = editorPart.getEditorSite().getId();
        AbstractEditorService service = editorServices.get(editorID);
        return service.getContentOfEditorAsInputStream(editorPart);
    }

    /**
	 * This method performs the retrieval of the (byte[]) content of the parametrized editor.
	 * 
	 * @deprecated Do not use with regard to modern cryptography. Use streams instead.
	 * 
	 * @param editorPart the editor the content is to be retrieved
	 * 
	 * @return the content as a byte[]
	 */
    public byte[] getContentAsBytes(IEditorPart editorPart) {
        if (editorServices == null) {
            editorServices = new HashMap<String, AbstractEditorService>();
            retrieveEditorServices();
        }
        final String editorID = editorPart.getEditorSite().getId();
        AbstractEditorService service = editorServices.get(editorID);
        return service.getContentOfEditorAsBytes(editorPart);
    }

    /**
	 * This method performs the retrieval of the (String) content of the parametrized editor.
	 * 
	 * @deprecated Do not use with regard to modern cryptography. Use streams instead.
	 * 
	 * @param editorPart the editor the content is to be retrieved
	 * 
	 * @return the content as a String
	 */
    public String getContentAsString(IEditorPart editorPart) {
        if (editorServices == null) {
            editorServices = new HashMap<String, AbstractEditorService>();
            retrieveEditorServices();
        }
        final String editorID = editorPart.getEditorSite().getId();
        AbstractEditorService service = editorServices.get(editorID);
        return service.getContentOfEditorAsString(editorPart);
    }

    /**
	 * This method performs the retrieval of the editor services, 
	 * which are registered in extension registry.
	 *
	 */
    private void retrieveEditorServices() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = registry.getExtensionPoint(OperationsPlugin.PLUGIN_ID, IOperationsConstants.PL_EDITOR_SERVICES);
        IExtension extensions[] = extensionPoint.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            IConfigurationElement configElements[] = extensions[i].getConfigurationElements();
            for (int j = 0; j < configElements.length; j++) {
                IConfigurationElement element = configElements[j];
                if (element.getName().equals(IOperationsConstants.TAG_SERVICE)) {
                    try {
                        AbstractEditorService service = (AbstractEditorService) element.createExecutableExtension(IOperationsConstants.ATT_CLASS);
                        String ID = service.getEditorID();
                        editorServices.put(ID, service);
                    } catch (CoreException e) {
                        logger.error("Exception while loading the EditorServices", e);
                    }
                }
            }
        }
    }
}
