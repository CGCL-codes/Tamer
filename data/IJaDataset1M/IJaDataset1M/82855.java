package org.eclipse.wst.xml.search.core.queryspecifications.container;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;

/**
 * 
 * Provider to get several {@link IStorage} (files coming from JAR....) used to
 * search DOM Document to use to search DOM Nodes.
 * 
 */
public interface IMultiStorageProvider {

    public static final IStorage[] EMPTY_STORAGE = new IStorage[0];

    /**
	 * Returns array of {@link IStorage} (files coming from JAR....) used to
	 * search DOM Document to use to search DOM Nodes. If null is returned,
	 * search is stopped.
	 * 
	 * @param selectedNode
	 *            the selected node which have launch the search.
	 * @param resource
	 *            the owner resource file of the selected node.
	 * @return
	 */
    IStorage[] getStorages(Object selectedNode, IResource resource);
}
