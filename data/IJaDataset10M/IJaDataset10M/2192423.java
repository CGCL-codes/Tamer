package com.xentelco.asterisk.agi.codec.command;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.chain.IoHandlerCommand;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSession;
import com.xentelco.asterisk.agi.codec.server.AgiRequest;

/**
 * @author Ussama Baggili
 * 
 */
public class AgiCommandIoHandler extends IoHandlerAdapter implements MessageHandler, IoHandlerCommand {

    public void handleMessage(IoSession session, Object message) throws Exception {
        this.messageReceived(session, message);
    }

    public void exceptionCaught(IoSession session, Throwable t) throws Exception {
        t.printStackTrace();
        session.close();
    }

    public void execute(NextCommand next, IoSession session, Object message) throws Exception {
        if (message instanceof AgiRequest) {
            messageReceived(session, message);
            next.execute(session, message);
        }
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
    }

    public void messageSent(IoSession session, Object message) throws Exception {
    }

    public void sessionCreated(IoSession session) throws Exception {
        if (session instanceof NioSession) ((SocketSessionConfig) session.getConfig()).setReceiveBufferSize(2048);
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        System.out.println("Disconnecting the idle.");
        session.close();
    }
}
