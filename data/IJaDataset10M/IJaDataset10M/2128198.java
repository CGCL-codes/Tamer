package jreceiver.server.rpc.xmlrpc;

import java.util.Hashtable;
import java.util.ListIterator;
import java.util.Vector;
import jreceiver.common.rec.BaseRec;
import jreceiver.common.rec.Key;
import jreceiver.common.rec.Rec;
import jreceiver.common.rec.RecException;
import jreceiver.common.rec.security.MethodKey;
import jreceiver.common.rec.security.RoleAuthKey;
import jreceiver.common.rec.security.RoleAuthRec;
import jreceiver.common.rec.security.User;
import jreceiver.common.rec.security.UserRec;
import jreceiver.common.rpc.RoleAuths;
import jreceiver.server.rpc.RoleAuthsDirect;

/**
 * Handle role authorization queries and updates via XML-RPC
 *
 * @author Reed Esau
 * @version $Revision: 1.3 $ $Date: 2002/09/25 00:00:13 $
 */
public class RoleAuthsHandler extends ServerTableHandler {

    /**
     * return true if reconstituteKey() will return a meaningful value
     */
    public boolean keysAreHashable() {
        return true;
    }

    /**
     * transform a key in hash form to object form
     */
    public Key reconstituteKey(Hashtable hashed_key) {
        return new RoleAuthKey(hashed_key);
    }

    /**
     * transform a rec in hash form to object form
     */
    public Rec reconstituteRec(Hashtable hashed_rec) throws RecException {
        return new RoleAuthRec(hashed_rec);
    }

    /**
     * Invoke the named direct method with the params provided.
     * <p>
     * If necessary, prepare the return value for the journey back to the client.
     */
    public final Object executeHandler(String method, Vector params, User user) throws Exception {
        RoleAuthsDirect direct = new RoleAuthsDirect(user);
        if (RoleAuths.IS_AUTHORIZED.equals(method)) {
            User xuser = new UserRec((Hashtable) params.get(0));
            MethodKey method_key = new MethodKey((Hashtable) params.get(1));
            return new Boolean(direct.isAuthorized(xuser, method_key));
        } else if (RoleAuths.GET_RECS_FOR_METHODS.equals(method)) {
            User xuser = new UserRec((Hashtable) params.get(0));
            Vector method_keys = (Vector) params.get(1);
            String order_by = (String) params.get(2);
            Hashtable args = (Hashtable) params.get(3);
            ListIterator it = method_keys.listIterator();
            while (it.hasNext()) {
                Hashtable hash = (Hashtable) it.next();
                it.set(new MethodKey(hash));
            }
            Vector recs = direct.getRecsForMethods(xuser, method_keys, order_by, args);
            BaseRec.dissolve(recs);
            return recs;
        } else if (RoleAuths.GET_KEY_COUNT_FOR_ROLE.equals(method)) {
            String role_id = (String) params.get(0);
            int count = direct.getKeyCountForRole(role_id);
            return new Integer(count);
        } else if (RoleAuths.GET_KEYS_FOR_ROLE.equals(method)) {
            String role_id = (String) params.get(0);
            String order_by = (String) params.get(1);
            int rec_offset = getInt(params, 2);
            int rec_count = getInt(params, 3);
            Vector keys = direct.getKeysForRole(role_id, order_by, rec_offset, rec_count);
            return BaseRec.dissolve(keys);
        }
        return super.executeDirect(direct, method, params);
    }
}
