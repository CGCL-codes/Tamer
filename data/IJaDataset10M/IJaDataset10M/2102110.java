package oracle.toplink.essentials.internal.databaseaccess;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import oracle.toplink.essentials.internal.sessions.AbstractSession;

/**
 * INTERNAL:
 * <p>
 * <b>Purpose</b>: To provide a base type for customary parameters' types
 * used for binding by DatabaseCall:
 * descendants of DatabasePlatform may create instances of descendants of this class
 * in customModifyInDatabaseCall method.
 * <p>
 * <b>Responsibilities</b>:
 * <ul>
 * </ul>
 */
public class BindCallCustomParameter {

    /**
    * INTERNAL:
    * Binds the custom parameter (obj) into  the passed PreparedStatement
    * for the passed DatabaseCall.
    *
    * Called only by DatabasePlatform.setParameterValueInDatabaseCall method
    */
    public BindCallCustomParameter(Object obj) {
        this.obj = obj;
    }

    protected BindCallCustomParameter() {
    }

    public void set(DatabasePlatform platform, PreparedStatement statement, int index, AbstractSession session) throws SQLException {
        platform.setParameterValueInDatabaseCall(obj, statement, index, session);
    }

    public String toString() {
        if (obj != null) {
            return obj.toString();
        } else {
            return "null";
        }
    }

    protected Object obj;
}
