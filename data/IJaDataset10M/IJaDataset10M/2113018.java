package com.aelitis.azureus.core.dht.db;

import com.aelitis.azureus.core.dht.transport.*;

/**
 * @author parg
 *
 */
public interface DHTDBValue extends DHTTransportValue {

    public void setFlags(byte flags);

    public DHTDBValue getValueForRelay(DHTTransportContact new_originator);
}
