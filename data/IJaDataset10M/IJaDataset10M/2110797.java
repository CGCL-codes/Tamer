package common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import viewerprotocol.Vp_notification_msg;
import viewerprotocol.Vp_error;

public class NotificationEventObject {

    private Vp_notification_msg msg;

    private List _listeners = new ArrayList();

    public synchronized void generateNotification(Vp_notification_msg m) {
        msg = m;
        fireNotificationEvent();
    }

    public synchronized void addNotificationListener(NotificationEventListener l) {
        _listeners.add(l);
    }

    public synchronized void removeNotificationListener(NotificationEventListener l) {
        _listeners.remove(l);
    }

    private synchronized void fireNotificationEvent() {
        NotificationEvent se = new NotificationEvent(this, msg);
        Iterator listeners = _listeners.iterator();
        while (listeners.hasNext()) {
            switch(msg.getErrorCode()) {
                case Vp_error.VP_CMDSTOP_SUCCESS:
                    ((NotificationEventListener) listeners.next()).stopReplyReceived(se);
                    break;
                case Vp_error.VP_CMDSTART_SUCCESS:
                    ((NotificationEventListener) listeners.next()).startReplyReceived(se);
                    break;
                case Vp_error.VP_CMDKILL_SUCCESS:
                    ((NotificationEventListener) listeners.next()).killReplyReceived(se);
                    break;
                case Vp_error.VP_QUERY_REMOVE_SUCCESS:
                    ((NotificationEventListener) listeners.next()).queryRemoveReplyReceived(se);
                    break;
                case Vp_error.VP_SEND_QUERY_SUCCESS:
                    ((NotificationEventListener) listeners.next()).sendQueryReplyReceived(se);
                    break;
                case Vp_error.VP_CONFIG_SUCCESS:
                    ((NotificationEventListener) listeners.next()).sendConfigReplyReceived(se);
                    break;
            }
        }
    }
}
