package com.jot.user;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.log4j.Logger;
import com.jot.system.AgentRecurringRpc;
import com.jot.system.ClientDbApi;
import com.jot.system.nio.JotSocket;
import com.jot.system.pjson.Guid;
import com.jot.system.utils.HttpUtils;

/**
 * FIXME: write docs and remove unnecessary parts.
 * 
 */
public abstract class GenericAgent implements Comparable<Object> {

    public static Logger logger = Logger.getLogger(GenericAgent.class);

    AgentRecurringRpc rpc;

    protected ClientDbApi api;

    public ClientDbApi getApi() {
        return api;
    }

    public GenericAgent(WriteIntf sock, Guid session) {
        init(sock, session);
    }

    void init(WriteIntf sock, Guid session) {
        rpc = new AgentRecurringRpc((JotSocket) sock, this, session);
        this.api = rpc.api;
        rpc.setTimeToLive(2 * 60 * 1000);
        setWidget(session);
        init();
    }

    public void init() {
    }

    public void reset(SocketIntf s) {
        rpc.sock = (JotSocket) s;
        rpc.bumpTimeout();
    }

    GenericAgent() {
    }

    public GenericAgent getAgent() {
        return this;
    }

    public void bumpTimeout() {
        rpc.bumpTimeout();
    }

    public void setTimeToLive(int milliseconds) {
        rpc.setTimeToLive(milliseconds);
        rpc.bumpTimeout();
    }

    public void zeroTimeout() {
        rpc.setTimeToLive(0);
        rpc.bumpTimeout();
    }

    public void noTimeToLive() {
        rpc.setTimeToLive(0);
        rpc.bumpTimeout();
    }

    public Guid getSession() {
        return rpc.guid;
    }

    public Guid getWidget() {
        return rpc.widget;
    }

    /** A widget is a 'subscription channel', or 'sub page', or any group of entities that
     * a page, or screen, or game might have in common with others.
     * It is an entirely optional optimization. The default is a random Guid.
     * See more: todo
     */
    public void setWidget(Guid widget) {
        rpc.widget = widget;
    }

    public WriteIntf getSock() {
        return rpc.sock;
    }

    /**
     * This is how a GenericAgent can write data back to a client. We could just let people write directly to the
     * SocketChannel but this implementation (in JotSocket) has some buffering.
     * 
     * @param bytes
     *            - raw data to be sent to the client.
     * @throws Exception
     */
    public void write(byte[] bytes) throws Exception {
        if (rpc.sock.isRunning()) try {
            rpc.sock.write(bytes);
        } catch (IOException e) {
            logger.warn(getSession() + " IOException Failure to write(byte[] bytes) sock=" + rpc.sock.getName());
            rpc.sock.stopRunning();
        } catch (Exception e) {
            logger.warn(getSession() + "Failure to write(byte[] bytes) ", e);
            rpc.sock.stopRunning();
        }
    }

    /**
     * This is how a GenericAgent, can write data back to a client. This call is faster than write(byte[] bytes). We
     * could just let people write directly to the SocketChannel but this implementation (in JotSocket) has some
     * buffering.
     * 
     * @param bytes
     *            - raw data to be sent to the client.
     * @throws Exception
     */
    public void write(ByteBuffer buff) throws Exception {
        if (rpc.sock.isRunning()) try {
            rpc.sock.write(buff);
        } catch (Exception e) {
            logger.warn(getSession() + " Failure to write(ByteBuffer buff) " + e.getMessage());
        }
    }

    /**
     * Return the name of the host that is servicing this agent. Useful for logging and debugging.
     * 
     * @return
     */
    public final String getServerName() {
        return rpc.g.getName();
    }

    /** return the official name of the server
     * 
     * @return
     */
    public final Guid getServerGuid() {
        return rpc.g.getGuid();
    }

    /**
     * Called when the connection closes.
     * 
     */
    public void disconnected() {
    }

    ;

    /**
     * convenience method
     * 
     * @param reply
     * @param mime
     * @throws Exception
     */
    public void sendReply(byte[] reply, String mime) throws Exception {
        ByteBuffer buf = java.nio.ByteBuffer.allocate(reply.length + 1024);
        HttpUtils.makeHTTPReply(buf, reply, mime);
        write(buf);
    }

    /**
     * convenience method
     * 
     * @param reply
     * @throws Exception
     */
    public void sendReply(byte[] reply) throws Exception {
        sendReply(reply, "text/text");
    }

    /**
     * Returns a reference to the global context for this server. JotScale is written with minimal use of static
     * variables, or singletons. All the state of a server is referenced from an object named JotContext. Normally, an
     * agent that is servicing an http request has no need to access this object. However, in some cases it's necessary.
     * Note that the toString method of this object will identify the server.
     * 
     * @return a JotContext object.
     */
    public Globals getJotContext() {
        return rpc.sock.getG();
    }

    public AgentRecurringRpc getRpc_private() {
        return rpc;
    }

    public int compareTo(Object o) {
        return rpc.compareTo(((GenericAgent) o).rpc);
    }
}
