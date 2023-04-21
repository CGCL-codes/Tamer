package org.libreplan.web.costcategories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.libreplan.business.common.IntegrationEntity;
import org.libreplan.business.common.entities.EntityNameEnum;
import org.libreplan.business.costcategories.entities.CostCategory;
import org.libreplan.business.costcategories.entities.ResourcesCostCategoryAssignment;
import org.libreplan.business.resources.daos.IResourceDAO;
import org.libreplan.business.resources.entities.Resource;
import org.libreplan.web.common.IntegrationEntityModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Model for UI operations related to {@link ResourcesCostCategoryAssignment}
 *
 * @author Jacobo Aragunde Perez <jaragunde@igalia.com>
 * @author Diego Pino Garcia <dpino@igalia.com>
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ResourcesCostCategoryAssignmentModel extends IntegrationEntityModel implements IResourcesCostCategoryAssignmentModel {

    private Resource resource;

    private ResourcesCostCategoryAssignment currentAssignment;

    @Autowired
    IResourceDAO resourceDAO;

    private List<ResourcesCostCategoryAssignment> resourceCostCategoryAssignments = new ArrayList<ResourcesCostCategoryAssignment>();

    @Override
    public List<ResourcesCostCategoryAssignment> getCostCategoryAssignments() {
        if (resource != null) {
            loadCostCategoryAssignments();
        }
        return resourceCostCategoryAssignments;
    }

    private void loadCostCategoryAssignments() {
        resourceCostCategoryAssignments.clear();
        resourceCostCategoryAssignments.addAll(resource.getResourcesCostCategoryAssignments());
    }

    @Override
    public void addCostCategory() {
        ResourcesCostCategoryAssignment assignment = ResourcesCostCategoryAssignment.create();
        resource.addResourcesCostCategoryAssignment(assignment);
        this.currentAssignment = assignment;
        this.setDefaultCode();
    }

    @Override
    public void removeCostCategoryAssignment(ResourcesCostCategoryAssignment assignment) {
        resource.removeResourcesCostCategoryAssignment(assignment);
        loadCostCategoryAssignments();
    }

    @Override
    @Transactional(readOnly = true)
    public void setResource(Resource resource) {
        resourceDAO.reattach(resource);
        initializeCostCategoryAssignments(resource.getResourcesCostCategoryAssignments());
        this.resource = resource;
    }

    private void initializeCostCategoryAssignments(Collection<ResourcesCostCategoryAssignment> resourceCostCategoryAssignments) {
        for (ResourcesCostCategoryAssignment each : resourceCostCategoryAssignments) {
            initializeCostCategoryAssignment(each);
        }
    }

    private void initializeCostCategoryAssignment(ResourcesCostCategoryAssignment resourceCostCategoryAssignment) {
        resourceCostCategoryAssignment.getEndDate();
        initializeCostCategory(resourceCostCategoryAssignment.getCostCategory());
    }

    private void initializeCostCategory(CostCategory costCategory) {
        costCategory.getName();
    }

    @Override
    protected Set<IntegrationEntity> getChildren() {
        return new HashSet<IntegrationEntity>();
    }

    @Override
    public IntegrationEntity getCurrentEntity() {
        return currentAssignment;
    }

    @Override
    public EntityNameEnum getEntityName() {
        return EntityNameEnum.RESOURCE_COST_CATEGORY_ASSIGNMENT;
    }
}
