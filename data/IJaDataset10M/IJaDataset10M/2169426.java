package middlegen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import middlegen.predicates.column.Mandatory;
import middlegen.predicates.column.PrimaryKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Category;

/**
 * This class represents a table in a database.
 *
 * @author Aslak Helles�y
 * @created 3. oktober 2001
 * @todo-javadoc Write javadocs
 */
public class DbTable extends PreferenceAware implements Table {

    /** The Ant table element. */
    private TableElement _tableElement;

    /** All the relations connected to this table */
    private final Collection _relationsipRoles = new ArrayList();

    /** The schema name. */
    private final String _schemaName;

    /** The table comments */
    private final String _remarks;

    /** All the columns of this table */
    private final List _columns = new ArrayList();

    /** The Map used to map columns to sql names. */
    private final Map _columnSqlName2ColumnMap = new HashMap();

    /** The unique tuples of this table */
    private final Collection _uniqueTuples = new ArrayList();

    /** Static logger. */
    private static Category _log = Category.getInstance(DbTable.class.getName());

    /**
    * Construct a DbTable from an Ant table element with schema.
    *
    * @param tableElement The Ant table element.
    * @param schemaName The jdbc schema the table belongs to.
    * @param remarks Comments for the table.
    */
    public DbTable(TableElement tableElement, String schemaName, String remarks) {
        _tableElement = tableElement;
        _schemaName = schemaName;
        _remarks = remarks;
    }

    /**
    * Sets the Position attribute of the DbTable object.
    *
    * @param x The new Position value.
    * @param y The new Position value.
    */
    public void setPosition(int x, int y) {
        setPrefsValue("x", String.valueOf(x));
        setPrefsValue("y", String.valueOf(y));
    }

    /**
    * Get the preferences x value.
    *
    * @return x position stored in prefs, or -1 if no value exists.
    */
    public int getPrefsX() {
        return getPrefsPos("x");
    }

    /**
    * Get the preferences y value.
    *
    * @return x position stored in prefs, or -1 if no value exists
    */
    public int getPrefsY() {
        return getPrefsPos("y");
    }

    /**
    * The all columns matching the given predicate.
    *
    * @param predicate The predicate to use.
    * @return Collection of matched columns.
    */
    public Collection getColumns(Predicate predicate) {
        return CollectionUtils.select(getColumns(), predicate);
    }

    /**
    * Gets the PkTableSqlName attribute of the DbTable object.
    *
    * @return The PkTableSqlName value.
    */
    public TableElement getTableElement() {
        return _tableElement;
    }

    /**
    * Gets the SqlName attribute of the DbTable object.
    *
    * @param withSchemaPrefix true if schema prefix should be included in name.
    * @return The SqlName value.
    */
    public String getSqlName(boolean withSchemaPrefix) {
        return withSchemaPrefix ? getSchemaPrefixedSqlName() : getSqlName();
    }

    /**
    * Gets the Name attribute of the Table object.
    *
    * @return The Name value.
    */
    public String getSchemaPrefixedSqlName() {
        boolean noschema = _schemaName == null || _schemaName.trim().equals("");
        String result;
        if (noschema) {
            result = getSqlName();
        } else {
            result = _schemaName + "." + getSqlName();
        }
        return result;
    }

    /**
    * Gets the SqlName attribute of the DbTable object.
    *
    * @return The SqlName value.
    */
    public String getSqlName() {
        return getTableElement().getSQLName();
    }

    /**
    * @return The PhysicalName value.
    * @see TableElement#getPhysicalName()
    */
    public String getPhysicalName() {
        return getTableElement().getPhysicalName();
    }

    /**
    * @return The PhysicalOwner value.
    * @see TableElement#getPhysicalOwner()
    */
    public String getPhysicalOwner() {
        return getTableElement().getPhysicalOwner();
    }

    /**
    * Gets the Name attribute of the DbTable object.
    *
    * @return The Name value.
    */
    public String getName() {
        return getTableElement().getName();
    }

    /**
    * Gets the enabled relationship roles.
    *
    * @return The RelationCount value.
    */
    public Collection getRelationshipRoles() {
        return _relationsipRoles;
    }

    /**
    * Gets all the columns.
    *
    * @return a list of all the columns.
    */
    public Collection getColumns() {
        return Collections.unmodifiableCollection(_columns);
    }

    /**
    * Gets all the mandatory columns (columns that are not nullable).
    *
    * @return a list of all the mandatory columns.
    */
    public final Collection getMandatoryColumns() {
        return getColumns(Mandatory.getInstance());
    }

    /**
    * Gets the PrimaryKeyColumns attribute of the DbTable object.
    *
    * @return The PrimaryKeyColumns value.
    */
    public Collection getPrimaryKeyColumns() {
        return getColumns(PrimaryKey.getInstance());
    }

    /**
    * Gets the RelationshipRoles attribute of the DbTable object.
    *
    * @param predicate The predicate to use when matching relationship roles.
    * @return The RelationshipRoles collection.
    */
    public Collection getRelationshipRoles(Predicate predicate) {
        return CollectionUtils.select(getRelationshipRoles(), predicate);
    }

    /**
    * Returns the column that is a pk column. If zero or 2+ columns are pk columns, null is returned.
    *
    * @return The PkColumn value.
    */
    public Column getPkColumn() {
        Column pkColumn = null;
        Iterator i = _columns.iterator();
        while (i.hasNext()) {
            Column column = (Column) i.next();
            if (column.isPk()) {
                if (pkColumn != null) {
                    pkColumn = null;
                    break;
                }
                pkColumn = column;
            }
        }
        _log.debug("Table " + getSqlName() + "'s unique pk column:" + pkColumn);
        return pkColumn;
    }

    /**
    * Gets the Index attribute of the DbTable object.
    *
    * @param columnSqlName The sql name of the column.
    * @return The Index value
    */
    public int getIndex(String columnSqlName) {
        return _columns.indexOf(getColumn(columnSqlName));
    }

    /**
    * Gets the Unique tuples for this Table object.
    *
    * @return a Collection of Collections of Colunns. Each entry in the returned collection represents one or more
    *      columns which make up a unique key for the table. This can be used to generate more intellegent finder
    *      methods.
    */
    public Collection getUniqueTuples() {
        return _uniqueTuples;
    }

    /**
    * Gets the column with the specified name.
    *
    * @todo we should really throw an ex instead of logging an error. has to do with m:n relationships
    * @param sqlName the name of the column in the database.
    * @return the column with the specified name.
    */
    public Column getColumn(String sqlName) {
        Column result = (Column) _columnSqlName2ColumnMap.get(sqlName.toLowerCase());
        if (result == null) {
            throw new IllegalArgumentException("There is no column named " + sqlName + " in the table named " + getSqlName());
        }
        return result;
    }

    /**
    * Return comments about this table that are stored in the database.
    *
    * @return Comments about this table if they exist in the database.
    */
    public String getRemarks() {
        return _remarks;
    }

    /**
    * Add a relationship role to this table.
    *
    * @param relationshipRole The relationship role to add.
    */
    public void addRelationshipRole(RelationshipRole relationshipRole) {
        _relationsipRoles.add(relationshipRole);
    }

    /**
    * Adds a feature to the Column attribute of the Table object.
    *
    * @param column The feature to be added to the Column attribute.
    */
    public void addColumn(Column column) {
        _columns.add(column);
        _columnSqlName2ColumnMap.put(column.getSqlName().toLowerCase(), column);
    }

    /**
    * Adds a unique tuple to this table.
    *
    * @param uniqueTuple The unique tuple to be added to the table.
    */
    public void addUniqueTuple(Collection uniqueTuple) {
        _uniqueTuples.add(uniqueTuple);
    }

    /**
    * Sorts the columns in the table.
    *
    * @param comparator The comparator to use for the sorting.
    */
    public void sortColumns(Comparator comparator) {
        Collections.sort(_columns, comparator);
    }

    /**
    * Compare the table to another table.
    *
    * @param o The other table.
    * @return true if they're equal.
    */
    public boolean equals(Object o) {
        if (o instanceof Table) {
            Table other = (Table) o;
            return getSqlName().equals(other.getSqlName());
        } else {
            return false;
        }
    }

    /**
    * Generate a hash code for the table.
    *
    * @return The hash code.
    */
    public int hashCode() {
        return getSqlName().hashCode();
    }

    /**
    * Get preferences prefix for the table.
    *
    * @return The table preferences prefix.
    */
    protected final String prefsPrefix() {
        return "tables/" + getSqlName();
    }

    /**
    * Gets the PrefsPos attribute of the DbTable object.
    *
    * @param coord The coordinate value for the preference.
    * @return The PrefsPos value.
    */
    private int getPrefsPos(String coord) {
        String c = getPrefsValue(coord);
        if (c == null) {
            return Integer.MIN_VALUE;
        } else {
            return Integer.parseInt(c);
        }
    }
}
