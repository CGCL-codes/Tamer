package org.gudy.azureus2.core3.util;

/**
 * A Runnable that calls the AECallback function when it's done running the 
 * code the implementer supplied
 * 
 * @author TuxPaper
 * @created Mar 22, 2007
 *
 */
public abstract class AERunnableWithCallback implements Runnable {

    private final AECallback callback;

    public AERunnableWithCallback(AECallback callback) {
        this.callback = callback;
    }

    public final void run() {
        try {
            Object o = runSupport();
            if (callback != null) {
                callback.callbackSuccess(o);
            }
        } catch (Throwable e) {
            Debug.out(e);
            if (callback != null) {
                callback.callbackFailure(e);
            }
        }
    }

    public abstract Object runSupport();
}
