package com.google.gdata.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gdata.model.ElementCreatorImpl.AttributeInfo;
import com.google.gdata.model.ElementCreatorImpl.ElementInfo;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A builder for the {@link AdaptationRegistry} class, this generates the
 * union maps of attributes and child elements. The {@link #create} method is
 * used to create an immutable adaptation registry that handles the runtime
 * information.
 *
 * 
 */
class AdaptationRegistryFactory {

    private static final Logger logger = Logger.getLogger(AdaptationRegistryFactory.class.getName());

    /**
   * Constructs a new adaptation registry from the given transform.
   */
    static AdaptationRegistry create(MetadataRegistry registry, ElementTransform transform) {
        return new AdaptationRegistry(transform.adaptations, unionAttributes(registry, transform), unionElements(registry, transform));
    }

    /**
   * Create a union of the base attribute map + any adaptor attribute maps.
   * The result is a map from attribute names to the most appropriate
   * adaptation and attribute key for that name.  This map will contain only
   * attributes that are not defined in the base transform and have the same
   * datatype in all adaptations in which they appear.
   *
   * <p>If we find multiple incompatible attributes of the same name, we log a
   * warning and leave them out of the union map.  This means that during
   * parsing the incompatible attributes will be parsed as if they were
   * undeclared, and later adapted to the correct datatype during resolution.
   */
    private static Map<QName, AttributeKey<?>> unionAttributes(MetadataRegistry registry, ElementTransform transform) {
        Map<QName, AttributeKey<?>> union = Maps.newLinkedHashMap();
        Set<QName> base = getAttributeNames(transform);
        Set<QName> invalid = Sets.newHashSet();
        for (ElementKey<?, ?> adaptorKey : transform.adaptations.values()) {
            ElementTransform adaptor = registry.getTransform(null, adaptorKey, null);
            if (adaptor == null) {
                throw new IllegalStateException("Invalid adaptor key " + adaptorKey);
            }
            for (AttributeInfo info : adaptor.attributes.values()) {
                AttributeKey<?> key = info.key;
                QName id = key.getId();
                if (base.contains(id)) {
                    continue;
                }
                if (invalid.contains(id)) {
                    continue;
                }
                AttributeKey<?> existing = union.get(id);
                if (existing != null) {
                    if (!checkCompatible(existing, key)) {
                        union.remove(id);
                        invalid.add(id);
                    }
                } else {
                    union.put(id, key);
                }
            }
        }
        return ImmutableMap.copyOf(union);
    }

    /**
   * Gets the set of qnames of attributes defined in the given transform.
   */
    private static Set<QName> getAttributeNames(ElementTransform transform) {
        Set<QName> result = Sets.newHashSet();
        for (AttributeInfo info : transform.attributes.values()) {
            result.add(info.key.getId());
        }
        return result;
    }

    /**
   * Create a union of the base element map + any adaptor element maps.
   * The result is a map from element names to the most appropriate
   * adaptation and element key for that name.  This map will contain only
   * elements that are not defined in the base transform and have the same
   * datatype and compatible element types in all adaptations in which they
   * appear. Two element types are compatible if one is assignable to the other.
   *
   * <p>If we find multiple incompatible elements of the same name, we log a
   * warning and leave them out of the union map.  This means that during
   * parsing the incompatible elements will be parsed as if they were
   * undeclared, and later adapted to the correct type during resolution.
   */
    private static Map<QName, ElementKey<?, ?>> unionElements(MetadataRegistry registry, ElementTransform transform) {
        Map<QName, ElementKey<?, ?>> union = Maps.newLinkedHashMap();
        Set<QName> invalid = Sets.newHashSet();
        Set<QName> base = getElementNames(transform);
        for (ElementKey<?, ?> adaptorKey : transform.adaptations.values()) {
            ElementTransform adaptor = registry.getTransform(null, adaptorKey, null);
            for (ElementInfo info : adaptor.elements.values()) {
                ElementKey<?, ?> key = info.key;
                QName id = key.getId();
                if (base.contains(id)) {
                    continue;
                }
                if (invalid.contains(id)) {
                    continue;
                }
                ElementKey<?, ?> existing = union.get(id);
                ElementKey<?, ?> compatible = key;
                if (existing != null) {
                    compatible = checkCompatibleElements(existing, key);
                }
                if (compatible == null) {
                    union.remove(id);
                    invalid.add(id);
                } else if (compatible == key) {
                    union.put(id, key);
                }
            }
        }
        return ImmutableMap.copyOf(union);
    }

    /**
   * Gets the set of qnames of child elements defined in the given transform.
   */
    private static Set<QName> getElementNames(ElementTransform transform) {
        Set<QName> result = Sets.newHashSet();
        for (ElementInfo info : transform.elements.values()) {
            result.add(info.key.getId());
        }
        return result;
    }

    /**
   * Checks that two metadata keys are compatible.  The datatypes of the
   * two metadata keys must be identical for them to be considered compatible.
   */
    private static boolean checkCompatible(MetadataKey<?> first, MetadataKey<?> second) {
        boolean compatible = true;
        Class<?> firstType = first.getDatatype();
        Class<?> secondType = second.getDatatype();
        if (firstType != secondType) {
            logger.warning("Incompatible datatypes.  First(" + first + "): " + firstType + " but Second(" + second + "): " + secondType);
            compatible = false;
        }
        return compatible;
    }

    /**
   * Checks that two element keys are compatible.  This checks that the element
   * types are compatible and the datatypes are the same. If the keys are not
   * compatible we log a warning explaining why and return {@code null}. If the
   * element types of the two elements are not the same, but one is a supertype
   * of the other, we use the supertype, so we parse into the most general type
   * first and later adapt to a more specific type if necessary.
   */
    private static ElementKey<?, ?> checkCompatibleElements(ElementKey<?, ?> first, ElementKey<?, ?> second) {
        ElementKey<?, ?> match = first;
        boolean compatible = true;
        if (!checkCompatible(first, second)) {
            compatible = false;
        }
        Class<? extends Element> firstType = first.getElementType();
        Class<? extends Element> secondType = second.getElementType();
        if (firstType != secondType && !firstType.isAssignableFrom(secondType)) {
            if (secondType.isAssignableFrom(firstType)) {
                match = second;
            } else {
                logger.warning("Incompatible element types." + " First(" + first + "): " + firstType + " but Second(" + second + "): " + secondType);
                compatible = false;
            }
        }
        return compatible ? match : null;
    }

    private AdaptationRegistryFactory() {
    }
}
