package hr.chus.cgateway.parlayx.utils;

import hr.chus.cgateway.db.model.Message;
import hr.chus.cgateway.db.model.Message.MessageType;
import hr.chus.cgateway.parlayx.model.DeliveryReport;
import hr.chus.cgateway.parlayx.model.DeliveryReport.DeliveryStatus;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 *
 */
public class MessageUtils {

    private static Pattern deliveryReportPattern = Pattern.compile("\\bID:(\\S+)\\s+STAT:(\\S+)", 2);

    public static boolean isDeliveryReport(Message message) {
        if (message != null && message.getMessageType().equals(MessageType.SMS)) {
            String text = message.getContent();
            if (message != null) {
                return deliveryReportPattern.matcher(text).matches();
            }
        }
        return false;
    }

    public static DeliveryReport message2DeliveryReport(Message message) {
        Matcher localMatcher = deliveryReportPattern.matcher(message.getContent());
        if (localMatcher.matches()) {
            String messageId = localMatcher.group(1);
            String status = localMatcher.group(2);
            return new DeliveryReport(message.getSender(), message.getReceiver(), messageId, message.getRegistrationIdentifier(), DeliveryStatus.getStatus(status));
        }
        return null;
    }
}
