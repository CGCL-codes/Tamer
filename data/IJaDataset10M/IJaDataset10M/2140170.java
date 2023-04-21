package org.openexi.proc.events;

import org.openexi.proc.common.CharacterSequence;
import org.openexi.proc.common.EventCode;
import org.openexi.proc.common.EventType;
import org.openexi.proc.common.EXIEvent;

public final class EXIEventDTD implements EXIEvent {

    private final String m_name;

    private final String m_publicId;

    private final String m_systemId;

    private final CharacterSequence m_text;

    private final EventType m_eventType;

    public EXIEventDTD(String name, String publicId, String systemId, CharacterSequence text, EventType eventType) {
        assert eventType.itemType == EventCode.ITEM_DTD;
        m_name = name;
        m_publicId = publicId;
        m_systemId = systemId;
        m_text = text;
        m_eventType = eventType;
    }

    public String getPublicId() {
        return m_publicId;
    }

    public String getSystemId() {
        return m_systemId;
    }

    public byte getEventVariety() {
        return EXIEvent.EVENT_DTD;
    }

    public String getURI() {
        return null;
    }

    public String getName() {
        return m_name;
    }

    public String getPrefix() {
        return null;
    }

    public CharacterSequence getCharacters() {
        return m_text;
    }

    public final EventType getEventType() {
        return m_eventType;
    }
}
