package oracle.toplink.essentials.internal.expressions;

import java.io.*;
import java.util.Vector;
import java.util.Collection;
import oracle.toplink.essentials.internal.sessions.AbstractSession;

/**
 * @author Andrei Ilitchev
 * @since TOPLink/Java 1.0
 */
public class SQLUpdateAllStatementForTempTable extends SQLModifyAllStatementForTempTable {

    protected Collection assignedFields;

    public void setAssignedFields(Collection assignedFields) {
        this.assignedFields = assignedFields;
    }

    public Collection getAssignedFields() {
        return assignedFields;
    }

    protected Collection getUsedFields() {
        Vector usedFields = new Vector(getPrimaryKeyFields());
        usedFields.addAll(getAssignedFields());
        return usedFields;
    }

    protected void writeUpdateOriginalTable(AbstractSession session, Writer writer) throws IOException {
        session.getPlatform().writeUpdateOriginalFromTempTableSql(writer, getTable(), new Vector(getPrimaryKeyFields()), new Vector(getAssignedFields()));
    }
}
