package p2p.chat.action;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import p2p.chat.dataobject.gui.Settings;
import p2p.chat.transfer.discovery.Discovery;
import p2p.chat.transfer.pipe.InputPipeListener;
import p2p.chat.transfer.pipe.MulticastPipe;
import p2p.chat.util.ApplicationSettings;

/**
 * Sep 26, 2008
 * 
 * @author S.Timofiychuk
 * 
 */
public class ChatCoreJxta implements IChatCore<PipeAdvertisement, PeerGroupAdvertisement, PipeID> {

    private final Settings settings;

    private final String userName;

    private PipeID pipeID;

    private PeerGroupID peerGroupId;

    private PeerID peerId;

    private PeerGroup peerGroup;

    private static ChatCoreJxta chatCore;

    private NetworkManager manager;

    private InputPipeListener inputPipe;

    private OutputPipe outputPipe;

    private static MulticastPipe multicastPipe;

    private static Discovery discovery;

    private PipeAdvertisement userIdentifier;

    private static final int timeOutPipeConnect = ApplicationSettings.getTimeOutPipeConnect();

    private static final int timeOutRdvConnect = ApplicationSettings.getTimeOutRdvConnect();

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private static final String HOME_USER = System.getProperty("user.home");

    public static final String HOME_APP = HOME_USER + FILE_SEPARATOR + ".jxtaChat";

    public static final String HOME_JXTA = HOME_APP + FILE_SEPARATOR + ".jxtaHome";

    private static final String MANAGER_INSTANCE_NAME = "jxtaChatService";

    public static final String GROUP_NAME = "jxtaChatGroup";

    public static final String SENDER_NAME_TAG = "SenderName:";

    public static final String SENDED_MSG_TAG = "SendedMsg:";

    /**
	 * @param settings
	 */
    public ChatCoreJxta(Settings settings) {
        this.settings = settings;
        this.userName = settings.getUserName();
        File home = new File(HOME_JXTA);
        try {
            this.manager = new NetworkManager(settings.getConfigMode(), MANAGER_INSTANCE_NAME, home.toURI());
            this.manager.setConfigPersistent(true);
            this.peerGroupId = IDFactory.newPeerGroupID(PeerGroupID.defaultNetPeerGroupID, GROUP_NAME.getBytes());
            this.peerId = IDFactory.newPeerID(this.peerGroupId, this.userName.getBytes());
            this.pipeID = IDFactory.newPipeID(PeerGroupID.defaultNetPeerGroupID, this.userName.getBytes());
            NetworkConfigurator configurator = this.manager.getConfigurator();
            if (!configurator.exists()) {
                configurator.setPeerID(this.peerId);
                configurator.clearRelaySeeds();
                configurator.clearRendezvousSeeds();
                configurator.setHttpEnabled(true);
                configurator.setHttpIncoming(true);
                configurator.setHttpOutgoing(true);
                configurator.setHttpPort(settings.getHttpPort());
                configurator.setTcpEnabled(true);
                configurator.setTcpOutgoing(true);
                configurator.setTcpIncoming(true);
                configurator.setTcpPort(settings.getTcpPort());
                configurator.setUseMulticast(true);
                configurator.setMulticastPort(settings.getMulticastPort());
                if (!settings.isServer()) {
                    if (settings.isUsePublicSeed()) {
                        manager.setUseDefaultSeeds(true);
                    } else {
                        manager.setUseDefaultSeeds(false);
                        configurator.addRelaySeedingURI(settings.getRelayList());
                        configurator.addRdvSeedingURI(settings.getRdvList());
                        configurator.setMulticastAddress(settings.getSeedHostName());
                    }
                } else {
                    this.manager.setUseDefaultSeeds(false);
                }
                if (settings.getHostName().equals("localhost")) {
                    configurator.setHttpInterfaceAddress(InetAddress.getLocalHost().toString());
                    configurator.setTcpInterfaceAddress(InetAddress.getLocalHost().toString());
                } else {
                    configurator.setHttpInterfaceAddress(settings.getHostName());
                    configurator.setTcpInterfaceAddress(settings.getHostName());
                }
            } else {
                this.manager.getConfigurator().load();
            }
            this.manager.startNetwork();
            this.peerGroup = this.manager.getNetPeerGroup();
            boolean isConnected = this.manager.waitForRendezvousConnection(timeOutRdvConnect);
        } catch (Exception e) {
            e.printStackTrace();
            this.destroy();
            System.exit(1);
        }
    }

    /**
	 * @param settings
	 */
    private static void initializeChat(Settings settings) {
        chatCore = new ChatCoreJxta(settings);
        discovery = new Discovery();
    }

    /**
	 * On runtime must live only one object of this type. On create you must
	 * specified Settings object.
	 * 
	 * @param settings
	 * @return
	 * @throws Exception
	 * 
	 */
    public static ChatCoreJxta getInstance(Settings settings) throws Exception {
        if (settings == null && !isInstanceExists()) {
            throw new Exception("Parameter Settings must be set! Instance of ChatCoreJxta do not create.");
        }
        if (!isInstanceExists()) {
            initializeChat(settings);
        }
        return chatCore;
    }

    /**
	 * @return
	 */
    public static boolean isInstanceExists() {
        if (chatCore != null) {
            return true;
        }
        return false;
    }

    public PipeID getUserId() {
        return this.pipeID;
    }

    public boolean registerPeer() {
        return discovery.registerPeer(discovery.getUserIdentifier(this.userName, this.getUserId(), "----Logged in state----"));
    }

    public boolean join(PeerGroupAdvertisement groupIdentifer) {
        try {
            multicastPipe = new MulticastPipe(this.settings.getUserName(), this.settings.isServer());
            this.inputPipe = new InputPipeListener(this.getCurrentUserIdentifier(this.userName, this.pipeID, "----logged state----"), this.settings.isServer());
            Message multicastMsg = new Message();
            multicastMsg.addMessageElement(new StringMessageElement(ChatCoreJxta.SENDER_NAME_TAG, this.getUserName(), null));
            multicastMsg.addMessageElement(new StringMessageElement(MulticastPipe.MULTICAST_TYPE_TAG, MulticastPipe.LOGIN_TYPE, null));
            this.sendMessage(multicastMsg, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public PipeAdvertisement getCurrentUserIdentifier(String userName, PipeID userId, String description) {
        this.userIdentifier = discovery.getUserIdentifier(userName, userId, description);
        return this.userIdentifier;
    }

    public List<PipeAdvertisement> searchAllUserIdent() throws InterruptedException {
        return ((Discovery) discovery).searchAllPeopleAdvert();
    }

    public PipeAdvertisement searchUserIdentByName(String name) throws InterruptedException {
        return ((Discovery) discovery).searchPeopleAdvertByName(name);
    }

    public PipeAdvertisement searchUserIdentById(PipeID id) throws InterruptedException {
        return ((Discovery) discovery).searchPeopleAdvertById(id);
    }

    public String getPeerState(PipeAdvertisement userIdentifier) {
        return null;
    }

    public void openOutConnection(PipeAdvertisement advertisement) throws IOException {
        this.outputPipe = this.getDefaultPeerGroup().getPipeService().createOutputPipe(advertisement, timeOutPipeConnect);
    }

    public boolean sendMessage(Message message, boolean isMultiCast) throws Exception {
        if (this.outputPipe == null && !isMultiCast) {
            throw new Exception("You must create Output connection first");
        }
        if (isMultiCast) {
            return multicastPipe.send(message);
        } else {
            return this.outputPipe.send(message);
        }
    }

    public boolean closeOutConnection(PipeAdvertisement advertisement) throws Exception {
        if (this.outputPipe == null) {
            throw new Exception("Output connection not opened!");
        }
        this.outputPipe.close();
        return true;
    }

    public void destroy() {
        if (multicastPipe != null) {
            multicastPipe.destroy();
        }
        if (this.outputPipe != null) {
            this.outputPipe.close();
        }
        if (this.inputPipe != null) {
            this.inputPipe.destroy();
        }
        if (this.manager != null) {
            this.manager.stopNetwork();
        }
    }

    /**
	 * @return
	 */
    public Settings getSettings() {
        return this.settings;
    }

    public String getUserName() {
        return this.userName;
    }

    /**
	 * @return the inputPipe
	 */
    public InputPipeListener getInputPipe() {
        return this.inputPipe;
    }

    /**
	 * @return the outputPipe
	 */
    public OutputPipe getOutputPipe() {
        return outputPipe;
    }

    /**
	 * @return the multicastPipe
	 */
    public MulticastPipe getMulticastPipe() {
        return multicastPipe;
    }

    /**
	 * @return
	 */
    public PeerID getDefaultPeerID() {
        return peerId;
    }

    /**
	 * @return
	 */
    public PeerGroupID getDefaultPeerGroupID() {
        return peerGroupId;
    }

    /**
	 * @return
	 */
    public PeerGroup getDefaultPeerGroup() {
        return peerGroup;
    }
}
