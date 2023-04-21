package be.vanvlerken.bert.logmonitor.logging;

import java.net.InetSocketAddress;

/**
 * This factory delivers Server instances, based on a String identifier 
 */
public class ServerFactory {

    public ServerFactory() {
    }

    public LogServer getLogServer(String id, InetSocketAddress listenAddress, ILogDatabase logDb, ILogEntryDecoder messageDecoder) {
        if (id.equals(MixedModeServer.class.getName())) {
            return new MixedModeServer(listenAddress, logDb, messageDecoder);
        } else if (id.equals(StringServer.class.getName())) {
            return new StringServer(listenAddress, logDb, messageDecoder);
        }
        return null;
    }

    public String[] getAvailableServers() {
        return new String[] { MixedModeServer.class.getName(), StringServer.class.getName() };
    }
}
