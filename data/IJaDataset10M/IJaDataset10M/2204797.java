package mou.net2.presence;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import mou.Main;
import mou.Modul;
import mou.Subsystem;
import mou.net2.PeerHandle;
import mou.net2.messaging.MessageReceiver;

/**
 * Modul ist verantwotlich fuer Tracking der Praesenz der Spielteilnehmer. Listener
 * werden benachrichtigt wenn ein Teilnehmen on- oder offline geht.
 * @author paul
 */
public final class PresenceModul extends Modul implements MessageReceiver {

    private static final int ONLINE = 0;

    private static final int OFFLINE = 1;

    private List<PresenceListener> listeners = new CopyOnWriteArrayList<PresenceListener>();

    private Set<PeerHandle> onlinePeers = Collections.synchronizedSet(new HashSet<PeerHandle>());

    public PresenceModul(Subsystem parent) {
        super(parent);
    }

    /**
	 * Merkt Peer als online und benachtigt die registrierte Listener
	 * @param handle
	 */
    public void peerOnline(PeerHandle handle) {
        onlinePeers.add(handle);
        dispatchOnlineEvent(handle);
    }

    /**
	 * Markiert Peer als offline und benachritigt die registrierte Listener
	 * @param handle
	 */
    public void peerOffline(PeerHandle handle) {
        onlinePeers.remove(handle);
        dispatchOfflineEvent(handle);
    }

    public Collection<PeerHandle> getOnlinePeers() {
        return new ArrayList<PeerHandle>(onlinePeers);
    }

    public void addListener(PresenceListener listener) {
        listeners.add(listener);
    }

    private void dispatchOnlineEvent(PeerHandle handle) {
        for (PresenceListener listener : listeners) listener.peerIsOnline(handle);
    }

    private void dispatchOfflineEvent(PeerHandle handle) {
        for (PresenceListener listener : listeners) listener.peerIsOffline(handle);
    }

    @Override
    public String getModulName() {
        return "Presence Modul";
    }

    @Override
    protected void shutdownIntern() {
    }

    @Override
    protected File getPreferencesFile() {
        return null;
    }

    @Override
    protected void startModulIntern() throws Exception {
        Main.instance().getNetSubsystem().getMessagingModul().addMessageListener(getModulName(), this);
    }

    public void messageReceived(InetSocketAddress source, byte[] msg) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(msg));
        long serial = in.readLong();
        int type = in.read();
        PeerHandle handle = new PeerHandle(serial, source);
        switch(type) {
            case ONLINE:
                peerOnline(handle);
                break;
            case OFFLINE:
                peerOffline(handle);
                break;
            default:
                getLogger().warning("Unknown presence message type");
        }
    }
}
