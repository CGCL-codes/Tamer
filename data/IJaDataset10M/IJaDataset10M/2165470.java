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
import jreceiver.common.rec.util.Transcoder;
import jreceiver.common.rpc.RpcException;
import jreceiver.common.rpc.RpcFactory;
import jreceiver.common.rpc.Transcoders;

/**
 * Handle incoming requests on "transcoders.do"
 * <P>
 * This is a wrapper around the business logic. Its purpose is to
 * translate the HttpServletRequest to the business logic, which
 * if significant, should be processed in a separate class.
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.8 $ $Date: 2003/05/05 07:59:26 $
 */
public final class TranscodersAction extends JRecTableAction {

    /** obtain the name of the handler that manages this table */
    protected String getHandlerName() {
        return Transcoders.HANDLER_NAME;
    }

    /**
    * generate a parameter list used in the redirection to the edit screen
    */
    protected void getEditParams(User user, HttpServletRequest req, Object rec_to_edit, Map params) throws JRecException {
        if (rec_to_edit != null) {
            Transcoder rec = (Transcoder) rec_to_edit;
            params.put("xcoderId", new Integer(rec.getId()));
        }
    }

    /**
    * return a range of keys for the current filter
    */
    protected Vector fetchKeys(User user, HttpServletRequest req, JRecTableForm form, int rec_offset, int rec_count) throws JRecException {
        try {
            Transcoders xcoder_rpc = RpcFactory.newTranscoders(user);
            return xcoder_rpc.getKeys(null, rec_offset, rec_count);
        } catch (RpcException e) {
            throw new JRecException("rpc-problem fetching transcoder recs", e);
        }
    }

    /**
    * grab recs from a vector of keys
    */
    protected Vector fetchRecs(User user, HttpServletRequest req, JRecTableForm form, Vector keys) throws JRecException {
        try {
            Hashtable args = new Hashtable();
            Transcoders xcoder_rpc = RpcFactory.newTranscoders(user);
            return xcoder_rpc.getRecs(keys, null, args);
        } catch (RpcException e) {
            throw new JRecException("rpc-problem fetching transcoder recs", e);
        }
    }

    /**
    * return an approximate count of recs for the current filter
    */
    protected int fetchKeyCount(User user, HttpServletRequest req) throws JRecException {
        try {
            Transcoders xcoder_rpc = RpcFactory.newTranscoders(user);
            return xcoder_rpc.getKeyCount();
        } catch (RpcException e) {
            throw new JRecException("rpc-problem fetching transcoder rec count", e);
        }
    }

    /**
    * delete the record(s) for the specified key(s)
    */
    protected void deleteRecs(User user, HttpSession session, Vector keys) throws JRecException {
        try {
            Transcoders xcoder_rpc = RpcFactory.newTranscoders(user);
            xcoder_rpc.deleteRecs(keys);
        } catch (RpcException e) {
            throw new JRecException("rpc-problem deleting transcoder rec");
        }
    }

    /**
    * initialize the form from data in the request
    */
    protected void onLoad(User user, HttpServletRequest req, JRecTableForm form) throws JRecException {
    }

    /**
     * logging object
     */
    protected static Log log = LogFactory.getLog(TranscodersAction.class);
}
