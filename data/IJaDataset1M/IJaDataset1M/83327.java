package services.discovery;

import Topology.AbstractTopology;
import Topology.EdgeImpl;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import net.jxta.credential.AuthenticationCredential;
import net.jxta.credential.Credential;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.StructuredDocument;
import net.jxta.exception.PeerGroupException;
import services.discovery.NetworkView.TreeObj;
import net.jxta.membership.Authenticator;
import net.jxta.membership.MembershipService;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.rendezvous.RendezVousService;
import net.jxta.rendezvous.RendezvousListener;

public class DiscoveryManager implements AbstractDiscoveryManager {

    private static final Enumeration<PeerGroupAdvertisement> EMPTY_PGA_ENUM = new Enumeration<PeerGroupAdvertisement>() {

        public boolean hasMoreElements() {
            return false;
        }

        public PeerGroupAdvertisement nextElement() {
            throw new NoSuchElementException("Vector Enumeration");
        }
    };

    public static Enumeration<Advertisement> EMPTY_ADV_ENUM = new Enumeration<Advertisement>() {

        public boolean hasMoreElements() {
            return false;
        }

        public Advertisement nextElement() {
            throw new NoSuchElementException("Vector Enumeration");
        }
    };

    public static Enumeration<PeerAdvertisement> EMPTY_PEERADV_ENUM = new Enumeration<PeerAdvertisement>() {

        public boolean hasMoreElements() {
            return false;
        }

        public PeerAdvertisement nextElement() {
            throw new NoSuchElementException("Vector Enumeration");
        }
    };

    protected PeerGroup rootGroup;

    protected Vector<PeerGroup> joinedGroups;

    /** Constructor - Starts JXTA.
     *
     *  @param rdvListener - can be null if you don't want rdv event notification.
     */
    public DiscoveryManager(RendezvousListener rdvListener, PeerGroup group) {
        rootGroup = group;
        rootGroup.getRendezVousService().addListener(rdvListener);
        joinedGroups = new Vector<PeerGroup>(20);
        joinedGroups.add(rootGroup);
        DiscoveryService disco = rootGroup.getDiscoveryService();
        try {
            disco.flushAdvertisements(null, DiscoveryService.PEER);
            disco.flushAdvertisements(null, DiscoveryService.GROUP);
            disco.flushAdvertisements(null, DiscoveryService.ADV);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /** Return my own peer name. */
    public String getMyPeerName() {
        return rootGroup.getPeerAdvertisement().getName();
    }

    /** Return advertisement for the default (initial) peer group. */
    public PeerGroupAdvertisement getDefaultAdv() {
        return rootGroup.getPeerGroupAdvertisement();
    }

    /** Return advertisement for my peer. */
    public PeerAdvertisement getMyPeerAdv() {
        return rootGroup.getPeerAdvertisement();
    }

    /** Return the default (initial) peer group. */
    public PeerGroup getDefaultGroup() {
        return rootGroup;
    }

    /** Launch peergroup discovery in the net peer group.
     *
     *  @param discoListener - listener for discovery events.  May be null,
     *                         if you don't want the notification.
     */
    public synchronized void discoverGroups(DiscoveryListener discoListener) {
        discoverGroups(rootGroup, discoListener);
    }

    /** Launch peergroup discovery in the specified group.
     *
     *	@param group           the group to search in 
     *  @param discoListener - listener for discovery events.  May be null,
     *                         if you don't want the notification.
     */
    public synchronized void discoverGroups(PeerGroupAdvertisement group, DiscoveryListener discoListener) {
        PeerGroup pg = findJoinedGroup(group);
        if (pg == null) return;
        discoverGroups(pg, discoListener);
    }

    /** Launch peergroup discovery.
     *
     *  @param group         - joined peer group where to run the discovery
     *  @param discoListener - listener for discovery events.  May be null,
     *                         if you don't want the notification.
     */
    protected synchronized void discoverGroups(PeerGroup group, DiscoveryListener discoListener) {
        DiscoveryService disco = group.getDiscoveryService();
        DiscoThread thread = new DiscoThread(disco, DiscoveryService.GROUP, discoListener);
        thread.start();
    }

    /** Launch peer discovery, for the specified group.
     *
     *  @param group         - peer group for which to find peers.
     *  @param discoListener - listener for discovery events.  May be null,
     *                         if you don't want the notification.
     */
    public synchronized void discoverPeers(PeerGroupAdvertisement group, DiscoveryListener discoListener) {
        PeerGroup pg = findJoinedGroup(group);
        if (pg == null) return;
        DiscoveryService disco = pg.getDiscoveryService();
        DiscoThread thread = new DiscoThread(disco, DiscoveryService.PEER, discoListener);
        thread.start();
    }

    /** Launch advertisement discovery, for the specified group.
     *
     *  @param group         - peer group for which to find peers.
     *  @param discoListener - listener for discovery events.  May be null,
     *                         if you don't want the notification.
     */
    public void discoverAdvs(PeerGroupAdvertisement group, DiscoveryListener discoListener) {
        PeerGroup pg = findJoinedGroup(group);
        if (pg == null) return;
        DiscoveryService disco = pg.getDiscoveryService();
        DiscoThread thread = new DiscoThread(disco, DiscoveryService.ADV, discoListener);
        thread.start();
    }

    /** Return an enumerator to an array of PeerGroupAdvertisement objects
     *  representing the groups known so far.
     *  Note this doesn't include the default "NetPeerGroup".
     */
    public Enumeration<PeerGroupAdvertisement> getKnownGroups() {
        return (getKnownGroups(rootGroup));
    }

    /** Return an enumerator to an array of PeerGroupAdvertisement objects
     *  representing the groups known so far within <code>group</code>.
     *  Note this doesn't include the default "NetPeerGroup".
     *  @param group - parent group
     */
    public Enumeration<PeerGroupAdvertisement> getKnownGroups(PeerGroupAdvertisement group) {
        PeerGroup pg = findJoinedGroup(group);
        if (pg == null) return EMPTY_PGA_ENUM;
        return (getKnownGroups(pg));
    }

    /** Return an enumerator to an array of PeerGroupAdvertisement objects
     *  representing the groups known so far.
     *  Note this doesn't include the default "NetPeerGroup".
     */
    protected Enumeration<PeerGroupAdvertisement> getKnownGroups(PeerGroup group) {
        MyPeerGroupEnumeration enumer = null;
        DiscoveryService disco = group.getDiscoveryService();
        try {
            enumer = new MyPeerGroupEnumeration(disco.getLocalAdvertisements(DiscoveryService.GROUP, null, null));
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return enumer;
    }

    /** Return an enumerator to an array of PeerAdvertisement objects
     *  representing the peers known so far, that are members of the specified
     *  peer group.
     */
    public Enumeration<PeerAdvertisement> getKnownPeers(PeerGroupAdvertisement group) {
        PeerGroup pg = findJoinedGroup(group);
        if (pg == null) return EMPTY_PEERADV_ENUM;
        MyPeerEnumeration enumeration = null;
        DiscoveryService disco = pg.getDiscoveryService();
        try {
            enumeration = new MyPeerEnumeration(disco.getLocalAdvertisements(DiscoveryService.PEER, null, null));
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return enumeration;
    }

    /** Returns an enumerator to the know Advertisements in the specified peer group.
     * 
     */
    public Enumeration<Advertisement> getKnownAds(PeerGroupAdvertisement grpAdvertisement) {
        PeerGroup pg = findJoinedGroup(grpAdvertisement);
        if (pg == null) {
            return EMPTY_ADV_ENUM;
        }
        Enumeration<Advertisement> enumeration = null;
        DiscoveryService disco = pg.getDiscoveryService();
        try {
            enumeration = disco.getLocalAdvertisements(DiscoveryService.ADV, null, null);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return enumeration;
    }

    /** Join the specified PeerGroup.
     *  @return true if we were able to join, or if we have already joined, the group.
     *          false if we were unable to join the group.
     */
    public synchronized boolean joinPeerGroup(PeerGroupAdvertisement groupAdv) {
        if (findJoinedGroup(groupAdv) != null) return true;
        PeerGroup newGroup = null;
        try {
            newGroup = rootGroup.newGroup(groupAdv);
        } catch (PeerGroupException e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null, "Unable to join group.\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        StructuredDocument creds = null;
        try {
            AuthenticationCredential authCred = new AuthenticationCredential(newGroup, null, creds);
            MembershipService membership = newGroup.getMembershipService();
            Authenticator auth = membership.apply(authCred);
            if (auth.isReadyForJoin()) {
                Credential myCred = membership.join(auth);
            } else {
                System.out.println("Failure: unable to join group.  " + "Authenticator not ready.");
                JOptionPane.showMessageDialog(null, "Unable to join group.\n" + "Authenticator not ready.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("Failure in authentication.");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to join group.\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        final DiscoveryService disco = newGroup.getDiscoveryService();
        try {
            disco.flushAdvertisements(null, DiscoveryService.PEER);
            disco.publish(newGroup.getPeerGroupAdvertisement());
            disco.publish(newGroup.getPeerAdvertisement());
            disco.remotePublish(newGroup.getPeerGroupAdvertisement(), DiscoveryService.GROUP);
            disco.remotePublish(newGroup.getPeerAdvertisement(), DiscoveryService.PEER);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        joinedGroups.add(newGroup);
        return true;
    }

    /** Is the indicated peer a rendezvous? */
    public boolean isRendezvous(PeerAdvertisement peerAdv) {
        PeerGroup group = findJoinedGroup(peerAdv.getPeerGroupID());
        if (group == null) return false;
        if (peerAdv.getPeerID().equals(rootGroup.getPeerAdvertisement().getPeerID())) return group.isRendezvous();
        RendezVousService rdv = (RendezVousService) group.getRendezVousService();
        if (rdv == null) return false;
        PeerID peerID = null;
        Enumeration rdvs = null;
        rdvs = rdv.getConnectedRendezVous();
        while (rdvs.hasMoreElements()) {
            try {
                peerID = (PeerID) rdvs.nextElement();
                if (peerID.equals(peerAdv.getPeerID())) return true;
            } catch (Exception e) {
            }
        }
        rdvs = rdv.getDisconnectedRendezVous();
        while (rdvs.hasMoreElements()) {
            try {
                peerID = (PeerID) rdvs.nextElement();
                if (peerID.equals(peerAdv.getPeerID())) return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    /** Search our array of joined groups for the requested group.
     *  @return PeerGroup, or null if not found.
     */
    protected PeerGroup findJoinedGroup(PeerGroupAdvertisement groupAdv) {
        return findJoinedGroup(groupAdv.getPeerGroupID());
    }

    /** Search our array of joined groups for the requested group.
     *  @return PeerGroup, or null if not found.
     */
    protected PeerGroup findJoinedGroup(PeerGroupID groupID) {
        PeerGroup group = null;
        Enumeration myGroups = joinedGroups.elements();
        while (myGroups.hasMoreElements()) {
            group = (PeerGroup) myGroups.nextElement();
            if (group.getPeerGroupID().equals(groupID)) return group;
        }
        return null;
    }

    /** Thread to perform the discovery process.
     *  A separate thread is needed because we don't want to hold up
     *  the main thread's UI & animation.  The JXTA remote discovery call
     *  can take several seconds.
     */
    class DiscoThread extends Thread {

        DiscoveryService disco;

        int type;

        DiscoveryListener discoListener;

        public DiscoThread(DiscoveryService disco, int type, DiscoveryListener discoListener) {
            this.disco = disco;
            this.type = type;
            this.discoListener = discoListener;
        }

        public void run() {
            disco.getRemoteAdvertisements(null, type, null, null, 10, discoListener);
        }
    }
}
