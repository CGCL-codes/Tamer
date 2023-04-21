package fulmine.model.component;

import fulmine.IAddressable;
import fulmine.IDescriptor;
import fulmine.ILifeCycle;
import fulmine.event.IEvent;
import fulmine.event.IEventSource;
import fulmine.event.listener.IEventListener;
import fulmine.protocol.wire.IWireState;

/**
 * An interface for a component object. This is the highest abstraction in the
 * data model framework. The data model framework can represent data entities
 * and their properties. A component is either an entity or a property of an
 * entity.
 * <p>
 * A component has an identity, a type and belongs to a domain. The identity
 * uniquely identifies the entity or entity property the component represents.
 * When a component represents an entity, the type provides a partitioning
 * mechanism for the logical characteristics of the entity and the domain
 * expresses the different modes the entity may exist in. These are abstract
 * concepts; concrete implementations choose how type and domain is interpreted.
 * <p>
 * When a component represents a property of an entity, the type represents the
 * class of the property of the entity and the domain is left unspecified.
 * <p>
 * A component can read its state from and write its state to a 'wire format'.
 * In its simplest form, a component can serialise a single property.
 * <p>
 * A component also participates in the event framework, it is an
 * {@link IEventSource} and can raise {@link IEvent} objects that are received
 * by registered {@link IEventListener} instances.
 * 
 * @author Ramon Servadei
 */
public interface IComponent extends IWireState, IEventSource, IEvent, ILifeCycle, IAddressable, IDescriptor {
}
