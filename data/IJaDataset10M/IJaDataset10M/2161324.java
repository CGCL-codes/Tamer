package jreceiver.common.callback.rpc.xmlrpc;

import java.util.Vector;
import java.util.Hashtable;
import jreceiver.common.callback.rpc.CommandListener;
import jreceiver.common.callback.rpc.CommandListenerDirect;
import jreceiver.common.rec.driver.*;
import jreceiver.common.rpc.xmlrpc.XmlRpcBaseHandler;

/**
 * XML-RPC interface for querying device control.
 *
 * @author Reed Esau
 * @version $Revision: 1.3 $ $Date: 2002/07/31 11:29:41 $
 */
public abstract class CommandListenerHandler extends XmlRpcBaseHandler {

    /** XML-RPC exports */
    public static final boolean forwardCommand_public = true;

    /** XML-RPC help strings */
    public static final String forwardCommand_help = "forward a command";

    /**
     * Invoke the named direct method with the params provided.
     * <p>
     * If necessary, prepare the return value for the journey back to the client.
     */
    public final Object executeDirect(CommandListenerDirect direct, String method, Vector params) throws Exception {
        if (CommandListener.FORWARD_COMMAND.equals(method)) {
            Hashtable device_hash = (Hashtable) params.elementAt(0);
            Hashtable command_hash = (Hashtable) params.elementAt(1);
            direct.forwardCommand(new DeviceRec(device_hash), new CommandRec(command_hash));
            return new Integer(0);
        } else throw new Exception("Method [" + method + "] not supported!");
    }
}
