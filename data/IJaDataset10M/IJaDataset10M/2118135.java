package org.hip.vif.bom;

import java.sql.SQLException;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.exc.VException;

/**
 * This interface defines the behaviour of the QuestionHierarchy domain object.
 * It is used to describe the hierarchy and dependencies of questions.
 * 
 * @author: Benno Luthiger
 */
public interface QuestionHierarchy extends DomainObject {

    /**
	 * Returns the question associated with this QuestionHiearchy entry.
	 * 
	 * @return Question
	 * @throws VException
	 * @throws SQLException
	 */
    public Question getAssociatedQuestion() throws VException, SQLException;
}
