package phex.net.repres;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.ByteChannel;
import phex.common.address.DestAddress;

public interface SocketFacade extends Closeable {

    public void setSoTimeout(int socketRWTimeout) throws SocketException;

    public ByteChannel getChannel() throws IOException;

    public void close() throws IOException;

    public DestAddress getRemoteAddress();
}
