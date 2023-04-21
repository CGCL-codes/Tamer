package a5c.part1;

import _untouchable_.BigMushroom_A;

/**
 * 
 * @author Timo Briddigkeit (Matrikelnummer: 2011218)
 *
 */
public class BigMushroom extends BigMushroom_A {

    private Elevator elevator = null;

    public BigMushroom(int nuol, boolean dbgen) {
        super(nuol, dbgen);
    }

    public void addElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    public synchronized Elevator getElevator() {
        return elevator;
    }
}
