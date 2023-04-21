package it.polito.appeal.traci.examples;

import it.polito.appeal.traci.SumoTraciConnection;
import java.util.Set;
import org.apache.log4j.BasicConfigurator;

public class OpenStepsClose {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SumoTraciConnection conn = new SumoTraciConnection("test/sumo_maps/box1l/test.sumo.cfg", 12345, false);
        try {
            conn.runServer();
            System.out.println("Map bounds are: " + conn.queryBounds());
            for (int i = 0; i < 10; i++) {
                int time = conn.getCurrentSimStep();
                Set<String> vehicles = conn.getActiveVehicles();
                System.out.println("At time step " + time + ", there are " + vehicles.size() + " vehicles: " + vehicles);
                conn.nextSimStep();
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
