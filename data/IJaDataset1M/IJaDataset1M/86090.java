package net.sf.xqore.games.ghosts.collection;

import net.sf.xqore.games.ghosts.IPeer;

/**
 * @author chcassan
 *
 * @todo Ci vorrebbe un visitor 
 */
public interface IPeerListIterator {

    public boolean hasNext();

    public boolean hasPrevious();

    public IPeer next();

    public IPeer previous();

    public void add(IPeer peer);

    public void remove(IPeer peer);

    public void remove(long peerID);
}
