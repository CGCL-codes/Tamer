package org.matsim.population;

import java.util.Map;
import org.matsim.basic.v01.BasicVehicleBuilder;
import org.matsim.basic.v01.BasicVehicleType;
import org.matsim.interfaces.basic.v01.Id;
import org.matsim.interfaces.core.v01.Vehicle;

/**
 * @author dgrether
 *
 */
public class VehicleBuilderImpl extends BasicVehicleBuilder {

    public VehicleBuilderImpl(Map<String, BasicVehicleType> vehicleTypes, Map<Id, Vehicle> vehicles) {
        super(vehicleTypes, (Map) vehicles);
    }

    @Override
    public Vehicle createVehicle(Id id, String type) {
        BasicVehicleType t = this.getVehicleTypes().get(type);
        if (t == null) {
            throw new IllegalArgumentException("Cannot create vehicle for unknown VehicleType: " + type);
        }
        Vehicle v = new VehicleImpl(id, t);
        this.getVehicles().put(id, v);
        return v;
    }
}
