package sw_emulator.hardware.device;

/**
 * This interface define some methods that a matrix keyboard may use.
 * Remember that a matrix keyboard is a type of keyboard, where there's n*m
 * keys connected in a matrix like this:
 * <pre>
 *        0   0   0
 *        |   |   |
 *  A  ---@---@---@---          @= a key switch
 *        |   |   |
 *  B  ---@---@---@---
 *        |   |   |
 *        C   D   E
 * </pre>
 * The methods <code>pressKey</code> and <code>releaseKey</code> are to be
 * called where a key in the matrix is pressed ore released.
 *
 * @author Ice
 * @version 1.00 16/04/2000
 */
public interface keys {

    /**
   * Press the key in the <code>a, b</code> matrix position
   *
   * @param a the row position
   * @param b the column position
   */
    public void pressKey(int a, int b);

    /**
   * Release the key in the <code>a, b</code> matrix position
   *
   * @param a the row position
   * @param b the column position
   */
    public void releaseKey(int a, int b);
}
