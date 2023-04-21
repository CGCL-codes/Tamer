package org.hibernate.search.engine;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.search.backend.LuceneWork;
import org.hibernate.search.impl.ConfigContext;

/**
 * Set up and provide a manager for classes which are indexed via {@code @IndexedEmbedded}, but themselves do not
 * contain the {@code @Indexed} annotation.
 *
 * @author Gavin King
 * @author Emmanuel Bernard
 * @author Sylvain Vieujot
 * @author Richard Hallier
 * @author Hardy Ferentschik
 */
public class DocumentBuilderContainedEntity<T> extends AbstractDocumentBuilder<T> implements DocumentBuilder {

    /**
	 * Constructor used on contained entities not annotated with {@code @Indexed} themselves.
	 *
	 * @param xClass The class for which to build a {@code DocumentBuilderContainedEntity}.
	 * @param context Handle to default configuration settings.
	 * @param reflectionManager Reflection manager to use for processing the annotations.
	 * @param optimizationBlackList mutable register, keeps track of types on which we need to disable collection events optimizations
	 */
    public DocumentBuilderContainedEntity(XClass xClass, ConfigContext context, ReflectionManager reflectionManager, Set<XClass> optimizationBlackList) {
        super(xClass, context, null, reflectionManager, optimizationBlackList);
        if (metadata.containedInGetters.size() == 0) {
            this.entityState = EntityState.NON_INDEXABLE;
        }
    }

    protected void documentBuilderSpecificChecks(XProperty member, PropertiesMetadata propertiesMetadata, boolean isRoot, String prefix, ConfigContext context) {
    }

    @Override
    public void addWorkToQueue(Class<T> entityClass, T entity, Serializable id, boolean delete, boolean add, boolean batch, List<LuceneWork> queue) {
    }

    @Override
    public Serializable getId(Object entity) {
        return null;
    }
}
