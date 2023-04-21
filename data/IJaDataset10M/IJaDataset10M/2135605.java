package net.sf.amemailchecker.gui.notification;

public interface INotificationWindow {

    void showNotification();

    void showNotification(NotificationType type);

    void hideNotification();
}
