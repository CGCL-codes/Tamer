package org.nomadpim.core.ui.notification;

public interface INotification {

    /**
     * Returns additional information that is relevant, but this not on the
     * level of the details, for example who sent an email that caused a
     * notification.
     */
    String getAdditionalDescription();

    /**
     * Returns the main description for this notification, e.g. the subject of
     * the email.
     */
    String getDescription();

    /**
     * Returns additional details about this notification that are used in
     * previews, e.g. tooltips.
     */
    String getDetails();

    /**
     * Returns an abstract description of the event that caused this
     * notification, e.g. "New EMail".
     */
    String getEventDescription();
}
