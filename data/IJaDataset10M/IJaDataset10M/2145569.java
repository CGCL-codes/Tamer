package net.natlib.testing.demuxing;

import java.io.IOException;
import java.net.InetSocketAddress;
import net.natlib.chat.ChatMessage;
import net.natlib.chat.ChatProtocolHandler;
import net.natlib.codec.NATProtocolCodecFactory;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketConnector;

public class DemuxingTester extends DemuxingIoHandler {

    public DemuxingTester() {
        this.addMessageHandler(String.class, new TextMessageHandler());
        this.addMessageHandler(Integer.class, new IntMessageHandler());
    }

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        DemuxingTester tester;
        tester = new DemuxingTester();
        try {
            if (args.length == 0) {
                System.out.println("Starting as server");
                SocketAcceptor acceptor = new SocketAcceptor();
                acceptor.bind(new InetSocketAddress(4330), tester);
            } else {
                System.out.println("Starting as connector..");
                SocketConnector connector = new SocketConnector();
                ConnectFuture future = connector.connect(new InetSocketAddress("192.168.0.3", 4330), tester);
                future.join();
                IoSession session = future.getSession();
                System.out.println("Writting messages...");
                session.write("Hello");
                Thread.sleep(1000);
                session.write(new Integer(5));
                System.out.println("Messages written");
                Thread.sleep(5000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("Session created from " + (InetSocketAddress) session.getRemoteAddress() + " to " + (InetSocketAddress) session.getLocalAddress());
        session.getFilterChain().addLast("codec", new ProtocolCodecFilter(new NATProtocolCodecFactory()));
    }

    @Override
    public void messageReceived(IoSession arg0, Object arg1) throws Exception {
        System.out.println("Message received trying to figure out what to do with it: " + arg1 + ", " + arg1.getClass());
        super.messageReceived(arg0, arg1);
    }
}
