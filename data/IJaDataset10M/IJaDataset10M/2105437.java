package org.j2eespider.templateeditor.velocityeditor.util;

import java.io.File;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * This class is based on project VelocityWebEdit with author permission.
 * http://velocitywebedit.sourceforge.net
 * 
 * @author joe.hudson, bruno.braga
 */
public class ExternalFileEditorInput implements IPathEditorInput, ILocationProvider {

    /**
	 * The workbench adapter which simply provides the label.
	 *
	 * @since 3.1
	 */
    private class WorkbenchAdapter implements IWorkbenchAdapter {

        public Object[] getChildren(Object o) {
            return null;
        }

        public ImageDescriptor getImageDescriptor(Object object) {
            return null;
        }

        public String getLabel(Object o) {
            return ((ExternalFileEditorInput) o).getName();
        }

        public Object getParent(Object o) {
            return null;
        }
    }

    private File fFile;

    private WorkbenchAdapter fWorkbenchAdapter = new WorkbenchAdapter();

    public ExternalFileEditorInput(File file) {
        super();
        fFile = file;
        fWorkbenchAdapter = new WorkbenchAdapter();
    }

    public boolean exists() {
        return fFile.exists();
    }

    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    public String getName() {
        return fFile.getName();
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public String getToolTipText() {
        return fFile.getAbsolutePath();
    }

    public Object getAdapter(Class adapter) {
        if (ILocationProvider.class.equals(adapter)) return this;
        if (IWorkbenchAdapter.class.equals(adapter)) return fWorkbenchAdapter;
        return Platform.getAdapterManager().getAdapter(this, adapter);
    }

    public IPath getPath(Object element) {
        if (element instanceof ExternalFileEditorInput) {
            ExternalFileEditorInput input = (ExternalFileEditorInput) element;
            return Path.fromOSString(input.fFile.getAbsolutePath());
        }
        return null;
    }

    public IPath getPath() {
        return Path.fromOSString(fFile.getAbsolutePath());
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof ExternalFileEditorInput) {
            ExternalFileEditorInput input = (ExternalFileEditorInput) o;
            return fFile.equals(input.fFile);
        }
        if (o instanceof IPathEditorInput) {
            IPathEditorInput input = (IPathEditorInput) o;
            return getPath().equals(input.getPath());
        }
        return false;
    }

    public int hashCode() {
        return fFile.hashCode();
    }
}
