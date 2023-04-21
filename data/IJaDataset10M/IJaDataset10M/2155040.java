package org.protune.core;

import org.protune.api.*;

/**
 * Dummy implementation of the interface {@link org.protune.core.Checker}, which is unable to handle any
 * notification type.
 * @author jldecoi
 */
public class DummyChecker implements Checker {

    public Check checkNotification(ActionWellPerformed awp) throws UnknownNotificationException {
        throw new UnknownNotificationException();
    }

    public Check checkNotification(ActionWrongPerformed awp) throws UnknownNotificationException {
        throw new UnknownNotificationException();
    }

    public Check checkNotification(Notification n) throws UnknownNotificationException {
        throw new UnknownNotificationException();
    }
}
