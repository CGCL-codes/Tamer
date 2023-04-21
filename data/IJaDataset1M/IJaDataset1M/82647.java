package ee.webAppToolkit.storage.db4o;

import ee.objectCloner.CloneAdvisor;
import ee.webAppToolkit.storage.Identifiable;

public class IdentifiableCloner implements CloneAdvisor {

    @Override
    public CloneAdvise getAdvise(Object source, Object destination) {
        Object sourceId = ((Identifiable) source).getId();
        Object destinationId = ((Identifiable) destination).getId();
        if (sourceId == destinationId) {
            return CloneAdvise.COPY;
        } else if (sourceId == null && destinationId != null) {
            return CloneAdvise.COPY;
        } else if (sourceId != null && destinationId == null) {
            return CloneAdvise.USE_SOURCE;
        } else {
            return CloneAdvise.USE_SOURCE;
        }
    }

    @Override
    public CloneAdvise getAdvise(Object source) {
        return CloneAdvise.USE_SOURCE;
    }

    @Override
    public boolean canAdvise(Class<?> sourceType, Class<?> destinationType) {
        return Identifiable.class.isAssignableFrom(sourceType) && Identifiable.class.isAssignableFrom(destinationType);
    }

    @Override
    public boolean canAdvise(Class<?> sourceType) {
        return Identifiable.class.isAssignableFrom(sourceType);
    }
}
