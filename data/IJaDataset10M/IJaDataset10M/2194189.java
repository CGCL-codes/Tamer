package org.xmdl.taslak.dao;

import java.util.Collection;
import org.xmdl.ida.lib.dao.GenericDao;
import org.xmdl.taslak.model.Supplier;
import org.xmdl.taslak.model.search.SupplierSearch;

public interface SupplierDao extends GenericDao<Supplier, Long> {

    Collection<Supplier> search(SupplierSearch supplierSearch);
}
