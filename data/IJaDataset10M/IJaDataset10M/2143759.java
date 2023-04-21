package net.sourceforge.slcwsn.event;

import net.sourceforge.slcwsn.network.Network;
import net.sourceforge.slcwsn.sensors.Sensor;
import net.sourceforge.slcwsn.supernodes.SuperNode;
import org.apache.log4j.Logger;

/**
 *
 * @author David Miguel Antunes <davidmiguel [ at ] antunes.net>
 */
public class SuperNodeDiscoveredEvent extends Event {

    private static final Logger logger = Logger.getLogger(SuperNodeDiscoveredEvent.class);

    private SuperNode node;

    public SuperNodeDiscoveredEvent(long timestamp, SuperNode node, Network network) {
        super(timestamp, network);
        this.node = node;
    }

    public SuperNode getNode() {
        return node;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "SensorDiscoveredEvent from sensor " + node + " at " + timestamp;
    }
}
