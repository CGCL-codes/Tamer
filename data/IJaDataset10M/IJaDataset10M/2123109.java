package org.nomicron.suber.event;

import org.nomicron.suber.exception.SuberException;
import org.nomicron.suber.model.bean.EventObjectType;
import org.nomicron.suber.model.object.SuberObject;
import java.util.Map;

/**
 * Event for intializing SuberObjects created in the spring configs.
 */
public class SuberObjectInitializationEvent extends AbstractSuberEvent {

    /**
     * Execute the event.
     *
     * @param objectMap map of objects for the event to use
     * @throws Exception
     */
    public void execute(Map<EventObjectType, Object> objectMap) throws Exception {
        log.info("Initializing SuberObjects from spring.");
        Map<String, SuberObject> suberObjects = getApplicationContext().getBeansOfType(SuberObject.class);
        for (String key : suberObjects.keySet()) {
            SuberObject suberObject = suberObjects.get(key);
            if (suberObject.getInitialize()) {
                String objectKey = suberObject.getObjectKey();
                if (objectKey == null) {
                    throw new SuberException("Object key not specified for initialized bean " + key);
                }
                SuberObject createdObject = getMetaFactory().getSuberFactory().getSuberObjectByClassAndObjectKey(suberObject.getClass(), objectKey);
                if (createdObject == null) {
                    log.info("Creating SuberObject: " + key);
                    suberObject.save();
                }
            }
        }
        log.info("SuberObject initialization complete.");
    }
}
