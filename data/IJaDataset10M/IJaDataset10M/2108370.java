package org.avis.client;

import java.util.Set;
import static java.util.Collections.unmodifiableSet;
import static org.avis.util.Collections.union;

/**
 * A notification event sent to Elvin subscription listeners.
 * 
 * @see Elvin#addNotificationListener(GeneralNotificationListener)
 * 
 * @author Matthew Phillips
 */
public final class GeneralNotificationEvent extends AvisEventObject {

    /**
   * The notification.
   */
    public final Notification notification;

    /**
   * The subscriptions that securely matched the notification.
   */
    public final Set<Subscription> secureMatches;

    /**
   * The subscriptions that insecurely matched the notification.
   */
    public final Set<Subscription> insecureMatches;

    /**
   * All subscriptions that matched the notification.
   */
    public final Set<Subscription> matches;

    GeneralNotificationEvent(Elvin elvin, Notification notification, Set<Subscription> theInsecureMatches, Set<Subscription> theSecureMatches) {
        super(elvin);
        this.notification = notification;
        this.insecureMatches = unmodifiableSet(theInsecureMatches);
        this.secureMatches = unmodifiableSet(theSecureMatches);
        this.matches = union(secureMatches, insecureMatches);
    }

    /**
   * The Elvin connection that the notification was received from.
   * This is the same as {@link #getSource()}.
   */
    public Elvin elvin() {
        return (Elvin) getSource();
    }

    /**
   * The subscriptions that securely matched the notification.
   * 
   * @deprecated Since release 1.1, use {@link #secureMatches} instead.
   */
    @Deprecated
    public Set<Subscription> secureMatches() {
        return secureMatches;
    }

    /**
   * The subscriptions that insecurely matched the notification.
   * 
   * @deprecated Since release 1.1, use {@link #insecureMatches} instead.
   */
    @Deprecated
    public Set<Subscription> insecureMatches() {
        return insecureMatches;
    }

    /**
   * All subscriptions that matched the notification.
   * 
   * @deprecated Since release 1.1, use {@link #matches} instead.
   */
    @Deprecated
    public Set<Subscription> matches() {
        return matches;
    }
}
