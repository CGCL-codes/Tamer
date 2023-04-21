package de.tudresden.inf.rn.mobilis.android.xhunt.proxy;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import de.tudresden.inf.rn.mobilis.android.xhunt.Const;
import de.tudresden.inf.rn.mobilis.android.xhunt.service.XHuntService;
import de.tudresden.inf.rn.mobilis.mxa.ConstMXA.MessageItems;
import de.tudresden.inf.rn.mobilis.mxa.IXMPPService;
import de.tudresden.inf.rn.mobilis.mxa.MXAController;
import de.tudresden.inf.rn.mobilis.mxa.MXAListener;
import de.tudresden.inf.rn.mobilis.mxa.parcelable.XMPPIQ;
import de.tudresden.inf.rn.mobilis.mxa.services.callbacks.IFileCallback;
import de.tudresden.inf.rn.mobilis.mxa.services.multiuserchat.IMultiUserChatService;

/**
 * The Class MXAProxy is a wrapper to simplify the handling with the MXA.
 */
public class MXAProxy implements MXAListener {

    /** The Constant TAG for logging. */
    public static final String TAG = "MXAProxy";

    /** The applications context. */
    private Context context;

    /** The xmpp service. */
    private IXMPPService iXMPPService;

    /** The connection messenger. */
    private Messenger mConnectMessenger;

    /** The disconnection messenger. */
    private Messenger mDisconnectMessenger;

    /** The messenger if an IQ acknowledgment was send. */
    private Messenger sendIQAckMessenger;

    /** The messenger if an IQ result was send. */
    private Messenger sendIQResMessenger;

    /** The IQProxy to wrap the handling using IQs. */
    private IQProxy mIQProxy;

    /** The xmpp connect handlers to notify if xmpp is connected. */
    private ArrayList<Handler> xmppConnectHandlers;

    /** The xmpp disconnect handlers to notify if xmpp is disconnected. */
    private ArrayList<Handler> xmppDisconnectHandlers;

    /** True if game is in static mode (for testing purposes only). */
    private boolean isStaticMode;

    /** The nickname of the player. */
    private String nickname = "";

    /** The IQ cache to cache the IQs before sending (packetId, xmppIq). */
    private ConcurrentHashMap<String, XMPPIQ> mIqCache;

    /** True if user chat was connected successfully. */
    private boolean mIsMUCConnected;

    /** The handler if an IQ acknowledgment was send. */
    private Handler sendIQAckHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
        }
    };

    /** The handler if an IQ result was send. */
    private Handler sendIQResHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
        }
    };

    /**
	 * Instantiates a new MXAProxy.
	 *
	 * @param xhuntService the XHuntService of the game
	 */
    public MXAProxy(XHuntService xhuntService) {
        this.context = xhuntService.getApplicationContext();
        this.mIQProxy = new IQProxy(this, xhuntService);
        mIqCache = new ConcurrentHashMap<String, XMPPIQ>();
        xmppConnectHandlers = new ArrayList<Handler>();
        xmppDisconnectHandlers = new ArrayList<Handler>();
        mIsMUCConnected = false;
    }

    /**
	 * Connect to MXA and to XMPP server.
	 *
	 * @throws RemoteException the remote exception
	 */
    public void connect() throws RemoteException {
        connectToMXA();
    }

    /**
	 * Connect to user chat.
	 *
	 * @param roomID the room id of the chat
	 * @param password the password of the chat
	 * @return true, if successful
	 * @throws RemoteException the remote exception if something goes wrong
	 */
    public boolean connectToMUC(String roomID, String password) throws RemoteException {
        if (this.isConnected()) {
            iXMPPService.getMultiUserChatService();
            if (roomID != null && password != null && this.getNickname() != null) {
                iXMPPService.getMultiUserChatService().joinRoom(roomID, password);
                iXMPPService.getMultiUserChatService().changeNickname(roomID, (this.getNickname() != null && this.getNickname().length() > 0) ? this.getNickname() : this.getXmppJid());
            }
            this.mIsMUCConnected = true;
            return true;
        }
        return false;
    }

    /**
	 * Connect to MXA.
	 *
	 * @throws RemoteException the remote exception if something goes wrong
	 */
    private void connectToMXA() throws RemoteException {
        if (iXMPPService == null) {
            mConnectMessenger = new Messenger(xmppResultConnectHandler);
            mDisconnectMessenger = new Messenger(xmppResultDisconnectHandler);
            sendIQAckMessenger = new Messenger(sendIQAckHandler);
            sendIQResMessenger = new Messenger(sendIQResHandler);
            MXAController.get().connectMXA(context, this);
        } else if (!iXMPPService.isConnected()) {
            connectXMPPService();
        }
    }

    /**
	 * Connect XMPP service.
	 */
    private void connectXMPPService() {
        if (iXMPPService != null) {
            try {
                iXMPPService.connect(mConnectMessenger);
            } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), "MXA Remote Service couldn't connect to XMPP Server");
            }
        }
    }

    /**
	 * Disconnect.
	 */
    public void disconnect() {
        disconnectXMPPService();
    }

    /**
	 * Disconnect XMPP service.
	 */
    private void disconnectXMPPService() {
        if (iXMPPService != null) {
            try {
                iXMPPService.disconnect(mDisconnectMessenger);
            } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), "MXA Remote Service couldn't disconnected from XMPP Server");
            }
        }
    }

    /**
	 * Gets the IQProxy.
	 *
	 * @return the IQProxy
	 */
    public IQProxy getIQProxy() {
        return this.mIQProxy;
    }

    /**
	 * Gets the nickname.
	 * 
	 * @return the nickname
	 */
    public String getNickname() {
        return (nickname != null && nickname.length() > 0) ? nickname : getXmppJid().substring(0, getXmppJid().indexOf("@"));
    }

    /**
	 * Gets the MultiUserChatService.
	 * 
	 * @return the MultiUserChatService
	 * 
	 * @throws RemoteException the remote exception if something goes wrong
	 */
    public IMultiUserChatService getMultiUserChatService() throws RemoteException {
        return iXMPPService.getMultiUserChatService();
    }

    /**
	 * Gets the own XMPP jid.
	 *
	 * @return the own XMPP jid
	 */
    public String getXmppJid() {
        String jid = "";
        try {
            if (this.isConnected()) jid = iXMPPService.getUsername();
        } catch (RemoteException e) {
        }
        return jid;
    }

    /**
	 * Gets the XMPP service.
	 *
	 * @return the XMPP service
	 */
    public IXMPPService getXMPPService() {
        return this.iXMPPService;
    }

    /**
	 * Checks if XMPP service is connected.
	 *
	 * @return true, if it is connected
	 */
    public boolean isConnected() {
        if (iXMPPService != null) {
            try {
                return iXMPPService.isConnected();
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    /**
	 * Checks if user chat is connected.
	 *
	 * @return true, if user chat is connected
	 */
    public boolean isMUCConnected() {
        return this.mIsMUCConnected;
    }

    /**
	 * Checks if game is running in static mode.
	 *
	 * @return true, if game is in static mode
	 */
    public boolean isStaticMode() {
        return this.isStaticMode;
    }

    @Override
    public void onMXAConnected() {
        iXMPPService = MXAController.get().getXMPPService();
        connectXMPPService();
    }

    @Override
    public void onMXADisconnected() {
    }

    /**
	 * Register file callback for filetransfers.
	 *
	 * @param fileCallback the file callback
	 * @throws RemoteException the remote exception if something goes wron
	 */
    public void registerFileCallback(IFileCallback fileCallback) throws RemoteException {
        if (isConnected()) iXMPPService.getFileTransferService().registerFileCallback(fileCallback);
    }

    /**
	 * Register an observer for incoming chat messages.
	 *
	 * @param activity the activity which want to be notified if a chat messges arrives
	 * @param resultHandler the result handler
	 * @param filter the filter
	 */
    public void registerIncomingMessageObserver(Activity activity, final Handler resultHandler, final String filter) {
        final Cursor msgCursor = activity.getContentResolver().query(MessageItems.CONTENT_URI, null, null, null, MessageItems.DEFAULT_SORT_ORDER);
        activity.startManagingCursor(msgCursor);
        ContentObserver co = new ContentObserver(resultHandler) {

            @Override
            public void onChange(boolean selfChange) {
                msgCursor.requery();
                msgCursor.moveToLast();
                String sender = msgCursor.getString(msgCursor.getColumnIndex(MessageItems.SENDER));
                sender = sender.replace(filter, "");
                sender = sender.replace("/", "");
                String body = msgCursor.getString(msgCursor.getColumnIndex(MessageItems.BODY));
                Message message = new Message();
                if (sender.length() > 0) {
                    if (body.length() > Const.MUC_NOTIFIER_MESSAGE_LENGTH) {
                        body = body.substring(0, Const.MUC_NOTIFIER_MESSAGE_LENGTH) + "...";
                    }
                    message.obj = sender + ": " + body;
                }
                resultHandler.sendMessage(message);
                super.onChange(selfChange);
            }
        };
        msgCursor.registerContentObserver(co);
    }

    /**
	 * Register XMPP connection handler.
	 *
	 * @param h the h
	 */
    public void registerXMPPConnectHandler(Handler h) {
        this.xmppConnectHandlers.add(h);
    }

    /**
	 * Unregister file callback.
	 *
	 * @param fileCallback the file callback
	 * @throws RemoteException the remote exception if something goes wrong
	 */
    public void unregisterFileCallback(IFileCallback fileCallback) throws RemoteException {
        if (isConnected()) iXMPPService.getFileTransferService().unregisterFileCallback(fileCallback);
    }

    /**
	 * Unregister XMPP connection handler.
	 *
	 * @param h the handler
	 */
    public void unregisterXMPPConnectHandler(Handler h) {
        this.xmppConnectHandlers.remove(h);
    }

    /**
	 * Register XMPP disconnection handler.
	 *
	 * @param h the handler
	 */
    public void registerXMPPDisconnectHandler(Handler h) {
        this.xmppDisconnectHandlers.add(h);
    }

    /**
	 * Send an XMPPIQ using the IQ cache.
	 *
	 * @param xmppIq the xmpp iq
	 */
    public void sendIQ(XMPPIQ xmppIq) {
        Log.v(TAG, "Send IQ: ID=" + xmppIq.packetID + " ns=" + xmppIq.namespace + " type=" + xmppIq.type + " to=" + xmppIq.to + " payload=" + xmppIq.payload);
        try {
            Log.v(TAG, ">>>>>>cached iq: " + xmppIq.packetID);
            mIqCache.put(xmppIq.packetID, xmppIq);
            if (this.isConnected()) {
                this.sendIQsFromCache();
            } else {
                this.connect();
                Log.v(TAG, "iXMPPConnected?: " + this.isConnected());
            }
        } catch (RemoteException e1) {
            Log.e(TAG, "ERROR while sending IQ: " + xmppIq.payload);
        }
    }

    /**
	 * Send all IQs waiting in cache.
	 *
	 * @throws RemoteException the remote exception if something goes wrong
	 */
    private void sendIQsFromCache() throws RemoteException {
        for (String packetId : mIqCache.keySet()) {
            iXMPPService.sendIQ(sendIQAckMessenger, sendIQResMessenger, 0, mIqCache.remove(packetId));
        }
    }

    /**
	 * Sets the nickname.
	 * 
	 * @param nick the new nickname
	 */
    public void setNickname(String nick) {
        this.nickname = nick;
    }

    /**
	 * Sets the static mode for this game.
	 *
	 * @param isStaticMode true for playing in static mode
	 */
    public void setStaticMode(boolean isStaticMode) {
        this.isStaticMode = isStaticMode;
    }

    /**
	 * Unregister XMPP disconnection handler.
	 *
	 * @param h the handler
	 */
    public void unregisterXMPPDisconnectHandler(Handler h) {
        this.xmppDisconnectHandlers.remove(h);
    }

    /** The XMPP result connection handler. */
    private Handler xmppResultConnectHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                if (iXMPPService != null && iXMPPService.isConnected()) {
                    for (int i = 0; i < xmppConnectHandlers.size(); i++) {
                        xmppConnectHandlers.get(i).sendEmptyMessage(0);
                    }
                    sendIQsFromCache();
                }
            } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
        }
    };

    /** The XMPP result disconnection handler. */
    private Handler xmppResultDisconnectHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                if (iXMPPService != null && iXMPPService.isConnected()) {
                    for (int i = 0; i < xmppDisconnectHandlers.size(); i++) {
                        xmppDisconnectHandlers.get(i).sendEmptyMessage(0);
                    }
                }
            } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
        }
    };
}
