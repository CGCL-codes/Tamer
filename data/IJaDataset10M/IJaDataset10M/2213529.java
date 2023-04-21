package sample;

import lejos.nxt.*;

public class MyRobot {

    public void go() {
        LCD.drawString("Press any button ", 0, 1);
        Button.waitForPress();
        Motor.A.rotate(angle);
        System.out.println("tc " + Motor.A.getTachoCount());
        Button.waitForPress();
    }

    private int angle = 90;
}
