package org.openremote.controller.protocol.knx;

import org.openremote.controller.event.Stateful;

/**
 * The Class KNXStatusEvent.
 * 
 * This class can hold connectionManager, groupAddress with extending KNXEvent,
 * and also can query status for specified groupAddress with connectionManager and implementing stateful interface.
 */
public class KNXStatusEvent extends KNXEvent implements Stateful {

    /**
     * Instantiates a new kNX status event.
     * 
     * @param connectionManager the connection manager
     * @param groupAddress the group address
     */
    public KNXStatusEvent(KNXConnectionManager connectionManager, String groupAddress) {
        this.connectionManager = connectionManager;
        this.groupAddress = groupAddress;
    }

    @Override
    public String queryStatus() {
        String rst = "";
        try {
            KNXConnection connection = connectionManager.getConnection();
            String groupAddress = "1.0.0";
            String dptTypeID = getDataPointTypeID();
            rst = connection.readDeviceStatus(groupAddress, dptTypeID);
        } catch (Exception e) {
            log.error("Occured exception when excuting knxStatusEvent", e);
        }
        return rst;
    }

    /**
     * Gets the DataPointType id.
     * 
     * @return the data point type id
     */
    private String getDataPointTypeID() {
        return "1.001";
    }
}
