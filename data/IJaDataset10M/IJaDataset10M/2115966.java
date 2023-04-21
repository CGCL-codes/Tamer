package ru.concretesoft.concretesplitviewer;

/**
 *
 * @author Mytinski Leonid
 * 
 * Class desribed one lap of distance.
 * 
 */
public class Lap {

    private int length;

    private int beginNumber;

    private int endNumber;

    /**
     * Start
     */
    public static final int START_CONTROL_POINT = 0;

    /**
     * Finsh
     */
    public static final int FINISH_CONTROL_POINT = -1;

    /**
     * Number of control point is unknown
     */
    public static final int UNKNOWN_CONTROL_POINT = -2;

    /**Create new lap
     * 
     * @param beginNumber real number of control point from which lap begin
     * @param endNumber real number of control point where lap ends
     * @param length length of lap
     */
    public Lap(int beginNumber, int endNumber, int length) {
        setBeginNumber(beginNumber);
        setEndNumber(endNumber);
        setLength(length);
    }

    /**
     * 
     * 
     * @param length length of lap
     */
    public Lap(int length) {
        this(UNKNOWN_CONTROL_POINT, UNKNOWN_CONTROL_POINT, length);
    }

    /**
     * 
     * @return length of lap
     */
    public int getLength() {
        return length;
    }

    /**
     * 
     * @param length length of lap
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * 
     * @return real number of control point from which lap begin
     */
    public int getBeginNumber() {
        return beginNumber;
    }

    /**
     * 
     * @param beginNumber real number of control point from which lap begin
     */
    public void setBeginNumber(int beginNumber) {
        this.beginNumber = beginNumber;
    }

    /**
     * 
     * @return real number of control point where lap ends
     */
    public int getEndNumber() {
        return endNumber;
    }

    /**
     * 
     * @param endNumber real number of control point where lap ends
     */
    public void setEndNumber(int endNumber) {
        this.endNumber = endNumber;
    }
}
