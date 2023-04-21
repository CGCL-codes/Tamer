package net.sf.orcc.df;

import net.sf.orcc.moc.MoC;
import org.eclipse.emf.common.util.EList;

/**
 * This class defines an instance. An instance has an id, a class, parameters
 * and attributes. The class of the instance points to an actor or a network.
 * 
 * @author Matthieu Wipliez
 * @model extends="Vertex"
 */
public interface Instance extends DfVertex {

    /**
	 * Returns the actor referenced by this instance.
	 * 
	 * @return the actor referenced by this instance, or <code>null</code> if
	 *         this instance does not reference an actor
	 */
    Actor getActor();

    /**
	 * Returns the list of argument of this instance.
	 * 
	 * @return the list of argument of this instance
	 * @model containment="true"
	 */
    public EList<Argument> getArguments();

    /**
	 * Returns the broadcast referenced by this instance.
	 * 
	 * @return the broadcast referenced by this instance, or <code>null</code>
	 *         if this instance does not reference a broadcasst
	 */
    Broadcast getBroadcast();

    /**
	 * Returns the instantiable object referenced by this instance.
	 * 
	 * @model
	 */
    Entity getEntity();

    /**
	 * Returns the identifier of this instance. Delegates to {@link #getName()}.
	 * 
	 * @return the identifier of this instance
	 */
    @Deprecated
    String getId();

    /**
	 * Returns the classification class of this instance.
	 * 
	 * @return the classification class of this instance
	 */
    MoC getMoC();

    /**
	 * Returns the network referenced by this instance.
	 * 
	 * @return the network referenced by this instance, or <code>null</code> if
	 *         this instance does not reference a network
	 */
    Network getNetwork();

    /**
	 * Returns <code>true</code> if this instance references an actor.
	 * 
	 * @return <code>true</code> if this instance references an actor
	 */
    boolean isActor();

    /**
	 * Returns <code>true</code> if this instance references a broadcast.
	 * 
	 * @return <code>true</code> if this instance references a broadcast
	 */
    boolean isBroadcast();

    /**
	 * Returns <code>true</code> if this instance references a network.
	 * 
	 * @return <code>true</code> if this instance references a network
	 */
    boolean isNetwork();

    /**
	 * Sets the entity referenced by this instance.
	 * 
	 * @param entity
	 *            an entity
	 */
    void setEntity(Entity entity);
}
