package jerry.html.events;

import java.util.List;

/**
 * 
 * 
 * Flag capture event
 * 
 * 
 * @author (h0t@_G0|i
 *
 */
public class FlagCappedEvtCapImpl extends EvtCap {

    public FlagCappedEvtCapImpl(List<String> list) {
        if (list.size() < 4) {
            validEvt = false;
            return;
        } else {
            validEvt = true;
            timeStamp = Double.valueOf(list.get(0)).doubleValue();
            playerID = Integer.parseInt(list.get(2));
            teamID = Integer.parseInt(list.get(3));
            return;
        }
    }

    public int playerID;

    public int teamID;
}
