package org.granite.openjpa;

import java.util.Map;
import javax.persistence.Entity;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.granite.logging.Logger;
import org.granite.messaging.amf.io.util.DefaultClassGetter;

/**
 * @author Franck WOLFF
 */
public class OpenJpaClassGetter extends DefaultClassGetter {

    public static final Logger log = Logger.getLogger(OpenJpaClassGetter.class);

    @Override
    public Class<?> getClass(Object o) {
        return super.getClass(o);
    }

    @Override
    public boolean isEntity(Object o) {
        return o.getClass().isAnnotationPresent(Entity.class);
    }

    @Override
    public boolean isInitialized(Object owner, String propertyName, Object propertyValue) {
        if (propertyValue != null || !(owner instanceof PersistenceCapable)) return true;
        Map<String, Boolean> loadedState = OpenJpaExternalizer.getLoadedState((PersistenceCapable) owner, getClass(owner));
        return loadedState.containsKey(propertyName) && loadedState.get(propertyName);
    }

    @Override
    public void initialize(Object owner, String propertyName, Object propertyValue) {
    }
}
