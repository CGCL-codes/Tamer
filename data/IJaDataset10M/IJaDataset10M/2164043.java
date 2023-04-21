package com.daffodilwoods.daffodildb.server.sql99.dql.tableexpression;

import java.util.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.common.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.execution.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.listenerevents.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.plan.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.plan.condition.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.plan.order.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.plan.table.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.tableexpression.fromclause.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.tableexpression.groupbyclause.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.tableexpression.havingclause.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.tableexpression.whereclause.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.sqlinitiator.*;
import com.daffodilwoods.database.utility.P;

/**
 * tableexpression represents from,where,group and having clause of a
 * select query. Only from clause is mandatary and remaining all are optional.
 * This class provides the functionality of semantic checking and Execution Plan.
 * It also provides all the information regarding from,where,group and having
 * clause.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class tableexpression extends JoinRelationAbstract implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter, TypeConstants, TableExpressionConstants, SimpleConstants {

    /**
    * Represents having clause of select query. It is optional
    */
    public havingclause _Opthavingclause0;

    /**
    * Represents group by clause of select query. It is optional
    */
    public groupbyclause _Optgroupbyclause1;

    /**
    * Represents where clause of select query. It is optional
    */
    public whereclause _Optwhereclause2;

    /**
    * Represents from clause of select query.
    */
    public fromclause _fromclause3;

    /**
    * Represents array of table plans.
    */
    private _TablePlan[] tablePlans;

    /**
    * Represents the plan of condition represented by from,where and having
    * clauses.
    */
    private _BVEPlan resultantBvePlan;

    /**
    * Represents the mapping of Table, single table plan and execution plan of
    * table.
    */
    private Object[][] tableAndQualifiedPlan;

    /**
    * Represents the plan of order.
    */
    private _OrderPlan orderPlan;

    /**
    * Represents the final table plan of this tableexpression.
    */
    private _TablePlan finalTablePlan;

    /**
    * Represents the remaining condition which will be solved at the top of
    * the plan represented by this tableexpression.
    */
    private booleanvalueexpression remainingCondition;

    /**
    * Represents the tables involved in from clause.
    */
    private TableDetails[] tableDetails;

    /**
    * Represents the tables involved in from clause. It is different from
    * tableDetails in the way if view is present, then it represents all the
    * tables involved in view, whereas tableDetails represents the view as table.
    */
    private TableDetails[] allTableDetails;

    _QueryColumns queryColumns0;

    public tableexpression() {
    }

    public Object run(Object object) throws com.daffodilwoods.database.resource.DException {
        throw new DException("DSE16", new Object[] { "run" });
    }

    /**
    * It is used to merge two arrays in to one.
    * @param param1
    * @param param2
    * @return
    */
    private Object[] addParameters(Object[] param1, Object[] param2) {
        return param1 == null ? param2 : param2 == null ? param1 : mergeParameters(param1, param2);
    }

    private Object[] mergeParameters(Object[] param1, Object[] param2) {
        int len1 = param1.length;
        int len2 = param2.length;
        int len = len1 + len2;
        Object[] result = new Object[len];
        System.arraycopy(param1, 0, result, 0, len1);
        System.arraycopy(param2, 0, result, len1, len2);
        return result;
    }

    /**
    * It allows user to obtain the combined plan of passed condition and the
    * condition represented by where clause.
    * @param condition
    * @return
    * @throws DException
    */
    private _BVEPlan mergeBvePlan(booleanvalueexpression condition) throws DException {
        _BVEPlan bvePlan = null;
        if (condition != null) {
            bvePlan = condition.getExecutionPlan();
        }
        if (_Optwhereclause2 != null) {
            _BVEPlan whereBvePlan = _Optwhereclause2.getExecutionPlan();
            bvePlan = BVEPlanMerger.mergeTablePlansWithAnd(bvePlan, whereBvePlan);
        }
        return bvePlan;
    }

    /**
    * This method allows user to obtain a plan through the help of which user
    * can obtain the resultset. This method firstly makes a plan for condition
    * with the help of passed condition and the condition present in from, where
    * and having clauses. After that plan for sorting needs of query is formed
    * with the help of passed order and order needed for group by clause. And
    * then plan of 'from clause' is obtained with the help of condition and
    * order plan. In this step, single table conditions and single table orders
    * are shifted to single table. After that join conditions are merged among
    * the plans obtained from 'from clause'. And finally a single plan is formed
    * for all the table plans obtained from 'from clause'.
    *
    * Detailed Description is given below.
    *
    * DaffodilDB chooses an execution plan based on the access paths available and assigns
    * a rank to these paths. DaffodilDB always chooses the path with the lower rank over one with the higher rank
    * Method is used to get an Plan which can provide us an Iterator.
    * The Plan which we recieve from the tableexpression class has order and condition merged , ConditionArray and QueryColumns
    * the method with all this arguments is invoked from the tableorQueryname class
    * In this method We take all the Plans from the FromClause and Merges All The single Level Conditions from the BVEPlan ,
    * in these Plans and then we take the OrderPlan From groupBy and OrderBy and merges
    * them . After that we check whether OrderSeqencePlan is possible for the singleTableLevel Orders and
    * depending on the possiblity of OrderSequencePlan we change the tablePlan Array . After we merge the JoinRelations
    * in this tablePlans Array.
    * Finally the JoinMissingLink make a final TablePlan.
    * @param session
    * @param bve              --> singleLevel condition of View
    * @param datedFrameWork
    * @param queryColumns     --> Columns involved in the Query[Used to calculate the cost at singleTablePlan]
    * @param order0           --> SingleTableOrder of the
    * @param conditionArray   --> ConditionArray contains the condition related to this Query ,
    *                            it will be null if it is root level Query , in case of View it Will contain the
    *                            SingleLevel Condition of the View.
    *--------------------------
    *REQUIREMENT OF ARGUMENTS |
    *--------------------------
    *Condition  >>   This are SingleLevel Conditions of View .
    *                For the above view we would like to shift the condition V.Id = 1 to the Orders Table as V.Id column is
    *                actually of Orders.OrderId .Since in this tableExpression/querySpecification we are having the tablePlan
    *                of Orders so we need to pass the information upto this level.
    *ViewOrder  >>     This are SingleLevel Conditions of View .
    *                  For the above view we would like to shift the order on column V.Id to the Orders Table as V.Id column is
    *                  actually of Orders.OrderId .Since in this tableExpression/querySpecification we are having the tablePlan
    *                  of Orders so we need to pass the information to this level.
    *QueryColumns >>   QueryColumns helps to calculate the cost of Plan Generated.As it tells the columns involved in the
    *                  Query.There is always a cost of extracting any column from the Database.This cost calculation work
    *                  is done at SingleTablePlan.
    *ConditionArray >> For Changing the type of joins beacuse of the condition present  in where clause or on clause
    *                  we need to pass the these conditions as they can change their meaning
    *                  for eg Select * from Orders left Outer Join "Order Details" on Orders.OrderId = 11111 where "Orders Details".OrderId > 1
    *                  This Query will act as Select * from Orders , "Order Details" where "Orders Details".OrderId > 1 and Orders.OrderId = 11111
    *                  We need to pass "Orders Details".OrderId > 1 information to qualifiedJoin of Orders left Outer Join "Order Details"
    *                  so that it can change treat as simple Join.
    *                  We could have passed TableDetails of condition only but for the following case
    *                  for eg  Select * from Orders left Outer Join "Order Details" on Orders.OrderId = 11111
    *                          where "Orders Details".OrderId is null
    *                  we need not to change the type as simple Join .
    *                  and if Condition TableDetails had been passed then we would not have any indication that
    *                  the tableDetails were of null Predicate.
    *
    * @return
    * @throws DException
    */
    public _TablePlan getExecutionPlan(_ServerSession session, booleanvalueexpression bve, _DatedFramework datedFrameWork, _QueryColumns queryColumns, _Order order0, ConditionArray conditionArray) throws DException {
        queryColumns0 = queryColumns;
        initializeBvePlan(bve);
        initializeOrderPlan(session, order0, queryColumns);
        initializeConditionArray(conditionArray);
        tablePlans = _fromclause3.getTablePlan(session, datedFrameWork, resultantBvePlan, orderPlan, queryColumns, conditionArray);
        return createTableExpressionPlan(session);
    }

    /**
    * It performs the following steps -
    * 1. It merges the condition of 'from clause' in condition plan.
    * 2. It initializes the mapping of table, single table plan and execution
    * plan of that table.
    * 3. It merges the join conditions among the table plans obtained from 'from
    * clause'.
    * 4. It makes a single plan for all the plans obtained from 'from clause'
    * 5. Finally it makes plan for this tableexpression.
    * @param session
    * @return
    * @throws DException
    */
    private TableExpressionPlan createTableExpressionPlan(_ServerSession session) throws DException {
        mergeFromClauseConditioninResultantBvePlan();
        initializeMapping();
        mergeJoinCondition(session);
        joinMissingLink();
        return new TableExpressionPlan(finalTablePlan, resultantBvePlan, orderPlan, _Optwhereclause2 == null ? null : GeneralPurposeStaticClass.getUnderLyingReferencesOnly(_Optwhereclause2.getReferences(tableDetails), tableDetails), _Opthavingclause0 == null ? null : GeneralPurposeStaticClass.getUnderLyingReferencesOnly(_Opthavingclause0.getReferences(tableDetails), tableDetails));
    }

    /**
    * It adds all the single table conditions and join conditions in passed
    * condition array. It is required to check whether any present qualified
    * join needs to be converted into simple joins.
    * @param conditionArray
    * @throws DException
    */
    private void initializeConditionArray(ConditionArray conditionArray) throws DException {
        if (_Optwhereclause2 != null) {
            if (conditionArray == null) {
                conditionArray = new ConditionArray();
            }
            _BVEPlan bvePlan = _Optwhereclause2.getExecutionPlan();
            _BVESingleTablePlan[] bveSingleTablePlans = bvePlan.getBVESingleTablePlans();
            if (bveSingleTablePlans != null) {
                for (int i = 0; i < bveSingleTablePlans.length; i++) {
                    conditionArray.addCondition(bveSingleTablePlans[i].getCondition());
                }
            }
            _AllTablesJoinRelation allTablesJoinRelation = bvePlan.getAllTableJoinRelation();
            if (allTablesJoinRelation != null) {
                _JoinRelation[] joinRelations = allTablesJoinRelation.getRelations();
                for (int i = 0; i < joinRelations.length; i++) {
                    conditionArray.addCondition(joinRelations[i].getCondition());
                }
            }
        }
    }

    /**
    * It merges the FromClause Conditions in resultantBvePlan. From Clause bve's
    * comes in to existence
    * a) When the inner join is Present
    * b) Qualified Join is converted to simple Join.
    * c) Optimizable View is present
    * @throws DException
    */
    private void mergeFromClauseConditioninResultantBvePlan() throws DException {
        _BVEPlan fromBvePlan = _fromclause3.getBveExecutionPlan();
        resultantBvePlan = BVEPlanMerger.mergeTablePlansWithAnd(resultantBvePlan, fromBvePlan);
    }

    /**
    * It is required to check whether passed single table order plans are
    * same or not. It is required to check the redundancy of sorting needs of
    * group by and order by.
    * @param singleTableOrderPlan1
    * @param singleTableOrderPlan2
    * @return
    * @throws DException
    */
    private boolean checkSequence(_SingleTableOrderPlan[] singleTableOrderPlan1, _SingleTableOrderPlan[] singleTableOrderPlan2) throws DException {
        int count = (singleTableOrderPlan1.length < singleTableOrderPlan2.length) ? singleTableOrderPlan1.length : singleTableOrderPlan2.length;
        for (int i = 0; i < count; i++) {
            if (!compareOrdersAndColumns(singleTableOrderPlan1[i].getOrder(), singleTableOrderPlan2[i].getOrder())) {
                return false;
            }
        }
        return true;
    }

    /**
    * It is required to check whether passed orders are same or not. It is
    * required to check the redundancy of sorting needs of group by and order by.
    * @param order1
    * @param order2
    * @return
    * @throws DException
    */
    private boolean checkSequence(_Order order1, _Order order2) throws DException {
        return (compareOrdersAndColumns(order1, order2));
    }

    /**
    * It is used to check whether columns and orderspecification(asc/desc) of
    * both orders are same or not.
    * @param order1
    * @param order2
    * @return
    * @throws DException
    */
    private boolean compareOrdersAndColumns(_Order order1, _Order order2) throws DException {
        String[] str1 = order1.getColumns();
        String[] str2 = order2.getColumns();
        boolean[] result1 = order1.getOrderOfColumns();
        boolean[] result2 = order2.getOrderOfColumns();
        int length = (str1.length < str2.length) ? str1.length : str2.length;
        for (int i = 0; i < length; i++) {
            if (!str1[i].equalsIgnoreCase(str2[i]) || !(result1[i] == result2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
    * initializeBvePlan method merges the BVEPlan of "where Clause" , "Having ",
    * "Inner Join of On condition" and "View BvePlan"
    * @param bve - bve of the View
    * @return
    * @throws DException
    */
    private void initializeBvePlan(booleanvalueexpression bve) throws DException {
        resultantBvePlan = bve == null ? null : bve.getExecutionPlan();
        resultantBvePlan = _Optwhereclause2 == null ? resultantBvePlan : BVEPlanMerger.mergeTablePlansWithAnd(resultantBvePlan, _Optwhereclause2.getExecutionPlan());
        resultantBvePlan = _Opthavingclause0 == null ? resultantBvePlan : BVEPlanMerger.mergeTablePlansWithAnd(resultantBvePlan, _Opthavingclause0.getExecutionPlan());
        resultantBvePlan = createLayerOfBveAllTablePlan(resultantBvePlan);
    }

    /**
    * This method is used to add the layer of BVEAllTablePlan to passed BvePlan.
    * It is done because we need the join condition of inner join present in
    * fromClause. So this BveAllTablePlan is passed to fromClause so that it can
    * add join conditions to this plan and it will be reflected in this class.
    * we can also have BveSingleTablePlan which will be shifted to singletable,
    * so in these case we need the cover of BveAllTablePlan.
    *
    * @param bvePlanPassed
    * @return
    * @throws DException
    */
    private _BVEPlan createLayerOfBveAllTablePlan(_BVEPlan bvePlanPassed) throws DException {
        if (bvePlanPassed == null) {
            return new BVEAllTablePlan(null, null, null);
        } else if (bvePlanPassed.getType() == BVEConstants.BVESINGLETABLEPLAN) {
            return new BVEAllTablePlan(new _BVESingleTablePlan[] { (_BVESingleTablePlan) bvePlanPassed }, null, null);
        } else if (bvePlanPassed.getType() == BVEConstants.BVEAGGREGATEPLAN) {
            booleanvalueexpression aggregateCondition = bvePlanPassed.getAggregateCondition();
            bvePlanPassed = new BVEAllTablePlan(null, null, null);
            ((BVEAllTablePlan) bvePlanPassed).andAggregateCondition(aggregateCondition);
            return bvePlanPassed;
        }
        return bvePlanPassed;
    }

    /**
    * InitializeMapping Creates an two2DArray whose length is the number of
    * tables involved. The 2DArray Contains the tableName at 0th Position ,
    * SingleTablePlan at 1st position , Execution Plan at 2nd Position.
    * eg A LOJ B on Condition will have the following mapping
    * A  SingleTablePlan[A] QualifiedLeftPlan[AB]
    * B  SingleTablePlan[B] QualifiedLeftPlan[AB]
    * This is used in merging of join condition. SingleTablePlans are used for
    * obtaining the cost of join relation and execution plan are actually used
    * on which join conditions are merged. After merging, resultant plan replaces
    * the execution plan of both tables involved in condition.
    * @throws DException
    */
    private void initializeMapping() throws DException {
        int sizeOfMapping = 0;
        int length = tablePlans.length;
        Object[] singleTblPlans = new Object[length];
        for (int i = 0; i < length; i++) {
            _SingleTablePlan singleTablePlans[] = tablePlans[i].getSingleTablePlans();
            sizeOfMapping += singleTablePlans.length;
            singleTblPlans[i] = singleTablePlans;
        }
        tableAndQualifiedPlan = new Object[sizeOfMapping][3];
        int index = 0;
        for (int i = 0; i < length; i++) {
            _SingleTablePlan singleTablePlans[] = (_SingleTablePlan[]) singleTblPlans[i];
            for (int j = 0; j < singleTablePlans.length; j++) {
                tableAndQualifiedPlan[index][0] = singleTablePlans[j].getTableDetails()[0];
                tableAndQualifiedPlan[index][1] = singleTablePlans[j];
                tableAndQualifiedPlan[index++][2] = tablePlans[i];
            }
        }
    }

    /**
    * MergeJoinCondition sorts the JoinRelation on the Basis of their capability
    * to reduce the rows. Then apporopriately merges the JoinCondition in the
    * tablePlans.
    * @throws DException
    */
    private void mergeJoinCondition(_ServerSession session) throws DException {
        if (resultantBvePlan == null) {
            return;
        }
        _AllTablesJoinRelation allTablesJoinRelation = resultantBvePlan.getAllTableJoinRelation();
        if (allTablesJoinRelation != null) {
            allTablesJoinRelation.sort(tableAndQualifiedPlan, session);
            mergeJoinRelations(allTablesJoinRelation);
        }
    }

    /**
    * Searches the tableName in the tableAndQualifiedPlan mapping and returns
    * the index of that.
    * @param tableName TableName need to be searched.
    * @return
    * @throws DException
    */
    private int getTablePosition(TableDetails table) throws DException {
        int length = tableAndQualifiedPlan.length;
        for (int i = 0; i < length; i++) {
            if (ifExists(((_TablePlan) tableAndQualifiedPlan[i][2]), table)) {
                return i;
            }
        }
        throw new DException("DSE3516", new Object[] { table.getNameOfTable() });
    }

    /**
    * It is used to check whether passed table exists in the passed plan.
    * @param tablePlan
    * @param tableDetail
    * @return
    * @throws DException
    */
    private boolean ifExists(_TablePlan tablePlan, TableDetails tableDetail) throws DException {
        TableDetails[] tds = tablePlan.getTableDetails();
        for (int i = 0; i < tds.length; i++) {
            if (tds[i] == tableDetail) {
                return true;
            }
        }
        return false;
    }

    /**
    * It updates the tableAndQualifiedPlan Mapping's ExecutionPlan at the
    * position specified.
    * @param mappingPositions
    * @param tablePlan
    * @throws DException
    */
    public void updateMapping(int[] mappingPositions, _TablePlan tablePlan) throws DException {
        for (int i = 0; i < mappingPositions.length; i++) {
            tableAndQualifiedPlan[mappingPositions[i]][2] = tablePlan;
        }
        TableDetails[] tableDetails = tablePlan.getViewTableDetails();
        int[] tablePositions = getTablePositionFromMapping(tableDetails);
        for (int i = 0; i < tablePositions.length; i++) {
            tableAndQualifiedPlan[tablePositions[i]][2] = tablePlan;
        }
    }

    /** @todo Changed its arg as TableDetails instead of tableNames */
    private int[] getTablePositionFromMapping(TableDetails[] tableDetails) throws DException {
        int intArray[] = new int[tableAndQualifiedPlan.length];
        int index = 0;
        for (int j = 0; j < tableDetails.length; j++) {
            for (int i = 0; i < tableAndQualifiedPlan.length; i++) {
                if (tableAndQualifiedPlan[i][0] == tableDetails[j]) {
                    intArray[index++] = i;
                    break;
                }
            }
        }
        int[] returnArray = new int[index];
        System.arraycopy(intArray, 0, returnArray, 0, index);
        return returnArray;
    }

    /**
    * This method is used to merge all the join relations represented by
    * AllTablesJoinRelation among the plans obtained from 'from clause'.
    * The steps involved are -
    * 1. Execution plan of both the tables, involved in join condition, are
    * retrieved from mapping.
    * 2. Then a plan is formed after merging join condition for these two plans.
    * 3. Correspondingly update the mapping with this plan. Now both the tables
    * will contain this plan as execution plan in mapping.
    * @param allTablesJoinRelation
    * @throws DException
    */
    private void mergeJoinRelations(_AllTablesJoinRelation allTablesJoinRelation) throws DException {
        _JoinRelation joinRelations[] = allTablesJoinRelation.getRelations();
        int length = joinRelations.length;
        for (int i = 0; i < length; i++) {
            TableDetails[] tableDetails = GeneralPurposeStaticClass.getAllTableDetails(joinRelations[i].getCondition());
            int mappingPosition1 = getTablePosition(tableDetails[0]);
            _TablePlan tablePlan1 = (_TablePlan) tableAndQualifiedPlan[mappingPosition1][2];
            int mappingPosition2 = getTablePosition(tableDetails[1]);
            _TablePlan tablePlan2 = (_TablePlan) tableAndQualifiedPlan[mappingPosition2][2];
            int[] tablePositions = new int[] { mappingPosition1, mappingPosition2 };
            tablePlans = mergingOfJoinRelation(tablePlan1, tablePlan2, joinRelations[i], tableDetails, tablePositions, tablePlans);
        }
    }

    /**
    * It is required when we have to merge join condition in OrderSequenceplan and
    * the tables involved in join condition are not present at consecutive
    * location in OrderSequencePlan. In this case TemporaryMerge plan is made
    * and placed in OrderSequencePlan.
    * @param orderSequencePlan OrderSequencePlan  which needs to be changed
    * @param bve  condition which is to be merged
    * @param index1 Starting index of tablePlan List from which we want to create the TemporaryMerge
    * @param index2 Ending index of tablePlan List from which we want to create the TemporaryMerge
    * @throws DException
    */
    private void prepareTableMergeAndUpdateTablePlans(_TablePlan orderSequencePlan, booleanvalueexpression bve, int index1, int index2) throws DException {
        _TablePlan[] tp = ((OrderSequencePlan) orderSequencePlan).getTablePlans();
        int len = tp.length;
        TemporaryMerge tempMerge = new TemporaryMerge();
        for (int i = index1; i <= index2; i++) {
            tempMerge.addTablePlan(tp[i]);
        }
        tempMerge.addCondition(bve);
        _TablePlan[] newTp = new _TablePlan[tp.length - (index2 - index1)];
        System.arraycopy(tp, 0, newTp, 0, index1);
        newTp[index1] = tempMerge;
        System.arraycopy(tp, index2 + 1, newTp, index1 + 1, tp.length - index2 - 1);
        ((OrderSequencePlan) orderSequencePlan).setPlans(newTp);
    }

    /**
    * It is required when we have to merge join condition in OrderSequenceplan and
    * the tables involved in join condition are present at consecutive
    * location in OrderSequencePlan. In this case TwoTableJoin plan is made
    * and placed in OrderSequencePlan.
    * @param orderSequencePlan OrderSequencePlan whose tablePlan List will be changed
    * @param bve condition which is to merged
    * @param index1 Plan's index of the new twoTableJOinPlan
    * @param index2 Plan's index of the new twoTableJOinPlan
    * @throws DException
    */
    private void prepareTwoTableJoinPlanAndUpdateTablePlans(_TablePlan orderSequencePlan, booleanvalueexpression bve, int index1, int index2) throws DException {
        _TablePlan[] tp = ((OrderSequencePlan) orderSequencePlan).getTablePlans();
        int len = tp.length;
        TwoTableJoinPlan ttJp = new TwoTableJoinPlan(tp[index1], tp[index2], bve);
        _TablePlan[] newTp = new _TablePlan[tp.length - 1];
        System.arraycopy(tp, 0, newTp, 0, index1);
        newTp[index1] = ttJp;
        System.arraycopy(tp, index1 + 1, newTp, index1 + 1, index2 - index1 - 1);
        System.arraycopy(tp, index2 + 1, newTp, index2, newTp.length - index2);
        ((OrderSequencePlan) orderSequencePlan).setPlans(newTp);
    }

    /**
    * Makes the final ExecutablePlan. It removes the cover of OrderSequencePlan
    * and TemporaryMerge, if they are present in plan. Because both these plans
    * are not executable.
    * Eg for Query like Select * from A, B , C where A.id = B.id
    * TablePlans have TTJp[AB] , STP[C]
    * but we need a single Execution Plan so we make a NestedLoopJoinPlan which
    * will do the Cartesian of the underLyingPlans
    * @throws DException
    */
    private void joinMissingLink() throws DException {
        int length = tablePlans.length;
        for (int i = 0; i < length; i++) {
            tablePlans[i] = tablePlans[i].joinMissingLink();
        }
        if (tablePlans.length > 1) {
            finalTablePlan = new NestedLoopJoinPlan(tablePlans);
        } else {
            finalTablePlan = tablePlans[0];
        }
    }

    /**
    * Returns the columns involved in from clause.
    * @return array of columns
    * @throws DException
    */
    public ColumnDetails[] getFromClauseColumnDetails() throws DException {
        return _fromclause3.getColumnDetails();
    }

    /**
    * Returns the columns involved in where clause.
    * @return array of columns
    * @throws DException
    */
    public ColumnDetails[] getWhereClauseColumnDetails() throws DException {
        return _Optwhereclause2 != null ? _Optwhereclause2.getColumnDetails() : null;
    }

    /**
    * Returns the columns involved in group by clause.
    * @return array of columns
    * @throws DException
    */
    public ColumnDetails[] getGroupByColumnDetails() throws DException {
        return _Optgroupbyclause1 != null ? _Optgroupbyclause1.getColumnDetails() : null;
    }

    /**
    * Returns the columns involved in having clause.
    * @return array of columns
    * @throws DException
    */
    public ColumnDetails[] getHavingClauseColumnDetails() throws DException {
        return _Opthavingclause0 != null ? _Opthavingclause0.getColumnDetails() : null;
    }

    /**
    * Returns the string representation of from clause
    * @return
    */
    public String getfromClause() {
        return _fromclause3.toString();
    }

    /**
    * Returns the string representation of group by clause
    * @return
    */
    public String getGroupByClause() {
        return _Optgroupbyclause1 == null ? null : _Optgroupbyclause1.toString();
    }

    /**
    * Returns the string representation of having clause
    * @return
    */
    public String getHavingClause() {
        return _Opthavingclause0 == null ? null : _Opthavingclause0.toString();
    }

    /**
    * Returns the string representation of where clause
    * @return
    */
    public String getWhereClause() {
        return _Optwhereclause2 == null ? null : _Optwhereclause2.toString();
    }

    /**
    * Returns all the tables involved in from clause. If view is present then
    * this view is returned as table.
    * @return
    * @throws DException
    */
    public TableDetails[] getViewTableDetails() throws DException {
        return _fromclause3.getViewTableDetails();
    }

    /**
    * This method is used to get all those columns of 'from clause' which avoids
    * scope management rule of semantic checking.
    * @return
    * @throws DException
    */
    public _Reference[] getUnderlyingReferences() throws DException {
        return _fromclause3.getUnderlyingReferences();
    }

    /**
    * Returns the condition which will be solved on the result of group by and
    * aggregate functions.
    * @return
    * @throws DException
    */
    public booleanvalueexpression getAggregateCondition() throws DException {
        return resultantBvePlan == null ? null : resultantBvePlan.getAggregateCondition();
    }

    /**
    * This method firstly check whether the sorting needs of group by and passed
    * order is similar or not. If similar then sorting is performed only once.
    * otherwise, the order plans of group by and passed order are merged to
    * find the sorting needs of select query.
    * e.g.
    * select a ,b from A group by a,b order by b
    * if b is not a primary key then the order and group by plan will not be
    * mergerd. This method checks for such cases by calling the checkForGroupAndOrderRedundant.
    * @param session
    * @param orderPlan0 OrderPlan of the ViewPlan
    * @throws DException
    */
    private void initializeOrderPlan(_ServerSession session, _Order viewOrder, _QueryColumns queryCols) throws DException {
        orderPlan = checkForGroupAndOrderRedundant(viewOrder, session, queryCols);
        if (orderPlan != null) {
            return;
        }
        if (viewOrder != null) {
            orderPlan = viewOrder.getOrderPlan(session);
        }
        if (_Optgroupbyclause1 != null) {
            _Order groupOrder = _Optgroupbyclause1.getOrder(queryCols);
            orderPlan = getMergedGroupAndOrderPlan(orderPlan, groupOrder.getOrderPlan(session));
        }
    }

    /**
    * This method is used to obtain the resultant sorting needs of select query
    * by merging order plan of group by and passed order. The following cases
    * can arise -
    * GroupByPlan-SingleTableOrder  , Order-SingleTableOrder
    *   1)	Check whether column are same and in same order. If same we make
    *           only one singleTableOrder.
    *   a.	Select * from A, B  group by A.id order by A.id , B.id
    *   i.	Only one SingleTableOrder is Created ie SingleTableOrderPlan(A.id , B.id)
    *
    *   b.	Select * from A, B  group by B.id order by A.id , B.id
    *    i.	SingleTableOrderPlan(B.id)
    *    ii.	JoinLevelOrderPlan(A.id , B.id)
    *
    *    GroupByPlan-SingleTableOrder  , Order-JoinLevelOrderPlan
    *    An OrderPlan which contains SingleTableOrder of Group and Order of JoinLevelOrderPlan
    *    GroupByPlan-SingleTableOrder  , Order-GroupByPlan
    *       An OrderPlan which contains SingleTableOrder of Group and Order of GroupByOrderPlan.
    *
    *    GroupByPlan-JoinLevelOrder  , Order-SingleTableOrder
    *       An OrderPlan which contains JoinLevelOrder  of Group and SingleTableOrder of GroupBy in GroupByLevel Order
    *
    * GroupByPlan-JoinLevelOrder  , Order-JoinLevelOrder
    *     If Both	are same then an OrderPlan which contains the common JoinLevelOrderPlan
    *     If different then an OrderPlan
    *     Whose joinOrderPlan	contains GroupByPlan-JoinLevelOrder
    *   And  GroupByLevelOrder  contains, Order-JoinLevelOrder
    * GroupByPlan-JoinLevelOrder  , Order-GroupByLevelOrder
    *  An OrderPlan which contains JoinLevelOrder  of GroupBy and GroupByLevelOrder of OrderBy.
    *
    * @param orderByOrderPlan
    * @param groupByOrderPlan
    * @return
    * @throws DException
    */
    private _OrderPlan getMergedGroupAndOrderPlan(_OrderPlan orderByOrderPlan, _OrderPlan groupByOrderPlan) throws DException {
        if (orderByOrderPlan == null) {
            return groupByOrderPlan;
        }
        _SingleTableOrderPlan[] stopOrderBy = orderByOrderPlan.getSingleTableOrderPlans();
        _SingleTableOrderPlan[] stopGroupBy = groupByOrderPlan.getSingleTableOrderPlans();
        if (stopGroupBy != null) {
            if (stopOrderBy != null) {
                return mergeSingleTableOrderPlans(stopOrderBy, stopGroupBy, orderByOrderPlan, groupByOrderPlan);
            }
            _Order joinOrderPlanOrderBy = orderByOrderPlan.getJoinLevelOrder();
            if (joinOrderPlanOrderBy != null) {
                groupByOrderPlan.setJoinOrderPlan(joinOrderPlanOrderBy);
                return groupByOrderPlan;
            }
            groupByOrderPlan.setGroupByOrderPlan(orderByOrderPlan.getGroupByLevelOrder());
            return groupByOrderPlan;
        }
        if (stopOrderBy != null) {
            groupByOrderPlan.setGroupByOrderPlan(OrderPlanMerger.getOrder(stopOrderBy));
            return groupByOrderPlan;
        }
        _Order joinOrderPlanOrderBy = orderByOrderPlan.getJoinLevelOrder();
        _Order joinOrderPlanGroupBy = groupByOrderPlan.getJoinLevelOrder();
        if (joinOrderPlanOrderBy != null) {
            return mergeJoinOrderPlan(joinOrderPlanOrderBy, joinOrderPlanGroupBy, orderByOrderPlan, groupByOrderPlan);
        }
        groupByOrderPlan.setGroupByOrderPlan(orderByOrderPlan.getGroupByLevelOrder());
        return groupByOrderPlan;
    }

    /**
    * This method is used to merge both the arrays of Single Table Order Plans.
    * If they are same, then order plan of largest length is used. Otherwise,
    * single table orders of OrderByPlan will be applied at groupby level.
    * For Query like
    * a) Select CustomerID,EmployeeID from Orders
    *         Group By CustomerID,EmployeeID
    *         Order By EmployeeID
    * We will generate
    *  OrderPlan  having
    *    STOP > EmployeeID,CustomerID
    *
    * b) Select CustomerID,EmployeeID from Orders
    *      Group By EmployeeID , CustomerID
    *      Order By EmployeeID
    *    OrderPlan  having
    *           STOP > EmployeeID , CustomerId
    * @param stop1
    * @param stop2
    * @param orderByOrderPlan
    * @param groupByOrderPlan
    * @return
    * @throws DException
    */
    private _OrderPlan mergeSingleTableOrderPlans(_SingleTableOrderPlan[] stop1, _SingleTableOrderPlan[] stop2, _OrderPlan orderByOrderPlan, _OrderPlan groupByOrderPlan) throws DException {
        if (!checkSequence(stop1, stop2)) {
            groupByOrderPlan.setGroupByOrderPlan(OrderPlanMerger.getOrder(stop1));
            return groupByOrderPlan;
        }
        return (stop1.length >= stop2.length) ? orderByOrderPlan : groupByOrderPlan;
    }

    /**
    * It is required to check the redundancy of sorting needs of groupby and
    * orderby in case when join level order is present in both. If join level
    * order plan of groupby is same as that of orderby, then query is sorted
    * only once instead of twice.
    * @param joinOrderPlanOrderBy
    * @param joinOrderPlanGroupBy
    * @param orderByOrderPlan
    * @param groupByOrderPlan
    * @return
    * @throws DException
    */
    private _OrderPlan mergeJoinOrderPlan(_Order joinOrderPlanOrderBy, _Order joinOrderPlanGroupBy, _OrderPlan orderByOrderPlan, _OrderPlan groupByOrderPlan) throws DException {
        if (!checkSequence(joinOrderPlanOrderBy, joinOrderPlanGroupBy)) {
            groupByOrderPlan.setGroupByOrderPlan(joinOrderPlanOrderBy);
            return groupByOrderPlan;
        }
        return (joinOrderPlanOrderBy.getColumnIndexes().length >= joinOrderPlanGroupBy.getColumnIndexes().length) ? orderByOrderPlan : groupByOrderPlan;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_fromclause3);
        sb.append(" ");
        if (_Optwhereclause2 != null) {
            sb.append(_Optwhereclause2);
        }
        sb.append(" ");
        if (_Optgroupbyclause1 != null) {
            sb.append(_Optgroupbyclause1);
        }
        sb.append(" ");
        if (_Opthavingclause0 != null) {
            sb.append(_Opthavingclause0);
        }
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        tableexpression tempClass = new tableexpression();
        if (_Opthavingclause0 != null) {
            tempClass._Opthavingclause0 = (havingclause) _Opthavingclause0.clone();
        }
        if (_Optgroupbyclause1 != null) {
            tempClass._Optgroupbyclause1 = (groupbyclause) _Optgroupbyclause1.clone();
        }
        if (_Optwhereclause2 != null) {
            tempClass._Optwhereclause2 = (whereclause) _Optwhereclause2.clone();
        }
        tempClass._fromclause3 = (fromclause) _fromclause3.clone();
        return tempClass;
    }

    /**
    * This method merges the JoinCondition in an OrderSequencePlan ,
    * orderSequencePlan contains both the Plan of JoinRelation.
    * eg Select * from Orders , "Order Details" where Orders.OrderId = "Order Details".OrderId
    * order by Orders.OrderId , "Order Details".OrderId
    * then OrderSequencePlan will have two SingleTablePlans
    *   STOP[Orders] and STOP["Order Details"]
    * Select * from Orders ,  Employees , "Order Details" where
    * Orders.OrderId = "Order Details".OrderId order by Orders.OrderId ,
    * Employees.employeeid, "Order Details".OrderId
    * then OrderSequencePlan will have three SingleTablePlans
    *    STP[Orders] , STP[Employees] , STP["Order Details"]
    * we will prepare TemporaryMerge which will have three SingleTablePlan
    *    STP[Orders] , STP[Employees] , STP["Order Details"]
    * with remainingCondition Orders.OrderId = "Order Details".OrderId
    * @param tablePlan1
    * @param joinRelation
    * @throws DException
    */
    protected void mergeJoinRelationInOrderSequencePlan(_TablePlan tablePlan1, _JoinRelation joinRelation) throws DException {
        TableDetails[] tableDetails = joinRelation.getTableDetails();
        int index1 = ((OrderTablePlanAbstract) tablePlan1).getIndex(tableDetails[0]);
        int index2 = ((OrderTablePlanAbstract) tablePlan1).getIndex(tableDetails[1]);
        if (index1 > index2) {
            int temp = index1;
            index1 = index2;
            index2 = temp;
        }
        if (index2 - index1 == 0) {
            ((OrderTablePlanAbstract) tablePlan1).setJoinConditionOfChild(index1, joinRelation);
        } else {
            if ((index2 - index1) > 1) {
                prepareTableMergeAndUpdateTablePlans(tablePlan1, joinRelation.getCondition(), index1, index2);
            } else {
                prepareTwoTableJoinPlanAndUpdateTablePlans(tablePlan1, joinRelation.getCondition(), index1, index2);
            }
        }
    }

    /**
    * Merges the fromClause's condition plan with condition plan of where and
    * having clause.
    * @return
    * @throws DException
    */
    public _BVEPlan getBveExecutionPlan() throws DException {
        _BVEPlan fromBvePlan = _fromclause3.getBveExecutionPlan();
        return BVEPlanMerger.mergeTablePlansWithAnd(resultantBvePlan, fromBvePlan);
    }

    /**
    * A query is called simple when it contains only one table and which is not
    * a view. It should not contain groupby, where, having clause. For purpose,
    * refer to documentation of queryexpressionbody
    *
    * Method isSimpleQuery also written in fromClause so as to avoid calling getTableDetails.
    * The previous approach(getTableDetails) leads to NPE in case of natural join.
    *
    * @return
    * @throws DException
    */
    public boolean isSimpleQuery(_ServerSession serverSession) throws DException {
        if (serverSession != null) {
            return _Optgroupbyclause1 == null && (_Optwhereclause2 == null || (_Optwhereclause2 != null && !_Optwhereclause2.checkForSubquery() && !_Optwhereclause2.hasContainClause())) && _Opthavingclause0 == null && _fromclause3.isSimpleQuery(serverSession);
        }
        return _Optgroupbyclause1 == null && _Optwhereclause2 == null && _Opthavingclause0 == null && tableDetails.length == 1 && tableDetails[0].getTableType() == TypeConstants.VIEW;
    }

    /**
    * Documentation for purpose or requirement, refer the documenation of
    * queryexpressionbody.

    * Detailed documentation is given below.
    *
    * It is needed when the View is Optimizable.It gives the Array Of Plans involved
    * in the view
    * eg Select * from View1 v where V.Id =1 order by V.Id1
    * View1 "Select OrderId as Id, EmployeeID as Id1 from Orders inner join "Order Details" where Orders.OrderId = "Order Details".OrderId";
    * and this tableexpession is of Query "Select OrderId as Id from Orders inner join "Order Details" where
    * Orders.OrderId = "Order Details".OrderId"
    * ie View1
    * then
    * @param session
    * @param condition will have V.Id = 1 with it's tableDetails changed
    * @param viewOrder will have V.id1   with it's tableDetails changed
    * @param queryCols V.id , V.id1 with there tableDetails changed
    * @param conditionArray - V.Id =1 condition
    * @return
    * @throws DException
    *--------------------------
    *REQUIREMENT OF ARGUMENTS |
    *--------------------------
    *Condition  >>   This are SingleLevel Conditions of View .
    *                For the above view we would like to shift the condition V.Id = 1 to the Orders Table as V.Id column is
    *                actually of Orders.OrderId .Since in this tableExpression/querySpecification we are having the tablePlan
    *                of Orders so we need to pass the information upto this level.
    *ViewOrder  >>     This are SingleLevel Conditions of View .
    *                  For the above view we would like to shift the order on column V.Id to the Orders Table as V.Id column is
    *                  actually of Orders.OrderId .Since in this tableExpression/querySpecification we are having the tablePlan
    *                  of Orders so we need to pass the information to this level.
    *QueryColumns >>   QueryColumns helps to calculate the cost of Plan Generated.As it tells the columns involved in the
    *                  Query.There is always a cost of extracting any column from the Database.This cost calculation work
    *                  is done at SingleTablePlan.
    *ConditionArray >> For Changing the type of joins beacuse of the condition present  in where clause or on clause
    *                  we need to pass the these conditions as they can change their meaning
    *                  for eg Select * from Orders left Outer Join "Order Details" on Orders.OrderId = 11111 where "Orders Details".OrderId > 1
    *                  This Query will act as Select * from Orders , "Order Details" where "Orders Details".OrderId > 1 and Orders.OrderId = 11111
    *                  We need to pass "Orders Details".OrderId > 1 information to qualifiedJoin of Orders left Outer Join "Order Details"
    *                  so that it can change treat as simple Join.
    *                  We could have passed TableDetails of condition only but for the following case
    *                  for eg  Select * from Orders left Outer Join "Order Details" on Orders.OrderId = 11111
    *                          where "Orders Details".OrderId is null
    *                  we need not to change the type as simple Join .
    *                  and if Condition TableDetails had been passed then we would not have any indication that
    *                  the tableDetails were of null Predicate.
    *
    */
    public _TablePlan[] getTablePlans(_ServerSession session, booleanvalueexpression condition, _Order viewOrder, _QueryColumns queryCols, ConditionArray conditionArray) throws DException {
        resultantBvePlan = mergeBvePlan(condition);
        resultantBvePlan = createLayerOfBveAllTablePlan(resultantBvePlan);
        initializeOrderPlan(session, viewOrder, queryCols);
        initializeConditionArray(conditionArray);
        tablePlans = _fromclause3.getTablePlan(session, null, resultantBvePlan, orderPlan, queryCols, conditionArray);
        return orderPlan != null && orderPlan.getJoinLevelOrder() != null ? new _TablePlan[] { createTableExpressionPlan(session) } : tablePlans;
    }

    /**
    * Returns the order which will be applied on the result of group by.
    * @return
    * @throws DException
    */
    public _Order getGroupByLevelOrder() throws DException {
        return orderPlan == null ? null : orderPlan.getGroupByLevelOrder();
    }

    /**
    * It is used to check whether this query is eligible for 'For update' option.
    * Only those select Query, in which only single table is present and that table
    * should not be a view, is allowed. No group by or having should be present
    * for 'For update' option.
    * @throws DException
    */
    public void checkSemanticForUpdate() throws DException {
        if (_Optgroupbyclause1 != null || (_Optwhereclause2 != null && _Optwhereclause2.checkForSubquery()) || tableDetails.length != 1 || tableDetails[0].getTableType() == TypeConstants.VIEW) {
            throw new DException("DSE6003", null);
        }
    }

    /**
    * This method checks if either of order and Group by is redundant
    * e.g.
    * select a,b from A group by a,b order by b
    * if we rewrite the query as
    * select a,b from A group by b,a
    * the result is the same
    * as the above query thus the order by is redundant
    * Algo::
    * First the order column Details are got
    * Then the group by column details are got
    *
    * For each order column that is of reference type and order specification is not DESC
    *  we check if the column exists in the group by columns
    * The view columns are checked
    * @param viewOrder
    * @param session
    * @param queryCols
    * @return
    * @throws DException
    */
    private _OrderPlan checkForGroupAndOrderRedundant(_Order viewOrder, _ServerSession session, _QueryColumns queryCols) throws DException {
        if (viewOrder == null || _Optgroupbyclause1 == null) {
            return null;
        }
        ColumnDetails[] orderCD = viewOrder.getColumnDetails();
        boolean[] orderSpecification = viewOrder.getOrderOfColumns();
        ColumnDetails[] groupCD = _Optgroupbyclause1.getOrder(queryCols).getColumnDetails();
        ArrayList result = new ArrayList();
        int i = 0;
        for (; i < orderCD.length; i++) {
            if (orderCD[i].getType() != REFERENCE || !orderSpecification[i]) {
                return null;
            }
            String column = orderCD[i].getAppropriateColumn();
            int j = 0;
            for (; j < groupCD.length; j++) {
                if (orderCD[i].getTable() == groupCD[j].getTable() && column.equalsIgnoreCase(groupCD[j].getAppropriateColumn())) {
                    result.add(groupCD[j]);
                    break;
                }
            }
            if (j == groupCD.length) {
                return null;
            }
        }
        if (i == orderCD.length) {
            for (int k = 0; k < groupCD.length; k++) {
                if (!result.contains(groupCD[k])) {
                    result.add(groupCD[k]);
                }
            }
            return new SelectOrder((ColumnDetails[]) result.toArray(new ColumnDetails[result.size()])).getOrderPlan(session);
        }
        return null;
    }

    /**
    * For the documentation of underlying methods, refer the documentation of
    * queryexpressionbody.
    */
    public _Reference[] getReferences(TableDetails[] tableDetails) throws DException {
        _Reference[] reference = null;
        if (_Optwhereclause2 != null) {
            reference = GeneralPurposeStaticClass.getJointReferences(reference, _Optwhereclause2.getReferences(tableDetails));
        }
        if (_Opthavingclause0 != null) {
            reference = GeneralPurposeStaticClass.getJointReferences(reference, _Opthavingclause0.getReferences(tableDetails));
        }
        reference = GeneralPurposeStaticClass.getJointReferences(reference, _fromclause3.getUnderlyingReferences());
        if (_Optgroupbyclause1 != null) {
            reference = GeneralPurposeStaticClass.getJointReferences(reference, _Optgroupbyclause1.getReferences(tableDetails));
        }
        return reference;
    }

    public Object[] getParameters(Object object) throws DException {
        Object[] param = _fromclause3.getParameters(object);
        if (_Optwhereclause2 != null) {
            param = addParameters(param, _Optwhereclause2.getParameters(object));
        }
        if (_Opthavingclause0 != null) {
            param = addParameters(param, _Opthavingclause0.getParameters(object));
        }
        return param;
    }

    public TableDetails[] getTableDetails(_ServerSession session, ColumnDetails[] queryColumns) throws DException {
        if (tableDetails == null) {
            tableDetails = _fromclause3.getTableDetails(session, queryColumns);
        }
        return tableDetails;
    }

    /**
    * This method adds all the columndetails present in query. This method
    * is different from getColumnDetails as getColumnDetails returns the
    * columns belong to this query while this method returns all the
    * columnDetails of this query as well as underlying query(either in
    * view or in subQuery present in condition)
    * This method is needed by DDL.
    * @param aList
    * @throws DException
    */
    public void getColumnsIncluded(ArrayList aList) throws DException {
        _fromclause3.getColumnsIncluded(aList);
        if (_Optwhereclause2 != null) {
            _Optwhereclause2.getColumnsIncluded(aList);
        }
        if (_Optgroupbyclause1 != null) {
            _Optgroupbyclause1.getColumnsIncluded(aList);
        }
        if (_Opthavingclause0 != null) {
            _Opthavingclause0.getColumnsIncluded(aList);
        }
    }

    /**
    * This method adds all the TableDetails present in query. This method
    * is different from getTableDetails as getTableDetails returns the
    * tables belong to this query while this method returns all the
    * TableDetails of this query as well as underlying query(either in
    * view or in subQuery present in condition)
    * This method is needed by DDL.
    * @param aList
    * @throws DException
    */
    public void getTablesIncluded(ArrayList aList) throws DException {
        _fromclause3.getTablesIncluded(aList);
        if (_Optwhereclause2 != null) {
            _Optwhereclause2.getTablesIncluded(aList);
        }
        if (_Optgroupbyclause1 != null) {
            _Optgroupbyclause1.getTablesIncluded(aList);
        }
        if (_Opthavingclause0 != null) {
            _Opthavingclause0.getTablesIncluded(aList);
        }
    }

    public ParameterInfo[] getParameterInfo() throws DException {
        ParameterInfo[] p1 = _fromclause3.getParameterInfo();
        ParameterInfo[] p2 = null, p3 = null;
        if (_Optwhereclause2 != null) {
            p2 = _Optwhereclause2.getParameterInfo();
        }
        if (_Opthavingclause0 != null) {
            p3 = _Opthavingclause0.getParameterInfo();
        }
        ArrayList aList = new ArrayList(5);
        if (p1 != null) {
            aList.addAll(Arrays.asList(p1));
        }
        if (p2 != null) {
            aList.addAll(Arrays.asList(p2));
        }
        if (p3 != null) {
            aList.addAll(Arrays.asList(p3));
        }
        return (ParameterInfo[]) aList.toArray(new ParameterInfo[0]);
    }

    /**
    * This method is used to verify the values given by the user.
    * <ol><li>Checks the conditional Column values given by user.
    * If the values mismatchs the join condition, exception is thrown.</li>
    * <li>Checks the values must satisfy where clause condition, if violates,
    * exception is thrown</li></ol>
    **/
    public void verifyValues(_VariableValueOperations variableValueOperation) throws DException {
        return;
    }

    /**
    * This method is used to set the default values according to where clause and join condition,
    * <li><li>If user gives value of one of the join specification conditional column used in join
    * condition, value is set for the unspecified conditonal column to maintain the join condition.</li>
    * <li>If the where clause conditional column value is not given by the user, it is set
    * as default value.</li></ol>
    */
    public void setDefaultValues(_VariableValueOperations variableValueOperation) throws DException {
        ((VariableValueOperations) variableValueOperation).setFlag(false);
        _fromclause3.setDefaultValues(variableValueOperation);
        if (_Opthavingclause0 != null) {
            _Opthavingclause0.setDefaultValues(variableValueOperation);
        }
    }

    public _Reference[] checkSemantic(_ServerSession session, ColumnDetails[] queryColumns, boolean checkUserRight) throws DException {
        _Reference[] references = _fromclause3.checkSemantic(session, queryColumns, checkUserRight);
        if (_Optwhereclause2 != null) {
            references = GeneralPurposeStaticClass.getJointReferences(references, _Optwhereclause2.checkSemantic(session));
        }
        if (_Opthavingclause0 != null) {
            _Reference[] temp = _Opthavingclause0.checkSemantic(session);
            references = GeneralPurposeStaticClass.getJointReferences(references, temp);
        }
        return references;
    }

    /**
    * In this method columnMappingHandler is passed as parameter which maintains
    * a list of the tables in which insertion will be made for a insert operation
    * in select query with qualified join chain. This method adds the tables if there
    * exists one or more tables for insert operation according to the values specified by the user.
    */
    public void setTablesForInsertion(ColumnMappingHandler columnMapping, _VariableValueOperations vv) throws DException {
        return;
    }

    public TableDetails[] getTableDetails() throws DException {
        return finalTablePlan == null ? tableDetails : finalTablePlan.getTableDetails();
    }

    public TableDetails[] getAllTableDetails() throws DException {
        return allTableDetails == null ? allTableDetails = _fromclause3.getAllTableDetails() : allTableDetails;
    }
}
