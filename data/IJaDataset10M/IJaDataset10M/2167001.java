package org.deft.share;

import java.io.InputStream;
import java.io.OutputStream;
import org.deft.repository.RepositoryFactory;
import org.deft.repository.fragment.Fragment;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

/**
 * An IEditorInput specialized for Fragments.
 * 
 * 
 * 
 */
public class FragmentEditorInput implements IStreamEditorInput, IPersistableElement {

    public static final String KEY = "FragmentEditor";

    private Fragment fragment;

    public FragmentEditorInput(Fragment fragment) {
        this.fragment = fragment;
    }

    public boolean exists() {
        return true;
    }

    public ImageDescriptor getImageDescriptor() {
        return ImageDescriptor.getMissingImageDescriptor();
    }

    public String getName() {
        return fragment.getName();
    }

    public IPersistableElement getPersistable() {
        return this;
    }

    public String getToolTipText() {
        return fragment.getName();
    }

    public Object getAdapter(Class adapter) {
        return null;
    }

    /**
	 * Two FragmentEditorInputs are equal if the two containing Fragments are
	 * equal. The two Fragments are equal if they have the same UUID.
	 */
    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof FragmentEditorInput) {
            FragmentEditorInput other = (FragmentEditorInput) arg0;
            return fragment.equals(other.getFragment());
        }
        return false;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public InputStream getInputStream() {
        return RepositoryFactory.getRepository().getInputStream(fragment);
    }

    public OutputStream getOutputStream() {
        return RepositoryFactory.getRepository().getOutputStream(fragment);
    }

    public String getFactoryId() {
        return FragmentEditorInputFactory.ID;
    }

    public void saveState(IMemento memento) {
        memento.putString(KEY, fragment.getUUID().toString());
    }
}
