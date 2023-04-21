package net.sourceforge.freejava.vfs.path.align;

import net.sourceforge.freejava.vfs.path.IPath;

/**
 * The alignment determines how to anchor to the context path.
 */
public interface IPathAlignment {

    /**
     * Get the aligned context path.
     * 
     * @return non-<code>null</code> path object.
     * @throws NullPointerException
     *             If any parameter is <code>null</code>.
     */
    IPath align(IPath context);

    String decorate(String localPath);

    RelativeAlignment RELATIVE = new RelativeAlignment();

    RootAlignment ROOT = new RootAlignment();

    RootLayerAlignment ROOT_LAYER = new RootLayerAlignment();
}
