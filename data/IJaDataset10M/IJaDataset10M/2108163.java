package org.dcopolis.problem;

public class VariableRemovedEvent extends ConstraintChangeEvent {

    private static final long serialVersionUID = -2169007356418135074L;

    Variable variable;

    public VariableRemovedEvent(Constraint constraint, Variable variable) {
        super(constraint);
        this.variable = variable;
        variables.add(variable);
    }

    public Variable getVariable() {
        return variable;
    }

    public void applyChange(DynDisCOP problem, ProblemChangeListener... ignoreListeners) {
        for (Constraint c : problem.getConstraints()) if (c.equals(constraint)) ((DynamicConstraint) c).removeVariable(variable, ignoreListeners);
    }

    @Override
    public String toString() {
        return "VariableRemovedEvent(Constraint = " + constraint + ", Variable = " + variable + ")";
    }
}
