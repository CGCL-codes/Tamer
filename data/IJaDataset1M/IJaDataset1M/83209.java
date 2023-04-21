package Lesson10;

import lejos.nxt.*;
import lejos.robotics.subsumption.*;

public class DriveForward_improved implements Behavior {

    public boolean takeControl() {
        return true;
    }

    public void suppress() {
        Motor.A.stop();
        Motor.C.stop();
    }

    public void action() {
        Motor.A.forward();
        Motor.C.forward();
        while (!Thread.interrupted()) ;
        Motor.A.stop();
        Motor.C.stop();
    }
}
