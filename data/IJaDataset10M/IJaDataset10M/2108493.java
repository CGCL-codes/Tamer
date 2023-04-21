package playground.thibautd.jointtrips.population;

import java.util.List;
import java.util.Map;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;

/**
 * @author thibautd
 */
public interface JointActing {

    public boolean getJoint();

    public void setLinkedElements(Map<Id, ? extends JointActing> linkedElements);

    public void addLinkedElement(Id id, JointActing act);

    public Map<Id, ? extends JointActing> getLinkedElements();

    public void setLinkedElementsById(List<? extends Id> linkedElements);

    public void addLinkedElementById(Id linkedElement);

    public List<? extends Id> getLinkedElementsIds();

    public Person getPerson();

    public void setPerson(Person person);
}
