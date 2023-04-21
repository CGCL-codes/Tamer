package org.hip.vif.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import org.hip.kernel.exc.VException;
import org.hip.kernel.workflow.WorkflowException;

/**
 * Interface for a conditional node processor.
 * The action on the node is done only when the node meets the per-condition.
 * 
 * @author Benno Luthiger
 * Created on Apr 16, 2005
 */
public interface INodeCheckedProcessor {

    /**
	 * Checks the pre-condition for the action to be processed
	 * on the node (i.e. question) with the given ID.
	 * 
	 * @param inQuestionID BigDecimal
	 * @return true, if the pre-condition is met.
	 */
    boolean checkPreCondition(BigDecimal inQuestionID);

    /**
	 * Process the node with the given ID.
	 * 
	 * @param inQuestionID BigDecimal
	 * @throws SQLException
	 * @throws VException
	 * @throws WorkflowException
	 */
    void doAction(BigDecimal inQuestionID) throws WorkflowException, VException, SQLException;
}
