package org.opennms.netmgt.importer.operations;

import java.util.List;
import org.opennms.netmgt.xml.event.Event;

public interface ImportOperation {

    void gatherAdditionalData();

    List<Event> persist();
}
