package uk.org.ogsadai.client.toolkit;

/**
 * Defines the skeleton for an algorithm that controls how frequently data 
 * should be written. Implementations decide what to do before and after blocks 
 * of data are put.
 *
 * @author The OGSA-DAI Project Team
 */
public interface DataSinkResourceCallController {

    /**
     * Method called before data is written.
     * 
     * @param n
     *     maximum number of data values to write
     */
    public void preDataCall(int n);

    /**
     * Method called after data is written.
     * 
     * @param requestedN
     *     requested number of data values
     * @param actualN
     *     actual number of data values written
     */
    public void postDataCall(int requestedN, int actualN);
}
