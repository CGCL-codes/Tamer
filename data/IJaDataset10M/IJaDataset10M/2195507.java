package org.exist.dom;

import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 * @author wolf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractNodeSetBase extends AbstractNodeSet {

    protected static final Logger LOG = Logger.getLogger(AbstractNodeSetBase.class);

    private static final int UNKNOWN = -1;

    private static final int NOT_INDEXED = 0;

    private static final int ALL_NODES_IN_INDEX = 1;

    private int hasIndex = UNKNOWN;

    private boolean isCached = false;

    protected AbstractNodeSetBase() {
        super();
    }

    public void setIsCached(boolean cached) {
        isCached = cached;
    }

    public boolean isCached() {
        return isCached;
    }

    /**
	 * Returns true if all nodes in this node set and their descendants
	 * are included in the fulltext index. This information is required
	 * to determine if comparison operators can use the
	 * fulltext index to speed up equality comparisons.
	 * 
	 * @see org.exist.xquery.GeneralComparison
	 * @see org.exist.xquery.ValueComparison
	 * @return
	 */
    public boolean hasIndex() {
        if (hasIndex == UNKNOWN) {
            hasIndex = ALL_NODES_IN_INDEX;
            for (Iterator i = iterator(); i.hasNext(); ) {
                if (!((NodeProxy) i.next()).hasIndex()) {
                    hasIndex = NOT_INDEXED;
                    break;
                }
            }
        }
        return hasIndex == ALL_NODES_IN_INDEX;
    }
}
