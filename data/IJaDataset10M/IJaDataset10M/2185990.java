package lb.prove;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {

    private static final String DATABASE_CREATE = "create table rssEntries (" + "url text, title text not null, body text not null, author text null, read integer not null default 0, created long not null, guid text not null primary key);";

    private static final String INDEX1_CREATE = "create index rss_created_index on rssEntries ( created ) ";

    private static final String DATABASE_NAME = "PlanetKDERSSFeeds";

    private static final String DATABASE_TABLE = "rssEntries";

    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public DBHelper(Context ctx) {
        try {
            db = ctx.openDatabase(DATABASE_NAME, null);
        } catch (FileNotFoundException e) {
            try {
                db = ctx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0, null);
                db.execSQL(DATABASE_CREATE);
                db.execSQL(INDEX1_CREATE);
            } catch (FileNotFoundException e1) {
                db = null;
            }
        }
    }

    public void close() {
        db.close();
    }

    public void createRow(String guid, String title, String body, String author, Date entryDate, String url) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("guid", guid);
        initialValues.put("title", title);
        initialValues.put("body", body);
        initialValues.put("author", author);
        initialValues.put("read", 0);
        initialValues.put("created", entryDate.getTime());
        initialValues.put("url", url);
        db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean addEntry(String guid, String title, String body, String author, Date entryDate, String url) {
        Cursor c = db.query(true, DATABASE_TABLE, new String[] { "guid" }, "guid=?", new String[] { guid }, null, null, null);
        if (c.count() == 0) {
            createRow(guid, title, body, author, entryDate, url);
            return true;
        }
        return false;
    }

    public void markRead(String guid) {
        ContentValues args = new ContentValues();
        args.put("read", 1);
        db.update(DATABASE_TABLE, args, "guid=?", new String[] { guid });
    }

    public void deleteItem(final String Id) {
        db.delete(DATABASE_TABLE, "guid=?", new String[] { Id });
    }

    public void deleteItems() {
        db.delete(DATABASE_TABLE, null, null);
    }

    public void eraseOldRows(final int nDays) {
        Date now = new Date();
        long millis = now.getTime();
        millis -= nDays * 1000 * 60 * 60 * 24;
        String ms = String.valueOf(millis);
        db.delete(DATABASE_TABLE, "created>?", new String[] { ms });
    }

    public List<RssItem> fetchAllRows() {
        List<RssItem> rows = new ArrayList<RssItem>();
        Cursor c = db.query(true, DATABASE_TABLE, new String[] { "guid", "title", "author", "body", "read", "created", "url" }, null, null, null, null, "created desc");
        int cnt = c.count();
        if (cnt > 0) {
            c.first();
            for (int i = 0; i < cnt; i++) {
                RssItem row = new RssItem();
                row.guid = c.getString(0);
                row.title = c.getString(1);
                row.body = c.getString(3);
                row.read = (c.getLong(4) != 0) ? true : false;
                long lTime = c.getLong(5);
                row.date = new Date(lTime);
                row.url = c.getString(6);
                rows.add(row);
                c.next();
            }
        }
        return rows;
    }
}
