package de.ios.kontor.sv.order.co;

import java.rmi.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.basic.co.*;

/**
 * Interface specifies Methods for accessing Data of some kind of a FreeLineEntry-Implementation-Instance.
 * @see FreeLineEntryImpl
 * @see OrderFreeLineEntry
 * @see InvoiceFreeLineEntry
 * @see LineEntry
 * @see RateLineEntry
 */
public interface FreeLineEntry extends LineEntry {

    /**
   * The createdFor field can be used for a objectid reference to a unspecified table. The table is
   * specified with a prefix, e.g. "DN:12345"  to reference _D_elivery_N_ote with objectid 12345.
   *
   * @return The createdFor field for this FreeLineEntry. 
   * @exception RemoteException thrown if connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   */
    public String getCreatedFor() throws RemoteException, KontorException;
}
