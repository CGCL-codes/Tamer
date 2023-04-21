package nakayo.gameserver.network.loginserver;

import com.aionemu.commons.network.Dispatcher;
import com.aionemu.commons.network.NioServer;
import nakayo.gameserver.configs.network.NetworkConfig;
import nakayo.gameserver.model.account.Account;
import nakayo.gameserver.model.account.AccountTime;
import nakayo.gameserver.network.aion.AionConnection;
import nakayo.gameserver.network.aion.serverpackets.SM_L2AUTH_LOGIN_CHECK;
import nakayo.gameserver.network.aion.serverpackets.SM_RECONNECT_KEY;
import nakayo.gameserver.network.loginserver.LoginServerConnection.State;
import nakayo.gameserver.network.loginserver.serverpackets.*;
import nakayo.gameserver.services.AccountService;
import nakayo.gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utill class for connecting GameServer to LoginServer.
 *
 * @author -Nemesiss-, PZIKO333
 */
public class LoginServer {

    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(LoginServer.class);

    /**
     * Map<accountId,Connection> for waiting request. This request is send to LoginServer and GameServer is waiting for
     * response.
     */
    private Map<Integer, AionConnection> loginRequests = new HashMap<Integer, AionConnection>();

    /**
     * Map<accountId,Connection> for all logged in accounts.
     */
    private Map<Integer, AionConnection> loggedInAccounts = new HashMap<Integer, AionConnection>();

    /**
     * Connection to LoginServer.
     */
    private LoginServerConnection loginServer;

    private NioServer nioServer;

    private boolean serverShutdown = false;

    public static final LoginServer getInstance() {
        return SingletonHolder.instance;
    }

    private LoginServer() {
    }

    public void setNioServer(NioServer nioServer) {
        this.nioServer = nioServer;
    }

    /**
     * Connect to LoginServer and return object representing this connection. This method is blocking and may block till
     * connect successful.
     *
     * @return LoginServerConnection
     */
    public LoginServerConnection connect() {
        SocketChannel sc;
        for (; ; ) {
            loginServer = null;
            log.info("Connecting to LoginServer: " + NetworkConfig.LOGIN_ADDRESS);
            try {
                sc = SocketChannel.open(NetworkConfig.LOGIN_ADDRESS);
                sc.configureBlocking(false);
                Dispatcher d = nioServer.getReadWriteDispatcher();
                loginServer = new LoginServerConnection(sc, d);
                return loginServer;
            } catch (Exception e) {
                log.info("Cant connect to LoginServer: " + e.getMessage());
            }
        }
    }

    /**
     * This method is called when we lost connection to LoginServer. We will disconnects all aionClients waiting for
     * LoginServer response and also try reconnect to LoginServer.
     */
    public void loginServerDown() {
        log.warn("Connection with LoginServer lost...");
        loginServer = null;
        synchronized (this) {
            for (AionConnection client : loginRequests.values()) {
                client.close(true);
            }
            loginRequests.clear();
        }
        if (!serverShutdown) {
            ThreadPoolManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    connect();
                }
            }, 5000);
        }
    }

    /**
     * Notify that client is disconnected - we must clear waiting request to LoginServer if any to prevent leaks. Also
     * notify LoginServer that this account is no longer on GameServer side.
     *
     * @param client
     */
    public void aionClientDisconnected(int accountId) {
        synchronized (this) {
            loginRequests.remove(accountId);
            loggedInAccounts.remove(accountId);
        }
        sendAccountDisconnected(accountId);
    }

    /**
     * @param accountId
     */
    private void sendAccountDisconnected(int accountId) {
        log.info("Sending account disconnected " + accountId);
        if (loginServer != null && loginServer.getState() == State.AUTHED) {
            loginServer.sendPacket(new SM_ACCOUNT_DISCONNECTED(accountId));
        }
    }

    /**
     * Starts authentication procedure of this client - LoginServer will sends response with information about account
     * name if authentication is ok.
     *
     * @param accountId
     * @param client
     * @param loginOk
     * @param playOk1
     * @param playOk2
     */
    public void requestAuthenticationOfClient(int accountId, AionConnection client, int loginOk, int playOk1, int playOk2) {
        if (loginServer == null || loginServer.getState() != State.AUTHED) {
            log.warn("LS !!! " + (loginServer == null ? "NULL" : loginServer.getState()));
            client.close(true);
            return;
        }
        synchronized (this) {
            if (loginRequests.containsKey(accountId)) {
                return;
            }
            loginRequests.put(accountId, client);
        }
        loginServer.sendPacket(new SM_ACCOUNT_AUTH(accountId, loginOk, playOk1, playOk2));
    }

    /**
     * This method is called by CM_ACCOUNT_AUTH_RESPONSE LoginServer packets to notify GameServer about results of
     * client authentication.
     *
     * @param accountId
     * @param accountName
     * @param result
     * @param accountTime
     */
    public void accountAuthenticationResponse(int accountId, String accountName, boolean result, AccountTime accountTime, byte accessLevel, byte membership, int toll_count) {
        AionConnection client = loginRequests.remove(accountId);
        if (client == null) {
            return;
        }
        if (result) {
            client.setState(AionConnection.State.AUTHED);
            loggedInAccounts.put(accountId, client);
            log.info("Account authed: " + accountId + " = " + accountName);
            client.setAccount(AccountService.getAccount(accountId, accountName, accountTime, accessLevel, membership, toll_count));
            client.sendPacket(new SM_L2AUTH_LOGIN_CHECK(true));
        } else {
            log.info("Account not authed: " + accountId);
            client.close(new SM_L2AUTH_LOGIN_CHECK(false), true);
        }
    }

    /**
     * Starts reconnection to LoginServer procedure. LoginServer in response will send reconnection key.
     *
     * @param client
     */
    public void requestAuthReconnection(AionConnection client) {
        if (loginServer == null || loginServer.getState() != State.AUTHED) {
            client.close(false);
            return;
        }
        synchronized (this) {
            if (loginRequests.containsKey(client.getAccount().getId())) {
                return;
            }
            loginRequests.put(client.getAccount().getId(), client);
        }
        loginServer.sendPacket(new SM_ACCOUNT_RECONNECT_KEY(client.getAccount().getId()));
    }

    /**
     * This method is called by CM_ACCOUNT_RECONNECT_KEY LoginServer packets to give GameServer reconnection key for
     * client that was requesting reconnection.
     *
     * @param accountId
     * @param reconnectKey
     */
    public void authReconnectionResponse(int accountId, int reconnectKey) {
        AionConnection client = loginRequests.remove(accountId);
        if (client == null) {
            return;
        }
        log.info("Account reconnecting: " + accountId + " = " + client.getAccount().getName());
        client.close(new SM_RECONNECT_KEY(reconnectKey), false);
    }

    /**
     * This method is called by CM_REQUEST_KICK_ACCOUNT LoginServer packets to request GameServer to disconnect client
     * with given account id.
     *
     * @param accountId
     */
    public void kickAccount(int accountId) {
        synchronized (this) {
            AionConnection client = loggedInAccounts.get(accountId);
            if (client != null) {
                closeClientWithCheck(client, accountId);
            } else {
                sendAccountDisconnected(accountId);
            }
        }
    }

    private void closeClientWithCheck(AionConnection client, final int accountId) {
        log.info("Closing client connection " + accountId);
        client.close(false);
        ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                AionConnection client = loggedInAccounts.get(accountId);
                if (client != null) {
                    log.warn("Removing client from server because of stalled connection");
                    client.close(false);
                    loggedInAccounts.remove(accountId);
                    sendAccountDisconnected(accountId);
                }
            }
        }, 5000);
    }

    /**
     * Returns unmodifiable map with accounts that are logged in to current GS Map Key: Account ID Map Value:
     * AionConnectionObject
     *
     * @return unmodifiable map wwith accounts
     */
    public Map<Integer, AionConnection> getLoggedInAccounts() {
        return Collections.unmodifiableMap(loggedInAccounts);
    }

    /**
     * When Game Server shutdown, have to close all pending client connection
     */
    public void gameServerDisconnected() {
        synchronized (this) {
            serverShutdown = true;
            for (AionConnection client : loginRequests.values()) {
                client.close(true);
            }
            loginRequests.clear();
            loginServer.close(false);
        }
        log.info("GameServer disconnected from the Login Server...");
    }

    public void sendLsControlPacket(String accountName, String playerName, String adminName, int param, int type) {
        if (loginServer != null && loginServer.getState() == State.AUTHED) {
            loginServer.sendPacket(new SM_LS_CONTROL(accountName, playerName, adminName, param, type));
        }
    }

    public void accountUpdate(int accountId, byte param, int type) {
        synchronized (this) {
            AionConnection client = loggedInAccounts.get(accountId);
            if (client != null) {
                Account account = client.getAccount();
                if (type == 1) {
                    account.setAccessLevel(param);
                }
                if (type == 2) {
                    account.setMembership(param);
                }
            }
        }
    }

    public void sendBanPacket(byte type, int accountId, String ip, int time, int adminObjId) {
        if (loginServer != null && loginServer.getState() == State.AUTHED) {
            loginServer.sendPacket(new SM_BAN(type, accountId, ip, time, adminObjId));
        }
    }

    public void sendTollInfo(int toll_count, String accountName) {
        if (loginServer != null && loginServer.getState() == State.AUTHED) {
            loginServer.sendPacket(new SM_ACCOUNT_TOLL_INFO(toll_count, accountName));
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final LoginServer instance = new LoginServer();
    }
}
