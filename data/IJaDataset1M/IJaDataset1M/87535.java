package net.sf.myway.gps.garmin.unit;

/**
 * @version $Revision: 1.1 $
 * @author andreas
 */
public interface MessageListener {

    /**
	 * Method messageReceived.
	 * 
	 * @param m
	 */
    void messageReceived(GarminMessage m);
}
