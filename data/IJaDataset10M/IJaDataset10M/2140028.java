package info.olteanu.utils.remoteservices.distributed.loadbalancing.strategies;

import info.olteanu.utils.remoteservices.*;
import info.olteanu.utils.remoteservices.distributed.loadbalancing.*;
import java.io.*;

public class RandomPingLoadBalancingClient implements RemoteService {

    private int noPings;

    private LoadBalancingClient[] lbc;

    public RandomPingLoadBalancingClient(LoadBalancingClient[] lbc, int noPings) {
        this.lbc = lbc;
        this.noPings = noPings;
    }

    public RandomPingLoadBalancingClient(String[] serverUrl, int noPings) throws IOException {
        this.lbc = new LoadBalancingClient[serverUrl.length];
        for (int i = 0; i < serverUrl.length; i++) this.lbc[i] = new LoadBalancingClient(serverUrl[i]);
        this.noPings = noPings;
    }

    public String[] service(String[] input) throws RemoteException {
        int minLoad = Integer.MAX_VALUE;
        int idMinLoad = -1;
        for (int i = 0; i < noPings; i++) {
            int id = (int) (Math.random() * Integer.MAX_VALUE) % lbc.length;
            int load = lbc[id].getLoad();
            if (load == 0) return lbc[id].service(input);
            if (load < minLoad) {
                minLoad = load;
                idMinLoad = id;
            }
        }
        return lbc[idMinLoad].service(input);
    }
}
