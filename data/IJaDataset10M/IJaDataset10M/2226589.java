package joliex.rmi;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import jolie.net.CommListener;
import jolie.Interpreter;
import jolie.net.ext.CommProtocolFactory;
import jolie.net.ports.InputPort;

public class RMIListener extends CommListener {

    private Registry registry;

    private final String entryName;

    private final JolieRemote jolieRemoteStub;

    public RMIListener(Interpreter interpreter, CommProtocolFactory protocolFactory, InputPort inputPort) throws IOException {
        super(interpreter, protocolFactory, inputPort);
        JolieRemote jolieRemote = new JolieRemoteImpl(interpreter, this);
        jolieRemoteStub = (JolieRemote) UnicastRemoteObject.exportObject(jolieRemote);
        registry = LocateRegistry.getRegistry(inputPort.location().getHost(), inputPort.location().getPort());
        entryName = inputPort.location().getPath();
        try {
            registry.bind(entryName, jolieRemoteStub);
        } catch (AlreadyBoundException e) {
            throw new IOException(e);
        } catch (RemoteException e) {
            if (e instanceof java.rmi.ConnectException) {
                registry = LocateRegistry.createRegistry(inputPort.location().getPort());
                try {
                    registry.bind(entryName, jolieRemoteStub);
                } catch (AlreadyBoundException ae) {
                    throw new IOException(ae);
                }
            } else {
                throw new IOException(e);
            }
        }
    }

    @Override
    public void shutdown() {
        try {
            registry.unbind(entryName);
        } catch (RemoteException e) {
        } catch (NotBoundException e) {
        }
    }

    @Override
    public void run() {
    }

    @Override
    public void start() {
    }
}
