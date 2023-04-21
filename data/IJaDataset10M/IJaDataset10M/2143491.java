package org.jwebsocket.plugins.system;

import java.util.List;
import java.util.Random;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import org.jwebsocket.api.PluginConfiguration;
import org.jwebsocket.api.WebSocketConnector;
import org.jwebsocket.config.JWebSocketCommonConstants;
import org.jwebsocket.config.JWebSocketServerConstants;
import org.jwebsocket.connectors.BaseConnector;
import org.jwebsocket.kit.BroadcastOptions;
import org.jwebsocket.kit.CloseReason;
import org.jwebsocket.logging.Logging;
import org.jwebsocket.kit.PlugInResponse;
import org.jwebsocket.plugins.TokenPlugIn;
import org.jwebsocket.security.SecurityFactory;
import org.jwebsocket.security.User;
import org.jwebsocket.token.BaseToken;
import org.jwebsocket.token.Token;
import org.jwebsocket.token.TokenFactory;
import org.jwebsocket.util.Tools;

/**
 * implements the jWebSocket system tokens like login, logout, send, broadcast
 * etc...
 * 
 * @author aschulze
 */
public class SystemPlugIn extends TokenPlugIn {

    private static Logger log = Logging.getLogger(SystemPlugIn.class);

    private static final String NS_SYSTEM_DEFAULT = JWebSocketServerConstants.NS_BASE + ".plugins.system";

    private static final String TT_SEND = "send";

    private static final String TT_BROADCAST = "broadcast";

    private static final String TT_WELCOME = "welcome";

    private static final String TT_GOODBYE = "goodBye";

    private static final String TT_LOGIN = "login";

    private static final String TT_LOGOUT = "logout";

    private static final String TT_CLOSE = "close";

    private static final String TT_GETCLIENTS = "getClients";

    private static final String TT_PING = "ping";

    private static final String TT_ECHO = "echo";

    private static final String TT_ALLOC_CHANNEL = "alloc";

    private static final String TT_DEALLOC_CHANNEL = "dealloc";

    private static final String VAR_GROUP = NS_SYSTEM_DEFAULT + ".group";

    private static boolean BROADCAST_OPEN = true;

    private static final String BROADCAST_OPEN_KEY = "broadcastOpenEvent";

    private static boolean BROADCAST_CLOSE = true;

    private static final String BROADCAST_CLOSE_KEY = "broadcastCloseEvent";

    private static boolean BROADCAST_LOGIN = true;

    private static final String BROADCAST_LOGIN_KEY = "broadcastLoginEvent";

    private static boolean BROADCAST_LOGOUT = true;

    private static final String BROADCAST_LOGOUT_KEY = "broadcastLogoutEvent";

    /**
	 * Default constructor
	 */
    public SystemPlugIn() {
        this(null);
    }

    /**
	 * Constructor with configuration object
	 */
    public SystemPlugIn(PluginConfiguration aConfiguration) {
        super(aConfiguration);
        if (log.isDebugEnabled()) {
            log.debug("Instantiating system plug-in...");
        }
        this.setNamespace(NS_SYSTEM_DEFAULT);
        mGetSettings();
    }

    private void mGetSettings() {
        BROADCAST_OPEN = "true".equals(getSetting(BROADCAST_OPEN_KEY, "true"));
        BROADCAST_CLOSE = "true".equals(getSetting(BROADCAST_CLOSE_KEY, "true"));
        BROADCAST_LOGIN = "true".equals(getSetting(BROADCAST_LOGIN_KEY, "true"));
        BROADCAST_LOGOUT = "true".equals(getSetting(BROADCAST_LOGOUT_KEY, "true"));
    }

    @Override
    public void processToken(PlugInResponse aResponse, WebSocketConnector aConnector, Token aToken) {
        String lType = aToken.getType();
        String lNS = aToken.getNS();
        if (lType != null && getNamespace().equals(lNS)) {
            if (lType.equals(TT_SEND)) {
                send(aConnector, aToken);
                aResponse.abortChain();
            } else if (lType.equals(TT_BROADCAST)) {
                broadcast(aConnector, aToken);
                aResponse.abortChain();
            } else if (lType.equals(TT_LOGIN)) {
                login(aConnector, aToken);
                aResponse.abortChain();
            } else if (lType.equals(TT_LOGOUT)) {
                logout(aConnector, aToken);
                aResponse.abortChain();
            } else if (lType.equals(TT_CLOSE)) {
                close(aConnector, aToken);
                aResponse.abortChain();
            } else if (lType.equals(TT_GETCLIENTS)) {
                getClients(aConnector, aToken);
                aResponse.abortChain();
            } else if (lType.equals(TT_PING)) {
                ping(aConnector, aToken);
            } else if (lType.equals(TT_ECHO)) {
                echo(aConnector, aToken);
            } else if (lType.equals(TT_ALLOC_CHANNEL)) {
                allocChannel(aConnector, aToken);
            } else if (lType.equals(TT_DEALLOC_CHANNEL)) {
                deallocChannel(aConnector, aToken);
            }
        }
    }

    @Override
    public void connectorStarted(WebSocketConnector aConnector) {
        Random lRand = new Random(System.nanoTime());
        aConnector.getSession().setSessionId(Tools.getMD5(aConnector.generateUID() + "." + lRand.nextInt()));
        sendWelcome(aConnector);
        broadcastConnectEvent(aConnector);
    }

    @Override
    public void connectorStopped(WebSocketConnector aConnector, CloseReason aCloseReason) {
        broadcastDisconnectEvent(aConnector);
    }

    private String getGroup(WebSocketConnector aConnector) {
        return aConnector.getString(VAR_GROUP);
    }

    private void setGroup(WebSocketConnector aConnector, String aGroup) {
        aConnector.setString(VAR_GROUP, aGroup);
    }

    private void removeGroup(WebSocketConnector aConnector) {
        aConnector.removeVar(VAR_GROUP);
    }

    /**
	 *
	 *
	 * @param aConnector
	 */
    public void broadcastConnectEvent(WebSocketConnector aConnector) {
        if (BROADCAST_OPEN) {
            if (log.isDebugEnabled()) {
                log.debug("Broadcasting connect...");
            }
            Token lConnect = TokenFactory.createToken(BaseToken.TT_EVENT);
            lConnect.setString("name", "connect");
            lConnect.setString("sourceId", aConnector.getId());
            String lNodeId = aConnector.getNodeId();
            if (lNodeId != null) {
                lConnect.setString("unid", lNodeId);
            }
            lConnect.setInteger("clientCount", getConnectorCount());
            broadcastToken(aConnector, lConnect);
        }
    }

    /**
	 *
	 *
	 * @param aConnector
	 */
    public void broadcastDisconnectEvent(WebSocketConnector aConnector) {
        if (BROADCAST_CLOSE) {
            if (log.isDebugEnabled()) {
                log.debug("Broadcasting disconnect...");
            }
            Token lDisconnect = TokenFactory.createToken(BaseToken.TT_EVENT);
            lDisconnect.setString("name", "disconnect");
            lDisconnect.setString("sourceId", aConnector.getId());
            String lNodeId = aConnector.getNodeId();
            if (lNodeId != null) {
                lDisconnect.setString("unid", lNodeId);
            }
            lDisconnect.setInteger("clientCount", getConnectorCount());
            broadcastToken(aConnector, lDisconnect);
        }
    }

    private void sendWelcome(WebSocketConnector aConnector) {
        if (log.isDebugEnabled()) {
            log.debug("Sending welcome...");
        }
        Token lWelcome = TokenFactory.createToken(TT_WELCOME);
        lWelcome.setString("ns", getNamespace());
        lWelcome.setString("vendor", JWebSocketCommonConstants.VENDOR);
        lWelcome.setString("version", JWebSocketServerConstants.VERSION_STR);
        lWelcome.setString("usid", aConnector.getSession().getSessionId());
        lWelcome.setString("sourceId", aConnector.getId());
        String lNodeId = aConnector.getNodeId();
        if (lNodeId != null) {
            lWelcome.setString("unid", lNodeId);
        }
        lWelcome.setInteger("timeout", aConnector.getEngine().getConfiguration().getTimeout());
        sendToken(aConnector, aConnector, lWelcome);
    }

    /**
	 *
	 */
    private void broadcastLoginEvent(WebSocketConnector aConnector) {
        if (BROADCAST_LOGIN) {
            if (log.isDebugEnabled()) {
                log.debug("Broadcasting login event...");
            }
            Token lLogin = TokenFactory.createToken(BaseToken.TT_EVENT);
            lLogin.setString("name", "login");
            lLogin.setString("username", getUsername(aConnector));
            lLogin.setInteger("clientCount", getConnectorCount());
            lLogin.setString("sourceId", aConnector.getId());
            String lNodeId = aConnector.getNodeId();
            if (lNodeId != null) {
                lLogin.setString("unid", lNodeId);
            }
            broadcastToken(aConnector, lLogin);
        }
    }

    /**
	 *
	 */
    private void broadcastLogoutEvent(WebSocketConnector aConnector) {
        if (BROADCAST_LOGOUT) {
            if (log.isDebugEnabled()) {
                log.debug("Broadcasting logout event...");
            }
            Token lLogout = TokenFactory.createToken(BaseToken.TT_EVENT);
            lLogout.setString("ns", getNamespace());
            lLogout.setString("name", "logout");
            lLogout.setString("username", getUsername(aConnector));
            lLogout.setInteger("clientCount", getConnectorCount());
            lLogout.setString("sourceId", aConnector.getId());
            String lNodeId = aConnector.getNodeId();
            if (lNodeId != null) {
                lLogout.setString("unid", lNodeId);
            }
            broadcastToken(aConnector, lLogout);
        }
    }

    /**
	 *
	 * @param aConnector
	 * @param aCloseReason
	 */
    private void sendGoodBye(WebSocketConnector aConnector, CloseReason aCloseReason) {
        if (log.isDebugEnabled()) {
            log.debug("Sending good bye...");
        }
        Token lGoodBye = TokenFactory.createToken(TT_GOODBYE);
        lGoodBye.setString("ns", getNamespace());
        lGoodBye.setString("vendor", JWebSocketCommonConstants.VENDOR);
        lGoodBye.setString("version", JWebSocketServerConstants.VERSION_STR);
        lGoodBye.setString("sourceId", aConnector.getId());
        if (aCloseReason != null) {
            lGoodBye.setString("reason", aCloseReason.toString().toLowerCase());
        }
        sendToken(aConnector, aConnector, lGoodBye);
    }

    private void login(WebSocketConnector aConnector, Token aToken) {
        Token lResponse = createResponse(aToken);
        String lUsername = aToken.getString("username");
        String lPassword = aToken.getString("password");
        String lSessionId = aToken.getString("usid");
        String lGroup = aToken.getString("group");
        Boolean lReturnRoles = aToken.getBoolean("getRoles", Boolean.FALSE);
        Boolean lReturnRights = aToken.getBoolean("getRights", Boolean.FALSE);
        if (log.isDebugEnabled()) {
            log.debug("Processing 'login' (username='" + lUsername + "', group='" + lGroup + "') from '" + aConnector + "'...");
        }
        if (lUsername != null) {
            User lUser = SecurityFactory.getUser(lUsername);
            lResponse.setString("username", lUsername);
            if (lSessionId != null) {
                lResponse.setString("usid", lSessionId);
            }
            lResponse.setString("sourceId", aConnector.getId());
            setUsername(aConnector, lUsername);
            setGroup(aConnector, lGroup);
            if (lUser != null) {
                if (lReturnRoles) {
                    lResponse.setList("roles", new FastList(lUser.getRoleIdSet()));
                }
                if (lReturnRights) {
                    lResponse.setList("rights", new FastList(lUser.getRightIdSet()));
                }
            }
        } else {
            lResponse.setInteger("code", -1);
            lResponse.setString("msg", "missing arguments for 'login' command");
        }
        sendToken(aConnector, aConnector, lResponse);
        if (lUsername != null) {
            broadcastLoginEvent(aConnector);
        }
    }

    private void logout(WebSocketConnector aConnector, Token aToken) {
        Token lResponse = createResponse(aToken);
        if (log.isDebugEnabled()) {
            log.debug("Processing 'logout' (username='" + getUsername(aConnector) + "') from '" + aConnector + "'...");
        }
        if (getUsername(aConnector) != null) {
            sendToken(aConnector, aConnector, lResponse);
            broadcastLogoutEvent(aConnector);
            lResponse.setString("sourceId", aConnector.getId());
            removeUsername(aConnector);
            removeGroup(aConnector);
        } else {
            lResponse.setInteger("code", -1);
            lResponse.setString("msg", "not logged in");
            sendToken(aConnector, aConnector, lResponse);
        }
    }

    private void send(WebSocketConnector aConnector, Token aToken) {
        if (!SecurityFactory.hasRight(getUsername(aConnector), NS_SYSTEM_DEFAULT + ".send")) {
            sendToken(aConnector, aConnector, createAccessDenied(aToken));
            return;
        }
        Token lResponse = createResponse(aToken);
        WebSocketConnector lTargetConnector;
        String lTargetId = aToken.getString("unid");
        if (lTargetId != null) {
            lTargetConnector = getNode(lTargetId);
        } else {
            lTargetId = aToken.getString("targetId");
            lTargetConnector = getConnector(lTargetId);
        }
        if (lTargetConnector != null) {
            if (log.isDebugEnabled()) {
                log.debug("Processing 'send' (username='" + getUsername(aConnector) + "') from '" + aConnector + "' to " + lTargetId + "...");
            }
            aToken.setString("sourceId", aConnector.getId());
            sendToken(aConnector, lTargetConnector, aToken);
        } else {
            log.warn("Target connector '" + lTargetId + "' not found.");
        }
    }

    private void broadcast(WebSocketConnector aConnector, Token aToken) {
        if (!SecurityFactory.hasRight(getUsername(aConnector), NS_SYSTEM_DEFAULT + ".broadcast")) {
            sendToken(aConnector, aConnector, createAccessDenied(aToken));
            return;
        }
        Token lResponse = createResponse(aToken);
        if (log.isDebugEnabled()) {
            log.debug("Processing 'broadcast' (username='" + getUsername(aConnector) + "') from '" + aConnector + "'...");
        }
        aToken.setString("sourceId", aConnector.getId());
        aToken.remove("usid");
        Boolean lIsSenderIncluded = aToken.getBoolean("senderIncluded", false);
        Boolean lIsResponseRequested = aToken.getBoolean("responseRequested", true);
        broadcastToken(aConnector, aToken, new BroadcastOptions(lIsSenderIncluded, lIsResponseRequested));
        if (lIsResponseRequested) {
            sendToken(aConnector, aConnector, lResponse);
        }
    }

    private void close(WebSocketConnector aConnector, Token aToken) {
        int lTimeout = aToken.getInteger("timeout", 0);
        if (lTimeout > 0) {
            sendGoodBye(aConnector, CloseReason.CLIENT);
        }
        if (getUsername(aConnector) != null) {
            broadcastLogoutEvent(aConnector);
        }
        removeUsername(aConnector);
        if (log.isDebugEnabled()) {
            log.debug("Closing client " + (lTimeout > 0 ? "with timeout " + lTimeout + "ms" : "immediately") + "...");
        }
        aConnector.stopConnector(CloseReason.CLIENT);
    }

    /**
	 *
	 * @param aToken
	 */
    private void echo(WebSocketConnector aConnector, Token aToken) {
        Token lResponse = createResponse(aToken);
        String lData = aToken.getString("data");
        if (lData != null) {
            if (log.isDebugEnabled()) {
                log.debug("echo " + lData);
            }
            lResponse.setString("data", lData);
        } else {
            lResponse.setInteger("code", -1);
            lResponse.setString("msg", "missing 'data' argument for 'echo' command");
        }
        sendToken(aConnector, aConnector, lResponse);
    }

    /**
	 *
	 * @param aConnector
	 * @param aToken
	 */
    public void ping(WebSocketConnector aConnector, Token aToken) {
        Boolean lEcho = aToken.getBoolean("echo", Boolean.TRUE);
        if (log.isDebugEnabled()) {
            log.debug("Processing 'Ping' (echo='" + lEcho + "') from '" + aConnector + "'...");
        }
        if (lEcho) {
            Token lResponse = createResponse(aToken);
            sendToken(aConnector, aConnector, lResponse);
        }
    }

    /**
	 *
	 * @param aConnector
	 * @param aToken
	 */
    public void getClients(WebSocketConnector aConnector, Token aToken) {
        Token lResponse = createResponse(aToken);
        if (log.isDebugEnabled()) {
            log.debug("Processing 'getClients' from '" + aConnector + "'...");
        }
        if (getUsername(aConnector) != null) {
            String lGroup = aToken.getString("group");
            Integer lMode = aToken.getInteger("mode", 0);
            FastMap lFilter = new FastMap();
            lFilter.put(BaseConnector.VAR_USERNAME, ".*");
            List<String> listOut = new FastList<String>();
            for (WebSocketConnector lConnector : getServer().selectConnectors(lFilter).values()) {
                listOut.add(getUsername(lConnector) + "@" + lConnector.getRemotePort());
            }
            lResponse.setList("clients", listOut);
            lResponse.setInteger("count", listOut.size());
        } else {
            lResponse.setInteger("code", -1);
            lResponse.setString("msg", "not logged in");
        }
        sendToken(aConnector, aConnector, lResponse);
    }

    /**
	 * allocates a "non-interruptable" communication channel between two clients.
	 *
	 * @param aConnector
	 * @param aToken
	 */
    public void allocChannel(WebSocketConnector aConnector, Token aToken) {
        Token lResponse = createResponse(aToken);
        if (log.isDebugEnabled()) {
            log.debug("Processing 'allocChannel' from '" + aConnector + "'...");
        }
    }

    /**
	 * deallocates a "non-interruptable" communication channel between two
	 * clients.
	 *
	 * @param aConnector
	 * @param aToken
	 */
    public void deallocChannel(WebSocketConnector aConnector, Token aToken) {
        Token lResponse = createResponse(aToken);
        if (log.isDebugEnabled()) {
            log.debug("Processing 'deallocChannel' from '" + aConnector + "'...");
        }
    }
}
