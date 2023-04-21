package ch.nostromo.lib.util;

import java.util.EventListener;

public abstract interface NosThreadListener extends EventListener {

    public abstract void threadFinished();

    public abstract void threadFailed(Throwable t);
}
