package joliex.rmi;

import java.io.IOException;
import java.net.URI;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import jolie.net.ext.CommChannelFactory;
import jolie.net.CommChannel;
import jolie.net.CommCore;
import jolie.net.ports.OutputPort;

public class RMICommChannelFactory extends CommChannelFactory {

    public RMICommChannelFactory(CommCore commCore) {
        super(commCore);
    }

    public CommChannel createChannel(URI location, OutputPort port) throws IOException {
        try {
            Registry registry = LocateRegistry.getRegistry(location.getHost(), location.getPort());
            JolieRemote remote = (JolieRemote) registry.lookup(location.getPath());
            return new RMICommChannel(remote.createRemoteBasicChannel());
        } catch (NotBoundException e) {
            throw new IOException(e);
        }
    }
}
