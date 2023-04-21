package com.koutra.dist.proc.designer.launching;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.ui.model.IWorkbenchAdapter;
import com.koutra.dist.proc.designer.KoutraXformPlugin;
import com.koutra.dist.proc.designer.Messages;

/**
 * This is a specialization of <code>FilteredItemsSelectionDialog</code> used to present
 * users with a listing of <code>IType</code>s that contain main methods
 * 
 * @since 3.3
 * 
 */
public class ProcFileSelectionDialog extends FilteredItemsSelectionDialog {

    /**
	 * Main list label provider
	 */
    public class ProcFileLabelProvider implements ILabelProvider {

        HashMap<ImageDescriptor, Image> imageMap = new HashMap<ImageDescriptor, Image>();

        public Image getImage(Object element) {
            if (element instanceof IAdaptable) {
                IWorkbenchAdapter adapter = (IWorkbenchAdapter) ((IAdaptable) element).getAdapter(IWorkbenchAdapter.class);
                if (adapter != null) {
                    ImageDescriptor descriptor = adapter.getImageDescriptor(element);
                    Image image = (Image) imageMap.get(descriptor);
                    if (image == null) {
                        image = descriptor.createImage();
                        imageMap.put(descriptor, image);
                    }
                    return image;
                }
            }
            return null;
        }

        public String getText(Object element) {
            if (element instanceof IFile) {
                IFile f = (IFile) element;
                String label = f.getName();
                String container = getDeclaringContainerName(f);
                if (container != null && !"".equals(container)) {
                    label += " - " + container;
                }
                return label;
            }
            return null;
        }

        /**
		 * Returns the name of the declaring container name
		 * @param type the type to find the container name for
		 * @return the container name for the specified type
		 */
        protected String getDeclaringContainerName(IFile f) {
            return f.getParent().getFullPath().toString();
        }

        /**
		 * Returns the narrowest enclosing <code>IJavaElement</code> which is either 
		 * an <code>IType</code> (enclosing) or an <code>IPackageFragment</code> (contained in)
		 * @param type the type to find the enclosing <code>IJavaElement</code> for.
		 * @return the enclosing element or <code>null</code> if none
		 */
        protected IContainer getDeclaringContainer(IFile f) {
            return f.getParent();
        }

        public void dispose() {
            imageMap.clear();
            imageMap = null;
        }

        public void addListener(ILabelProviderListener listener) {
        }

        public boolean isLabelProperty(Object element, String property) {
            return false;
        }

        public void removeListener(ILabelProviderListener listener) {
        }
    }

    /**
	 * Provides a label and image for the details area of the dialog
	 */
    class ProcFileDetailsLabelProvider extends ProcFileLabelProvider {

        public String getText(Object element) {
            if (element instanceof IFile) {
                IFile f = (IFile) element;
                return getDeclaringContainerName(f);
            }
            return null;
        }

        public Image getImage(Object element) {
            if (element instanceof IFile) {
                return super.getImage(getDeclaringContainer(((IFile) element)));
            }
            return super.getImage(element);
        }
    }

    /**
	 * Simple items filter
	 */
    class ProcFileItemsFilter extends ItemsFilter {

        public boolean isConsistentItem(Object item) {
            return item instanceof IFile;
        }

        public boolean matchItem(Object item) {
            if (!(item instanceof IFile) || !Arrays.asList(files).contains(item)) {
                return false;
            }
            return matches(((IFile) item).getName());
        }
    }

    /**
	 * The selection history for the dialog
	 */
    class ProcFileSelectionHistory extends SelectionHistory {

        protected Object restoreItemFromMemento(IMemento memento) {
            IPath p = Path.fromPortableString(memento.getTextData());
            IFile f = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(p);
            return (f != null && f.exists()) ? f : null;
        }

        protected void storeItemToMemento(Object item, IMemento memento) {
            if (item instanceof IFile) {
                memento.putTextData(((IFile) item).getFullPath().toPortableString());
            }
        }
    }

    private static final String SETTINGS_ID = KoutraXformPlugin.PLUGIN_ID + ".KOUTRAXFORM_SELECTION_DIALOG";

    private IFile[] files = null;

    /**
	 * Constructor
	 * @param elements the types to display in the dialog
	 */
    public ProcFileSelectionDialog(Shell shell, IFile[] elements, String title) {
        super(shell, false);
        setTitle(title);
        files = elements;
        setMessage(Messages.ProcFileSelectionDialog_FileSelectionDialogDescription);
        setInitialPattern(Messages.ProcFileSelectionDialog_FileSelectionDialogInitialPattern);
        setListLabelProvider(new ProcFileLabelProvider());
        setDetailsLabelProvider(new ProcFileDetailsLabelProvider());
        setSelectionHistory(new ProcFileSelectionHistory());
    }

    protected Control createDialogArea(Composite parent) {
        Control ctrl = super.createDialogArea(parent);
        return ctrl;
    }

    /**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getDialogSettings()
	 */
    protected IDialogSettings getDialogSettings() {
        IDialogSettings settings = KoutraXformPlugin.getDefault().getDialogSettings();
        IDialogSettings section = settings.getSection(SETTINGS_ID);
        if (section == null) {
            section = settings.addNewSection(SETTINGS_ID);
        }
        return section;
    }

    /**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getItemsComparator()
	 */
    protected Comparator<IFile> getItemsComparator() {
        Comparator<IFile> comp = new Comparator<IFile>() {

            public int compare(IFile f1, IFile f2) {
                return f1.getName().compareTo(f2.getName());
            }
        };
        return comp;
    }

    /**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#validateItem(java.lang.Object)
	 */
    protected IStatus validateItem(Object item) {
        return Status.OK_STATUS;
    }

    /**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#createExtendedContentArea(org.eclipse.swt.widgets.Composite)
	 */
    protected Control createExtendedContentArea(Composite parent) {
        return null;
    }

    /**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#createFilter()
	 */
    protected ItemsFilter createFilter() {
        return new ProcFileItemsFilter();
    }

    /**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#fillContentProvider(org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.AbstractContentProvider, org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter, org.eclipse.core.runtime.IProgressMonitor)
	 */
    protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter, IProgressMonitor progressMonitor) throws CoreException {
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (itemsFilter.isConsistentItem(files[i])) {
                    contentProvider.add(files[i], itemsFilter);
                }
            }
        }
    }

    /**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getElementName(java.lang.Object)
	 */
    public String getElementName(Object item) {
        if (item instanceof IFile) {
            return ((IFile) item).getName();
        }
        return null;
    }
}
