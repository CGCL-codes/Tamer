package de.matthiasmann.twl.model;

import de.matthiasmann.twl.utils.CallbackSupport;

/**
 * A base class for option boolean model.
 *
 * This class handles the callback filtering from the source model.
 * The callback on the source model is installed when the first
 * callback on this model has been added, and is removed again
 * when the last callback has been removed. This allows instances
 * of {@code AbstractOptionModel} to be GCed when no longer needed.
 * 
 * Without this dynamic subscription the callback on the source model
 * would form a cycle of strong references between the
 * {@code AbstractOptionModel} instance and it's source model which
 * would prevent all instances from beeing GCed until the source model
 * is also GCed.
 *
 * @author Matthias Mann
 */
public abstract class AbstractOptionModel implements BooleanModel {

    Runnable[] callbacks;

    Runnable srcCallback;

    public void addCallback(Runnable callback) {
        if (callback == null) {
            throw new NullPointerException("callback");
        }
        if (callbacks == null) {
            srcCallback = new Runnable() {

                boolean lastValue = getValue();

                public void run() {
                    boolean value = getValue();
                    if (lastValue != value) {
                        lastValue = value;
                        CallbackSupport.fireCallbacks(callbacks);
                    }
                }
            };
            callbacks = new Runnable[] { callback };
            installSrcCallback(srcCallback);
        } else {
            callbacks = CallbackSupport.addCallbackToList(callbacks, callback, Runnable.class);
        }
    }

    public void removeCallback(Runnable callback) {
        callbacks = CallbackSupport.removeCallbackFromList(callbacks, callback);
        if (callbacks == null && srcCallback != null) {
            removeSrcCallback(srcCallback);
            srcCallback = null;
        }
    }

    protected abstract void installSrcCallback(Runnable cb);

    protected abstract void removeSrcCallback(Runnable cb);
}
