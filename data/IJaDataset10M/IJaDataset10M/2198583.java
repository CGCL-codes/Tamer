package org.skycastle.core.old;

import org.skycastle.core.old.acting.Action;
import org.skycastle.core.old.acting.ActionFacade;
import org.skycastle.core.old.acting.ActionFacadeImpl;
import org.skycastle.core.old.property.PropertyFacade;
import org.skycastle.core.old.property.PropertyFacadeImpl;
import org.skycastle.old.messaging.Message;
import org.skycastle.old.messaging.MessageListener;
import org.skycastle.old.messaging.modifications.action.InvokeActionMessage;
import org.skycastle.old.messaging.updates.UpdateMessage;
import org.skycastle.util.ParameterChecker;
import org.skycastle.util.parameters.metadata.ParameterMetadata;
import org.skycastle.util.parameters.metadata.ParameterValidationException;
import org.skycastle.util.parameters.metadata.validators.ParameterValidator;
import java.io.Serializable;
import java.util.Set;

public class DefaultGameObject implements GameObject {

    private final PropertyFacade myPropertyFacade;

    private final ActionFacadeImpl myActionFacade;

    private final MessagingFacade myMessagingFacade;

    private GameObjectId myId = null;

    private static final long serialVersionUID = 1L;

    /**
     * The constructor is protected, as GameObjects should only be created by the {@link GameObjectContext}s.
     */
    @SuppressWarnings({ "ThisEscapedInObjectConstruction" })
    protected DefaultGameObject() {
        myPropertyFacade = new PropertyFacadeImpl(this);
        myActionFacade = new ActionFacadeImpl(this);
        myMessagingFacade = new MessagingFacadeImpl(this);
    }

    public final void startAction(final InvokeActionMessage invokeActionMessage) {
        myActionFacade.startAction(invokeActionMessage);
    }

    public final long startAction(final GameObjectId callerId, final String actionIdentifier) {
        return myActionFacade.startAction(callerId, actionIdentifier);
    }

    public final long startAction(final GameObjectId callerId, final String actionIdentifier, final Object... parameters) {
        return myActionFacade.startAction(callerId, actionIdentifier, parameters);
    }

    public final void stopAction(final String actionIdentifier, final long actId) {
        myActionFacade.stopAction(actionIdentifier, actId);
    }

    public final ActionMetadata getActionMetadata(final String actionIdentifier) {
        return myActionFacade.getActionMetadata(actionIdentifier);
    }

    public final void addAction(final Action action) {
        myActionFacade.addAction(action);
    }

    public final void removeAction(final String actionIdentifier) {
        myActionFacade.removeAction(actionIdentifier);
    }

    public final boolean hasAction(final String actionIdentifier) {
        return myActionFacade.hasAction(actionIdentifier);
    }

    public final Set<String> getActionIdentifiers() {
        return myActionFacade.getActionIdentifiers();
    }

    public final GameObjectId getId() {
        if (myId == null) {
            throw new IllegalStateException("The ID of this object has not been initialized during construction");
        }
        return myId;
    }

    public final void onMessage(final Message message) {
        myMessagingFacade.onMessage(message);
    }

    public final void addIndirectUpdateListener(final UpdateListenerFilter filter, final GameObjectId observerId) {
        myMessagingFacade.addIndirectUpdateListener(filter, observerId);
    }

    public final void removeIndirectUpdateListener(final UpdateListenerFilter filter, final GameObjectId observerId) {
        myMessagingFacade.removeIndirectUpdateListener(filter, observerId);
    }

    public final void addDirectUpdateListener(final UpdateListenerFilter filter, final MessageListener listener) {
        myMessagingFacade.addDirectUpdateListener(filter, listener);
    }

    public final void removeDirectUpdateListener(final UpdateListenerFilter filter, final MessageListener messageListener) {
        myMessagingFacade.removeDirectUpdateListener(filter, messageListener);
    }

    public final void sendUpdateToObservers(final UpdateMessage updateMessage) {
        myMessagingFacade.sendUpdateToObservers(updateMessage);
    }

    public final <T> T getPropertyValue(final String propertyIdentifier, final T defaultValue) {
        return myPropertyFacade.getPropertyValue(propertyIdentifier, defaultValue);
    }

    public final void setPropertyValue(final String propertyIdentifier, final Serializable value) throws ParameterValidationException {
        myPropertyFacade.setPropertyValue(propertyIdentifier, value);
    }

    public final boolean hasProperty(final String propertyIdentifier) {
        return myPropertyFacade.hasProperty(propertyIdentifier);
    }

    public final Set<String> getPropertyIdentifiers() {
        return myPropertyFacade.getPropertyIdentifiers();
    }

    public final ParameterMetadata getPropertyMetadata(final String propertyIdentifier) {
        return myPropertyFacade.getPropertyMetadata(propertyIdentifier);
    }

    public final void addProperty(final String propertyIdentifier, final Serializable initialValue) throws ParameterValidationException {
        myPropertyFacade.addProperty(propertyIdentifier, initialValue);
    }

    public final void addProperty(final String propertyIdentifier, final Serializable initialValue, final ParameterMetadata propertyMetadata) throws ParameterValidationException {
        myPropertyFacade.addProperty(propertyIdentifier, initialValue, propertyMetadata);
    }

    public final void addProperty(final String propertyIdentifier, final Serializable initialValue, final String description, final ParameterValidator... validators) throws ParameterValidationException {
        myPropertyFacade.addProperty(propertyIdentifier, initialValue, description, validators);
    }

    public final <T extends Serializable> void addProperty(final String propertyIdentifier, final T initialValue, final Class<T> type, final String description, final ParameterValidator... validators) throws ParameterValidationException {
        myPropertyFacade.addProperty(propertyIdentifier, initialValue, type, description, validators);
    }

    public final void removeProperty(final String propertyIdentifier) throws ParameterValidationException {
        myPropertyFacade.removeProperty(propertyIdentifier);
    }

    public final void addPropertyElement(final String propertyIdentifier, final Serializable collectionPropertyItem) throws ParameterValidationException {
        myPropertyFacade.addPropertyElement(propertyIdentifier, collectionPropertyItem);
    }

    public final void removePropertyElement(final String propertyIdentifier, final Serializable collectionPropertyItem) throws ParameterValidationException {
        myPropertyFacade.removePropertyElement(propertyIdentifier, collectionPropertyItem);
    }

    public final void addPropertyMapping(final String propertyIdentifier, final Serializable key, final Serializable value) throws ParameterValidationException {
        myPropertyFacade.addPropertyMapping(propertyIdentifier, key, value);
    }

    public final void removePropertyMapping(final String propertyIdentifier, final Serializable key) throws ParameterValidationException {
        myPropertyFacade.removePropertyMapping(propertyIdentifier, key);
    }

    public final void run() throws Exception {
        myActionFacade.runScheduledActs(getGameObjectContext().getCurrentGameTime_ms());
    }

    /**
     * @return an interface that provides property related operations for this {@link GameObject}.
     */
    public final PropertyFacade getPropertyFacade() {
        return myPropertyFacade;
    }

    /**
     * @return an interface that provides action related operations for this {@link GameObject}.
     */
    public final ActionFacade getActionFacade() {
        return myActionFacade;
    }

    /**
     * @return an interface that provides messaging related operations for this {@link GameObject}.
     */
    public final MessagingFacade getMessagingFacade() {
        return myMessagingFacade;
    }

    protected final void setId(final GameObjectId id) {
        ParameterChecker.checkNotNull(id, "id");
        myId = id;
    }

    /**
     * @return a {@link GameObjectContext} that provides environment specific functions.
     */
    protected final GameObjectContext getGameObjectContext() {
        return GameContext.getGameObjectContext();
    }
}
