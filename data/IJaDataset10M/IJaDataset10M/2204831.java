package org.jwebsocket.plugins.rpc.rrpc;

import java.util.List;
import javolution.util.FastList;
import org.jwebsocket.api.WebSocketConnector;
import org.jwebsocket.plugins.rpc.AbstractRrpc;
import org.jwebsocket.plugins.rpc.CommonRpcPlugin;
import org.jwebsocket.plugins.rpc.RPCPlugIn;
import org.jwebsocket.plugins.rpc.rrpc.RrpcConnectorNotFoundException;
import org.jwebsocket.plugins.rpc.rrpc.RrpcRightNotGrantedException;
import org.jwebsocket.security.SecurityFactory;
import org.jwebsocket.token.Token;

/**
 * Class used to call a Rrpc method (S2C)
 * Example: new Rrpc.Call("aClass", "aMethod").send("hello", "it's a rrpc call", 123).from(aConnector).to(anotherConnector)
 *       or new Rrpc.Call("aClass", "aMethod").send(SomethingToSend).to(anotherConnector) (in this case, the sender will be the server)
 * @author Quentin Ambard
 */
public class Rrpc extends AbstractRrpc {

    private WebSocketConnector mConnectorFrom = null;

    private List<WebSocketConnector> mConnectorsTo;

    public Rrpc(String aClassname, String aMethod) {
        super(aClassname, aMethod);
    }

    public Rrpc(String aClassname, String aMethod, boolean aSpawnTread) {
        super(aClassname, aMethod, aSpawnTread);
    }

    /**
		 * The token should contains all the necessary informations. 
		 * Can be usefull to create a direct call from an already-created token
		 * @param aToken
		 * @throws RrpcRightNotGrantedException
		 * @throws RrpcConnectorNotFoundException
		 */
    public Rrpc(Token aToken) throws RrpcRightNotGrantedException {
        super(aToken);
        String lConnectorFromId = aToken.getString(CommonRpcPlugin.RRPC_KEY_SOURCE_ID);
        if (lConnectorFromId != null) {
            from(lConnectorFromId);
        }
    }

    /**
		 * Eventually, the connector the rrpc comes from.
		 * If this method is not called during the rrpc, the server will be the source.
		 * @param aConnector
		 * @throws RrpcRightNotGrantedException
		 */
    public Rrpc from(WebSocketConnector aConnector) throws RrpcRightNotGrantedException {
        mConnectorFrom = aConnector;
        if (mConnectorFrom != null && !SecurityFactory.hasRight(RPCPlugIn.getUsernameStatic(mConnectorFrom), CommonRpcPlugin.NS_RPC_DEFAULT + "." + CommonRpcPlugin.RRPC_RIGHT_ID)) {
            throw new RrpcRightNotGrantedException();
        }
        return this;
    }

    /**
		 * Eventually, the connectorId the rrpc comes from.
		 * If this method is not called during the rrpc, the server will be the source.
		 * @param aConnector
		 * @throws RrpcRightNotGrantedException
		 */
    public Rrpc from(String aConnectorId) throws RrpcRightNotGrantedException {
        return from(RPCPlugIn.getConnector("tcp0", aConnectorId));
    }

    /**
		 * The connector you want to send the rrpc
		 * @param aConnector
		 */
    public Rrpc to(WebSocketConnector aConnector) {
        if (mConnectorsTo == null) {
            mConnectorsTo = new FastList<WebSocketConnector>();
        }
        mConnectorsTo.add(aConnector);
        return this;
    }

    /**
		 * The connectors you want to send the rrpc
		 * @param aConnector
		 */
    public Rrpc toConnectors(List<WebSocketConnector> aConnectors) {
        mConnectorsTo = aConnectors;
        return this;
    }

    /**
		 * The connectorId you want to send the rrpc
		 * @param aConnector
		 */
    public Rrpc to(String aConnectorId) {
        WebSocketConnector lConnector = RPCPlugIn.getConnector("tcp0", aConnectorId);
        to(lConnector);
        return this;
    }

    /**
		 * The connectorsId you want to send the rrpc
		 * @param aConnector
		 * @throws RrpcConnectorNotFoundException
		 */
    public Rrpc to(List<String> aConnectorsId) {
        mConnectorsTo = new FastList<WebSocketConnector>();
        for (String lConnectorId : aConnectorsId) {
            WebSocketConnector lConnector = RPCPlugIn.getConnector("tcp0", lConnectorId);
            mConnectorsTo.add(lConnector);
        }
        return this;
    }

    public Token call() {
        Token lToken = super.call();
        String idConnectorFrom;
        if (mConnectorFrom == null) {
            idConnectorFrom = CommonRpcPlugin.SERVER_ID;
        } else {
            idConnectorFrom = mConnectorFrom.getId();
        }
        lToken.setString(CommonRpcPlugin.RRPC_KEY_SOURCE_ID, idConnectorFrom);
        RPCPlugIn.processRrpc(mConnectorFrom, mConnectorsTo, lToken);
        return lToken;
    }
}
