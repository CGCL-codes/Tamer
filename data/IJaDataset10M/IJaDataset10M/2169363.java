package com.daffodilwoods.daffodildb.server.sessionsystem;

import com.daffodilwoods.daffodildb.server.datadictionarysystem.*;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.daffodildb.server.datasystem.utility.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.utility.P;

/**
 * wrapper over the Session Table which is responsible for checking of privileges
 * of the user for which this object has been taken. It checks the table level
 * privileges as well as column level privileges for read and write operations.
 * After verifying, it passes all the calls to SessionTable to complete the operation.
 */
public class UserSessionTable implements _UserSessionTable {

    private _UserSession userSession;

    /**
    * Object of  session table for performing Read and Write operations
    */
    private _SessionTable sessionTable;

    /**
    *  Qualified name of the table
    */
    private QualifiedIdentifier tableName;

    /**
    *  Object of Privilege Table to check the privileges
    */
    private _PrivilegeTable privilegeTable;

    /**
    * Constructs the UserSessionTable
    */
    public UserSessionTable(QualifiedIdentifier tableName0, _SessionTable sessionTable0, _PrivilegeTable privilegeTable0, _UserSession userSession0) throws DException {
        tableName = tableName0;
        sessionTable = sessionTable0;
        userSession = userSession0;
        privilegeTable = privilegeTable0;
    }

    /**
    * After verifying the privileges, forwards the call to interface _SessionTable
    * to insert the record in memory.
    * @param columns indexes of columns whose values are to be inserted
    * @param values values to be inserted
    * @return returns an event with the type insert
    * @throws DException
    */
    public RecordVersion insert(int[] columns, Object[] values) throws DException {
        boolean insertRight = privilegeTable.hasTablePrivileges(_PrivilegeTable.INSERT);
        if (columns == null && !insertRight) throw new PrivilegeException("DSE340", new Object[] { "INSERT", tableName });
        insertRight = privilegeTable.hasColumnPrivileges(_PrivilegeTable.INSERT, columns);
        if (!insertRight && columns != null && !(columns[0] == SystemFields.invalidSessionId && columns.length == 1)) throw new PrivilegeException("DSE340", new Object[] { "INSERT", tableName });
        return sessionTable.insert(columns, values);
    }

    /**
    * After verifying the privileges, forwards the call to interface
    * _SessionTable.
    * @param columns indexes of columns whose values are to be inserted
    * @param values values to be inserted
    * @param date value of the fromDate column
    * @return returns an event with the type insert
    * @throws DException
    */
    public RecordVersion insertVersion(int[] columns, Object[] values, java.sql.Date date) throws DException {
        boolean insertRight = privilegeTable.hasTablePrivileges(_PrivilegeTable.INSERT);
        if (!insertRight) throw new PrivilegeException("DSE340", new Object[] { "INSERT", tableName });
        return sessionTable.insertVersion(columns, values, date);
    }

    /**
    * 	After verifying the privileges, forwards the call to interface _
    * SessionTable to delete the record.
    * @param iterator provides the record which is to be deleted
    * @return returns an event with the type delete
    * @throws DException
    */
    public RecordVersion delete(_Iterator iterator) throws DException {
        boolean deleteRight = privilegeTable.hasTablePrivileges(_PrivilegeTable.DELETE);
        if (!deleteRight) throw new PrivilegeException("DSE340", new Object[] { "DELETE", tableName });
        return sessionTable.delete(iterator);
    }

    /**
    * 	After verifying the privileges, forwards the call to interface _
    * SessionTable to delete the record.
    * @param iterator provides the record which is to be deleted
    * @param date date which is to be set in ToDate column to mark it as deleted
    * @return returns an event with the type delete
    * @throws DException
    */
    public RecordVersion deleteVersion(_Iterator iterator, java.sql.Date date) throws DException {
        boolean deleteRight = privilegeTable.hasTablePrivileges(_PrivilegeTable.DELETE);
        if (!deleteRight) throw new PrivilegeException("DSE340", new Object[] { "DELETE", tableName });
        return sessionTable.deleteVersion(iterator, date);
    }

    /**
    * After verifying the privileges on table and columns passed by user, forwards
    * call to  _SessionTable interaface.
    * @param iterator provides the record which is to be deleted
    * @param columns indexes of columns whose values are to be inserted
    * @param values values to be inserted
    * @return returns an event with the type update
    * @throws DException
    */
    public RecordVersion update(_Iterator iterator, int[] columns, Object[] values) throws DException {
        boolean updateRight = privilegeTable.hasTablePrivileges(_PrivilegeTable.UPDATE);
        if (columns == null && !updateRight) throw new PrivilegeException("DSE340", new Object[] { "UPDATE", tableName });
        updateRight = privilegeTable.hasColumnPrivileges(_PrivilegeTable.UPDATE, columns);
        if (!updateRight) throw new PrivilegeException("DSE340", new Object[] { "UPDATE", tableName });
        return sessionTable.update(iterator, columns, values);
    }

    /**
    * After verifying the privileges on table and columns passed by user, forwards
    * call to  _SessionTable interaface.
    * @param iterator provides the record which is to be deleted
    * @param columns indexes of columns whose values are to be inserted
    * @param values values to be inserted
    * @param date date upto which record is to be marked as invalid
    * @return returns an event with the type update
    * @throws DException
    */
    public RecordVersion updateVersion(_Iterator iterator, int[] columns, Object[] values, java.sql.Date date) throws DException {
        boolean updateRight = privilegeTable.hasTablePrivileges(_PrivilegeTable.UPDATE);
        if (!updateRight) throw new PrivilegeException("DSE340", new Object[] { "UPDATE", tableName });
        updateRight = privilegeTable.hasColumnPrivileges(_PrivilegeTable.UPDATE, columns);
        if (!updateRight) throw new PrivilegeException("DSE340", new Object[] { "UPDATE", tableName });
        return sessionTable.updateVersion(iterator, columns, values, date);
    }

    /**
    * Provides an iterator to fetch the records after verifying the privileges
    * @param _SingleTableExecuter This object contains the condition and order
    * @param _ServerSession Object of serverSession which is used to get the
    *        information for inner query
    * @return  returns an object of _Iterator interaface to fetch the record
    * @throws DException
    */
    public _Iterator getIterator(_SingleTableExecuter singleTableExecuter, _ServerSession serverSession) throws DException {
        if (singleTableExecuter.checkUserRight()) {
            boolean fetchRight = false;
            if (singleTableExecuter.getColumns() != null) {
                fetchRight = privilegeTable.hasColumnPrivileges(_PrivilegeTable.SELECT, singleTableExecuter.getColumns());
                if (!fetchRight) {
                    throw new DException("DSE8132", null);
                }
            } else if (singleTableExecuter.getColumns() == null) {
                fetchRight = privilegeTable.hasTablePrivileges(_PrivilegeTable.SELECT);
                if (!fetchRight) {
                    throw new DException("DSE342", null);
                }
            }
        }
        ParameterisedCondition pCondition = privilegeTable.getPrivilegeCondition();
        singleTableExecuter.addCondition(pCondition.getBVE());
        _Reference[] references = GeneralPurposeStaticClass.changeReferences(pCondition.getBVE().getParameters(null));
        _Iterator iterator = sessionTable.getIterator(singleTableExecuter, serverSession);
        if (references != null && references.length != 0) {
            iterator.setConditionVariableValue(references, (Object[]) pCondition.getVariableValues().getColumnValues(references), 1);
        }
        return iterator;
    }

    /**
    * Provides an iterator to fetch the records of both referencing as well as
    * referenced after verifying the privileges
    * @param _SingleTableExecuter This object contains the condition and order
    * @param _IndexTable Object of ForeignConstraint table which is having
    *        all the information required to make the Foreign key iterator.
    * @return  returns an object of _Iterator interaface to fetch the record
    * @throws DException
    */
    public _Iterator getForeignConstraintIterator(_SingleTableExecuter conditionExecuter, _IndexTable foreignConstraintTable) throws DException {
        boolean fetchRight = false;
        try {
            fetchRight = privilegeTable.hasTablePrivileges(_PrivilegeTable.SELECT);
        } catch (DatabaseException ex) {
            throw new DException("DSE342", null);
        }
        if (!fetchRight) throw new DException("DSE342", null);
        ParameterisedCondition pCondition = privilegeTable.getPrivilegeCondition();
        conditionExecuter.addCondition(pCondition.getBVE());
        _Reference[] references = (_Reference[]) pCondition.getBVE().getParameters(null);
        _Iterator iterator = sessionTable.getForeignConstraintIterator(conditionExecuter, foreignConstraintTable);
        if (references != null) {
            iterator.setConditionVariableValue(references, (Object[]) pCondition.getVariableValues().getColumnValues(references), 1);
        }
        return new SessionIterator(iterator);
    }

    public _Iterator getInternalIterator(_SingleTableExecuter singleTableExecuter, _ServerSession serverSession) throws DException {
        boolean fetchRight = false;
        if (singleTableExecuter.getColumns() != null) {
            fetchRight = privilegeTable.hasColumnPrivileges(_PrivilegeTable.SELECT, singleTableExecuter.getColumns());
            if (!fetchRight) {
                throw new DException("DSE8132", null);
            }
        }
        ParameterisedCondition pCondition = privilegeTable.getPrivilegeCondition();
        singleTableExecuter.addCondition(pCondition.getBVE());
        _Reference[] references = GeneralPurposeStaticClass.changeReferences(pCondition.getBVE().getParameters(null));
        _Iterator iterator = sessionTable.getConditionalIterator(singleTableExecuter, serverSession);
        if (references != null && references.length != 0) {
            iterator.setConditionVariableValue(references, (Object[]) pCondition.getVariableValues().getColumnValues(references), 1);
        }
        return iterator;
    }

    public RecordVersion insertWithoutRights(int[] columns, Object[] values) throws DException {
        return sessionTable.insert(columns, values);
    }

    public RecordVersion updateWithoutRights(_Iterator iterator, int[] columns, Object[] values) throws DException {
        return sessionTable.update(iterator, columns, values);
    }

    public RecordVersion deleteWithoutRights(_Iterator iterator) throws DException {
        return sessionTable.delete(iterator);
    }

    public _PrivilegeTable getPrivilegeTable() throws DException {
        return privilegeTable;
    }
}
