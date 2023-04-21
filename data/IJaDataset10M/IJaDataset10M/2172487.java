package filebot.ui;

import javax.swing.Icon;
import javax.swing.SwingConstants;
import resources.ResourceManager;
import tuned.ui.notification.MessageNotification;
import tuned.ui.notification.NotificationManager;
import tuned.ui.notification.QueueNotificationLayout;

public class MessageManager {

    private static final int TIMEOUT = 2500;

    private static final NotificationManager manager = new NotificationManager(new QueueNotificationLayout(SwingConstants.NORTH, SwingConstants.SOUTH));

    public static void showInfo(String message) {
        show(message, ResourceManager.getIcon("message.info"), TIMEOUT);
    }

    public static void showWarning(String message) {
        show(message, ResourceManager.getIcon("message.warning"), TIMEOUT * 2);
    }

    private static void show(String message, Icon icon, int timeout) {
        manager.show(new MessageNotification("FileBot", message, icon, timeout));
    }
}
