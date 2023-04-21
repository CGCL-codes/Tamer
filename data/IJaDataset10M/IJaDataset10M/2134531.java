package org.objectstyle.wolips.datasets;

import org.eclipse.core.resources.IResource;

/**
 * @author ulrich
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IDataSet extends IDataSetTypes {

    /**
	 * @return The IDataSet type;
	 */
    public abstract int getType();

    /**
	 * @return The corresponding resource. 
	 */
    public IResource getCorrespondingResource();
}
