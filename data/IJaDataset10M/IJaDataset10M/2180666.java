package fulmine.model.container;

import fulmine.IType;
import fulmine.context.IFrameworkContext;
import fulmine.event.IEvent;
import fulmine.event.IEventFrame;
import fulmine.event.listener.IEventListener;
import fulmine.model.component.IComponent;
import fulmine.model.container.IContainerFactory.IContainerBuilder;
import fulmine.model.container.subscription.remote.IRemoteSubscribable;
import fulmine.model.field.BooleanField;
import fulmine.model.field.DoubleField;
import fulmine.model.field.FloatField;
import fulmine.model.field.IField;
import fulmine.model.field.IntegerField;
import fulmine.model.field.LongField;
import fulmine.model.field.StringField;
import fulmine.model.field.containerdefinition.IContainerDefinitionField;

/**
 * A container represents an entity with multiple properties. The properties
 * make up the state of the entity. The properties are represented by
 * {@link IField} objects. The {@link IField} implementations are for scalar
 * primitive types only. Each property is addressed by its identity (a string).
 * Non-scalar properties can be simulated by using identities with suitable
 * namespacing.
 * <p>
 * As each field changes, it notifies its container with the {@link IEvent} that
 * encapsulates the change. These events are marshaled into an event frame by
 * the container.
 * <p>
 * A container may be static or dynamic; static container types have an
 * immutable {@link IField} population. The definition of the fields is held in
 * a {@link IContainerDefinitionField} that will be keyed against the
 * container's type. If the field definition changes for the container type, it
 * must be changed in <b>all</b> contexts. If not, then the container type will
 * not be read correctly by receiving contexts that have an incorrect static
 * definition for the type.
 * <p>
 * Dynamic containers have a variable (mutable) population with no constraints
 * of the container's type.
 * 
 * @see IComponent
 * @see IContainerDefinitionField
 * @see IContainerBuilder
 * @author Ramon Servadei
 * 
 */
public interface IContainer extends IComponent, IEventFrame, IRemoteSubscribable {

    /**
     * The enumeration of data states for an {@link IContainer}.
     * <p>
     * There are 2 data states
     * <ul>
     * <li>LIVE - in this state, the data of the container is being updated (in
     * real-time) and can be considered trustworthy.
     * <li>STALE - in this state, the data of the container is not being updated
     * and should be considered untrustable.
     * </ul>
     * A container is automatically 'live' when it is created and automatically
     * 'stale' when it is destroyed. Application logic may also change the data
     * state.
     * <p>
     * The data state is orthogonal to the life-cycle state of the container.
     * 
     * @author Ramon Servadei
     */
    public enum DataState {

        /**
         * Enumerates the live data state. The data in the container is
         * up-to-date and trustworthy.
         * 
         * @see IContainer#getDataState
         */
        LIVE, /**
         * Enumerates the stale data state. The data in the container is not
         * being updated and is not trustworthy.
         * 
         * @see IContainer#getDataState
         */
        STALE;

        /**
         * Find the enum from its ordinal
         * 
         * @param ordinal
         *            the ordinal
         * @return the enum for the ordinal
         * @throws IllegalArgumentException
         *             if the ordinal does not match
         */
        public static DataState fromOrdinal(int ordinal) {
            final DataState[] values = values();
            for (DataState dataState : values) {
                if (dataState.ordinal() == ordinal) {
                    return dataState;
                }
            }
            throw new IllegalArgumentException("Unknown ordinal: " + ordinal);
        }
    }

    /**
     * Add the field to this container. Any {@link IEvent} events raised by the
     * field will reach this container via its {@link IEventFrame} interface.
     * 
     * @param field
     *            the field to add to this container.
     */
    void add(IField field);

    /**
     * Remove the field from this container. Any events raised by the field will
     * no longer reach this container.
     * 
     * @param field
     *            the field to remove from this container.
     * @return the removed field
     */
    <T extends IField> T remove(T field);

    /**
     * Find the field in the container with the identity equal to the string
     * argument.
     * 
     * @param identity
     *            the string identity of the field to find
     * @return the field in the container with the same identity or
     *         <code>null</code> if not found.
     */
    <T extends IField> T get(String identity);

    /**
     * Get all the identities of the fields in this container.
     * 
     * @return an array of the string identities of all the {@link IField}
     *         objects in this container.
     */
    String[] getComponentIdentities();

    /**
     * A life-cycle method of a container to initialise all resources. Failure
     * to call this method results in a container with an unusable state.
     * Implementations may throw a {@link RuntimeException} if a container is
     * accessed without being initialised.
     * <p>
     * The proper way to retrieve a container is via the
     * {@link ContainerFactory#createContainer(String, int)}. This will
     * automatically invoke this method.
     * <p>
     * Idempotent operation.
     * 
     * @see #destroy()
     */
    void start();

    /**
     * This is a life-cycle method of a container to release all resources and
     * perform cleaning up prior to being made redundant.
     * <p>
     * Idempotent operation.
     * 
     * @see #start()
     */
    void destroy();

    /**
     * Set the container state. The state is a logical business state and is not
     * connected to the object lifecycle state. The business state indicates if
     * the data is trustworthy.
     * 
     * @param dataState
     *            the new business state.
     * @see DataState for a description of the states.
     */
    void setState(DataState dataState);

    /**
     * Determine the number of {@link IField} instances held in this container
     * 
     * @return the number of {@link IField} instances held in this container
     */
    int size();

    /**
     * Get the container's data state. This represents the if the data is being
     * updated and is trustworthy.
     * 
     * @return the current data state
     * @see DataState DataState for a description of the states.
     */
    DataState getDataState();

    /**
     * Get the type of the container. A container type represents a family of
     * related container instances. This is synonymous with a class in an object
     * oriented language.
     * 
     * @return a byte describing the type of container.
     */
    IType getType();

    /**
     * Determines if this container empty.
     * 
     * @return <code>true</code> if there are no fields in this container.
     */
    boolean isEmpty();

    /**
     * Determines if there is an {@link IField} instance with an identity equal
     * to the key argument.
     * 
     * @param key
     *            the identity of a field to find in this container.
     * @return <code>true</code> if there is an {@link IField} with this
     *         identity held in this.
     */
    boolean contains(String key);

    /**
     * Identify if the container is dynamic or static. Static container types
     * have an immutable {@link IField} population. Dynamic containers have a
     * variable (mutable) population.
     * 
     * @return <code>true</code> if the definition is dynamic
     */
    boolean isDynamic();

    /**
     * Is the container local to the host context returned by
     * {@link #getContext()}
     * 
     * @return <code>true</code> if the container is local
     */
    boolean isLocal();

    /**
     * Get the host context for the container instance. This may not be the
     * native context for the container.
     * 
     * @see #isLocal()
     * @see #getNativeContextIdentity()
     * @return the {@link IFrameworkContext} hosting this instance
     */
    IFrameworkContext getContext();

    /**
     * Get the {@link BooleanField} identified by the identity
     * 
     * @param identity
     *            the identity of the field to get
     * @return the {@link BooleanField} for the identity, <code>null</code> if
     *         not found
     * @throws ClassCastException
     *             if the field is not of this type
     */
    public BooleanField getBooleanField(String identity);

    /**
     * Get the {@link StringField} identified by the identity
     * 
     * @param identity
     *            the identity of the field to get
     * @return the {@link StringField} for the identity, <code>null</code> if
     *         not found
     * @throws ClassCastException
     *             if the field is not of this type
     */
    public StringField getStringField(String identity);

    /**
     * Get the {@link IntegerField} identified by the identity
     * 
     * @param identity
     *            the identity of the field to get
     * @return the {@link IntegerField} for the identity, <code>null</code> if
     *         not found
     * @throws ClassCastException
     *             if the field is not of this type
     */
    public IntegerField getIntegerField(String identity);

    /**
     * Get the {@link LongField} identified by the identity
     * 
     * @param identity
     *            the identity of the field to get
     * @return the {@link LongField} for the identity, <code>null</code> if not
     *         found
     * @throws ClassCastException
     *             if the field is not of this type
     */
    public LongField getLongField(String identity);

    /**
     * Get the {@link FloatField} identified by the identity
     * 
     * @param identity
     *            the identity of the field to get
     * @return the {@link FloatField} for the identity, <code>null</code> if not
     *         found
     * @throws ClassCastException
     *             if the field is not of this type
     */
    public FloatField getFloatField(String identity);

    /**
     * Get the {@link DoubleField} identified by the identity
     * 
     * @param identity
     *            the identity of the field to get
     * @return the {@link DoubleField} for the identity, <code>null</code> if
     *         not found
     * @throws ClassCastException
     *             if the field is not of this type
     */
    public DoubleField getDoubleField(String identity);

    /**
     * Get the identity of the context this container is native to; this is its
     * local context identity. If this container is local to the host context,
     * this is the same as the identity of {@link #getContext()}. If this
     * container is a remote container, this will return the remote context
     * identity.
     * <p>
     * Note: this is more for informational purposes, the true mechanism for
     * determining whether a container is local to the host context or not is by
     * checking {@link #isLocal()}.
     * 
     * @see #isLocal()
     * @see #getContext()
     * @return the identity of this container's local context
     */
    String getNativeContextIdentity();

    /**
     * Determine if this instance is the original or a clone. Any
     * {@link IEventListener} instances processing {@link IContainer} instances
     * as {@link IEvent} instances will receive the container instance as a
     * <b>clone</b>. In this way, the container changes are broadcast as a
     * logically immutable instance.
     * 
     * @return <code>true</code> if this is a clone of the original
     */
    boolean isClone();
}
