package mmq;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import mmq.proto.Marathon;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;

public class ClientMain extends IoHandlerAdapter {

    private final NioSocketConnector connector;

    private IoSession session;

    private ClientMain(Message prototype) {
        connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.setHandler(this);
    }

    public void connect(SocketAddress address) {
        session = connector.connect(address).awaitUninterruptibly().getSession();
    }

    public void send(Marathon.Message message) {
        int size = message.getSerializedSize();
        System.out.println(String.format("Message %d: %d", message.getId(), size + CodedOutputStream.computeRawVarint32Size(size)));
        session.write(message);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        ClientMain client = new ClientMain(Marathon.Ack.getDefaultInstance());
        client.connect(new InetSocketAddress("localhost", 6667));
        int i = 1;
        client.send(Marathon.Message.newBuilder().setId(i++ * 100).build());
        client.send(Marathon.Message.newBuilder().setId(i++ * 100).setExtension(Marathon.StringMessage.payload, FileUtils.readFileToString(new File("src/main/java/mmq/ClientMain.java"))).addHeader(Marathon.Header.newBuilder().setName("foo").setExtension(Marathon.StringHeader.value, "BAZ")).addHeader(Marathon.Header.newBuilder().setName("meep").setExtension(Marathon.IntegerHeader.value, 666)).build());
        client.send(Marathon.Message.newBuilder().setId(i++ * 100).setExtension(Marathon.BytesMessage.payload, ByteString.copyFrom(new byte[] { 6, 6, 6 })).addHeader(Marathon.Header.newBuilder().setName("gromit").setExtension(Marathon.StringHeader.value, "walace")).build());
        client.send(Marathon.Message.newBuilder().setId(i++ * 100).setExtension(Marathon.StringMessage.payload, "TEST PAYLOAD").addHeader(Marathon.Header.newBuilder().setName("name").setExtension(Marathon.StringHeader.value, "Tommy")).build());
        InputStream is = ClientMain.class.getResourceAsStream("/mmq/ServerMain.class");
        try {
            client.send(Marathon.Message.newBuilder().setId(i++ * 100).setExtension(Marathon.BytesMessage.payload, ByteString.copyFrom(IOUtils.toByteArray(is))).build());
        } finally {
            IOUtils.closeQuietly(is);
        }
        is = ClientMain.class.getResourceAsStream("/mmq/ClientMain.class");
        try {
            client.send(Marathon.Message.newBuilder().setId(i++ * 100).setExtension(Marathon.BytesMessage.payload, ByteString.copyFrom(IOUtils.toByteArray(is))).addHeader(Marathon.Header.newBuilder().setName("Comment").setExtension(Marathon.StringHeader.value, "Big Binary Payload")).build());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Marathon.Ack msg = (Marathon.Ack) message;
        System.out.println(String.format("ACK: %d", msg.getId()));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
