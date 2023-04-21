package engine.clients;

import java.util.EventListener;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;

public interface ClientEventsListenerIF extends EventListener {

    public void DataArrived(ChannelHandlerContext ctx, MessageEvent e);

    public void DataSentFromQueue(ChannelBuffer bufferedMessage);

    public void Connected(ChannelHandlerContext ctx, ChannelStateEvent e);

    public void Disconnected(ChannelHandlerContext ctx, ChannelStateEvent e);

    public void ExceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e);
}
