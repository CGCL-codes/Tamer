package wilos.model.spem2.phase;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import wilos.model.misc.concretephase.ConcretePhase;
import wilos.model.spem2.activity.Activity;

/**
 * 
 * Phase is a special {@link Activity}, which prescribes predefined values for
 * its instances for the attributes prefix ('Phase') and isRepeatable ('False').
 * It has been included into the meta-model for convenience and to provide a
 * special stereotype, because it represents a very commonly used
 * {@link Activity} type. Phase represent a significant period in a project,
 * ending with major management checkpoint, milestone or set of Deliverables. It
 * is included in the model as a predefined special {@link Activity}, because
 * of its significance in defining breakdowns.
 * <p />
 * It's an element of the SPEM2 specification of the OMG
 *         organization (http://www.omg.org/).
 * 
 */
public class Phase extends Activity implements Cloneable {

    /**
	 * The ConcretePhases
	 */
    private Set<ConcretePhase> concretePhases;

    /**
	 * Default constructor
	 * 
	 */
    public Phase() {
        super();
        this.concretePhases = new HashSet<ConcretePhase>();
    }

    /**
	 * Returns a copy of the current instance of Phase
	 * 
	 * @return a copy of the Phase
	 * @throws CloneNotSupportedException
	 */
    @Override
    public Phase clone() throws CloneNotSupportedException {
        Phase phase = new Phase();
        phase.copy(this);
        return phase;
    }

    /**
	 * Copy the values of the specified Phase into the current instance of the
	 * class.
	 * 
	 * @param _phase
	 *            The Phase to copy.
	 */
    protected void copy(final Phase _phase) {
        super.copy(_phase);
        this.concretePhases.addAll(_phase.getConcretePhases());
    }

    /**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the Phase.
	 * 
	 * @param obj
	 *            the Object to be compare to the Phase
	 * @return true if the specified Object is the same, false otherwise
	 */
    public boolean equals(Object obj) {
        if (obj instanceof Phase == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Phase phase = (Phase) obj;
        return new EqualsBuilder().appendSuper(super.equals(phase)).append(this.concretePhases, phase.concretePhases).isEquals();
    }

    /**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of Phase
	 */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).toHashCode();
    }

    /**
	 * Removes the relation between the Phase and the specified ConcretePhase
	 * 
	 * @param _concretePhase the ConcretePhase to be removed
	 */
    public void removeConcretePhase(ConcretePhase _concretePhase) {
        _concretePhase.setPhase(null);
        this.concretePhases.remove(_concretePhase);
    }

    /**
	 * Adds relation between a specified concretePhase and the Phase 
	 * 
	 * @param _concretePhase the ConcretePhase related to the Phase
	 */
    public void addConcretePhase(ConcretePhase _concretePhase) {
        this.concretePhases.add(_concretePhase);
        _concretePhase.setPhase(this);
    }

    /**
	 * Removes all the concretePhase related to the Phase
	 */
    public void removeAllConcretePhases() {
        for (ConcretePhase tmp : this.concretePhases) {
            tmp.setPhase(null);
        }
        this.concretePhases.clear();
    }

    /**
	 * Assigns the Phase to all the ConcretePhase in parameter
	 * 
	 * @param _concretePhase the Set of COncretePhase to be related to
	 */
    public void addAllConcretePhases(Set<ConcretePhase> _concretePhase) {
        for (ConcretePhase td : _concretePhase) {
            td.addPhase(this);
        }
    }

    /**
	 * Returns the Collection of ConcretPhase related to the Phase
	 * 
	 * @return a Collection of ConcretPhase
	 */
    public Set<ConcretePhase> getConcretePhases() {
        return concretePhases;
    }

    /**
	 * Sets the ConcretePhase that are related to the Phase
	 * 
	 * @param concretePhases the Set of ConcretePhase related
	 */
    public void setConcretePhases(Set<ConcretePhase> concretePhases) {
        this.concretePhases = concretePhases;
    }
}
