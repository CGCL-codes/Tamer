package org.datanucleus.store.rdbms.sql;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.PropertyNames;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.DiscriminatorMetaData;
import org.datanucleus.metadata.DiscriminatorStrategy;
import org.datanucleus.store.mapped.DatastoreClass;
import org.datanucleus.store.mapped.DatastoreContainerObject;
import org.datanucleus.store.mapped.DatastoreIdentifier;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.sql.expression.BooleanExpression;
import org.datanucleus.store.rdbms.sql.expression.NullLiteral;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpressionFactory;
import org.datanucleus.store.rdbms.sql.expression.StringLiteral;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.StringUtils;

/**
 * Class to generate an SQLStatement for iterating through instances of a particular type (and 
 * optionally subclasses). Based around the candidate type having subclasses and we use UNIONs to
 * return all possible types of candidate. Also allows select of a dummy column to return the
 * type for the part of the UNION that the object came from. Please refer to the specific constructors
 * for the usages.
 * <h3>Supported options</h3>
 * <p>
 * This generator supports
 * <ul>
 * <li><b>selectNucleusType</b> : adds a SELECT of a dummy column accessible as "NUCLEUS_TYPE" storing the 
 *     class name.</li>
 * <li><b>allowNulls</b> : whether we allow for null objects (only happens when we have a join table
 *     collection.</li>
 * </p>
 */
public class UnionStatementGenerator extends AbstractStatementGenerator {

    /** Name of column added when using "selectNucleusType" */
    public static final String NUC_TYPE_COLUMN = "NUCLEUS_TYPE";

    /**
     * Constructor using the candidateTable as the primary table.
     * If we are querying objects of type A with subclasses A1, A2 the query will be of the form :-
     * <PRE>
     * SELECT ['mydomain.A' AS NUCLEUS_TYPE]
     * FROM A THIS
     *   LEFT OUTER JOIN A1 SUBELEMENT0 ON SUBELEMENT0.A1_ID = THIS.A_ID
     *   LEFT OUTER JOIN A1 SUBELEMENT1 ON SUBELEMENT0.A2_ID = THIS.A_ID
     * WHERE SUBELEMENT0.A1_ID IS NULL
     * AND SUBELEMENT0.A2_ID IS NULL
     *
     * UNION 
     *
     * SELECT ['mydomain.A1' AS NUCLEUS_TYPE] 
     * FROM A THIS
     *   INNER JOIN A1 'ELEMENT' ON 'ELEMENT'.A1_ID = THIS.A_ID
     *
     * UNION
     *
     * SELECT ['mydomain.A2' AS NUCLEUS_TYPE] 
     * FROM A THIS
     *   INNER JOIN A2 'ELEMENT' ON 'ELEMENT'.A2_ID = THIS.A_ID
     * </PRE>
     * So the first part of the UNION returns the objects just present in the A table, whilst the
     * second part returns those just in table A1, and the third part returns those just in table A2.
     * @param storeMgr the store manager
     * @param clr ClassLoader resolver
     * @param candidateType the candidate that we are looking for
     * @param includeSubclasses if the subclasses of the candidate should be included in the result
     * @param candidateTableAlias Alias to use for the candidate table (optional)
     * @param candidateTableGroupName Name of the table group for the candidate(s) (optional)
     */
    public UnionStatementGenerator(RDBMSStoreManager storeMgr, ClassLoaderResolver clr, Class candidateType, boolean includeSubclasses, DatastoreIdentifier candidateTableAlias, String candidateTableGroupName) {
        super(storeMgr, clr, candidateType, includeSubclasses, candidateTableAlias, candidateTableGroupName);
    }

    /**
     * Constructor using a join table as the primary table.
     * If we are querying elements (B) of a collection in class A and B has subclasses B1, B2 
     * stored via a join table (A_B) the query will be of the form :-
     * <PRE>
     * SELECT ['mydomain.B' AS NUCLEUS_TYPE]
     * FROM A_B T0
     *   INNER JOIN B T1 ON T0.B_ID_EID = T1.B_ID
     *   LEFT OUTER JOIN B1 T2 ON T2.B1_ID = T0.B_ID_EID
     *   LEFT OUTER JOIN B2 T3 ON T3.B2_ID = T0.B_ID_EID
     * WHERE T2.B1_ID IS NULL
     * AND T3.B2_ID IS NULL
     *
     * UNION 
     *
     * SELECT ['mydomain.B1' AS NUCLEUS_TYPE] 
     * FROM A_B THIS
     *   INNER JOIN B T1 ON T1.B_ID = T0.B_ID_EID
     *   INNER JOIN B1 T2 ON T2.B1_ID = T1.B_ID
     *
     * UNION
     *
     * SELECT ['mydomain.A2' AS NUCLEUS_TYPE] 
     * FROM A_B THIS
     *   INNER JOIN B T1 ON T1.B_ID = T0.B_ID_EID
     *   INNER JOIN B2 T2 ON T2.B2_ID = T1.B_ID
     * </PRE>
     * So the first part of the UNION returns the objects just present in the B table, whilst the
     * second part returns those just in table B1, and the third part returns those just in table B2.
     * When we have a join table collection we MUST select the join table since this then caters for the
     * situation of having null elements (if we had selected the root element table we wouldn't know if 
     * there was a null element in the collection).
     * @param storeMgr the store manager
     * @param clr ClassLoader resolver
     * @param candidateType the candidate that we are looking for
     * @param includeSubclasses if the subclasses of the candidate should be included in the result
     * @param candidateTableAlias Alias to use for the candidate table (optional)
     * @param candidateTableGroupName Name of the table group for the candidate(s) (optional)
     * @param joinTable Join table linking owner to elements
     * @param joinTableAlias any alias to use for the join table in the SQL
     * @param joinElementMapping Mapping in the join table to link to the element
     */
    public UnionStatementGenerator(RDBMSStoreManager storeMgr, ClassLoaderResolver clr, Class candidateType, boolean includeSubclasses, DatastoreIdentifier candidateTableAlias, String candidateTableGroupName, DatastoreContainerObject joinTable, DatastoreIdentifier joinTableAlias, JavaTypeMapping joinElementMapping) {
        super(storeMgr, clr, candidateType, includeSubclasses, candidateTableAlias, candidateTableGroupName, joinTable, joinTableAlias, joinElementMapping);
    }

    public void setParentStatement(SQLStatement stmt) {
        this.parentStmt = stmt;
    }

    private int maxClassNameLength = -1;

    /**
     * Accessor for the SQL Statement for the candidate [+ subclasses].
     * @return The SQL Statement returning objects with a UNION statement.
     */
    public SQLStatement getStatement() {
        Collection<String> candidateClassNames = new ArrayList<String>();
        AbstractClassMetaData acmd = storeMgr.getMetaDataManager().getMetaDataForClass(candidateType, clr);
        candidateClassNames.add(acmd.getFullClassName());
        if (includeSubclasses) {
            Set<String> subclasses = storeMgr.getSubClassesForClass(candidateType.getName(), true, clr);
            candidateClassNames.addAll(subclasses);
        }
        Iterator<String> iter = candidateClassNames.iterator();
        while (iter.hasNext()) {
            String className = iter.next();
            try {
                Class cls = clr.classForName(className);
                if (Modifier.isAbstract(cls.getModifiers())) {
                    iter.remove();
                }
            } catch (Exception e) {
                iter.remove();
            }
        }
        if (hasOption(OPTION_SELECT_NUCLEUS_TYPE)) {
            iter = candidateClassNames.iterator();
            while (iter.hasNext()) {
                String className = iter.next();
                if (className.length() > maxClassNameLength) {
                    maxClassNameLength = className.length();
                }
            }
        }
        if (candidateClassNames.isEmpty()) {
            throw new NucleusException("Attempt to generate SQL statement using UNIONs for " + candidateType.getName() + " yet there are no concrete classes with their own table available");
        }
        SQLStatement stmt = null;
        iter = candidateClassNames.iterator();
        while (iter.hasNext()) {
            String candidateClassName = iter.next();
            SQLStatement candidateStmt = null;
            if (joinTable == null) {
                candidateStmt = getSQLStatementForCandidate(candidateClassName);
            } else {
                candidateStmt = getSQLStatementForCandidateViaJoin(candidateClassName);
            }
            if (candidateStmt != null) {
                if (stmt == null) {
                    stmt = candidateStmt;
                } else {
                    stmt.union(candidateStmt);
                }
            }
        }
        return stmt;
    }

    /**
     * Convenience method to return the SQLStatement for a particular class.
     * Returns a SQLStatement with primaryTable of the "candidateTable", and which joins to
     * the table of the class (if different).
     * @param className The class name to generate the statement for
     * @return The SQLStatement
     */
    protected SQLStatement getSQLStatementForCandidate(String className) {
        DatastoreClass table = storeMgr.getDatastoreClass(className, clr);
        if (table == null) {
            NucleusLogger.GENERAL.info("Generation of statement to retrieve objects of type " + candidateType.getName() + (includeSubclasses ? " including subclasses " : "") + " attempted to include " + className + " but this has no table of its own; ignored");
            return null;
        }
        SQLStatement stmt = new SQLStatement(storeMgr, candidateTable, candidateTableAlias, candidateTableGroupName);
        stmt.setClassLoaderResolver(clr);
        stmt.setCandidateClassName(className);
        String tblGroupName = stmt.getPrimaryTable().getGroupName();
        if (table != candidateTable) {
            JavaTypeMapping candidateIdMapping = candidateTable.getIdMapping();
            JavaTypeMapping tableIdMapping = table.getIdMapping();
            SQLTable tableSqlTbl = stmt.innerJoin(null, candidateIdMapping, table, null, tableIdMapping, null, stmt.getPrimaryTable().getGroupName());
            tblGroupName = tableSqlTbl.getGroupName();
        }
        SQLExpressionFactory factory = storeMgr.getSQLExpressionFactory();
        JavaTypeMapping discriminatorMapping = table.getDiscriminatorMapping(false);
        DiscriminatorMetaData discriminatorMetaData = table.getDiscriminatorMetaData();
        if (discriminatorMapping != null && discriminatorMetaData.getStrategy() != DiscriminatorStrategy.NONE) {
            String discriminatorValue = className;
            if (discriminatorMetaData.getStrategy() == DiscriminatorStrategy.VALUE_MAP) {
                AbstractClassMetaData targetCmd = storeMgr.getNucleusContext().getMetaDataManager().getMetaDataForClass(className, clr);
                discriminatorValue = targetCmd.getInheritanceMetaData().getDiscriminatorMetaData().getValue();
            }
            SQLExpression discExpr = factory.newExpression(stmt, stmt.getPrimaryTable(), discriminatorMapping);
            SQLExpression discVal = factory.newLiteral(stmt, discriminatorMapping, discriminatorValue);
            stmt.whereAnd(discExpr.eq(discVal), false);
        }
        if (table.getMultitenancyMapping() != null) {
            JavaTypeMapping tenantMapping = table.getMultitenancyMapping();
            SQLTable tenantSqlTbl = stmt.getTable(tenantMapping.getDatastoreContainer(), tblGroupName);
            SQLExpression tenantExpr = stmt.getSQLExpressionFactory().newExpression(stmt, tenantSqlTbl, tenantMapping);
            SQLExpression tenantVal = stmt.getSQLExpressionFactory().newLiteral(stmt, tenantMapping, storeMgr.getStringProperty(PropertyNames.PROPERTY_TENANT_ID));
            stmt.whereAnd(tenantExpr.eq(tenantVal), true);
        }
        Iterator<String> subIter = storeMgr.getSubClassesForClass(className, false, clr).iterator();
        while (subIter.hasNext()) {
            String subclassName = subIter.next();
            DatastoreClass[] subclassTables = null;
            DatastoreClass subclassTable = storeMgr.getDatastoreClass(subclassName, clr);
            if (subclassTable == null) {
                AbstractClassMetaData targetSubCmd = storeMgr.getNucleusContext().getMetaDataManager().getMetaDataForClass(subclassName, clr);
                AbstractClassMetaData[] targetSubCmds = storeMgr.getClassesManagingTableForClass(targetSubCmd, clr);
                subclassTables = new DatastoreClass[targetSubCmds.length];
                for (int i = 0; i < targetSubCmds.length; i++) {
                    subclassTables[i] = storeMgr.getDatastoreClass(targetSubCmds[i].getFullClassName(), clr);
                }
            } else {
                subclassTables = new DatastoreClass[1];
                subclassTables[0] = subclassTable;
            }
            for (int i = 0; i < subclassTables.length; i++) {
                if (subclassTables[i] != table) {
                    JavaTypeMapping tableIdMapping = table.getIdMapping();
                    JavaTypeMapping subclassIdMapping = subclassTables[i].getIdMapping();
                    SQLTable sqlTableSubclass = stmt.leftOuterJoin(null, tableIdMapping, subclassTables[i], null, subclassIdMapping, null, stmt.getPrimaryTable().getGroupName());
                    SQLExpression subclassIdExpr = factory.newExpression(stmt, sqlTableSubclass, subclassIdMapping);
                    SQLExpression nullExpr = new NullLiteral(stmt, null, null, null);
                    stmt.whereAnd(subclassIdExpr.eq(nullExpr), false);
                }
            }
        }
        if (hasOption(OPTION_SELECT_NUCLEUS_TYPE)) {
            addTypeSelectForClass(stmt, className);
        }
        return stmt;
    }

    /**
     * Convenience method to return the SQLStatement for a particular class selecting a join table.
     * Returns a SQLStatement with primaryTable of the "joinTable", and which joins to
     * the table of the class.
     * @param className The class name to generate the statement for
     * @return The SQLStatement
     */
    protected SQLStatement getSQLStatementForCandidateViaJoin(String className) {
        DatastoreClass table = storeMgr.getDatastoreClass(className, clr);
        if (table == null) {
        }
        SQLStatement stmt = new SQLStatement(storeMgr, joinTable, joinTableAlias, candidateTableGroupName);
        stmt.setClassLoaderResolver(clr);
        stmt.setCandidateClassName(className);
        JavaTypeMapping candidateIdMapping = candidateTable.getIdMapping();
        SQLTable candidateSQLTable = null;
        if (hasOption(OPTION_ALLOW_NULLS)) {
            candidateSQLTable = stmt.leftOuterJoin(null, joinElementMapping, candidateTable, null, candidateIdMapping, null, stmt.getPrimaryTable().getGroupName());
        } else {
            candidateSQLTable = stmt.innerJoin(null, joinElementMapping, candidateTable, null, candidateIdMapping, null, stmt.getPrimaryTable().getGroupName());
        }
        if (table != candidateTable) {
            JavaTypeMapping tableIdMapping = table.getIdMapping();
            stmt.innerJoin(candidateSQLTable, candidateIdMapping, table, null, tableIdMapping, null, stmt.getPrimaryTable().getGroupName());
        }
        SQLExpressionFactory factory = storeMgr.getSQLExpressionFactory();
        JavaTypeMapping discriminatorMapping = table.getDiscriminatorMapping(false);
        DiscriminatorMetaData discriminatorMetaData = table.getDiscriminatorMetaData();
        if (discriminatorMapping != null && discriminatorMetaData.getStrategy() != DiscriminatorStrategy.NONE) {
            BooleanExpression discExpr = SQLStatementHelper.getExpressionForDiscriminatorForClass(stmt, className, discriminatorMetaData, discriminatorMapping, stmt.getPrimaryTable(), clr);
            stmt.whereAnd(discExpr, false);
        }
        Iterator<String> subIter = storeMgr.getSubClassesForClass(className, false, clr).iterator();
        while (subIter.hasNext()) {
            String subclassName = subIter.next();
            DatastoreClass[] subclassTables = null;
            DatastoreClass subclassTable = storeMgr.getDatastoreClass(subclassName, clr);
            if (subclassTable == null) {
                AbstractClassMetaData targetSubCmd = storeMgr.getNucleusContext().getMetaDataManager().getMetaDataForClass(subclassName, clr);
                AbstractClassMetaData[] targetSubCmds = storeMgr.getClassesManagingTableForClass(targetSubCmd, clr);
                subclassTables = new DatastoreClass[targetSubCmds.length];
                for (int i = 0; i < targetSubCmds.length; i++) {
                    subclassTables[i] = storeMgr.getDatastoreClass(targetSubCmds[i].getFullClassName(), clr);
                }
            } else {
                subclassTables = new DatastoreClass[1];
                subclassTables[0] = subclassTable;
            }
            for (int i = 0; i < subclassTables.length; i++) {
                if (subclassTables[i] != table) {
                    JavaTypeMapping subclassIdMapping = subclassTables[i].getIdMapping();
                    SQLTable sqlTableSubclass = stmt.leftOuterJoin(null, joinElementMapping, subclassTables[i], null, subclassIdMapping, null, stmt.getPrimaryTable().getGroupName());
                    SQLExpression subclassIdExpr = factory.newExpression(stmt, sqlTableSubclass, subclassIdMapping);
                    SQLExpression nullExpr = new NullLiteral(stmt, null, null, null);
                    stmt.whereAnd(subclassIdExpr.eq(nullExpr), false);
                }
            }
        }
        if (hasOption(OPTION_SELECT_NUCLEUS_TYPE)) {
            addTypeSelectForClass(stmt, className);
        }
        return stmt;
    }

    /**
     * Convenience method to add a SELECT of a dummy column accessible as "NUCLEUS_TYPE" storing the
     * class name.
     * @param stmt SQLStatement
     * @param className Name of the class
     */
    private void addTypeSelectForClass(SQLStatement stmt, String className) {
        if (hasOption(OPTION_SELECT_NUCLEUS_TYPE)) {
            JavaTypeMapping m = storeMgr.getMappingManager().getMapping(String.class);
            String nuctypeName = className;
            if (maxClassNameLength > nuctypeName.length()) {
                nuctypeName = StringUtils.leftAlignedPaddedString(nuctypeName, maxClassNameLength);
            }
            StringLiteral lit = new StringLiteral(stmt, m, nuctypeName, null);
            stmt.select(lit, NUC_TYPE_COLUMN);
        }
    }
}
