package org.matsim.core.events;

import java.util.Map;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.basic.v01.events.BasicPersonEvent;
import org.matsim.core.api.population.Person;

/**
 * @author mrieser
 */
public abstract class PersonEvent extends BasicEventImpl implements BasicPersonEvent {

    public static final String ATTRIBUTE_PERSON = "person";

    private Person person;

    private final Id personId;

    public PersonEvent(final double time, final Person person) {
        super(time);
        this.person = person;
        this.personId = person.getId();
    }

    public PersonEvent(final double time, final Id personId) {
        super(time);
        this.personId = personId;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_PERSON, this.personId.toString());
        return attr;
    }

    /** @deprecated use {@link #getPersonId()} instead */
    public Person getPerson() {
        return this.person;
    }

    public Id getPersonId() {
        return this.personId;
    }
}
