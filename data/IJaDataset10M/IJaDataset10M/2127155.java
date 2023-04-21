package org.nakedobjects.plugins.remoting.command.server;

import org.nakedobjects.plugins.remoting.command.transport.socket.server.ServerListenerAbstract;
import org.nakedobjects.plugins.remoting.command.transport.socket.server.ServerListenerInstallerAbstract;
import org.nakedobjects.runtime.remoting.ServerListenerInstaller;

public class SerializingOverSocketsServerListenerInstaller extends ServerListenerInstallerAbstract implements ServerListenerInstaller {

    @Override
    protected ServerListenerAbstract createListenerInstance() {
        return new SerializingOverSocketsServerListener();
    }

    public String getName() {
        return "serializing";
    }
}
