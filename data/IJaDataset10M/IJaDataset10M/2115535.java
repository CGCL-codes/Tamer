package com.javaeye.procurement.service;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.javaeye.procurement.dao.SupplierDAO;
import com.javaeye.procurement.dto.Supplier;
import com.javaeye.common.web.PageInfo;

public class SupplierService implements ISupplierService {

    private static Log log = LogFactory.getLog(SupplierService.class);

    private SupplierDAO dao;

    public void setDao(SupplierDAO dao) {
        this.dao = dao;
    }

    public List<Supplier> getAutoCompleteSuppliers(String name, int size) {
        List<Supplier> results = dao.getAutoCompleteSuppliers(name, size);
        if (results == null || results.size() == 0) {
            log.warn("在数据库中未找到供应商数据");
        }
        return results;
    }

    @Override
    public Supplier getSupplier(String id) {
        int intId = Integer.parseInt(id);
        Supplier supplier = dao.getSupplier(intId);
        if (supplier == null) {
            log.warn("在数据库中未找到指定的供应商  id：" + id);
        }
        return supplier;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> getSupplierList(Supplier condition, PageInfo pageInfo) {
        List results = dao.getPageData(condition, pageInfo);
        if (results == null || results.size() == 0) {
            log.warn("在数据库中未找到供应商数据");
        }
        return results;
    }

    @Override
    public boolean removeSupplier(String id) {
        int intId = Integer.parseInt(id);
        dao.removeSupplier(intId);
        return true;
    }

    @Override
    public void saveSupplier(Supplier supplier) {
        dao.saveSupplier(supplier);
    }
}
