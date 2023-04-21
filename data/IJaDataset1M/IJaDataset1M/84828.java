package jreceiver.common.rpc;

import java.util.Vector;
import java.util.Set;

/**
 * Folder-related queries and updates for a (possibly-remote) JRec server
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.3 $ $Date: 2002/07/31 11:29:41 $
 */
public interface Folders extends ServerTable {

    public static final String HANDLER_NAME = "Folders";

    public static final String GET_CHILD_KEYS = "getChildKeys";

    public static final String GET_ROOT_KEYS = "getRootKeys";

    public static final String FIND_PARENT = "findParent";

    /**
     * Obtain a list of folder_ids which represent the children
     * of the folder associated with parent_id.
     */
    public Vector getChildKeys(int parent_id, String order_by, boolean recurse, int rec_offset, int rec_count) throws RpcException;

    /**
     * Obtain a list of folder_ids which represent the root folders
     */
    public Vector getRootKeys(String order_by, int rec_offset, int rec_count) throws RpcException;

    /**
     * Attempt to determine whether or not folder_id
     * has a parent amongst the set of parents.
     * <p>
     * Note that 0 is not considered a valid folder_id
     * and will be ignored if in the list of parents.
     * @return the folder_id of the parent, if found; otherwise 0.
     */
    public int findParent(int folder_id, Set parents) throws RpcException;
}
