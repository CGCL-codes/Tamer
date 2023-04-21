package com.daffodilwoods.daffodildb.server.sql99.dcl.sqlcontrolstatement;

import java.util.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.expressionprimary.SQLparameterreference;

public class assignmentstatement implements SQLcontrolstatement {

    public assignmentsource _assignmentsource0;

    public Sequalsoperator182046693 _Sequalsoperator1820466931;

    public assignmenttarget _assignmenttarget2;

    public SRESERVEDWORD1206543922 _SRESERVEDWORD12065439223;

    private _Reference[] sourceRefs;

    private _Reference targetRef;

    private _ServerSession serverSession;

    public Object run(Object object) throws DException {
        return this;
    }

    public void setOuterControlStatement(SQLcontrolstatement outerst) {
    }

    public Object[] getParameters(Object object) throws DException {
        ArrayList list = new ArrayList();
        Object[] par1 = _assignmentsource0.getParameters(object);
        if (par1 != null) {
            list.addAll(Arrays.asList(par1));
        }
        Object[] par2 = _assignmenttarget2.getParameters(object);
        if (par2 != null) {
            list.addAll(Arrays.asList(par2));
        }
        return list.toArray();
    }

    public Object execute(_VariableValues variableValues) throws DException {
        Object value = SearchConditionUtility.executeExpression(_assignmentsource0, sourceRefs, variableValues, serverSession);
        Object[][] afterExecuteRefValuePair = variableValues.getReferenceAndValuePair();
        if (afterExecuteRefValuePair != null) {
            for (int i = 0; i < afterExecuteRefValuePair.length; i++) {
                _Reference vvRef = (_Reference) afterExecuteRefValuePair[i][0];
                if (vvRef.getQualifiedColumnName().equalsIgnoreCase(targetRef.getQualifiedColumnName())) {
                    value = FieldUtility.convertToAppropriateType((FieldBase) value, vvRef.getDatatype(), vvRef.getSize(), null);
                    variableValues.setConditionVariableValue(new _Reference[] { vvRef }, new Object[] { value }, 1);
                }
            }
        }
        return value;
    }

    public Object execute(Object[] values) throws DException {
        throw new UnsupportedOperationException();
    }

    public Object executeForFresh(Object[] values) throws DException {
        throw new UnsupportedOperationException();
    }

    public ParameterInfo[] getParameterInfo() throws DException {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_SRESERVEDWORD12065439223);
        sb.append(" ");
        sb.append(_assignmenttarget2);
        sb.append(" ");
        sb.append(_Sequalsoperator1820466931);
        sb.append(" ");
        sb.append(_assignmentsource0);
        return sb.toString().trim();
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    public ColumnDetails[] getColumnDetails() throws DException {
        ColumnDetails[] sourceCD = _assignmentsource0.getColumnDetails();
        ColumnDetails[] targetCD = ((SQLparameterreference) _assignmenttarget2).getColumnDetails();
        ColumnDetails[] cd = GeneralPurposeStaticClass.getJointColumnDetails(sourceCD, targetCD);
        ArrayList list = new ArrayList();
        GeneralPurposeStaticClass.addRecursively(cd, list);
        return (ColumnDetails[]) list.toArray(new ColumnDetails[0]);
    }

    public _Reference[] checkSemantic(_ServerSession object) throws DException {
        serverSession = object;
        ArrayList set = new ArrayList();
        _assignmentsource0.checkSemantic(object);
        sourceRefs = _assignmentsource0.getReferences(dummyTableDetail);
        _Reference[] par2 = _assignmenttarget2.getReferences(dummyTableDetail);
        if (sourceRefs != null) {
            for (int i = 0; i < sourceRefs.length; i++) {
                if (sourceRefs[i].getReferenceType() != SimpleConstants.SUBQUERY) set.add(sourceRefs[i]);
            }
        }
        targetRef = par2[0];
        if (targetRef instanceof ColumnDetails) ((ColumnDetails) targetRef).setAssignmentTarget(true);
        set.add(targetRef);
        return (_Reference[]) set.toArray(new _Reference[0]);
    }

    public void getTableIncluded(ArrayList aList) throws DException {
    }

    public void getColumnsIncluded(ArrayList aList) throws DException {
    }

    private void checkStatementValidity(_Reference targetRefs, assignmentsource assignmentsource, _ServerSession object) throws DException {
        try {
            Check.checkForDatatype(targetRefs.getDatatype(), assignmentsource.getByteComparison(object).getDataTypes()[0]);
        } catch (DException ex) {
            throw new DException("DSE8214", null);
        }
    }
}
