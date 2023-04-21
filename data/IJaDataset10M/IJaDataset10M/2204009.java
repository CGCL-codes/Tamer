package org.arastreju.core.modelling.access;

import java.util.List;
import org.arastreju.api.modelling.accessors.ProperNameStrategy;
import org.arastreju.api.ontology.model.Association;
import org.arastreju.api.ontology.model.EntityAccess;
import org.arastreju.api.ontology.model.sn.SNEntity;

/**
 * Provides access to an entity.
 * 
 * Created: 17.02.2009
 *
 * @author Oliver Tigges
 */
public class EntityAccessImpl implements EntityAccess {

    private ProperNameStrategy nameStrategy;

    private SNEntity entity;

    public EntityAccessImpl(SNEntity entity, ProperNameStrategy nameStrategy) {
        this.entity = entity;
        this.nameStrategy = nameStrategy;
    }

    public String getFullname() {
        return nameStrategy.getFullName(entity);
    }

    public List<Association> getOrderedNameAssocs() {
        return nameStrategy.getOrderedNameAssocs(entity);
    }
}
