package org.openexi.fujitsu.proc.events;

import org.openexi.fujitsu.proc.common.CharacterSequence;
import org.openexi.fujitsu.proc.common.EventCode;
import org.openexi.fujitsu.proc.common.EventType;

public final class EXIEventSchemaMixedCharactersByRef extends EXIEventSchemaMixedCharacters {

    private EXITextProvider m_textProvider;

    public EXIEventSchemaMixedCharactersByRef(EXITextProvider textProvider, EventType eventType) {
        super(eventType);
        assert eventType.itemType == EventCode.ITEM_SCHEMA_CH_MIXED;
        m_textProvider = textProvider;
    }

    @Override
    public CharacterSequence getCharacters() {
        return m_textProvider.getCharacters();
    }
}
