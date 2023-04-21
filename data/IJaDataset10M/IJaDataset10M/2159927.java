package symore.dissemination.msgSpread;

import java.util.Properties;
import symore.dissemination.AbstractMsgSpreadFactory;
import symore.dissemination.DisseminationException;
import symore.dissemination.ISymoreDisseminator;
import symore.dissemination.ISymoreSynchronizer;

/**
 * Provides a synchronizer that exchanges the first and the second received message.
 * Only interesting for testing the db...
 * @author Frank Bregulla, Manuel Scholz
 *
 */
public class TcpOutOfOrderMsgSpreadFactory extends AbstractMsgSpreadFactory {

    private TcpOutOfOrderSynchronizer tcpSync;

    private TcpDisseminator tcpDissem;

    public ISymoreSynchronizer getSynchronizer(Properties properties) throws DisseminationException {
        if (properties == null) throw new DisseminationException("Disseminator not initialized.");
        int sendPort = Integer.parseInt(properties.getProperty("sendPort"));
        int recvPort = Integer.parseInt(properties.getProperty("receivePort"));
        tcpSync = new TcpOutOfOrderSynchronizer(sendPort, recvPort);
        return tcpSync;
    }

    public void stop() {
        tcpSync.stop();
        tcpDissem.stop();
    }

    public ISymoreDisseminator getDisseminator(Properties properties) throws DisseminationException {
        if (properties == null) throw new DisseminationException("Disseminator not initialized.");
        int sendPort = Integer.parseInt(properties.getProperty("sendPortDisseminator"));
        int recvPort = Integer.parseInt(properties.getProperty("receivePortDisseminator"));
        tcpDissem = new TcpDisseminator(sendPort, recvPort);
        return tcpDissem;
    }
}
