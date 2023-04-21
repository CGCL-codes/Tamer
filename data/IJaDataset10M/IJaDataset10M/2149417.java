package javaclient3.structures.dio;

import javaclient3.structures.*;

/**
 * Data: input values (PLAYER_DIO_DATA_VALUES)
 * The dio interface returns data regarding the current state of the
 * digital inputs.
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerDioData implements PlayerConstants {

    private int count;

    private int digin;

    /**
     * @return  number of samples
     */
    public synchronized int getCount() {
        return this.count;
    }

    /**
     * @param newCount  number of samples
     */
    public synchronized void setCount(int newCount) {
        this.count = newCount;
    }

    /**
     * @return  bitfield of samples
     */
    public synchronized int getDigin() {
        return this.digin;
    }

    /**
     * @param newDigin  bitfield of samples
     */
    public synchronized void setDigin(int newDigin) {
        this.digin = newDigin;
    }
}
