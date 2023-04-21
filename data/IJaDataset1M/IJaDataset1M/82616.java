package jreceiver.common.callback.rpc;

import java.net.URL;
import jreceiver.common.callback.rpc.xmlrpc.*;
import jreceiver.common.rec.security.User;
import jreceiver.common.rpc.RpcException;
import jreceiver.common.rpc.RpcFactoryBase;

/**
 * Factory to create an object for the server to execute XML-RPC
 * (or other protocol) queries to a driver or player.
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.5 $ $Date: 2002/12/29 00:44:08 $
 */
public final class RpcCallbackFactory extends RpcFactoryBase {

    private static final String class_root = "jreceiver.common.callback.rpc.";

    /** produce an object from the factory, using the default host */
    public static StatusListener newStatusListener(User user) throws RpcException {
        return newStatusListener(null, user);
    }

    /** produce an object from the factory, using the specified host */
    public static StatusListener newStatusListener(URL host, User user) throws RpcException {
        if (host == null) host = m_default_host;
        if (host == null) return (StatusListener) loadDirect(class_root + "StatusListenerDirect", user); else return new StatusListenerImpl(host, user);
    }

    /** produce an object from the factory, using the default host */
    public static SettingListener newSettingListener(User user) throws RpcException {
        return newSettingListener(null, user);
    }

    /** produce an object from the factory, using the specified host */
    public static SettingListener newSettingListener(URL host, User user) throws RpcException {
        if (host == null) host = m_default_host;
        if (host == null) return (SettingListener) loadDirect(class_root + "SettingListenerDirect", user); else return new SettingListenerImpl(host, user);
    }

    /** produce an object from the factory, using the default host */
    public static ControllerListener newControllerListener(User user) throws RpcException {
        return newControllerListener(null, user);
    }

    /** produce an object from the factory, using the specified host */
    public static ControllerListener newControllerListener(URL host, User user) throws RpcException {
        if (host == null) host = m_default_host;
        if (host == null) return (ControllerListener) loadDirect(class_root + "ControllerListenerDirect", user); else return new ControllerListenerImpl(host, user);
    }

    /** produce an object from the factory, using the default host */
    public static CommandListener newCommandListener(User user) throws RpcException {
        return newCommandListener(null, user);
    }

    /** produce an object from the factory, using the specified host */
    public static CommandListener newCommandListener(URL host, User user) throws RpcException {
        if (host == null) host = m_default_host;
        if (host == null) return (CommandListener) loadDirect(class_root + "CommandListenerDirect", user); else return new CommandListenerImpl(host, user);
    }
}
