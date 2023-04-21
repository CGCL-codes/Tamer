package ca.ucalgary.jazzconnect;

import com.ibm.team.process.common.IIteration;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.model.AttributeOperation;

public interface IQueryBuilder {

    public Expression workItemByID(AttributeOperation op, int id) throws TeamRepositoryException;

    public Expression workItemBySummary(AttributeOperation op, String text) throws TeamRepositoryException;

    public Expression workItemByIteration(AttributeOperation op, IIteration iteration) throws TeamRepositoryException;

    public Expression workItemByState(AttributeOperation op, State state) throws TeamRepositoryException;

    public Expression workItemByAPID(AttributeOperation op, long id) throws TeamRepositoryException;
}
