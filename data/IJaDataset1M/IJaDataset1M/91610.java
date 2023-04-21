package edu.sdsc.rtdsm.framework.sink;

import java.util.Vector;
import edu.sdsc.rtdsm.framework.data.DataPacket;

public interface SinkCallBackListener {

    public void callBack(DataPacket pkt);
}
