package de.lulusoft.anothertorrent.gui.views.Providers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import de.lulusoft.anothertorrent.core.rtorrent.Peer;
import de.lulusoft.anothertorrent.core.rtorrent.PeerArrayList;

public class PeersTableContentProvider implements PropertyChangeListener, IStructuredContentProvider {

    private TableViewer viewer;

    @SuppressWarnings("unchecked")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ArrayList<Peer> changedPeers = (ArrayList<Peer>) evt.getNewValue();
        if (evt.getPropertyName().equals(PeerArrayList.PROP_UPDATED)) viewer.update(changedPeers.toArray(), null); else if (evt.getPropertyName().equals(PeerArrayList.PROP_NEW)) viewer.add(changedPeers.toArray()); else if (evt.getPropertyName().equals(PeerArrayList.PROP_REMOVED)) viewer.remove(changedPeers.toArray());
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof PeerArrayList) {
            return ((PeerArrayList) inputElement).toArray();
        }
        return new Object[0];
    }

    @Override
    public void dispose() {
        if (viewer.getInput() instanceof PeerArrayList) {
            PeerArrayList Peers = (PeerArrayList) viewer.getInput();
            Peers.removeListener(this);
        }
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.viewer = (TableViewer) viewer;
        if (oldInput instanceof PeerArrayList) {
            PeerArrayList Peers = (PeerArrayList) oldInput;
            Peers.removeListener(this);
        }
        if (newInput instanceof PeerArrayList) {
            PeerArrayList Peers = (PeerArrayList) newInput;
            Peers.addListener(this);
        }
        viewer.refresh();
    }
}
