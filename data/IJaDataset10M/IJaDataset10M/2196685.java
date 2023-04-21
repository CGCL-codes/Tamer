package com.hs.mail.imap.server;

import java.io.PrintStream;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.MessageEvent;

/**
 * A ChannelHandler that logs all protocol streams.
 * 
 * @author Won Chul Doh
 * @since Jul 7, 2010
 */
@ChannelPipelineCoverage("all")
public class DebuggingHandler implements ChannelUpstreamHandler, ChannelDownstreamHandler {

    private PrintStream out;

    public DebuggingHandler() {
    }

    /**
	 * Set the stream to be used for debugging output. If <code>out</code> is
	 * null, <code>System.out</code> will be used.
	 * 
	 * @param out
	 *            the PrintStream to use for debugging output
	 */
    public synchronized void setDebugOut(PrintStream out) {
        this.out = out;
    }

    /**
	 * Returns the stream to be used for debugging output. If no stream has been
	 * set, <code>System.out</code> is returned.
	 * 
	 * @return the PrintStream to use for debugging output.
	 */
    public synchronized PrintStream getDebugOut() {
        if (out == null) return System.out; else return out;
    }

    private String toString(ChannelBuffer buffer) {
        byte[] dst = new byte[buffer.readableBytes()];
        buffer.getBytes(buffer.readerIndex(), dst);
        return new String(dst);
    }

    /**
	 * Logs the specified event to the {@link out} returned by
	 * {@link #getDebugOut()}.
	 */
    public void debug(ChannelEvent e) {
        if (e instanceof MessageEvent) {
            Object msg = ((MessageEvent) e).getMessage();
            if (msg instanceof ChannelBuffer) {
                getDebugOut().print(toString((ChannelBuffer) msg));
            } else {
                getDebugOut().print(msg);
            }
        }
    }

    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e != null) {
            debug(e);
            ctx.sendUpstream(e);
        }
    }

    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e != null) {
            debug(e);
            ctx.sendDownstream(e);
        }
    }
}
