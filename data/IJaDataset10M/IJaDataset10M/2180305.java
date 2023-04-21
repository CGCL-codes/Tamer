package jreceiver.client.mgr.struts;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jreceiver.client.common.struts.JRecTableAction;
import jreceiver.client.common.struts.JRecTableForm;
import jreceiver.common.JRecException;
import jreceiver.common.rec.security.User;
import jreceiver.common.rec.source.Playlist;
import jreceiver.common.rec.source.Source;
import jreceiver.common.rpc.Playlists;
import jreceiver.common.rpc.RpcException;
import jreceiver.common.rpc.RpcFactory;
import jreceiver.util.HelperServlet;

/**
 * Handle incoming requests on "playlists.do" for one or more playlist types.
 * <P>
 * This is a wrapper around the business logic. Its purpose is to
 * translate the HttpServletRequest to the business logic, which
 * if significant, should be processed in a separate class.
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.16 $ $Date: 2003/05/05 07:59:26 $
 */
public final class PlaylistsAction extends JRecTableAction {

    /** obtain the name of the handler that manages this table */
    protected String getHandlerName() {
        return Playlists.HANDLER_NAME;
    }

    /** helper to obtain filter mask from request */
    protected int getPlMask(HttpServletRequest req) {
        return HelperServlet.getIntParam(req, "plMask", Playlist.PLAYLIST_TYPE_ALL);
    }

    /** helper to obtain playlist type from request */
    protected int getPlType(HttpServletRequest req) {
        return HelperServlet.getIntParam(req, "plType", 0);
    }

    /**
    * initialize the form from data in the request (occurs prior to key
    * fetching so that filter params can be specified by querying the form)
    */
    protected void onLoad(User user, HttpServletRequest req, JRecTableForm table_form) throws JRecException {
        if (log.isDebugEnabled()) log.debug("onLoad");
        PlaylistsForm form = (PlaylistsForm) table_form;
        int pl_mask = getPlMask(req);
        form.setPlMask(pl_mask);
    }

    /**
     * subclasses can optionally override to include additional params beyond pageNo
     * <p>
     * e.g. PlaylistsAction can append "&plMask=3"
     */
    protected void getUrlPatternParams(HttpServletRequest req, Map params) throws JRecException {
        int pl_mask = getPlMask(req);
        params.put("plMask", new Integer(pl_mask));
    }

    /**
    * return a range of keys for the current filter
    */
    protected Vector fetchKeys(User user, HttpServletRequest req, JRecTableForm form, int rec_offset, int rec_count) throws JRecException {
        if (log.isDebugEnabled()) log.debug("fetchKeys: mask=" + getPlMask(req));
        try {
            Playlists pl_rpc = RpcFactory.newPlaylists(user);
            return pl_rpc.getKeysForMask(getPlMask(req), null, rec_offset, rec_count);
        } catch (RpcException e) {
            throw new JRecException("rpc-problem fetching playlist recs", e);
        }
    }

    /**
    * grab recs from a vector of keys
    */
    protected Vector fetchRecs(User user, HttpServletRequest req, JRecTableForm form, Vector keys) throws JRecException {
        if (log.isDebugEnabled()) log.debug("fetchRecs: keys=" + keys);
        int pl_mask = getPlMask(req);
        try {
            Hashtable args = new Hashtable();
            if (0 != (pl_mask & Playlist.PLAYLIST_TYPE_STATION)) args.put(Playlist.POPULATE_MEXTERNAL, new Boolean(true));
            if (0 != (pl_mask & Playlist.PLAYLIST_TYPE_DYNAMIC)) args.put(Playlist.POPULATE_FILTERABLE, new Boolean(true));
            args.put(Source.POPULATE_CONTENT_URL, Source.DEFAULT_URL);
            Playlists pl_rpc = RpcFactory.newPlaylists(user);
            return pl_rpc.getRecs(keys, null, args);
        } catch (RpcException e) {
            throw new JRecException("rpc-problem fetching playlist recs", e);
        }
    }

    /**
    * return an approximate count of recs for the current filter
    */
    protected int fetchKeyCount(User user, HttpServletRequest req) throws JRecException {
        try {
            int pl_mask = getPlMask(req);
            Playlists pl_rpc = RpcFactory.newPlaylists(user);
            int count = pl_rpc.getKeyCountForMask(pl_mask);
            if (log.isDebugEnabled()) log.debug("fetchKeyCount: count=" + count + " pl_mask=" + getPlMask(req));
            return count;
        } catch (RpcException e) {
            throw new JRecException("rpc-problem fetching playlist key count", e);
        }
    }

    /**
     * return the forward of the edit form
     *
     * If a rec is specified, invoke based upon its type.
     *
     * If no rec is specified, invoke based upon the plType of the combo list box.
     */
    protected String getEditForward(HttpServletRequest req, Object rec_to_edit) throws JRecException {
        String forward_name;
        int pl_type;
        if (rec_to_edit != null) {
            Playlist pl = (Playlist) rec_to_edit;
            pl_type = pl.getPlaylistType();
        } else {
            pl_type = getPlType(req);
        }
        switch(pl_type) {
            case Playlist.PLAYLIST_TYPE_FILE:
                forward_name = "fplaylistEdit";
                break;
            case Playlist.PLAYLIST_TYPE_DYNAMIC:
                forward_name = "dplaylistEdit";
                break;
            case Playlist.PLAYLIST_TYPE_TREE:
                forward_name = "hplaylistEdit";
                break;
            case Playlist.PLAYLIST_TYPE_STATION:
                forward_name = "splaylistEdit";
                break;
            default:
                throw new JRecException("playlist type not recognized");
        }
        return forward_name;
    }

    /**
    * generate a parameter list used in the redirection to the edit screen
    */
    protected void getEditParams(User user, HttpServletRequest req, Object rec_to_edit, Map params) throws JRecException {
        if (rec_to_edit != null) {
            Playlist pl = (Playlist) rec_to_edit;
            params.put("srcId", pl.getKey());
        }
    }

    /**
    * delete the record(s) for the specified key(s)
    */
    protected void deleteRecs(User user, HttpSession session, Vector keys) throws JRecException {
        try {
            Playlists pl_rpc = RpcFactory.newPlaylists(user);
            pl_rpc.deleteRecs(keys);
        } catch (RpcException e) {
            throw new JRecException("rpc-problem deleting playlist recs");
        }
    }

    /**
     * logging object
     */
    protected static Log log = LogFactory.getLog(PlaylistsAction.class);
}
