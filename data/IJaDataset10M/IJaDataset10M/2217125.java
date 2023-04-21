package de.ios.kontor.sv.order.impl;

import java.rmi.*;
import java.util.*;
import de.ios.framework.basic.*;
import de.ios.framework.db2.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.order.co.*;
import de.ios.kontor.sv.main.co.*;
import de.ios.kontor.sv.main.impl.*;
import de.ios.kontor.sv.address.co.*;
import de.ios.kontor.sv.basic.impl.*;
import de.ios.kontor.sv.basic.co.*;

/**
 * OrderRateLineEntryControllerImpl deals with a set of OrderRateLineEntries
 * within the kontor framework.
 */
public abstract class OrderRateLineEntryControllerImpl extends OrderLineEntryControllerImpl implements OrderRateLineEntryController {

    /**
   * Constructor.
   * @param _db Connection to the database.
   * @param _f A DBFactory for creating instances of OrderRateLineEntires.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception FactoryException if determining the Class of the DBOs created by the Factory failed.
   */
    public OrderRateLineEntryControllerImpl(DBObjectServer _db, BasicFactoryImpl _f) throws java.rmi.RemoteException, FactoryException {
        super(_db, _f);
    }

    /**
   * Get all OrderRateLineEntries to an Order ordered by LineNumbers.
   * @param o the Order for which to get all LineEntries.
   */
    public Iterator getAllRateLineEntries(Order o) throws KontorException {
        try {
            if (getKind() == null) return super.getAllRateLineEntries(o); else {
                long ooid = (o == null) ? -1 : ((OrderDBO) o.getDBObject()).getOId();
                OrderLineEntryImpl le = (OrderLineEntryImpl) createImpl();
                if (ooid > 0) ((OrderLineEntryDBO) le.getDBObject()).orderOId.set(ooid);
                return getOrderLineEntrys(le);
            }
        } catch (Throwable t) {
            throw new KontorException("Loading of the OrderLineEntries failed!", t);
        }
    }

    /**
   * Get all orderable OrderLineEntry-Description/Kinds.
   * @param c Customer for which to return all orderable LineEntries
   * @returns a Hashtable with the Kinds(Objects) and Short-Descriptions(Keys) of all orderable LineEntries
   * @exception de.ios.kontor.utils.KontorException if the merging of the LineEntry-Lists failed.
   */
    public Hashtable getAllOrderableLineEntries(Customer c) throws KontorException {
        try {
            Iterator i = getRateHeadControllerImpl().getAllOrderable(c);
            Hashtable h = new Hashtable();
            while (i.next()) h.put((String) i.getObject(), getKind());
            return h;
        } catch (Throwable t) {
            throw new KontorException("Loading of the Orderable-OrderRateLineEntry-Descriptions failed!", t);
        }
    }

    /**
   * Get all orderable OrderRateLineEntry-Descriptions/Kinds.
   * @param c Customer for which to return all orderable LineEntries
   * @returns a Hashtable with the Kinds(Objects) and Short-Descriptions(Keys) of all orderable RateLineEntries.
   */
    public Hashtable getAllOrderableRateLineEntries(Customer c) throws KontorException {
        return getAllOrderableLineEntries(c);
    }

    /**
   * @return the RateHead-ControllerImpl matching to this RateLineEntry.
   * @exception de.ios.kontor.utils.KontorException if the loading of RateHeadControllerImpl failed.
   */
    public abstract RateHeadControllerImpl getRateHeadControllerImpl() throws KontorException;

    /**
   * @return the RateHead-Controller matching to this RateLineEntry.
   * @exception de.ios.kontor.utils.KontorException if the loading of RateHeadController failed.
   */
    public RateHeadController getRateHeadController() throws KontorException {
        return getRateHeadControllerImpl();
    }
}

;
