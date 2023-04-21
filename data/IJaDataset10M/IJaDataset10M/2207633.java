package org.pixory.pxtapestry;

import java.util.List;
import org.apache.tapestry.IRequestCycle;

public interface PXPathBarDelegate {

    /**
	 * @return List of String
	 */
    public List getPathElements(IRequestCycle cycle);

    public void selectPath(List path, IRequestCycle cycle);
}
