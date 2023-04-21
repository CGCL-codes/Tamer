package com.f2ms.service.warehouse;

import java.util.List;
import com.f2ms.dao.DAOFactory;
import com.f2ms.exception.DAOException;
import com.f2ms.model.Warehouse;

public class WarehouseServiceImpl implements IWarehouseService {

    private DAOFactory daoFactory;

    public void setDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<Warehouse> findAllWarehouses() throws DAOException {
        return daoFactory.getWarehouseDAO().findAllWarehouses();
    }

    @Override
    public Warehouse create(Warehouse warehouse) throws DAOException {
        return daoFactory.getWarehouseDAO().create(warehouse);
    }

    @Override
    public Warehouse edit(Warehouse warehouse) throws DAOException {
        return daoFactory.getWarehouseDAO().edit(warehouse);
    }

    @Override
    public Warehouse findWarehouseById(long warehouseId) throws DAOException {
        return daoFactory.getWarehouseDAO().findWarehouseById(warehouseId);
    }

    @Override
    public List<Warehouse> findWarehouseByCriteria(Warehouse warehouse) throws DAOException {
        return daoFactory.getWarehouseDAO().findWarehouseByCriteria(warehouse);
    }
}
