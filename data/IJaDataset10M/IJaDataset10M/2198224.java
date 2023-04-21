package org.fontbox.afm;

/**
 * This represents some kern pair data.
 *
 * @author Ben Litchfield (ben@benlitchfield.com)
 * @version $Revision: 1.1 $
 */
public class KernPair {

    private String firstKernCharacter;

    private String secondKernCharacter;

    private float x;

    private float y;

    /** Getter for property firstKernCharacter.
     * @return Value of property firstKernCharacter.
     */
    public java.lang.String getFirstKernCharacter() {
        return firstKernCharacter;
    }

    /** Setter for property firstKernCharacter.
     * @param firstKernCharacterValue New value of property firstKernCharacter.
     */
    public void setFirstKernCharacter(String firstKernCharacterValue) {
        firstKernCharacter = firstKernCharacterValue;
    }

    /** Getter for property secondKernCharacter.
     * @return Value of property secondKernCharacter.
     */
    public java.lang.String getSecondKernCharacter() {
        return secondKernCharacter;
    }

    /** Setter for property secondKernCharacter.
     * @param secondKernCharacterValue New value of property secondKernCharacter.
     */
    public void setSecondKernCharacter(String secondKernCharacterValue) {
        secondKernCharacter = secondKernCharacterValue;
    }

    /** Getter for property x.
     * @return Value of property x.
     */
    public float getX() {
        return x;
    }

    /** Setter for property x.
     * @param xValue New value of property x.
     */
    public void setX(float xValue) {
        x = xValue;
    }

    /** Getter for property y.
     * @return Value of property y.
     */
    public float getY() {
        return y;
    }

    /** Setter for property y.
     * @param yValue New value of property y.
     */
    public void setY(float yValue) {
        y = yValue;
    }
}
