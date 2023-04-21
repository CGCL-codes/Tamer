package com.f2ms.service.shipmentmode;

import java.util.List;
import com.f2ms.dao.DAOFactory;
import com.f2ms.exception.DAOException;
import com.f2ms.model.ShipmentMode;

public class ShipmentModeServiceImpl implements IShipmentModeService {

    private DAOFactory daoFactory;

    public void setDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<ShipmentMode> findAllShipmentModes() throws DAOException {
        return daoFactory.getShipmentModeDAO().findAllShipmentModes();
    }

    @Override
    public ShipmentMode create(ShipmentMode shipmentMode) throws DAOException {
        return daoFactory.getShipmentModeDAO().create(shipmentMode);
    }

    @Override
    public ShipmentMode edit(ShipmentMode shipmentMode) throws DAOException {
        return daoFactory.getShipmentModeDAO().edit(shipmentMode);
    }

    @Override
    public ShipmentMode findShipmentModeById(long shipmentModeId) throws DAOException {
        return daoFactory.getShipmentModeDAO().findShipmentModeById(shipmentModeId);
    }

    @Override
    public List<ShipmentMode> findShipmentModeByCriteria(ShipmentMode shipmentMode) throws DAOException {
        return daoFactory.getShipmentModeDAO().findShipmentModeByCriteria(shipmentMode);
    }
}
