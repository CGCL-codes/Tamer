package egs.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClientManager {

    private Map<String, ClientState> stateMap;

    private List<String> waitingOpps;

    private Map<String, String> offerMap;

    public ClientManager() {
        stateMap = new HashMap<String, ClientState>();
        waitingOpps = new LinkedList<String>();
        offerMap = new HashMap<String, String>();
    }

    public synchronized void setClientState(String name, ClientState state) {
        stateMap.put(name, state);
    }

    public synchronized ClientState getClientState(String name) {
        if (stateMap.containsKey(name)) {
            ClientState state = stateMap.get(name);
            return state;
        }
        return null;
    }

    public synchronized void removeClientState(String name) {
        if (stateMap.containsKey(name)) {
            stateMap.remove(name);
        }
    }

    public synchronized LinkedList<String> getOppList() {
        LinkedList<String> list = new LinkedList<String>();
        list.addAll(waitingOpps);
        return list;
    }

    public synchronized boolean isOppWaiting(String opp) {
        return waitingOpps.contains(opp);
    }

    public synchronized boolean addWaitingOpp(String opp) {
        if (!waitingOpps.contains(opp)) {
            waitingOpps.add(opp);
            return true;
        }
        return false;
    }

    public synchronized boolean removeWaitingOpp(String opp) {
        if (waitingOpps.contains(opp)) {
            waitingOpps.remove(opp);
            if (offerMap.containsKey(opp)) {
                offerMap.remove(opp);
            }
            return true;
        }
        return false;
    }

    public synchronized boolean setOppOffer(String opp, String offeringOpp) {
        if (waitingOpps.contains(opp) && !offerMap.containsKey(opp)) {
            offerMap.put(opp, offeringOpp);
            return true;
        }
        return false;
    }

    public synchronized String getOppOffer(String opp) {
        if (offerMap.containsKey(opp)) {
            return offerMap.get(opp);
        }
        return null;
    }

    public synchronized boolean unsetOppOffer(String opp) {
        if (offerMap.containsKey(opp)) {
            offerMap.remove(opp);
            return true;
        }
        return false;
    }
}
