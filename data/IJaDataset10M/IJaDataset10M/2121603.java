package edu.gatech.cc.cnc.plans.stmt;

import static edu.gatech.cc.jcrasher.Assertions.notNull;
import java.lang.reflect.Method;
import edu.gatech.cc.jcrasher.plans.JavaCode;
import edu.gatech.cc.jcrasher.plans.expr.Expression;
import edu.gatech.cc.jcrasher.plans.expr.FunctionCall;
import edu.gatech.cc.jcrasher.plans.expr.MethodCall;
import edu.gatech.cc.jcrasher.plans.expr.Variable;
import edu.gatech.cc.jcrasher.plans.stmt.Block;
import edu.gatech.cc.jcrasher.plans.stmt.BlockStatement;
import edu.gatech.cc.jcrasher.plans.stmt.ExpressionStatement;
import edu.gatech.cc.jcrasher.plans.stmt.LocalVariableDeclarationStatement;

/**
 * Sequence of statements terminated by a method call statement.
 * 
 * @param <T> type of value returned by wrapped method.
 * 
 * @author csallner@gatech.edu (Christoph Csallner)
 */
public class MethodCallingBlockStatementSequence<T> extends AbstractBlockStatementSequence<T> {

    /**
	 * Calling a static method on parameters created in plans.
	 * Supports CompundPlan in plans.
	 * 
	 * @return block containing statements to call pMeth with curPlans.
	 */
    public MethodCallingBlockStatementSequence(Class<?> testeeType, BlockStatement<?>[] plans, Block<?> enclosingBlock, Method method) {
        super(enclosingBlock);
        notNull(plans);
        notNull(method);
        init(plans);
        FunctionCall<T> methPlan = new MethodCall<T>(testeeType, method, localNames);
        BlockStatement<?> methStmt = null;
        if (method.getReturnType().equals(Void.TYPE)) methStmt = new ExpressionStatement(methPlan); else {
            this.name = enclosingBlock.getNextID((Class<T>) method.getReturnType());
            methStmt = new LocalVariableDeclarationStatement<T>(name, methPlan);
        }
        super.statements.add(methStmt);
    }

    /**
	 * Calling an instance method on parameters created in plans.
	 * Supports CompundPlan in plans.
	 * 
	 * @return block containing statements to call pMeth with curPlans.
	 */
    public MethodCallingBlockStatementSequence(Class<?> testeeType, BlockStatement<?>[] plans, Block<?> enclosingBlock, Method method, JavaCode<?> instance) {
        super(enclosingBlock);
        notNull(plans);
        notNull(method);
        notNull(instance);
        init(plans);
        Variable<?> instanceName = null;
        if (instance instanceof Expression) instanceName = addPlan((Expression) instance);
        if (instance instanceof BlockStatementSequence) instanceName = addPlan((BlockStatementSequence) instance);
        if (instance instanceof LocalVariableDeclarationStatement) instanceName = addPlan((LocalVariableDeclarationStatement) instance);
        setReceiver(testeeType, method, instanceName);
    }

    /**
   * Sets receiver of method call.
   */
    protected void setReceiver(Class<?> testeeType, Method method, Variable<?> instanceName) {
        FunctionCall<T> methPlan = new MethodCall<T>(testeeType, method, localNames, instanceName);
        BlockStatement methStmt = null;
        if (method.getReturnType().equals(Void.TYPE)) {
            methStmt = new ExpressionStatement(methPlan);
        } else {
            this.name = enclosingBlock.getNextID((Class<T>) method.getReturnType());
            methStmt = new LocalVariableDeclarationStatement(this.name, methPlan);
        }
        super.statements.add(methStmt);
    }
}
