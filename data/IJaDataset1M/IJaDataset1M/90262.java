package info.metlos.jdc.nmdc;

import info.metlos.jdc.HubRegister;
import info.metlos.jdc.JDC;
import info.metlos.jdc.common.AbstractDCHub;
import info.metlos.jdc.common.ConnectionMode;
import info.metlos.jdc.common.ConnectionSettings;
import info.metlos.jdc.common.HubDataChangeType;
import info.metlos.jdc.common.IChatClient;
import info.metlos.jdc.common.IDCClient;
import info.metlos.jdc.common.IMessage;
import info.metlos.jdc.common.ISearchResultListener;
import info.metlos.jdc.common.MessageDirection;
import info.metlos.jdc.common.RemoteConnectionEventType;
import info.metlos.jdc.common.SearchRequest;
import info.metlos.jdc.common.SearchResult;
import info.metlos.jdc.fileshare.ISearchResultHandler;
import info.metlos.jdc.fileshare.ShareManager;
import info.metlos.jdc.fileshare.list.FileListItem;
import info.metlos.jdc.nmdc.messages.AbstractNMDCMessage;
import info.metlos.jdc.nmdc.messages.ChatMessage;
import info.metlos.jdc.nmdc.messages.ForceMoveMessage;
import info.metlos.jdc.nmdc.messages.GetInfoMessage;
import info.metlos.jdc.nmdc.messages.GetNickListMessage;
import info.metlos.jdc.nmdc.messages.HelloMessage;
import info.metlos.jdc.nmdc.messages.HubIsFullMessage;
import info.metlos.jdc.nmdc.messages.HubNameMessage;
import info.metlos.jdc.nmdc.messages.HubTopicMessage;
import info.metlos.jdc.nmdc.messages.KeyMessage;
import info.metlos.jdc.nmdc.messages.LockMessage;
import info.metlos.jdc.nmdc.messages.MyInfoMessage;
import info.metlos.jdc.nmdc.messages.NickListMessage;
import info.metlos.jdc.nmdc.messages.OpListMessage;
import info.metlos.jdc.nmdc.messages.QuitMessage;
import info.metlos.jdc.nmdc.messages.SearchMessage;
import info.metlos.jdc.nmdc.messages.SearchResultMessage;
import info.metlos.jdc.nmdc.messages.SupportsMessage;
import info.metlos.jdc.nmdc.messages.ToMessage;
import info.metlos.jdc.nmdc.messages.ValidateDenideMessage;
import info.metlos.jdc.nmdc.messages.ValidateNickMessage;
import info.metlos.jdc.nmdc.messages.VersionMessage;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Implementation of the NMDC hub.
 * <p>
 * This class should not be instantiated directly. Use
 * {@link HubRegister#getHub(java.net.URI, Charset)} to create one. If an
 * instance is created directly using constructors, the JDC library won't be
 * able to track the number of connected hubs, etc.
 * 
 * @see JDC
 * 
 * @author metlos
 * 
 * @version $Id: NMDCHub.java 237 2008-09-28 17:03:21Z metlos $
 */
public class NMDCHub extends AbstractDCHub {

    private static final Logger logger = LogManager.getLogger(NMDCHub.class);

    private String myEmail;

    private MyStatus myStatus;

    private boolean hubSupportsNoHello = false;

    private boolean hubSupportsNoGetINFO = false;

    private boolean hubSupportsUserIP2 = false;

    private final ISearchResultHandler searchHandler = new ISearchResultHandler() {

        public void handleSearchResults(Object token, Set<? extends FileListItem> results) {
            SearchMessage sm = (SearchMessage) token;
            List<SearchResultMessage> messages = getSRMessages(sm, results);
            if (sm.isActive()) {
                if (sm.getPort() <= 0) {
                    sm.setPort(411);
                }
                InetSocketAddress destination = new InetSocketAddress(sm.getIp(), sm.getPort());
                for (SearchResultMessage srm : messages) {
                    sendDatagramMessage(destination, srm);
                }
            } else {
                NMDCClient c = (NMDCClient) findByNickname(sm.getNickname());
                if (c != null) {
                    for (SearchResultMessage srm : messages) {
                        srm.setTargetNick(c.getNicknameAt(NMDCHub.this));
                        sendMessage(srm);
                    }
                }
            }
            if (logger.isDebugEnabled()) {
                StringBuilder bld = new StringBuilder(sm.isActive() ? "Via UDP: " : "Via Hub: ");
                bld.append(sm.toString());
                bld.append(" results: ");
                if (results != null) {
                    for (FileListItem f : results) {
                        bld.append(f.getAbsolutePath()).append(", ");
                    }
                }
                logger.debug(bld.toString());
            }
        }
    };

    /**
	 * This enum defines possible states of conversation with the hub.
	 * 
	 * @author metlos
	 * 
	 */
    private enum ConversationState {

        notConnected, handshakeOldHub, handshake, supportNegotiation, connected
    }

    private class MessageHandler extends AbstractNMDCMessageHandler {

        private ConversationState conversationState = ConversationState.notConnected;

        @Override
        public void handle(ChatMessage message) {
            dispatchChatMessageEvent(message);
        }

        @Override
        public void handle(HubNameMessage message) {
            setName(message.getName());
            dispatchDataChangeEvent(HubDataChangeType.name);
        }

        @Override
        public void handle(HubTopicMessage message) {
            setTopic(message.getTopic());
            dispatchDataChangeEvent(HubDataChangeType.topic);
        }

        @Override
        public void handle(ForceMoveMessage message) {
            ChatMessage m = new ChatMessage(NMDCHub.this, null);
            m.setAuthorNick("");
            m.setMessage("Hub sent a redirect to " + message.getAddress());
            m.setDateCreated(new Date());
            m.setPrivateMessage(false);
            dispatchChatMessageEvent(m);
        }

        @Override
        public void handle(HubIsFullMessage message) {
            ChatMessage m = new ChatMessage(NMDCHub.this, null);
            m.setAuthorNick("");
            m.setMessage("Hub is full...");
            m.setDateCreated(new Date());
            m.setPrivateMessage(false);
            dispatchChatMessageEvent(m);
        }

        @Override
        public void handle(QuitMessage message) {
            Iterator<IChatClient> i = getConnectedClients().iterator();
            QuitMessage qm = message;
            while (i.hasNext()) {
                IChatClient c = i.next();
                if (qm.getNickname().equals(c.getNicknameAt(NMDCHub.this))) {
                    i.remove();
                    dispatchClientListChangeEvent(c, false);
                    break;
                }
            }
        }

        @Override
        public void handle(LockMessage message) {
            if (conversationState == ConversationState.notConnected) {
                KeyMessage key = new KeyMessage(NMDCHub.this, MessageDirection.outgoing);
                key.setKey(generateKey(message.getLock()));
                sendMessage(key);
                if (message.getLock().startsWith("EXTENDEDPROTOCOL")) {
                    SupportsMessage supports = new SupportsMessage(NMDCHub.this);
                    supports.getTags().add("NoHello");
                    supports.getTags().add("NoGetINFO");
                    supports.getTags().add("UserIP2");
                    supports.getTags().add("HubTopic");
                    supports.getTags().add("TTHSearch");
                    sendMessage(supports);
                    conversationState = ConversationState.supportNegotiation;
                } else {
                    ValidateNickMessage nick = new ValidateNickMessage(NMDCHub.this);
                    nick.setNick(getMyNickname());
                    sendMessage(nick);
                    conversationState = ConversationState.handshakeOldHub;
                }
            }
        }

        @Override
        public void handle(HelloMessage message) {
            NMDCClient client;
            switch(conversationState) {
                case handshakeOldHub:
                    if (message.getNickname().equals(getMyNickname())) {
                        MyInfoMessage mi = MyInfoMessage.createMyInfoToSend(NMDCHub.this, getMyNickname(), null, getMyEmail(), getMyStatus());
                        sendMessage(new VersionMessage(NMDCHub.this));
                        sendMessage(new GetNickListMessage(NMDCHub.this));
                        sendMessage(mi);
                        client = NMDCClient.createFromMyInfo(NMDCHub.this, mi);
                        conversationState = ConversationState.connected;
                        dispatchConnectionEvent(RemoteConnectionEventType.activated);
                    } else {
                        client = NMDCClient.createWithNickName(NMDCHub.this, message.getNickname());
                    }
                    getConnectedClients().add(client);
                    dispatchClientListChangeEvent(client, true);
                    break;
                case handshake:
                    if (message.getNickname().equals(getMyNickname())) {
                        sendMessage(MyInfoMessage.createMyInfoToSend(NMDCHub.this, getMyNickname(), null, getMyEmail(), getMyStatus()));
                        sendMessage(new GetNickListMessage(NMDCHub.this));
                        conversationState = ConversationState.connected;
                        dispatchConnectionEvent(RemoteConnectionEventType.activated);
                    }
                    break;
                case connected:
                    if (hubSupportsNoGetINFO) {
                        client = NMDCClient.createWithNickName(NMDCHub.this, message.getNickname());
                        client.setBotAt(NMDCHub.this, true);
                        getConnectedClients().add(client);
                        dispatchClientListChangeEvent(client, true);
                    } else {
                        GetInfoMessage gim = new GetInfoMessage(NMDCHub.this);
                        gim.setMyNick(getMyNickname());
                        gim.setRequestedNick(message.getNickname());
                        sendMessage(gim);
                    }
            }
        }

        @Override
        public void handle(ValidateDenideMessage message) {
            dispatchConnectionEvent(RemoteConnectionEventType.deactivated);
        }

        @Override
        public void handle(SupportsMessage message) {
            if (conversationState == ConversationState.supportNegotiation) {
                Set<String> tags = message.getTags();
                hubSupportsNoGetINFO = tags.contains("NoGetINFO");
                hubSupportsNoHello = tags.contains("NoHello");
                hubSupportsUserIP2 = tags.contains("UserIP2");
                sendMessage(new VersionMessage(NMDCHub.this));
                ValidateNickMessage nick = new ValidateNickMessage(NMDCHub.this);
                nick.setNick(getMyNickname());
                sendMessage(nick);
                conversationState = ConversationState.handshake;
            }
        }

        @Override
        public void handle(MyInfoMessage message) {
            if (conversationState == ConversationState.connected) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding user : " + message.toString());
                }
                MyInfoMessage mm = message;
                NMDCClient client = NMDCClient.createFromMyInfo(NMDCHub.this, mm);
                if (getConnectedClients().add(client)) {
                    dispatchClientListChangeEvent(client, true);
                } else {
                    client = (NMDCClient) findByNickname(mm.getNickname());
                    if (client != null) {
                        client.updateFrom(mm);
                        dispatchClientStateChangeEvent(client);
                    }
                }
            }
        }

        @Override
        public void handle(NickListMessage message) {
            if (conversationState == ConversationState.connected) {
                for (String nick : message.getNickNames()) {
                    NMDCClient cl = NMDCClient.createWithNickName(NMDCHub.this, nick);
                    if (getConnectedClients().add(cl)) {
                        dispatchClientListChangeEvent(cl, true);
                        GetInfoMessage gim = new GetInfoMessage(NMDCHub.this);
                        gim.setMyNick(getMyNickname());
                        gim.setRequestedNick(nick);
                        sendMessage(gim);
                    }
                }
            }
        }

        @Override
        public void handle(OpListMessage message) {
            if (conversationState == ConversationState.connected) {
                for (String op : message.getOps()) {
                    NMDCClient cl = (NMDCClient) findByNickname(op);
                    if (cl == null) {
                        cl = NMDCClient.createWithNickName(NMDCHub.this, op);
                        cl.setOperatorAt(NMDCHub.this, true);
                        dispatchClientListChangeEvent(cl, true);
                    } else {
                        cl.setOperatorAt(NMDCHub.this, true);
                        dispatchClientStateChangeEvent(cl);
                    }
                }
            }
        }

        @Override
        public void handle(SearchMessage message) {
            if (conversationState == ConversationState.connected) {
                if (!getMyNickname().equals(message.getNickname()) && !(JDC.getState().getConnectionSettings().getExternalIp().equals(message.getIp()) && JDC.getState().getConnectionSettings().getUdpPortForward() == message.getPort())) {
                    ShareManager.startSearch(searchHandler, message.getRequest(), message.isActive() ? 10 : 5, message);
                }
            }
        }

        @Override
        public void handle(SearchResultMessage message) {
            if (conversationState == ConversationState.connected) {
                String sourceNick = message.getSourceNick();
                if (sourceNick != null) {
                    SearchResult result = new SearchResult();
                    IDCClient client = (IDCClient) findByNickname(sourceNick);
                    result.setClient(client);
                    result.setResult(message.getResult());
                    SearchResultResolver.resolve(NMDCHub.this, result);
                }
            }
        }
    }

    MessageHandler messageHandler = new MessageHandler();

    public NMDCHub(InetSocketAddress address) {
        super(address, new NMDCMessageEncoder(), new NMDCMessageDecoder());
        init();
    }

    public NMDCHub(InetSocketAddress address, Charset charset) {
        super(address, new NMDCMessageEncoder(charset), new NMDCMessageDecoder(charset));
        init();
    }

    public Charset getCharset() {
        return ((NMDCMessageDecoder) getDecoder()).getCharset();
    }

    private void init() {
        ((NMDCMessageDecoder) getDecoder()).setRemote(this);
        myStatus = MyStatus.normal;
        messageHandler.conversationState = ConversationState.notConnected;
    }

    /**
	 * @return the myEmail
	 */
    public String getMyEmail() {
        return myEmail;
    }

    /**
	 * @param myEmail
	 *            the myEmail to set
	 */
    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

    /**
	 * @return the myStatus
	 */
    public MyStatus getMyStatus() {
        return myStatus;
    }

    /**
	 * @param myStatus
	 *            the myStatus to set
	 */
    public void setMyStatus(MyStatus myStatus) {
        this.myStatus = myStatus;
    }

    @Override
    protected void disconnected() {
        messageHandler.conversationState = ConversationState.notConnected;
        getConnectedClients().clear();
        super.disconnected();
    }

    @Override
    protected void handleIncomingMessage(IMessage message) {
        if (message != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Received message: " + message.toString());
            }
            ((AbstractNMDCMessage) message).accept(messageHandler);
        }
    }

    @Override
    protected void handleInterruption() {
    }

    public void sendBroadcastMessage(String message) {
        ChatMessage m = new ChatMessage(this);
        m.setAuthorNick(getMyNickname());
        m.setMessage(message);
        m.setDateCreated(new Date());
        m.setPrivateMessage(false);
        sendMessage(m);
    }

    public void sendPrivateMessage(String message, IChatClient toClient) {
        ToMessage m = new ToMessage(this);
        m.setFrom(getMyNickname());
        m.setTo(toClient.getNicknameAt(this));
        m.setMessage(message);
        sendMessage(m);
    }

    public NMDCHubSearchHandle sendSearchRequest(ISearchResultListener listener, SearchRequest request) {
        NMDCHubSearchHandle handle = new NMDCHubSearchHandle(request);
        handle.addListener(listener);
        SearchMessage message = new SearchMessage(this);
        message.setRequest(new NMDCSearchRequest(request));
        ConnectionSettings connectionSettings = JDC.getState().getConnectionSettings();
        if (connectionSettings.getMode() == ConnectionMode.active) {
            message.setIp(connectionSettings.getExternalIp());
            message.setPort(connectionSettings.getUdpPortForward());
        } else {
            message.setNickname(getMyNickname());
        }
        sendMessage(message);
        return handle;
    }

    /**
	 * Method for generating key from the lock in the initial handshake.
	 * 
	 * Copied from {@linkplain http://dcpp.net/wiki/index.php/LockToKey}.
	 */
    private String generateKey(String lockString) {
        int i = 0;
        String trimmed = lockString.trim();
        final byte[] lock = new byte[trimmed.length()];
        for (i = 0; i < lock.length; i++) {
            lock[i] = (byte) trimmed.charAt(i);
        }
        final byte[] key = new byte[lock.length];
        for (i = 1; i < lock.length; i++) {
            key[i] = (byte) ((lock[i] ^ lock[i - 1]) & 0xFF);
        }
        key[0] = (byte) ((lock[0] ^ lock[lock.length - 1] ^ lock[lock.length - 2] ^ 5) & 0xFF);
        for (i = 0; i < key.length; i++) {
            key[i] = (byte) ((key[i] << 4 & 0xF0 | key[i] >> 4 & 0x0F) & 0xFF);
        }
        StringBuilder bld = new StringBuilder();
        for (byte b : key) {
            bld.append((char) (b & 0xFF));
        }
        return dcnEncode(bld.toString());
    }

    /**
	 * Support method for {@link #generateKey(String)}.
	 * 
	 * @param string
	 * @return
	 */
    private String dcnEncode(String string) {
        char[] replacements = null;
        int i = 0;
        int index = 0;
        replacements = new char[] { 0, 5, 36, 96, 124, 126 };
        String str = string;
        for (i = 0; i < replacements.length; i++) {
            while ((index = str.indexOf(replacements[i])) >= 0) {
                str = str.substring(0, index) + "/%DCN" + leadz(replacements[i]) + "%/" + str.substring(index + 1, str.length());
            }
        }
        return str;
    }

    /**
	 * Support method for {@link #dcnEncode(String)}.
	 * 
	 * @param nr
	 * @return
	 */
    private String leadz(int nr) {
        if (nr < 100 && nr > 10) {
            return "0" + nr;
        } else if (nr < 10) {
            return "00" + nr;
        } else {
            return "" + nr;
        }
    }

    /**
	 * Converts the search results for given search message into the list of
	 * search result messages.
	 * 
	 * @param sm
	 *            the search message
	 * @param results
	 *            the search results
	 * @return the list of search result messages
	 */
    private List<SearchResultMessage> getSRMessages(SearchMessage sm, Set<? extends FileListItem> results) {
        ArrayList<SearchResultMessage> ret = new ArrayList<SearchResultMessage>();
        for (FileListItem fli : results) {
            SearchResultMessage sr = new SearchResultMessage(this);
            sr.setResult(fli);
            sr.setSourceNick(getMyNickname());
            sr.setTargetNick(sm.getNickname());
            sr.setHubAddress(getAddress());
            sr.setHubName(getName());
            sr.setFreeSlots(JDC.getState().getFreeSlots());
            sr.setTotalSlots(JDC.getState().getOpenSlots());
            ret.add(sr);
        }
        return ret;
    }
}
