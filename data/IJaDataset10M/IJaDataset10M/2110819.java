package org.matsim.core.events;

import java.util.Map;
import org.matsim.api.core.v01.Id;
import org.matsim.core.api.experimental.events.PersonEvent;

/**
 * @author mrieser
 */
public abstract class PersonEventImpl extends EventImpl implements PersonEvent {

    public static final String ATTRIBUTE_PERSON = "person";

    private final Id personId;

    public PersonEventImpl(final double time, final Id personId) {
        super(time);
        this.personId = personId;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_PERSON, this.personId.toString());
        return attr;
    }

    @Override
    public Id getPersonId() {
        return this.personId;
    }
}
