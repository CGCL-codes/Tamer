package com.luxoft.fitpro.plugin.dialogs;

import java.util.ArrayList;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.NewFolderDialog;
import com.luxoft.fitpro.plugin.messages.PluginMessages;

/**
 * ElementDialog is a dialog that shows the tree-structure of directories in an Eclipse-project and a button for
 * creating new directories.
 */
public class ElementDialog extends ElementTreeSelectionDialog {

    /**
     * Constructor
     * 
     * @param parent the shell
     * @param project the actual project
     * @param folderOnly folders/folders and files
     */
    public ElementDialog(Shell parent, IProject project, boolean folderOnly) {
        super(parent, new FileFolderLabelProvider(), new FileFolderTreeContentProvider(folderOnly, project));
        setInput(project.getParent());
    }

    /**
     * Adds a button Create New Folder to the dialog with the tree-structure.
     * 
     * @see ElementTreeSelectionDialog#createDialogArea(Composite)
     */
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        Button button = new Button(composite, SWT.PUSH);
        button.setText(PluginMessages.getMessage("fit.runner.create_new_folder"));
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent selectionEvent) {
                IResource res = handleCreateNewFolder();
                if (res != null) {
                    getTreeViewer().refresh();
                }
            }
        });
        return composite;
    }

    /**
     * Handles the click to the button Create New Folder...
     * 
     * @return selected resource or null
     */
    protected IResource handleCreateNewFolder() {
        NewFolderDialog dialog = new NewFolderDialog(getShell(), (IContainer) getFirstResult());
        return (dialog.open() == Window.OK) ? (IResource) dialog.getFirstResult() : null;
    }

    /**
     * Tree content provider for files and folders.
     */
    private static class FileFolderTreeContentProvider implements ITreeContentProvider {

        /**
         * Flag to indicate if provider is dealing with folders only.
         */
        private final boolean folderOnly;

        private final IProject project;

        /**
         * Contructor for creating new file and folder tree content provider.
         * 
         * @param folderOnly flag indicating if provider is dealing with folders only
         */
        public FileFolderTreeContentProvider(boolean folderOnly, IProject project) {
            this.folderOnly = folderOnly;
            this.project = project;
        }

        /**
         * {@inheritDoc}
         */
        public Object[] getChildren(final Object parentElement) {
            if (parentElement instanceof IFile) {
                return new Object[] {};
            } else if (parentElement instanceof IContainer) {
                ArrayList<IResource> list = new ArrayList<IResource>();
                IContainer folder = (IContainer) parentElement;
                try {
                    IResource[] resources = folder.members(IContainer.INCLUDE_TEAM_PRIVATE_MEMBERS);
                    for (int i = 0; i < resources.length; i++) {
                        IResource resource = resources[i];
                        if (resource instanceof IFolder || !folderOnly || (resource instanceof IProject && resource.equals(project))) {
                            list.add(resource);
                        }
                    }
                    return list.toArray(new IResource[list.size()]);
                } catch (CoreException e) {
                    return new Object[] {};
                }
            }
            return new Object[] {};
        }

        /**
         * {@inheritDoc}
         */
        public Object getParent(Object element) {
            IResource resource = (IResource) element;
            return resource.getParent();
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasChildren(Object element) {
            Object[] children = getChildren(element);
            return children.length > 0;
        }

        /**
         * {@inheritDoc}
         */
        public Object[] getElements(Object inputElement) {
            return getChildren(inputElement);
        }

        /**
         * {@inheritDoc}
         */
        public void dispose() {
        }

        /**
         * {@inheritDoc}
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    /**
     * Label provider for files and folders.
     */
    private static class FileFolderLabelProvider extends LabelProvider {

        /**
         * Image representing a folder.
         */
        private static Image folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);

        /**
         * Image representing a file.
         */
        private static Image fileImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);

        /**
         * Image representing a project.
         */
        private static Image projectImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_PROJECT);

        /**
         * Returns image of file or folder depending on element type, else returns null if element type not recognized.
         * 
         * @param element the element to get the image for
         * @return image
         */
        public Image getImage(Object element) {
            if (element instanceof IFolder) {
                return folderImage;
            } else if (element instanceof IFile) {
                return fileImage;
            } else if (element instanceof IProject) {
                return projectImage;
            } else {
                return null;
            }
        }

        /**
         * Returns the name of the element.
         * 
         * @param element the element to get the name for
         * @return name of element
         */
        public String getText(Object element) {
            IResource resource = (IResource) element;
            return resource.getName();
        }
    }
}
