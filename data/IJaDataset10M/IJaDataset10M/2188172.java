package org.objectwiz.representation;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectwiz.Collections;
import org.objectwiz.Collections.CollectionIterator;
import org.objectwiz.PersistenceUnit;
import org.objectwiz.metadata.MappedClass;
import org.objectwiz.metadata.MappedProperty;
import org.objectwiz.util.TypeUtils;

/**
 * Class that handles the mechanism that cascades unsaved entities before
 * saving the root entity.
 *
 * This class is thread-safe.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class CascadeSaveHandler {

    private static final Log logger = LogFactory.getLog(CascadeSaveHandler.class);

    private PersistenceUnit unit;

    public CascadeSaveHandler(PersistenceUnit unit) {
        this.unit = unit;
    }

    /**
     * Performs save (update/persist) operations in cascade on all the inner objects
     * of the given POJO that require it (i.e. they are transient or modified).
     * Then saves the root object.
     *
     * @param er                  The representation used by <code>originalRepresentedEntity</code>.
     * @param representedEntity   The entity in its original representation (necessary to know the scope).
     * @param pojo                The corresponding POJO (upon which update/merge operations have to be performed)
     * @param merge               TRUE if the operation on the root entity shall be a 'merge' operation, FALSE
     *                            for a 'persist' operation.
     * @param inProxy             A {@link ObjectProxy} for resolving incoming references in <code>representedEntity</code>.
     *
     * @return the persisted or merged entity
     */
    public Object cascadeSave(EntityRepresentation er, Object representedEntity, Object pojo, final boolean merge, ObjectProxy inProxy) {
        return cascadeSave(er, representedEntity, pojo, merge, false, null, inProxy);
    }

    /**
     * Recursive inner method of #{@link cascadeSave(Object,Object,boolean,ObjectProxy)}.
     * @param recursiveCall         Indicator whether we are already within a recursive loop or not.
     * @param objectsToUpdate       The list of objects on which to perform an update when all the
     *                              recursive calls are performed.
     */
    private Object cascadeSave(EntityRepresentation er, Object representedEntity, Object pojo, final boolean merge, boolean recursiveCall, List<Object> objectsToUpdate, ObjectProxy inProxy) {
        boolean isDebugEnabled = logger.isDebugEnabled();
        if (isDebugEnabled) logger.debug("Cascading: " + pojo);
        final EntityRepresentation or = EntityRepresentation.POJO;
        final MappedClass mc = or.getMappedClass(unit, pojo, true, true);
        if (objectsToUpdate == null) objectsToUpdate = new ArrayList();
        if (!mc.isEntity()) {
            return pojo;
        }
        boolean isPersisted = or.isPersisted(mc, pojo);
        if (!recursiveCall && (isPersisted != merge)) {
            throw new RuntimeException("Root operation is " + (merge ? "merge" : "persist") + " but the object is " + (isPersisted ? "" : "not ") + "persisted: " + pojo);
        }
        for (MappedProperty property : mc.getMappedProperties()) {
            if (!property.getType().isAssociationType()) continue;
            if (property.isVirtual()) {
                if (isDebugEnabled) logger.debug("Processing virtual property assignment: " + property.getFullName());
                MappedProperty oppositeProperty = property.getType().getAssociationType().getAssociatedProperty();
                if (isDebugEnabled) logger.debug("Opposite property is: " + oppositeProperty.getFullName());
                Object[] targetValues = er.extractPojoValues(representedEntity, property, unit, inProxy.getReferenceResolver());
                for (Object oppositePojo : targetValues) {
                    if (logger.isDebugEnabled()) logger.debug("Opposite value is: " + oppositePojo);
                    if (oppositeProperty.getType().isCollection()) {
                        Object collection = or.getValue(oppositeProperty, oppositePojo);
                        Collections.update(collection, false, null, new Object[] { pojo });
                    } else {
                        if (!property.getType().isCollection()) {
                            Object existingValue = unit.getPropertyValues(pojo, new String[] { property.getName() }, null)[0];
                            if (existingValue != null) {
                                or.setValue(oppositeProperty, existingValue, null);
                                objectsToUpdate.add(existingValue);
                            }
                        }
                        or.setValue(oppositeProperty, oppositePojo, pojo);
                    }
                    objectsToUpdate.add(oppositePojo);
                }
                continue;
            }
            if (isDebugEnabled) logger.debug("Checking property '" + property.getFullName() + "' for unsaved objects.");
            Object propertyValueInPojo = or.getValue(property, pojo);
            final Object propertyValueInRequest = er.getValue(property, representedEntity);
            if (propertyValueInPojo == null || propertyValueInRequest == null) continue;
            if (property.getType().isCollection()) {
                CollectionIterator itPojo = Collections.iterator(propertyValueInPojo);
                CollectionIterator itP = Collections.iterator(propertyValueInRequest);
                while (itP.hasNext()) {
                    if (!itPojo.hasNext()) throw new RuntimeException("Collection mismatch");
                    itP.next();
                    itPojo.next();
                    if (property.getType().isKeyedCollection() && er.isRepresented(itP.getKey())) {
                        itPojo.updateKey(cascadeSave(er, itP.getKey(), itPojo.getKey(), merge, true, objectsToUpdate, inProxy));
                    }
                    if (er.isRepresented(itP.getValue())) {
                        itPojo.updateValue(cascadeSave(er, itP.getValue(), itPojo.getValue(), merge, true, objectsToUpdate, inProxy));
                    }
                }
            } else {
                if (er.isReference(propertyValueInRequest)) continue;
                propertyValueInPojo = cascadeSave(er, propertyValueInRequest, propertyValueInPojo, merge, true, objectsToUpdate, inProxy);
            }
            or.setValue(property, pojo, propertyValueInPojo);
        }
        if (isDebugEnabled) logger.debug("Saving object [merge: " + isPersisted + "]");
        pojo = isPersisted ? unit.update(pojo) : unit.persist(pojo);
        if (!recursiveCall) {
            for (Object object : objectsToUpdate) {
                if (isDebugEnabled) logger.debug("Additional merge: " + object);
                unit.update(object);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Object saved: " + TypeUtils.describeBean(pojo));
        }
        return pojo;
    }

    private MappedClass getMappedClass(String className) {
        try {
            return unit.getMetadata().getMappedClass(className, true);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
