package com.pragprog.dhnako.carserv.fixture.vehicle;

import org.nakedobjects.applib.fixtures.AbstractFixture;
import com.pragprog.dhnako.carserv.dom.service.Car;
import com.pragprog.dhnako.carserv.dom.service.Motorcycle;
import com.pragprog.dhnako.carserv.dom.service.ServiceableVehicle;
import com.pragprog.dhnako.carserv.dom.service.Van;
import com.pragprog.dhnako.carserv.dom.vehicle.Make;
import com.pragprog.dhnako.carserv.dom.vehicle.Model;
import com.pragprog.dhnako.carserv.dom.vehicle.VehicleType;

public class MakesAndModelsFixture extends AbstractFixture {

    public void install() {
        VehicleType carVehicleType = createVehicleType(Car.class);
        VehicleType vanVehicleType = createVehicleType(Van.class);
        VehicleType motorcycleVehicleType = createVehicleType(Motorcycle.class);
        Make fordMake = createMake("Ford");
        Make toyotaMake = createMake("Toyota");
        Make yamahaMake = createMake("Yamaha");
        Make hondaMake = createMake("Honda");
        createModel(fordMake, "Focus", carVehicleType, 12);
        createModel(fordMake, "Mustang", carVehicleType, 3);
        createModel(toyotaMake, "Corolla", carVehicleType, 9);
        createModel(toyotaMake, "Yaris", carVehicleType, 18);
        createModel(hondaMake, "Civic", carVehicleType, 12);
        createModel(hondaMake, "Accord", carVehicleType, 9);
        createModel(fordMake, "Ranger", vanVehicleType, 9);
        createModel(fordMake, "Transit", vanVehicleType, 6);
        createModel(toyotaMake, "Hiace", vanVehicleType, 9);
        createModel(toyotaMake, "Hilux", vanVehicleType, 9);
        createModel(hondaMake, "Varadero", motorcycleVehicleType, 12);
        createModel(hondaMake, "Shadow", motorcycleVehicleType, 12);
        createModel(hondaMake, "VFR800", motorcycleVehicleType, 12);
        createModel(yamahaMake, "Tenere", motorcycleVehicleType, 12);
        createModel(yamahaMake, "FZ1 Fazer", motorcycleVehicleType, 12);
    }

    private VehicleType createVehicleType(Class<? extends ServiceableVehicle> subclass) {
        VehicleType vehicleType = newTransientInstance(VehicleType.class);
        vehicleType.setFullyQualifiedClassName(subclass.getName());
        persist(vehicleType);
        return vehicleType;
    }

    private Make createMake(String name) {
        Make make = newTransientInstance(Make.class);
        make.setName(name);
        persist(make);
        return make;
    }

    private Model createModel(Make make, String name, VehicleType vehicleType, int serviceInterval) {
        Model model = newTransientInstance(Model.class);
        model.setName(name);
        model.setVehicleType(vehicleType);
        model.setServiceInterval(serviceInterval);
        make.addToModels(model);
        persist(model);
        return model;
    }
}
