package com.daffodilwoods.daffodildb.server.serversystem.dmlvalidation.constraintsystem;

import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.*;

/**
 *
 * <p>Title: Constraint Table</p>
 * <p>Description: </p>
 * Objective of the constraint table is to verify the values of the constraints , set for update , delete and insert operations.
 */
public class ConstraintTable implements _ConstraintTable {

    boolean deferrable;

    public PrimaryAndUniqueConstraintVerifier primaryAndUniqueConstraintVerifier;

    CheckConstraintsVerifier checkConstraintVerifier;

    ReferencingConstraintsVerifier referencingConstraintsVerifier;

    ReferencedConstraintsVerifier referencedConstraintsVerifier;

    _ConstraintDatabase constraintDatabase;

    private boolean hasDefferred = false;

    private QualifiedIdentifier tableName;

    /**
    * Used to construct the constraint table with the following arguments.
    * @param deferrabl used to detemine whether the constraints are of deferred type.
    * @param primaryAndUniqueVerifier instance of primaryAndUniqueConstraintVerifier used to verify the
    * primary and the unique constraints on the table
    * @param cConstraintVerifier instance of checkconstraintverifier used to verify the
    * check constraints on the table.
    * @param referencingVerifier instance of referncingconstraintverifier used to verify the
    * referencing constraints on the table
    * @param referencedVerifier instance of referencedconstraintverifier used to verify the
    * referenced constraints on the table.
    * @param constrantDatabase instance of constraint table to which the constraint table belongs.
    * @param hasDeff
    * @throws DException
    */
    public ConstraintTable(boolean deferrabl, PrimaryAndUniqueConstraintVerifier primaryAndUniqueVerifier, CheckConstraintsVerifier cConstraintVerifier, ReferencingConstraintsVerifier referencingVerifier, ReferencedConstraintsVerifier referencedVerifier, _ConstraintDatabase constrantDatabase, boolean hasDeff, QualifiedIdentifier tabName) throws DException {
        deferrable = deferrabl;
        primaryAndUniqueConstraintVerifier = primaryAndUniqueVerifier;
        checkConstraintVerifier = cConstraintVerifier;
        referencedConstraintsVerifier = referencedVerifier;
        referencingConstraintsVerifier = referencingVerifier;
        constraintDatabase = constrantDatabase;
        hasDefferred = hasDeff;
        tableName = tabName;
    }

    /**
    * used to retreive the constraint table from constraint Database.
    * @param tableName name of the table
    * @return _ConstraintTable instance of _constraint table that has been retreived by the method.
    * @throws DException
    */
    public _ConstraintTable getConstraintTable(QualifiedIdentifier tableName) throws DException {
        return constraintDatabase.getConstraintTable(tableName);
    }

    /**
    * retreives a boolean determining whether the constraints on the table has been set to Defferred (Defferred means all the changes will be made at the time of Commit Otherwise the changes will be made side by side)
    * @return boolean true if the constraints are defferred
    * @throws DException
    */
    public boolean hasDefferred() throws DException {
        return hasDefferred;
    }

    /**
    * used to verify all the constraints for the delete operation.
    * @param globalSession instance of server session in which the operation is performed.
    * @param statementExecutionContext instance of statementexecutioncontext
    * @throws ConstraintException
    */
    public void checkDeleteConstraints(_ServerSession globalSession, _StatementExecutionContext statementExecutionContext) throws DException {
        referencedConstraintsVerifier.verifyReferencedConstraintsForDelete(globalSession, statementExecutionContext);
    }

    /**
    *used to verify all the constraints for the update operation.
    * @param globalSession instance of server session in which the operation is performed.
    * @param statementExecutionContext instance of statementexecutioncontext
    * @param columns array of columns whose value are to be updated
    * @throws ConstraintException
    * @throws DException
    */
    public void checkUpdateConstraints(_ServerSession globalSession, _StatementExecutionContext statementExecutionContext, int[] columns) throws ConstraintException, DException {
        checkConstraintVerifier.verifyCheckConstraintsForUpdate(columns, statementExecutionContext, globalSession);
        primaryAndUniqueConstraintVerifier.verifyPrimaryConstraintsForUpdate(columns, statementExecutionContext, globalSession);
        primaryAndUniqueConstraintVerifier.verifyUniqueConstraintsForUpdate(columns, statementExecutionContext, globalSession);
        referencingConstraintsVerifier.verifyReferencingConstraintsForUpdate(columns, globalSession, statementExecutionContext);
        referencedConstraintsVerifier.verifyReferencedConstraintsForUpdate(columns, globalSession, statementExecutionContext);
    }

    /**
       used to verify all the constraints for the insert operation.
    * @param globalSession instance of server session in which the operation is performed.
    * @param statementExecutionContext instance of statementexecutioncontext
    * @param columns array of columns which are to be inserted in the table.
    * @throws ConstraintException
    * @throws DException
    */
    public void checkInsertConstraints(_ServerSession globalSession, _StatementExecutionContext statementExecutionContext, int[] columns) throws ConstraintException, DException {
        try {
            checkConstraintVerifier.verifyCheckConstraints(statementExecutionContext, globalSession);
        } catch (DException ex) {
            if (ex.getDseCode().equalsIgnoreCase("DSE1251")) {
                Object[] parameters = ex.getParameters();
                parameters[0] = "INSERT";
                ex.setParameters(parameters);
                throw new DException("DSE1251", parameters);
            }
            throw ex;
        }
        primaryAndUniqueConstraintVerifier.verifyUniqueConstraints(columns, statementExecutionContext, globalSession);
        primaryAndUniqueConstraintVerifier.verifyPrimaryConstraints(columns, statementExecutionContext, globalSession);
        referencingConstraintsVerifier.verifyReferencingConstraints(columns, globalSession, statementExecutionContext);
    }

    /**
    * retreives a boolean that determines whether the constraints are deferred or not.
    * @return boolean returns true if the table type has been set to Defferrable (Defferred means all the
    * changes will be made at the time of Commit  Otherwise the changes will be made side by side)
    * @throws DException
    */
    public boolean isDeferredType() throws DException {
        return deferrable;
    }

    public String toString() {
        return " XXXXXXXXXXXXXYYYYYYYYYYYYYYZZZZZZZZZZZZ   " + referencingConstraintsVerifier.toString();
    }
}
