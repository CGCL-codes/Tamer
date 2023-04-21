package org.tranche.annotation.fieldvalue;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.tranche.commons.DebugUtil;
import org.tranche.annotation.annotation.Annotation;
import org.tranche.annotation.database.DBUtil;
import org.tranche.mysql.MySQLUtil;

/**
 * <p>Utility methods that have to do with FieldValue objects.</p>
 * @author James "Augie" Hill - augman85@gmail.com
 * @author Shelly Chang - hlchang.shelly@gmail.com
 */
public class FieldValueUtil {

    /**
     * <p>This class cannot be instantiated.</p>
     */
    private FieldValueUtil() {
    }

    /**
     * <p>Creates a field value.</p>
     * @param annotation The annotation to which the field value will be assigned.
     * @param fieldID The primary key of the field the field value represents.
     * @param value The value for the field value.
     * @param category_instance The category instance number to which the field value is assigned.
     * @param parent_category_instance The category instance number of the parent category instance to which the field value is assigned.
     * @return The primary key of the new field value.
     * @throws java.lang.Exception
     */
    public static int create(Annotation annotation, int fieldID, String value, int category_instance, int parent_category_instance) throws Exception {
        DebugUtil.debugOut(FieldValueUtil.class, "Creating");
        if (annotation == null) {
            throw new NullPointerException("Annotation is null.");
        }
        validateValue(value);
        int fieldValueID = DBUtil.getAnnotations().executeInsert("INSERT INTO " + FieldValue.TABLE.getName() + " (" + FieldValue.COLUMN_ANNOTATION_ID.getName() + ", " + FieldValue.COLUMN_FIELD_ID.getName() + ", " + FieldValue.COLUMN_VALUE.getName() + ", " + FieldValue.COLUMN_CATEGORY_INSTANCE.getName() + ", " + FieldValue.COLUMN_PARENT_CATEGORY_INSTANCE.getName() + ") VALUES ('" + annotation.getPrimaryKey() + "', '" + fieldID + "', '" + MySQLUtil.escape(value) + "', '" + category_instance + "', '" + parent_category_instance + "');");
        if (fieldValueID == -1) {
            throw new Exception("Could not add field value to database.");
        }
        return fieldValueID;
    }

    /**
     * <p>Creates a field value that has the same attributes as the field value with the given primary key. Assigns the new field value to the annotation set with the given primary key.</p>
     * @param cloneFieldValue The field value to clone.
     * @param annotation The annotation to which the field value is assigned.
     * @return The primary key of the new field value.
     * @throws java.lang.Exception
     */
    public static int clone(FieldValue cloneFieldValue, Annotation annotation) throws Exception {
        if (cloneFieldValue == null) {
            throw new Exception("Field value to clone is null.");
        }
        if (annotation == null) {
            throw new Exception("Annotation is null.");
        }
        return create(annotation, cloneFieldValue.getFieldID(), cloneFieldValue.getValue(), cloneFieldValue.getCategoryInstance(), cloneFieldValue.getParentCategoryInstance());
    }

    /**
     * <p>Deletes the field value with the given primary key.</p>
     * @param fieldValueID The primary key of the field value to be deleted.
     * @throws java.lang.Exception
     */
    public static void delete(int fieldValueID) throws Exception {
        if (!DBUtil.getAnnotations().executeUpdate("DELETE FROM " + FieldValue.TABLE.getName() + " WHERE " + FieldValue.COLUMN_PRIMARY_KEY.getName() + " = '" + fieldValueID + "';")) {
            throw new Exception("Could not remove field value from the database.");
        }
        FieldValueCache.remove(fieldValueID);
    }

    /**
     * <p>Deletes the field values with the given primary keys.</p>
     * @param fieldValueIDs The primary keys of the field values to be deleted.
     * @throws java.lang.Exception
     */
    public static void delete(Collection<Integer> fieldValueIDs) throws Exception {
        if (fieldValueIDs == null || fieldValueIDs.isEmpty()) {
            return;
        }
        String SQL = "";
        for (int fieldValueID : fieldValueIDs) {
            SQL = SQL + FieldValue.COLUMN_PRIMARY_KEY.getName() + " = '" + fieldValueID + "' OR ";
        }
        SQL = SQL.substring(0, SQL.length() - 4);
        if (!DBUtil.getAnnotations().executeUpdate("DELETE FROM " + FieldValue.TABLE.getName() + " WHERE " + SQL + ";")) {
            throw new Exception("Could not remove field values from the database.");
        }
        FieldValueCache.remove(fieldValueIDs);
    }

    /**
     * <p>Deletes the given field values.</p>
     * @param fieldValues The field values to be deleted.
     * @throws java.lang.Exception
     */
    public static void deleteObjects(List<FieldValue> fieldValues) throws Exception {
        if (fieldValues == null || fieldValues.isEmpty()) {
            return;
        }
        String SQL = "";
        for (FieldValue fieldValue : fieldValues) {
            if (fieldValue == null) {
                continue;
            }
            SQL = SQL + FieldValue.COLUMN_PRIMARY_KEY.getName() + " = '" + fieldValue.getPrimaryKey() + "' OR ";
        }
        SQL = SQL.substring(0, SQL.length() - 4);
        if (!DBUtil.getAnnotations().executeUpdate("DELETE FROM " + FieldValue.TABLE.getName() + " WHERE " + SQL + ";")) {
            throw new Exception("Could not remove field values from the database.");
        }
        FieldValueCache.removeObjects(fieldValues);
    }

    /**
     * 
     * @param value
     * @throws Exception
     */
    public static void validateValue(String value) throws Exception {
        if (value == null) {
            throw new NullPointerException("Value is null.");
        }
        if (value.length() > FieldValue.COLUMN_VALUE.getSize()) {
            throw new Exception("Value is too long.");
        }
    }

    /**
     * <p>Get field values related to the specific field_id.</p>
     * @param fieldID The primary key of the field the field value represents.
     * @return LinkedLiat The field value.
     * @throws java.lang.Exception
     */
    public static LinkedList<FieldValue> getFieldValue(String AnnID, String[] fieldIDs) throws Exception {
        ResultSet rs = null;
        LinkedList lresult = new LinkedList();
        String cond = "";
        for (int i = 0; i < fieldIDs.length; i++) {
            cond += fieldIDs[i];
            if (i < (fieldIDs.length - 1)) {
                cond += ",";
            }
        }
        try {
            String stat = "SELECT * FROM " + FieldValue.TABLE.getName() + " WHERE " + FieldValue.COLUMN_ANNOTATION_ID.getName() + "=" + AnnID + " and " + FieldValue.COLUMN_FIELD_ID.getName() + " in (" + cond + ")" + " ORDER by " + FieldValue.COLUMN_ANNOTATION_ID.getName();
            rs = DBUtil.getAnnotations().executeQuery(stat);
            while (rs.next()) {
                FieldValue pData = new FieldValue(rs);
                lresult.add(pData);
            }
        } catch (Exception e) {
            DebugUtil.debugErr(FieldValueUtil.class, e);
        } finally {
            MySQLUtil.safeClose(rs);
        }
        return lresult;
    }

    /**
     * <p>update field values related to the specific annotation with new field_id.</p>
     * @param annotationID The primary key of the annotation.
     * @param FROMfieldID The primary key of the field the field value represents.
     * @param TOfieldID The primary key of the field the field value will represent.
     * @return boolean The update status.
     * @throws java.lang.Exception
     */
    public static boolean updateFieldValues(int annotationID, int FROMfieldID, int TOfieldID) throws Exception {
        String statement = "UPDATE " + FieldValue.TABLE.getName() + " SET " + FieldValue.COLUMN_FIELD_ID.getName() + " = " + TOfieldID + " WHERE " + FieldValue.COLUMN_ANNOTATION_ID.getName() + " = " + annotationID + " AND " + FieldValue.COLUMN_FIELD_ID.getName() + " = " + FROMfieldID;
        if (!DBUtil.getAnnotations().executeUpdate(statement)) {
            throw new Exception("Could not update field values from the database.");
        }
        FieldValueCache.clear();
        return true;
    }
}
