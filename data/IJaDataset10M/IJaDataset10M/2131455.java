package org.openscada.opc.dcom.ae;

import org.jinterop.dcom.core.JIArray;

/**
 *
 */
public interface IOPCEventCallback {

    public Object[] onEvent(int hClientSubscription, int bRefresh, int bLastRefresh, int dwCount, JIArray events);
}
