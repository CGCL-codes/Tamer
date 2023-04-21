package mimosa.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import mimosa.util.OrderedLinkedListElement;
import mimosa.util.Pair;

/**
 * A simulator is in charge of executing a single entity in a consistent way.
 * Some information are actually stored and used by the global coordinator.
 * 
 * @author Jean-Pierre Muller
 */
public class Simulator implements OrderedLinkedListElement<Simulator> {

    public static final int INFINITY = -1;

    /** The entity the simulator is in charge of */
    private Entity entity = null;

    /** The current local time */
    private long localTime = 0;

    /** The coordinator */
    private Coordinator coordinator = null;

    /** Defines the origin in the coordinator time */
    private transient long origin = 0;

    /** The internal influence scheduled global date */
    private transient long nextInternalDate = INFINITY;

    /** The current internal influence */
    private transient InternalInfluence nextInternalInfluence = null;

    /** The external influences scheduled global date */
    private transient long nextExternalDate = INFINITY;

    /** The external influences to be sent at the next external date */
    private transient Collection<ExternalInfluence> nextExternalInfluences = new ArrayList<ExternalInfluence>();

    /** The incoming logical influences */
    private transient Collection<LogicalInfluence> nextLogicalInfluences = new ArrayList<LogicalInfluence>();

    /** The next simulator in the list */
    private transient Simulator next;

    /** The previous simulator in the list */
    private transient Simulator previous;

    /** Embeds an entity within a simulator for execution */
    public Simulator(Entity entity, Coordinator coordinator) {
        this.entity = entity;
        entity.setSimulator(this);
        this.coordinator = coordinator;
    }

    /**
	 * @return Returns the next date for internal transition.
	 */
    public long getNextInternalDate() {
        return nextInternalDate;
    }

    /**
	 * @return Returns the closest date of an internal or external event.
	 */
    public long getNextDate() {
        if (nextInternalDate == INFINITY) return nextExternalDate; else if (nextExternalDate == INFINITY) return nextInternalDate; else return Math.min(nextInternalDate, nextExternalDate);
    }

    /**
	 * @param nextInternalDate The next internal transition date to set.
	 */
    public void setNextInternalDate(long nextInternalDate) {
        this.nextInternalDate = nextInternalDate;
    }

    /**
	 * @return Returns the next internal influence.
	 */
    public InternalInfluence getNextInternalInfluence() {
        return nextInternalInfluence;
    }

    /**
	 * @param nextInternalInfluence The internalInfluence to set.
	 */
    public void setNextInternalInfluence(InternalInfluence nextInternalInfluence) {
        this.nextInternalInfluence = nextInternalInfluence;
    }

    /**
	 * Clears the internal influence.
	 */
    public void releaseNextInternalInfluence() {
        if (nextInternalInfluence != null) {
            nextInternalInfluence.release();
            nextInternalInfluence = null;
        }
    }

    /**
	 * @return Returns the next date for external transition.
	 */
    public long getNextExternalDate() {
        return nextExternalDate;
    }

    /**
	 * @param nextExternalDate The next external transition date to set.
	 */
    public void setNextExternalDate(long nextExternalDate) {
        this.nextExternalDate = nextExternalDate;
    }

    /**
	 * @return Returns the next external influences.
	 */
    public Collection<ExternalInfluence> getNextExternalInfluences() {
        return nextExternalInfluences;
    }

    /**
	 * @param nextExternalInfluence The external influence to add.
	 */
    public void addNextExternalInfluence(ExternalInfluence nextExternalInfluence) {
        nextExternalInfluences.add(nextExternalInfluence);
    }

    /**
	 * Releases the external influences.
	 */
    public void releaseNextExternalInfluences() {
        for (ExternalInfluence externalInfluence : nextExternalInfluences) externalInfluence.release();
        nextExternalInfluences.clear();
    }

    /**
	 * @return The entity the simulator is associated with.
	 */
    public Entity getEntity() {
        return entity;
    }

    /**
	 * @return The ratio between the global time grain and the local time grain.
	 */
    protected long getRatio() {
        return entity.getRatio();
    }

    /**
	 * @param origin The origin to set.
	 */
    public void setOrigin(long origin) {
        this.origin = origin;
    }

    /**
	 * The origin in the coordinator time.
	 * @return Returns the origin date.
	 */
    public long getOrigin() {
        return origin;
    }

    /**
	 * @return Returns the localTime.
	 */
    public long getLocalTime() {
        return localTime;
    }

    /**
	 * @return Returns the simulation global time.
	 */
    public long getGlobalTime() {
        return coordinator.getGlobalTime();
    }

    /**
	 * @return Returns whether the coordinator is checking.
	 */
    public boolean isChecking() {
        return coordinator.isChecking();
    }

    /**
	 * @return Returns the outgoing logical influences.
	 */
    public Collection<LogicalInfluence> getNextLogicalInfluences() {
        return nextLogicalInfluences;
    }

    /**
	 * @return Returns the outgoing reply influences.
	 */
    public Collection<ReplyInfluence> getReplyInfluences() {
        return entity.getReplyInfluences();
    }

    /**
	 * Adds an influence to the set of simultaneous incoming influences.
	 * @param influence The influence to add.
	 */
    public void addNextLogicalInfluence(LogicalInfluence influence) {
        nextLogicalInfluences.add(influence);
    }

    /**
	 * Clears the bag of incoming influences.
	 */
    public void releaseNextLogicalInfluences() {
        for (LogicalInfluence influence : nextLogicalInfluences) influence.release();
        nextLogicalInfluences.clear();
    }

    /**
	 * Clears the bag of reply influences.
	 */
    public void releaseReplyInfluences() {
        for (ReplyInfluence influence : entity.getReplyInfluences()) influence.release();
    }

    /**
	 * @see mimosa.util.LinkedListElement#getNext()
	 */
    public Simulator getNext() {
        return next;
    }

    /**
	 * @see mimosa.util.LinkedListElement#setNext(mimosa.util.LinkedListElement)
	 */
    public void setNext(Simulator next) {
        this.next = next;
    }

    /**
	 * @see mimosa.util.LinkedListElement#getPrevious()
	 */
    public Simulator getPrevious() {
        return previous;
    }

    /**
	 * @see mimosa.util.LinkedListElement#setPrevious(mimosa.util.LinkedListElement)
	 */
    public void setPrevious(Simulator previous) {
        this.previous = previous;
    }

    public Pair<Collection<StructuralInfluence>, Collection<LogicalInfluence>> initialize(long origin) throws EntityException {
        this.origin = origin;
        localTime = 0;
        return entity.initialize();
    }

    /** 
	 * Called by the coordinator for spontaneous behavior. 
	 * @param eventTime the local time at which the internal transition occurs
	 * @throws EntityException
	 */
    public void executeInternalTransition(long eventTime) throws EntityException {
        long duration = eventTime - localTime;
        entity.internalTransition(duration);
        localTime = eventTime;
    }

    /**
	 * Called by the coordinator for reactive behavior. 
	 * @param eventTime the local time at which the external transition occurs
	 * @throws EntityException
	 */
    public void executeExternalTransition(long eventTime) throws EntityException {
        long duration = eventTime - localTime;
        entity.externalTransition(duration);
        localTime = eventTime;
    }

    /**
	 * Called by the coordinator for logical behavior. 
	 * @param eventTime the local time at which the external transition occurs
	 * @throws EntityException
	 */
    public void executeLogicalTransition(long eventTime) throws EntityException {
        long duration = eventTime - localTime;
        entity.logicalTransition(duration);
    }

    /**
	 * Called by the central scheduler for confluent reaction behavior. 
	 * @throws EntityException 
	 */
    public void executeConfluentInfluence(long eventTime) throws EntityException {
        long duration = eventTime - localTime;
        entity.confluentTransition(duration);
        localTime = eventTime;
    }

    /**
	 * @return the next internal influence to be produced.
	 * @throws EntityException 
	 */
    public InternalInfluence getInternal() throws EntityException {
        InternalInfluence influence = entity.getInternal();
        if (influence != null) {
            influence.setLocalTime(influence.getDuration() + localTime);
        }
        return influence;
    }

    /**
	 * @return the next logical influences to be produced.
	 * @throws EntityException 
	 */
    public Collection<LogicalInfluence> getLogical() throws EntityException {
        Collection<LogicalInfluence> logicalInfluences = entity.getLogical();
        return logicalInfluences;
    }

    /**
	 * Gets the collection of external influences generated before an internal
	 * transition occurs.
	 * @param eventTime the local time at which the method is called.
	 * @return the next external influences to be produced.
	 * @throws EntityException 
	 */
    public Collection<ExternalInfluence> getExternal(long eventTime) throws EntityException {
        Collection<ExternalInfluence> externalInfluences = entity.getExternal(eventTime - localTime);
        return externalInfluences;
    }

    /**
	 * @return the next logical influences to be produced.
	 * @throws EntityException 
	 */
    public Collection<StructuralInfluence> getStructural() throws EntityException {
        Collection<StructuralInfluence> structuralInfluences = entity.getStructural();
        return structuralInfluences;
    }

    public Collection<Entity> getPortLinks(Port name) {
        return entity.getPortLinks(name);
    }

    /**
	 * Requests the entity to actually issue its cached probes.
	 * @param globalTime The global time of the coordinator.
	 */
    public void sendProbes(long globalTime) {
        entity.sendProbes(globalTime, localTime);
    }

    /**
	 * The method to call for adding the created entity to the system.
	 * @param source The creating entity.
	 * @param behaviour The behavior name.
	 * @param parameters The map of parameter/value pairs.
	 */
    public Entity createEntity(Entity source, String behaviour, Map<String, Object> parameters) throws EntityException {
        return coordinator.createEntity(source, behaviour, parameters);
    }

    /**
	 * The method to call for adding the created entity to the system.
	 * @param source The creating entity.
	 * @param name The name of the entity.
	 * @param behaviour The behavior name.
	 * @param parameters The map of parameter/value pairs.
	 */
    public Entity createEntity(Entity source, String name, String behaviour, Map<String, Object> parameters) throws EntityException {
        return coordinator.createEntity(source, name, behaviour, parameters);
    }

    /**
	 * Requests the coordinator to disappear.
	 */
    public void die() {
        coordinator.removeEntity(entity);
    }

    /**
	 * Requests the coordinator to stop the simulation.
	 */
    public void stop() {
        coordinator.requestStop();
    }

    /**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(Simulator simulator) {
        if (simulator == null) throw new NullPointerException();
        if (getNextDate() == INFINITY) if (simulator.getNextDate() == INFINITY) return 0; else return 1; else if (simulator.getNextDate() == INFINITY) return -1; else return (int) (getNextDate() - simulator.getNextDate());
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return "simulator(" + entity + ")";
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Simulator) return getNextDate() == ((Simulator) obj).getNextDate(); else return false;
    }
}
