package TestTraces;

import Listener.TextMessageListener;
import Message.AbstractMessage;
import Message.PacketField;
import NetworkEngine.ConcretePacket;
import NetworkEngine.INetworkTransport;
import NetworkEngine.INetworkTransport.Protocol;
import NetworkEngine.ISocket;
import NetworkEngine.NetworkLibrary;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Paul Grace
 */
public class UPnPMultipleSender {

    private static String MDL = "SSDP.txt";

    private static String DSLFolder = "DSL/MDL";

    public static void main(String[] args) {
        ArrayList<Long> lVals = new ArrayList<Long>();
        String thisDir = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        String MDLFile = thisDir + separator + DSLFolder + separator + MDL;
        INetworkTransport network = (INetworkTransport) NetworkLibrary.instance();
        Protocol pType = Protocol.UDP;
        ISocket socket = network.newSocket("239.255.255.250", 1901, pType, true);
        for (int k = 0; k < 25; k++) {
            int percent = 0;
            ArrayList<Long> lVals2 = new ArrayList<Long>();
            for (int j = 0; j < 100; j++) {
                long timeOne = System.nanoTime();
                try {
                    String msg = "M-SEARCH * HTTP/1.1 \r\n" + "S: uuid:ijklmnop-7dec-11d0-a765-00a0c91e6bf6 \r\n" + "Host: 239.255.255.250:1901 \r\n" + "Man: \"ssdp:discover\" \r\n" + "ST: service" + j + " \r\n" + "MX: 3 \r\n";
                    network.Send(socket, msg.getBytes());
                    ConcretePacket receivedMessage = null;
                    receivedMessage = network.Receive(socket, 2000);
                    if (receivedMessage != null) {
                        percent += 1;
                        System.out.println(new String(receivedMessage.msg));
                        TextMessageListener Listener = new TextMessageListener(MDLFile);
                        AbstractMessage msg22 = Listener.MessageParse(receivedMessage.msg);
                        System.out.println("The msg is: " + msg22.MessageName);
                        PacketField pF = msg22.Fields.get("LOCATION".toLowerCase());
                        System.out.println("The Service URL is: " + pF.Value);
                        String addr = (String) pF.Value;
                        StringTokenizer st = new StringTokenizer(addr.substring(1), ":");
                        String host = st.nextToken();
                        int port = new Integer(st.nextToken()).intValue();
                        ISocket socket2 = network.newSocket(host, port, Protocol.TCP, false);
                        socket2.Connect(host, port);
                        String msg2 = "GET /index.htm HTTP/1.1 \r\n" + "Host: 239.255.255.250:1901 \r\n\r\n";
                        socket2.sendMessage(msg2.getBytes());
                        ConcretePacket r2 = socket2.receiveMessage();
                        long timeTwo = System.nanoTime();
                        System.out.println(new String(r2.msg));
                        lVals.add(timeTwo - timeOne);
                        lVals2.add(timeTwo - timeOne);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Run " + k);
            System.out.println("----------------------------------------");
            System.out.println("Percentage found: " + percent);
            System.out.println("The Median is: " + Statistics.Median(lVals2));
            System.out.println("The Max is: " + Statistics.Max(lVals2));
            System.out.println("The Min is: " + Statistics.Min(lVals2));
            System.out.println("The Mean is: " + Statistics.Mean(lVals2));
            lVals2.clear();
        }
        System.out.println("Overall");
        System.out.println("----------------------------------------");
        System.out.println("The Median is: " + Statistics.Median(lVals));
        System.out.println("The Max is: " + Statistics.Max(lVals));
        System.out.println("The Min is: " + Statistics.Min(lVals));
        System.out.println("The Mean is: " + Statistics.Mean(lVals));
    }
}
