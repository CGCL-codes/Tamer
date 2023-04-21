package org.matsim.core.basic.v01.vehicles;

import org.matsim.api.basic.v01.Identifiable;

/**
 * @author dgrether
 */
public interface BasicVehicle extends Identifiable {

    public BasicVehicleType getType();
}
