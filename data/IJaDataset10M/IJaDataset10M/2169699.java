package architecture.ee.jdbc.recordset;

import architecture.common.util.ImplFactory;

/**
 * @author  donghyuck
 */
public class RecordsetFactory {

    public static interface Implementation {

        public abstract Recordset createRecordset(java.sql.ResultSet rs) throws Throwable;

        public abstract Recordset createRecordset();
    }

    /**
	 */
    private static Implementation impl = null;

    static {
        impl = (Implementation) ImplFactory.loadImplFromKey(RecordsetFactory.Implementation.class);
    }

    public static Recordset createRecordset(java.sql.ResultSet rs) throws Throwable {
        return impl.createRecordset(rs);
    }

    public static Recordset createRecordset() {
        return impl.createRecordset();
    }
}
