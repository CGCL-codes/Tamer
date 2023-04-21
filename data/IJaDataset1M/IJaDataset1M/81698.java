package imp.brickdictionary;

import polya.Polylist;
import static polya.Polylist.list;

/**
 * purpose: Object for key/mode pairs corresponding to blocks
 * @author Zachary Merritt
 */
public class KeyMode {

    private long key = -1;

    private String mode = "";

    /** KeyMode / 2
     * Constructs a properly initialized KeyMode
     * @param k, a key (a long)
     * @param m, a mode (a String)
     */
    public KeyMode(long k, String m) {
        key = k;
        mode = m;
    }

    /** KeyMode / 0
     * Default constructor for a KeyMode. Returns a no-chord modeless KeyMode.
     */
    public KeyMode() {
    }

    /** getKey
     * Gets the value of the key
     * @return the key, a long
     */
    public long getKey() {
        return key;
    }

    /** setKey
     * Sets the key to the specified value
     * @param k, a key as a long
     */
    public void setKey(long k) {
        key = k;
    }

    /** getMode
     * Gets the mode of the KeyMode
     * @return the mode, a String
     */
    public String getMode() {
        return mode;
    }

    /** setMode
     * Sets the mode to the specified value
     * @param m, a mode as a String
     */
    public void setMode(String m) {
        mode = m;
    }

    /** toPolylist
     * Returns a Polylist representation of a KeyMode.
     * @return a Polylist of form (mode, key)
     */
    public Polylist toPolylist() {
        return list(mode, key);
    }

    /** toString
     * Returns a String representation of a KeyMode.
     * @return a String of the Polylist of the KeyMode
     */
    @Override
    public String toString() {
        return toPolylist().toString();
    }
}
