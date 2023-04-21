package com.javaeye.procurement.dao;

import java.util.List;
import com.javaeye.common.dao.PageDAO;
import com.javaeye.procurement.dto.Supplier;

public interface SupplierDAO extends PageDAO {

    public Supplier getSupplier(int supplierId);

    public void saveSupplier(Supplier supplier);

    public void removeSupplier(int supplierId);

    public List<Supplier> getAutoCompleteSuppliers(String name, int size);
}
