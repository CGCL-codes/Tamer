package javaclient3.structures.mcom;

import javaclient3.structures.*;

/**
 * A piece of data.
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerMcomData implements PlayerConstants {

    private char full;

    private int data_count;

    private char[] data = new char[MCOM_DATA_LEN];

    /**
     * @return  a flag
     */
    public synchronized char getFull() {
        return this.full;
    }

    /**
     * @param newFull  a flag
     */
    public synchronized void setFull(char newFull) {
        this.full = newFull;
    }

    /**
     * @return  length of data
     */
    public synchronized int getData_count() {
        return this.data_count;
    }

    /**
     * @param newData_count  length of data
     */
    public synchronized void setData_count(int newData_count) {
        this.data_count = newData_count;
    }

    /**
     * @return  the data
     */
    public synchronized char[] getData() {
        return this.data;
    }

    /**
     * @param newData  the data
     */
    public synchronized void setData(char[] newData) {
        this.data = newData;
    }
}
