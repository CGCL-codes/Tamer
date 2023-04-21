package edu.mit.wi.omnigene.dbsieve.ejb;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import edu.mit.wi.omnigene.util.*;
import edu.mit.wi.omnigene.dbsieve.*;

public interface DBDimensionFilter extends EJBObject {

    /**
     * Function to expose available dimension, through which user is allowed to query.
     * passes back <CODE>DimensionDTO</CODE> array
     * @throws OmnigeneException
     * @throws RemoteException
     * @return <CODE>DimensionDTO</CODE>
     */
    public DimensionDTO[] getDimension() throws OmnigeneException, RemoteException;

    /**
     * Pass back results based on filter condition and
     * what dimension values asked
     * @param dimensionFilter
     * @throws RemoteException
     * @return <CODE>DimensionDTO</CODE>
     */
    public DimensionDTO[] getFilterResult(DimensionFilter dimensionFilter) throws OmnigeneException, RemoteException;
}
