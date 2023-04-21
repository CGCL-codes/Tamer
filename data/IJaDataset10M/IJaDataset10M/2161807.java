package net.sourceforge.squirrel_sql.fw.datasetviewer;

/**
 * @author gwg
 *
 * This is the block of data needed to save/restore the import/export
 * file name and command previously selected by the user for a given
 * table column.
 * The full table+column name is stored in this data object because
 * it is needed when the application saves this info to a file
 * for re-loading the next time the app starts.
 */
public class CellImportExportInfo {

    /**
	 * The full name of the table and column for which these user inputs apply.
	 */
    private String _tableColumnName;

    /**
	 * The file name selected by the user.
	 */
    private String _fileName;

    /**
	 * The executable command entered by the user
	 */
    private String _command;

    /**
	 * Null Ctor - used only by XMLBeanReader when loading data from file on startup.
	 */
    public CellImportExportInfo() {
        this("", "", "");
    }

    /**
	 * Normal Constructor
	 */
    CellImportExportInfo(String tableColumnName, String fileName, String command) {
        _tableColumnName = tableColumnName;
        _fileName = fileName;
        _command = command;
    }

    public String getTableColumnName() {
        return _tableColumnName;
    }

    public void setTableColumnName(String tableColumnName) {
        _tableColumnName = tableColumnName;
    }

    public String getFileName() {
        return _fileName;
    }

    public void setFileName(String fileName) {
        _fileName = fileName;
    }

    public String getCommand() {
        return _command;
    }

    public void setCommand(String command) {
        _command = command;
    }
}
