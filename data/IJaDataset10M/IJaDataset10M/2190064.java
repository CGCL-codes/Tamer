package org.slasoi.monitoring.city.database.functionalExprOperatorParametricTemplate;

import uk.ac.city.soi.database.EntityManagerInterface;

/**
 * This interface provides methods for managing the FunctionalExprOperatorParametricTemplate entity
 * 
 */
public interface FunctionalExprOperatorParametricTemplateEntityManagerInterface extends EntityManagerInterface {

    /**
     * Inserts a Functional Expression Operator, its parametric template and conditions pointer into table.
     * 
     * @param qosTerm -
     *            the functional expression operator in general
     * @param parametricTemplate -
     *            the parametric template of the operators
     * @param conditionsPointer -
     *            the conditions pointer
     */
    public void insert(String functionalExprOperator, String parametricTemplate, String conditionsPointer);

    /**
     * Retrieves the parametric template for a given operator.
     * 
     * @param functionalExprOperator
     * @return parametric template
     */
    public String getTemplate(String functionalExprOperator);

    /**
     * Get the conditions for a given operator
     * 
     * @param functionalExprOperator
     * @return parametric template
     */
    public String getConditionsPointer(String functionalExprOperator);
}
