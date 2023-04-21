package it.polito.appeal.traci;

import it.polito.appeal.traci.protocol.Constants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import de.uniluebeck.itm.tcpip.Storage;

public class ChangeGlobalTravelTimeQuery extends ChangeObjectStateQuery {

    private int beginTime;

    private int endTime;

    private double travelTime;

    public ChangeGlobalTravelTimeQuery(DataInputStream dis, DataOutputStream dos, String edgeID) {
        super(dis, dos, Constants.CMD_SET_EDGE_VARIABLE, edgeID, Constants.VAR_EDGE_TRAVELTIME);
    }

    /**
	 * @param beginTime the beginTime to set
	 */
    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    /**
	 * @param endTime the endTime to set
	 */
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    /**
	 * @param travelTime the travelTime to set
	 */
    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    @Override
    protected void writeParamsTo(Storage content) {
        content.writeByte(Constants.TYPE_COMPOUND);
        content.writeInt(3);
        content.writeByte(Constants.TYPE_INTEGER);
        content.writeInt(beginTime);
        content.writeByte(Constants.TYPE_INTEGER);
        content.writeInt(endTime);
        content.writeByte(Constants.TYPE_DOUBLE);
        content.writeDouble(travelTime);
    }
}
