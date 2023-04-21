package net.sf.elbe.core.events;

import net.sf.elbe.core.ELBECoreMessages;
import net.sf.elbe.core.model.IEntry;

public class AttributesInitializedEvent extends EntryModificationEvent {

    public AttributesInitializedEvent(IEntry initializedEntry, ModelModifier source) {
        super(initializedEntry.getConnection(), initializedEntry, source);
    }

    public String toString() {
        return ELBECoreMessages.bind(ELBECoreMessages.event__dn_attributes_initialized, new String[] { getModifiedEntry().getDn().toString() });
    }
}
