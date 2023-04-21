package com.gorillalogic.dal;

import com.gorillalogic.dal.model.*;
import com.gorillalogic.dal.common.CommonType;
import com.gorillalogic.dal.common.CommonTypeFactory;

/**
 * <code>Type</code> defines a structured data representation,
 * including:
 *
 *   <li>Number and name/type of columns
 *   <li>Key structure for uniquely identifying <code>Table.Row</code>s
 *   <li>Meta attributes for the type
 *   <li>Access to the type's <code>TypeBuilder</code>
 *
 * In contrast to <code>Table</code>, <code>Type</code>s do not have rows.
 * <code>Table</code>s have rows, and <code>Table</code> is a subclass of
 * <code>Type</code> thus each <code>Table</code> encapsulates its
 * <code>Type</code>.
 *
 * Individual rows of any conforming <code>Table</code> are uniquely 
 * identified by one or more keys. Keys may be user or system 
 * defined. The two kinds of system ids are Object IDentifiers 
 * (OIDs) and Row IDentifiers (RIDS, or rowIds). The various 
 * forms of keys serve different purposes:
 *
 *   Rows usually, and rows from user-defined rows always, have an 
 *   <i>oid</i>, which never changes throughout the lifetime of that 
 *   row. An oid may or may not have a value drawn from the target 
 *   domain (i.e. if not it is machine-generated value that may be
 *   hard to read). An oid is the persistent, cross-process space
 *   identifier for a row.
 *
 *   Every row is also guaranteed to have exactly one numeric
 *   <i>rowId</i> that is immutable within a process space but may
 *   vary across process spaces. RowIds directly identify rows (like 
 *   pointers) and are thus faster than oids. Note that even though
 *   a rowId is always available from Table.Row.getRowId(), there
 *   may or may not be an explicit column that includes the rowId
 *   in the type.
 *
 *   Rows may also have one or more user-defined keys, each of
 *   which can be composite (i.e. be comprised of more than one
 *   column). One among the set of user-defined keys is the
 *   <i>primary key</i>. A primary key is either explicitly flagged
 *   as such in its model, else defaults to the leftmost user-defined
 *   key.
 *
 * @author bpm
 * @version 1.0
 */
public interface Type {

    /**
	 * Every type has a simple name, which is unique in its owning scope.
	 * The name is usually used in addition as part of the path().
	 *
	 * @return a <code>String</code> value
	 */
    String getName();

    /**
	 * Use <code>PathStrategy.defaultStrategy</code> to invoke path(strategy).
	 *
	 * @return the GCL path description
	 */
    String path();

    /**
	 * Provide a GCL descriptor of this type. The detail, readability,
	 * and scope uniqueness of that descriptor are in accordance with
	 * the supplied strategy. More detailed paths include more container
	 * detail, more human readable forms.
	 *
	 * @param strategy the <code>PathStrategy</code> prescribing the format
	 * of the returned result
	 * @return the GCL path description
	 */
    String path(PathStrategy strategy);

    /**
	 * Format various forms of path() on this table as rows in
	 * the return result.
	 *
	 * @return a <code>Table</code> value
	 */
    Table pathView();

    /**
	 * Every Type exists in a package. That package usually, but not 
	 * necessarily, will in turn claim to own that type. Some temporary
	 * types will claim to be in a package (usually the root package),
	 * but that package will not claim to own them. Thus this routine
	 * always returns a valid package but this does not necessarily imply
	 * ownership on the part of that package. 
	 *
	 * Assert: pkg.getPkg() == pkg <--> pkg is the single system root package.
	 *
	 * @return the <code>PkgTable</code> enclosing this type definition.
	 */
    PkgTable getPkg();

    /**
	 * Generate a zero-row table of this type.
	 *
	 * @return a <code>Table</code> value
	 * @param extendable iff true, the result is a reference table which
	 * can be extended with addRef()
	 */
    Table makeEmptyTable(boolean extendable);

    /**
	 * Return the number of columns for this table.
	 *
	 * @return the column total
	 */
    int columnCount();

    /**
	 * Lookup a column on this table by name and return its position. 
	 *
	 * @param columnName a <code>String</code> column name
	 * @return it's index, else -1 if no match
	 */
    int columnIndex(String columnName);

    /**
	 * Lookup a column on this table by name and return its position. 
	 *
	 * @param columnName a <code>String</code> column name
	 * @param toss boolean controls not-found behavior: if true, throws an 
	 * exception else return -1
	 * @return it's index, else -1 if no match
	 * @exception BoundsException if column not found
	 */
    int columnIndex(String columnName, boolean toss) throws BoundsException;

    /**
	 * Each types contains one or more columns, indexed starting at zero.
	 *
	 * @param column a column index
	 * @return the matching <code>Expr</code>, else null if no match
	 * @exception AccessException if column not found
	 */
    ColumnHdr column(String columnName);

    ColumnHdr column(String columnName, boolean toss) throws BoundsException;

    ColumnHdr column(int column);

    ColumnHdr column(int column, boolean toss) throws BoundsException;

    /**
	 * Types may or may not expose a <code>TypeBuilder</code> for 
	 * modify the type structure.
	 *
	 * @param toss iff true, throws an exception rather than return null
	 * @return a <code>TypeBuilder</code> for changing this table structure
	 */
    TypeBuilder builder();

    TypeBuilder builder(boolean toss) throws OperationException;

    /**
	 * Return the extent table, i.e. all rows of this type (the
	 * equivalent of a 'base table' in relational terms). If a
	 * table is not an extent, it is a reference table which holds
	 * references to other tables (which in turn may be extents or
	 * reference tables).
	 *
	 * To test if a given table is an extent, and not a reference 
	 * table:
	 *
	 *   table.extent() == table
	 *
	 * @return the extent <code>Table</code> for this type, which
	 * may be this same table (if this is already an extent table)
	 */
    Table getExtent();

    /**
	 * Return the row from which this table was defined, or null
	 * if this is a temp table. This row exists in the model world
	 * for this world.
	 *
	 * @return the <code>dal.model.Entity.Row</code> type specification
	 * -- non null for types, null for packages (see {@PkgTable.metaPkgRow})
	 */
    Entity.Row metaRow();

    /**
	 * Some Types have a finite set of values from which their
	 * values may be drawn.
	 *
	 * @return a <code>Table</code> of valid values for this type,
	 * else null if this type cannot be described this way
	 */
    Table getRange();

    /**
	 * Provide a table representation of this table's type
	 *
	 * @return a non-updatable <code>Table</code> with a row for
	 * each column in this table.
	 */
    Table invertTypeAsTable();

    /**
	 * Determine inheritance relationships.
	 *
	 * @param type any <code>Type</code>
	 * @return true iff this == type, or this is a supertype of type
	 */
    boolean isTypeOrSuperTypeOf(Type type);

    /**
	 * Find the common supertype between this and the given type,
	 * which is most derived among all such possibilities.
	 *
	 * @param type any <code>Type</code> 
	 * @return the first common <code>Type</code> supertype, or
	 * null if there is not common supertype
	 * @exception if the types cannot possibly be related, e.g. if
	 * they are in different worlds
	 */
    Type firstCommonSuperType(Type type) throws StructureException;

    public interface TypeItr {

        boolean next();

        Type type();
    }

    TypeItr getAncestry(boolean includeSelf);

    TypeItr getRoots();

    /**
	 * A tabular type references <code>Table</code>s. A 
	 * homomorphic  type references elements from the same 
	 * extent. The possible values of these booleans in
	 * relation to available types is as follows:
	 *
	 *  |      Type      |  Tabular  |  Homomorphic  |
	 *  |----------------|-----------|---------------|
	 *  | Any base type  |   false   |     true      |
	 *  | Type.TABLE     |   true    |     true      |
	 *  | Type.ANY       |   maybe   |     false     |
	 *
	 * Type.ANY can hold any kind of Java object, including
	 * <code>Table</code>s, but these are not necessaritly drawn
	 * from the same extent as is the case with Type.TABLE.
	 *
	 * @return a <code>boolean</code> value
	 */
    boolean tabular();

    boolean homomorphic();

    /**
	 * A value type has values that constitute its primary key,
	 * and no other values. Two value types are considered the
	 * same object if their keys match. 
	 *
	 * An access operation on a single-column value type is
	 * the same as applying that operation on that single column.
	 * For example, given a Table.Row ri of integer value type 
	 * then the following is true:
	 *
	 *   ri.equals(ri.getInt(0))
	 *
	 * @return true iff this type is a value type
	 */
    boolean isValueType();

    /**
	 * Determine if the type is numeric, i.e. int, long, float, or double.
	 *
	 * @return true iff type is numeric
	 */
    boolean isNumeric();

    /**
	 * Every Type exists with a domain, which defines a scope
	 * for its rowIds.
	 *
	 * @return the <code>IdDomain</code> for this type
	 */
    IdDomain getDomain();

    /**
	 * Convenience routine returns the <code>Model</code> associated
	 * with a type.
	 *
	 * @return the <code>Model</code> spawned from this types's 
	 * <code>PkgTable</code>, else null if that pkg does not spawn a universe
	 */
    Model model();

    /**
	 * Return the number of keys for this table.
	 *
	 * @return the number of keys for which key() will be valid
	 */
    int numKeys();

    /**
	 * Return a combination of one or more row indices that
	 * are candidate keys, i.e. guaranteed to produce one row
	 * when their values are provided. 
	 *
	 * @param keyIndex an index between 0 and numKeys()
	 * @return an <code>int[]</code> array of column indices
	 */
    int[] keyIndex(int keyIndex) throws BoundsException;

    /** 
	 * Standard prefix for all columns generated by GXE, i.e. that
	 * are not otherwise named within a user model.
	 */
    public static final String GLPREFIX = "_";

    /** 
	 * Names for standard internal key columns.
	 */
    public static final String OIDNAME = GLPREFIX + "oid";

    public static final String RIDNAME = GLPREFIX + "rid";

    /**
	 * Return the index of the rowId column. Note that getRowId() on
	 * a <code>Table.Row</code> will always return a valid rowId even
	 * if this routine returns -1, since some types do not explicitly
	 * includer rowIds in their column structure.
	 *
	 * @exception StructureException if no rowId column and toss=true
	 * @return The index of the rowId column if there is one, else -1
	 */
    int getRowIdColumnIndex();

    int getRowIdColumnIndex(boolean toss) throws StructureException;

    /**
	 * Return the index of the rowId key, which may be used as argument
	 * to key(keyIndex) or other routines that take a keyIndex argument.
	 *
	 * @exception StructureException if no rowId column and toss=true
	 * @return The index of the rowId key if there is one, else k-1
	 */
    int getRowIdKeyIndex();

    int getRowIdKeyIndex(boolean toss) throws StructureException;

    /**
	 * Return the index of the oid column. 
	 *
	 * Note that <code>Table.Row.getOid()</code> may still substitute an 
	 * available frozen user-defined key value for an oid even if this 
	 * routine returns -1.
	 *
	 * @exception StructureException if no oid column and toss=true
	 * @return The index of the oid column if there is one, else -1
	 */
    int getOidColumnIndex();

    int getOidColumnIndex(boolean toss) throws StructureException;

    /**
	 * Return the index of the oid key, which may be used as argument
	 * to key(keyIndex) or other routines that take a keyIndex argument.
	 *
	 * If <code>Table.Row.getOid()</code> returns a non-null value,
	 * this routine will return a value >= 0.
	 *
	 * @exception StructureException if no rowId column and toss=true
	 * @return The index of the oid key if there is one, else -1
	 */
    int getOidKeyIndex();

    int getOidKeyIndex(boolean toss) throws StructureException;

    /**
	 * Return the index of the primary key column.
	 *
	 * @exception StructureException if no primary key or there is a
	 * composite primary key, and toss=true
	 * @return The index of the single primary-key column if there is 
	 * one, else -1
	 */
    int getPrimaryColumnIndex();

    int getPrimaryColumnIndex(boolean toss) throws StructureException;

    /**
	 * Return the index of the primary key, which may be used as argument
	 * to key(keyIndex) or other routines that take a keyIndex argument.
	 *
	 * @exception StructureException if no primary key and toss=true
	 * @return The index of the primary key if there is one, else -1 
	 */
    int getPrimaryKeyIndex();

    int getPrimaryKeyIndex(boolean toss) throws StructureException;

    /**
	 * Return a named method.
	 *
	 * @param name the method name to search for
	 * @return an executable <code>ParameterizedExpr</code> or
	 * null if none can be found that matches the supplied name
	 *
	 * TBD the one-arg form throwing an AccessException is bogus & there
	 * only for message.proxy.DataWorldObjectImpl
	 */
    ParameterizedExpr method(String name) throws AccessException;

    ParameterizedExpr method(String name, boolean toss) throws AccessException;

    /**
	 * Compute an expression against this type.
	 *
	 * @param gcl expression
	 * @return an <code>Expr</code> that can be applied to tables of this type
	 */
    Expr precompile(String gcl) throws AccessException;

    public static final Type INT = CommonType.XINT;

    public static final Type LONG = CommonType.XLONG;

    public static final Type FLOAT = CommonType.XFLOAT;

    public static final Type DOUBLE = CommonType.XDOUBLE;

    public static final Type BOOLEAN = CommonType.XBOOLEAN;

    public static final Type STRING = CommonType.XSTRING;

    public static final Type ANY = CommonType.XANY;

    public static final Type VOID = CommonType.XVOID;

    public static final Type CURRENCY = CommonType.XCURRENCY;

    public static final Type DATE = CommonType.XDATE;

    public static final Type TIME = CommonType.XTIME;

    public static final Type DATETIME = CommonType.XDATETIME;

    public static final Type GSL = CommonType.XGSL;

    public static final Type GOSH = CommonType.XGOSH;

    public static final Factory factory = new CommonTypeFactory.Remap();

    public interface Factory {

        /**
		 * Return a built-in or covered type for the 
		 * supplied name.
		 *
		 * @param typeName name of a type
		 * @return a matching <code>Type</code> else null
		 */
        Type map(String typeName);
    }

    public int apply(DispatchReturningInt dispatch) throws AccessException;

    public interface DispatchReturningInt {

        int applyInt(Type type) throws AccessException;

        int applyLong(Type type) throws AccessException;

        int applyFloat(Type type) throws AccessException;

        int applyDouble(Type type) throws AccessException;

        int applyBoolean(Type type) throws AccessException;

        int applyString(Type type) throws AccessException;

        int applyAny(Type type) throws AccessException;

        int applyTable(Type type) throws AccessException;
    }

    CommonType asCommonType();
}
