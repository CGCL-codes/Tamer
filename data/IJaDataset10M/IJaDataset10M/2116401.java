package com.daffodilwoods.daffodildb.server.sql99.dql.plan.condition;

import java.util.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.predicates.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.rowvalueexpression.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.database.resource.*;

/**
 * This class represents predicate which involve more than one column of type
 * reference and solved by index known as joinpredicate.e.g A=B. It is required
 * for appropriate merging of different predicates.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class JoinPredicate extends PredicateAbstract implements predicate {

    /**
   * It represents join condition on which join predicate is made.
   */
    private booleanvalueexpression condition;

    public JoinPredicate(booleanvalueexpression condition0) {
        condition = condition0;
    }

    /**
    * It is required to check whether condition involve is subquery or not.
    * If condition is subquey it return true. else return false.
    * @return
    * @throws DException
    */
    public boolean checkForSubQuery() throws DException {
        return condition.checkForSubQuery();
    }

    /**
    * Note:-following method never been called
    * @param tableDetails
    * @return
    * @throws DException
    */
    public _QualifiedBVE getQualifiedBVE(TableDetails[] tableDetails) throws DException {
        throw new DException("DSE565", new Object[] { "getQualifiedBVE()" });
    }

    public void getTablesIncluded(ArrayList aList) throws DException {
        throw new DException("DSE565", new Object[] { "getTablesIncluded(ArrayList aList)" });
    }

    public void setColumnPredicates(_AllColumnPredicates allColumnPredicates) throws DException {
        throw new DException("DSE565", new Object[] { "execute()" });
    }

    public ParameterInfo[] getParameterInfo() throws DException {
        throw new DException("DSE565", new Object[] { "getParameterInfo()" });
    }

    public com.daffodilwoods.daffodildb.server.sql99.utils._Reference[] checkSemantic(com.daffodilwoods.daffodildb.server.serversystem._ServerSession parent) throws DException {
        throw new DException("DSE565", new Object[] { "checkSemantic(queryspecification parent)" });
    }

    /**
    * Computes Cost of solving predicate for rows passed, boolean is passed to
    * indicate whether predicate is solvable by index or not.
    */
    public double getCost(long rowCount, boolean index) throws DException {
        return condition.getCost(rowCount, index);
    }

    public Object run(Object object) throws DException {
        return condition.run(object);
    }

    public String toString() {
        return condition.toString();
    }

    /**
    * This method used to get condition from predicate.Usage of this method in
    * tableplanmerger during merging of predicate.
    * @return
    * @throws DException
    */
    public booleanvalueexpression getCondition() throws DException {
        return condition;
    }

    /**
    * This method returns all the children of this class. It is needed in
    * Abstract classes, so as to move common methods in Abstract class.
    * @return
    */
    public AbstractRowValueExpression[] getChilds() {
        AbstractRowValueExpression[] childs = new AbstractRowValueExpression[] { (AbstractRowValueExpression) condition };
        return childs;
    }

    public void setDefaultValues(_VariableValueOperations variableValueOperations) throws DException {
    }

    /**
    * This method returns the estimate number of the rows remaining after the execution
    * of this condition on passed number of rows.
    * @param noOfRows
    * @return
    * @throws DException
    */
    public long getEstimatedRows(long noOfRows) throws DException {
        return condition.getEstimatedRows(noOfRows);
    }

    /**
    * This method is used to get type of predicate.
    * @return
    * @throws DException
    */
    public int getPredicateType() throws DException {
        return OperatorConstants.JOINCOMPARISONPREDICATE;
    }
}
