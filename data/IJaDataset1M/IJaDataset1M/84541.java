package com.kongur.network.erp.dao.warehouse.impl;

import org.springframework.stereotype.Repository;
import com.kongur.network.erp.common.Paginable;
import com.kongur.network.erp.dao.BaseDaoiBatis;
import com.kongur.network.erp.dao.warehouse.WarehouseLocationDAO;
import com.kongur.network.erp.domain.warehouse.WarehouseLocationDO;

@Repository(value = "warehouseLocationDAO")
public class WarehouseLocationDAOImpl extends BaseDaoiBatis<WarehouseLocationDO> implements WarehouseLocationDAO {

    @Override
    public Long insertWarehouseLocation(WarehouseLocationDO warehouseLocationDO) throws Exception {
        return (Long) executeInsert("WarehouseLocationDAO.insertWarehouseLocation", warehouseLocationDO);
    }

    @Override
    public Integer updateWarehouseLocation(WarehouseLocationDO warehouseLocationDO) throws Exception {
        return executeUpdate("WarehouseLocationDAO.updateWarehouseLocation", warehouseLocationDO);
    }

    @Override
    public Integer deleteWarehouseLocation(WarehouseLocationDO warehouseLocationDO) throws Exception {
        return this.executeUpdate("WarehouseRegionDAO.deleteWarehouseLocation", warehouseLocationDO);
    }

    @Override
    public Integer openWarehouseLocation(WarehouseLocationDO warehouseLocationDO) throws Exception {
        return this.executeUpdate("WarehouseRegionDAO.openWarehouseLocation", warehouseLocationDO);
    }

    @Override
    public WarehouseLocationDO selectWarehouseLocationById(Long id) throws Exception {
        return this.queryForObjectById("WarehouseLocationDAO.selectWarehouseLocationById", id);
    }

    @Override
    public Paginable<WarehouseLocationDO> selectWarehouseLocationForPagin(WarehouseLocationDO warehouseLocationDO) throws Exception {
        return this.getPagination(warehouseLocationDO, "WarehouseLocationDAO.selectWarehouseLocationForPaginTotalCount", "WarehouseLocationDAO.selectWarehouseLocationForPagin");
    }
}
