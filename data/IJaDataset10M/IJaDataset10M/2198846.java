package examples.ipmodem;

import java.util.ArrayList;
import java.util.List;
import org.smslib.AGateway;
import org.smslib.ICallNotification;
import org.smslib.IGatewayStatusNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Library;
import org.smslib.Service;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.AGateway.Protocols;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.modem.IPModemGateway;
import org.smslib.modem.ModemGateway.IPProtocols;

public class ReadMessages {

    public void doIt() throws Exception {
        List<InboundMessage> msgList;
        InboundNotification inboundNotification = new InboundNotification();
        CallNotification callNotification = new CallNotification();
        GatewayStatusNotification statusNotification = new GatewayStatusNotification();
        try {
            System.out.println("Example: Read messages from a serial gsm modem.");
            System.out.println(Library.getLibraryDescription());
            System.out.println("Version: " + Library.getLibraryVersion());
            IPModemGateway gateway = new IPModemGateway("modem.com1", "127.0.0.1", 2000, "Nokia", "");
            gateway.setIpProtocol(IPProtocols.BINARY);
            gateway.setProtocol(Protocols.PDU);
            gateway.setInbound(true);
            gateway.setOutbound(true);
            gateway.setSimPin("0000");
            Service.getInstance().setInboundMessageNotification(inboundNotification);
            Service.getInstance().setCallNotification(callNotification);
            Service.getInstance().setGatewayStatusNotification(statusNotification);
            Service.getInstance().addGateway(gateway);
            Service.getInstance().startService();
            System.out.println();
            System.out.println("Modem Information:");
            System.out.println("  Manufacturer: " + gateway.getManufacturer());
            System.out.println("  Model: " + gateway.getModel());
            System.out.println("  Serial No: " + gateway.getSerialNo());
            System.out.println("  SIM IMSI: " + gateway.getImsi());
            System.out.println("  Signal Level: " + gateway.getSignalLevel() + " dBm");
            System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");
            System.out.println();
            msgList = new ArrayList<InboundMessage>();
            Service.getInstance().readMessages(msgList, MessageClasses.ALL);
            for (InboundMessage msg : msgList) System.out.println(msg);
            System.out.println("Now Sleeping - Hit <enter> to stop service.");
            System.in.read();
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Service.getInstance().stopService();
        }
    }

    public class InboundNotification implements IInboundMessageNotification {

        public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) {
            if (msgType == MessageTypes.INBOUND) System.out.println(">>> New Inbound message detected from Gateway: " + gateway.getGatewayId()); else if (msgType == MessageTypes.STATUSREPORT) System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
            System.out.println(msg);
            try {
            } catch (Exception e) {
                System.out.println("Oops!!! Something gone bad...");
                e.printStackTrace();
            }
        }
    }

    public class CallNotification implements ICallNotification {

        public void process(AGateway gateway, String callerId) {
            System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
        }
    }

    public class GatewayStatusNotification implements IGatewayStatusNotification {

        public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus) {
            System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
        }
    }

    public static void main(String args[]) {
        ReadMessages app = new ReadMessages();
        try {
            app.doIt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
