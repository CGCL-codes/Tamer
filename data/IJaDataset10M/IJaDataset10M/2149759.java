package org.datanucleus.store.rdbms.query2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.jdo.spi.PersistenceCapable;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.FetchPlan;
import org.datanucleus.ObjectManager;
import org.datanucleus.StateManager;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.Relation;
import org.datanucleus.query.QueryUtils;
import org.datanucleus.query.compiler.CompilationComponent;
import org.datanucleus.query.compiler.QueryCompilation;
import org.datanucleus.query.evaluator.AbstractExpressionEvaluator;
import org.datanucleus.query.expression.CastExpression;
import org.datanucleus.query.expression.ClassExpression;
import org.datanucleus.query.expression.CreatorExpression;
import org.datanucleus.query.expression.DyadicExpression;
import org.datanucleus.query.expression.Expression;
import org.datanucleus.query.expression.InvokeExpression;
import org.datanucleus.query.expression.JoinExpression;
import org.datanucleus.query.expression.Literal;
import org.datanucleus.query.expression.OrderExpression;
import org.datanucleus.query.expression.ParameterExpression;
import org.datanucleus.query.expression.PrimaryExpression;
import org.datanucleus.query.expression.VariableExpression;
import org.datanucleus.query.expression.JoinExpression.JoinType;
import org.datanucleus.query.symbol.Symbol;
import org.datanucleus.store.mapped.DatastoreClass;
import org.datanucleus.store.mapped.StatementClassMapping;
import org.datanucleus.store.mapped.StatementMappingIndex;
import org.datanucleus.store.mapped.StatementNewObjectMapping;
import org.datanucleus.store.mapped.StatementParameterMapping;
import org.datanucleus.store.mapped.StatementResultMapping;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.mapped.mapping.MappingConsumer;
import org.datanucleus.store.query.QueryCompilerSyntaxException;
import org.datanucleus.store.rdbms.RDBMSManager;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.SQLStatementHelper;
import org.datanucleus.store.rdbms.sql.SQLTable;
import org.datanucleus.store.rdbms.sql.expression.BooleanExpression;
import org.datanucleus.store.rdbms.sql.expression.NewObjectExpression;
import org.datanucleus.store.rdbms.sql.expression.ParameterLiteral;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpressionFactory;
import org.datanucleus.store.rdbms.sql.expression.SQLLiteral;
import org.datanucleus.store.rdbms.table.CollectionTable;
import org.datanucleus.util.ClassUtils;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.StringUtils;

/**
 * Class which maps a compiled query to an SQL query.
 * Will form part of DataNucleus AccessPlatform 1.1 and later.
 * Takes in an SQLStatement, and use of compile() will update the SQLStatement to reflect
 * the filter, result, grouping, having, ordering etc.
 */
public class QueryToSQLMapper extends AbstractExpressionEvaluator implements QueryGenerator {

    final String candidateAlias;

    final AbstractClassMetaData candidateCmd;

    final QueryCompilation compilation;

    /** Input parameter values, keyed by the parameter name. Will be null if compiled pre-execution. */
    final Map parameters;

    /** SQL statement that we are updating. */
    final SQLStatement stmt;

    /** Definition of mapping for the results of this SQL statement (candidate). */
    final StatementClassMapping resultDefinitionForClass;

    /** Definition of mapping for the results of this SQL statement (result). */
    final StatementResultMapping resultDefinition;

    /** Definition of mapping for any input parameters of this SQL statement. */
    StatementParameterMapping parameterDefinition;

    final RDBMSManager storeMgr;

    final FetchPlan fetchPlan;

    final SQLExpressionFactory exprFactory;

    ObjectManager om;

    ClassLoaderResolver clr;

    Map<String, Object> compileProperties = new HashMap();

    /** State variable for the component being compiled. */
    CompilationComponent compileComponent;

    /** State variable for the number of the next parameter. */
    int paramNumber = 1;

    /** Stack of expressions, used for compilation of the query into SQL. */
    Stack<SQLExpression> stack = new Stack();

    /** Map of SQLTable/mapping keyed by the name of the primary that it relates to. */
    Map<String, SQLTableMapping> sqlTableByPrimary = new HashMap<String, SQLTableMapping>();

    /** Map of parameter position, keyed by the parameter expression. */
    Map<SQLExpression, int[]> paramPositionByExpression = new HashMap<SQLExpression, int[]>();

    boolean caseInsensitive = false;

    /**
     * State variable for whether this query is precompilable (hence whether it is cacheable).
     * Or in other words, whether we can compile it without knowing parameter values.
     */
    boolean precompilable = true;

    class SQLTableMapping {

        SQLTable table;

        AbstractClassMetaData cmd;

        JavaTypeMapping mapping;

        public SQLTableMapping(SQLTable tbl, AbstractClassMetaData cmd, JavaTypeMapping m) {
            this.table = tbl;
            this.cmd = cmd;
            this.mapping = m;
        }

        public String toString() {
            return "SQLTableMapping: tbl=" + table + " class=" + cmd.getFullClassName() + " mapping=" + mapping;
        }
    }

    /**
     * Constructor.
     * @param stmt SQL Statement to update with the query contents.
     * @param compilation The generic query compilation
     * @param parameters Parameters needed
     * @param resultDefForClass Definition of results for the statement (populated here)
     * @param resultDef Definition of results if we have a result clause (populated here)
     * @param cmd Metadata for the candidate class
     * @param fetchPlan Fetch Plan to apply
     * @param paramInfo Definition of parameters for the statement (populated here)
     * @param om ObjectManager
     */
    public QueryToSQLMapper(SQLStatement stmt, QueryCompilation compilation, Map parameters, StatementClassMapping resultDefForClass, StatementResultMapping resultDef, AbstractClassMetaData cmd, FetchPlan fetchPlan, StatementParameterMapping paramInfo, ObjectManager om) {
        this.parameters = parameters;
        this.compilation = compilation;
        this.stmt = stmt;
        this.resultDefinitionForClass = resultDefForClass;
        this.resultDefinition = resultDef;
        this.parameterDefinition = paramInfo;
        this.candidateAlias = compilation.getCandidateAlias();
        this.fetchPlan = fetchPlan;
        this.storeMgr = stmt.getRDBMSManager();
        this.exprFactory = stmt.getRDBMSManager().getSQLExpressionFactory();
        this.candidateCmd = cmd;
        this.om = om;
        this.clr = om.getClassLoaderResolver();
        if (compilation.getQueryLanguage().equalsIgnoreCase("JPQL")) {
            caseInsensitive = true;
        }
        this.stmt.setQueryGenerator(this);
        SQLTableMapping tblMapping = new SQLTableMapping(stmt.getPrimaryTable(), candidateCmd, stmt.getPrimaryTable().getTable().getIDMapping());
        setSQLTableMappingForAlias(candidateAlias, tblMapping);
    }

    /**
     * Accessor for the query language that this query pertains to.
     * Can be used to decide how to handle the input.
     * @return The query language
     */
    public String getQueryLanguage() {
        return compilation.getQueryLanguage();
    }

    public ClassLoaderResolver getClassLoaderResolver() {
        return clr;
    }

    public CompilationComponent getCompilationComponent() {
        return compileComponent;
    }

    public ObjectManager getObjectManager() {
        return om;
    }

    public Object getProperty(String name) {
        return compileProperties.get(name);
    }

    /**
     * Accessor for whether the query is precompilable (doesn't need to know parameter values
     * to be able to compile it).
     * @return Whether the query is precompilable and hence cacheable
     */
    public boolean isPrecompilable() {
        return precompilable;
    }

    public void useParameterExpressionAsLiteral(SQLLiteral paramLiteral) {
        int[] exprPositions = paramPositionByExpression.get(paramLiteral);
        if (exprPositions != null) {
            int increment = exprPositions.length;
            String[] paramNames = parameterDefinition.getParameterNames();
            for (int i = 0; i < paramNames.length; i++) {
                StatementMappingIndex idx = parameterDefinition.getMappingForParameter(paramNames[i]);
                int numOccurs = idx.getNumberOfParameterOccurrences();
                for (int j = 0; j < numOccurs; j++) {
                    int[] posns = idx.getParameterPositionsForOccurrence(j);
                    if (posns[0] == exprPositions[0]) {
                        idx.removeParameterOccurrence(posns);
                        paramNumber = paramNumber - increment;
                        NucleusLogger.QUERY.debug(">> Removed parameter positions for " + paramLiteral + " : was " + StringUtils.intArrayToString(exprPositions));
                    } else if (posns[0] > exprPositions[0]) {
                        for (int k = 0; k < posns.length; k++) {
                            posns[k] = posns[k] - increment;
                        }
                        NucleusLogger.QUERY.debug(">> Incremented back parameter positions for " + paramLiteral + " to " + StringUtils.intArrayToString(posns));
                    }
                }
            }
        }
        paramLiteral.setNotParameter();
        precompilable = false;
    }

    /**
     * Method to update the supplied SQLStatement with the components of the specified query.
     * During the compilation process this updates the SQLStatement "compileComponent" to the
     * component of the query being compiled.
     */
    public void compile() {
        NucleusLogger.QUERY.debug(compilation.toString());
        if (compilation.getExprFrom() != null) {
            compileComponent = CompilationComponent.FROM;
            Expression[] fromExprs = compilation.getExprFrom();
            for (int i = 0; i < fromExprs.length; i++) {
                ClassExpression clsExpr = (ClassExpression) fromExprs[i];
                compileFromClassExpression(clsExpr);
            }
        }
        if (compilation.getExprResult() != null) {
            compileComponent = CompilationComponent.RESULT;
            Expression[] resultExprs = compilation.getExprResult();
            for (int i = 0; i < resultExprs.length; i++) {
                if (resultExprs[i] instanceof InvokeExpression) {
                    processInvokeExpression((InvokeExpression) resultExprs[i]);
                    SQLExpression sqlExpr = stack.pop();
                    int col = stmt.select(sqlExpr, null);
                    StatementMappingIndex idx = new StatementMappingIndex(sqlExpr.getJavaTypeMapping());
                    idx.setColumnPositions(new int[] { col });
                    resultDefinition.addMappingForResultExpression(i, idx);
                } else if (resultExprs[i] instanceof PrimaryExpression) {
                    processPrimaryExpression((PrimaryExpression) resultExprs[i]);
                    SQLExpression sqlExpr = stack.pop();
                    if (sqlExpr instanceof SQLLiteral) {
                        int col = stmt.select(sqlExpr, null);
                        StatementMappingIndex idx = new StatementMappingIndex(sqlExpr.getJavaTypeMapping());
                        idx.setColumnPositions(new int[] { col });
                        resultDefinition.addMappingForResultExpression(i, idx);
                    } else {
                        int[] cols = stmt.select(sqlExpr.getSQLTable(), sqlExpr.getJavaTypeMapping(), null);
                        StatementMappingIndex idx = new StatementMappingIndex(sqlExpr.getJavaTypeMapping());
                        idx.setColumnPositions(cols);
                        resultDefinition.addMappingForResultExpression(i, idx);
                    }
                } else if (resultExprs[i] instanceof ParameterExpression) {
                    processParameterExpression((ParameterExpression) resultExprs[i]);
                    SQLExpression sqlExpr = stack.pop();
                    int col = stmt.select(sqlExpr, null);
                    StatementMappingIndex idx = new StatementMappingIndex(sqlExpr.getJavaTypeMapping());
                    idx.setColumnPositions(new int[] { col });
                    resultDefinition.addMappingForResultExpression(i, idx);
                } else if (resultExprs[i] instanceof VariableExpression) {
                    processVariableExpression((VariableExpression) resultExprs[i]);
                    SQLExpression sqlExpr = stack.pop();
                    int col = stmt.select(sqlExpr, null);
                    StatementMappingIndex idx = new StatementMappingIndex(sqlExpr.getJavaTypeMapping());
                    idx.setColumnPositions(new int[] { col });
                    resultDefinition.addMappingForResultExpression(i, idx);
                } else if (resultExprs[i] instanceof Literal) {
                    processLiteral((Literal) resultExprs[i]);
                    SQLExpression sqlExpr = stack.pop();
                    int col = stmt.select(sqlExpr, null);
                    StatementMappingIndex idx = new StatementMappingIndex(sqlExpr.getJavaTypeMapping());
                    idx.setColumnPositions(new int[] { col });
                    resultDefinition.addMappingForResultExpression(i, idx);
                } else if (resultExprs[i] instanceof CreatorExpression) {
                    processCreatorExpression((CreatorExpression) resultExprs[i]);
                    NewObjectExpression sqlExpr = (NewObjectExpression) stack.pop();
                    StatementNewObjectMapping stmtMap = getStatementMappingForNewObjectExpression(sqlExpr);
                    resultDefinition.addMappingForResultExpression(i, stmtMap);
                } else {
                    throw new NucleusException("Dont currently support result clause containing expression of type " + resultExprs[i]);
                }
            }
            if (stmt.getNumberOfSelects() == 0) {
                stmt.select(exprFactory.newLiteral(stmt, storeMgr.getMappingManager().getMapping(Integer.class), 1), null);
            }
        } else {
            SQLStatementHelper.selectFetchPlanOfCandidateInStatement(stmt, resultDefinitionForClass, fetchPlan, candidateCmd, 1);
        }
        if (compilation.getExprFilter() != null) {
            compileComponent = CompilationComponent.FILTER;
            if (QueryUtils.expressionHasOrOperator(compilation.getExprFilter())) {
                compileProperties.put("Filter.OR", true);
            }
            if (QueryUtils.expressionHasNotOperator(compilation.getExprFilter())) {
                compileProperties.put("Filter.NOT", true);
            }
            BooleanExpression filterExpr = (BooleanExpression) compilation.getExprFilter().evaluate(this);
            stmt.whereAnd(filterExpr, true);
        }
        if (compilation.getExprGrouping() != null) {
            compileComponent = CompilationComponent.GROUPING;
            Expression[] groupExprs = compilation.getExprGrouping();
            for (int i = 0; i < groupExprs.length; i++) {
                Expression groupExpr = groupExprs[i];
                SQLExpression sqlGroupExpr = (SQLExpression) groupExpr.evaluate(this);
                stmt.addGroupingExpression(sqlGroupExpr);
            }
        }
        if (compilation.getExprHaving() != null) {
            compileComponent = CompilationComponent.HAVING;
            Expression havingExpr = compilation.getExprHaving();
            BooleanExpression sqlHavingExpr = (BooleanExpression) havingExpr.evaluate(this);
            stmt.setHaving(sqlHavingExpr);
        }
        if (stmt.getNumberOfUnions() > 0) {
            String[] paramNames = parameterDefinition.getParameterNames();
            if (paramNames != null) {
                Map<String, Integer> paramCols = new HashMap();
                for (int j = 0; j < paramNames.length; j++) {
                    StatementMappingIndex idx = parameterDefinition.getMappingForParameter(paramNames[j]);
                    paramCols.put(paramNames[j], idx.getNumberOfParameterOccurrences());
                }
                for (int i = 0; i < stmt.getNumberOfUnions(); i++) {
                    for (int j = 0; j < paramNames.length; j++) {
                        int numOccurrences = paramCols.get(paramNames[j]);
                        StatementMappingIndex idx = parameterDefinition.getMappingForParameter(paramNames[j]);
                        for (int k = 0; k < numOccurrences; k++) {
                            int[] paramPositions = idx.getParameterPositionsForOccurrence(k);
                            int[] newPositions = new int[paramPositions.length];
                            for (int l = 0; l < newPositions.length; l++) {
                                newPositions[l] = paramNumber++;
                            }
                            idx.addParameterOccurrence(newPositions);
                        }
                    }
                }
            }
        }
        if (compilation.getExprOrdering() != null) {
            compileComponent = CompilationComponent.ORDERING;
            Expression[] orderingExpr = compilation.getExprOrdering();
            SQLExpression[] orderSqlExprs = new SQLExpression[orderingExpr.length];
            boolean[] directions = new boolean[orderingExpr.length];
            for (int i = 0; i < orderingExpr.length; i++) {
                OrderExpression orderExpr = (OrderExpression) orderingExpr[i];
                orderSqlExprs[i] = (SQLExpression) orderExpr.getLeft().evaluate(this);
                String orderDir = orderExpr.getSortOrder();
                directions[i] = ((orderDir == null || orderDir.equals("ascending")) ? false : true);
            }
            stmt.setOrdering(orderSqlExprs, directions);
        }
        compileComponent = null;
        Collection<String> symbols = compilation.getSymbolTable().getSymbolNames();
        Iterator<String> symIter = symbols.iterator();
        while (symIter.hasNext()) {
            Symbol sym = compilation.getSymbolTable().getSymbol(symIter.next());
            if (sym.getType() == Symbol.VARIABLE) {
                if (!hasSQLTableMappingForAlias(sym.getQualifiedName())) {
                    throw new QueryCompilerSyntaxException("Query has variable \"" + sym.getQualifiedName() + "\" which is not bound to the query");
                }
            }
        }
    }

    /**
     * Method to take a ClassExpression (in a FROM clause) and process the candidate and any
     * linked JoinExpression(s), adding joins to the SQLStatement as required.
     * @param clsExpr The ClassExpression
     */
    protected void compileFromClassExpression(ClassExpression clsExpr) {
        Symbol clsExprSym = clsExpr.getSymbol();
        Class baseCls = clsExprSym.getValueType();
        SQLTable candSqlTbl = stmt.getPrimaryTable();
        AbstractClassMetaData cmd = storeMgr.getMetaDataManager().getMetaDataForClass(baseCls, clr);
        if (baseCls != null && baseCls != compilation.getCandidateClass()) {
            DatastoreClass candTbl = storeMgr.getDatastoreClass(baseCls.getName(), clr);
            candSqlTbl = stmt.crossJoin(candTbl, clsExpr.getAlias(), null);
            SQLTableMapping tblMapping = new SQLTableMapping(candSqlTbl, cmd, candTbl.getIDMapping());
            setSQLTableMappingForAlias(clsExpr.getAlias(), tblMapping);
        }
        Expression rightExpr = clsExpr.getRight();
        while (rightExpr != null) {
            if (rightExpr instanceof JoinExpression) {
                JoinExpression joinExpr = (JoinExpression) rightExpr;
                JoinType joinType = joinExpr.getType();
                String joinAlias = joinExpr.getAlias();
                PrimaryExpression joinPrimExpr = joinExpr.getPrimaryExpression();
                List<String> joinPrimTuples = joinPrimExpr.getTuples();
                Iterator<String> iter = joinPrimTuples.iterator();
                iter.next();
                SQLTable sqlTbl = candSqlTbl;
                while (iter.hasNext()) {
                    String id = iter.next();
                    AbstractMemberMetaData mmd = cmd.getMetaDataForMember(id);
                    int relationType = mmd.getRelationType(clr);
                    DatastoreClass relTable = null;
                    AbstractMemberMetaData relMmd = null;
                    switch(relationType) {
                        case Relation.ONE_TO_ONE_UNI:
                            relTable = storeMgr.getDatastoreClass(mmd.getTypeName(), clr);
                            cmd = storeMgr.getMetaDataManager().getMetaDataForClass(mmd.getType(), clr);
                            if (joinType == JoinType.JOIN_INNER || joinType == JoinType.JOIN_INNER_FETCH) {
                                sqlTbl = stmt.innerJoin(sqlTbl, sqlTbl.getTable().getMemberMapping(mmd), relTable, null, relTable.getIDMapping(), null, null);
                            } else {
                                sqlTbl = stmt.leftOuterJoin(sqlTbl, sqlTbl.getTable().getMemberMapping(mmd), relTable, null, relTable.getIDMapping(), null, null);
                            }
                            break;
                        case Relation.ONE_TO_ONE_BI:
                            relTable = storeMgr.getDatastoreClass(mmd.getTypeName(), clr);
                            cmd = storeMgr.getMetaDataManager().getMetaDataForClass(mmd.getType(), clr);
                            if (mmd.getMappedBy() != null) {
                                relMmd = mmd.getRelatedMemberMetaData(clr)[0];
                                if (joinType == JoinType.JOIN_INNER || joinType == JoinType.JOIN_INNER_FETCH) {
                                    sqlTbl = stmt.innerJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), relTable, null, relTable.getMemberMapping(relMmd), null, null);
                                } else {
                                    sqlTbl = stmt.leftOuterJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), relTable, null, relTable.getMemberMapping(relMmd), null, null);
                                }
                            } else {
                                if (joinType == JoinType.JOIN_INNER || joinType == JoinType.JOIN_INNER_FETCH) {
                                    sqlTbl = stmt.innerJoin(sqlTbl, sqlTbl.getTable().getMemberMapping(mmd), relTable, null, relTable.getIDMapping(), null, null);
                                } else {
                                    sqlTbl = stmt.leftOuterJoin(sqlTbl, sqlTbl.getTable().getMemberMapping(mmd), relTable, null, relTable.getIDMapping(), null, null);
                                }
                            }
                            break;
                        case Relation.ONE_TO_MANY_BI:
                            relTable = storeMgr.getDatastoreClass(mmd.getCollection().getElementType(), clr);
                            cmd = mmd.getCollection().getElementClassMetaData(clr);
                            relMmd = mmd.getRelatedMemberMetaData(clr)[0];
                            if (mmd.getJoinMetaData() != null || relMmd.getJoinMetaData() != null) {
                                CollectionTable joinTbl = (CollectionTable) storeMgr.getDatastoreContainerObject(mmd);
                                if (joinType == JoinType.JOIN_INNER || joinType == JoinType.JOIN_INNER_FETCH) {
                                    SQLTable joinSqlTbl = stmt.innerJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), joinTbl, null, joinTbl.getOwnerMapping(), null, null);
                                    sqlTbl = stmt.innerJoin(joinSqlTbl, joinTbl.getElementMapping(), relTable, null, relTable.getIDMapping(), null, null);
                                } else {
                                    SQLTable joinSqlTbl = stmt.leftOuterJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), joinTbl, null, joinTbl.getOwnerMapping(), null, null);
                                    sqlTbl = stmt.leftOuterJoin(joinSqlTbl, joinTbl.getElementMapping(), relTable, null, relTable.getIDMapping(), null, null);
                                }
                            } else {
                                if (joinType == JoinType.JOIN_INNER || joinType == JoinType.JOIN_INNER_FETCH) {
                                    sqlTbl = stmt.innerJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), relTable, null, relTable.getMemberMapping(relMmd), null, null);
                                } else {
                                    sqlTbl = stmt.leftOuterJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), relTable, null, relTable.getMemberMapping(relMmd), null, null);
                                }
                            }
                            break;
                        case Relation.ONE_TO_MANY_UNI:
                            relTable = storeMgr.getDatastoreClass(mmd.getCollection().getElementType(), clr);
                            cmd = mmd.getCollection().getElementClassMetaData(clr);
                            if (mmd.getJoinMetaData() != null) {
                                CollectionTable joinTbl = (CollectionTable) storeMgr.getDatastoreContainerObject(mmd);
                                if (joinType == JoinType.JOIN_INNER || joinType == JoinType.JOIN_INNER_FETCH) {
                                    SQLTable joinSqlTbl = stmt.innerJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), joinTbl, null, joinTbl.getOwnerMapping(), null, null);
                                    sqlTbl = stmt.innerJoin(joinSqlTbl, joinTbl.getElementMapping(), relTable, null, relTable.getIDMapping(), null, null);
                                } else {
                                    SQLTable joinSqlTbl = stmt.leftOuterJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), joinTbl, null, joinTbl.getOwnerMapping(), null, null);
                                    sqlTbl = stmt.leftOuterJoin(joinSqlTbl, joinTbl.getElementMapping(), relTable, null, relTable.getIDMapping(), null, null);
                                }
                            } else {
                                JavaTypeMapping relMapping = relTable.getExternalMapping(mmd, MappingConsumer.MAPPING_TYPE_EXTERNAL_FK);
                                if (joinType == JoinType.JOIN_INNER || joinType == JoinType.JOIN_INNER_FETCH) {
                                    sqlTbl = stmt.innerJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), relTable, null, relMapping, null, null);
                                } else {
                                    sqlTbl = stmt.leftOuterJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), relTable, null, relMapping, null, null);
                                }
                            }
                            break;
                        case Relation.MANY_TO_MANY_BI:
                            relTable = storeMgr.getDatastoreClass(mmd.getCollection().getElementType(), clr);
                            cmd = mmd.getCollection().getElementClassMetaData(clr);
                            relMmd = mmd.getRelatedMemberMetaData(clr)[0];
                            CollectionTable joinTbl = (CollectionTable) storeMgr.getDatastoreContainerObject(mmd);
                            if (joinType == JoinType.JOIN_INNER || joinType == JoinType.JOIN_INNER_FETCH) {
                                SQLTable joinSqlTbl = stmt.innerJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), joinTbl, null, joinTbl.getOwnerMapping(), null, null);
                                sqlTbl = stmt.innerJoin(joinSqlTbl, joinTbl.getElementMapping(), relTable, null, relTable.getIDMapping(), null, null);
                            } else {
                                SQLTable joinSqlTbl = stmt.leftOuterJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), joinTbl, null, joinTbl.getOwnerMapping(), null, null);
                                sqlTbl = stmt.leftOuterJoin(joinSqlTbl, joinTbl.getElementMapping(), relTable, null, relTable.getIDMapping(), null, null);
                            }
                            break;
                        case Relation.MANY_TO_ONE_BI:
                            relTable = storeMgr.getDatastoreClass(mmd.getTypeName(), clr);
                            cmd = storeMgr.getMetaDataManager().getMetaDataForClass(mmd.getType(), clr);
                            relMmd = mmd.getRelatedMemberMetaData(clr)[0];
                            if (mmd.getJoinMetaData() != null || relMmd.getJoinMetaData() != null) {
                                joinTbl = (CollectionTable) storeMgr.getDatastoreContainerObject(relMmd);
                                if (joinType == JoinType.JOIN_INNER || joinType == JoinType.JOIN_INNER_FETCH) {
                                    SQLTable joinSqlTbl = stmt.innerJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), joinTbl, null, joinTbl.getElementMapping(), null, null);
                                    sqlTbl = stmt.innerJoin(joinSqlTbl, joinTbl.getOwnerMapping(), relTable, null, relTable.getIDMapping(), null, null);
                                } else {
                                    SQLTable joinSqlTbl = stmt.leftOuterJoin(sqlTbl, sqlTbl.getTable().getIDMapping(), joinTbl, null, joinTbl.getElementMapping(), null, null);
                                    sqlTbl = stmt.leftOuterJoin(joinSqlTbl, joinTbl.getOwnerMapping(), relTable, null, relTable.getIDMapping(), null, null);
                                }
                            } else {
                                JavaTypeMapping fkMapping = sqlTbl.getTable().getMemberMapping(mmd);
                                if (joinType == JoinType.JOIN_INNER || joinType == JoinType.JOIN_INNER_FETCH) {
                                    sqlTbl = stmt.innerJoin(sqlTbl, fkMapping, relTable, null, relTable.getIDMapping(), null, null);
                                } else {
                                    sqlTbl = stmt.leftOuterJoin(sqlTbl, fkMapping, relTable, null, relTable.getIDMapping(), null, null);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
                SQLTableMapping tblMapping = new SQLTableMapping(sqlTbl, cmd, sqlTbl.getTable().getIDMapping());
                setSQLTableMappingForAlias(joinAlias, tblMapping);
            }
            rightExpr = rightExpr.getRight();
        }
    }

    /**
     * Convenience method to convert a NewObjectExpression into a StatementNewObjectMapping.
     * Handles recursive new object calls (where a new object is an arg to a new object construction).
     * @param expr The NewObjectExpression
     * @return The mapping for the new object
     */
    protected StatementNewObjectMapping getStatementMappingForNewObjectExpression(NewObjectExpression expr) {
        List argExprs = expr.getConstructorArgExpressions();
        StatementNewObjectMapping stmtMap = new StatementNewObjectMapping(expr.getNewClass());
        if (argExprs != null) {
            Iterator<SQLExpression> argIter = argExprs.iterator();
            int j = 0;
            while (argIter.hasNext()) {
                SQLExpression argExpr = argIter.next();
                if (argExpr instanceof SQLLiteral) {
                    stmtMap.addConstructorArgMapping(j, ((SQLLiteral) argExpr).getValue());
                } else if (argExpr instanceof NewObjectExpression) {
                    stmtMap.addConstructorArgMapping(j, getStatementMappingForNewObjectExpression((NewObjectExpression) argExpr));
                } else {
                    StatementMappingIndex idx = new StatementMappingIndex(argExpr.getJavaTypeMapping());
                    int col = stmt.select(argExpr, null);
                    idx.setColumnPositions(new int[] { col });
                    stmtMap.addConstructorArgMapping(j, idx);
                }
                j++;
            }
        }
        return stmtMap;
    }

    protected Object processAndExpression(Expression expr) {
        BooleanExpression rightExpr = (BooleanExpression) stack.pop();
        BooleanExpression leftExpr = (BooleanExpression) stack.pop();
        BooleanExpression opExpr = leftExpr.and(rightExpr);
        stack.push(opExpr);
        return opExpr;
    }

    @Override
    protected Object processOrExpression(Expression expr) {
        BooleanExpression rightExpr = (BooleanExpression) stack.pop();
        BooleanExpression leftExpr = (BooleanExpression) stack.pop();
        BooleanExpression opExpr = leftExpr.ior(rightExpr);
        stack.push(opExpr);
        return opExpr;
    }

    protected Object processEqExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        if (left instanceof ParameterLiteral && !(right instanceof ParameterLiteral)) {
            left = replaceParameterLiteral((ParameterLiteral) left, right.getJavaTypeMapping());
        } else if (right instanceof ParameterLiteral && !(left instanceof ParameterLiteral)) {
            right = replaceParameterLiteral((ParameterLiteral) right, left.getJavaTypeMapping());
        }
        if (left.isParameter() && right.isParameter()) {
            if (left.isParameter() && left instanceof SQLLiteral && ((SQLLiteral) left).getValue() != null) {
                useParameterExpressionAsLiteral((SQLLiteral) left);
            }
            if (right.isParameter() && right instanceof SQLLiteral && ((SQLLiteral) right).getValue() != null) {
                useParameterExpressionAsLiteral((SQLLiteral) right);
            }
        }
        BooleanExpression opExpr = left.eq(right);
        stack.push(opExpr);
        return opExpr;
    }

    @Override
    protected Object processNoteqExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        if (left instanceof ParameterLiteral && !(right instanceof ParameterLiteral)) {
            left = replaceParameterLiteral((ParameterLiteral) left, right.getJavaTypeMapping());
        } else if (right instanceof ParameterLiteral && !(left instanceof ParameterLiteral)) {
            right = replaceParameterLiteral((ParameterLiteral) right, left.getJavaTypeMapping());
        }
        if (left.isParameter() && right.isParameter()) {
            if (left.isParameter() && left instanceof SQLLiteral && ((SQLLiteral) left).getValue() != null) {
                useParameterExpressionAsLiteral((SQLLiteral) left);
            }
            if (right.isParameter() && right instanceof SQLLiteral && ((SQLLiteral) right).getValue() != null) {
                useParameterExpressionAsLiteral((SQLLiteral) right);
            }
        }
        BooleanExpression opExpr = left.ne(right);
        stack.push(opExpr);
        return opExpr;
    }

    @Override
    protected Object processGteqExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        if (left instanceof ParameterLiteral && !(right instanceof ParameterLiteral)) {
            left = replaceParameterLiteral((ParameterLiteral) left, right.getJavaTypeMapping());
        } else if (right instanceof ParameterLiteral && !(left instanceof ParameterLiteral)) {
            right = replaceParameterLiteral((ParameterLiteral) right, left.getJavaTypeMapping());
        }
        BooleanExpression opExpr = left.ge(right);
        stack.push(opExpr);
        return opExpr;
    }

    @Override
    protected Object processGtExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        if (left instanceof ParameterLiteral && !(right instanceof ParameterLiteral)) {
            left = replaceParameterLiteral((ParameterLiteral) left, right.getJavaTypeMapping());
        } else if (right instanceof ParameterLiteral && !(left instanceof ParameterLiteral)) {
            right = replaceParameterLiteral((ParameterLiteral) right, left.getJavaTypeMapping());
        }
        BooleanExpression opExpr = left.gt(right);
        stack.push(opExpr);
        return opExpr;
    }

    @Override
    protected Object processLteqExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        if (left instanceof ParameterLiteral && !(right instanceof ParameterLiteral)) {
            left = replaceParameterLiteral((ParameterLiteral) left, right.getJavaTypeMapping());
        } else if (right instanceof ParameterLiteral && !(left instanceof ParameterLiteral)) {
            right = replaceParameterLiteral((ParameterLiteral) right, left.getJavaTypeMapping());
        }
        BooleanExpression opExpr = left.le(right);
        stack.push(opExpr);
        return opExpr;
    }

    @Override
    protected Object processLtExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        if (left instanceof ParameterLiteral && !(right instanceof ParameterLiteral)) {
            left = replaceParameterLiteral((ParameterLiteral) left, right.getJavaTypeMapping());
        } else if (right instanceof ParameterLiteral && !(left instanceof ParameterLiteral)) {
            right = replaceParameterLiteral((ParameterLiteral) right, left.getJavaTypeMapping());
        }
        BooleanExpression opExpr = left.lt(right);
        stack.push(opExpr);
        return opExpr;
    }

    protected Object processLiteral(Literal expr) {
        Object litValue = expr.getLiteral();
        if (litValue instanceof Class) {
            litValue = ((Class) litValue).getName();
        }
        JavaTypeMapping m = null;
        if (litValue != null) {
            m = exprFactory.getMappingForType(litValue.getClass(), false);
        }
        SQLExpression sqlExpr = exprFactory.newLiteral(stmt, m, litValue);
        stack.push(sqlExpr);
        return sqlExpr;
    }

    protected Object processPrimaryExpression(PrimaryExpression expr) {
        SQLExpression sqlExpr = null;
        if (expr.getLeft() != null) {
            if (expr.getLeft() instanceof ParameterExpression) {
                precompilable = false;
                ParameterExpression paramExpr = (ParameterExpression) expr.getLeft();
                Symbol paramSym = compilation.getSymbolTable().getSymbol(paramExpr.getId());
                if (paramSym.getValueType().isArray()) {
                    String first = expr.getTuples().get(0);
                    processParameterExpression(paramExpr, true);
                    SQLExpression paramSqlExpr = stack.pop();
                    sqlExpr = exprFactory.invokeMethod(stmt, "ARRAY", first, paramSqlExpr, null);
                    stack.push(sqlExpr);
                    return sqlExpr;
                } else {
                    processParameterExpression(paramExpr, true);
                    SQLExpression paramSqlExpr = stack.pop();
                    SQLLiteral lit = (SQLLiteral) paramSqlExpr;
                    Object paramValue = lit.getValue();
                    List<String> tuples = expr.getTuples();
                    Iterator<String> tuplesIter = tuples.iterator();
                    Object objValue = paramValue;
                    while (tuplesIter.hasNext()) {
                        String fieldName = tuplesIter.next();
                        objValue = getValueForObjectField(objValue, fieldName);
                        if (objValue == null) {
                            break;
                        }
                    }
                    if (objValue == null) {
                        sqlExpr = exprFactory.newLiteral(stmt, null, null);
                        stack.push(sqlExpr);
                        return sqlExpr;
                    } else {
                        JavaTypeMapping m = exprFactory.getMappingForType(objValue.getClass(), false);
                        sqlExpr = exprFactory.newLiteral(stmt, m, objValue);
                        stack.push(sqlExpr);
                        return sqlExpr;
                    }
                }
            } else if (expr.getLeft() instanceof VariableExpression) {
                VariableExpression varExpr = (VariableExpression) expr.getLeft();
                processVariableExpression(varExpr);
                SQLExpression varSqlExpr = stack.pop();
                NucleusLogger.QUERY.debug(">> PrimaryExpr left(var)=" + expr.getLeft() + " id=" + expr.getId() + " varExpr=" + varSqlExpr + " varType=" + varSqlExpr.getJavaTypeMapping().getType() + " varExpr.tbl=" + varSqlExpr.getSQLTable() + " stmt=" + stmt + " varExpr.stmt=" + varSqlExpr.getSQLStatement());
                Class varType = clr.classForName(varSqlExpr.getJavaTypeMapping().getType());
                AbstractClassMetaData varCmd = om.getMetaDataManager().getMetaDataForClass(varType, clr);
                if (varCmd != null) {
                    SQLTableMapping sqlMapping = getSQLTableMappingForPrimaryExpression(varExpr.getId(), expr.getTuples(), false);
                    NucleusLogger.QUERY.debug(">> QueryToSQL.processPrimary (VAR) sqlMapping=" + sqlMapping + " stmt(after)=" + stmt.getSelectStatement().toSQL());
                }
                throw new NucleusUserException("Dont currently support VariableExpression.field(s) : " + expr.getLeft() + "." + expr.getId());
            } else if (expr.getLeft() instanceof CastExpression) {
                CastExpression castExpr = (CastExpression) expr.getLeft();
                PrimaryExpression castLeftExpr = (PrimaryExpression) castExpr.getLeft();
                processPrimaryExpressionInternal(castLeftExpr, true);
                SQLExpression castLeftSqlExpr = stack.pop();
                String castClassName = castExpr.getClassName();
                SQLExpression castClassNameExpr = exprFactory.newLiteral(stmt, exprFactory.getMappingForType(String.class, false), castClassName);
                sqlExpr = castLeftSqlExpr.cast(castClassNameExpr);
                String exprPrimName = "CAST_" + castLeftExpr.getId();
                AbstractClassMetaData castCmd = om.getMetaDataManager().getMetaDataForClass(castClassName, clr);
                SQLTableMapping tblMapping = new SQLTableMapping(sqlExpr.getSQLTable(), castCmd, sqlExpr.getJavaTypeMapping());
                setSQLTableMappingForAlias(exprPrimName, tblMapping);
                SQLTableMapping sqlMapping = getSQLTableMappingForPrimaryExpression(exprPrimName, expr.getTuples(), false);
                if (sqlMapping == null) {
                    throw new NucleusException("PrimaryExpression " + expr + " is not yet supported");
                }
                sqlExpr = exprFactory.newExpression(stmt, sqlMapping.table, sqlMapping.mapping);
                stack.push(sqlExpr);
                return sqlExpr;
            } else if (expr.getLeft() instanceof InvokeExpression) {
                processInvokeExpression((InvokeExpression) expr.getLeft());
                SQLExpression invokeSqlExpr = stack.pop();
                throw new NucleusUserException("Dont currently support evaluating " + expr.getId() + " on " + invokeSqlExpr);
            } else {
                throw new NucleusUserException("Dont currently support PrimaryExpression with 'left' of " + expr.getLeft());
            }
        }
        return processPrimaryExpressionInternal(expr, false);
    }

    private SQLExpression processPrimaryExpressionInternal(PrimaryExpression primExpr, boolean forceJoin) {
        SQLTableMapping sqlMapping = getSQLTableMappingForPrimaryExpression(null, primExpr.getTuples(), forceJoin);
        if (sqlMapping == null) {
            throw new NucleusException("PrimaryExpression " + primExpr.getId() + " is not yet supported");
        }
        SQLExpression sqlExpr = exprFactory.newExpression(stmt, sqlMapping.table, sqlMapping.mapping);
        stack.push(sqlExpr);
        return sqlExpr;
    }

    /**
     * Method to take in a PrimaryExpression and return the SQLTable mapping info that it signifies.
     * If the primary expression implies joining to other objects then adds the joins to the statement.
     * Only adds joins if necessary; so if there is a further component after the required join, or if
     * the "forceJoin" flag is set.
     * @param exprName Name for an expression that this primary is relative to (optional)
     *                 If not specified then the tuples are relative to the candidate.
     *                 If specified then should have an entry in sqlTableByPrimary under this name.
     * @param tuples Tuples of the primary expression
     * @param forceJoin Whether to force a join if a relation member
     * @return The SQL table mapping information for the specified primary
     */
    private SQLTableMapping getSQLTableMappingForPrimaryExpression(String exprName, List<String> tuples, boolean forceJoin) {
        SQLTableMapping sqlMapping = null;
        Iterator<String> iter = tuples.iterator();
        String first = tuples.get(0);
        String primaryName = null;
        if (exprName != null) {
            sqlMapping = getSQLTableMappingForAlias(exprName);
            primaryName = exprName;
        } else if (hasSQLTableMappingForAlias(first)) {
            sqlMapping = getSQLTableMappingForAlias(first);
            primaryName = first;
            iter.next();
        } else {
            sqlMapping = getSQLTableMappingForAlias(candidateAlias);
            primaryName = candidateAlias;
        }
        while (iter.hasNext()) {
            String component = iter.next();
            primaryName += "." + component;
            SQLTableMapping sqlMappingNew = getSQLTableMappingForAlias(primaryName);
            if (sqlMappingNew == null) {
                AbstractMemberMetaData mmd = sqlMapping.cmd.getMetaDataForMember(component);
                if (mmd == null) {
                    throw new NucleusUserException("Query contains access of " + primaryName + " yet this field/property doesnt exist");
                }
                int relationType = mmd.getRelationType(clr);
                DatastoreClass table = storeMgr.getDatastoreClass(sqlMapping.cmd.getFullClassName(), clr);
                JavaTypeMapping mapping = table.getMemberMapping(mmd);
                SQLTable sqlTbl = SQLStatementHelper.getSQLTableForMappingOfTable(stmt, sqlMapping.table, mapping);
                switch(relationType) {
                    case Relation.NONE:
                        sqlMappingNew = new SQLTableMapping(sqlTbl, sqlMapping.cmd, mapping);
                        setSQLTableMappingForAlias(primaryName, sqlMappingNew);
                        break;
                    case Relation.ONE_TO_ONE_UNI:
                    case Relation.ONE_TO_ONE_BI:
                    case Relation.MANY_TO_ONE_BI:
                        if (mmd.getMappedBy() != null) {
                            AbstractMemberMetaData relMmd = mmd.getRelatedMemberMetaData(clr)[0];
                            DatastoreClass relTable = storeMgr.getDatastoreClass(mmd.getTypeName(), clr);
                            JavaTypeMapping relMapping = relTable.getMemberMapping(relMmd);
                            sqlTbl = stmt.getTable(relTable, primaryName);
                            if (sqlTbl == null) {
                                sqlTbl = SQLStatementHelper.addJoinForOneToOneRelation(stmt, sqlMapping.table.getTable().getIDMapping(), sqlMapping.table, relMapping, relTable, null, null, primaryName);
                            }
                            if (iter.hasNext()) {
                                sqlMappingNew = new SQLTableMapping(sqlTbl, relMmd.getAbstractClassMetaData(), relTable.getIDMapping());
                            } else {
                                sqlMappingNew = new SQLTableMapping(sqlTbl, sqlMapping.cmd, relTable.getIDMapping());
                            }
                        } else {
                            if (iter.hasNext() || forceJoin) {
                                AbstractClassMetaData relCmd = null;
                                JavaTypeMapping relMapping = null;
                                DatastoreClass relTable = null;
                                if (relationType == Relation.ONE_TO_ONE_BI) {
                                    AbstractMemberMetaData relMmd = mmd.getRelatedMemberMetaData(clr)[0];
                                    relCmd = relMmd.getAbstractClassMetaData();
                                } else {
                                    relCmd = om.getMetaDataManager().getMetaDataForClass(mmd.getTypeName(), clr);
                                }
                                relTable = storeMgr.getDatastoreClass(relCmd.getFullClassName(), clr);
                                relMapping = relTable.getIDMapping();
                                sqlTbl = stmt.getTable(relTable, primaryName);
                                if (sqlTbl == null) {
                                    sqlTbl = SQLStatementHelper.addJoinForOneToOneRelation(stmt, mapping, sqlMapping.table, relMapping, relTable, null, null, primaryName);
                                }
                                sqlMappingNew = new SQLTableMapping(sqlTbl, relCmd, relMapping);
                                setSQLTableMappingForAlias(primaryName, sqlMappingNew);
                            } else {
                                sqlMappingNew = new SQLTableMapping(sqlTbl, sqlMapping.cmd, mapping);
                                setSQLTableMappingForAlias(primaryName, sqlMappingNew);
                            }
                        }
                        break;
                    case Relation.ONE_TO_MANY_UNI:
                    case Relation.ONE_TO_MANY_BI:
                    case Relation.MANY_TO_MANY_BI:
                        sqlMappingNew = new SQLTableMapping(sqlTbl, sqlMapping.cmd, mapping);
                        setSQLTableMappingForAlias(primaryName, sqlMappingNew);
                        break;
                    default:
                        break;
                }
            }
            sqlMapping = sqlMappingNew;
        }
        return sqlMapping;
    }

    @Override
    protected Object processParameterExpression(ParameterExpression expr) {
        return processParameterExpression(expr, false);
    }

    /**
     * Method to process a parameter expression. The optional argument controls whether we should
     * create this as a parameter or as a literal (i.e the param value is known etc).
     * If the parameter doesn't have its value defined then returns ParameterLiteral
     * otherwise we get an XXXLiteral of the (declared) type of the parameter
     * @param expr The ParameterExpression
     * @param asLiteral Whether to create a SQLLiteral rather than a parameter literal
     */
    protected Object processParameterExpression(ParameterExpression expr, boolean asLiteral) {
        Object paramValue = null;
        if (parameters != null && parameters.containsKey(expr.getId())) {
            paramValue = parameters.get(expr.getId());
        }
        JavaTypeMapping m = null;
        StatementMappingIndex paramIdx = parameterDefinition.getMappingForParameter(expr.getId());
        if (paramIdx != null) {
            m = paramIdx.getMapping();
        } else {
            if (paramValue != null) {
                m = exprFactory.getMappingForType(paramValue.getClass(), false);
                if (expr.getSymbol() != null && expr.getSymbol().getValueType() != null) {
                    if (!QueryUtils.queryParameterTypesAreCompatible(expr.getSymbol().getValueType(), paramValue.getClass())) {
                        throw new QueryCompilerSyntaxException("Supplied parameter " + expr.getId() + " is declared as " + expr.getSymbol().getValueType().getName() + " yet a value of type " + paramValue.getClass().getName() + " was supplied");
                    }
                    if (expr.getSymbol().getValueType() != paramValue.getClass()) {
                        precompilable = false;
                    }
                }
            } else if (expr.getSymbol() != null && expr.getSymbol().getValueType() != null) {
                m = exprFactory.getMappingForType(expr.getSymbol().getValueType(), false);
            }
        }
        SQLExpression sqlExpr = null;
        if (asLiteral && paramValue != null) {
            sqlExpr = exprFactory.newLiteral(stmt, m, paramValue);
        } else {
            sqlExpr = exprFactory.newLiteralParameter(stmt, m, paramValue);
            if (sqlExpr instanceof ParameterLiteral) {
                ((ParameterLiteral) sqlExpr).setName(expr.getId());
            }
            if (paramIdx == null) {
                paramIdx = new StatementMappingIndex(m);
            }
            int[] paramPositions = null;
            if (m == null) {
                paramPositions = new int[] { paramNumber++ };
            } else {
                paramPositions = new int[m.getNumberOfDatastoreFields()];
                for (int i = 0; i < paramPositions.length; i++) {
                    paramPositions[i] = paramNumber++;
                }
            }
            paramIdx.addParameterOccurrence(paramPositions);
            paramPositionByExpression.put(sqlExpr, paramPositions);
            parameterDefinition.addMappingForParameter(expr.getId(), paramIdx);
        }
        stack.push(sqlExpr);
        return sqlExpr;
    }

    protected Object processInvokeExpression(InvokeExpression expr) {
        Expression invokedExpr = expr.getLeft();
        SQLExpression invokedSqlExpr = null;
        if (invokedExpr == null) {
        } else if (invokedExpr instanceof PrimaryExpression) {
            processPrimaryExpression((PrimaryExpression) invokedExpr);
            invokedSqlExpr = stack.pop();
        } else if (invokedExpr instanceof Literal) {
            processLiteral((Literal) invokedExpr);
            invokedSqlExpr = stack.pop();
        } else if (invokedExpr instanceof ParameterExpression) {
            precompilable = false;
            processParameterExpression((ParameterExpression) invokedExpr, true);
            invokedSqlExpr = stack.pop();
        } else if (invokedExpr instanceof InvokeExpression) {
            processInvokeExpression((InvokeExpression) invokedExpr);
            invokedSqlExpr = stack.pop();
        } else {
            throw new NucleusException("Dont currently support invoke expression " + invokedExpr);
        }
        String operation = expr.getOperation();
        List args = expr.getArguments();
        List sqlExprArgs = null;
        if (args != null) {
            sqlExprArgs = new ArrayList<SQLExpression>();
            Iterator<Expression> iter = args.iterator();
            while (iter.hasNext()) {
                Expression argExpr = iter.next();
                if (argExpr instanceof PrimaryExpression) {
                    processPrimaryExpression((PrimaryExpression) argExpr);
                    sqlExprArgs.add(stack.pop());
                } else if (argExpr instanceof ParameterExpression) {
                    processParameterExpression((ParameterExpression) argExpr);
                    sqlExprArgs.add(stack.pop());
                } else if (argExpr instanceof InvokeExpression) {
                    processInvokeExpression((InvokeExpression) argExpr);
                    sqlExprArgs.add(stack.pop());
                } else if (argExpr instanceof Literal) {
                    processLiteral((Literal) argExpr);
                    sqlExprArgs.add(stack.pop());
                } else if (argExpr instanceof DyadicExpression) {
                    argExpr.evaluate(this);
                    sqlExprArgs.add(stack.pop());
                } else if (argExpr instanceof VariableExpression) {
                    processVariableExpression((VariableExpression) argExpr);
                    sqlExprArgs.add(stack.pop());
                } else {
                    throw new NucleusException("Dont currently support invoke expression argument " + argExpr);
                }
            }
        }
        SQLExpression sqlExpr = null;
        if (invokedSqlExpr != null) {
            sqlExpr = invokedSqlExpr.invoke(operation, sqlExprArgs);
        } else {
            sqlExpr = exprFactory.invokeMethod(stmt, null, operation, null, sqlExprArgs);
        }
        stack.push(sqlExpr);
        return sqlExpr;
    }

    protected Object processAddExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        SQLExpression resultExpr = left.add(right);
        stack.push(resultExpr);
        return resultExpr;
    }

    protected Object processDivExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        SQLExpression resultExpr = left.div(right);
        stack.push(resultExpr);
        return resultExpr;
    }

    protected Object processMulExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        SQLExpression resultExpr = left.mul(right);
        stack.push(resultExpr);
        return resultExpr;
    }

    protected Object processSubExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        SQLExpression resultExpr = left.sub(right);
        stack.push(resultExpr);
        return resultExpr;
    }

    @Override
    protected Object processComExpression(Expression expr) {
        SQLExpression sqlExpr = stack.pop();
        SQLExpression resultExpr = sqlExpr.com();
        stack.push(resultExpr);
        return resultExpr;
    }

    @Override
    protected Object processModExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        SQLExpression resultExpr = left.mod(right);
        stack.push(resultExpr);
        return resultExpr;
    }

    @Override
    protected Object processNegExpression(Expression expr) {
        SQLExpression sqlExpr = stack.pop();
        SQLExpression resultExpr = sqlExpr.neg();
        stack.push(resultExpr);
        return resultExpr;
    }

    @Override
    protected Object processNotExpression(Expression expr) {
        SQLExpression sqlExpr = stack.pop();
        SQLExpression resultExpr = sqlExpr.not();
        stack.push(resultExpr);
        return resultExpr;
    }

    @Override
    protected Object processCastExpression(CastExpression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        SQLExpression instanceofExpr = left.cast(right);
        stack.push(instanceofExpr);
        return instanceofExpr;
    }

    @Override
    protected Object processIsExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        SQLExpression instanceofExpr = left.is(right);
        stack.push(instanceofExpr);
        return instanceofExpr;
    }

    @Override
    protected Object processCreatorExpression(CreatorExpression expr) {
        String className = expr.getId();
        Class cls = clr.classForName(className);
        List<SQLExpression> ctrArgExprs = null;
        List args = expr.getArguments();
        if (args != null) {
            ctrArgExprs = new ArrayList<SQLExpression>(args.size());
            Iterator iter = args.iterator();
            while (iter.hasNext()) {
                Expression argExpr = (Expression) iter.next();
                SQLExpression sqlExpr = (SQLExpression) evaluate(argExpr);
                ctrArgExprs.add(sqlExpr);
            }
        }
        NewObjectExpression newExpr = new NewObjectExpression(stmt, cls, ctrArgExprs);
        stack.push(newExpr);
        return newExpr;
    }

    @Override
    protected Object processLikeExpression(Expression expr) {
        SQLExpression right = stack.pop();
        SQLExpression left = stack.pop();
        List args = new ArrayList();
        args.add(right);
        SQLExpression likeExpr = exprFactory.invokeMethod(stmt, String.class.getName(), "like", left, args);
        stack.push(likeExpr);
        return likeExpr;
    }

    @Override
    protected Object processVariableExpression(VariableExpression expr) {
        Symbol varSym = expr.getSymbol();
        String varName = varSym.getQualifiedName();
        NucleusLogger.QUERY.debug(">> QueryToSQL.processVariable expr=" + expr + " var=" + varName);
        QueryCompilation subCompilation = compilation.getCompilationForSubquery(varName);
        if (hasSQLTableMappingForAlias(varName)) {
            SQLTableMapping tblMapping = getSQLTableMappingForAlias(varName);
            NucleusLogger.QUERY.debug(">> QueryToSQL.processVariable var=" + varName + " tableMapping=" + tblMapping);
            SQLExpression sqlExpr = exprFactory.newExpression(stmt, tblMapping.table, tblMapping.mapping);
            stack.push(sqlExpr);
            return sqlExpr;
        } else if (subCompilation != null) {
            throw new QueryCompilerSyntaxException("Query makes use of subquery " + varName + " but subqueries not yet supported");
        } else {
            AbstractClassMetaData cmd = om.getMetaDataManager().getMetaDataForClass(varSym.getValueType(), clr);
            if (cmd != null) {
                DatastoreClass varTable = storeMgr.getDatastoreClass(varSym.getValueType().getName(), clr);
                SQLTable varSqlTbl = stmt.crossJoin(varTable, "VAR_" + varName, null);
                SQLTableMapping varSqlTblMapping = new SQLTableMapping(varSqlTbl, cmd, varTable.getIDMapping());
                setSQLTableMappingForAlias(varName, varSqlTblMapping);
                SQLExpression sqlExpr = exprFactory.newExpression(stmt, varSqlTbl, varTable.getIDMapping());
                stack.push(sqlExpr);
                return sqlExpr;
            } else {
            }
        }
        throw new NucleusUserException("We dont currently support " + expr);
    }

    /**
     * Convenience method to return a parameter-based literal using the supplied mapping to replace
     * the provided ParameterLiteral (generated before its type was known).
     * Also updates any entry in the parameter indices to use this mapping.
     * @param paramLit The parameter literal
     * @param mapping Mapping to use
     * @return The replacement expression
     */
    protected SQLExpression replaceParameterLiteral(ParameterLiteral paramLit, JavaTypeMapping mapping) {
        String paramName = paramLit.getName();
        StatementMappingIndex paramIdxOld = parameterDefinition.getMappingForParameter(paramName);
        if (paramIdxOld.getMapping() == null) {
            StatementMappingIndex paramIdxNew = new StatementMappingIndex(mapping);
            for (int i = 0; i < paramIdxOld.getNumberOfParameterOccurrences(); i++) {
                paramIdxNew.addParameterOccurrence(paramIdxOld.getParameterPositionsForOccurrence(i));
            }
            parameterDefinition.addMappingForParameter(paramName, paramIdxNew);
        }
        SQLExpression paramExpr = exprFactory.newLiteralParameter(stmt, mapping, paramLit.getValue());
        int[] positions = paramPositionByExpression.get(paramLit);
        if (positions != null) {
            paramPositionByExpression.remove(paramLit);
            paramPositionByExpression.put(paramExpr, positions);
        }
        return paramExpr;
    }

    /**
     * Convenience method to return the value of a field of the supplied object.
     * If the object is null then returns null for the field.
     * @param obj The object
     * @param fieldName The field name
     * @return The field value
     */
    protected Object getValueForObjectField(Object obj, String fieldName) {
        if (obj != null) {
            Object paramFieldValue = null;
            if (om.getApiAdapter().isPersistable(obj)) {
                StateManager paramSM = om.findStateManager(obj);
                AbstractClassMetaData paramCmd = om.getMetaDataManager().getMetaDataForClass(obj.getClass(), clr);
                AbstractMemberMetaData paramFieldMmd = paramCmd.getMetaDataForMember(fieldName);
                if (paramSM != null) {
                    paramSM.isLoaded((PersistenceCapable) obj, paramFieldMmd.getAbsoluteFieldNumber());
                    paramFieldValue = paramSM.provideField(paramFieldMmd.getAbsoluteFieldNumber());
                } else {
                    paramFieldValue = ClassUtils.getValueOfFieldByReflection(obj, fieldName);
                }
            } else {
                paramFieldValue = ClassUtils.getValueOfFieldByReflection(obj, fieldName);
            }
            return paramFieldValue;
        } else {
            return null;
        }
    }

    protected SQLTableMapping getSQLTableMappingForAlias(String alias) {
        if (caseInsensitive) {
            return sqlTableByPrimary.get(alias.toUpperCase());
        } else {
            return sqlTableByPrimary.get(alias);
        }
    }

    protected void setSQLTableMappingForAlias(String alias, SQLTableMapping mapping) {
        if (alias == null) {
            return;
        }
        if (caseInsensitive) {
            sqlTableByPrimary.put(alias.toUpperCase(), mapping);
        } else {
            sqlTableByPrimary.put(alias, mapping);
        }
    }

    protected boolean hasSQLTableMappingForAlias(String alias) {
        if (caseInsensitive) {
            return sqlTableByPrimary.containsKey(alias.toUpperCase());
        } else {
            return sqlTableByPrimary.containsKey(alias);
        }
    }
}
