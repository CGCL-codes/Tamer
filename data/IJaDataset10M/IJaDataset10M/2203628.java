package com.f2ms.service.cargotype;

import java.util.List;
import com.f2ms.dao.DAOFactory;
import com.f2ms.exception.DAOException;
import com.f2ms.model.CargoType;

public class CargoTypeServiceImpl implements ICargoTypeService {

    private DAOFactory daoFactory;

    public void setDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<CargoType> findAllCargoTypes() throws DAOException {
        return daoFactory.getCargoTypeDAO().findAllCargoTypes();
    }

    @Override
    public CargoType create(CargoType cargoType) throws DAOException {
        return daoFactory.getCargoTypeDAO().create(cargoType);
    }

    @Override
    public CargoType edit(CargoType cargoType) throws DAOException {
        return daoFactory.getCargoTypeDAO().edit(cargoType);
    }

    @Override
    public CargoType findCargoTypeById(long cargoTypeId) throws DAOException {
        return daoFactory.getCargoTypeDAO().findCargoTypeById(cargoTypeId);
    }

    @Override
    public List<CargoType> findCargoTypeByCriteria(CargoType cargoType) throws DAOException {
        return daoFactory.getCargoTypeDAO().findCargoTypeByCriteria(cargoType);
    }
}
