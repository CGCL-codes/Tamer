package wevote_app;

import org.smslib.AGateway;
import org.smslib.IOutboundMessageNotification;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

/**
 *
 * @author hollgam
 */
public class SendMessage {

    /**
     * 
     */
    public static boolean sendingPollsStopped = false;

    /**
     * Sends the poll to mobile phones that are registered.
     * @throws Exception
     */
    public void doIt() throws Exception {
        try {
            Service srv;
            OutboundMessage msg;
            OutboundNotification outboundNotification = new OutboundNotification();
            ;
            srv = new Service();
            SerialModemGateway gateway = new SerialModemGateway("modem.com1", Main.commPort, Main.baud, Main.modemManufactorer, Main.modemModel);
            gateway.setInbound(true);
            gateway.setOutbound(true);
            gateway.setSimPin(Main.pinCode);
            srv.setOutboundMessageNotification(outboundNotification);
            srv.addGateway(gateway);
            srv.startService();
            for (MobileNumber mobileNumber : Main.mobileNumberArray) {
                msg = new OutboundMessage(mobileNumber.getPhoneNumber(), Main.currentQuestion);
                try {
                    srv.sendMessage(msg);
                    Main.frame.refreshLog("Poll has been sent to " + mobileNumber.getPhoneNumber() + ".");
                } catch (Exception e) {
                    Main.frame.refreshLog("Failed to send Poll to " + mobileNumber.getPhoneNumber() + ".");
                }
                Main.frame.setBattery(Integer.toString(gateway.getBatteryLevel()));
                Main.frame.setSignal(Integer.toString(gateway.getSignalLevel()));
            }
            srv.stopService();
            sendingPollsStopped = true;
        } catch (Exception e) {
        }
    }

    /**
         *
         */
    public class OutboundNotification implements IOutboundMessageNotification {

        /**
             *
             * @param gateway
             * @param msg
             */
        public void process(AGateway gateway, OutboundMessage msg) {
        }
    }
}
