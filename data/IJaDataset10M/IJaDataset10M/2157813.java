package com.daffodilwoods.daffodildb.server.sql99.dql.plan;

import com.daffodilwoods.daffodildb.client.*;
import com.daffodilwoods.daffodildb.server.datasystem.utility._Record;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.listenerevents.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.plan.condition.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.plan.order.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.plan.table.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.sqlinitiator.*;
import java.util.*;

/**
 * It represents the plan for tables present in select query. It is required in
 * formation of query plan for optimal execution.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public interface _TablePlan {

    /**
    * Returns the type of the plan. It is needed in merging of different table
    * plans.
    * @return type of table plan. It can be SingleTable, Qualified, NestedLoop.
    * @throws DException
    */
    public int getType() throws DException;

    /**
   * This method returns the estimated number of rows results after the execution
   * of this plan.
   * @param serverSession
   * @return
   * @throws DException
   */
    public long getRowCount(_ServerSession serverSession) throws DException;

    /**
   * The cost relateds methods return the cost of the plan based on the passed
   * argument as well as the attributes of the plan itself. These attributes
   * includes - Order and Condition. And getCostWithOutOrder methods don't take
   * care of order while calculating cost. These are required to choose the best
   * among different possible combination of plan execution like left plan seeks
   * in right, right plan seeks in left, No seek etc. based on the join condition.
   * @param session
   * @return
   * @throws DException
   */
    public double getCost(_ServerSession session) throws DException;

    public double getCost(_ServerSession session, booleanvalueexpression condition, long estimatedRows) throws DException;

    public double getCostWithoutOrder(_ServerSession session) throws DException;

    public double getCostWithoutOrder(_ServerSession session, booleanvalueexpression condition, long estimatedRowCount) throws DException;

    /**
   * The execute methods executes the plan and return the appropriate resultset.
   * The way to get the resultset is decided in getCost methods and that way is
   * cached in the plan. Based on that way the plan get executed.
   * It was done because, firstly we determine which path is best say LeftSeekRight,
   * RightSeekLeft or NoSeek then we actually execute according to optimal decision.
   * @param session
   * @return
   * @throws DException
   */
    public _Iterator execute(_ServerSession session) throws DException;

    public _Iterator execute(_ServerSession session, booleanvalueexpression condition) throws DException;

    public _Iterator executeWithOutOrder(_ServerSession session, booleanvalueexpression condition) throws DException;

    public _Iterator executeWithoutOrder(_ServerSession session) throws DException;

    /**
   * This method returns the plan of those tables on which condition or order is
   * shifted.
   * @return
   * @throws DException
   */
    public _SingleTablePlan[] getSingleTablePlans() throws DException;

    /**
   * This method remove the cover of NonExecutable Plans like OrderSequencePlan
   * and TemporaryMerge. And also returns a single Plan if there are array of
   * Plans. This is required to give final plan to execute.
   * @return
   * @throws DException
   */
    public _TablePlan joinMissingLink() throws DException;

    /**
   * Returns the order which will be applied at the top level of resultset.
   * @return
   * @throws DException
   */
    public _Order getOrder() throws DException;

    /**
   * This method returns estimated rows based on the passed join condition and
   * estimated rows of other plan. It is needed to calculate the cost of join
   * of other plan with this plan so as to choose optimal path of execution.
   * @param conditionArg represents join condition.
   * @param estimatedRowCount of other plan.
   * @param serverSession
   * @return
   * @throws DException
   */
    public long getEstimatedRows(booleanvalueexpression conditionArg, long estimatedRowCount, _ServerSession serverSession) throws DException;

    /**
   * These methods returns the TableDetails involved in Plan. getTableDetails
   * returns all the TableDetails including the View's underlying TableDetails
   * while getViewTableDetails return the TableDetails of top query level, means
   * if any view is specified only tableDetails of that view will be included.
   * These methods are required in formation of final plan.
   * @return
   * @throws DException
   */
    public TableDetails[] getViewTableDetails() throws DException;

    public TableDetails[] getTableDetails() throws DException;

    /**
   * These methods are required to check the whole tree of the plans.
   * @return
   * @throws DException
   */
    public String getVerifier() throws DException;

    public _QueryPlan getQueryPlan() throws DException;

    /**
   * It is required when both tables of join condition belongs to same plan.
   * This method merges the passed joinCondition at the appropriate place in
   * the tree of Plan.
   * @param joinRelation
   * @throws DException
   */
    public void merge(_JoinRelation joinRelation) throws DException;

    /**
   * This method tells whether the passed tableDetails exist in this plan or not.
   * It is required in merging of join condition among different plans.
   * @param tableDetails0
   * @return
   * @throws DException
   */
    public boolean ifExists(TableDetails tableDetails0) throws DException;

    public _TablePlan[] getChildTablePlans() throws DException;

    /**
   * This method is required to check whether OrderSequencePlan is present in
   * hierarchy of tree. It is needed while merging of join conditions among
   * different table plans.
   * @return
   * @throws DException
   */
    public boolean containsOrderSequencePlan() throws DException;
}
