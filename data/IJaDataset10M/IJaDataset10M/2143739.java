package com.codemobiles.droidslator.dicts;

import java.util.ArrayList;
import java.util.List;
import com.codemobiles.droidslator.ETLexBean;
import com.codemobiles.droidslator.TELexBean;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DBAdapter {

    private final String ETLEX_ID = "_id";

    private final String ETLEX_TENTRY = "tentry";

    private final String ETLEX_ETHAI = "ethai";

    private final String ETLEX_ESYN = "esyn";

    private final String ETLEX_ECAT = "ecat";

    private final String ETLEX_EANT = "eant";

    public final String TELEX_ID = "_id";

    public final String TELEX_EENTRY = "eentry";

    public final String TELEX_TCAT = "tcat";

    public final String TELEX_TSYN = "tsyn";

    public final String TELEX_TSAMPLE = "tsample";

    public final String TELEX_TANT = "tant";

    private final String TAG = "DBAdapter";

    private final String DATABASE_FILE = "/sdcard/.dsdb";

    private final String ET_TABLES = "etlex_";

    private final String TEV1_TABLES = "telexV1";

    private final String TEV2_TABLES = "telexV2";

    private SQLiteDatabase db;

    public DBAdapter() {
    }

    /**
   * @author preetam.palwe
   * 
   */
    public static class DBUtil {

        public static void safeCloseCursor(Cursor cursor) {
            if (cursor != null) {
                cursor.close();
            }
        }

        public static void safeCloseDataBase(SQLiteDatabase database) {
            if (database != null) {
                database.close();
            }
        }
    }

    public DBAdapter open() throws SQLException {
        try {
            db = SQLiteDatabase.openDatabase(DATABASE_FILE, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException ex) {
            Log.e(TAG, "error -- " + ex.getMessage(), ex);
        }
        return this;
    }

    public void close() {
        DBUtil.safeCloseDataBase(db);
    }

    public List<TELexBean> searchTE(final String word, String tableName) {
        List<TELexBean> result = new ArrayList<TELexBean>();
        open();
        try {
            Cursor mCursor = db.query(true, tableName, new String[] { TELEX_ID, TELEX_EENTRY, TELEX_TCAT, TELEX_TSYN, TELEX_TSAMPLE, TELEX_TANT }, TELEX_ID + " like " + word, null, null, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
                do {
                    TELexBean bn = new TELexBean();
                    bn.setTsearch(mCursor.getString(mCursor.getColumnIndex(TELEX_ID)));
                    bn.setEentry(mCursor.getString(mCursor.getColumnIndex(TELEX_EENTRY)));
                    bn.setTcat(mCursor.getString(mCursor.getColumnIndex(TELEX_TCAT)));
                    bn.setTsyn(mCursor.getString(mCursor.getColumnIndex(TELEX_TSYN)));
                    bn.setTsample(mCursor.getString(mCursor.getColumnIndex(TELEX_TSAMPLE)));
                    bn.setTant(mCursor.getString(mCursor.getColumnIndex(TELEX_TANT)));
                    result.add(bn);
                } while (mCursor.moveToNext());
            }
            close();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return result;
        }
    }

    public String searchTEV1(String word) {
        String res = "";
        List<TELexBean> result = searchTE("'" + word + "'", TEV1_TABLES);
        TELexBean bn;
        for (int i = 0; i < result.size(); i++) {
            bn = result.get(i);
            res = res + extractTE(bn) + "\n";
        }
        if (res.equals("")) {
            res = "";
        }
        return res;
    }

    public String searchTEV2(String word) {
        String res = "";
        List<TELexBean> result = searchTE("'" + word + "'", TEV2_TABLES);
        TELexBean bn;
        for (int i = 0; i < result.size(); i++) {
            bn = result.get(i);
            res = res + extractTE(bn) + "\n";
        }
        if (res.equals("")) {
            res = "";
        }
        return res;
    }

    /**
   * @param word
   * @return
   */
    public List<ETLexBean> searchET(final String word) {
        List<ETLexBean> result = new ArrayList<ETLexBean>();
        open();
        try {
            Cursor mCursor = db.query(true, ET_TABLES + word.substring(1, 2).toLowerCase(), new String[] { ETLEX_ID, ETLEX_TENTRY, ETLEX_ETHAI, ETLEX_ESYN, ETLEX_EANT, ETLEX_ECAT }, ETLEX_ID + " like " + word, null, null, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
                do {
                    ETLexBean bn = new ETLexBean();
                    bn.setId(mCursor.getString(mCursor.getColumnIndex(ETLEX_ID)));
                    bn.setTentry(mCursor.getString(mCursor.getColumnIndex(ETLEX_TENTRY)));
                    bn.setEthai(mCursor.getString(mCursor.getColumnIndex(ETLEX_ETHAI)));
                    bn.setEsyn(mCursor.getString(mCursor.getColumnIndex(ETLEX_ESYN)));
                    bn.setEant(mCursor.getString(mCursor.getColumnIndex(ETLEX_EANT)));
                    bn.setEcat(mCursor.getString(mCursor.getColumnIndex(ETLEX_ECAT)));
                    result.add(bn);
                } while (mCursor.moveToNext());
            }
            close();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return result;
        }
    }

    public String searchETV1(String word) {
        String res = "";
        List<ETLexBean> result = searchET("'" + word + "'");
        ETLexBean bn;
        for (int i = 0; i < result.size(); i++) {
            bn = result.get(i);
            res = res + extractET(bn) + "\n";
        }
        if (res.equals("")) {
            res = "";
        }
        return res;
    }

    public String extractET(ETLexBean bn) {
        String res = "";
        res = bn.getId() + " <" + bn.getEcat() + "> " + bn.getTentry() + "\n";
        if (bn.getEsyn() != null) res = res + "Syn. " + bn.getEsyn() + "\n";
        if (bn.getEant() != null) res = res + "Related. " + bn.getEant();
        return res;
    }

    public String extractTE(TELexBean bn) {
        String res = "";
        res = bn.getTsearch() + " <" + bn.getTcat() + "> " + bn.getEentry() + "\n";
        if (bn.getTsample() != null) res = res + "Sample. " + bn.getTsample() + "\n";
        if (bn.getTsyn() != null) res = res + "Syn. " + bn.getTsyn() + "\n";
        if (bn.getTant() != null) res = res + "Related. " + bn.getTant();
        return res;
    }
}
