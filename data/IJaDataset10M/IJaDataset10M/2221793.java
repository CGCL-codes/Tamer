package net.sf.orcc.ir;

import org.eclipse.emf.common.util.EList;

/**
 * This interface defines a Call instruction, which possibly stores the result
 * to a local variable.
 * 
 * @author Matthieu Wipliez
 * @model extends="net.sf.orcc.ir.Instruction"
 */
public interface InstCall extends Instruction {

    /**
	 * Returns the parameters of this call instruction.
	 * 
	 * @return the parameters of this call instruction
	 * @model containment="true"
	 */
    EList<Arg> getParameters();

    /**
	 * Returns the procedure referenced by this call instruction.
	 * 
	 * @return the procedure referenced by this call instruction
	 * @model
	 */
    Procedure getProcedure();

    /**
	 * Returns the target of this call (may be <code>null</code>).
	 * 
	 * @return the target of this node (may be <code>null</code>)
	 * @model containment="true"
	 */
    Def getTarget();

    /**
	 * Returns <code>true</code> if this call has a result.
	 * 
	 * @return <code>true</code> if this call has a result
	 */
    boolean hasResult();

    /**
	 * Returns <code>true</code> if this call is a call to the built-in "print"
	 * procedure.
	 * 
	 * @return <code>true</code> if this call is a call to the built-in "print"
	 *         procedure
	 */
    boolean isPrint();

    /**
	 * Sets the procedure referenced by this call instruction.
	 * 
	 * @param procedure
	 *            a procedure
	 */
    void setProcedure(Procedure procedure);

    /**
	 * Sets the target of this call instruction.
	 * 
	 * @param target
	 *            a local variable (may be <code>null</code>)
	 */
    void setTarget(Def target);
}
