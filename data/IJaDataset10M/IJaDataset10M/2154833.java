package info.olteanu.utils.remoteservices.dispatcher;

import info.olteanu.utils.*;
import info.olteanu.utils.remoteservices.*;
import info.olteanu.utils.remoteservices.client.*;
import info.olteanu.utils.remoteservices.server.*;
import java.util.*;
import java.io.*;

public class ApplicationDispatcherServer implements RemoteService {

    private final HashMap<String, String> map;

    public ApplicationDispatcherServer(String[] params) throws IOException {
        map = new HashMap<String, String>();
        for (int i = 0; i < params.length; ) {
            String appID = params[i];
            i++;
            String address = params[i];
            i++;
            System.err.println("Registered " + appID + " at " + address);
            map.put(appID, address);
        }
    }

    public String[] service(String[] input) throws RemoteException {
        if (input.length == 0) throw new RemoteException("Missing application id");
        String address = map.get(input[0]);
        RemoteService rs;
        try {
            rs = new RemoteConnector(address, false, false, false, false);
        } catch (IOException e) {
            throw new RemoteException(e);
        }
        if (rs == null) throw new RemoteException("Application " + input[0] + " not registered");
        return rs.service(StringTools.cutFirst(input, 1));
    }

    public static void main(String[] args) throws Exception {
        ApplicationDispatcherServer dispatcher = new ApplicationDispatcherServer(StringTools.cutFirst(args, 1));
        Server server = new Server(dispatcher, args[0]);
        server.debugLevel = 2;
        System.out.println("Providing service...");
        server.start();
    }
}
