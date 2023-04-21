package org.gudy.azureus2.core3.tracker.host;

/**
 * @author parg
 *
 */
public interface TRHostTorrentWillBeRemovedListener {

    public void torrentWillBeRemoved(TRHostTorrent torrent) throws TRHostTorrentRemovalVetoException;
}
