package com.android.ide.eclipse.adt.io;

import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.IAbstractResource;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import java.util.ArrayList;

/**
 * An implementation of {@link IAbstractFolder} on top of either an {@link IFolder} or an
 * {@link IContainer} object.
 */
public class IFolderWrapper implements IAbstractFolder {

    private final IFolder mFolder;

    private final IContainer mContainer;

    public IFolderWrapper(IFolder folder) {
        mContainer = mFolder = folder;
    }

    public IFolderWrapper(IContainer container) {
        mFolder = container instanceof IFolder ? (IFolder) container : null;
        mContainer = container;
    }

    public String getName() {
        return mContainer.getName();
    }

    public boolean exists() {
        return mContainer.exists();
    }

    public IAbstractResource[] listMembers() {
        try {
            IResource[] members = mContainer.members();
            final int count = members.length;
            IAbstractResource[] afiles = new IAbstractResource[count];
            for (int i = 0; i < count; i++) {
                IResource f = members[i];
                if (f instanceof IFile) {
                    afiles[i] = new IFileWrapper((IFile) f);
                } else {
                    afiles[i] = new IFolderWrapper((IContainer) f);
                }
            }
            return afiles;
        } catch (CoreException e) {
        }
        return new IAbstractResource[0];
    }

    public boolean hasFile(String name) {
        try {
            IResource[] files = mContainer.members();
            for (IResource file : files) {
                if (name.equals(file.getName())) {
                    return true;
                }
            }
        } catch (CoreException e) {
        }
        return false;
    }

    public IAbstractFile getFile(String name) {
        if (mFolder != null) {
            IFile file = mFolder.getFile(name);
            return new IFileWrapper(file);
        }
        IFile file = mContainer.getFile(new Path(name));
        return new IFileWrapper(file);
    }

    /**
     * Returns the {@link IFolder} object that the receiver could represent.
     * Can be <code>null</code>
     */
    public IFolder getIFolder() {
        return mFolder;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IFolderWrapper) {
            return mFolder.equals(((IFolderWrapper) obj).mFolder);
        }
        if (obj instanceof IFolder) {
            return mFolder.equals(obj);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return mContainer.hashCode();
    }

    public IAbstractFolder getFolder(String name) {
        if (mFolder != null) {
            IFolder folder = mFolder.getFolder(name);
            return new IFolderWrapper(folder);
        }
        IFolder folder = mContainer.getFolder(new Path(name));
        return new IFolderWrapper(folder);
    }

    public String getOsLocation() {
        return mContainer.getLocation().toOSString();
    }

    public String[] list(FilenameFilter filter) {
        try {
            IResource[] members = mContainer.members();
            if (members.length > 0) {
                ArrayList<String> list = new ArrayList<String>();
                for (IResource res : members) {
                    if (filter.accept(this, res.getName())) {
                        list.add(res.getName());
                    }
                }
                return list.toArray(new String[list.size()]);
            }
        } catch (CoreException e) {
        }
        return new String[0];
    }

    public IAbstractFolder getParentFolder() {
        IContainer p = mContainer.getParent();
        if (p != null) {
            return new IFolderWrapper(p);
        }
        return null;
    }
}
