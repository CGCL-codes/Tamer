package info.metlos.jdc.nmdc.messages;

import info.metlos.jdc.common.MessageDirection;
import info.metlos.jdc.nmdc.INMDCMessageHandler;
import info.metlos.jdc.nmdc.NMDCHub;

/**
 * This class represents the $GetINFO message.
 * 
 * @author metlos
 * 
 * @version $Id: GetInfoMessage.java 232 2008-09-11 23:37:31Z metlos $
 */
public class GetInfoMessage extends AbstractHubMessage {

    private String myNick;

    private String requestedNick;

    public GetInfoMessage(NMDCHub hub) {
        super(hub, MessageDirection.outgoing, null);
    }

    @Override
    public void accept(INMDCMessageHandler handler) {
        handler.handle(this);
    }

    /**
	 * @return the myNick
	 */
    public String getMyNick() {
        return myNick;
    }

    /**
	 * @param myNick
	 *            the myNick to set
	 */
    public void setMyNick(String myNick) {
        this.myNick = myNick;
    }

    /**
	 * @return the requestedNick
	 */
    public String getRequestedNick() {
        return requestedNick;
    }

    /**
	 * @param requestedNick
	 *            the requestedNick to set
	 */
    public void setRequestedNick(String requestedNick) {
        this.requestedNick = requestedNick;
    }

    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder("$GetINFO ");
        bld.append(requestedNick).append(" ").append(myNick);
        return bld.toString();
    }
}
