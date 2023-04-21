package de.lulusoft.anothertorrent.core.rtorrent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class holds all files of a torrent. When a file is added to a torrent
 * via Torrent.addFile then this class is added as PropertyChangeListener to the 
 * file. Gui elements should add themself as PropertyChangeListener to this class
 * so they get notified when an update of a file in this list occurred.
 * @author 
 *
 */
public class PeerArrayList extends ArrayList<Peer> implements PropertyChangeListener {

    private static final long serialVersionUID = 6853514126040693721L;

    public static final String PROP_UPDATED = "updatedPeerProperty";

    public static final String PROP_NEW = "newPeerProperty";

    public static final String PROP_REMOVED = "removedPeerProperty";

    private PropertyChangeSupport propertyChangeSupport = null;

    protected ArrayList<Peer> updatedPeers = null;

    protected ArrayList<Peer> newPeers = null;

    protected ArrayList<Peer> removedPeers = null;

    public PeerArrayList() {
        super();
        propertyChangeSupport = new PropertyChangeSupport(this);
        updatedPeers = new ArrayList<Peer>();
        newPeers = new ArrayList<Peer>();
        removedPeers = new ArrayList<Peer>();
    }

    public Peer getById(String id) {
        Iterator<Peer> it = this.iterator();
        while (it.hasNext()) {
            Peer p = it.next();
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    /**
	 * Notifies all Listeners about the updates of a
	 * previous RTorrentManager.updatePeers call.
	 */
    public void updateGUI() {
        try {
            if (updatedPeers.size() > 0) propertyChangeSupport.firePropertyChange(PROP_UPDATED, null, updatedPeers);
            if (newPeers.size() > 0) propertyChangeSupport.firePropertyChange(PROP_NEW, null, newPeers);
            if (removedPeers.size() > 0) propertyChangeSupport.firePropertyChange(PROP_REMOVED, null, removedPeers);
            updatedPeers.clear();
            newPeers.clear();
            removedPeers.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * If a Peer changed locally, then add it to list of changed Peers and notify the gui.
	 */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof Peer) {
            if (evt.getPropertyName().equals(Peer.PROP_CHANGED)) {
                updatedPeers.add((Peer) evt.getSource());
                propertyChangeSupport.firePropertyChange(PROP_UPDATED, null, updatedPeers);
            }
        }
    }

    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
