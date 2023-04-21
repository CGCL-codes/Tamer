package com.totsp.database.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.totsp.database.Main;
import com.totsp.database.util.StringUtil;
import java.util.ArrayList;

/**
 * Oversimplified Android DB example.
 * Includes SQLite foreign keys and unique constraints - though contrived example.
 * 
 * @author ccollins
 *
 */
public class DataHelper {

    private static final String DATABASE_NAME = "sample.db";

    private static final int DATABASE_VERSION = 1;

    private static final String BOOK_TABLE = "book";

    private static final String AUTHOR_TABLE = "author";

    private static final String BOOKAUTHOR_TABLE = "bookauthor";

    private SQLiteDatabase db;

    public DataHelper(Context context) {
        OpenHelper openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
        if (openHelper.isDbCreated()) {
        }
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void resetDbConnection() {
        Log.i(Main.LOG_TAG, "resetting database connection (close and re-open).");
        cleanup();
        db = SQLiteDatabase.openDatabase("/data/data/com.totsp.database/databases/sample.db", null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void cleanup() {
        if ((db != null) && db.isOpen()) {
            db.close();
        }
    }

    public long insertBook(Book b) {
        long bookId = 0L;
        if ((b != null) && (b.title != null)) {
            Book bookExists = selectBook(b.title);
            if (bookExists != null) {
                return bookExists.id;
            }
            db.beginTransaction();
            try {
                ArrayList<Long> authorIds = new ArrayList<Long>();
                if (b.authors != null) {
                    String[] names = StringUtil.expandComma(b.authors);
                    for (String name : names) {
                        Author authorExists = selectAuthor(name);
                        if (authorExists == null) {
                            authorIds.add(insertAuthor(new Author(name)));
                        } else {
                            authorIds.add(authorExists.id);
                        }
                    }
                }
                ContentValues values = new ContentValues();
                values.put(DataConstants.TITLE, b.title);
                bookId = db.insert(DataHelper.BOOK_TABLE, null, values);
                insertBookAuthorData(bookId, authorIds);
                db.setTransactionSuccessful();
            } catch (SQLException e) {
                Log.e(Main.LOG_TAG, "Error inserting book", e);
            } finally {
                db.endTransaction();
            }
        } else {
            throw new IllegalArgumentException("Error, book cannot be null, and must have a unique title");
        }
        return bookId;
    }

    public Book selectBook(long id) {
        Book b = null;
        Cursor c = db.query(DataHelper.BOOK_TABLE, new String[] { DataConstants.TITLE }, DataConstants.BOOKID + " = ?", new String[] { String.valueOf(id) }, null, null, null, "1");
        if (c.moveToFirst()) {
            b = new Book();
            b.id = id;
            b.title = c.getString(0);
        }
        if ((c != null) && !c.isClosed()) {
            c.close();
        }
        if (b != null) {
            appendAuthors(b);
        }
        return b;
    }

    public Book selectBook(String title) {
        Book b = null;
        Cursor c = db.query(DataHelper.BOOK_TABLE, new String[] { DataConstants.BOOKID }, DataConstants.TITLE + " = ?", new String[] { title }, null, null, null, "1");
        if (c.moveToFirst()) {
            b = selectBook(c.getLong(0));
        }
        if ((c != null) && !c.isClosed()) {
            c.close();
        }
        return b;
    }

    public ArrayList<Book> selectAllBooks() {
        ArrayList<Book> list = new ArrayList<Book>();
        Cursor c = db.query(DataHelper.BOOK_TABLE, new String[] { DataConstants.BOOKID, DataConstants.TITLE }, null, null, null, null, DataConstants.TITLE + " desc", null);
        if (c.moveToFirst()) {
            do {
                Book b = new Book();
                b.id = c.getLong(0);
                b.title = c.getString(1);
                list.add(b);
            } while (c.moveToNext());
        }
        if ((c != null) && !c.isClosed()) {
            c.close();
        }
        for (Book b : list) {
            appendAuthors(b);
        }
        return list;
    }

    private void appendAuthors(Book b) {
        ArrayList<Author> authors = selectAuthorsByBook(b.id);
        String[] names = new String[authors.size()];
        int i = 0;
        for (Author a : authors) {
            names[i] = a.name;
            i++;
        }
        b.authors = StringUtil.contractComma(names);
    }

    public void insertBookAuthorData(long bookId, ArrayList<Long> authorIds) {
        for (Long authorId : authorIds) {
            ContentValues values = new ContentValues();
            values.put(DataConstants.BOOKID, bookId);
            values.put(DataConstants.AUTHORID, authorId);
            db.insert(DataHelper.BOOKAUTHOR_TABLE, null, values);
        }
    }

    public long insertAuthor(Author a) {
        long authorId = 0L;
        if ((a != null) && (a.name != null)) {
            Author authorExists = selectAuthor(a.name);
            if (authorExists != null) {
                return authorExists.id;
            }
            ContentValues values = new ContentValues();
            values.put(DataConstants.NAME, a.name);
            authorId = db.insert(DataHelper.AUTHOR_TABLE, null, values);
        }
        return authorId;
    }

    public Author selectAuthor(long id) {
        Author a = null;
        Cursor c = db.query(DataHelper.AUTHOR_TABLE, new String[] { DataConstants.NAME }, DataConstants.AUTHORID + " = ?", new String[] { String.valueOf(id) }, null, null, null, "1");
        if (c.moveToFirst()) {
            a = new Author();
            a.id = id;
            a.name = c.getString(0);
        }
        if ((c != null) && !c.isClosed()) {
            c.close();
        }
        return a;
    }

    public Author selectAuthor(String name) {
        Author a = null;
        Cursor c = db.query(DataHelper.AUTHOR_TABLE, new String[] { DataConstants.AUTHORID }, DataConstants.NAME + " = ?", new String[] { name }, null, null, null, "1");
        if (c.moveToFirst()) {
            a = selectAuthor(c.getLong(0));
        }
        if ((c != null) && !c.isClosed()) {
            c.close();
        }
        return a;
    }

    public ArrayList<Author> selectAllAuthors() {
        ArrayList<Author> list = new ArrayList<Author>();
        Cursor c = db.query(DataHelper.AUTHOR_TABLE, new String[] { DataConstants.AUTHORID, DataConstants.NAME }, null, null, null, null, DataConstants.NAME + " desc", null);
        if (c.moveToFirst()) {
            do {
                Author a = new Author();
                a.id = c.getLong(0);
                a.name = c.getString(1);
                list.add(a);
            } while (c.moveToNext());
        }
        if ((c != null) && !c.isClosed()) {
            c.close();
        }
        return list;
    }

    public ArrayList<Author> selectAuthorsByBook(long bookId) {
        ArrayList<Author> authors = new ArrayList<Author>();
        Cursor c = db.query(DataHelper.BOOKAUTHOR_TABLE, new String[] { DataConstants.AUTHORID }, DataConstants.BOOKID + " = ?", new String[] { String.valueOf(bookId) }, null, null, null);
        if (c.moveToFirst()) {
            do {
                Author a = selectAuthor(c.getLong(0));
                authors.add(a);
            } while (c.moveToNext());
        }
        if ((c != null) && !c.isClosed()) {
            c.close();
        }
        return authors;
    }

    public void deleteAllDataYesIAmSure() {
        Log.i(Main.LOG_TAG, "deleting all data from database - deleteAllYesIAmSure invoked");
        db.beginTransaction();
        try {
            db.delete(DataHelper.AUTHOR_TABLE, null, null);
            db.delete(DataHelper.BOOKAUTHOR_TABLE, null, null);
            db.delete(DataHelper.BOOK_TABLE, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.execSQL("vacuum");
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        private boolean dbCreated;

        OpenHelper(Context context) {
            super(context, DataHelper.DATABASE_NAME, null, DataHelper.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE " + DataHelper.BOOK_TABLE + " (");
            sb.append(DataConstants.BOOKID + " INTEGER PRIMARY KEY, ");
            sb.append(DataConstants.TITLE + " TEXT");
            sb.append(");");
            db.execSQL(sb.toString());
            sb.setLength(0);
            sb.append("CREATE TABLE " + DataHelper.AUTHOR_TABLE + " (");
            sb.append(DataConstants.AUTHORID + " INTEGER PRIMARY KEY, ");
            sb.append(DataConstants.NAME + " TEXT");
            sb.append(");");
            db.execSQL(sb.toString());
            sb.setLength(0);
            sb.append("CREATE TABLE " + DataHelper.BOOKAUTHOR_TABLE + " (");
            sb.append(DataConstants.BOOKAUTHORID + " INTEGER PRIMARY KEY, ");
            sb.append(DataConstants.BOOKID + " INTEGER, ");
            sb.append(DataConstants.AUTHORID + " INTEGER, ");
            sb.append("FOREIGN KEY(" + DataConstants.BOOKID + ") REFERENCES " + DataHelper.BOOK_TABLE + "(" + DataConstants.BOOKID + "), ");
            sb.append("FOREIGN KEY(" + DataConstants.AUTHORID + ") REFERENCES " + DataHelper.AUTHOR_TABLE + "(" + DataConstants.AUTHORID + ") ");
            sb.append(");");
            db.execSQL(sb.toString());
            db.execSQL("CREATE UNIQUE INDEX uidxBookTitle ON " + DataHelper.BOOK_TABLE + "(" + DataConstants.TITLE + " COLLATE NOCASE)");
            db.execSQL("CREATE UNIQUE INDEX uidxAuthorName ON " + DataHelper.AUTHOR_TABLE + "(" + DataConstants.NAME + " COLLATE NOCASE)");
            dbCreated = true;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(Main.LOG_TAG, "SQLiteOpenHelper onUpgrade - oldVersion:" + oldVersion + " newVersion:" + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + DataHelper.BOOK_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DataHelper.AUTHOR_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DataHelper.BOOKAUTHOR_TABLE);
            onCreate(db);
        }

        public boolean isDbCreated() {
            return dbCreated;
        }
    }
}
