package com.pragprog.dhnako.carserv.dom.vehicle;

import com.pragprog.dhnako.carserv.dom.payment.PaymentMethodOwner;

public interface VehicleOwner extends PaymentMethodOwner {

    void addToVehicles(Vehicle vehicle);

    void removeFromVehicles(Vehicle vehicle);

    String validateAddToVehicles(Vehicle vehicle);

    String validateRemoveFromVehicles(Vehicle vehicle);
}
