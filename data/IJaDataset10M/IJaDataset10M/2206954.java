package net.sourceforge.javautil.developer.ide.eclipse.project;

import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;
import net.sourceforge.javautil.common.io.IVirtualArtifact;
import net.sourceforge.javautil.common.io.VirtualArtifactNotFoundException;
import net.sourceforge.javautil.common.io.IVirtualDirectory;
import net.sourceforge.javautil.common.io.impl.SimplePath;
import net.sourceforge.javautil.developer.common.project.IProjectLayout;

/**
 * The base for all {@link EclipseProject} layouts.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: EclipseProjectLayout.java 2304 2010-06-16 02:46:42Z ponderator $
 */
public class EclipseProjectLayout implements IProjectLayout {

    protected final EclipseProject project;

    protected final IVirtualDirectory root;

    public EclipseProjectLayout(EclipseProject project, IVirtualDirectory root) {
        this.root = root;
        this.project = project;
    }

    public IVirtualDirectory getRootDirectory() {
        return root;
    }

    public IVirtualArtifact resolve(String path) {
        try {
            return path.startsWith("/") ? project.getWorkspace().getRoot().getArtifact(new SimplePath(path)) : root.getArtifact(new SimplePath(path));
        } catch (VirtualArtifactNotFoundException e) {
            ThrowableManagerRegistry.caught(e);
            return null;
        }
    }
}
