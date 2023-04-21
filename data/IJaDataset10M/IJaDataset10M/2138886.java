package monitor;

import java.beans.PropertyChangeEvent;
import java.util.UUID;

/**
 * @author Robbie
 *
 */
public class HeartBeatAnalyzer extends Analyzer {

    /**
	 * @param pktID
	 * @param field
	 * @throws Exception
	 */
    public HeartBeatAnalyzer(String pktID, String field) {
        super(pktID, field);
    }

    public HeartBeatAnalyzer() {
        super("HRT BEAT", "heartBeat");
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(getPacketID())) {
            byte[] data = (byte[]) evt.getNewValue();
            UUID uuid = ((RemoteHost) evt.getOldValue()).getUUID();
            broadcastFieldUpdate(uuid, new String(data));
        }
    }
}
