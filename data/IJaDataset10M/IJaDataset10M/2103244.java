package net.sf.orcc.backends.instructions;

import net.sf.orcc.ir.Expression;
import org.eclipse.emf.common.util.EList;

/**
 * This interface defines a specific instruction that sets the address of a RAM.
 * 
 * @author Matthieu Wipliez
 * @model extends="net.sf.orcc.backends.instructions.InstRam"
 */
public interface InstRamSetAddress extends InstRam {

    /**
	 * Returns the list of indexes of this instruction.
	 * 
	 * @return the list of indexes of this instruction
	 * @model containment="true"
	 */
    EList<Expression> getIndexes();
}
