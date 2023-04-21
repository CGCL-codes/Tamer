package Lesson2;

import Common.Car;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class WallFollower {

    public static void main(String[] aArg) throws Exception {
        UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
        int dist_01 = 50, dist_02 = 25, dist_03 = 15, distance;
        final int forward = 1;
        final int backward = 2;
        final int power = 80;
        LCD.drawString("Distance: ", 0, 1);
        LCD.drawString("Power:    ", 0, 2);
        LCD.drawString("Action:   ", 0, 4);
        distance = us.getDistance();
        while (!Button.ESCAPE.isPressed()) {
            distance = us.getDistance();
            if (distance >= dist_01) {
                MotorPort.C.controlMotor(power, forward);
                MotorPort.B.controlMotor(60, forward);
                LCD.drawString("Turn Right", 0, 5);
            } else {
                if (distance >= dist_02) {
                    MotorPort.C.controlMotor(power, forward);
                    MotorPort.B.controlMotor(power, forward);
                    LCD.drawString("Go ahead", 0, 5);
                } else {
                    if (distance >= dist_03) {
                        MotorPort.C.controlMotor(60, forward);
                        MotorPort.B.controlMotor(power, forward);
                        LCD.drawString("Turn Left", 0, 5);
                    } else {
                        MotorPort.C.controlMotor(80, backward);
                        MotorPort.B.controlMotor(60, backward);
                        LCD.drawString("Rotation Power", 0, 5);
                    }
                }
            }
            LCD.drawInt(distance, 4, 10, 1);
            LCD.drawInt(power, 4, 10, 2);
            Thread.sleep(300);
        }
        Car.stop();
        LCD.clear();
        LCD.drawString("Program stopped", 0, 0);
        Thread.sleep(2000);
    }
}
