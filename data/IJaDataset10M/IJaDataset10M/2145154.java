package org.datashare;

import org.datashare.DataShareServer;

/**
 * this interface is to be implemented by the class that provides the ServiceInterface,
 * functionality of this class enables DataShare to run as an NT service if implemented.
 * Other Operating System should be able to be supported also.
 * @date July 25, 2001
 * @author Charles Wood
 * @version 1.0
 */
public interface ServiceInterface {

    void initialize(DataShareServer dataShareServer);
}
