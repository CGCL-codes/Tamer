package playground.kai.ids;

import org.matsim.api.core.v01.Id;
import org.matsim.core.population.PersonImpl;

/**
 * @author nagel
 *
 */
public class MyPersonImpl extends PersonImpl {

    PersonId id;

    /**
	 * @param id
	 */
    public MyPersonImpl(Id id) {
        super(id);
        this.id = (PersonId) id;
    }

    public PersonId getId() {
        return id;
    }
}
