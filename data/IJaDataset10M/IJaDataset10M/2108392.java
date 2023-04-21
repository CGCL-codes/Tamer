package net.sourceforge.gda.impl.cayenne;

import net.sourceforge.gda.GenericDataObject;
import net.sourceforge.gda.GenericDataObjectProviderConstants;
import net.sourceforge.gda.messaging.GenericMessage;
import net.sourceforge.gda.messaging.GenericMessageQueue;
import net.sourceforge.gda.messaging.GenericMessageQueueFactory;
import net.sourceforge.gda.provider.GenericDataObjectProvider;
import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.PersistenceState;
import java.util.Collection;

/**
 * GenericDataObject adapter.
 *
 * @author Hasani Hunter
 */
public abstract class CayenneDataObjectAdapter extends CayenneDataObject implements GenericDataObject {

    /** The provider that created this object. */
    private transient GenericDataObjectProvider provider = null;

    /**
     * Returns the provider that this object is registered with.
     *
     * @return GenericDataObjectProvider
     * @created 02/15/07
     */
    public GenericDataObjectProvider getProvider() {
        return provider;
    }

    /**
     * Sets the provider.. typically used by the provider or session upon creation.
     *
     * @param provider the provider property to set.
     */
    public void setProvider(final GenericDataObjectProvider provider) {
        this.provider = provider;
    }

    /**
     * Informs the caller if this object is "dirty" e.g has changes that
     * have not been commited to the database.
     */
    public boolean isModified() {
        return (getPersistenceState() == PersistenceState.MODIFIED);
    }

    public Object readProperty(final String propName) {
        Object objProperty = super.readProperty(propName);
        if (objProperty != null && Collection.class.isAssignableFrom(objProperty.getClass())) {
            GenericMessageQueue messageQueue = GenericMessageQueueFactory.findMessageQueueById(GenericDataObjectProviderConstants.OBJECT_RELATIONSHIP_RESOLVED_NOTIFICATION);
            GenericMessage message = GenericMessageQueueFactory.createGenericMessage(GenericDataObjectProviderConstants.OBJECT_RELATIONSHIP_RESOLVED_NOTIFICATION_ID, objProperty, this);
            messageQueue.postMessage(message);
        }
        return objProperty;
    }
}
