package com.sun.jndi.ldap;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.EventObject;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.event.*;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.UnsolicitedNotificationListener;
import javax.naming.ldap.UnsolicitedNotificationEvent;
import javax.naming.ldap.UnsolicitedNotification;

/**
 * This is a utility class that can be used by a context that supports
 * event notification.  You can use an instance of this class as a member field
 * of your context and delegate various work to it.
 * It is currently structured so that each context should have its own
 * EventSupport (instead of static version shared by all contexts
 * of a service provider).
 *<p>
 * This class supports two types of listeners: those that register for
 * NamingEvents, and those for UnsolicitedNotificationEvents (they can be mixed
 * into the same listener).
 * For NamingEvent listeners, it maintains a hashtable that maps
 * registration requests--the key--to
 * <em>notifiers</em>--the value. Each registration request consists of:
 *<ul>
 *<li>The name argument of the registration.
 *<li>The filter (default is "(objectclass=*)").
 *<li>The search controls (default is null SearchControls).
 *<li>The events that the listener is interested in. This is determined by
 * finding out which <tt>NamingListener</tt> interface the listener supports.
 *</ul>
 *<p>
 *A notifier (<tt>NamingEventNotifier</tt>) is a worker thread that is responsible
 *for gathering information for generating events requested by its listeners.
 *Each notifier maintains its own list of listeners; these listeners have
 *all made the same registration request (at different times) and implements
 *the same <tt>NamingListener</tt> interfaces.
 *<p>
 *For unsolicited listeners, this class maintains a vector, unsolicited.
 *When an unsolicited listener is registered, this class adds itself
 *to the context's LdapClient. When LdapClient receives an unsolicited
 *notification, it notifies this EventSupport to fire an event to the
 *the listeners. Special handling in LdapClient is done for the DISCONNECT
 *notification. [It results in the EventSupport firing also a
 *NamingExceptionEvent to the unsolicited listeners.]
 *<p>
 *
 *When a context no longer needs this EventSupport, it should invoke
 *cleanup() on it.
 *<p>
 *<h4>Registration</h4>
 *When a registration request is made, this class attempts to find an
 *existing notifier that's already working on the request. If one is
 *found, the listener is added to the notifier's list. If one is not found,
 *a new notifier is created for the listener.
 *
 *<h4>Deregistration</h4>
 *When a deregistration request is made, this class attemps to find its
 *corresponding notifier. If the notifier is found, the listener is removed
 *from the notifier's list. If the listener is the last listener on the list,
 *the notifier's thread is terminated and removed from this class's hashtable.
 *Nothing happens if the notifier is not found.
 *
 *<h4>Event Dispatching</h4>
 *The notifiers are responsible for gather information for generating events
 *requested by their respective listeners. When a notifier gets sufficient
 *information to generate an event, it creates invokes the
 *appropriate <tt>fireXXXEvent</tt> on this class with the information and list of
 *listeners. This causes an event and the list of listeners to be added
 *to the <em>event queue</em>.
 *This class maintains an event queue and a dispatching thread that dequeues
 *events from the queue and dispatches them to the listeners.
 *
 *<h4>Synchronization</h4>
 *This class is used by the main thread (LdapCtx) to add/remove listeners.
 *It is also used asynchronously by NamingEventNotifiers threads and
 *the context's Connection thread. It is used by the notifier threads to
 *queue events and to update the notifiers list when the notifiers exit.
 *It is used by the Connection thread to fire unsolicited notifications.
 *Methods that access/update the 'unsolicited' and 'notifiers' lists are
 *thread-safe.
 *
 * @author Rosanna Lee
 */
final class EventSupport {

    private static final boolean debug = false;

    private LdapCtx ctx;

    /**
     * NamingEventNotifiers; hashed by search arguments;
     */
    private Hashtable notifiers = new Hashtable(11);

    /**
     * List of unsolicited notification listeners.
     */
    private Vector unsolicited = null;

    /**
     * Constructs EventSupport for ctx.
     * <em>Do we need to record the name of the target context?
     * Or can we assume that EventSupport is called on a resolved
     * context? Do we need other add/remove-NamingListener methods?
     * package private;
     */
    EventSupport(LdapCtx ctx) {
        this.ctx = ctx;
    }

    synchronized void addNamingListener(String nm, int scope, NamingListener l) throws NamingException {
        if (l instanceof ObjectChangeListener || l instanceof NamespaceChangeListener) {
            NotifierArgs args = new NotifierArgs(nm, scope, l);
            NamingEventNotifier notifier = (NamingEventNotifier) notifiers.get(args);
            if (notifier == null) {
                notifier = new NamingEventNotifier(this, ctx, args, l);
                notifiers.put(args, notifier);
            } else {
                notifier.addNamingListener(l);
            }
        }
        if (l instanceof UnsolicitedNotificationListener) {
            if (unsolicited == null) {
                unsolicited = new Vector(3);
            }
            unsolicited.addElement(l);
        }
    }

    /**
     * Adds <tt>l</tt> to list of listeners interested in <tt>nm</tt>
     * and filter.
     */
    synchronized void addNamingListener(String nm, String filter, SearchControls ctls, NamingListener l) throws NamingException {
        if (l instanceof ObjectChangeListener || l instanceof NamespaceChangeListener) {
            NotifierArgs args = new NotifierArgs(nm, filter, ctls, l);
            NamingEventNotifier notifier = (NamingEventNotifier) notifiers.get(args);
            if (notifier == null) {
                notifier = new NamingEventNotifier(this, ctx, args, l);
                notifiers.put(args, notifier);
            } else {
                notifier.addNamingListener(l);
            }
        }
        if (l instanceof UnsolicitedNotificationListener) {
            if (unsolicited == null) {
                unsolicited = new Vector(3);
            }
            unsolicited.addElement(l);
        }
    }

    /**
     * Removes <tt>l</tt> from all notifiers in this context.
     */
    synchronized void removeNamingListener(NamingListener l) {
        Enumeration allnotifiers = notifiers.elements();
        NamingEventNotifier notifier;
        if (debug) System.err.println("EventSupport removing listener");
        while (allnotifiers.hasMoreElements()) {
            notifier = (NamingEventNotifier) allnotifiers.nextElement();
            if (notifier != null) {
                if (debug) System.err.println("EventSupport removing listener from notifier");
                notifier.removeNamingListener(l);
                if (!notifier.hasNamingListeners()) {
                    if (debug) System.err.println("EventSupport stopping notifier");
                    notifier.stop();
                    notifiers.remove(notifier.info);
                }
            }
        }
        if (debug) System.err.println("EventSupport removing unsolicited: " + unsolicited);
        if (unsolicited != null) {
            unsolicited.removeElement(l);
        }
    }

    synchronized boolean hasUnsolicited() {
        return (unsolicited != null && unsolicited.size() > 0);
    }

    /**
      * package private;
      * Called by NamingEventNotifier to remove itself when it encounters
      * a NamingException.
      */
    synchronized void removeDeadNotifier(NotifierArgs info) {
        if (debug) {
            System.err.println("EventSupport.removeDeadNotifier: " + info.name);
        }
        notifiers.remove(info);
    }

    /**
     * Fire an event to unsolicited listeners.
     * package private;
     * Called by LdapCtx when its clnt receives an unsolicited notification.
     */
    synchronized void fireUnsolicited(Object obj) {
        if (debug) {
            System.err.println("EventSupport.fireUnsolicited: " + obj + " " + unsolicited);
        }
        if (unsolicited == null || unsolicited.size() == 0) {
            return;
        }
        if (obj instanceof UnsolicitedNotification) {
            UnsolicitedNotificationEvent evt = new UnsolicitedNotificationEvent(ctx, (UnsolicitedNotification) obj);
            queueEvent(evt, unsolicited);
        } else if (obj instanceof NamingException) {
            NamingExceptionEvent evt = new NamingExceptionEvent(ctx, (NamingException) obj);
            queueEvent(evt, unsolicited);
            unsolicited = null;
        }
    }

    /**
     * Stops notifier threads that are collecting event data and
     * stops the event queue from dispatching events.
     * Package private; used by LdapCtx.
     */
    synchronized void cleanup() {
        if (debug) System.err.println("EventSupport clean up");
        if (notifiers != null) {
            for (Enumeration ns = notifiers.elements(); ns.hasMoreElements(); ) {
                ((NamingEventNotifier) ns.nextElement()).stop();
            }
            notifiers = null;
        }
        if (eventQueue != null) {
            eventQueue.stop();
            eventQueue = null;
        }
    }

    private EventQueue eventQueue;

    /**
     * Add the event and vector of listeners to the queue to be delivered.
     * An event dispatcher thread dequeues events from the queue and dispatches
     * them to the registered listeners.
     * Package private; used by NamingEventNotifier to fire events
     */
    synchronized void queueEvent(EventObject event, Vector vector) {
        if (eventQueue == null) eventQueue = new EventQueue();
        Vector v = (Vector) vector.clone();
        eventQueue.enqueue(event, v);
    }
}
