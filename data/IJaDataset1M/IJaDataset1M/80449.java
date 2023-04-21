package net.sourceforge.slcwsn.sensors;

import net.sourceforge.slcwsn.network.Network;
import org.apache.log4j.Logger;

/**
 *
 * @author David Miguel Antunes <davidmiguel [ at ] antunes.net>
 */
public class SensorFactory {

    private static final int BOARD_TYPE_SIMPLE_NODE_V5 = 0;

    private static final Logger logger = Logger.getLogger(SensorFactory.class);

    private Network network;

    public SensorFactory(Network network) {
        this.network = network;
    }

    public Sensor createSensor(int type, long id) throws SensorCreationException {
        switch(type) {
            case BOARD_TYPE_SIMPLE_NODE_V5:
                return new SimpleNodeSensorVersion5(id, network);
            default:
                throw new SensorCreationException("Unkown sensor type");
        }
    }
}
