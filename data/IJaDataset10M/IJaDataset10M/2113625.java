package de.uni_leipzig.lots.server.persist.hibernate;

import de.uni_leipzig.lots.common.objects.Entity;
import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Kiel
 * @version $Id: EntityNameResolver.java,v 1.4 2007/10/23 06:30:04 mai99bxd Exp $
 */
interface EntityNameResolver<T extends Entity> {

    /**
     * Returns the class of the entity. This is The class which is returned by the repository.
     *
     * @return the class of the entity
     */
    Class<T> getEntityClass();

    /**
     * If the mapped hibernate class is different from the {@link #getEntityClass() entity class}, this method
     * returns the mapped class.
     *
     * @return mapped hibernate class
     */
    Class<? extends T> getHibernateMappedClass();

    @NotNull
    String getEntityName(@NotNull Element entity);
}
