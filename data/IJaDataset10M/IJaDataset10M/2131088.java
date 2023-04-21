package org.nox.domain.vehicle.dao;

import org.nox.domain.vehicle.Vehicle;

public interface VehicleDao {

    public void insert(Vehicle vehicle);

    public void update(Vehicle vehicle);

    public void delete(Vehicle vehicle);

    public Vehicle findByVehicleNo(String vehicleNo);
}
