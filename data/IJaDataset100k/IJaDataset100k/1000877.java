package ucar.netcdf;

import ucar.multiarray.RemoteAccessor;
import java.rmi.RemoteException;
import java.rmi.Remote;

/**
 * This interface wraps a single instance of Netcdf to
 * provide Remote services required in the construction
 * of an instance of RemoteNetcdf.
 * <p>
 * This interface is only needed by directory services like NetcdfService
 * to bootstrap instances of RemoteNetcdf.
 * It could be considered package or implementation private.
 *
 * @author $Author: rboller_cvs $
 * @version $Revision: 1.1 $ $Date: 2009/04/17 18:05:36 $
 */
public interface NetcdfRemoteProxy extends Remote {

    /**
	 * @return a Schema for the Netcdf this
	 * represents.
	 */
    public Schema getSchema() throws RemoteException;

    /**
	 * Get an Accessor for a Variable, by name.
	 * Given the Accessor and the ProtoVariable
	 * obtained indirectly from getSchema() above,
	 * RemoteNetcdf can create a remote proxy for the Variable.
	 * @param varName String which names a Variable in the
	 * Netcdf this represents.
	 * @return a (Remote)Accessor for the Variable.
	 */
    public RemoteAccessor getAccessor(String varName) throws RemoteException;

    /**
	 * Indicate that you are done with this
	 * Netcdf data set. Allows the service to free
	 * resources (close the data set).
	 */
    public void release() throws RemoteException;
}
