package org.libreplan.business.resources.daos;

import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.libreplan.business.common.daos.IIntegrationEntityDAO;
import org.libreplan.business.common.exceptions.InstanceNotFoundException;
import org.libreplan.business.resources.entities.CriterionType;
import org.libreplan.business.resources.entities.PredefinedCriterionTypes;
import org.libreplan.business.resources.entities.ResourceEnum;

/**
 * DAO for {@link CriterionTypeDAO} <br />
 * @author Diego Pino Garcia <dpino@igalia.com>
 * @author Fernando Bellas Permuy <fbellas@udc.es>
 */
public interface ICriterionTypeDAO extends IIntegrationEntityDAO<CriterionType> {

    CriterionType findUniqueByName(String name) throws InstanceNotFoundException;

    CriterionType findUniqueByNameAnotherTransaction(String name) throws InstanceNotFoundException;

    CriterionType findUniqueByName(CriterionType criterionType) throws InstanceNotFoundException;

    CriterionType findByName(String name);

    public boolean existsOtherCriterionTypeByName(CriterionType criterionType);

    boolean existsByNameAnotherTransaction(CriterionType criterionType);

    boolean hasDiferentTypeSaved(Long id, ResourceEnum resource);

    public void removeByName(CriterionType criterionType);

    List<CriterionType> getCriterionTypes();

    List<CriterionType> getCriterionTypesByResources(Collection<ResourceEnum> resources);

    List<CriterionType> getSortedCriterionTypes();

    /**
     * Checks if exists the equivalent {@link CriterionType} on the DB for a
     * {@link CriterionType} created from a {@link PredefinedCriterionTypes}
     *
     * @param criterionType
     * @return
     */
    boolean existsPredefinedType(CriterionType criterionType);

    /**
     * Checks if exists any {@link Criteria} of the {@link CriterionType} has
     * been assigned to any @ Resource}
     * @param criterionType
     * @return
     */
    boolean checkChildrenAssignedToAnyResource(CriterionType criterionType);

    /**
     * Searches for the equivalent {@link CriterionType} on the DB for a
     * CriterionType created from a {@link PredefinedCriterionTypes}
     * @param predefinedCriterionType
     * @return <code>null</code> if there is no {@link CriterionType} for the
     *         predefinedCriterionType. Otherwise the equivalent
     *         {@link CriterionType} on DB
     */
    CriterionType findPredefined(CriterionType predefinedCriterionType);
}
