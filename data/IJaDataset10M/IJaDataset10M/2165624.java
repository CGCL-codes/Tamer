package org.ws4d.osgi.eventConverter.eventing;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.osgi.service.log.LogService;
import org.ws4d.java.communication.DPWSException;
import org.ws4d.java.eventing.EventManager;
import org.ws4d.java.eventing.EventingUtil;
import org.ws4d.java.service.AbstractAction;
import org.ws4d.java.service.remote.IRemoteService;
import org.ws4d.java.service.remote.RemoteDevice;
import org.ws4d.java.service.remote.RemoteFactory;
import org.ws4d.java.xml.QualifiedName;
import org.ws4d.osgi.constants.GlobalConstants;
import org.ws4d.osgi.eventConverter.DPWSClient;
import org.ws4d.osgi.eventConverter.logging.LogServiceTracker;
import org.ws4d.osgi.eventConverter.util.Constants;
import org.ws4d.osgi.eventConverter.util.DeviceQueueElement;
import org.ws4d.osgi.eventConverter.util.EventConverterProperties;
import org.ws4d.osgi.eventConverter.util.Queue;

/**
 * Manages the handling of all available EventConverters
 * 
 * On received <code>wsa:Hello</code> and <code>wsa:Bye</code> Messages new
 * EventConverterServices will be subscribed / removed.
 */
public class EventServiceManager {

    private DeviceQueueThread devThread;

    private static EventServiceManager thisInstance;

    private EventingUtil eventingUtil;

    private Hashtable duuid2subsriptionID = new Hashtable();

    private Hashtable duuid2subsriptionID_secure = new Hashtable();

    private Queue devicesQueue;

    private boolean running = false;

    /**
	 * The standard constructor.
	 * 
	 * Creates a new {@link DeviceQueueThread} that monitors the Device-{@link Queue}
	 * for new events
	 */
    private EventServiceManager() {
        devicesQueue = new Queue();
        devThread = new DeviceQueueThread();
        devThread.start();
    }

    /**
	 * Singleton Class
	 * 
	 * @return The instance of {@link EventServiceManager}
	 */
    public static EventServiceManager getInstance() {
        if (thisInstance == null) thisInstance = new EventServiceManager();
        return thisInstance;
    }

    public static void shutdown() {
        if (thisInstance != null) thisInstance.stop();
        thisInstance = null;
    }

    /**
	 * Handles DPWS EventConverter Devices.
	 * 
	 * @param rd
	 *            The {@code RemoteDevice} that has sent a
	 *            <code>wsa:Hello</code> or has been found.
	 */
    public void addEventConverter(RemoteDevice rd) {
        RemoteFactory.fillServices(rd);
        String duuid = rd.getDeviceUUID().toString();
        IRemoteService eventService = rd.getRemoteService(new QualifiedName(Constants.PORTTYPE_EVENTSERVICE, GlobalConstants.NAMESPACE_OSAMI));
        if (eventService == null) {
            removeEventConverter(duuid);
            removeSecureEventConverter(duuid);
            return;
        } else {
            Vector remoteUUIDs = (Vector) EventConverterProperties.getInstance().getProperty(GlobalConstants.EventConverterProperties.PROP_REMOTE_DPWS_EVENTSERVICES);
            if (remoteUUIDs.contains(duuid) && !eventService.getEndpointLocation().getSchema().equals("https")) {
                if (duuid2subsriptionID.containsKey(duuid)) {
                    if (GlobalConstants.GLOBAL_LOGGING) LogServiceTracker.log.log(LogService.LOG_DEBUG, "EventConverter already subscribed");
                } else {
                    EventManager.initEventing();
                    eventingUtil = new EventingUtil(DPWSClient.getInstance());
                    AbstractAction osgiEvent = eventService.getAction(Constants.ACTION_NAME_EVENT, new QualifiedName(Constants.PORTTYPE_EVENTSERVICE, GlobalConstants.NAMESPACE_OSAMI));
                    if (GlobalConstants.GLOBAL_LOGGING) LogServiceTracker.log.log(LogService.LOG_DEBUG, "Subscribing EventService");
                    String subscriptionID = eventingUtil.subscribe(osgiEvent, 0, true);
                    duuid2subsriptionID.put(duuid, subscriptionID);
                }
            } else if (duuid2subsriptionID.containsKey(duuid)) {
                removeEventConverter(duuid);
            }
        }
        Vector secureRemoteUUIDs = (Vector) EventConverterProperties.getInstance().getProperty(GlobalConstants.EventConverterProperties.PROP_REMOTE_DPWS_EVENTSERVICES_SECURE);
        if (secureRemoteUUIDs.contains(duuid) && eventService.getEndpointLocation().getSchema().equals("https")) {
            if (duuid2subsriptionID_secure.containsKey(duuid)) {
                LogServiceTracker.log.log(LogService.LOG_DEBUG, "EventConverter already subscribed");
            } else {
                EventManager.initEventing();
                eventingUtil = new EventingUtil(DPWSClient.getInstance());
                AbstractAction osgiEvent = eventService.getAction(Constants.ACTION_NAME_EVENT, new QualifiedName(Constants.PORTTYPE_EVENTSERVICE, GlobalConstants.NAMESPACE_OSAMI));
                if (GlobalConstants.GLOBAL_LOGGING) LogServiceTracker.log.log(LogService.LOG_DEBUG, "Subscribing secure EventService");
                String subscriptionID = eventingUtil.subscribe(osgiEvent, 0, true);
                duuid2subsriptionID_secure.put(duuid, subscriptionID);
            }
        } else if (duuid2subsriptionID_secure.containsKey(duuid)) {
            removeSecureEventConverter(duuid);
        }
    }

    /**
	 * Removes subscriptions for remote EventConverterServices, that went
	 * offline
	 * 
	 * @param duuid
	 *            The DeviceUUID
	 */
    public void removeEventConverter(String duuid) {
        if (duuid2subsriptionID.containsKey(duuid)) {
            if (GlobalConstants.GLOBAL_LOGGING) LogServiceTracker.log.log(LogService.LOG_DEBUG, "Removing EventConverter: " + duuid);
            String subscriptionID = (String) duuid2subsriptionID.remove(duuid);
            eventingUtil.removeSubscription(subscriptionID);
        }
    }

    /**
	 * Removes subscriptions for remote secure EventConverterServices, that went
	 * offline
	 * 
	 * @param duuid
	 *            The DeviceUUID
	 */
    public void removeSecureEventConverter(String duuid) {
        if (duuid2subsriptionID_secure.containsKey(duuid)) {
            if (GlobalConstants.GLOBAL_LOGGING) LogServiceTracker.log.log(LogService.LOG_DEBUG, "Removing secure EventConverter: " + duuid);
            String subscriptionID = (String) duuid2subsriptionID_secure.remove(duuid);
            eventingUtil.removeSubscription(subscriptionID);
        }
    }

    /**
	 * Shutting down the {@link EventServiceManager}.
	 * 
	 * Unsubscribes all subscribed EventConverters.
	 * 
	 */
    private void stop() {
        if (GlobalConstants.GLOBAL_LOGGING) LogServiceTracker.log.log(LogService.LOG_DEBUG, "shutting down...");
        if (eventingUtil != null && !(duuid2subsriptionID.isEmpty()) || duuid2subsriptionID_secure.isEmpty()) {
            if (GlobalConstants.GLOBAL_LOGGING) LogServiceTracker.log.log(LogService.LOG_DEBUG, "Unsubscribing EventConverter");
            for (Enumeration enumeration = duuid2subsriptionID.elements(); enumeration.hasMoreElements(); ) {
                String subscriptionID = (String) enumeration.nextElement();
                try {
                    eventingUtil.unsubscribe(subscriptionID);
                } catch (DPWSException e) {
                    eventingUtil.removeSubscription(subscriptionID);
                }
            }
            for (Enumeration enumeration = duuid2subsriptionID_secure.elements(); enumeration.hasMoreElements(); ) {
                String subscriptionID = (String) enumeration.nextElement();
                try {
                    eventingUtil.unsubscribe(subscriptionID);
                } catch (DPWSException e) {
                    eventingUtil.removeSubscription(subscriptionID);
                }
            }
        }
        running = false;
        devicesQueue = null;
    }

    private class DeviceQueueThread extends Thread {

        public void run() {
            running = true;
            while (running) {
                handleLastQueueElement();
            }
        }
    }

    private void handleLastQueueElement() {
        DeviceQueueElement queueElement = (DeviceQueueElement) devicesQueue.dequeue();
        if (queueElement.getEvent() == DeviceQueueElement.EVENT_HELLO) {
            addEventConverter(queueElement.getRd());
        } else if (queueElement.getEvent() == DeviceQueueElement.EVENT_BYE) {
            removeEventConverter(queueElement.getDeviceUUID());
            removeSecureEventConverter(queueElement.getDeviceUUID());
        }
    }

    /**
	 * Returns the queue of devices, that need to be handled because of a status
	 * change
	 * 
	 * @return {@link Queue}
	 */
    public Queue getDevicesQueue() {
        return devicesQueue;
    }

    public void checkSubscribedServices() {
        Vector remoteUUIDs = ((Vector) EventConverterProperties.getInstance().getProperty(GlobalConstants.EventConverterProperties.PROP_REMOTE_DPWS_EVENTSERVICES));
        for (Enumeration e = duuid2subsriptionID.keys(); e.hasMoreElements(); ) {
            String duuid = (String) e.nextElement();
            if (!remoteUUIDs.contains(duuid)) {
                String subscriptionID = (String) duuid2subsriptionID.remove(duuid);
                if (GlobalConstants.GLOBAL_LOGGING) LogServiceTracker.log.log(LogService.LOG_DEBUG, "Unsubscribing " + duuid);
                try {
                    eventingUtil.unsubscribe(subscriptionID);
                } catch (DPWSException e1) {
                    eventingUtil.removeSubscription(subscriptionID);
                }
            }
        }
        Vector secureRemoteUUIDs = ((Vector) EventConverterProperties.getInstance().getProperty(GlobalConstants.EventConverterProperties.PROP_REMOTE_DPWS_EVENTSERVICES_SECURE));
        for (Enumeration e = duuid2subsriptionID_secure.keys(); e.hasMoreElements(); ) {
            String duuid = (String) e.nextElement();
            if (!secureRemoteUUIDs.contains(duuid)) {
                String subscriptionID = (String) duuid2subsriptionID_secure.remove(duuid);
                if (GlobalConstants.GLOBAL_LOGGING) LogServiceTracker.log.log(LogService.LOG_DEBUG, "Unsubscribing " + duuid);
                try {
                    eventingUtil.unsubscribe(subscriptionID);
                } catch (DPWSException e1) {
                    eventingUtil.removeSubscription(subscriptionID);
                }
            }
        }
    }
}
