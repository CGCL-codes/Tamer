package com.philip.journal.home.controller.action;

import com.philip.journal.core.controller.BaseAction;
import com.philip.journal.core.controller.ResponseHandler;
import com.philip.journal.core.service.ServiceProxy;

/**
 * @author cry30
 */
public abstract class AbstractTreeAction extends BaseAction {

    /** Reusable form parentId. */
    protected static final String PARENT_ID = "parentId";

    /** Reusable form Branch name. */
    protected static final String BRANCH_NAME = "branchName";

    /** Reusable form Branch Id. */
    protected static final String BRANCH_ID = "branchId";

    /** Reusable form Entry Id. */
    protected static final String ENTRY_ID = "entryId";

    /** Reusable form Node Id. Can be Branch or Entry. */
    protected static final String NODE_ID = "nodeId";

    /** attribute to identify if request is for leaf or for a branch. */
    protected static final String IS_LEAF = "isLeaf";

    /**
     * Default delegate constructor.
     *
     * @param serviceProxy Service proxy to be injected by Spring.
     * @param responseHandler Response handler to be injected by Spring.
     */
    public AbstractTreeAction(final ServiceProxy serviceProxy, final ResponseHandler responseHandler) {
        super(serviceProxy, responseHandler);
    }

    /**
     * Helper method to remove the prefix on the id. If ID format has no dash, it will return the id as a long
     * value.
     *
     * @param nodeId node id used in the view either l-xxx for leaf or b-xxx for branch.
     * @return id convertible to number.
     */
    protected long parseId(final String nodeId) {
        long retval;
        if (nodeId.indexOf('-') == -1) {
            retval = Long.parseLong(nodeId);
        } else {
            final String[] arr = nodeId.split("-");
            retval = Long.parseLong(arr[1]);
        }
        return retval;
    }
}
