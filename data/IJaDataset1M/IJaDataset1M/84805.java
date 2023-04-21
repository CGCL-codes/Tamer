package nhb.webflag.importtools.source.channelwatch;

import java.util.Iterator;
import java.util.Map;

/**
 * Iterates over all servers
 *
 * @author hendrik
 */
public class ChannelMemberWatchServerIterator implements Iterator<ChannelMemberWatchServer> {

    private Map<String, Map<String, Map<String, Object>>> servers = null;

    private Iterator<?> itr = null;

    /**
	 * Creates a new FlagServerIterator
	 *
	 * @param servers server-data
	 */
    public ChannelMemberWatchServerIterator(Map<String, Map<String, Map<String, Object>>> servers) {
        this.servers = servers;
        this.itr = servers.keySet().iterator();
    }

    public boolean hasNext() {
        return itr.hasNext();
    }

    public ChannelMemberWatchServer next() {
        Object key = itr.next();
        return new ChannelMemberWatchServer(key.toString(), servers.get(key));
    }

    public void remove() {
        itr.remove();
    }
}
