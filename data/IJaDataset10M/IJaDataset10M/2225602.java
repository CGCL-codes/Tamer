package core.specification.compositionexpression;

import automata.fsa.FiniteStateAutomaton;
import core.specification.Specification;
import core.specification.compositionexpression.extensionpoint.ExtensionPoint;
import core.specification.operator.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Siamak
 * Date: May 12, 2006
 * Time: 5:43:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompositionExpression implements Serializable {

    protected FiniteStateAutomaton baseUseCase;

    protected FiniteStateAutomaton referredUseCase;

    protected FiniteStateAutomaton newUseCase;

    protected Operator operator;

    protected boolean minimal;

    protected boolean affected;

    public CompositionExpression(FiniteStateAutomaton newUseCase, Operator operator, boolean minimal) {
        this.baseUseCase = operator.getBaseUseCase();
        this.referredUseCase = operator.getReferredUseCase();
        this.newUseCase = newUseCase;
        this.operator = operator;
        this.minimal = minimal;
        affected = false;
    }

    public boolean isAffected() {
        return affected;
    }

    public void setAffected(boolean affected) {
        this.affected = affected;
    }

    public void updateUsecases(Specification specification) {
        setBaseUseCase(specification.getUseCaseByID(baseUseCase.getID()));
        setReferredUseCase(specification.getUseCaseByID(referredUseCase.getID()));
        updateRelatedStates();
    }

    /**
     * The method to be overriden in the expression for the forward updating
     * of the extendion point during the recomposition process;
     */
    public void updateRelatedStates() {
        operator.updateRelatedStates();
    }

    /**
     * toString...
     */
    public String toString() {
        String toReturn = newUseCase.getID() + " := " + operator + " (" + baseUseCase.getID() + ", " + referredUseCase.getID() + ") ";
        toReturn = toReturn + operator.toStringExtensionPoints();
        if (minimal) toReturn = toReturn + " - Minimal";
        return toReturn;
    }

    /**       
     * Getters and setters
     */
    public FiniteStateAutomaton getBaseUseCase() {
        return baseUseCase;
    }

    public void setBaseUseCase(FiniteStateAutomaton baseUseCase) {
        this.baseUseCase = baseUseCase;
    }

    public void setNewUseCase(FiniteStateAutomaton newUseCase) {
        this.newUseCase = newUseCase;
    }

    public void setReferredUseCase(FiniteStateAutomaton referredUseCase) {
        this.referredUseCase = referredUseCase;
    }

    public FiniteStateAutomaton getReferredUseCase() {
        return referredUseCase;
    }

    public FiniteStateAutomaton getNewUseCase() {
        return newUseCase;
    }

    public String getNewUseCaseID() {
        return newUseCase.getID();
    }

    public Operator getOperator() {
        return operator;
    }

    public ExtensionPoint getFirstExtensionPoint() {
        return null;
    }

    public boolean getMinimal() {
        return minimal;
    }
}
