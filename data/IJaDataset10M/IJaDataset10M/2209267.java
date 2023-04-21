package org.etexascode.examples;

import org.etexascode.api.Vehicle;
import org.etexascode.api.VehicleManager;
import org.etexascode.api.VehicleManagerListener;
import org.etexascode.api.eTEXAS;
import java.util.Collection;

public class SimulatorWithVehicleInfo {

    public static void main(String args[]) {
        eTEXAS etexas = new eTEXAS();
        VehicleManager vehicleManager = new VehicleManager(etexas, etexas.getModelData());
        ExampleVehicleManagerListener listener = new ExampleVehicleManagerListener();
        vehicleManager.registerListener(listener);
        System.out.println("Starting simulation...");
        while (!etexas.isFinished()) {
            System.out.println("Simulated " + etexas.getCurrentTime() + " seconds...");
            etexas.nextTimeStep();
            Collection<Vehicle> currentVehicles = vehicleManager.getCurrentVehicles();
            System.out.println("There are " + currentVehicles.size() + " vehicles...");
        }
        System.out.println("Simulation is finished.");
        vehicleManager.removeListener(null);
    }

    static class ExampleVehicleManagerListener implements VehicleManagerListener {

        public void vehicleLogin(Vehicle vehicle) {
            System.out.println("Vehicle Login:");
            System.out.println(vehicle);
        }

        public void vehicleLogout(Vehicle vehicle) {
            System.out.println("Vehicle Logout:");
            System.out.println(vehicle);
        }
    }
}
