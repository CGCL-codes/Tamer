package com.volantis.mcs.migrate.impl.notification;

import com.volantis.mcs.migrate.api.notification.Notification;
import com.volantis.mcs.migrate.api.notification.NotificationType;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Notification for passing on information regarding an exception or error.
 */
public class ThrowableNotification implements Notification {

    private NotificationType type;

    /**
     * The throwable object that caused this notification to be generated.
     */
    private Throwable cause;

    /**
     * Construct a notification for a specified throwable cause.
     *
     * @param type
     * @param cause The exception or error that caused this notification
     * @throws java.lang.IllegalArgumentException if the cause is null
     */
    public ThrowableNotification(NotificationType type, Throwable cause) {
        if (type == null) {
            throw new IllegalArgumentException("Must specify a type");
        }
        if (cause == null) {
            throw new IllegalArgumentException("Must specify a Throwable cause");
        }
        this.type = type;
        this.cause = cause;
    }

    public String getMessage() {
        StringWriter writer = new StringWriter();
        PrintWriter print = new PrintWriter(writer);
        cause.printStackTrace(print);
        print.close();
        return writer.toString();
    }

    public NotificationType getType() {
        return type;
    }

    /**
     * Retrieves the root cause of the notification.
     *
     * @return The {@link java.lang.Throwable} object that caused this notification
     */
    public Throwable getThrowableCause() {
        return cause;
    }
}
