package net.jwde.database.object;

import net.jwde.database.object.GenericDAO;
import net.jwde.database.DatabaseException;
import net.jwde.object.*;
import java.util.*;

public interface SupplierShippingFeeDAO extends GenericDAO<SupplierShippingFee> {

    public Object insertSupplierShippingFee(SupplierShippingFee supShipFee) throws DatabaseException;

    public boolean insertSupplierShippingFees(Collection<SupplierShippingFee> supShipFees) throws DatabaseException;

    public SupplierShippingFee getSupplierShippingFee(long supplierID) throws DatabaseException;

    public SupplierShippingFee findSupplierShippingFeeByName(String name) throws DatabaseException;

    public List<SupplierShippingFee> findAll() throws DatabaseException;

    public boolean updateSupplierShippingFee(SupplierShippingFee supShipFee) throws DatabaseException;

    public boolean deleteSupplierShippingFee(long supplierID) throws DatabaseException;

    public boolean deleteSupplierShippingFees(Collection<SupplierShippingFee> supShipFees) throws DatabaseException;
}
