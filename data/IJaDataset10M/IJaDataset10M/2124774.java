package org.matsim.vehicles;

import org.matsim.api.basic.v01.Id;
import org.matsim.vehicles.BasicEngineInformation.FuelType;

/**
 * @author dgrether
 */
public class BasicVehiclesFactoryImpl implements VehiclesFactory {

    public BasicVehiclesFactoryImpl() {
    }

    public BasicVehicle createVehicle(Id id, BasicVehicleType type) {
        BasicVehicle veh = new BasicVehicleImpl(id, type);
        return veh;
    }

    public BasicVehicleType createVehicleType(Id typeId) {
        BasicVehicleType veh = new BasicVehicleTypeImpl(typeId);
        return veh;
    }

    public BasicVehicleCapacity createVehicleCapacity() {
        return new BasicVehicleCapacityImpl();
    }

    public BasicFreightCapacity createFreigthCapacity() {
        return new BasicFreightCapacityImpl();
    }

    public BasicEngineInformation createEngineInformation(FuelType fuelType, double gasConsumption) {
        return new BasicEngineInformationImpl(fuelType, gasConsumption);
    }
}
