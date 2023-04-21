package examples.modem;

import org.smslib.IOutboundMessageNotification;
import org.smslib.Library;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

public class SendMessage {

    public void doIt() throws Exception {
        Service srv;
        OutboundMessage msg;
        OutboundNotification outboundNotification = new OutboundNotification();
        System.out.println("Example: Send message from a serial gsm modem.");
        System.out.println(Library.getLibraryDescription());
        System.out.println("Version: " + Library.getLibraryVersion());
        srv = new Service();
        SerialModemGateway gateway = new SerialModemGateway("modem.com1", "COM1", 19200, "Nokia", "6310i");
        gateway.setInbound(true);
        gateway.setOutbound(true);
        gateway.setSimPin("0000");
        gateway.setOutboundNotification(outboundNotification);
        srv.addGateway(gateway);
        srv.startService();
        System.out.println();
        System.out.println("Modem Information:");
        System.out.println("  Manufacturer: " + gateway.getManufacturer());
        System.out.println("  Model: " + gateway.getModel());
        System.out.println("  Serial No: " + gateway.getSerialNo());
        System.out.println("  SIM IMSI: " + gateway.getImsi());
        System.out.println("  Signal Level: " + gateway.getSignalLevel() + "%");
        System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");
        System.out.println();
        msg = new OutboundMessage("+306948494037", "Hello World!");
        srv.sendMessage(msg);
        System.out.println(msg);
        System.out.println("Now Sleeping - Hit <enter> to terminate.");
        System.in.read();
        srv.stopService();
    }

    public class OutboundNotification implements IOutboundMessageNotification {

        public void process(String gatewayId, OutboundMessage msg) {
            System.out.println("Outbound handler called from Gateway: " + gatewayId);
            System.out.println(msg);
        }
    }

    public static void main(String args[]) {
        SendMessage app = new SendMessage();
        try {
            app.doIt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
