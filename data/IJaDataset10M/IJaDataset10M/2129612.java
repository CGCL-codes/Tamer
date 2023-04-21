package net.sf.javailp;

public interface Formulation {

    public Constraint add(Constraint constraint);

    public Variable add(Variable variable);

    public void add(Problem problem);

    public Constraint add(Expression lhs, Operator operator, Expression rhs);

    public Constraint add(Expression lhs, Operator operator, Number rhs);

    public Constraint add(Number lhs, Operator operator, Expression rhs);

    public Constraint add(Expression lhs, String operator, Expression rhs);

    public Constraint add(Expression lhs, String operator, Number rhs);

    public Constraint add(Number lhs, String operator, Expression rhs);

    public Variable addVar(Number lower, Object variable, Number upper, Class<?> type);

    public Variable addVar(Object variable, Class<?> type);

    public Variable addVar(Number lower, Object variable, Class<?> type);

    public Variable addVar(Object variable, Number upper, Class<?> type);

    public void setObjective(Expression expression, OptType type);

    public void setObjective(Expression expression);

    public void setMinObjective(Expression expression);

    public void setMaxObjective(Expression expression);

    public void setMinObjective();

    public void setMaxObjective();
}
