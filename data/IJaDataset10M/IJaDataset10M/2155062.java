package net.sourceforge.jcoupling.peer;

import net.sourceforge.jcoupling.peer.property.RequestKey;
import net.sourceforge.jcoupling.peer.destination.Destination;
import net.sourceforge.jcoupling.wca.WCAChannel;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * @author Lachlan Aldred
 */
public abstract class Request implements Serializable {

    protected List<WCAChannel> _destinations;

    public Request(List<WCAChannel> destinations) {
        _destinations = destinations;
    }

    public Request(WCAChannel channel) {
        _destinations = new ArrayList<WCAChannel>();
        _destinations.add(channel);
    }

    public List<WCAChannel> getDestinations() {
        return _destinations;
    }
}
