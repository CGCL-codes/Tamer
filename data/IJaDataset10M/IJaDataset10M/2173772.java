package jreceiver.common.rpc.xmlrpc;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Set;
import java.net.URL;
import jreceiver.common.rec.Rec;
import jreceiver.common.rec.RecException;
import jreceiver.common.rec.security.User;
import jreceiver.common.rpc.Folders;
import jreceiver.common.rpc.RpcException;
import jreceiver.common.rec.site.FolderRec;

/**
 * Folder-related queries to a remote server via XML-RPC.
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.5 $ $Date: 2002/07/31 11:29:42 $
 */
public class FoldersImpl extends ServerTableImpl implements Folders {

    /**
     * ctor for this implementation
     */
    public FoldersImpl(URL remote_host, User user) throws RpcException {
        super(HANDLER_NAME, remote_host, user);
    }

    /**
     * restore a rec object from hashtable form
     */
    public Rec reconstituteRec(Hashtable hash) throws RecException {
        return new FolderRec(hash);
    }

    /**
     * Obtain a list of folder_ids which represent the children
     * of the folder associated with parent_id.
     */
    public Vector getChildKeys(int parent_id, String order_by, boolean recurse, int rec_offset, int rec_count) throws RpcException {
        if (parent_id < 1) throw new IllegalArgumentException();
        Vector params = new Vector();
        params.addElement(new Integer(parent_id));
        params.addElement(order_by != null ? order_by : "");
        params.addElement(new Boolean(recurse));
        params.addElement(new Integer(rec_offset));
        params.addElement(new Integer(rec_count));
        return (Vector) execute(GET_CHILD_KEYS, params);
    }

    /**
     * Obtain a list of folder_ids which represent the root folders
     */
    public Vector getRootKeys(String order_by, int rec_offset, int rec_count) throws RpcException {
        Vector params = new Vector();
        params.addElement(order_by != null ? order_by : "");
        params.addElement(new Integer(rec_offset));
        params.addElement(new Integer(rec_count));
        return (Vector) execute(GET_ROOT_KEYS, params);
    }

    /**
     * see interface for description and behavior
     */
    public int findParent(int folder_id, Set folders) throws RpcException {
        if (folder_id < 1 || folders == null) throw new IllegalArgumentException();
        Vector params = new Vector();
        params.addElement(new Integer(folder_id));
        params.addElement(new Vector(folders));
        Integer parent_id = (Integer) execute(FIND_PARENT, params);
        return parent_id.intValue();
    }
}
