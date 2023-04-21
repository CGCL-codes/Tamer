package pkg1;

import java.io.Serializable;

/**
 * Test for Serializable
 *
 * @author Bhavesh Patel
 * @deprecated This class is no longer used.
 */
@Deprecated
public abstract class C5 implements Serializable {

    /**
     * The name for this class.
     *
     * @serial
     */
    private String name;

    /**
     * @serial
     */
    private int publicKey;

    /**
     * Constructor for serialization only.
     */
    protected C5() {
    }

    /**
     * Prints general information.
     *
     */
    public void printInfo() {
    }
}
