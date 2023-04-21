package sw_emulator.hardware.device;

import java.lang.Thread;

/**
 * Emulate a matrix keyboard.
 * In this type of keyboard, there's n*m keys connected in a matrix like this:
 * <pre>
 *        1   1   1
 *        |   |   |
 *  A  ---@---@---@---          @= a key switch
 *        |   |   |
 *  B  ---@---@---@---
 *        |   |   |
 *        C   D   E
 * </pre>
 * If we make A equal to 0 and B equal to 1, from the C, D, and E values, we can
 * determine how key is pressed in the A line.
 * Note that if there's some special keys (e.g. keys that are not in the matrix,
 * because they have special function) they must be mapped also in the matrix
 * (this is for simplify the emulation).
 *
 * @author Ice
 * @version 1.00 16/04/2000
 */
public abstract class Keyboard extends Thread implements keys {

    public static final int K_PRESS = 0;

    public static final int K_RELEASE = 1;

    /**
   * General n*m matrix of keys: 0 means key pressed
   */
    protected int[][] keysMatrix;

    /**
   * Construct a generic matrix keyboard.
   *
   * @param keysMatrix a matrix of keys
   */
    public Keyboard(int[][] keysMatrix) {
        this.keysMatrix = keysMatrix;
    }

    /**
   * Press the key in the <code>a, b</code> matrix position
   *
   * @param a the row position
   * @param b the column position
   */
    public void pressKey(int a, int b) {
        keysMatrix[a][b] = K_PRESS;
        updateState();
    }

    /**
   * Release the key in the <code>a, b</code> matrix position
   *
   * @param a the row position
   * @param b the column position
   */
    public void releaseKey(int a, int b) {
        keysMatrix[a][b] = K_RELEASE;
        updateState();
    }

    /**
   * Update the state of output.
   * This is abstract, so it must be implemented: the methods must read the
   * input and then calculate the matrix output.
   */
    public abstract void updateState();
}
