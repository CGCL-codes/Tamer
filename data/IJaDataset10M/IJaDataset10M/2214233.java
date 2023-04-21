package de.spec.maintenance;

/**
 * Constants used to control the speech recognition. The
 * <code>DataController</code> use these constants.<br>
 * <p>
 * Possible values are:
 * <ul>
 * <li><code>RECORD</code>
 * <li><code>STOP</code>
 * <li><code>PLAY</code>
 * <li><code>DELETE</code>
 * </ul>
 * 
 * @see DataController#audioRequest(int)
 * 
 * @author antoras
 * @version 0.1
 * @since JDK1.6, 25.04.2009
 * 
 */
public interface AudioConstants {

    /**
	 * Record audio data
	 */
    public static final int RECORD = 0;

    /**
	 * Stop recording audio data
	 */
    public static final int STOP = 1;

    /**
	 * Play the recorded audio data
	 */
    public static final int PLAY = 2;

    /**
	 * Delete the recorded audio data
	 */
    public static final int DELETE = 3;
}
