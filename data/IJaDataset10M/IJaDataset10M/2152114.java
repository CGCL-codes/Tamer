package mmq;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import mmq.proto.Marathon;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;

public class ServerMain extends IoHandlerAdapter {

    private final NioSocketAcceptor acceptor;

    private boolean dump;

    private ServerMain(boolean dump, Message prototype, GeneratedExtension<? extends Message, ?>... extensions) {
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        ExtensionRegistry extRegistry = ExtensionRegistry.newInstance();
        for (GeneratedExtension<? extends Message, ?> ext : extensions) extRegistry.add(ext);
        acceptor.setHandler(this);
        this.dump = dump;
    }

    public void listen(SocketAddress address) throws IOException {
        acceptor.bind(address);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        ServerMain server = new ServerMain(false, Marathon.Message.getDefaultInstance(), Marathon.StringHeader.value, Marathon.IntegerHeader.value, Marathon.StringMessage.payload, Marathon.BytesMessage.payload);
        server.listen(new InetSocketAddress(6667));
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Marathon.Message msg = (Marathon.Message) message;
        System.out.println(String.format("MESSAGE: %d", msg.getId()));
        if (dump) {
            for (Marathon.Header header : msg.getHeaderList()) {
                System.out.print("Header: ");
                System.out.print(header.getName());
                System.out.print(" = ");
                if (header.hasExtension(Marathon.StringHeader.value)) System.out.println(header.getExtension(Marathon.StringHeader.value));
                if (header.hasExtension(Marathon.IntegerHeader.value)) System.out.println(header.getExtension(Marathon.IntegerHeader.value));
            }
            if (msg.hasExtension(Marathon.StringMessage.payload)) {
                System.out.println("Payload:");
                System.out.println(msg.getExtension(Marathon.StringMessage.payload));
            }
            if (msg.hasExtension(Marathon.BytesMessage.payload)) {
                System.out.println("Payload:");
                System.out.println(msg.getExtension(Marathon.BytesMessage.payload).toString());
            }
        }
        session.write(Marathon.Ack.newBuilder().setId(msg.getId()).build());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
