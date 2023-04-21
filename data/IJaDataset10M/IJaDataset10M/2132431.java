package net.etherstorm.jopenrpg;

import net.etherstorm.jopenrpg.event.PlayerInfoEvent;

/**
 * 
 * 
 * 
 * $Date: 2006/11/16 19:59:18 $<br>
 * @author tedberg
 * @author $Author: tedberg $
 * @version $Revision: 1.2 $
 * @since Oct 5, 2003
 */
public class PlayerEvent extends PlayerInfoEvent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Constructor declaration
	 *
	 *
	 * @param source
	 * @param id
	 * @param msgType
	 *
	 */
    public PlayerEvent(Object source, String id, int msgType) {
        super(source, id, msgType);
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param id
	 *
	 */
    public void setId(String id) {
        playerId = id;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param type
	 *
	 */
    public void setMsgType(int type) {
        messageType = type;
    }

    /**
	 * @param handle
	 * @see net.etherstorm.jopenrpg.event.PlayerInfoEvent#setHandle(java.lang.String)
	 */
    public void setHandle(String handle) {
        super.setHandle(handle);
    }

    /**
	 * @param status
	 * @see net.etherstorm.jopenrpg.event.PlayerInfoEvent#setStatus(java.lang.String)
	 */
    public void setStatus(String status) {
        super.setStatus(status);
    }
}
