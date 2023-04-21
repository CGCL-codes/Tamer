package ch.oblivion.comixviewer.ui.avd.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.MessageFormat;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ch.oblivion.comixviewer.ui.avd.ComixException;
import ch.oblivion.comixviewer.ui.avd.utils.IOUtils;

class ComixViewerDatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = ComixViewerDatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "ComixViewer";

    private static final int DATABASE_VERSION = 12;

    private boolean useSampleData;

    public ComixViewerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDatabase) {
        executeScript(sqlDatabase, DatabaseConstants.getScript(DatabaseConstants.CREATE_TABLES_FILE));
        if (useSampleData) {
            addSampleData(sqlDatabase);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, MessageFormat.format("Upgrading database from version {0} to {1}, which will remove all old data !", oldVersion, newVersion));
        executeScript(sqlDatabase, DatabaseConstants.getScript(DatabaseConstants.DROP_TABLES_FILE));
        onCreate(sqlDatabase);
    }

    public void addSampleData(SQLiteDatabase sqlDatabase) {
        executeScript(sqlDatabase, DatabaseConstants.getScript(DatabaseConstants.SAMPLE_DATA_FILE));
    }

    private void executeScript(SQLiteDatabase sqlDatabase, InputStream input) {
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(input, writer);
        } catch (IOException e) {
            throw new ComixException("Could not read the database script", e);
        }
        String multipleSql = writer.toString();
        String[] split = multipleSql.split("-- SCRIPT_SPLIT --");
        for (String sql : split) {
            if (!sql.trim().equals("")) {
                sqlDatabase.execSQL(sql);
            }
        }
    }

    public boolean isUseSampleData() {
        return useSampleData;
    }

    public void setUseSampleData(boolean useSampleData) {
        this.useSampleData = useSampleData;
    }
}
