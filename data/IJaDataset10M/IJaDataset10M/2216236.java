package org.matsim.core.basic.v01;

import java.util.Map;
import org.matsim.api.basic.v01.Id;
import org.matsim.core.basic.v01.BasicEngineInformation.FuelType;

/**
 * @author dgrether
 */
public class BasicVehicleBuilder implements VehicleBuilder {

    private Map<String, BasicVehicleType> vehicleTypes;

    private Map<Id, BasicVehicle> vehicles;

    public BasicVehicleBuilder(Map<String, BasicVehicleType> vehicleTypes, Map<Id, BasicVehicle> vehicles) {
        this.vehicleTypes = vehicleTypes;
        this.vehicles = vehicles;
    }

    public BasicVehicleType createVehicleType(String type) {
        if (!this.vehicleTypes.containsKey(type)) {
            BasicVehicleType veh = new BasicVehicleTypeImpl(type);
            this.vehicleTypes.put(type, veh);
            return veh;
        }
        throw new IllegalArgumentException("Vehicle type with id: " + type + " already exists!");
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

    public BasicVehicle createVehicle(Id id, String type) {
        BasicVehicle veh = new BasicVehicleImpl(id, type);
        this.vehicles.put(id, veh);
        return veh;
    }

    public Map<String, BasicVehicleType> getVehicleTypes() {
        return this.vehicleTypes;
    }

    public Map<Id, BasicVehicle> getVehicles() {
        return this.vehicles;
    }
}
