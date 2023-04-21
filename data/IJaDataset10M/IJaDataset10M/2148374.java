package druid.interfaces;

import java.sql.SQLException;
import java.util.List;
import ddf.type.QueryField;
import druid.core.jdbc.JdbcConnection;

public interface RecordIOModule extends IOModule {

    /** Performs the import
	  */
    public void doImport(JdbcConnection conn, String table, String fileName, ImportListener l) throws Exception;

    /** Performs the export
	  */
    public void doExport(JdbcConnection conn, String query, String fileName, ExportListener l) throws Exception;

    public static interface ImportListener {

        public void insertingRow(List<QueryField> fields, List<Object> row, long recordNum);

        public void updatingRow(List<QueryField> fields, List<Object> row, long recordNum);

        public void deletingRow(List<QueryField> fields, List<Object> row, long recordNum);

        public enum ActionType {

            RETRY, SKIP, SKIP_ALL, ABORT
        }

        ;

        public ActionType onInsertError(SQLException e, long recordNum, String line);

        public ActionType onUpdateError(SQLException e, long recordNum, String line);

        public ActionType onDeleteError(SQLException e, long recordNum, String line);
    }

    public static interface ExportListener {

        public void exportedRow(List<Object> row, long recordNum);
    }
}
