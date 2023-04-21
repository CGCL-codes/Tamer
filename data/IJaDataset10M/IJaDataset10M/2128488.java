package net.etherstorm.jopenrpg.event;

/**
 * 
 * 
 * @author Ted Berg
 * @author $Author: tedberg $
 * @version $Revision: 1.6 $
 * $Date: 2002/03/09 08:28:38 $
 */
public interface MapMessageListener {

    /**
	 *
	 */
    public void mapMessageReceived(MapMessageEvent evt);
}
