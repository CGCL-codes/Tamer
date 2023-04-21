package castadiva;

import castadiva.TrafficRecords.RandomTrafficRecord;
import castadiva.TrafficRecords.TrafficRecord;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * This class store the information about the interaction between AP. Contain all
 * the APs.
 *
 * @author Jorge Hortelano
 * @version %I%, %G%
 * @since 1.4
 * @see AP
 */
public class APs implements Serializable {

    private List<AP> accessPoints;

    private Vector<TrafficRecord> trafficData;

    private Vector<RandomTrafficRecord> randomTrafficData;

    /**
     * The constructor prepare a list to store all the APs of the simulation and
     * the traffic defined between them.
     */
    public APs() {
        trafficData = new Vector();
        randomTrafficData = new Vector<RandomTrafficRecord>();
        accessPoints = new ArrayList<AP>();
    }

    /**
     * Return how many APs are storted in this class.
     */
    public Integer Size() {
        return accessPoints.size();
    }

    /**
     * Return an AP stored in the parameter given.
     *
     * @param i the index of the AP.
     */
    public AP Get(int i) {
        return (AP) accessPoints.get(i);
    }

    /**
     * Store a given AP into the position i.
     *
     * @param i The postion to store the AP.
     * @param node The AP to store.
     */
    public void Set(Integer i, AP node) {
        accessPoints.set(i, node);
    }

    /**
     * Store an AP at the end of the list.
     *
     * @param node The AP to store.
     */
    public void Add(AP node) {
        accessPoints.add(node);
    }

    /**
     * Delete the AP of the list.
     *
     * @param node The Object to delete.
     */
    public void Remove(AP node) {
        accessPoints.remove(node);
    }

    /**
     * Delete the AP stored in a defined position.
     *
     * @param i The position to delete.
     */
    public void RemoveIndex(int i) {
        accessPoints.remove(i);
    }

    /**
     * Return the traffic defined between all the nodes.
     *
     * @return The Vector in each line represent a flux of packets.
     */
    public Vector<TrafficRecord> GetTraffic() {
        return trafficData;
    }

    /**
     * Return the random traffic defined in the scenary.
     * @return The Vector in each line represent a flux of packets.
     */
    public Vector<RandomTrafficRecord> GetRandomTraffic() {
        return randomTrafficData;
    }

    /**
     * Store a new traffic defined to be used.
     * @param traffic A Vector to be used as traffic.
     */
    public void SetTraffic(Vector<TrafficRecord> traffic) {
        trafficData = traffic;
    }

    /**
     * Store a new traffic defined to be used.
     * @param traffic A Vector to be used as traffic.
     */
    public void SetRandomTraffic(Vector<RandomTrafficRecord> traffic) {
        randomTrafficData = traffic;
    }

    /**
     * Return number of flux of packets defined in the simulation.
     */
    public Integer GetTrafficSize() {
        return trafficData.size() - 1;
    }

    /**
     * Return number of flux of packets defined in the random simulation.
     */
    public Integer GetRandomTrafficSize() {
        return randomTrafficData.size() - 1;
    }

    /**
     * Return the IP of a desired AP.
     * @param tag The Id of the AP to search
     */
    public String SearchIP_AP(String tag) {
        AP nodo;
        for (int i = 0; i < accessPoints.size(); i++) {
            nodo = (AP) accessPoints.get(i);
            if (nodo.WhatAP().equals(tag)) {
                return nodo.WhatWifiIP();
            }
        }
        return null;
    }

    /**
     * Return the position of an AP stored in the list.
     *
     * @param tag The Id of the AP to search.
     */
    public Integer SearchAP(String tag) {
        AP router;
        for (int i = 0; i < accessPoints.size(); i++) {
            router = (AP) accessPoints.get(i);
            if (router.WhatAP().toString().trim().equals(tag)) return i;
        }
        return -1;
    }
}
