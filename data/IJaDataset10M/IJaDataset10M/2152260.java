package edu.mapi.ir.controllers.reactive;

import edu.mapi.ir.ciberIF.beaconMeasure;
import edu.mapi.ir.ciberIF.ciberIF;

/**
 * @author ZP
 */
public class GoToTarget implements ReactiveController {

    private int beaconToFollow = 0;

    private double speed = 0.1;

    public boolean decide(ciberIF cif) {
        if (true) return true;
        if (cif.IsBeaconReady(beaconToFollow)) {
            beaconMeasure beacon = cif.GetBeaconSensor(beaconToFollow);
            if (beacon.beaconVisible) {
                double valLeft = speed;
                double valRight = speed;
                valLeft *= (1 - (beacon.beaconDir) / 180);
                valRight *= (1 + (beacon.beaconDir) / 180);
                cif.DriveMotors(valLeft, valRight);
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return "Try to reach the target";
    }
}
