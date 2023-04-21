package org.tm4j.topicmap.unified;

import org.tm4j.net.Locator;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;

public class UnifiedOccurrence extends UnifiedScopedObject implements Occurrence {

    public UnifiedOccurrence(UnifiedTopicMap tm, Occurrence base) {
        super(tm, base);
    }

    public Topic getParent() {
        Topic raw = ((Occurrence) getBaseObject()).getParent();
        if (raw != null) {
            return new UnifiedTopic(m_tm, raw);
        }
        return null;
    }

    public Topic getType() {
        Topic raw = ((Occurrence) getBaseObject()).getType();
        if (raw != null) {
            return new UnifiedTopic(m_tm, raw);
        }
        return null;
    }

    public boolean isOfType(Topic type) {
        Topic rawType = ((Association) getBaseObject()).getType();
        if (type instanceof UnifiedTopic) {
            if (((UnifiedTopic) type).getBaseTopic().equals(rawType)) {
                return true;
            }
        } else {
            if (type.equals(rawType)) {
                return true;
            }
        }
        return m_tm.getMergedTopics(type).contains(rawType);
    }

    public void setParent(Topic parent) {
        unsupported();
    }

    public void setType(Topic type) {
        unsupported();
    }

    public String getData() {
        return ((Occurrence) getBaseObject()).getData();
    }

    public Locator getDataLocator() {
        return ((Occurrence) getBaseObject()).getDataLocator();
    }

    public boolean isDataInline() {
        return ((Occurrence) getBaseObject()).isDataInline();
    }

    public void setData(String data) {
        unsupported();
    }

    public void setDataLocator(Locator loc) {
        unsupported();
    }
}
