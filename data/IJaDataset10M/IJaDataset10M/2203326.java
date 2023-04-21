package com.acv.common.model.container;

import java.io.Serializable;
import com.acv.common.model.BusObject;
import com.acv.common.model.ConnectorSessionContext;

/**
 * Interface to define some common features of <code>BusRequest</code> and <code>BusResponse</code>.
 * @author Bin Chen
 *
 */
public interface BusData<T extends BusObject> extends Serializable {

    int getMailBoxID();

    void setMailBoxID(int mailBoxID);

    T getBusObject();

    void setBusObject(T busObject);

    String getUid();

    void setUid(String uid);

    ConnectorSessionContext getConnectorSessionContext();

    void setConnectorSessionContext(ConnectorSessionContext connectorSessionContext);
}
