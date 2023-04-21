package com.incendiaryblue.cmslite.search;

import com.incendiaryblue.cmslite.*;
import com.incendiaryblue.database.Database;
import com.incendiaryblue.database.SQLDialect;
import java.util.*;
import com.incendiaryblue.lang.StringHelper;
import java.sql.*;
import java.util.Date;
import Zql.*;
import com.incendiaryblue.appframework.AppConfig;

/**
 * An abstract class representing a search query on the incendiaryblue Java database.
 * A subclass will typically make calls to the addXXX() mthods to set up the
 * select fields, joins etc so that a call to buildSql() will produce the
 * required SQL query string.
 */
public abstract class SearchSelectQuery {

    private static SQLDialect dialect = ((Database) AppConfig.getDefaultComponent(Database.class)).getSQLDialect();

    private static Map itemAliases = new HashMap();

    private Node node;

    private CategoryStub category;

    private Structure structure;

    private List tableAliases = new ArrayList();

    private Map subselects = new HashMap();

    private List joinExprs = new ArrayList();

    private String whereClause = null;

    private List orderByList = new ArrayList();

    private Set selectFields = new HashSet();

    protected SearchSelectQuery(Node n) {
        this.category = n.getCategoryStub();
        this.node = n;
    }

    /**
	 * Create a new SearchSelectQuery on the given Category
	 */
    protected SearchSelectQuery(Node n, Structure structure) {
        this(n);
        this.structure = structure;
    }

    protected Node getNode() {
        return node;
    }

    protected boolean getRecursive() {
        return false;
    }

    /**
	 * Get the Structure of the categories being searched
	 */
    protected Structure getStructure() {
        return structure;
    }

    /**
	 * Get the ID of the category being searched
	 */
    protected Integer getCategoryId() {
        return (Integer) category.getPrimaryKey();
    }

    /**
	 * Converts friendly names from the (content) where and order clauses of the
	 * query to actual database fields. Invalid field names found will cause a
	 * SearchFormatException to be thrown. Rules for field names in a content
	 * query are:
	 *
	 * To search a structure item (i.e. CMS field):
	 * [field.]<FIELD_NAME> e.g. 'field.summary', 'field.headline', 'headline'
	 *
	 * To search a content table field (e.g. name, description):
	 * content.<FIELD_NAME> e.g. 'content.name', 'content.description'
	 */
    protected String convertField(String name) {
        String str = name.toUpperCase();
        String dataType = null;
        if (str.startsWith("FIELD_")) {
            int ind = str.indexOf(".");
            if (ind == -1) {
                return getTableAlias(str.substring(6), "ANY") + ".DATA";
            } else {
                dataType = str.substring(6, ind);
            }
        }
        if (str.startsWith("CONTENT.")) {
            return str;
        } else if (str.startsWith("CATEGORY.")) {
            return "CAT." + str.substring(9);
        }
        if (structure == null) {
            if (dataType == null) {
                throw new SearchFormatException("When no structure is specified please enter a full field name e.g. field_varchar.name");
            }
            int dotIdx = str.indexOf(".");
            if (dotIdx == -1) {
                throw new SearchFormatException("When no structure is specified please enter a full field name e.g. field_varchar.name");
            }
            String itemName = name.substring(dotIdx + 1);
            return getTableAlias(dataType, itemName) + ".DATA";
        } else {
            if (str.startsWith("FIELD.")) {
                str = name.substring(6);
            } else {
                str = name;
            }
            return getTableAlias(structure, str) + ".DATA";
        }
    }

    /**
	 * Recursive method to parse a where expression and return a valid SQL
	 * string with all field names converted to correct database format.
	 */
    private String parseWhereExpression(ZExpression exp) {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        String op = exp.getOperator();
        String fieldName;
        boolean firstDone = false;
        boolean isDate = false;
        for (Iterator i = exp.getOperands().iterator(); i.hasNext(); ) {
            Object next = i.next();
            if (firstDone) {
                buf.append(" " + op + " ");
            }
            if (next instanceof ZExpression) {
                buf.append(parseWhereExpression((ZExpression) next));
            } else {
                if (!firstDone) {
                    fieldName = convertField(next.toString());
                    if (fieldName.startsWith(StructureItem.VARCHAR) || fieldName.startsWith(StructureItem.TEXT)) {
                        fieldName = dialect.getUpperCaseFunction(fieldName);
                    } else if (fieldName.startsWith(StructureItem.DATE)) {
                        isDate = true;
                    }
                    buf.append(fieldName);
                    firstDone = true;
                } else {
                    if (isDate) {
                        try {
                            String val = next.toString();
                            val = StringHelper.replace(val, "'", "", true);
                            Date d;
                            d = Content.DF.parse(val);
                            d = new Timestamp(d.getTime());
                            buf.append("'" + d.toString() + "'");
                        } catch (Exception e) {
                            throw new SearchFormatException("where clause date field must be in 'dd/MM/yyyy' format");
                        }
                    } else {
                        buf.append(next.toString().toUpperCase());
                    }
                }
            }
            firstDone = true;
        }
        buf.append(")");
        return buf.toString();
    }

    /**
	 * Parse a where clause to a query and convert all friendly field names into
	 * actual database fields as available in the query.
	 */
    protected String parseWhere(String where) {
        StringBuffer temp = new StringBuffer();
        if (where == null) {
            return null;
        }
        ZExpression whereExp = SearchSyntax.parseWhere(where);
        if (whereExp != null) {
            temp.append(parseWhereExpression(whereExp));
        }
        return temp.toString();
    }

    /**
	 * Build and return the SQL String that this object represents.
	 */
    public String buildSql() {
        StringBuffer query = new StringBuffer("SELECT DISTINCT ");
        String sep = "";
        for (Iterator i = selectFields.iterator(); i.hasNext(); ) {
            query.append(sep);
            query.append((String) i.next());
            sep = ", ";
        }
        query.append("\nFROM ");
        sep = "";
        for (Iterator i = tableAliases.iterator(); i.hasNext(); ) {
            query.append(sep);
            query.append((String) i.next());
            sep = ",\n";
        }
        for (Iterator i = subselects.keySet().iterator(); i.hasNext(); ) {
            query.append(sep);
            query.append((String) subselects.get(i.next()));
        }
        query.append("\nWHERE ");
        sep = "";
        for (Iterator i = joinExprs.iterator(); i.hasNext(); ) {
            query.append(sep);
            query.append((String) i.next());
            sep = "\nAND ";
        }
        if (whereClause != null) {
            query.append(sep);
            query.append("\n");
            query.append(whereClause);
        }
        query.append("\nORDER BY ");
        sep = "";
        for (Iterator i = orderByList.iterator(); i.hasNext(); ) {
            query.append(sep);
            query.append(i.next());
            sep = ", ";
        }
        String result = query.toString();
        return result;
    }

    /**
	 * Each StructureItem in the where or order by clauses of the query must be
	 * referenced as a table alias in the query. This method returns that alias
	 * as well as registering the required joins to include the alias in the
	 * query.
	 */
    protected String getTableAlias(Structure s, String name) {
        StructureItem si = s.getStructureItem(name);
        if (si == null) {
            throw new SearchFormatException("Field not found: " + s.getName() + "." + name);
        }
        Object itemKey = si.getPrimaryKey();
        String alias = null;
        if (itemAliases.get(itemKey) == null) {
            synchronized (itemAliases) {
                if (itemAliases.get(itemKey) == null) {
                    alias = si.getDataType() + "__" + si.getPrimaryKey();
                    itemAliases.put(itemKey, alias);
                }
            }
        }
        if (alias == null) {
            alias = (String) itemAliases.get(itemKey);
        }
        if (subselects.get(itemKey) == null) {
            String tableDef = getContentPrefix(false) + "DATA_" + si.getDataType();
            tableDef += " " + alias;
            subselects.put(itemKey, tableDef);
            joinExprs.add(alias + ".STRUCTURE_ITEM_ID = " + itemKey);
            joinExprs.add(alias + ".CONTENT_ID = CC.CONTENT_ID");
        }
        return alias;
    }

    protected String getTableAlias(String dataType, String name) {
        List structureItems = null;
        if (!name.equals("ANY")) {
            structureItems = StructureItem.getStructureItems(name, dataType);
            if (structureItems == null || structureItems.size() == 0) {
                throw new SearchFormatException("Field not found: " + dataType + "." + name);
            }
        }
        String key = dataType + "__" + name;
        String alias = (String) itemAliases.get(key);
        if (alias == null) {
            synchronized (itemAliases) {
                if (itemAliases.get(key) == null) {
                    alias = key;
                    itemAliases.put(key, alias);
                }
            }
        }
        if (subselects.get(key) == null) {
            String tableDef = getContentPrefix(false) + "DATA_" + dataType;
            tableDef += " " + alias;
            subselects.put(key, tableDef);
            if (structureItems != null) {
                String inStr = "(", sep = "";
                for (Iterator i = structureItems.iterator(); i.hasNext(); ) {
                    inStr += sep;
                    inStr += ((StructureItem) i.next()).getPrimaryKey();
                    sep = ",";
                }
                inStr += ")";
                joinExprs.add(alias + ".STRUCTURE_ITEM_ID IN " + inStr);
            }
            joinExprs.add(alias + ".CONTENT_ID = CC.CONTENT_ID");
        }
        return alias;
    }

    /**
	 * Get the current prefix for content, in short or long form. Short form
	 * will be 'LC_' or 'WC_', long form will be 'LIVE_' or 'WORKING_'.
	 */
    protected String getContentPrefix(boolean isLong) {
        if (Content.getContentType() == Content.WORKING) {
            return (isLong) ? "WORKING_" : "WC_";
        } else {
            return (isLong) ? "LIVE_" : "LC_";
        }
    }

    /**
	 * Get the name of the content database table.
	 */
    protected String getContentTableName() {
        return getContentPrefix(true) + "CONTENT";
    }

    /**
	 * Add a select field to the query. For example, calling
	 * addSelectField("foo") on a new query object will cause the following in
	 * the output of buildSql(): 'SELECT foo FROM...'
	 */
    protected void addSelectField(String selectField) {
        this.selectFields.add(selectField);
    }

    /**
	 * Add a table alias to the query. This can be in the form '<TABLE_NAME> AS
	 * <ALIAS>' or '<TABLE_NAME> <ALIAS>'. E.g. calling addTableAlias("foo bar")
	 * on a new query would cause the following in the output of buildSql():
	 *
	 * 'SELECT xxx FROM foo bar WHERE ....'
	 */
    protected void addTableAlias(String tableAlias) {
        this.tableAliases.add(tableAlias);
    }

    /**
	 * Add a join expression to the query. Typically a join expression string is
	 * of the form '<TABLE_FIELD_1> = <TABLE_FIELD_2>'. This forms an inner join
	 * between the tables. Only inner joins can be reliably handled by this
	 * class. E.g. calling
	 * addJoinExpression("live_content.content_id = cc.content_id") will cause
	 * the following output in buildSql():
	 *
	 * 'SELECT xxx FROM yyy WHERE live_content.content_id = cc.content_id...'
	 */
    protected void addJoinExpression(String joinExpr) {
        this.joinExprs.add(joinExpr);
    }

    /**
	 * Add an order by clause to the query. Can be of the form '<FIELD>[ (ASC|DESC)]'.
	 */
    protected void addOrderBy(String orderBy) {
        this.orderByList.add(orderBy);
    }

    /**
	 * Set the where criteria of the query. This will usually be one long where
	 * expression, contained in brackets, that is included as the last part of
	 * the query's where clause.
	 */
    protected void setWhereClause(String where) {
        this.whereClause = where;
    }

    protected abstract List getResults(ResultSet rs) throws SQLException;
}
