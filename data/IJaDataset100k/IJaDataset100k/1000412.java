package com.jcorporate.expresso.core.dataobjects;

/**
 * Nestable indicates that the derived class includes nested data objects.
 * <p>While
 * the dataobject 'cluster' should be able to be acted on as a single unit,
 * once in a while, it is necessary to get at the internal or &quot;nested&quot;
 * data objects.  </p>
 * <p>An example of the Nestable usage is in
 * <code>com.jcorporate.expresso.services.controller.dbmaint.ViewBlob</code>
 * in conjunction with a
 * <code>com.jcorporate.expresso.cores.dataobjects.jdbc.JoinedDataObject</code>.
 * The ViewBlob routine needs to get to the low-level JDBC data objects rather
 * than operate on the join as a whole.  Since JoinedDataObject implements the
 * <code>Nestable</code> interface.  It is then possible to work with the underlying
 * JDBCDataObjects in the join.
 * </p>
 * @author Michael Rimov
 * @version $Revision: 3 $ on  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 */
public interface NestableDataObject {

    /**
     * Retrieves a nested dataobject based upon the public field name that
     * the DataObject publishes.  For example, in JoinedDataObjects, the field
     * name &quot;"abcd.efgh" represents DataObject 'abcd', and field name
     * 'efgh'.
     * <p>The actual usage of the naming convention will differ from the, thus
     * you'll need to use getMetaData().getFieldList() to get the names of the
     * fields of a <code>Nestable</code> object to get a valid field Name</p>
     * @param fieldName the full field name  to get the nested data object.
     * @return DataObject or possibly null.
     * @throws IllegalArgumentException if the field name does not map to any
     * DataObject
     */
    public DataObject getNestedFromFieldName(String fieldName);

    /**
     * Often times, field names for the external interface will be different
     * from the of a nested field name in a data object.  Use this function
     * to get the internal field name for the DataObject returned by
     * <code>getNestedFromFieldName()</code>
     * @param fieldName The external name of the field
     * @return java.lang.String, the field corresponding to the field name
     * of the data object
     * @throws IllegalArgumentException if the given field name cannot map
     * to a field.
     */
    public String getFieldFromNestedName(String fieldName);

    /**
     * Retrieve an array of all nested data objects.  May be empty if there
     * are no nested data objects.  Ordering is undefined by this function.
     * @return Array of DataObjects.  Should never return null.
     */
    public DataObject[] getAllNested();
}
