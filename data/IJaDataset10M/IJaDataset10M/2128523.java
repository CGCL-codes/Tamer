package jreceiver.server.rpc;

import java.util.Hashtable;
import java.util.Vector;
import jreceiver.common.JRecSecurityException;
import jreceiver.common.rec.security.MethodKey;
import jreceiver.common.rec.security.User;
import jreceiver.common.rpc.RoleAuths;
import jreceiver.common.rpc.RpcException;
import jreceiver.server.bus.BusException;
import jreceiver.server.bus.RoleAuthBus;

/**
 * Direct interface for reading and writing RoleAuths into the RoleAuths
 * table
 *
 * @author Reed Esau
 * @version $Revision: 1.4 $ $Date: 2002/12/29 00:44:09 $
 */
public class RoleAuthsDirect extends ServerTableDirect implements RoleAuths {

    public RoleAuthsDirect(User user) {
        super(user, RoleAuthBus.getInstance());
    }

    public RoleAuthBus getBus() {
        return (RoleAuthBus) getBaseBus();
    }

    /** handler name needed for authorization */
    public final String getHandlerName() {
        return RoleAuths.HANDLER_NAME;
    }

    /**
     * Obtain a total count of command keys for a role_auth
     * <p>
     * Recommended when traversing large lists.
     */
    public int getKeyCountForRole(String role_id) throws RpcException {
        try {
            authorize(RoleAuths.GET_KEY_COUNT_FOR_ROLE);
            return getBus().getKeyCountForRole(role_id);
        } catch (JRecSecurityException e) {
            throw new RpcException("security-problem getting key count for role_auth", e);
        } catch (BusException e) {
            throw new RpcException("bus-problem getting key count for role_auth", e);
        }
    }

    /**
     * obtain an ordered range of keys for the specified filter
     */
    public Vector getKeysForRole(String role_id, String order_by, int rec_offset, int rec_count) throws RpcException {
        try {
            authorize(RoleAuths.GET_KEYS_FOR_ROLE);
            return getBus().getKeysForRole(role_id, order_by, rec_offset, rec_count);
        } catch (JRecSecurityException e) {
            throw new RpcException("security-problem getting keys for role_auth", e);
        } catch (BusException e) {
            throw new RpcException("bus-problem getting keys for role_auth", e);
        }
    }

    /**
     * plural - obtain an ordered range of keys for the specified role
     */
    public Vector getRecsForMethods(User user, Vector method_keys, String order_by, Hashtable args) throws RpcException {
        try {
            authorize(RoleAuths.GET_RECS_FOR_METHODS);
            return getBus().getRecsForMethods(user, method_keys, order_by, args);
        } catch (JRecSecurityException e) {
            throw new RpcException("security-problem getting recs for methods", e);
        } catch (BusException e) {
            throw new RpcException("bus-problem getting recs for methods", e);
        }
    }

    /** */
    public boolean isAuthorized(User user, MethodKey method) throws RpcException {
        try {
            authorize(RoleAuths.IS_AUTHORIZED);
            return getBus().isAuthorized(user, method);
        } catch (JRecSecurityException e) {
            throw new RpcException("security-problem getting rec for method", e);
        } catch (BusException e) {
            throw new RpcException("bus-problem getting rec for method", e);
        }
    }
}
