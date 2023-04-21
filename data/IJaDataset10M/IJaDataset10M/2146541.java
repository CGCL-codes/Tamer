package de.greenteam.msg;

import java.util.Date;
import java.util.List;
import de.greenteam.core.Util;

/**
 * A clientbean sends this directly to the server to send an instant message to
 * a list of targets
 * 
 * @author Markus
 */
public class MsgInstantMessageRequest extends Msg {

    private List<Integer> listOfTargetUserID;

    private Date date;

    /**
	 * 
	 * @param sourceUserID source
	 * @param listOfTargets destination
	 * @param text chatmessage
	 */
    public MsgInstantMessageRequest(int sourceUserID, List<Integer> listOfTargetUserID, String text, Date date) {
        super(MsgType.INSTANT_MESSAGE_REQUEST, true);
        i0 = sourceUserID;
        this.listOfTargetUserID = Util.optimizeIntegerList(listOfTargetUserID);
        s0 = Util.integerList2String(this.listOfTargetUserID);
        s1 = text;
        this.date = date;
    }

    public int getSourceUserID() {
        return i0;
    }

    public String getText() {
        return s1;
    }

    public List<Integer> getListOfTargetUserID() {
        return listOfTargetUserID;
    }

    public Date getDate() {
        return date;
    }
}
