package net.sf.persistant.metadata;

import static net.sf.persistant.util.CollectionUtils.toMap;
import net.sf.persistant.util.Extractor;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * <code>EntityMetadataImpl</code> is the default implementation of the {@link EntityMetadata} interface.
 * </p>
 */
public class EntityMetadataImpl implements EntityMetadata {

    /**
     * <p>
     * The entity class.
     * </p>
     */
    private final Class<?> m_entityClass;

    /**
     * <p>
     * The entity name.
     * </p>
     */
    private final String m_entityName;

    /**
     * <p>
     * The identifier class. 
     * </p>
     */
    private final Class<?> m_identifierClass;

    /**
     * <p>
     * The property metadata map.
     * </p>
     */
    private final Map<String, PropertyMetadata> m_propertyMetadata;

    /**
     * <p>
     * Construct a {@link EntityMetadata} instance.
     * </p>
     *
     * @param entityName the entity name.
     * @param entityClass the entity class.
     * @param identifierClass the identifier class.
     * @param propertyMetadata the property metadata.
     */
    public EntityMetadataImpl(final String entityName, final Class<?> entityClass, final Class<?> identifierClass, final Set<PropertyMetadata> propertyMetadata) {
        super();
        assert null != entityClass : "The [entityClass] argument cannot be null.";
        assert null != entityName : "The [entityName] argument cannot be null.";
        assert null != identifierClass : "The [identifierClass] argument cannot be null.";
        assert null != propertyMetadata : "The [propertyMetadata] argument cannot be null.";
        m_entityClass = entityClass;
        m_entityName = entityName;
        m_identifierClass = identifierClass;
        m_propertyMetadata = toMap(propertyMetadata, new Extractor<String, PropertyMetadata>() {

            public String extract(final PropertyMetadata object) {
                return object.getName();
            }
        });
    }

    public Class<?> getEntityClass() {
        return m_entityClass;
    }

    public String getEntityName() {
        return m_entityName;
    }

    public Class<?> getIdentifierClass() {
        return m_identifierClass;
    }

    public Collection<PropertyMetadata> getProperties() {
        return m_propertyMetadata.values();
    }

    public Iterator<String> getPropertyNames() {
        return m_propertyMetadata.keySet().iterator();
    }

    public PropertyMetadata getProperty(final String name) throws IllegalArgumentException {
        if (!m_propertyMetadata.containsKey(name)) {
            throw new IllegalArgumentException("Property [" + name + "] does not exist on entity [" + m_entityName + "].");
        }
        return m_propertyMetadata.get(name);
    }
}
