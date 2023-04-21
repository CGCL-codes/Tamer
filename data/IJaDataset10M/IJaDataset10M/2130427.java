package edu.java.lectures.lec03.design.patterns;

import java.awt.Color;

public class CarTest {

    public static void main(String[] args) {
        Car car2 = CarFactory.instance().createCar(4, Color.BLACK, 20);
        Car car3 = CarFactory.instance().createDefaultCar();
        car3.setWeelsNumber(4);
        car3.setColor(Color.RED);
        car3.setReservoirVolume(20);
    }
}
