package xslt.debugger;

import org.apache.fop.messaging.MessageListener;
import org.apache.fop.messaging.MessageEvent;

public class FOPMessageListener implements MessageListener {

    Observer observer;

    StringBuffer line = new StringBuffer(80);

    public FOPMessageListener(Observer observer) {
        this.observer = observer;
    }

    public void processMessage(MessageEvent event) {
        String message = event.getMessage();
        int fromIndex = 0;
        int endIndex;
        while ((endIndex = message.indexOf("\n", fromIndex)) != -1) {
            line.append(message.substring(fromIndex, endIndex));
            observer.displayInfo("Message: " + line.toString());
            line.delete(0, line.length());
            fromIndex = endIndex + 1;
        }
        line.append(message.substring(fromIndex));
        observer.displayInfo("Message: " + line.toString());
        line.delete(0, line.length());
    }
}
