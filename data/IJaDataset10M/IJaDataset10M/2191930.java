package org.datashare.client;

import org.datashare.objects.DataShareObject;

public interface ClientDataReceiverInterface {

    /**
    * this method will be called when data is received
    */
    public void dataReceived(DataShareObject dso);

    /**
    * this method is called when the data connection has been lost,
    * this will be used to notify anybody that cares...
    */
    public void connectionLost(DataShareConnection dsc);
}
