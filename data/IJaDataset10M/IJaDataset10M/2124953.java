package org.limewire.mojito.util;

import java.net.SocketAddress;
import java.util.concurrent.ExecutionException;
import org.limewire.mojito.MojitoDHT;
import org.limewire.mojito.concurrent.DHTFuture;
import org.limewire.mojito.result.BootstrapResult;
import org.limewire.mojito.result.PingResult;

public class MojitoUtils {

    private MojitoUtils() {
    }

    /**
     * A helper method to bootstrap a MojitoDHT instance.
     * 
     * It tries to ping the given SocketAddress (this blocks) and in
     * case of a success it will kick off a bootstrap process and returns
     * a DHTFuture for the process.
     */
    public static DHTFuture<BootstrapResult> bootstrap(MojitoDHT dht, SocketAddress addr) throws ExecutionException, InterruptedException {
        PingResult pong = dht.ping(addr).get();
        return dht.bootstrap(pong.getContact());
    }
}
