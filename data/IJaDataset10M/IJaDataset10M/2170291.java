package org.datanucleus.store.rdbms.sql.expression;

import java.util.Iterator;
import java.util.List;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.exceptions.ClassNotResolvedException;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.identity.OID;
import org.datanucleus.identity.OIDFactory;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.DiscriminatorMetaData;
import org.datanucleus.metadata.DiscriminatorStrategy;
import org.datanucleus.metadata.IdentityType;
import org.datanucleus.metadata.InheritanceStrategy;
import org.datanucleus.query.expression.Expression;
import org.datanucleus.store.mapped.DatastoreClass;
import org.datanucleus.store.mapped.mapping.BigDecimalMapping;
import org.datanucleus.store.mapped.mapping.BigIntegerMapping;
import org.datanucleus.store.mapped.mapping.BooleanMapping;
import org.datanucleus.store.mapped.mapping.ByteMapping;
import org.datanucleus.store.mapped.mapping.CharacterMapping;
import org.datanucleus.store.mapped.mapping.DateMapping;
import org.datanucleus.store.mapped.mapping.DiscriminatorMapping;
import org.datanucleus.store.mapped.mapping.DoubleMapping;
import org.datanucleus.store.mapped.mapping.EmbeddedMapping;
import org.datanucleus.store.mapped.mapping.FloatMapping;
import org.datanucleus.store.mapped.mapping.IntegerMapping;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.mapped.mapping.LongMapping;
import org.datanucleus.store.mapped.mapping.PersistableIdMapping;
import org.datanucleus.store.mapped.mapping.PersistableMapping;
import org.datanucleus.store.mapped.mapping.ReferenceMapping;
import org.datanucleus.store.mapped.mapping.ShortMapping;
import org.datanucleus.store.mapped.mapping.SqlDateMapping;
import org.datanucleus.store.mapped.mapping.SqlTimeMapping;
import org.datanucleus.store.mapped.mapping.SqlTimestampMapping;
import org.datanucleus.store.mapped.mapping.StringMapping;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.SQLStatementHelper;
import org.datanucleus.store.rdbms.sql.SQLTable;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.util.Localiser;
import org.datanucleus.util.NucleusLogger;

/**
 * Representation of an Object expression in a Query. Typically represents a persistable object,
 * and so its identity, though could be used to represent any Object.
 * <p>
 * Let's take an example. We have classes A and B, and A contains a reference to B "b".
 * If we do a JDOQL query for class A of "b == value" then "b" is interpreted first 
 * and an ObjectExpression is created to represent that object (of type B).
 * </p>
 */
public class ObjectExpression extends SQLExpression {

    /** Localiser for messages */
    protected static final Localiser LOCALISER_CORE = Localiser.getInstance("org.datanucleus.Localisation", org.datanucleus.ClassConstants.NUCLEUS_CONTEXT_LOADER);

    /**
     * Constructor for an SQL expression for a (field) mapping in a specified table.
     * @param stmt The statement
     * @param table The table in the statement
     * @param mapping The mapping for the field
     */
    public ObjectExpression(SQLStatement stmt, SQLTable table, JavaTypeMapping mapping) {
        super(stmt, table, mapping);
    }

    /**
     * Method to change the expression to use only the first column.
     * This is used where we want to use the expression with COUNT(...) and that only allows 1 column.
     */
    public void useFirstColumnOnly() {
        if (mapping.getNumberOfDatastoreMappings() <= 1) {
            return;
        }
        subExprs = new ColumnExpressionList();
        ColumnExpression colExpr = new ColumnExpression(stmt, table, (Column) mapping.getDatastoreMapping(0).getDatastoreField());
        subExprs.addExpression(colExpr);
        st.clearStatement();
        st.append(subExprs.toString());
    }

    /**
     * Equals operator. Called when the query contains "obj == value" where "obj" is this object.
     * @param expr The expression we compare with (the right-hand-side in the query)
     * @return Boolean expression representing the comparison.
     */
    public BooleanExpression eq(SQLExpression expr) {
        addSubexpressionsToRelatedExpression(expr);
        if (mapping instanceof PersistableIdMapping) {
            if (expr instanceof StringLiteral) {
                String oidString = (String) ((StringLiteral) expr).getValue();
                if (oidString != null) {
                    AbstractClassMetaData cmd = stmt.getRDBMSManager().getMetaDataManager().getMetaDataForClass(mapping.getType(), stmt.getQueryGenerator().getClassLoaderResolver());
                    if (cmd.getIdentityType() == IdentityType.DATASTORE) {
                        try {
                            OID oid = OIDFactory.getInstance(stmt.getRDBMSManager().getNucleusContext(), oidString);
                            if (oid == null) {
                            }
                        } catch (IllegalArgumentException iae) {
                            NucleusLogger.QUERY.info("Attempted comparison of " + this + " and " + expr + " where the former is a datastore-identity and the latter is of incorrect form (" + oidString + ")");
                            SQLExpressionFactory exprFactory = stmt.getSQLExpressionFactory();
                            JavaTypeMapping m = exprFactory.getMappingForType(boolean.class, true);
                            return exprFactory.newLiteral(stmt, m, false).eq(exprFactory.newLiteral(stmt, m, true));
                        }
                    } else if (cmd.getIdentityType() == IdentityType.APPLICATION) {
                    }
                }
            }
        }
        if (mapping instanceof ReferenceMapping && expr.mapping instanceof PersistableMapping) {
            return processComparisonOfImplementationWithReference(this, expr, false);
        } else if (mapping instanceof PersistableMapping && expr.mapping instanceof ReferenceMapping) {
            return processComparisonOfImplementationWithReference(expr, this, false);
        }
        BooleanExpression bExpr = null;
        if (isParameter() || expr.isParameter()) {
            if (this.subExprs.size() > 1) {
                for (int i = 0; i < subExprs.size(); i++) {
                    BooleanExpression subexpr = subExprs.getExpression(i).eq(((ObjectExpression) expr).subExprs.getExpression(i));
                    bExpr = (bExpr == null ? subexpr : bExpr.and(subexpr));
                }
                return bExpr;
            } else {
                return new BooleanExpression(this, Expression.OP_EQ, expr);
            }
        } else if (expr instanceof NullLiteral) {
            for (int i = 0; i < subExprs.size(); i++) {
                BooleanExpression subexpr = expr.eq(subExprs.getExpression(i));
                bExpr = (bExpr == null ? subexpr : bExpr.and(subexpr));
            }
            return bExpr;
        } else if (literalIsValidForSimpleComparison(expr)) {
            if (subExprs.size() > 1) {
                return super.eq(expr);
            } else {
                return new BooleanExpression(this, Expression.OP_EQ, expr);
            }
        } else if (expr instanceof ObjectExpression) {
            return ExpressionUtils.getEqualityExpressionForObjectExpressions(this, (ObjectExpression) expr, true);
        } else {
            return super.eq(expr);
        }
    }

    protected BooleanExpression processComparisonOfImplementationWithReference(SQLExpression refExpr, SQLExpression implExpr, boolean negate) {
        ReferenceMapping refMapping = (ReferenceMapping) refExpr.mapping;
        JavaTypeMapping[] implMappings = refMapping.getJavaTypeMapping();
        int subExprStart = 0;
        int subExprEnd = 0;
        for (int i = 0; i < implMappings.length; i++) {
            if (implMappings[i].getType().equals(implExpr.mapping.getType())) {
                subExprEnd = subExprStart + implMappings[i].getNumberOfDatastoreMappings();
                break;
            } else {
                subExprStart += implMappings[i].getNumberOfDatastoreMappings();
            }
        }
        BooleanExpression bExpr = null;
        int implMappingNum = 0;
        for (int i = subExprStart; i < subExprEnd; i++) {
            BooleanExpression subexpr = refExpr.subExprs.getExpression(i).eq(implExpr.subExprs.getExpression(implMappingNum++));
            bExpr = (bExpr == null ? subexpr : bExpr.and(subexpr));
        }
        if (bExpr == null) {
            return ExpressionUtils.getEqualityExpressionForObjectExpressions((ObjectExpression) refExpr, (ObjectExpression) implExpr, true);
        }
        return (negate ? new BooleanExpression(Expression.OP_NOT, bExpr.encloseInParentheses()) : bExpr);
    }

    /**
     * Not equals operator. Called when the query contains "obj != value" where "obj" is this object.
     * @param expr The expression we compare with (the right-hand-side in the query)
     * @return Boolean expression representing the comparison.
     */
    public BooleanExpression ne(SQLExpression expr) {
        addSubexpressionsToRelatedExpression(expr);
        if (mapping instanceof ReferenceMapping && expr.mapping instanceof PersistableMapping) {
            return processComparisonOfImplementationWithReference(this, expr, true);
        } else if (mapping instanceof PersistableMapping && expr.mapping instanceof ReferenceMapping) {
            return processComparisonOfImplementationWithReference(expr, this, true);
        }
        BooleanExpression bExpr = null;
        if (isParameter() || expr.isParameter()) {
            if (this.subExprs.size() > 1) {
                for (int i = 0; i < subExprs.size(); i++) {
                    BooleanExpression subexpr = subExprs.getExpression(i).eq(((ObjectExpression) expr).subExprs.getExpression(i));
                    bExpr = (bExpr == null ? subexpr : bExpr.and(subexpr));
                }
                return new BooleanExpression(Expression.OP_NOT, bExpr.encloseInParentheses());
            } else {
                return new BooleanExpression(this, Expression.OP_NOTEQ, expr);
            }
        } else if (expr instanceof NullLiteral) {
            for (int i = 0; i < subExprs.size(); i++) {
                BooleanExpression subexpr = expr.eq(subExprs.getExpression(i));
                bExpr = (bExpr == null ? subexpr : bExpr.and(subexpr));
            }
            return new BooleanExpression(Expression.OP_NOT, bExpr.encloseInParentheses());
        } else if (literalIsValidForSimpleComparison(expr)) {
            if (subExprs.size() > 1) {
                return super.ne(expr);
            } else {
                return new BooleanExpression(this, Expression.OP_NOTEQ, expr);
            }
        } else if (expr instanceof ObjectExpression) {
            return ExpressionUtils.getEqualityExpressionForObjectExpressions(this, (ObjectExpression) expr, false);
        } else {
            return super.ne(expr);
        }
    }

    /**
     * Updates the supplied expression with sub-expressions of consistent types to this expression.
     * This is called when we have some comparison expression (e.g this == expr) and where the
     * other expression has no sub-expressions currently.
     * @param expr The expression
     */
    protected void addSubexpressionsToRelatedExpression(SQLExpression expr) {
        if (expr.subExprs == null) {
            expr.subExprs = new ColumnExpressionList();
            for (int i = 0; i < subExprs.size(); i++) {
                expr.subExprs.addExpression(new ColumnExpression(stmt, expr.parameterName, expr.mapping, null, i));
            }
        }
    }

    /**
     * Convenience method to return if this object is valid for simple comparison
     * with the passed expression. Performs a type comparison of the object and the expression
     * for compatibility. The expression must be a literal of a suitable type for simple
     * comparison (e.g where this object is a String, and the literal is a StringLiteral).
     * @param expr The expression
     * @return Whether a simple comparison is valid
     */
    private boolean literalIsValidForSimpleComparison(SQLExpression expr) {
        if ((expr instanceof BooleanLiteral && (mapping instanceof BooleanMapping)) || (expr instanceof ByteLiteral && (mapping instanceof ByteMapping)) || (expr instanceof CharacterLiteral && (mapping instanceof CharacterMapping)) || (expr instanceof FloatingPointLiteral && (mapping instanceof FloatMapping || mapping instanceof DoubleMapping || mapping instanceof BigDecimalMapping)) || (expr instanceof IntegerLiteral && (mapping instanceof IntegerMapping || mapping instanceof LongMapping || mapping instanceof BigIntegerMapping) || mapping instanceof ShortMapping) || (expr instanceof TemporalLiteral && (mapping instanceof DateMapping || mapping instanceof SqlDateMapping || mapping instanceof SqlTimeMapping || mapping instanceof SqlTimestampMapping)) || (expr instanceof StringLiteral && (mapping instanceof StringMapping || mapping instanceof CharacterMapping))) {
            return true;
        }
        return false;
    }

    public BooleanExpression in(SQLExpression expr, boolean not) {
        return new BooleanExpression(this, not ? Expression.OP_NOTIN : Expression.OP_IN, expr);
    }

    /**
     * Cast operator. Called when the query contains "(type)obj" where "obj" is this object.
     * @param expr Expression representing the type to cast to
     * @return Scalar expression representing the cast object.
     */
    public SQLExpression cast(SQLExpression expr) {
        RDBMSStoreManager storeMgr = stmt.getRDBMSManager();
        ClassLoaderResolver clr = stmt.getClassLoaderResolver();
        String castClassName = (String) ((StringLiteral) expr).getValue();
        Class type = null;
        try {
            type = stmt.getQueryGenerator().resolveClass(castClassName);
        } catch (ClassNotResolvedException cnre) {
            type = null;
        }
        if (type == null) {
            throw new NucleusUserException(LOCALISER_CORE.msg("037017", castClassName));
        }
        SQLExpressionFactory exprFactory = stmt.getSQLExpressionFactory();
        Class memberType = clr.classForName(mapping.getType());
        if (!memberType.isAssignableFrom(type) && !type.isAssignableFrom(memberType)) {
            JavaTypeMapping m = exprFactory.getMappingForType(boolean.class, true);
            return exprFactory.newLiteral(stmt, m, false).eq(exprFactory.newLiteral(stmt, m, true));
        } else if (memberType == type) {
            return this;
        }
        if (mapping instanceof EmbeddedMapping) {
            JavaTypeMapping m = exprFactory.getMappingForType(boolean.class, true);
            return exprFactory.newLiteral(stmt, m, false).eq(exprFactory.newLiteral(stmt, m, true));
        } else if (mapping instanceof ReferenceMapping) {
            ReferenceMapping refMapping = (ReferenceMapping) mapping;
            if (refMapping.getMappingStrategy() != ReferenceMapping.PER_IMPLEMENTATION_MAPPING) {
                throw new NucleusUserException("Impossible to do cast of interface to " + type.getName() + " since interface is persisted as embedded String." + " Use per-implementation mapping to allow this query");
            }
            JavaTypeMapping[] implMappings = refMapping.getJavaTypeMapping();
            for (int i = 0; i < implMappings.length; i++) {
                Class implType = clr.classForName(implMappings[i].getType());
                if (type.isAssignableFrom(implType)) {
                    DatastoreClass castTable = storeMgr.getDatastoreClass(type.getName(), clr);
                    SQLTable castSqlTbl = stmt.leftOuterJoin(table, implMappings[i], refMapping, castTable, null, castTable.getIdMapping(), null, null, null);
                    return exprFactory.newExpression(stmt, castSqlTbl, castTable.getIdMapping());
                }
            }
            NucleusLogger.QUERY.warn("Unable to process cast of interface field to " + type.getName() + " since it has no implementations that match that type");
            JavaTypeMapping m = exprFactory.getMappingForType(boolean.class, true);
            return exprFactory.newLiteral(stmt, m, false).eq(exprFactory.newLiteral(stmt, m, true));
        } else if (mapping instanceof PersistableMapping) {
            DatastoreClass castTable = storeMgr.getDatastoreClass(type.getName(), clr);
            SQLTable castSqlTbl = stmt.getTable(castTable, table.getGroupName());
            if (castSqlTbl == null) {
                castSqlTbl = stmt.leftOuterJoin(table, table.getTable().getIdMapping(), castTable, null, castTable.getIdMapping(), null, table.getGroupName());
            }
            return exprFactory.newExpression(stmt, castSqlTbl, castTable.getIdMapping());
        } else {
        }
        throw new NucleusUserException("Dont currently support ObjectExpression.cast(" + type + ")");
    }

    /**
     * An "is" (instanceOf) expression, providing a BooleanExpression whether this expression
     * is an instanceof the provided type.
     * @param expr The expression representing the type
     * @param not Whether the operator is "!instanceof"
     * @return Whether this expression is an instance of the provided type
     */
    public BooleanExpression is(SQLExpression expr, boolean not) {
        RDBMSStoreManager storeMgr = stmt.getRDBMSManager();
        ClassLoaderResolver clr = stmt.getClassLoaderResolver();
        String instanceofClassName = (String) ((StringLiteral) expr).getValue();
        Class type = null;
        try {
            type = stmt.getQueryGenerator().resolveClass(instanceofClassName);
        } catch (ClassNotResolvedException cnre) {
            type = null;
        }
        if (type == null) {
            throw new NucleusUserException(LOCALISER_CORE.msg("037016", instanceofClassName));
        }
        SQLExpressionFactory exprFactory = stmt.getSQLExpressionFactory();
        Class memberType = clr.classForName(mapping.getType());
        if (!memberType.isAssignableFrom(type) && !type.isAssignableFrom(memberType)) {
            JavaTypeMapping m = exprFactory.getMappingForType(boolean.class, true);
            return exprFactory.newLiteral(stmt, m, true).eq(exprFactory.newLiteral(stmt, m, not));
        } else if (memberType == type) {
            JavaTypeMapping m = exprFactory.getMappingForType(boolean.class, true);
            return exprFactory.newLiteral(stmt, m, true).eq(exprFactory.newLiteral(stmt, m, !not));
        }
        if (mapping instanceof EmbeddedMapping) {
            AbstractClassMetaData fieldCmd = storeMgr.getMetaDataManager().getMetaDataForClass(mapping.getType(), clr);
            if (fieldCmd.hasDiscriminatorStrategy()) {
                JavaTypeMapping discMapping = ((EmbeddedMapping) mapping).getDiscriminatorMapping();
                DiscriminatorMetaData dismd = fieldCmd.getDiscriminatorMetaDataRoot();
                AbstractClassMetaData typeCmd = storeMgr.getMetaDataManager().getMetaDataForClass(type, clr);
                SQLExpression discExpr = stmt.getSQLExpressionFactory().newExpression(stmt, table, discMapping);
                SQLExpression discVal = null;
                if (dismd.getStrategy() == DiscriminatorStrategy.CLASS_NAME) {
                    discVal = stmt.getSQLExpressionFactory().newLiteral(stmt, discMapping, typeCmd.getFullClassName());
                } else {
                    discVal = stmt.getSQLExpressionFactory().newLiteral(stmt, discMapping, typeCmd.getDiscriminatorMetaData().getValue());
                }
                BooleanExpression typeExpr = (not ? discExpr.ne(discVal) : discExpr.eq(discVal));
                Iterator<String> subclassIter = storeMgr.getSubClassesForClass(type.getName(), true, clr).iterator();
                while (subclassIter.hasNext()) {
                    String subclassName = subclassIter.next();
                    AbstractClassMetaData subtypeCmd = storeMgr.getMetaDataManager().getMetaDataForClass(subclassName, clr);
                    if (dismd.getStrategy() == DiscriminatorStrategy.CLASS_NAME) {
                        discVal = stmt.getSQLExpressionFactory().newLiteral(stmt, discMapping, subtypeCmd.getFullClassName());
                    } else {
                        discVal = stmt.getSQLExpressionFactory().newLiteral(stmt, discMapping, subtypeCmd.getDiscriminatorMetaData().getValue());
                    }
                    BooleanExpression subtypeExpr = (not ? discExpr.ne(discVal) : discExpr.eq(discVal));
                    if (not) {
                        typeExpr = typeExpr.and(subtypeExpr);
                    } else {
                        typeExpr = typeExpr.ior(subtypeExpr);
                    }
                }
                return typeExpr;
            } else {
                JavaTypeMapping m = exprFactory.getMappingForType(boolean.class, true);
                return exprFactory.newLiteral(stmt, m, true).eq(exprFactory.newLiteral(stmt, m, not));
            }
        } else if (mapping instanceof PersistableMapping || mapping instanceof ReferenceMapping) {
            AbstractClassMetaData fieldCmd = storeMgr.getMetaDataManager().getMetaDataForClass(mapping.getType(), clr);
            DatastoreClass memberTable = null;
            if (fieldCmd.getInheritanceMetaData().getStrategy() == InheritanceStrategy.SUBCLASS_TABLE) {
                AbstractClassMetaData[] cmds = storeMgr.getClassesManagingTableForClass(fieldCmd, clr);
                if (cmds != null) {
                    if (cmds.length > 1) {
                        NucleusLogger.QUERY.warn(LOCALISER_CORE.msg("037006", mapping.getMemberMetaData().getFullFieldName(), cmds[0].getFullClassName()));
                    }
                    memberTable = storeMgr.getDatastoreClass(cmds[0].getFullClassName(), clr);
                } else {
                    throw new NucleusUserException(LOCALISER_CORE.msg("037005", mapping.getMemberMetaData().getFullFieldName()));
                }
            } else {
                memberTable = storeMgr.getDatastoreClass(mapping.getType(), clr);
            }
            DiscriminatorMetaData dismd = memberTable.getDiscriminatorMetaData();
            DiscriminatorMapping discMapping = (DiscriminatorMapping) memberTable.getDiscriminatorMapping(false);
            if (discMapping != null) {
                SQLTable targetSqlTbl = null;
                if (mapping.getDatastoreContainer() != memberTable) {
                    targetSqlTbl = stmt.getTable(memberTable, null);
                    if (targetSqlTbl == null) {
                        targetSqlTbl = stmt.innerJoin(getSQLTable(), mapping, memberTable, null, memberTable.getIdMapping(), null, null);
                    }
                } else {
                    targetSqlTbl = SQLStatementHelper.getSQLTableForMappingOfTable(stmt, getSQLTable(), discMapping);
                }
                SQLTable discSqlTbl = targetSqlTbl;
                BooleanExpression discExpr = SQLStatementHelper.getExpressionForDiscriminatorForClass(stmt, type.getName(), dismd, discMapping, discSqlTbl, clr);
                Iterator subclassIter = storeMgr.getSubClassesForClass(type.getName(), true, clr).iterator();
                boolean hasSubclass = false;
                while (subclassIter.hasNext()) {
                    String subclassName = (String) subclassIter.next();
                    BooleanExpression discExprSub = SQLStatementHelper.getExpressionForDiscriminatorForClass(stmt, subclassName, dismd, discMapping, discSqlTbl, clr);
                    discExpr = discExpr.ior(discExprSub);
                    hasSubclass = true;
                }
                if (hasSubclass) {
                    discExpr.encloseInParentheses();
                }
                return (not ? discExpr.not() : discExpr);
            } else {
                DatastoreClass table = null;
                if (fieldCmd.getInheritanceMetaData().getStrategy() == InheritanceStrategy.SUBCLASS_TABLE) {
                    AbstractClassMetaData[] cmds = storeMgr.getClassesManagingTableForClass(fieldCmd, clr);
                    if (cmds != null) {
                        if (cmds.length > 1) {
                            NucleusLogger.QUERY.warn(LOCALISER_CORE.msg("037006", mapping.getMemberMetaData().getFullFieldName(), cmds[0].getFullClassName()));
                        }
                        table = storeMgr.getDatastoreClass(cmds[0].getFullClassName(), clr);
                    } else {
                        throw new NucleusUserException(LOCALISER_CORE.msg("037005", mapping.getMemberMetaData().getFullFieldName()));
                    }
                } else {
                    table = storeMgr.getDatastoreClass(mapping.getType(), clr);
                }
                if (table.managesClass(type.getName())) {
                    JavaTypeMapping m = exprFactory.getMappingForType(boolean.class, true);
                    return exprFactory.newLiteral(stmt, m, true).eq(exprFactory.newLiteral(stmt, m, !not));
                } else {
                    if (table == stmt.getPrimaryTable().getTable()) {
                        JavaTypeMapping m = exprFactory.getMappingForType(boolean.class, true);
                        if (stmt.getNumberOfUnions() > 0) {
                            Class mainCandidateCls = clr.classForName(stmt.getCandidateClassName());
                            if (type.isAssignableFrom(mainCandidateCls) == not) {
                                SQLExpression unionClauseExpr = exprFactory.newLiteral(stmt, m, true).eq(exprFactory.newLiteral(stmt, m, false));
                                stmt.whereAnd((BooleanExpression) unionClauseExpr, false);
                            }
                            List<SQLStatement> unionStmts = stmt.getUnions();
                            Iterator<SQLStatement> iter = unionStmts.iterator();
                            while (iter.hasNext()) {
                                SQLStatement unionStmt = iter.next();
                                Class unionCandidateCls = clr.classForName(unionStmt.getCandidateClassName());
                                if (type.isAssignableFrom(unionCandidateCls) == not) {
                                    SQLExpression unionClauseExpr = exprFactory.newLiteral(unionStmt, m, true).eq(exprFactory.newLiteral(unionStmt, m, false));
                                    unionStmt.whereAnd((BooleanExpression) unionClauseExpr, false);
                                }
                            }
                            SQLExpression returnExpr = exprFactory.newLiteral(stmt, m, true).eq(exprFactory.newLiteral(stmt, m, true));
                            return (BooleanExpression) returnExpr;
                        } else {
                            Class mainCandidateCls = clr.classForName(stmt.getCandidateClassName());
                            if (!type.isAssignableFrom(mainCandidateCls)) {
                                return exprFactory.newLiteral(stmt, m, true).eq(exprFactory.newLiteral(stmt, m, not));
                            } else {
                                return exprFactory.newLiteral(stmt, m, true).eq(exprFactory.newLiteral(stmt, m, !not));
                            }
                        }
                    } else {
                        DatastoreClass instanceofTable = storeMgr.getDatastoreClass(type.getName(), clr);
                        if (stmt.getNumberOfUnions() > 0) {
                            NucleusLogger.QUERY.debug("InstanceOf for " + table + " but no discriminator so adding inner join to " + instanceofTable + " : in some cases with UNIONs this may fail");
                        }
                        stmt.innerJoin(this.table, this.table.getTable().getIdMapping(), instanceofTable, null, instanceofTable.getIdMapping(), null, this.table.getGroupName());
                        JavaTypeMapping m = exprFactory.getMappingForType(boolean.class, true);
                        return exprFactory.newLiteral(stmt, m, true).eq(exprFactory.newLiteral(stmt, m, !not));
                    }
                }
            }
        } else {
            throw new NucleusException("Dont currently support " + this + " instanceof " + type.getName());
        }
    }

    public SQLExpression invoke(String methodName, List args) {
        return stmt.getRDBMSManager().getSQLExpressionFactory().invokeMethod(stmt, Object.class.getName(), methodName, this, args);
    }
}
