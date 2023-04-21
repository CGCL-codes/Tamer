package playground.gregor.snapshots.postprocessors;

import java.util.HashMap;
import org.matsim.core.events.LinkLeaveEvent;
import org.matsim.core.events.handler.LinkLeaveEventHandler;
import playground.gregor.snapshots.writers.PositionInfo;

public class DestinationDependentColorizer implements PostProcessorI, LinkLeaveEventHandler {

    private static final int NUM_OF_COLOR_SLOTS = 256;

    private final HashMap<String, String> destNodeMapping = new HashMap<String, String>();

    public String[] processEvent(final String[] event) {
        String id = event[0];
        String color = getColor(id);
        event[15] = color;
        return event;
    }

    public void processPositionInfo(PositionInfo pos) {
        String color = getColor(pos.getAgentId().toString());
        pos.setUserData(Integer.parseInt(color));
    }

    public String getColor(final String id) {
        if (!this.destNodeMapping.containsKey(id)) {
            return "0";
        }
        return this.destNodeMapping.get(id);
    }

    public void handleEvent(final LinkLeaveEvent event) {
        if (event.getLinkId().toString().contains("shelter")) {
            this.destNodeMapping.put(event.getPersonId().toString(), event.getLinkId().toString().replace("shelter", ""));
        } else if (event.getLinkId().toString().contains("rev_el")) {
            this.destNodeMapping.put(event.getPersonId().toString(), event.getLinkId().toString().replace("rev_el", ""));
        } else if (event.getLinkId().toString().contains("el")) {
            this.destNodeMapping.put(event.getPersonId().toString(), event.getLinkId().toString().replace("el", ""));
        } else if (event.getLinkId().toString().contains("rev_")) {
            this.destNodeMapping.put(event.getPersonId().toString(), event.getLinkId().toString().replace("rev_", ""));
        }
    }

    public void reset(final int iteration) {
    }
}
