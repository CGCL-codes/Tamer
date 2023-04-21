package jerry.html.events;

import java.util.List;

/**
 * 
 * 
 * Item get event
 * 
 * 
 * @author (h0t@_G0|i
 * 
 */
public class ItmGetEvtCapImpl extends EvtCap {

    public ItmGetEvtCapImpl(List<String> list) {
        if (list.size() < 4) {
            validEvt = false;
            return;
        } else {
            validEvt = true;
            timeStamp = Double.valueOf(list.get(0)).doubleValue();
            itemName = list.get(2);
            playerId = Integer.parseInt(list.get(3));
            return;
        }
    }

    public String itemName;

    public int playerId;
}
