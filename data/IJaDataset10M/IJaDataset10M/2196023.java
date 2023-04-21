package org.snu.ids.servermonitor;

import org.snu.ids.servermonitor.sms.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author BabarTareen
 */
public class SmsNotifier implements INotifier {

    private Logger logger;

    private SmsSender smsSender;

    private Vector<User> phoneNumbers;

    public SmsNotifier(SmsSender smsSender, Logger logger) {
        this.smsSender = smsSender;
        this.phoneNumbers = new Vector<User>();
        this.logger = logger;
    }

    public Vector<User> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void sendAlertMessage(String message) {
        if (message.length() > 80) {
            message = message.substring(0, 80);
        }
        Sms sms = new Sms();
        sms.setMessage(message);
        for (int i = 0; i < phoneNumbers.size(); i++) {
            User user = (User) phoneNumbers.get(i);
            sms.setToNumber(user.getPhoneNumber());
            try {
                boolean smsSent = smsSender.sendSms(sms);
                if (!smsSent) {
                    logger.log(Level.SEVERE, "Unable to sending SMS");
                    return;
                }
                String logMessage = "SMS Message Sent [" + user.getName() + "," + user.getPhoneNumber() + "]:" + message;
                logger.log(Level.INFO, logMessage);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Exception in sending SMS", ex);
            }
        }
    }
}
