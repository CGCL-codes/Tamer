package org.sqlexp.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import org.sqlexp.sql.util.ISqlLoadListener;

/**
 * Abstract class for loadable objects.<br>
 * A loadable object can be "filled" with content got by server structure readers.
 * @param <Parent> class
 * @param <Child> class
 * @author Matthieu Réjou
 */
public class LoadableObject<Parent extends SqlObject<?, ?>, Child extends SqlObject<?, ?>> extends SqlObject<Parent, Child> {

    private static ArrayList<ISqlLoadListener> loadListeners = new ArrayList<ISqlLoadListener>();

    /**
	 * Adds a load listener notified when any loadable object has been loaded.
	 * @param loadListener to add
	 */
    public static void addLoadListener(final ISqlLoadListener loadListener) {
        synchronized (loadListener) {
            loadListeners.add(loadListener);
        }
    }

    /**
	 * Removes a load listener notified when any loadable object has been loaded.
	 * @param loadListener to remove
	 */
    public static void removeLoadListener(final ISqlLoadListener loadListener) {
        synchronized (loadListener) {
            loadListeners.remove(loadListener);
        }
    }

    private boolean loading;

    private boolean loaded;

    /**
	 * @param parent to add the object to
	 * @param name of the object
	 */
    public LoadableObject(final Parent parent, final String name) {
        super(parent, name);
    }

    /**
	 * Determines if an object is has been loaded.<br>
	 * <b>Synchronized</b>
	 * @return true if object content has been loaded.
	 */
    public final boolean isLoaded() {
        synchronized (this) {
            return loaded;
        }
    }

    /**
	 * Marks the object as loaded.<br>
	 * <b>Synchronized</b>
	 * @param loaded true if object content has been loaded.
	 */
    public final void setLoaded(final boolean loaded) {
        synchronized (this) {
            this.loaded = loaded;
        }
    }

    /**
	 * Determines if an object is currently loading.<br>
	 * <b>Synchronized</b>
	 * @return true if object content is currently loaded.<br>
	 * Children of a loading object children list should not be accessed.<br>
	 * <b>Synchronized</b>
	 */
    public final boolean isLoading() {
        synchronized (this) {
            return loading;
        }
    }

    /**
	 * Marks the object as loading.<br>
	 * Children of a loading object children list should not be accessed.<br>
	 * <b>Synchronized</b>
	 * @param loading the loading to set
	 */
    public final void setLoading(final boolean loading) {
        synchronized (this) {
            this.loading = loading;
        }
    }

    /**
	 * Notifies listeners the receiver has been loaded successfully.
	 */
    public final void notifyLoadCompleted() {
        ArrayList<ISqlLoadListener> notified = new ArrayList<ISqlLoadListener>();
        synchronized (loadListeners) {
            notified.addAll(loadListeners);
        }
        for (ISqlLoadListener listener : notified) {
            listener.loadCompleted(this);
        }
    }

    /**
	 * Notifies listeners the receiver load has failed.
	 * @param exception cause of the event
	 */
    public final void notifyLoadFailed(final SQLException exception) {
        ArrayList<ISqlLoadListener> notified = new ArrayList<ISqlLoadListener>();
        synchronized (loadListeners) {
            notified.addAll(loadListeners);
        }
        for (ISqlLoadListener listener : notified) {
            listener.loadFailed(this, exception);
        }
    }

    /**
	 * Notifies listeners the receiver load has been interrupted.
	 */
    public final void notifyLoadInterrupted() {
        ArrayList<ISqlLoadListener> notified = new ArrayList<ISqlLoadListener>();
        synchronized (loadListeners) {
            notified.addAll(loadListeners);
        }
        for (ISqlLoadListener listener : notified) {
            listener.loadInterrupted(this);
        }
    }
}
