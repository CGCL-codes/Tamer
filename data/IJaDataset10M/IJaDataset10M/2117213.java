package mou.net.transport;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;

public interface Connection {

    public abstract DataInputStream getIn();

    public abstract DataOutputStream getOut();

    public abstract InetSocketAddress getTarget();
}
