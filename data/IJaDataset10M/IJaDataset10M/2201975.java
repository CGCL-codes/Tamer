package gear.location;

import gear.application.Event;
import gear.application.EventHost;
import gear.application.EventManager;
import gear.application.debug.Logger;
import gear.application.events.EnableDisableLocationProviderEvent;
import gear.application.events.LocationRequest;
import gear.application.events.args.LocationResponseArgs;
import gear.application.utils.StringHelper;

/**
 * Base class extended by all location methods classes
 * @author Paolo Burelli
 *
 */
public abstract class GearLocationProvider implements Runnable, EventHost {

    /** Thread management property */
    protected Object threadLock = new Object();

    /** Current location */
    protected GearLocation location = GearLocation.INVALID_LOCATION;

    /** Current thread */
    protected Thread mThread;

    /** Thread status property */
    protected boolean available = false;

    /** Default timeout for LocationAPI and CBS requests */
    protected static final int DEFAULT_TIMEOUT = 20;

    private Event lastRequest;

    public GearLocationProvider() {
    }

    /**
     * This method is called by the EventManager to notify the class at a new event
     * @param event Notified event
     */
    public void notify(Event event) {
        if (event.isInstanceOf(LocationRequest.class)) {
            if (((LocationRequest) event).getLocationTypeRequested() == locationTypeProvided()) {
                lastRequest = (LocationRequest) event;
                synchronized (threadLock) {
                    threadLock.notify();
                }
            }
        } else if (event.isInstanceOf(EnableDisableLocationProviderEvent.class)) {
            EnableDisableLocationProviderEvent ede = (EnableDisableLocationProviderEvent) event;
            Object target = ede.getTarget();
            if (target == this || target == null) {
                if (ede.isEnabling() && mThread == null) start(); else if (!ede.isEnabling() && mThread != null) stop();
            }
        }
    }

    /**
	 * This method is used to start the Location thread, alternatively
	 * it is possible to send a ENABLEDISABLE using the event manager  
	 */
    public void start() {
        onStart();
        stop();
        available = true;
        mThread = new Thread(this, StringHelper.getClassName(this.getClass()));
        mThread.start();
    }

    /**
	 * This method is used to stop the Location thread, alternatively
	 * it is possible to send a ENABLEDISABLE using the event manager  
	 */
    public void stop() {
        onStop();
        if (mThread != null) {
            mThread = null;
            available = false;
        }
    }

    /**
     * This method puts the thread on a wait status
     */
    protected void pause() {
        synchronized (threadLock) {
            try {
                available = true;
                threadLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        available = false;
    }

    /**
     * This is the main thread cycle
     */
    public void run() {
        try {
            while (mThread != null) {
                pause();
                location = getLocation();
                EventManager.getInstance().enqueueEvent(lastRequest.positiveResponse(this, new LocationResponseArgs(location)));
            }
        } catch (Exception e) {
            Logger.out.println(e, this);
        }
    }

    /**
	 * This method returns whether the thread is busy or not
	 * @return true if the tread is not busy
	 */
    public boolean isAvailable() {
        return available;
    }

    /**
	 * This method should execute all the necessary actions at the thread start
	 */
    protected abstract void onStart();

    /**
     * This method should execute all the necessary actions at the thread stop
     */
    protected abstract void onStop();

    /**
     * This method should return the specific type of Location provided
     * @return a GearLocation.Type constant identifying the location type
     */
    public abstract int locationTypeProvided();

    /**
     * This method should return the current location
     * @return current location
     */
    public abstract GearLocation getLocation();
}
