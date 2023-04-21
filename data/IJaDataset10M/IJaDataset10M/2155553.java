package org.objectstyle.cayenne.remote.service;

import org.objectstyle.cayenne.CayenneRuntimeException;
import org.objectstyle.cayenne.DataChannel;
import org.objectstyle.cayenne.remote.BootstrapMessage;
import org.objectstyle.cayenne.remote.ClientMessage;
import org.objectstyle.cayenne.remote.QueryMessage;
import org.objectstyle.cayenne.remote.SyncMessage;

/**
 * A helper class to match message types with DataChannel methods.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
class DispatchHelper {

    static Object dispatch(DataChannel channel, ClientMessage message) {
        if (message instanceof QueryMessage) {
            return channel.onQuery(null, ((QueryMessage) message).getQuery());
        } else if (message instanceof SyncMessage) {
            SyncMessage sync = (SyncMessage) message;
            return channel.onSync(null, sync.getSenderChanges(), sync.getType());
        } else if (message instanceof BootstrapMessage) {
            return channel.getEntityResolver().getClientEntityResolver();
        } else {
            throw new CayenneRuntimeException("Message dispatch error. Unsupported message: " + message);
        }
    }
}
