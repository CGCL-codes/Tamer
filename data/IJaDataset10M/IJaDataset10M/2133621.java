package org.opennms.netmgt.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.opennms.netmgt.eventd.datablock.EventConfData;
import org.opennms.netmgt.xml.eventconf.Events;
import org.springframework.core.io.Resource;

class EventConfiguration {

    /**
     * Map of configured event files and their events
     */
    private Map<Resource, Events> m_eventFiles = new HashMap<Resource, Events>();

    /**
     * The mapping of all the event configuration objects for searching
     */
    private EventConfData m_eventConfData = new EventConfData();

    /**
     * The list of secure tags.
     */
    private Set<String> m_secureTags = new HashSet<String>();

    /**
     * Total count of events in these files.
     */
    private int m_eventCount = 0;

    public EventConfData getEventConfData() {
        return m_eventConfData;
    }

    public void setEventConfData(EventConfData eventConfData) {
        m_eventConfData = eventConfData;
    }

    public Map<Resource, Events> getEventFiles() {
        return m_eventFiles;
    }

    public void setEventFiles(Map<Resource, Events> eventFiles) {
        m_eventFiles = eventFiles;
    }

    public Set<String> getSecureTags() {
        return m_secureTags;
    }

    public void setSecureTags(Set<String> secureTags) {
        m_secureTags = secureTags;
    }

    public int getEventCount() {
        return m_eventCount;
    }

    public void incrementEventCount(int incrementCount) {
        m_eventCount += incrementCount;
    }
}
