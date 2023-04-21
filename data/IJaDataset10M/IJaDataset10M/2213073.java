package edu.gatech.cc.jcrasher.plans.stmt;

import static edu.gatech.cc.jcrasher.Assertions.check;
import static edu.gatech.cc.jcrasher.Assertions.notNull;
import java.lang.reflect.InvocationTargetException;
import edu.gatech.cc.jcrasher.plans.expr.Expression;
import edu.gatech.cc.jcrasher.plans.expr.Variable;

/**
 * Hides a code statement to generate a needed instance.
 * 
 * <p>
 * We need only need the following subset:
 * <code>
 * LocalVariableDeclarationStatement
 *     ::= Type VariableDeclaratorId = VariableInitializer;
 * </code>
 * 
 * <p>
 * Each reference type parameter of each method must be non-null.
 * Each method returns a non-null value.
 * 
 * @param <V> type of the wrapped variable.
 * 
 * @author csallner@gatech.edu (Christoph Csallner)
 * http://java.sun.com/docs/books/jls/third_edition/html/statements.html#14.4
 */
public class LocalVariableDeclarationStatement<V> implements BlockStatement<V> {

    protected Variable<V> var = null;

    protected Expression<? extends V> varInitPlan = null;

    /**
   * Constructor
   */
    public LocalVariableDeclarationStatement(Variable<V> pID, Expression<? extends V> plan) {
        notNull(pID);
        notNull(plan);
        final Class<V> idType = pID.getReturnType();
        final Class<? extends V> planType = plan.getReturnType();
        check(idType.isAssignableFrom(planType) || idType.isPrimitive());
        var = pID;
        varInitPlan = plan;
    }

    /**
   * @return variable being assigned to.
   */
    public Variable<V> getVariable() {
        return var;
    }

    /**
   * @return true.
   */
    public V execute() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        V value = varInitPlan.execute();
        var.assign(value);
        return value;
    }

    /**
   * @return a specialized representation of the statement, for example:
   * <ul>
   * <li>A a = new A(null);
   * <li>B b = a.m(0);
   */
    public String text() {
        return var.textDeclaration() + " = " + varInitPlan.text() + ";";
    }

    /**
   * @return a representative example
   */
    @Override
    public String toString() {
        return text();
    }
}
