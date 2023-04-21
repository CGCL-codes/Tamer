package org.iqual.chaplin.example.basic.composite.couple4;

/**
 * @author Zbynek Slajchrt
 * @since Mar 31, 2009 8:42:37 PM
 */
public class Car {

    private int fuel;

    public int getFuel() {
        return fuel;
    }

    public void addFuel(int f) {
        System.out.println("Adding fuel: " + f);
        fuel = f;
    }
}
