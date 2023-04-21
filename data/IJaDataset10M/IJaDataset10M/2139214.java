package eu.popeye.networkabstraction.communication.basic.adapter;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgroups.ChannelException;
import org.jgroups.Message;
import org.jgroups.protocols.UpperHeader;
import org.jgroups.structures.Group;
import org.jgroups.structures.UpperStruct;
import eu.popeye.middleware.groupmanagement.management.GroupParameters;
import eu.popeye.middleware.groupmanagement.membership.GroupListChangeListener;
import eu.popeye.network.TransportPopeye;
import eu.popeye.networkabstraction.communication.basic.util.PDSStructLog;
import eu.popeye.networkabstraction.communication.basic.util.PopeyeClassLoader;
import eu.popeye.networkabstraction.communication.basic.util.PopeyeView;
import eu.popeye.networkabstraction.communication.basic.util.SuperPeerConfig;
import eu.popeye.networkabstraction.communication.message.MessageWrapper;

/**
 * This class is in charge of starting PDS main entry point, PullPushAdapterInterface and
 * make it accessible to the communication adapter and other classes
 * This class also receives all the messages coming from the PDS and forwards the messages
 * to the corresponding PDSPullPushAdapters. Messages are received with a group identifier, thanks to the PDSListeners
 * @see eu.popeye.network.PullPushAdapterInterface 
 * @author Gerard Paris Aixala
 *
 */
public class PDSMultiplexer implements TransportPopeye {

    protected static final Log log = LogFactory.getLog(PopeyeClassLoader.class);

    protected static final long TIME_TO_CHANGE_ROLE = 15000;

    private Hashtable<String, PDSListener> pdsListeners = new Hashtable<String, PDSListener>();

    private UpperStruct currentUpperStruct;

    private eu.popeye.network.PullPushAdapterInterface pds;

    protected GroupListChangeListener groupListChangeListener = null;

    private List<GroupParameters> currentGroupList = null;

    private Hashtable<String, PopeyeView> popeyeViewsHashtable = new Hashtable<String, PopeyeView>();

    private PDSMultiplexer instanceofThis = this;

    private boolean adapterStarted = false;

    private boolean pdsStarted = false;

    private static PDSMultiplexer singletonInstance = null;

    private PDSMultiplexer() {
        try {
            System.out.println("Starting PPAI as a " + SuperPeerConfig.getROLE());
            pds = new eu.popeye.network.PullPushAdapterInterface(this, SuperPeerConfig.getUSERNAME(), SuperPeerConfig.amISuperpeer(), SuperPeerConfig.getPUBLICKEY());
            pds.startAdapter();
        } catch (ChannelException e) {
            log.error("Unable to create Peer Discovery Services");
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            System.out.println("Starting PPAI as a " + SuperPeerConfig.getROLE());
            pds = new eu.popeye.network.PullPushAdapterInterface(this, SuperPeerConfig.getUSERNAME(), SuperPeerConfig.amISuperpeer(), SuperPeerConfig.getPUBLICKEY());
            Thread changeRoleThread = new Thread() {

                public void run() {
                    System.err.println("-- Waiting for " + TIME_TO_CHANGE_ROLE + " milliseconds to change from " + SuperPeerConfig.getROLE() + " to superpeer");
                    try {
                        Thread.sleep(TIME_TO_CHANGE_ROLE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!adapterStarted) {
                        System.err.println("--- Stopping the adapter...");
                        pds.stopAdapter();
                        SuperPeerConfig.setSuperPeerRole(true);
                        System.err.println("--- Launching PDS again as superpeer");
                        try {
                            pds = new eu.popeye.network.PullPushAdapterInterface(instanceofThis, SuperPeerConfig.getUSERNAME(), true, SuperPeerConfig.getPUBLICKEY());
                            pds.startAdapter();
                            System.err.println("--- adapter stopped, PPAI launched again and adapter started...");
                        } catch (ChannelException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.err.println("--- It was not necessary to restart the adapter, I am " + SuperPeerConfig.getROLE());
                    }
                }
            };
            if (SuperPeerConfig.amISuperpeer() == false) {
                changeRoleThread.run();
            }
            pds.startAdapter();
            adapterStarted = true;
        } catch (ChannelException e) {
            log.error("Unable to create Peer Discovery Services");
            e.printStackTrace();
        }
    }

    /**
	 * Obtains an instance of this singleton class
	 * @return a singleton instance of the class
	 */
    public static PDSMultiplexer getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new PDSMultiplexer();
        }
        return singletonInstance;
    }

    /**
	 * Obtain an instance of the PullPushAdapterInterface of the PeerDiscoveryServices
	 * @return instance of PullPushAdapterInterface
	 */
    public eu.popeye.network.PullPushAdapterInterface getPDS() {
        return pds;
    }

    /**
	 * 
	 * @param groupName
	 * @param l
	 */
    public void addPDSListener(String groupName, PDSListener l) {
        pdsListeners.put(groupName, l);
        System.out.println("PDSMULTPX: PDSListener registered. Checking for old views");
        if (popeyeViewsHashtable.get(groupName) != null) {
            System.out.println("PDSMULTPX: invoking receivePopeyeView from the last view");
            l.receivePopeyeView(popeyeViewsHashtable.get(groupName));
        }
    }

    /**
	 * 
	 * @param groupName
	 */
    public PDSListener getPDSListener(String groupName) {
        return pdsListeners.get(groupName);
    }

    /**
	 * 
	 * @param groupName
	 */
    public void removePDSListener(String groupName) {
        pdsListeners.remove(groupName);
    }

    public void registerGroupListChangeListener(GroupListChangeListener l) {
        if (l == null) {
            if (log.isErrorEnabled()) log.error("message listener is null");
            return;
        }
        groupListChangeListener = l;
    }

    public void unregisterGroupListChangeListener() {
        groupListChangeListener = null;
    }

    public UpperStruct getUpperStruct() {
        if (currentUpperStruct == null) {
            return new UpperStruct();
        } else {
            return currentUpperStruct;
        }
    }

    public void receiveData(Message msg, String groupName) {
        try {
            System.err.println("PDSMultiplexer: Received data for group" + groupName + " Message=" + msg);
            System.err.println(msg.getHeaders());
            PDSListener listener = pdsListeners.get(groupName);
            if (listener != null) {
                listener.receiveMessage(msg);
            } else {
                MessageWrapper msgWrap = (MessageWrapper) msg.getObject();
                String msgGroupName = msgWrap.getGroupName();
                listener = pdsListeners.get(msgGroupName);
                if (listener != null) {
                    listener.receiveMessage(msg);
                } else {
                    System.err.println("Message discarded");
                }
            }
        } catch (Exception e) {
            log.debug("Received a message for group" + groupName + ". There is no listener registered for this group. Discarding message.");
        }
    }

    public void receiveStruct(Message msg) {
        UpperHeader uphead = (UpperHeader) msg.getHeader("Upper");
        if (uphead != null) {
            log.debug("Received struct " + uphead);
            System.err.println("Received struct " + uphead);
            currentUpperStruct = uphead.upper.clone();
            PDSStructLog.addLog(this, currentUpperStruct.toString());
            currentGroupList = new LinkedList<GroupParameters>();
            org.jgroups.util.List groupList = currentUpperStruct.getGroups();
            Enumeration groupEnum = groupList.elements();
            while (groupEnum.hasMoreElements()) {
                Group group = (Group) groupEnum.nextElement();
                currentGroupList.add(new GroupParameters(group.getIPMulticast().toString(), "0", group.getName()));
            }
            if (groupListChangeListener != null) {
                groupListChangeListener.receiveGroupList(currentGroupList);
            }
            groupEnum = groupList.elements();
            while (groupEnum.hasMoreElements()) {
                Group group = (Group) groupEnum.nextElement();
                org.jgroups.util.List nodeList = group.getNodes();
                PopeyeView popeyeView = ViewAdapter.createPopeyeView(nodeList);
                if (pdsListeners.containsKey(group.getName())) {
                    System.err.println("Receiving Popeye view for group " + group.getName());
                    pdsListeners.get(group.getName()).receivePopeyeView(popeyeView);
                } else {
                    System.out.println("PDSMULTPX: save popeyeView since we don't have a reg listener");
                    popeyeViewsHashtable.put(group.getName(), popeyeView);
                }
            }
        }
    }
}
