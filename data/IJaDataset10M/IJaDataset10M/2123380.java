package com.anaxima.eslink.model.core;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;

/**
 * Common interface for all elements containing
 * <code>IEslinkSourceCatalog</code>s.
 * <p>
 * @author Thomas Vater
 */
public interface IEslinkSourceCatalogRoot extends IEslinkElement, IParent {

    /**
     * Adds a new folder as source catalog folder to this element.
     * <b>NOTE:</b> The given folder must be relative to the containing
     * project and consist of at least two segments.
     * 
     * @param   argFolder
     *          Folder to add as source catalog.
     *          
     * @exception   EslinkModelException
     *              If the folder is not outlined as expected.          
     */
    public void addSourceCatalogFolder(IFolder argFolder) throws EslinkModelException;

    /**
     * Removes a source catalog from this element.
     * <b>NOTE:</b> The given object must be child of this element.
     * 
     * @param   argChild
     *          Child to remove.
     *          
     * @exception   EslinkModelException
     *              If the folder is not outlined as expected.          
     */
    public void removeSourceCatalog(IEslinkSourceCatalog argChild) throws EslinkModelException;

    /**
     * Returns the resource container backing this element.
     */
    public IContainer getContainer();
}
