package engine.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import util.Globals;

@ChannelPipelineCoverage("all")
public class Handler extends SimpleChannelHandler {

    public Handler() {
        super();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        Globals.getInstance().getServer().fireDataArrived(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
        Server.getChannelsArray().remove(e.getChannel().getId());
        Globals.getInstance().getServer().fireExceptionCaught(ctx, e);
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Server.getAllChannels().add(e.getChannel());
        Server.getChannelsArray().put(e.getChannel().getId(), e.getChannel().getRemoteAddress().toString());
        Globals.getInstance().getServer().fireChannelOpen(ctx, e);
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Server.getAllChannels().remove(e.getChannel());
        Server.getChannelsArray().remove(e.getChannel().getId());
        Globals.getInstance().getServer().fireChannelClose(ctx, e);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
