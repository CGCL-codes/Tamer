package fr.inria.zvtm.common.kernel;

/**
 * This class gathers every temporizing constant
 * @author Julien Altieri
 *
 */
public class Temporizer {

    /**
	 * Viewer Temporizer
	 */
    public static long repaintMinDelay = 0;

    /**
	 * InputForwarder temporizer
	 */
    public static long sendEventDelay = 0;

    /**
	 * Delay used for processing the input queue of the connection
	 */
    public static long connectionProcessingDelay = 0;
}
