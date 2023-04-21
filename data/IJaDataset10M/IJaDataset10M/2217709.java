package org.libreplan.web.orders.criterionrequirements;

/**
 *
 * @author Diego Pino Garcia <dpino@igalia.com>
 *
 */
public class OrderElementCriterionRequirementComponent extends CriterionRequirementComponent {

    private AssignedCriterionRequirementToOrderElementController controller = new AssignedCriterionRequirementToOrderElementController();

    @Override
    public AssignedCriterionRequirementToOrderElementController getController() {
        return controller;
    }
}
