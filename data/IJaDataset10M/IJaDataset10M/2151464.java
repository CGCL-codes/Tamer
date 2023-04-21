package visad.data.in;

import java.rmi.RemoteException;
import visad.*;

/**
 * Interface for a filter-module in a data-import pipe.  In general, such
 * a filter-module obtains VisAD data objects its upstream data source and
 * transforms them in some way before passing them on.
 *
 * @author Steven R. Emmerson
 */
public interface DataInputStream {

    /**
     * Returns the next VisAD data object in the input stream. Returns 
     * <code>null</code> if there is no next object.
     *
     * @return			A VisAD data object or <code>null</code> if 
     *				there are no more such objects.
     * @throws VisADException	VisAD failure.
     * @throws RemoteException	Java RMI failure.
     */
    DataImpl readData() throws VisADException, RemoteException;
}
