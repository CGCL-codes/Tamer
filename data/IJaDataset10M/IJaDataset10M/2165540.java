package playground.gregor.snapshots.postprocessor.processors;

import java.util.HashMap;
import org.matsim.events.LinkLeaveEvent;
import org.matsim.events.handler.LinkLeaveEventHandler;

public class DestinationDependentColorizer implements PostProcessorI, LinkLeaveEventHandler {

    private static final int NUM_OF_COLOR_SLOTS = 256;

    private final HashMap<String, String> destNodeMapping = new HashMap<String, String>();

    public String[] processEvent(final String[] event) {
        String id = event[0];
        String color = getColor(id);
        event[15] = color;
        return event;
    }

    public String getColor(final String id) {
        if (!this.destNodeMapping.containsKey(id)) {
            return "0";
        }
        return this.destNodeMapping.get(id);
    }

    public void handleEvent(final LinkLeaveEvent event) {
        if (event.linkId.contains("shelter")) {
            this.destNodeMapping.put(event.agentId, event.linkId.replace("shelter", ""));
        } else if (event.linkId.contains("rev_el")) {
            this.destNodeMapping.put(event.agentId, event.linkId.replace("rev_el", ""));
        } else if (event.linkId.contains("el")) {
            this.destNodeMapping.put(event.agentId, event.linkId.replace("el", ""));
        } else if (event.linkId.contains("rev_")) {
            this.destNodeMapping.put(event.agentId, event.linkId.replace("rev_", ""));
        }
    }

    public void reset(final int iteration) {
    }
}
