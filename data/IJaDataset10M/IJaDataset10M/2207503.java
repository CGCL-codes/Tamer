package org.tm4j.topicmap.ozone.index;

import org.tm4j.net.Locator;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.memory.index.MemoryOccurrenceLocatorIndex;
import java.io.Externalizable;
import java.util.Collection;

public class OzoneOccurrenceLocatorIndexImpl extends OzoneIndexImpl implements OzoneOccurrenceLocatorIndex, Externalizable {

    public OzoneOccurrenceLocatorIndexImpl() {
        super();
    }

    public void initialise(TopicMap tm) {
        m_helper = new MemoryOccurrenceLocatorIndex(tm);
    }

    public Collection getOccurrencesOfLocator(Locator loc) {
        return ((MemoryOccurrenceLocatorIndex) m_helper).getOccurrencesOfLocator(loc);
    }
}
