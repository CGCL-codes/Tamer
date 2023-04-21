package com.aptana.ide.core.ui.io.file;

import java.util.ArrayList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import com.aptana.ide.core.io.IVirtualFileManager;
import com.aptana.ide.core.io.IVirtualFileManagerDialog;
import com.aptana.ide.core.io.ProtocolManager;
import com.aptana.ide.core.io.sync.SyncManager;
import com.aptana.ide.core.ui.CoreUIPlugin;

/**
 * @author Kevin Lindsey
 */
public class ProjectProtocolManager extends ProtocolManager {

    private static Image fProjectIcon;

    /**
	 * static constructor
	 */
    static {
        ImageDescriptor imageDescriptor = CoreUIPlugin.getImageDescriptor("icons/project_protocol.gif");
        if (imageDescriptor != null) {
            fProjectIcon = imageDescriptor.createImage();
        }
    }

    /**
	 * FileSystemRoots
	 */
    public static String FileSystemRoots = "::";

    /**
	 * LocalProtocolManager
	 */
    static ProjectProtocolManager _localProtocolManager = new ProjectProtocolManager();

    /**
	 * @see com.aptana.ide.core.io.ProtocolManager#createFileManager()
	 */
    public IVirtualFileManager createFileManager() {
        return createFileManager(false);
    }

    /**
	 * Creates a file manager, but does not add it to the list
	 * 
	 * @param temporary
	 * @return IVirtualFileManager
	 */
    public IVirtualFileManager createFileManager(boolean temporary) {
        ProjectFileManager lfm = new ProjectFileManager(this);
        lfm.setHidden(true);
        if (!temporary) {
            this.addFileManager(lfm);
        }
        return lfm;
    }

    /**
	 * getInstance
	 * 
	 * @return ProjectProtocolManager
	 */
    public static ProjectProtocolManager getInstance() {
        return _localProtocolManager;
    }

    /**
	 * @see ProtocolManager#getStaticInstance()
	 */
    public ProtocolManager getStaticInstance() {
        return getInstance();
    }

    /**
	 * @see ProtocolManager#createPropertyDialog(Shell, int)
	 */
    public IVirtualFileManagerDialog createPropertyDialog(Shell parent, int style) {
        return new ProjectLocationDialog(parent, style);
    }

    /**
	 * getImage
	 * 
	 * @return Image
	 */
    public Image getImage() {
        return fProjectIcon;
    }

    /**
	 * getFileManager
	 * 
	 * @param relativePath
	 *            the local path
	 * @return Returns the file manager that matches this base path
	 */
    public IVirtualFileManager[] getFileManagers(String relativePath) {
        IVirtualFileManager[] fms = getFileManagers();
        ArrayList newManagers = new ArrayList();
        for (int i = 0; i < fms.length; i++) {
            ProjectFileManager manager = (ProjectFileManager) fms[i];
            if (relativePath.equals(manager.getRelativePath())) {
                newManagers.add(manager);
            }
        }
        return (IVirtualFileManager[]) newManagers.toArray(new IVirtualFileManager[0]);
    }

    /**
	 * @see com.aptana.ide.core.io.ProtocolManager#getManagedType()
	 */
    public String getManagedType() {
        return ProjectFileManager.class.getName();
    }

    /**
	 * @see com.aptana.ide.core.io.ProtocolManager#getFileManagers()
	 */
    public IVirtualFileManager[] getFileManagers() {
        return (IVirtualFileManager[]) SyncManager.getSyncManager().getItems(ProjectFileManager.class);
    }
}
