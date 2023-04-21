package ntorrent.torrenttrackers.model;

import javax.swing.DefaultListModel;

/**
 * @author Kim Eik
 *
 */
public class TorrentTrackersListModel extends DefaultListModel {

    private static final long serialVersionUID = 1L;

    public void add(TorrentTracker tt) {
        super.addElement(tt);
    }

    public void fireContentsChanged(Object source) {
        super.fireContentsChanged(source, 0, getSize() - 1);
    }
}
