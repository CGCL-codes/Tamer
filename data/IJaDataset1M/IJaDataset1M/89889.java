package owagate.util;

import java.util.ArrayList;
import java.util.List;

/**
 This class manages a collection of RunningObject instances. As objects are
 added, they are started given a NotificationSink that allows this object to
 cleanup.
 */
public class RunningObjectManager implements Destroyable {

    public static interface NotificationSink {

        public void onObjectEnd(RunningObject object);
    }

    /** Constructs a new instance of RunningObjectManager. */
    public RunningObjectManager(NotificationSink sink) {
        mSink = sink;
    }

    /** Constructs a new instance of RunningObjectManager. */
    public RunningObjectManager() {
        this(null);
    }

    /**
     Adds a new RunningObject to this manager's collection of objects. The first
     object adding starts this object's Reaper Thread.
     */
    public void add(RunningObject object) {
        synchronized (this) {
            if (mActive == null) throw new IllegalStateException("Object has been destroyed");
            mActive.add(object);
            if (mReaper == null) {
                mReaper = new Thread(new Runnable() {

                    public void run() {
                        reaper();
                    }
                });
                mReaper.start();
            }
        }
        boolean ok = false;
        try {
            object.start(new RunningObject.NotificationSink() {

                public void onEnd(RunningObject object) {
                    cleanup(object);
                }
            });
            ok = true;
        } finally {
            if (!ok) cleanup(object);
        }
    }

    /** Destroys this object, stopping the Reaper Thread. */
    public void destroy() {
        List<RunningObject> active = null;
        synchronized (this) {
            active = mActive;
            mActive = null;
            if (mReaper != null) mReaper.interrupt();
        }
        stopAll(active, null);
        if (mReaper != null) while (mReaper.isAlive()) try {
            mReaper.join();
        } catch (InterruptedException ex) {
        }
    }

    /** Returns true if there are no active RunningObjects. */
    public synchronized boolean isEmpty() {
        if (mActive == null) return true;
        return mActive.isEmpty();
    }

    /**
     This method returns the list of currently dead objects. The list is now
     only useful to the caller.
     */
    private synchronized List<RunningObject> bringOutYourDead() {
        try {
            while (mDead == null && !mReaper.isInterrupted()) wait();
        } catch (InterruptedException ex) {
            return null;
        }
        List<RunningObject> dead = mDead;
        mDead = null;
        return dead;
    }

    /**
     Moves the given object from the active collection to the dead list. If the
     dead list does not exist, one is created.
     */
    private synchronized void cleanup(RunningObject object) {
        for (int i = mActive.size(); i-- > 0; ) if (mActive.get(i) == object) {
            mActive.remove(i);
            if (mDead == null) mDead = new ArrayList<RunningObject>();
            mDead.add(object);
            notify();
            break;
        }
    }

    /**
     This is the Reaper Thread's main loop method.
     */
    private void reaper() {
        List<RunningObject> dead;
        while ((dead = bringOutYourDead()) != null) stopAll(dead, mSink);
    }

    /**
     Stops all RunningObjects in the given collection, calling a Sink as needed.
     */
    private static void stopAll(List<RunningObject> objects, NotificationSink sink) {
        if (objects != null) for (int i = objects.size(); i-- > 0; ) {
            RunningObject object = objects.get(i);
            if (sink != null) sink.onObjectEnd(object);
            object.destroy();
        }
    }

    private List<RunningObject> mActive = new ArrayList<RunningObject>();

    private List<RunningObject> mDead = null;

    private Thread mReaper = null;

    private NotificationSink mSink;
}
