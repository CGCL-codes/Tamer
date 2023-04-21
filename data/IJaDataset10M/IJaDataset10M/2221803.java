package com.aptana.ide.debug.internal.core.sourcelookup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.sourcelookup.ISourceContainerType;
import org.eclipse.debug.core.sourcelookup.containers.AbstractSourceContainer;
import com.aptana.ide.core.resources.LocalFileStorage;

/**
 * @author Max Stepanov
 */
public class JSSourceContainer extends AbstractSourceContainer {

    private IWorkspaceRoot fRoot;

    /**
	 * JSSourceContainer
	 */
    public JSSourceContainer() {
        fRoot = ResourcesPlugin.getWorkspace().getRoot();
    }

    /**
	 * @see org.eclipse.debug.core.sourcelookup.ISourceContainer#findSourceElements(java.lang.String)
	 */
    public Object[] findSourceElements(String path) throws CoreException {
        ArrayList sources = new ArrayList();
        IPath ipath = new Path(path);
        if (ipath.isAbsolute()) {
            File osFile = new File(path);
            if (osFile.exists()) {
                try {
                    IPath canonicalPath = new Path(osFile.getCanonicalPath());
                    IFile[] files = fRoot.findFilesForLocation(canonicalPath);
                    if (files.length > 0) {
                        for (int i = 0; i < files.length; i++) {
                            sources.add(files[i]);
                        }
                    } else {
                        sources.add(new LocalFileStorage(osFile));
                    }
                } catch (IOException e) {
                }
            }
        } else {
            IResource res = fRoot.findMember(ipath);
            if (res instanceof IFile) {
                sources.add(res);
            }
        }
        return sources.toArray();
    }

    /**
	 * @see org.eclipse.debug.core.sourcelookup.ISourceContainer#getName()
	 */
    public String getName() {
        return Messages.JSSourceContainer_LocalFileSourceContainer;
    }

    /**
	 * Not persisted via the launch configuration
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.ISourceContainer#getType()
	 */
    public ISourceContainerType getType() {
        return null;
    }
}
