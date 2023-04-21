package medi.db.table;

import javatools.db.DbConstraint;
import javatools.db.DbDatabase;
import javatools.db.DbException;
import medi.db.constraint.FKEditorTelephoneNo;

/**
 * Represents the EDITOR_TELEPHONE_NO table (see relational model).
 * @author Antonio Petrelli
 * @version 0.2.0
 */
public class EditorTelephoneNo extends javatools.db.DbFixedAbstractTable {

    /** Creates new EditorTelephoneNo.
     * @param db The database to use.
     * @throws DbException If something goes wrong.
     */
    public EditorTelephoneNo(DbDatabase db) throws DbException {
        super(db);
        tableName = "EDITOR_TELEPHONE_NO";
        try {
            loadStructure();
        } catch (DbException e) {
            buildUp();
            catchDefaults();
            saveStructure();
        }
        fk = new FKEditorTelephoneNo(this);
    }

    /** Returns the constraint (of type FKEditorTelephoneNo).
     * @return The constraint.
     */
    public DbConstraint getConstraint() {
        return fk;
    }

    private FKEditorTelephoneNo fk;
}
