package jaxlib.ee.socketserver.spi;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.resource.ResourceException;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import jaxlib.net.socket.DatagramServerPacketHandler;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: DatagramServerChannelActivationSpec.java 2749 2009-10-15 11:33:50Z joerg_wassmer $
 */
public interface DatagramServerChannelActivationSpec<H extends DatagramServerPacketHandler> extends SocketServerPortActivationSpec {

    @Nonnull
    public H createDatagramServerPacketHandler(BootstrapContext bootstrapContext, MessageEndpointFactory endpointFactory) throws ResourceException;

    @CheckForNull
    public Boolean getBroadcast();

    @CheckForNull
    public Integer getDatagramSizeInbound();

    @CheckForNull
    public Integer getDatagramSizeOutbound();

    public void setBroadcast(@Nullable Boolean v);

    public void setDatagramSizeInbound(@Nullable Integer v);

    public void setDatagramSizeOutbound(@Nullable Integer v);
}
