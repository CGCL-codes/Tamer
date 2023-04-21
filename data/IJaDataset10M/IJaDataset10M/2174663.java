package info.decamps.m2settings.model;

/**
 *
 * @author regis
 */
public abstract class AbstractRepositoryTableModel extends ExtendableTableModel {

    public static final int COLUMN_ID = 0;

    public static final int COLUMN_NAME = 1;

    public static final int COLUMN_URL = 2;

    public static final int COLUMN_LAYOUT = 3;

    /** Creates a new instance of AbsrtactRepositoryTableModel */
    public void initColumnTitles() {
        columnTitles = new String[4];
        columnTitles[COLUMN_ID] = "id";
        columnTitles[COLUMN_NAME] = "name";
        columnTitles[COLUMN_URL] = "url";
        columnTitles[COLUMN_LAYOUT] = "layout";
    }
}
