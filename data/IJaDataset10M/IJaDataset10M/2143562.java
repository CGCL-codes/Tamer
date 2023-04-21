package de.unikoblenz.isweb.xcosima.dns;

import java.util.LinkedList;
import java.util.List;
import org.openrdf.model.URI;
import de.unikoblenz.isweb.xcosima.FieldOverride;
import de.unikoblenz.isweb.xcosima.Property;
import de.unikoblenz.isweb.xcosima.ontologies.ONT;
import de.unikoblenz.isweb.xcosima.storage.Repository;
import de.unikoblenz.isweb.xcosima.storage.syncstatus;

/**
 * The Java class representing a DOLCE endurant.
 * @author Thomas Franz, http://isweb.uni-koblenz.de
 *
 */
@FieldOverride(fields = { "classifiers" }, properties = { @Property(ONT.EDNS_P_plays) })
public class Endurant extends Particular {

    @Property(value = ONT.DOLCE_P_participant, inverse = true)
    List<Perdurant> events = new LinkedList<Perdurant>();

    public Endurant() {
        super();
    }

    protected Endurant(URI uri) {
        super(uri);
    }

    protected Endurant(syncstatus unloaded) {
        super(unloaded);
    }

    public List<Concept<?>> getRoles(Repository r) {
        return super.getClassifiers(r);
    }

    public void addRole(Role r) {
        super.addClassifier(r);
    }

    /** State that this endurant participates in <code>p</code>.
	 * 
	 * @param p
	 */
    public void addEvent(Perdurant p) {
        if (!events.contains(p)) {
            events.add(p);
            p.addParticipant(this);
        }
    }

    public List<Perdurant> getEvents(Repository r) {
        load(events, r);
        return events;
    }
}
