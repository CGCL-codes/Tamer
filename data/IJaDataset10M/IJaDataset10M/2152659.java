package com.exm.chp08;

/**
 *
 * @author Supervisor
 */
public class ByThree implements Series {

    public int start;

    public int val;

    public ByThree() {
        start = 0;
        val = 0;
    }

    public int getNext() {
        val += 3;
        return val;
    }

    public void reset() {
        start = 0;
        val = 0;
    }

    public void setStart(int x) {
        start = x;
        val = x;
    }
}
