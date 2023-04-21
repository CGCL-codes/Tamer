package org.matsim.network;

import org.matsim.mobsim.QueueNetworkLayer;

public class NetworkLayerBuilder {

    public static final int NETWORK_DEFAULT = 0;

    public static final int NETWORK_SIMULATION = 1;

    private static int type = 0;

    public static NetworkLayer newNetworkLayer() {
        NetworkLayer result = null;
        switch(type) {
            case NETWORK_SIMULATION:
                result = new QueueNetworkLayer();
                break;
            case NETWORK_DEFAULT:
            default:
                result = new NetworkLayer();
                break;
        }
        ;
        return result;
    }

    public static void setNetworkLayerType(int newtype) {
        type = newtype;
    }
}
