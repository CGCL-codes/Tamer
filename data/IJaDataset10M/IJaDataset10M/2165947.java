package org.ajwcc.pduUtils.test.integration;

import org.smslib.*;
import org.smslib.http.*;

public class ClickatellSendPortMessage extends AbstractTester {

    @Override
    public void test() throws Exception {
        OutboundMessage msg;
        OutboundNotification outboundNotification = new OutboundNotification();
        System.out.println("Example: Send message from Clickatell HTTP Interface.");
        System.out.println(Library.getLibraryDescription());
        System.out.println("Version: " + Library.getLibraryVersion());
        ClickatellHTTPGateway gateway = new ClickatellHTTPGateway("clickatell.http.1", " 2982992", "tdelenikas", "AFghjkr3");
        gateway.setOutbound(true);
        Service.getInstance().setOutboundMessageNotification(outboundNotification);
        gateway.setSecure(true);
        Service.getInstance().addGateway(gateway);
        gateway.startGateway();
        msg = new OutboundMessage("xxxx", "Hello from SMSLib (Clickatell handler)");
        msg.setFrom("SMSLIB.ORG");
        msg.setSrcPort(0);
        msg.setDstPort(4501);
        System.out.println("Is recipient's network covered? : " + gateway.queryCoverage(msg));
        gateway.sendMessage(msg);
        System.out.println(msg);
        System.out.println(msg.getPduUserDataHeader());
        System.out.println("Remaining credit: " + gateway.queryBalance());
        System.out.println("Now Sleeping - Hit <enter> to terminate.");
        System.in.read();
        Service.getInstance().stopService();
    }

    public class OutboundNotification implements IOutboundMessageNotification {

        public void process(AGateway gateway, OutboundMessage msg) {
            System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
            System.out.println(msg);
        }
    }

    public static void main(String args[]) {
        ClickatellSendPortMessage app = new ClickatellSendPortMessage();
        try {
            app.initModem();
            app.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
