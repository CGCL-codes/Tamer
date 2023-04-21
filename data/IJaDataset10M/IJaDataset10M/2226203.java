package example;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.yz.net.Configure;
import com.yz.net.IoFuture;
import com.yz.net.IoHandler;
import com.yz.net.IoSession;
import com.yz.net.NetMessage;
import com.yz.net.ProtocolHandler;
import com.yz.net.expand.IoConnector;

public class ClientExample {

    static Random rand = new Random();

    public static void main(String[] args) {
        try {
            Configure config = new Configure();
            config.setAddress(new InetSocketAddress("127.0.0.1", 8899));
            config.setProtocolHandler(new Protocol());
            config.setIoHandler(new DataHandler());
            IoConnector connector = new IoConnector();
            config.start(connector);
            IoSession session = IoConnector.newSession(connector);
            IoFuture future = session.connect();
            future.await();
            while (true) {
                int num = rand.nextInt(5000);
                session.write(new ExampleMessage(num));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class DataHandler implements IoHandler {

        @Override
        public void ioSessionClosed(IoFuture future) {
        }

        @Override
        public void messageReceived(IoSession session, NetMessage msg) {
            ExampleMessage message = (ExampleMessage) msg;
            System.out.println("Num = " + message.getNumber());
        }

        @Override
        public void messageReceived(IoSession session, byte[] msgdata) {
        }
    }

    public static class Protocol implements ProtocolHandler {

        @Override
        public List<NetMessage> onData(ByteBuffer data, IoSession session) {
            ArrayList<NetMessage> list = new ArrayList<NetMessage>();
            while (data.remaining() >= 4) {
                int number = data.getInt();
                list.add(new ExampleMessage(number));
            }
            return list;
        }
    }

    public static class ExampleMessage implements NetMessage {

        private int number;

        public ExampleMessage(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        @Override
        public byte[] getContent() {
            byte[] content = new byte[4];
            content[0] = (byte) ((number >>> 24) & 0xFF);
            content[1] = (byte) ((number >>> 16) & 0xFF);
            content[2] = (byte) ((number >>> 8) & 0xFF);
            content[3] = (byte) ((number >>> 0) & 0xFF);
            return content;
        }
    }

    public static class PrintRspTime implements Runnable {

        ConcurrentLinkedQueue<long[]> queue = new ConcurrentLinkedQueue();

        @Override
        public void run() {
            while (true) {
                long[] times = queue.poll();
                if (times != null) {
                    System.out.println("RSPTIME = " + (times[1] - times[0]));
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
