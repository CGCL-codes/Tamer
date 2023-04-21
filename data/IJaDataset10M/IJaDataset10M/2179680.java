package TestTraces;

import Actuator.MessageActuator;
import Listener.MessageListener;
import Message.AbstractMessage;
import Message.PacketField;
import NetworkEngine.ConcretePacket;
import NetworkEngine.INetworkTransport;
import NetworkEngine.INetworkTransport.Protocol;
import NetworkEngine.ISocket;
import NetworkEngine.NetworkLibrary;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Paul Grace
 */
public class SLPMultipleReceiver {

    private static String MDL = "SLP.txt";

    private static String DSLFolder = "DSL/MDL";

    public static void main(String[] args) throws FileNotFoundException {
        String thisDir = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        String MDLFile = thisDir + separator + DSLFolder + separator + MDL;
        int[] list = new int[] { 1, 2, 3, 4, 5, 15, 16, 17, 18, 19, 20, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 83, 85, 86, 87, 88, 88, 89, 90 };
        Hashtable hht = new Hashtable();
        for (int i = 0; i < list.length; i++) {
            hht.put("Service" + list[i], "Service" + list[i]);
        }
        INetworkTransport network = (INetworkTransport) NetworkLibrary.instance();
        Protocol pType = Protocol.UDP;
        ISocket socket = network.newSocket("239.255.255.253", 5356, pType, true);
        ConcretePacket receivedMessage = null;
        int i = 0;
        while (true) {
            try {
                receivedMessage = network.Receive(socket);
                MessageListener Listener = new MessageListener(MDLFile);
                AbstractMessage msg = Listener.MessageParse(receivedMessage.msg);
                PacketField pF = msg.Fields.get("SRVListstringTable".toLowerCase());
                String request = (String) pF.Value;
                System.out.println("The Service Type is: " + request);
                i++;
                String result = (String) hht.get(request);
                if (result != null) {
                    Hashtable FieldMsg = new Hashtable<String, Object>();
                    FieldMsg.put("Version".toLowerCase(), 2);
                    FieldMsg.put("Function-ID".toLowerCase(), 2);
                    FieldMsg.put("MessageLength".toLowerCase(), 48);
                    FieldMsg.put("mcast".toLowerCase(), 0);
                    FieldMsg.put("Next-Ext-Offset".toLowerCase(), 0);
                    FieldMsg.put("XID".toLowerCase(), 2345);
                    FieldMsg.put("LanguageTagLen".toLowerCase(), 2);
                    FieldMsg.put("Language-Tag".toLowerCase(), "en");
                    FieldMsg.put("ErrorCode".toLowerCase(), 0);
                    FieldMsg.put("URLEntryCount".toLowerCase(), 1);
                    FieldMsg.put("URLEntry".toLowerCase(), "http://149.33.45.67/test" + i);
                    i++;
                    MessageActuator results = new MessageActuator(MDLFile);
                    AbstractMessage test = new AbstractMessage("SLPSrvReply");
                    Enumeration fields = FieldMsg.keys();
                    while (fields.hasMoreElements()) {
                        String fieldName = (String) fields.nextElement();
                        PacketField tmp = new PacketField(fieldName);
                        tmp.setValue(FieldMsg.get(fieldName));
                        test.Fields.put(fieldName, tmp);
                    }
                    byte[] Message = results.MessageCompose(test);
                    ISocket socket2 = network.newSocket(receivedMessage.IPaddress, receivedMessage.Port, pType, false);
                    network.Send(socket2, Message);
                }
            } catch (Exception e) {
            }
        }
    }
}
