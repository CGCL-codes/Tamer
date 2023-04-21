package org.josef.demo.jpa;

import static org.josef.annotations.Status.Stage.PRODUCTION;
import static org.josef.annotations.Status.UnitTests.COMPLETE;
import org.josef.annotations.Review;
import org.josef.annotations.Reviews;
import org.josef.annotations.Status;
import org.josef.jpa.AbstractSearchCriteria;
import org.josef.jpa.RelationalOperator;
import org.josef.jpa.WhereClause;

/**
 * Search criteria to find entities of type {@link Element}.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 2841 $
 */
@Status(stage = PRODUCTION, unitTests = COMPLETE)
@Reviews(@Review(by = "Kees Schotanus", at = "2009-09-18", reason = "Initial review"))
public class ElementSearchCriteria extends AbstractSearchCriteria {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = 6695052545011270181L;

    /**
     * Number of the Element.
     */
    private Long number;

    /**
     * Symbol of the Element.
     */
    private String symbol;

    /**
     * Name of the Element.
     */
    private String name;

    /**
     * The minimum atomic weight of the Element.
     */
    private Double fromWeight;

    /**
     * The maximum atomic weight of the Element.
     */
    private Double toWeight;

    /**
     * Public default constructor to make this a bean.
     */
    public ElementSearchCriteria() {
    }

    /**
     * Gets the number of the Element.
     * @return The number of the Element.
     */
    public Long getNumber() {
        return number;
    }

    /**
     * Sets the number of the Element.
     * @param number The number of the Element.
     */
    public void setNumber(final Long number) {
        this.number = number;
    }

    /**
     * Gets the symbol of the Element.
     * @return The symbol of the Element.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol of the Element.
     * @param symbol The symbol of the Element.
     */
    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the name of the Element.
     * @return The name of the Element.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Element.
     * @param name The name of the Element.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the minimum atomic weight of the Element.
     * @return The minimum atomic weight of the Element.
     */
    public Double getFromWeight() {
        return fromWeight;
    }

    /**
     * Sets the minimum atomic weight of the Element.
     * @param fromWeight The minimum atomic weight of the Element.
     */
    public void setFromWeight(final Double fromWeight) {
        this.fromWeight = fromWeight;
    }

    /**
     * Gets the maximum atomic weight of the Element.
     * @return The maximum atomic weight of the Element.
     */
    public Double getToWeight() {
        return toWeight;
    }

    /**
     * Sets the maximum atomic weight of the Element.
     * @param toWeight The maximum atomic weight of the Element.
     */
    public void setToWeight(final Double toWeight) {
        this.toWeight = toWeight;
    }

    /**
     * Gets the JPQL where-clause.
     * @return The JPQL where-clause.
     */
    public WhereClause getWhereClause() {
        final WhereClause whereClause = new WhereClause();
        whereClause.addRelationalExpression("e.id", RelationalOperator.EQUAL, getNumber());
        whereClause.addRelationalExpression("e.symbol", RelationalOperator.EQUAL, getSymbol());
        whereClause.addStartsWith("e.name", getName());
        whereClause.addBetween("e.weight", getFromWeight(), getToWeight());
        return whereClause;
    }
}
