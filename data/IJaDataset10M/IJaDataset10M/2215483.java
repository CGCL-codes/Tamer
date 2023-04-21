package org.ajwcc.pduUtils.test.integration;

import org.smslib.*;

public class SendTextConcatMessage extends AbstractTester {

    @Override
    public void test() throws Exception {
        OutboundMessage msg;
        msg = new OutboundMessage(MODEM_NUMBER, "3 8 Thank you for using this service.  Your transaction has been logged as TXN 7 abcdef 7 Thank you for using this service.  Your transaction has been logged as TXN 7 abcdefz");
        msg.setStatusReport(true);
        Service.getInstance().sendMessage(msg);
        System.out.println(msg);
        System.out.println("Now Sleeping - Hit <enter> to terminate.");
        System.in.read();
        Service.getInstance().stopService();
    }

    public static void main(String args[]) {
        SendTextConcatMessage app = new SendTextConcatMessage();
        try {
            app.initModem();
            app.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
