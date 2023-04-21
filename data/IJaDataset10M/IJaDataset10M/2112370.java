package medi.db.constraint;

import javatools.db.DbAbstractTable;
import javatools.db.DbException;
import javatools.db.DbFixedAbstractTable;
import medi.db.table.Data;
import medi.db.table.NeedsExecutionOf;

/**
 * Represents the constraint for FILE_TYPE table.
 * @author Antonio Petrelli
 * @version 0.3.0
 */
public class FKFileType extends javatools.db.DbConstraint {

    /** Creates new FKFileType.
     * @param tbl The FileType table to use.
     */
    public FKFileType(DbAbstractTable tbl) {
        super(tbl);
        fatherTables = null;
        sonTables = new DbFixedAbstractTable[2];
        sonIndexes = new Integer[2];
        updateOperations = null;
        deleteOperations = null;
    }

    /** Sets the Data table.
     * @param tbl The Data table to use.
     * @param index How does this "Data" table reference this table?
     */
    public void setDataTable(Data tbl, int index) {
        sonTables[0] = tbl;
        sonIndexes[0] = new Integer(index);
    }

    /** Sets the NeedsExecutionOf table.
     * @param tbl The NeedsExecutionOf table to use.
     * @param index How does this "NeedsExecutionOf" table reference this table?
     */
    public void setNeedsExecutionOfTable(NeedsExecutionOf tbl, int index) {
        sonTables[1] = tbl;
        sonIndexes[1] = new Integer(index);
    }

    /** Checks if the specified operation can be made.
     * @param operation The operation to do.
     * @throws DbException If something goes wrong.
     */
    public void check(int operation) throws DbException {
        checkThis(operation);
    }

    /** Checks (if automatic checking is set) and updates, performing the operation.
     * @param operation The operation to do.
     * @throws DbException If something goes wrong.
     * @return The result from the <CODE>execute()</CODE> method.
     */
    public int update(int operation) throws DbException {
        return updateThis(operation);
    }
}
