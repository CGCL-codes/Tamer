package net.ontopia.persistence.query.sql;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.ontopia.persistence.proxy.ClassInfoIF;
import net.ontopia.persistence.proxy.FieldInfoIF;
import net.ontopia.persistence.proxy.FieldUtils;
import net.ontopia.persistence.proxy.ObjectAccessIF;
import net.ontopia.persistence.proxy.ObjectRelationalMappingIF;
import net.ontopia.persistence.query.jdo.JDOAggregateIF;
import net.ontopia.persistence.query.jdo.JDOAnd;
import net.ontopia.persistence.query.jdo.JDOBoolean;
import net.ontopia.persistence.query.jdo.JDOCollection;
import net.ontopia.persistence.query.jdo.JDOContains;
import net.ontopia.persistence.query.jdo.JDOEndsWith;
import net.ontopia.persistence.query.jdo.JDOEquals;
import net.ontopia.persistence.query.jdo.JDOExpressionIF;
import net.ontopia.persistence.query.jdo.JDOField;
import net.ontopia.persistence.query.jdo.JDOFunction;
import net.ontopia.persistence.query.jdo.JDOIsEmpty;
import net.ontopia.persistence.query.jdo.JDOLike;
import net.ontopia.persistence.query.jdo.JDONativeValue;
import net.ontopia.persistence.query.jdo.JDONot;
import net.ontopia.persistence.query.jdo.JDONotEquals;
import net.ontopia.persistence.query.jdo.JDOObject;
import net.ontopia.persistence.query.jdo.JDOOr;
import net.ontopia.persistence.query.jdo.JDOOrderBy;
import net.ontopia.persistence.query.jdo.JDOParameter;
import net.ontopia.persistence.query.jdo.JDOPrimitive;
import net.ontopia.persistence.query.jdo.JDOQuery;
import net.ontopia.persistence.query.jdo.JDOSetOperation;
import net.ontopia.persistence.query.jdo.JDOStartsWith;
import net.ontopia.persistence.query.jdo.JDOString;
import net.ontopia.persistence.query.jdo.JDOValueExpression;
import net.ontopia.persistence.query.jdo.JDOValueIF;
import net.ontopia.persistence.query.jdo.JDOVariable;
import net.ontopia.utils.OntopiaRuntimeException;

/**
 * INTERNAL: Class used to build SQL queries from JDO queries. 
 */
public class SQLBuilder {

    protected boolean debug;

    protected ObjectRelationalMappingIF mapping;

    public SQLBuilder(ObjectRelationalMappingIF mapping) {
        this(mapping, false);
    }

    public SQLBuilder(ObjectRelationalMappingIF mapping, boolean debug) {
        this.mapping = mapping;
        this.debug = debug;
    }

    /**
   * INTERNAL: Class used to hold information collected after having
   * analyzed the SQL filter.
   */
    class BuildInfo {

        protected ObjectAccessIF oaccess;

        protected JDOQuery jdoquery;

        protected SQLQuery sqlquery;

        protected Map tables = new HashMap();

        protected Map valiases = new HashMap();

        protected int tblcount;

        protected Map nfvals = new HashMap();

        protected Map ntvals = new HashMap();

        SQLTable createNamedValueTable(JDOValueIF value, List expressions) {
            String valname;
            String prefix;
            switch(value.getType()) {
                case JDOValueIF.VARIABLE:
                    {
                        valname = ((JDOVariable) value).getName();
                        prefix = "V";
                        break;
                    }
                case JDOValueIF.PARAMETER:
                    {
                        valname = ((JDOParameter) value).getName();
                        prefix = "P";
                        break;
                    }
                default:
                    throw new OntopiaRuntimeException("Non-supported named value: '" + value + "'");
            }
            String alias = (debug ? valname : (String) valiases.get(value));
            if (alias == null) {
                while (true) {
                    alias = prefix + (tblcount++);
                    if (tables.containsKey(alias)) continue;
                    valiases.put(value, alias);
                    break;
                }
            }
            if (tables.containsKey(alias)) {
                return (SQLTable) tables.get(alias);
            } else {
                if (nfvals.containsKey(value)) {
                    FieldInfoIF finfo = (FieldInfoIF) nfvals.get(value);
                    String tblname = finfo.getTable();
                    if (tblname == null) tblname = (String) ntvals.get(value);
                    if (tblname == null) throw new OntopiaRuntimeException("Not able to figure out table for value: '" + value + "'");
                    SQLTable table = new SQLTable(tblname, alias);
                    tables.put(table.getAlias(), table);
                    return table;
                } else {
                    Class valtype = getValueType(value, this);
                    ClassInfoIF cinfo = mapping.getClassInfo(valtype);
                    SQLTable table = new SQLTable(cinfo.getMasterTable(), alias);
                    tables.put(table.getAlias(), table);
                    if (value.getType() == JDOValueIF.PARAMETER) {
                        FieldInfoIF finfo = cinfo.getIdentityFieldInfo();
                        SQLParameter sqlparam = new SQLParameter(valname, finfo.getColumnCount());
                        sqlparam.setValueType(valtype);
                        sqlparam.setFieldHandler(finfo);
                        expressions.add(new SQLEquals(new SQLColumns(table, finfo.getValueColumns()), sqlparam));
                    }
                    return table;
                }
            }
        }

        String createTableAlias(String prefix) {
            if (!debug) return prefix + (tblcount++);
            String alias = prefix + (tblcount++);
            if (jdoquery.hasVariableName(alias) || tables.containsKey(alias)) return createTableAlias(prefix); else return alias;
        }

        void analyze() {
            if (jdoquery == null) throw new OntopiaRuntimeException("JDO query not registered with SQLbuilder build info.");
            JDOExpressionIF filter = jdoquery.getFilter();
            if (filter != null) analyzeExpression(filter);
        }

        protected void analyzeExpression(JDOExpressionIF jdoexpr) {
            switch(jdoexpr.getType()) {
                case JDOExpressionIF.EQUALS:
                    JDOEquals eq = (JDOEquals) jdoexpr;
                    analyzeCompatible(eq.getLeft(), eq.getRight());
                    break;
                case JDOExpressionIF.CONTAINS:
                    JDOContains contains = (JDOContains) jdoexpr;
                    analyzeCompatible(contains.getLeft(), contains.getRight());
                    break;
                case JDOExpressionIF.NOT_EQUALS:
                    JDONotEquals neq = (JDONotEquals) jdoexpr;
                    analyzeCompatible(neq.getLeft(), neq.getRight());
                    break;
                case JDOExpressionIF.IS_EMPTY:
                    break;
                case JDOExpressionIF.STARTS_WITH:
                    JDOStartsWith swith = (JDOStartsWith) jdoexpr;
                    analyzeCompatible(swith.getLeft(), swith.getRight());
                    break;
                case JDOExpressionIF.ENDS_WITH:
                    JDOEndsWith ewith = (JDOEndsWith) jdoexpr;
                    analyzeCompatible(ewith.getLeft(), ewith.getRight());
                    break;
                case JDOExpressionIF.LIKE:
                    JDOLike like = (JDOLike) jdoexpr;
                    analyzeCompatible(like.getLeft(), like.getRight());
                    break;
                case JDOExpressionIF.AND:
                    analyzeExpression(((JDOAnd) jdoexpr).getExpressions());
                    break;
                case JDOExpressionIF.OR:
                    analyzeExpression(((JDOOr) jdoexpr).getExpressions());
                    break;
                case JDOExpressionIF.NOT:
                    analyzeExpression(((JDONot) jdoexpr).getExpression());
                    break;
                case JDOExpressionIF.BOOLEAN:
                    break;
                case JDOExpressionIF.VALUE_EXPRESSION:
                    {
                        JDOValueIF value = ((JDOValueExpression) jdoexpr).getValue();
                        if (value.getType() == JDOValueIF.FUNCTION) {
                            JDOFunction func = (JDOFunction) value;
                            String fname = func.getName();
                            if (fname.equals(">") || fname.equals(">=") || fname.equals("<") || fname.equals("<=") || fname.equals("substring")) {
                                JDOValueIF[] args = func.getArguments();
                                analyzeCompatible(args[0], args[1]);
                            }
                        }
                        break;
                    }
                case JDOExpressionIF.SET_OPERATION:
                    break;
                default:
                    throw new OntopiaRuntimeException("Invalid expression: '" + jdoexpr + "'");
            }
        }

        protected void analyzeExpression(JDOExpressionIF[] exprs) {
            for (int i = 0; i < exprs.length; i++) {
                analyzeExpression(exprs[i]);
            }
        }

        protected JDOValueIF extractValue(JDOValueIF jdovalue) {
            switch(jdovalue.getType()) {
                case JDOValueIF.FIELD:
                case JDOValueIF.VARIABLE:
                case JDOValueIF.PARAMETER:
                case JDOValueIF.PRIMITIVE:
                case JDOValueIF.OBJECT:
                case JDOValueIF.STRING:
                case JDOValueIF.COLLECTION:
                case JDOValueIF.NULL:
                    return (JDOValueIF) jdovalue;
                case JDOValueIF.FUNCTION:
                    {
                        return ((JDOFunction) jdovalue).getArguments()[0];
                    }
                case JDOValueIF.NATIVE_VALUE:
                    return ((JDONativeValue) jdovalue).getRoot();
                default:
                    throw new OntopiaRuntimeException("Cannot extract root from unknown JDOValueIF: " + jdovalue);
            }
        }

        protected JDOValueIF extractRootValue(JDOValueIF jdovalue) {
            switch(jdovalue.getType()) {
                case JDOValueIF.FIELD:
                    return extractRootValue(((JDOField) jdovalue).getRoot());
                case JDOValueIF.VARIABLE:
                case JDOValueIF.PARAMETER:
                case JDOValueIF.PRIMITIVE:
                case JDOValueIF.OBJECT:
                case JDOValueIF.STRING:
                case JDOValueIF.COLLECTION:
                case JDOValueIF.NULL:
                    return (JDOValueIF) jdovalue;
                case JDOValueIF.FUNCTION:
                    {
                        return extractRootValue(((JDOFunction) jdovalue).getArguments()[0]);
                    }
                case JDOValueIF.NATIVE_VALUE:
                    return extractRootValue(((JDONativeValue) jdovalue).getRoot());
                default:
                    throw new OntopiaRuntimeException("Cannot extract root from unknown JDOValueIF: " + jdovalue);
            }
        }

        protected void analyzeCompatible(JDOValueIF value1, JDOValueIF value2) {
            value1 = extractValue(value1);
            value2 = extractValue(value2);
            JDOValueIF rvalue1 = extractRootValue(value1);
            JDOValueIF rvalue2 = extractRootValue(value2);
            if (rvalue1 == null || rvalue2 == null || rvalue1 == rvalue2) return;
            boolean identifiable1 = isIdentifiableValueType(rvalue1, this);
            boolean identifiable2 = isIdentifiableValueType(rvalue2, this);
            int rvtype1 = rvalue1.getType();
            int rvtype2 = rvalue2.getType();
            if (identifiable1 && !identifiable2 && (rvtype2 == JDOValueIF.VARIABLE || rvtype2 == JDOValueIF.PARAMETER)) analyzeMatchingNonIdentifiableValue(rvalue2, value1); else if (identifiable2 && !identifiable1 && (rvtype1 == JDOValueIF.VARIABLE || rvtype1 == JDOValueIF.PARAMETER)) analyzeMatchingNonIdentifiableValue(rvalue1, value2); else if (!identifiable1 && !identifiable2) {
                FieldInfoIF finfo1 = getFieldInfo(value1, this);
                if (finfo1 == null) finfo1 = (FieldInfoIF) nfvals.get(value1);
                String table1 = (finfo1 != null && finfo1.getTable() != null ? finfo1.getTable() : null);
                FieldInfoIF finfo2 = getFieldInfo(value2, this);
                if (finfo2 == null) finfo2 = (FieldInfoIF) nfvals.get(value2);
                String table2 = (finfo2 != null && finfo2.getTable() != null ? finfo2.getTable() : null);
                if (!nfvals.containsKey(value1)) {
                    if (finfo1 != null) {
                        nfvals.put(value1, finfo1);
                    } else if (finfo2 != null) {
                        nfvals.put(value1, finfo2);
                    }
                    if (table1 != null) {
                        ntvals.put(value1, table1);
                    } else if (table2 != null) {
                        ntvals.put(value1, table2);
                    } else {
                        if (nfvals.containsKey(rvalue1)) {
                            FieldInfoIF finfo = (FieldInfoIF) nfvals.get(rvalue1);
                            if (finfo.getTable() != null) ntvals.put(value1, finfo.getTable());
                        }
                        if (!ntvals.containsKey(value1) && nfvals.containsKey(rvalue2)) {
                            FieldInfoIF finfo = (FieldInfoIF) nfvals.get(rvalue2);
                            if (finfo.getTable() != null) ntvals.put(value1, finfo.getTable());
                        }
                    }
                }
                if (!nfvals.containsKey(value2)) {
                    if (finfo2 != null) {
                        nfvals.put(value2, finfo2);
                    } else if (finfo1 != null) {
                        nfvals.put(value2, finfo1);
                    }
                    if (table2 != null) {
                        ntvals.put(value2, table2);
                    } else if (table1 != null) {
                        ntvals.put(value2, table1);
                    } else {
                        if (nfvals.containsKey(rvalue2)) {
                            FieldInfoIF finfo = (FieldInfoIF) nfvals.get(rvalue2);
                            if (finfo.getTable() != null) ntvals.put(value2, finfo.getTable());
                        }
                        if (!ntvals.containsKey(value2) && nfvals.containsKey(rvalue1)) {
                            FieldInfoIF finfo = (FieldInfoIF) nfvals.get(rvalue1);
                            if (finfo.getTable() != null) ntvals.put(value2, finfo.getTable());
                        }
                    }
                }
            }
        }

        protected void analyzeMatchingNonIdentifiableValue(JDOValueIF value1, JDOValueIF value2) {
            switch(value1.getType()) {
                case JDOValueIF.VARIABLE:
                case JDOValueIF.PARAMETER:
                    break;
                default:
                    throw new OntopiaRuntimeException("Non-supported named value: '" + value1 + "'");
            }
            if (nfvals.containsKey(value1)) return;
            FieldInfoIF finfo = getFieldInfo(value2, this);
            nfvals.put(value1, finfo);
            if (!finfo.isIdentityField()) {
                if (finfo.getTable() == null) {
                    Class ctype = getIdentifiableValueType(value2, this);
                    ntvals.put(value1, mapping.getClassInfo(ctype).getMasterTable());
                } else {
                    ntvals.put(value1, finfo.getTable());
                }
            }
        }
    }

    public SQLQuery makeQuery(JDOQuery jdoquery, ObjectAccessIF oaccess) {
        SQLQuery sqlquery = new SQLQuery();
        sqlquery.setDistinct(jdoquery.getDistinct());
        sqlquery.setOffset(jdoquery.getOffset());
        sqlquery.setLimit(jdoquery.getLimit());
        BuildInfo info = new BuildInfo();
        info.oaccess = oaccess;
        info.jdoquery = jdoquery;
        info.sqlquery = sqlquery;
        info.analyze();
        if (!jdoquery.isSetQuery()) {
            List expressions = new ArrayList();
            JDOExpressionIF filter = jdoquery.getFilter();
            if (filter != null) produceExpression(filter, expressions, info);
            List select = jdoquery.getSelect();
            int select_length = select.size();
            for (int i = 0; i < select_length; i++) {
                Object value = select.get(i);
                String alias = String.valueOf((char) (97 + i));
                if (value instanceof JDOValueIF) {
                    SQLValueIF sval = produceSelectSQLValueIF((JDOValueIF) value, expressions, info);
                    sval.setAlias(alias);
                    sqlquery.addSelect(sval);
                } else {
                    SQLAggregateIF sagg = produceSelectSQLAggregateIF((JDOAggregateIF) value, expressions, info);
                    sagg.setAlias(alias);
                    sqlquery.addSelect(sagg);
                }
            }
            List orderby = jdoquery.getOrderBy();
            int orderby_length = orderby.size();
            for (int i = 0; i < orderby_length; i++) {
                sqlquery.addOrderBy(produceSQLOrderBy((JDOOrderBy) orderby.get(i), expressions, info));
            }
            if (!expressions.isEmpty()) sqlquery.setFilter(makeAndExpression(expressions));
            return sqlquery;
        } else {
            JDOSetOperation jdoset = (JDOSetOperation) jdoquery.getFilter();
            SQLSetOperation sqlset = produceSetOperation(jdoset, info);
            sqlquery.setFilter(sqlset);
            SQLQuery _first = getFirstSQLQuery(sqlset);
            Map valuemap = new HashMap();
            List jdoselect = jdoquery.getSelect();
            List sqlselect = _first.getSelect();
            for (int i = 0; i < jdoselect.size(); i++) {
                Object jdoval = jdoselect.get(i);
                SQLValueIF sqlval = (SQLValueIF) sqlselect.get(i);
                String alias = String.valueOf((char) (97 + i));
                if (jdoval instanceof JDOValueIF) {
                    sqlval.setAlias(alias);
                    SQLValueIF pval = new SQLValueReference(sqlval);
                    sqlquery.addSelect(pval);
                    valuemap.put(jdoval, pval);
                } else {
                    sqlval.setAlias(alias);
                    int aggtype = ((JDOAggregateIF) jdoval).getType();
                    SQLAggregateIF pval = new SQLAggregateReference(wrapAggregate(aggtype, sqlval));
                    sqlquery.addSelect(pval);
                    valuemap.put(jdoval, pval);
                }
            }
            List jdoorderby = jdoquery.getOrderBy();
            for (int i = 0; i < jdoorderby.size(); i++) {
                JDOOrderBy jdoob = (JDOOrderBy) jdoorderby.get(i);
                int sqlorder = getSQLOrder(jdoob);
                if (jdoob.isAggregate()) {
                    JDOAggregateIF jdoagg = jdoob.getAggregate();
                    SQLAggregateIF sqlagg = (SQLAggregateIF) valuemap.get(jdoagg);
                    if (sqlagg == null) throw new OntopiaRuntimeException("SQL aggregate for JDO aggregate not found: " + jdoagg);
                    sqlquery.addOrderBy(new SQLOrderBy(sqlagg, sqlorder));
                } else {
                    JDOValueIF jdoval = jdoob.getValue();
                    SQLValueIF sqlval = (SQLValueIF) valuemap.get(jdoval);
                    if (sqlval == null) throw new OntopiaRuntimeException("SQL value for JDO value not found: " + jdoval);
                    sqlquery.addOrderBy(new SQLOrderBy(sqlval, sqlorder));
                }
            }
            return sqlquery;
        }
    }

    protected SQLQuery getFirstSQLQuery(SQLSetOperation sqlset) {
        Object first = sqlset.getSets().get(0);
        if (first instanceof SQLQuery) return (SQLQuery) first; else return getFirstSQLQuery((SQLSetOperation) first);
    }

    protected SQLOrderBy produceSQLOrderBy(JDOOrderBy orderby, List expressions, BuildInfo info) {
        int order = getSQLOrder(orderby);
        if (orderby.isAggregate()) return new SQLOrderBy(produceSelectSQLAggregateIF(orderby.getAggregate(), expressions, info), order); else return new SQLOrderBy(produceSelectSQLValueIF(orderby.getValue(), expressions, info), order);
    }

    protected int getSQLOrder(JDOOrderBy orderby) {
        if (orderby.getOrder() == JDOOrderBy.ASCENDING) return SQLOrderBy.ASCENDING; else return SQLOrderBy.DESCENDING;
    }

    protected SQLValueIF produceSelectSQLValueIF(JDOValueIF value, List expressions, BuildInfo info) {
        switch(value.getType()) {
            case JDOValueIF.PARAMETER:
            case JDOValueIF.VARIABLE:
                {
                    Class valtype = getValueType(value, info);
                    FieldInfoIF finfo;
                    if (isAggregateType(valtype) || isPrimitiveType(valtype)) finfo = (FieldInfoIF) info.nfvals.get(value); else finfo = getFieldInfo(value, info);
                    SQLTable table = info.createNamedValueTable(value, expressions);
                    SQLColumns columns = new SQLColumns(table, finfo.getValueColumns());
                    columns.setValueType(valtype);
                    columns.setFieldHandler(finfo);
                    return columns;
                }
            case JDOValueIF.FIELD:
                {
                    JDOField field = (JDOField) value;
                    Values values = produceFieldValues(field, null, expressions, info);
                    SQLColumns columns = (SQLColumns) values.vcols;
                    columns.setValueType(getValueType(field, info));
                    columns.setFieldHandler(getFieldInfo(field, info));
                    return columns;
                }
            case JDOValueIF.NULL:
                {
                    return new SQLNull();
                }
            default:
                throw new OntopiaRuntimeException("Non-supported select value: '" + value + "'");
        }
    }

    protected String[] getKeyColumns(FieldInfoIF finfo) {
        return finfo.getValueClassInfo().getIdentityFieldInfo().getValueColumns();
    }

    protected String[] getInlineColumns(FieldInfoIF finfo) {
        ClassInfoIF cinfo = finfo.getValueClassInfo();
        List vcols = new ArrayList();
        FieldUtils.addColumns(cinfo.getIdentityFieldInfo(), vcols);
        FieldUtils.addColumns(cinfo.getOne2OneFieldInfos(), vcols);
        return FieldUtils.toStringArray(vcols);
    }

    protected SQLAggregateIF produceSelectSQLAggregateIF(JDOAggregateIF aggregate, List expressions, BuildInfo info) {
        SQLValueIF sqlvalue = produceSelectSQLValueIF(aggregate.getValue(), expressions, info);
        return wrapAggregate(aggregate.getType(), sqlvalue);
    }

    protected SQLAggregateIF wrapAggregate(int aggtype, SQLValueIF sqlvalue) {
        switch(aggtype) {
            case JDOAggregateIF.COUNT:
                return new SQLAggregate(sqlvalue, SQLAggregateIF.COUNT);
            default:
                throw new OntopiaRuntimeException("Invalid aggregate function type: '" + aggtype + "'");
        }
    }

    protected SQLExpressionIF makeAndExpression(List expressions) {
        if (expressions.size() > 1) {
            SQLExpressionIF[] exprlist = new SQLExpressionIF[expressions.size()];
            expressions.toArray(exprlist);
            return new SQLAnd(exprlist);
        } else if (expressions.size() == 1) return (SQLExpressionIF) expressions.get(0); else throw new OntopiaRuntimeException("No expressions were found.");
    }

    protected SQLExpressionIF makeOrExpression(SQLExpressionIF[] expressions) {
        if (expressions.length > 1) {
            return new SQLOr(expressions);
        } else if (expressions.length == 1) return (SQLExpressionIF) expressions[0]; else throw new OntopiaRuntimeException("No expressions were found.");
    }

    protected void produceExpression(JDOExpressionIF jdoexpr, List expressions, BuildInfo info) {
        switch(jdoexpr.getType()) {
            case JDOExpressionIF.EQUALS:
                {
                    JDOEquals expr = (JDOEquals) jdoexpr;
                    checkCompatibility(expr.getLeft(), expr.getRight(), info);
                    produceEquals(expr.getLeft(), expr.getRight(), expressions, info);
                    return;
                }
            case JDOExpressionIF.NOT_EQUALS:
                {
                    JDONotEquals expr = (JDONotEquals) jdoexpr;
                    checkCompatibility(expr.getLeft(), expr.getRight(), info);
                    produceNotEquals(expr.getLeft(), expr.getRight(), expressions, info);
                    return;
                }
            case JDOExpressionIF.CONTAINS:
                {
                    JDOContains expr = (JDOContains) jdoexpr;
                    checkCompatibility(expr.getLeft(), Collection.class, info);
                    produceContains(expr.getLeft(), expr.getRight(), expressions, info);
                    return;
                }
            case JDOExpressionIF.IS_EMPTY:
                {
                    JDOIsEmpty expr = (JDOIsEmpty) jdoexpr;
                    checkCompatibility(expr.getValue(), Collection.class, info);
                    produceIsEmpty(expr.getValue(), expressions, info);
                    return;
                }
            case JDOExpressionIF.STARTS_WITH:
                {
                    JDOStartsWith expr = (JDOStartsWith) jdoexpr;
                    checkCompatibility(expr.getLeft(), expr.getRight(), info);
                    produceStartsWith(expr.getLeft(), expr.getRight(), expressions, info);
                    return;
                }
            case JDOExpressionIF.ENDS_WITH:
                {
                    JDOEndsWith expr = (JDOEndsWith) jdoexpr;
                    checkCompatibility(expr.getLeft(), expr.getRight(), info);
                    produceEndsWith(expr.getLeft(), expr.getRight(), expressions, info);
                    return;
                }
            case JDOExpressionIF.LIKE:
                {
                    JDOLike expr = (JDOLike) jdoexpr;
                    checkCompatibility(expr.getLeft(), expr.getRight(), info);
                    produceLike(expr.getLeft(), expr.getRight(), expr.getCaseSensitive(), expressions, info);
                    return;
                }
            case JDOExpressionIF.AND:
                {
                    produceAnd((JDOAnd) jdoexpr, expressions, info);
                    return;
                }
            case JDOExpressionIF.OR:
                {
                    produceOr((JDOOr) jdoexpr, expressions, info);
                    return;
                }
            case JDOExpressionIF.NOT:
                {
                    produceNot((JDONot) jdoexpr, expressions, info);
                    return;
                }
            case JDOExpressionIF.BOOLEAN:
                {
                    produceBoolean((JDOBoolean) jdoexpr, expressions, info);
                    return;
                }
            case JDOExpressionIF.VALUE_EXPRESSION:
                {
                    produceValueExpression((JDOValueExpression) jdoexpr, expressions, info);
                    return;
                }
            default:
                throw new OntopiaRuntimeException("Expression is of unknown type: '" + jdoexpr + "'");
        }
    }

    protected SQLSetOperation produceSetOperation(JDOSetOperation setop_expr, BuildInfo info) {
        List jdosets = setop_expr.getSets();
        List sqlsets = new ArrayList(jdosets.size());
        int length = jdosets.size();
        for (int i = 0; i < length; i++) {
            Object set = jdosets.get(i);
            if (set instanceof JDOQuery) sqlsets.add(makeQuery((JDOQuery) set, info.oaccess)); else sqlsets.add(produceSetOperation((JDOSetOperation) set, info));
        }
        int optype;
        switch(setop_expr.getOperator()) {
            case JDOSetOperation.UNION:
                optype = SQLSetOperation.UNION;
                break;
            case JDOSetOperation.UNION_ALL:
                optype = SQLSetOperation.UNION_ALL;
                break;
            case JDOSetOperation.INTERSECT:
                optype = SQLSetOperation.INTERSECT;
                break;
            case JDOSetOperation.EXCEPT:
                optype = SQLSetOperation.EXCEPT;
                break;
            default:
                throw new OntopiaRuntimeException("Unsupported set operator: '" + setop_expr.getOperator() + "'");
        }
        return new SQLSetOperation(sqlsets, optype);
    }

    protected void produceBoolean(JDOBoolean boolean_expr, List expressions, BuildInfo info) {
        SQLValueIF value = new SQLPrimitive(new Integer(0), Types.INTEGER);
        if (boolean_expr.getValue()) expressions.add(new SQLEquals(value, value)); else expressions.add(new SQLNotEquals(value, value));
    }

    protected void produceValueExpression(JDOValueExpression jdoexpr, List expressions, BuildInfo info) {
        expressions.add(new SQLValueExpression(produceValue(jdoexpr.getValue(), expressions, info)));
    }

    protected void produceAnd(JDOAnd and_expr, List expressions, BuildInfo info) {
        expressions.add(new SQLAnd(produceExpressions(and_expr.getExpressions(), info)));
    }

    protected void produceNot(JDONot not_expr, List expressions, BuildInfo info) {
        JDOExpressionIF jdoexpr = not_expr.getExpression();
        List templist = new ArrayList();
        produceExpression(jdoexpr, templist, info);
        expressions.add(new SQLNot(new SQLExists(makeAndExpression(templist))));
    }

    protected void produceOr(JDOOr or_expr, List expressions, BuildInfo info) {
        JDOExpressionIF[] jdoexprs = or_expr.getExpressions();
        SQLExpressionIF[] sqlexprs = new SQLExpressionIF[jdoexprs.length];
        for (int i = 0; i < jdoexprs.length; i++) {
            List templist = new ArrayList();
            produceExpression(jdoexprs[i], templist, info);
            sqlexprs[i] = new SQLExists(makeAndExpression(templist));
        }
        expressions.add(makeOrExpression(sqlexprs));
    }

    protected SQLExpressionIF[] produceExpressions(JDOExpressionIF[] jdoexprs, BuildInfo info) {
        List expressions = new ArrayList();
        for (int i = 0; i < jdoexprs.length; i++) {
            produceExpression(jdoexprs[i], expressions, info);
        }
        SQLExpressionIF[] result = new SQLExpressionIF[expressions.size()];
        expressions.toArray(result);
        return result;
    }

    protected void produceEquals(JDOValueIF left, JDOValueIF right, List expressions, BuildInfo info) {
        expressions.add(new SQLEquals(produceValue(left, expressions, info), produceValue(right, expressions, info)));
    }

    protected void produceNotEquals(JDOValueIF left, JDOValueIF right, List expressions, BuildInfo info) {
        expressions.add(new SQLNotEquals(produceValue(left, expressions, info), produceValue(right, expressions, info)));
    }

    protected void produceContains(JDOValueIF left, JDOValueIF right, List expressions, BuildInfo info) {
        switch(left.getType()) {
            case JDOValueIF.FIELD:
                {
                    JDOField field = (JDOField) left;
                    FieldInfoIF finfo = getFieldInfo(field, info);
                    if (!finfo.isCollectionField() && (finfo.getValueClassInfo() != null && finfo.getValueClassInfo().getStructure() != ClassInfoIF.STRUCTURE_COLLECTION)) throw new OntopiaRuntimeException("contains's left field must be a collection field: '" + left + "'");
                    SQLTable endtable = null;
                    switch(right.getType()) {
                        case JDOValueIF.PARAMETER:
                        case JDOValueIF.VARIABLE:
                            {
                                endtable = info.createNamedValueTable(right, expressions);
                                break;
                            }
                        case JDOValueIF.OBJECT:
                            {
                                break;
                            }
                        default:
                            throw new OntopiaRuntimeException("Unsupported contains right value: '" + right + "'");
                    }
                    JDOValueIF root = field.getRoot();
                    switch(root.getType()) {
                        case JDOValueIF.PARAMETER:
                        case JDOValueIF.VARIABLE:
                            {
                                Values lvalues = produceVariableFieldValues(root, field.getPath(), endtable, expressions, info);
                                SQLValueIF vcols = lvalues.vcols;
                                if (finfo.getCardinality() == FieldInfoIF.MANY_TO_MANY || finfo.getCardinality() == FieldInfoIF.ONE_TO_MANY) {
                                    SQLValueIF rvalue = produceValue(right, expressions, info);
                                    expressions.add(new SQLEquals(vcols, rvalue));
                                } else if (finfo.getValueClassInfo() != null && finfo.getValueClassInfo().getStructure() == ClassInfoIF.STRUCTURE_COLLECTION) {
                                    ClassInfoIF vcinfo = finfo.getValueClassInfo();
                                    SQLTable table = new SQLTable(vcinfo.getMasterTable(), info.createTableAlias("T"));
                                    SQLColumns rvalue = new SQLColumns(table, vcinfo.getIdentityFieldInfo().getValueColumns());
                                    expressions.add(new SQLEquals(vcols, rvalue));
                                    SQLValueIF rvalue1 = produceValue(right, expressions, info);
                                    SQLValueIF rvalue2 = new SQLColumns(table, vcinfo.getFieldInfoByName("element").getValueColumns());
                                    expressions.add(new SQLEquals(rvalue1, rvalue2));
                                } else {
                                    throw new OntopiaRuntimeException("contains's left field of unknown collection type: '" + left + "'");
                                }
                                break;
                            }
                        case JDOValueIF.OBJECT:
                            {
                                Values lvalues = produceObjectFieldValues((JDOObject) root, field.getPath(), info);
                                if (!(lvalues.finfo.isCollectionField() || (finfo.getValueClassInfo() != null && finfo.getValueClassInfo().getStructure() == ClassInfoIF.STRUCTURE_COLLECTION))) throw new OntopiaRuntimeException("contains's left field is not of collection type: '" + left + "'");
                                if (lvalues.vcols.getType() == SQLValueIF.NULL || (lvalues.vcols.getType() == SQLValueIF.TUPLE && ((SQLTuple) lvalues.vcols).getArity() == 0)) {
                                    expressions.add(SQLFalse.getInstance());
                                } else {
                                    SQLValueIF rval = produceValue(right, expressions, info);
                                    if (rval.getArity() == 1 && lvalues.vcols.getType() == SQLValueIF.TUPLE && lvalues.vcols.getArity() > 1) {
                                        SQLValueIF[] vals = ((SQLTuple) lvalues.vcols).getValues();
                                        for (int i = 0; i < vals.length; i++) {
                                            expressions.add(new SQLEquals(vals[i], rval));
                                        }
                                    } else {
                                        expressions.add(new SQLEquals(lvalues.vcols, rval));
                                    }
                                }
                                break;
                            }
                        default:
                            throw new OntopiaRuntimeException("Unsupported contains left value: '" + left + "'");
                    }
                    return;
                }
            case JDOValueIF.VARIABLE:
                {
                    JDOVariable var = (JDOVariable) left;
                    FieldInfoIF finfo = getFieldInfo(var, info);
                    ClassInfoIF cinfo = finfo.getParentClassInfo();
                    if (cinfo.getStructure() == ClassInfoIF.STRUCTURE_COLLECTION) {
                        SQLTable table = info.createNamedValueTable(var, expressions);
                        SQLValueIF lvalue = new SQLColumns(table, cinfo.getFieldInfoByName("element").getValueColumns());
                        SQLValueIF rvalue = produceValue(right, expressions, info);
                        expressions.add(new SQLEquals(lvalue, rvalue));
                        return;
                    } else throw new OntopiaRuntimeException("variable.contains(...) expression must be of type with collection structure: " + var);
                }
            case JDOValueIF.PARAMETER:
                {
                    JDOParameter param = (JDOParameter) left;
                    String parname = param.getName();
                    switch(right.getType()) {
                        case JDOValueIF.VARIABLE:
                            {
                                JDOVariable var = (JDOVariable) right;
                                FieldInfoIF finfo = getFieldInfo(var, info);
                                int arity = finfo.getColumnCount();
                                if (arity != 1) throw new OntopiaRuntimeException("parameter<collection>.contains(variable) requires a value arity of exactly 1: " + var);
                                SQLParameter sqlparam = new SQLParameter(parname, arity);
                                sqlparam.setValueType(info.jdoquery.getParameterType(parname));
                                sqlparam.setFieldHandler(finfo);
                                expressions.add(new SQLIn(produceValue(var, expressions, info), sqlparam));
                                return;
                            }
                        default:
                            throw new OntopiaRuntimeException("Unsupported contains right value: '" + right + "'");
                    }
                }
            case JDOValueIF.COLLECTION:
                {
                    expressions.add(new SQLIn(produceValue(right, expressions, info), produceCollection((JDOCollection) left, info)));
                    return;
                }
            default:
                throw new OntopiaRuntimeException("Unsupported contains left value: '" + left + "'");
        }
    }

    protected void produceIsEmpty(JDOValueIF value, List expressions, BuildInfo info) {
        switch(value.getType()) {
            case JDOValueIF.FIELD:
                {
                    JDOField field = (JDOField) value;
                    FieldInfoIF finfo = getFieldInfo(field, info);
                    if (!finfo.isCollectionField() && (finfo.getValueClassInfo() != null && finfo.getValueClassInfo().getStructure() != ClassInfoIF.STRUCTURE_COLLECTION)) throw new OntopiaRuntimeException("isEmpty's field must be a collection field: '" + field + "'");
                    List lexpressions = new ArrayList();
                    Values lvalues = produceFieldValues(field, null, lexpressions, info);
                    if (finfo.getValueClassInfo() != null && finfo.getValueClassInfo().getStructure() == ClassInfoIF.STRUCTURE_COLLECTION) {
                        ClassInfoIF vcinfo = finfo.getValueClassInfo();
                        SQLTable table = new SQLTable(vcinfo.getMasterTable(), info.createTableAlias("T"));
                        SQLColumns rvalue = new SQLColumns(table, vcinfo.getIdentityFieldInfo().getValueColumns());
                        lexpressions.add(new SQLEquals(lvalues.vcols, rvalue));
                    }
                    expressions.add(new SQLNot(new SQLExists(makeAndExpression(lexpressions))));
                    return;
                }
            case JDOValueIF.VARIABLE:
                {
                    throw new OntopiaRuntimeException("variable<collection>.isEmpty() not yet supported.");
                }
            default:
                throw new OntopiaRuntimeException("Unsupported isEmpty value: '" + value + "'");
        }
    }

    protected void produceStartsWith(JDOValueIF left, JDOValueIF right, List expressions, BuildInfo info) {
        produceLikeWithPattern(left, right, false, expressions, true, info);
    }

    protected void produceEndsWith(JDOValueIF left, JDOValueIF right, List expressions, BuildInfo info) {
        produceLikeWithPattern(left, right, false, expressions, false, info);
    }

    protected void produceLike(JDOValueIF left, JDOValueIF right, boolean caseSensitive, List expressions, BuildInfo info) {
        expressions.add(new SQLLike(produceValue(left, expressions, info), produceValue(right, expressions, info), caseSensitive));
    }

    protected void produceLikeWithPattern(JDOValueIF left, JDOValueIF right, boolean caseSensitive, List expressions, boolean starts_not_ends, BuildInfo info) {
        SQLValueIF lvalue = produceValue(left, expressions, info);
        int arity = lvalue.getArity();
        if (arity != 1) if (starts_not_ends) throw new OntopiaRuntimeException("Arity of left String.startsWith value is not 1: " + arity); else throw new OntopiaRuntimeException("Arity of left String.endsWith value is not 1: " + arity);
        switch(right.getType()) {
            case JDOValueIF.STRING:
            case JDOValueIF.PRIMITIVE:
                String value = ((JDOString) right).getValue();
                if (starts_not_ends) expressions.add(new SQLLike(lvalue, new SQLPrimitive(value + "%", Types.VARCHAR), caseSensitive)); else expressions.add(new SQLLike(lvalue, new SQLPrimitive("%" + value, Types.VARCHAR), caseSensitive));
                return;
            default:
                expressions.add(new SQLLike(lvalue, produceValue(right, expressions, info), caseSensitive));
                return;
        }
    }

    protected SQLValueIF[] produceValues(JDOValueIF[] values, List expressions, BuildInfo info) {
        SQLValueIF[] retval = new SQLValueIF[values.length];
        for (int i = 0; i < values.length; i++) {
            retval[i] = produceValue(values[i], expressions, info);
        }
        return retval;
    }

    protected SQLValueIF produceValue(JDOValueIF value, List expressions, BuildInfo info) {
        switch(value.getType()) {
            case JDOValueIF.FIELD:
                return produceField((JDOField) value, null, expressions, info);
            case JDOValueIF.VARIABLE:
                return produceVariable((JDOVariable) value, expressions, info);
            case JDOValueIF.PARAMETER:
                return produceParameter((JDOParameter) value, expressions, info);
            case JDOValueIF.PRIMITIVE:
                return producePrimitive((JDOPrimitive) value, info);
            case JDOValueIF.OBJECT:
                return produceObject((JDOObject) value, info);
            case JDOValueIF.STRING:
                return new SQLPrimitive(((JDOString) value).getValue(), Types.VARCHAR);
            case JDOValueIF.COLLECTION:
                return produceCollection((JDOCollection) value, info);
            case JDOValueIF.NULL:
                return new SQLNull();
            case JDOValueIF.NATIVE_VALUE:
                return produceNativeValue((JDONativeValue) value, expressions, info);
            case JDOValueIF.FUNCTION:
                return produceFunction((JDOFunction) value, expressions, info);
            default:
                throw new OntopiaRuntimeException("Unsupported value: '" + value + "'");
        }
    }

    protected SQLValueIF produceField(JDOField field, SQLTable endtable, List expressions, BuildInfo info) {
        return produceFieldValues(field, endtable, expressions, info).vcols;
    }

    static class Values {

        Values prev;

        SQLValueIF vcols;

        SQLValueIF jcols;

        Class vtype;

        FieldInfoIF finfo;

        Values getFirst() {
            if (prev != null) return prev.getFirst(); else return this;
        }

        public String toString() {
            return "[" + vcols + ", " + jcols + "]";
        }
    }

    protected Values produceFieldValues(JDOField field, SQLTable endtable, List expressions, BuildInfo info) {
        JDOValueIF root = field.getRoot();
        switch(root.getType()) {
            case JDOValueIF.VARIABLE:
            case JDOValueIF.PARAMETER:
                return produceVariableFieldValues(root, field.getPath(), endtable, expressions, info);
            case JDOValueIF.OBJECT:
                return produceObjectFieldValues((JDOObject) root, field.getPath(), info);
            default:
                throw new OntopiaRuntimeException("Only variables are supported field roots at this time: '" + root + "'");
        }
    }

    protected Values produceVariableFieldValues(JDOValueIF root, String[] path, SQLTable endtable, List expressions, BuildInfo info) {
        Values pvalues = null;
        Values cvalues = null;
        pvalues = new Values();
        pvalues.vcols = produceValue(root, expressions, info);
        pvalues.vtype = getValueType(root, info);
        pvalues.finfo = null;
        for (int i = 0; i < path.length; i++) {
            String fname = path[i];
            if (pvalues.finfo != null && pvalues.finfo.getCardinality() != FieldInfoIF.ONE_TO_ONE) throw new OntopiaRuntimeException("Field navigation can only be used with single value fields.");
            ClassInfoIF pcinfo = mapping.getClassInfo(pvalues.vtype);
            FieldInfoIF finfo = pcinfo.getFieldInfoByName(fname);
            if (finfo == null) throw new OntopiaRuntimeException("'" + pvalues.vtype + "' does not have a field called '" + fname + "'.");
            cvalues = new Values();
            cvalues.prev = pvalues;
            cvalues.vtype = finfo.getValueClass();
            cvalues.finfo = finfo;
            SQLValueIF pvalues_vcols = pvalues.vcols;
            switch(finfo.getCardinality()) {
                case FieldInfoIF.ONE_TO_ONE:
                    {
                        String tblname = finfo.getTable();
                        if (tblname == null || (pvalues_vcols instanceof SQLColumns && tblname.equals(((SQLColumns) pvalues_vcols).getTable().getName()))) {
                            cvalues.vcols = new SQLColumns(((SQLColumns) pvalues_vcols).getTable(), finfo.getValueColumns());
                            cvalues.jcols = null;
                        } else {
                            SQLTable table = new SQLTable(tblname, info.createTableAlias("T"));
                            if (!mapping.isDeclared(cvalues.vtype)) {
                                cvalues.vcols = new SQLColumns(table, finfo.getValueColumns());
                                cvalues.jcols = pvalues_vcols;
                            } else {
                                ClassInfoIF vcinfo = mapping.getClassInfo(cvalues.vtype);
                                if (vcinfo.isAggregate()) {
                                    cvalues.vcols = new SQLColumns(table, finfo.getValueColumns());
                                    cvalues.jcols = new SQLColumns(table, pcinfo.getIdentityFieldInfo().getValueColumns());
                                } else {
                                    cvalues.vcols = new SQLColumns(table, finfo.getValueColumns());
                                    cvalues.jcols = pvalues_vcols;
                                }
                            }
                            expressions.add(new SQLEquals(pvalues_vcols, cvalues.vcols));
                        }
                        break;
                    }
                case FieldInfoIF.ONE_TO_MANY:
                    {
                        SQLTable table;
                        String tblname = finfo.getJoinTable();
                        if (endtable != null) {
                            table = endtable;
                            if (!tblname.equals(endtable.getName())) throw new OntopiaRuntimeException("Incompatible tables: '" + tblname + "' <-> '" + endtable.getName() + "'.");
                        } else table = new SQLTable(tblname, info.createTableAlias("O"));
                        ClassInfoIF _cinfo = finfo.getValueClassInfo();
                        cvalues.jcols = new SQLColumns(table, finfo.getJoinKeys());
                        if (_cinfo.isAggregate()) cvalues.vcols = new SQLColumns(table, getInlineColumns(finfo)); else cvalues.vcols = new SQLColumns(table, _cinfo.getIdentityFieldInfo().getValueColumns());
                        expressions.add(new SQLEquals(pvalues_vcols, cvalues.jcols));
                        break;
                    }
                case FieldInfoIF.MANY_TO_MANY:
                    {
                        ClassInfoIF _cinfo = finfo.getValueClassInfo();
                        String tblname = _cinfo.getMasterTable();
                        if (endtable != null) {
                            if (!tblname.equals(endtable.getName())) throw new OntopiaRuntimeException("Incompatible tables: '" + tblname + "' <-> '" + endtable.getName() + "'.");
                        }
                        SQLTable j_table = new SQLTable(finfo.getJoinTable(), info.createTableAlias("M"));
                        cvalues.vcols = new SQLColumns(j_table, finfo.getManyKeys());
                        cvalues.jcols = new SQLColumns(j_table, finfo.getJoinKeys());
                        expressions.add(new SQLEquals(pvalues_vcols, cvalues.jcols));
                        break;
                    }
                default:
                    throw new OntopiaRuntimeException("Invalid field cardinality: '" + finfo.getCardinality() + "'");
            }
            pvalues = cvalues;
        }
        return cvalues;
    }

    protected Values produceObjectFieldValues(JDOObject obj, String[] path, BuildInfo info) {
        Object value = obj.getValue();
        Class ctype = value.getClass();
        FieldInfoIF finfo = null;
        for (int i = 0; i < path.length; i++) {
            if (mapping.isDeclared(ctype)) {
                ClassInfoIF cinfo = mapping.getClassInfo(ctype);
                finfo = cinfo.getFieldInfoByName(path[i]);
                if (finfo == null) throw new OntopiaRuntimeException("Parent '" + ctype + "' do not have field called '" + path[i] + "'");
                if (cinfo.isIdentifiable()) {
                    value = info.oaccess.getValue(value, finfo);
                } else {
                    try {
                        value = finfo.getValue(value);
                    } catch (Exception e) {
                        throw new OntopiaRuntimeException(e);
                    }
                }
                ctype = finfo.getValueClass();
            } else throw new OntopiaRuntimeException("Parent of field  '" + path[i] + "' of undeclared type: '" + ctype + "'");
        }
        Values values = new Values();
        values.finfo = finfo;
        if (value != null) {
            if (finfo.isCollectionField() || (finfo.getValueClassInfo() != null && finfo.getValueClassInfo().getStructure() == ClassInfoIF.STRUCTURE_COLLECTION)) {
                Collection cvalue = (Collection) value;
                List tuples = new ArrayList(cvalue.size());
                Iterator iter = cvalue.iterator();
                while (iter.hasNext()) {
                    List list = new ArrayList(finfo.getColumnCount());
                    finfo.retrieveSQLValues(iter.next(), list);
                    tuples.add((list.size() == 1 ? list.get(0) : new SQLTuple(list)));
                }
                values.vcols = (tuples.size() == 1 ? (SQLValueIF) tuples.get(0) : new SQLTuple(tuples));
            } else {
                List list = new ArrayList();
                finfo.retrieveSQLValues(value, list);
                values.vcols = (list.size() == 1 ? (SQLValueIF) list.get(0) : new SQLTuple(list));
            }
        } else {
            values.vcols = new SQLNull();
        }
        return values;
    }

    protected SQLValueIF produceVariable(JDOVariable var, List expressions, BuildInfo info) {
        String varname = var.getName();
        if (isIdentifiableVariable(varname, info.jdoquery)) {
            FieldInfoIF finfo = getFieldInfo(var, info);
            SQLTable table = info.createNamedValueTable(var, expressions);
            return new SQLColumns(table, finfo.getValueColumns());
        } else if (isAggregateVariable(varname, info.jdoquery) || isPrimitiveVariable(varname, info.jdoquery)) {
            FieldInfoIF finfo = (FieldInfoIF) info.nfvals.get(var);
            SQLTable table = info.createNamedValueTable(var, expressions);
            return new SQLColumns(table, finfo.getValueColumns());
        } else {
            throw new OntopiaRuntimeException("Variable '" + varname + "' of unknown type." + info.jdoquery.getVariableNames() + " " + info.jdoquery.getVariableType(varname));
        }
    }

    protected SQLValueIF produceParameter(JDOParameter par, List expressions, BuildInfo info) {
        String parname = par.getName();
        if (isIdentifiableParameter(parname, info.jdoquery)) {
            FieldInfoIF finfo = getFieldInfo(par, info);
            SQLTable table = info.createNamedValueTable(par, expressions);
            return new SQLColumns(table, finfo.getValueColumns());
        } else if (isAggregateParameter(parname, info.jdoquery) || isPrimitiveParameter(parname, info.jdoquery)) {
            FieldInfoIF finfo = (FieldInfoIF) info.nfvals.get(par);
            SQLTable table = info.createNamedValueTable(par, expressions);
            return new SQLColumns(table, finfo.getValueColumns());
        } else {
            throw new OntopiaRuntimeException("Parameter '" + parname + "' of unknown type. " + info.jdoquery.getParameterNames() + " " + info.jdoquery.getParameterType(parname));
        }
    }

    protected SQLValueIF producePrimitive(JDOPrimitive primitive, BuildInfo info) {
        switch(primitive.getPrimitiveType()) {
            case JDOPrimitive.INTEGER:
                return new SQLPrimitive(primitive.getValue(), Types.INTEGER);
            case JDOPrimitive.LONG:
                return new SQLPrimitive(primitive.getValue(), Types.BIGINT);
            case JDOPrimitive.SHORT:
                return new SQLPrimitive(primitive.getValue(), Types.SMALLINT);
            case JDOPrimitive.FLOAT:
                return new SQLPrimitive(primitive.getValue(), Types.REAL);
            case JDOPrimitive.DOUBLE:
                return new SQLPrimitive(primitive.getValue(), Types.DOUBLE);
            case JDOPrimitive.BOOLEAN:
                return new SQLPrimitive(primitive.getValue(), Types.BIT);
            case JDOPrimitive.BYTE:
                return new SQLPrimitive(primitive.getValue(), Types.TINYINT);
            case JDOPrimitive.BIGDECIMAL:
                return new SQLPrimitive(primitive.getValue(), Types.DECIMAL);
            case JDOPrimitive.BIGINTEGER:
                return new SQLPrimitive(primitive.getValue(), Types.NUMERIC);
            default:
                throw new OntopiaRuntimeException("Unsupported primitive type: '" + primitive.getPrimitiveType() + "'");
        }
    }

    protected SQLValueIF produceNativeValue(JDONativeValue field, List expressions, BuildInfo info) {
        JDOVariable var = field.getRoot();
        SQLColumns varcols = (SQLColumns) produceVariable(var, expressions, info);
        return new SQLColumns(varcols.getTable(), field.getArguments());
    }

    protected SQLValueIF produceFunction(JDOFunction func, List expressions, BuildInfo info) {
        return new SQLFunction(func.getName(), produceValues(func.getArguments(), expressions, info));
    }

    protected SQLValueIF produceObject(JDOObject object, BuildInfo info) {
        List values = new ArrayList();
        FieldInfoIF id_finfo = getFieldInfo(object, info);
        id_finfo.retrieveSQLValues(object.getValue(), values);
        if (values.size() == 1) return (SQLValueIF) values.get(0); else return new SQLTuple(values);
    }

    protected SQLValueIF produceCollection(JDOCollection coll, BuildInfo info) {
        List values = new ArrayList();
        FieldInfoIF id_finfo = getFieldInfo(coll, info);
        Collection _coll = coll.getValue();
        Iterator iter = _coll.iterator();
        while (iter.hasNext()) {
            id_finfo.retrieveSQLValues(iter.next(), values);
        }
        if (values.size() == 1) return (SQLValueIF) values.get(0); else return new SQLTuple(values);
    }

    protected FieldInfoIF getFieldInfo(JDOValueIF jdovalue, BuildInfo info) {
        switch(jdovalue.getType()) {
            case JDOValueIF.FIELD:
                return getFieldInfo((JDOField) jdovalue, info);
            case JDOValueIF.VARIABLE:
                return getFieldInfo((JDOVariable) jdovalue, info);
            case JDOValueIF.PARAMETER:
                return getFieldInfo((JDOParameter) jdovalue, info);
            case JDOValueIF.OBJECT:
                return getFieldInfo((JDOObject) jdovalue, info);
            case JDOValueIF.COLLECTION:
                return getFieldInfo((JDOCollection) jdovalue, info);
            default:
                return null;
        }
    }

    protected FieldInfoIF getFieldInfo(JDOVariable var, BuildInfo info) {
        Class vtype = info.jdoquery.getVariableType(var.getName());
        if (mapping.isDeclared(vtype)) {
            ClassInfoIF cinfo = mapping.getClassInfo(vtype);
            return cinfo.getIdentityFieldInfo();
        } else return null;
    }

    protected FieldInfoIF getFieldInfo(JDOParameter param, BuildInfo info) {
        Class ptype = info.jdoquery.getParameterType(param.getName());
        ClassInfoIF cinfo = mapping.getClassInfo(ptype);
        return cinfo.getIdentityFieldInfo();
    }

    protected FieldInfoIF getFieldInfo(JDOObject object, BuildInfo info) {
        Class otype = object.getValueType();
        ClassInfoIF cinfo = mapping.getClassInfo(otype);
        return cinfo.getIdentityFieldInfo();
    }

    protected FieldInfoIF getFieldInfo(JDOCollection coll, BuildInfo info) {
        Class eltype = coll.getElementType();
        if (isPrimitiveType(eltype)) return null;
        ClassInfoIF cinfo = mapping.getClassInfo(eltype);
        return cinfo.getIdentityFieldInfo();
    }

    protected FieldInfoIF getFieldInfo(JDOField field, BuildInfo info) {
        Class ctype = getValueType(field.getRoot(), info);
        String[] path = field.getPath();
        FieldInfoIF finfo = null;
        for (int i = 0; i < path.length; i++) {
            if (mapping.isDeclared(ctype)) {
                ClassInfoIF cinfo = mapping.getClassInfo(ctype);
                finfo = cinfo.getFieldInfoByName(path[i]);
                if (finfo == null) throw new OntopiaRuntimeException("Parent '" + ctype + "' do not have field called '" + path[i] + "'");
                ctype = finfo.getValueClass();
            } else throw new OntopiaRuntimeException("Parent of field  '" + path[i] + "' of undeclared type: '" + ctype + "'");
        }
        return finfo;
    }

    protected Class checkCompatibility(JDOValueIF value1, JDOValueIF value2, BuildInfo info) {
        Class type1 = getValueType(value1, info);
        Class type2 = getValueType(value2, info);
        if (type1 == null) return type2;
        if (type2 == null) return type1;
        if (type1 != type2) {
            if (type1 == String.class && type2 == java.io.Reader.class) return String.class; else if (type1 == java.io.Reader.class && type2 == String.class) return String.class; else throw new OntopiaRuntimeException("Values '" + value1 + "' (" + type1 + ") and '" + value2 + "' (" + type2 + ") are not compatible.");
        }
        return type1;
    }

    protected Class checkCompatibility(JDOValueIF value, Class type, BuildInfo info) {
        Class vtype = getValueType(value, info);
        if (!type.isAssignableFrom(vtype)) throw new OntopiaRuntimeException("Value '" + value + "' (" + vtype + ") is not compatible with type " + type + ".");
        return vtype;
    }

    protected Class getValueType(JDOValueIF value, BuildInfo info) {
        switch(value.getType()) {
            case JDOValueIF.FIELD:
                return getValueType((JDOField) value, info);
            case JDOValueIF.VARIABLE:
                return info.jdoquery.getVariableType(((JDOVariable) value).getName());
            case JDOValueIF.PARAMETER:
                return info.jdoquery.getParameterType(((JDOParameter) value).getName());
            case JDOValueIF.PRIMITIVE:
                return ((JDOPrimitive) value).getValueType();
            case JDOValueIF.OBJECT:
                return ((JDOObject) value).getValueType();
            case JDOValueIF.STRING:
                return java.lang.String.class;
            case JDOValueIF.COLLECTION:
                return ((JDOCollection) value).getValueType();
            case JDOValueIF.NATIVE_VALUE:
                return ((JDONativeValue) value).getValueType();
            case JDOValueIF.NULL:
                return null;
            case JDOValueIF.FUNCTION:
                return ((JDOFunction) value).getValueType();
            default:
                throw new OntopiaRuntimeException("Invalid value: '" + value + "'");
        }
    }

    protected Class getValueType(JDOField field, BuildInfo info) {
        FieldInfoIF finfo = getFieldInfo(field, info);
        if (finfo == null) throw new OntopiaRuntimeException("Unknown field: '" + field + "'");
        if (finfo.isCollectionField()) return Collection.class; else {
            Class klass = finfo.getValueClass();
            return (klass == java.io.Reader.class ? String.class : klass);
        }
    }

    protected boolean isIdentifiableValueType(JDOValueIF jdovalue, BuildInfo info) {
        return (getIdentifiableValueType(jdovalue, info) != null);
    }

    protected Class getIdentifiableValueType(JDOValueIF jdovalue, BuildInfo info) {
        Class ctype = null;
        switch(jdovalue.getType()) {
            case JDOValueIF.FIELD:
                return getIdentifiableValueType((JDOField) jdovalue, info);
            case JDOValueIF.VARIABLE:
                ctype = getValueType((JDOVariable) jdovalue, info);
                break;
            case JDOValueIF.PARAMETER:
                ctype = getValueType((JDOParameter) jdovalue, info);
                break;
            case JDOValueIF.OBJECT:
                ctype = getValueType((JDOObject) jdovalue, info);
                break;
            case JDOValueIF.COLLECTION:
                ctype = getValueType((JDOCollection) jdovalue, info);
                break;
            default:
                return null;
        }
        return (isIdentifiableType(ctype) ? ctype : null);
    }

    protected Class getIdentifiableValueType(JDOField field, BuildInfo info) {
        Class ctype = getValueType(field.getRoot(), info);
        String[] path = field.getPath();
        FieldInfoIF finfo = null;
        for (int i = 0; i < path.length; i++) {
            if (mapping.isDeclared(ctype)) {
                ClassInfoIF cinfo = mapping.getClassInfo(ctype);
                finfo = cinfo.getFieldInfoByName(path[i]);
                if (finfo == null) throw new OntopiaRuntimeException("Parent '" + ctype + "' do not have field called '" + path[i] + "'");
                Class _ctype = finfo.getValueClass();
                if (isIdentifiableType(_ctype)) ctype = _ctype; else break;
            } else throw new OntopiaRuntimeException("Parent of field  '" + path[i] + "' of undeclared type: '" + ctype + "'");
        }
        return (isIdentifiableType(ctype) ? ctype : null);
    }

    protected boolean isIdentifiableVariable(String var, JDOQuery jdoquery) {
        return isIdentifiableType(jdoquery.getVariableType(var));
    }

    protected boolean isIdentifiableParameter(String param, JDOQuery jdoquery) {
        return isIdentifiableType(jdoquery.getParameterType(param));
    }

    protected boolean isIdentifiableType(Class type) {
        if (mapping.isDeclared(type)) {
            ClassInfoIF cinfo = mapping.getClassInfo(type);
            return cinfo.isIdentifiable();
        } else return false;
    }

    protected boolean isAggregateVariable(String var, JDOQuery jdoquery) {
        return isAggregateType(jdoquery.getVariableType(var));
    }

    protected boolean isAggregateParameter(String param, JDOQuery jdoquery) {
        return isAggregateType(jdoquery.getParameterType(param));
    }

    protected boolean isAggregateType(Class type) {
        if (mapping.isDeclared(type)) {
            ClassInfoIF cinfo = mapping.getClassInfo(type);
            return cinfo.isAggregate();
        } else return false;
    }

    protected boolean isPrimitiveVariable(String var, JDOQuery jdoquery) {
        return isPrimitiveType(jdoquery.getVariableType(var));
    }

    protected boolean isPrimitiveParameter(String param, JDOQuery jdoquery) {
        return isPrimitiveType(jdoquery.getParameterType(param));
    }

    protected boolean isPrimitiveType(Class type) {
        if (type == java.lang.String.class || type == java.io.Reader.class || type == java.lang.Integer.class || type == java.lang.Float.class || type == java.lang.Long.class) {
            return true;
        }
        return false;
    }
}
