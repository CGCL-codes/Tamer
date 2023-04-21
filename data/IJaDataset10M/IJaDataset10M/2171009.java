package com.grt192.actuator;

import com.grt192.core.Actuator;
import com.grt192.core.Command;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Student
 */
public class GRTVictor extends Actuator implements PIDOutput {

    Victor victor;

    public GRTVictor(int channel) {
        victor = new Victor(channel);
    }

    public void executeCommand(Command c) {
        double value = c.getValue();
        if (value > 1.0) {
            value = 1.0;
        }
        if (value < -1.0) {
            value = -1.0;
        }
        System.out.println(this + " " + c.getValue());
        victor.set(value);
    }

    protected void halt() {
        victor.set(0);
    }

    public String toString() {
        return "Victor";
    }

    public void pidWrite(double output) {
        this.enqueueCommand(output);
    }
}
