package org.inigma.utopia.paper;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import org.inigma.utopia.paper.events.AbstractEvent;

public class PaperParser {

    public static Collection<AbstractEvent> parse(String paper) {
        LinkedList<AbstractEvent> list = new LinkedList<AbstractEvent>();
        for (String token : paper.split("\n")) {
            for (PaperEvent eventType : PaperEvent.values()) {
                AbstractEvent event = eventType.getEvent(token);
                if (event != null) {
                    list.add(event);
                    break;
                }
            }
        }
        Collections.sort(list);
        return list;
    }
}
