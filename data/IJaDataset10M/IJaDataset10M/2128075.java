package jreceiver.j2me.common.rpc.xmlrpc;

import java.util.Hashtable;
import java.util.Vector;
import jreceiver.j2me.common.rec.Rec;
import jreceiver.j2me.common.rec.RecException;
import jreceiver.j2me.common.rec.security.User;
import jreceiver.j2me.common.rec.source.PlaylistRec;
import jreceiver.j2me.common.rpc.Playlists;
import jreceiver.j2me.common.rpc.RpcException;

/**
 * Dynamic-playlist-related queries to a remote server via XML-RPC.
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:10 $
 */
public class PlaylistsImpl extends ServerTableImpl implements Playlists {

    /**
     * ctor for this implementation
     *
     * @param host
     */
    public PlaylistsImpl(String remote_host, User user) throws RpcException {
        super(HANDLER_NAME, remote_host, user);
    }

    /**
     * restore a rec object from hashtable form
     */
    public Rec reconstituteRec(Hashtable hash) throws RecException {
        return PlaylistRec.createInstance(hash);
    }

    /**
     * Obtain a total count of keys for the specified filter.
     * <p>
     * Recommended when traversing large lists.
     */
    public int getKeyCountForMask(int pl_mask) throws RpcException {
        Vector params = new Vector();
        params.addElement(new Integer(pl_mask));
        Integer ii = (Integer) execute(GET_KEY_COUNT_FOR_MASK, params);
        return ii.intValue();
    }

    /**
     * Obtain an ordered range of keys for the specified mask.
     * <p>
     * Recommended when traversing large lists where a complex filter
     * is used.
     */
    public Vector getKeysForMask(int pl_mask, String order_by, int rec_offset, int rec_count) throws RpcException {
        if (log.isDebugEnabled()) log.debug("getKeysForMask");
        Vector params = new Vector();
        params.addElement(new Integer(pl_mask));
        params.addElement(order_by != null ? order_by : "");
        params.addElement(new Integer(rec_offset));
        params.addElement(new Integer(rec_count));
        return (Vector) execute(GET_KEYS_FOR_MASK, params);
    }

    /**
     * see interface for description and behavior
     */
    public void refresh(Vector keys) throws RpcException {
        if (keys == null) throw new IllegalArgumentException();
        Vector params = new Vector();
        params.addElement(keys);
        execute(REFRESH, params);
    }

    /**
     * see interface for description and behavior
     */
    public String validateFilter(String raw_filter, String order_by) throws RpcException {
        if (raw_filter == null) throw new IllegalArgumentException();
        if (order_by == null) order_by = "";
        Vector params = new Vector();
        params.addElement(raw_filter);
        params.addElement(order_by);
        return (String) execute(VALIDATE_FILTER, params);
    }
}
