package tristero.search.dbm;

import tristero.*;
import com.sleepycat.db.*;
import java.util.*;
import java.io.*;

public class CountedDb extends Db {

    public static boolean debug = false;

    public static final int ALL = 0;

    public static final int SUBJECT = 1;

    public static final int PREDICATE = 2;

    public static final int OBJECT = 3;

    public static final int SUBJECT_PREDICATE = 4;

    public static final int SUBJECT_OBJECT = 5;

    public static final int PREDICATE_OBJECT = 6;

    public static final int OTHER = 7;

    public static DbEnv env;

    public static final int MAXDBS = 20;

    public static int refcount = 0;

    protected File myFile;

    public int keyType;

    protected static synchronized void increment() {
        refcount++;
        if (refcount > MAXDBS) {
            if (debug) System.out.println("GARBAGE COLLECTING...(" + refcount + ")");
            if (debug) System.out.flush();
            System.gc();
            System.runFinalization();
        }
    }

    protected static synchronized void decrement() {
        refcount--;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (debug) System.out.println("Closing " + myFile.getName());
        if (debug) System.out.flush();
        sync(0);
        close(0);
        if (myFile.getName().startsWith("tmp/")) myFile.delete();
        if (refcount == 0) {
            env.close(0);
            env = null;
        }
    }

    protected static DbEnv makeEnv() throws DbException, IOException {
        if (env != null) return env;
        env = new DbEnv(0);
        env.set_error_stream(System.err);
        env.set_errpfx("Db3TripleStore");
        env.open(".", Db.DB_CREATE | Db.DB_INIT_MPOOL, 0);
        return env;
    }

    public CountedDb(File f, int dbType, int keyType) throws DbException, IOException {
        super(makeEnv(), 0);
        myFile = f;
        this.keyType = keyType;
        set_error_stream(System.err);
        set_errpfx("Db3TripleStore");
        if (debug) System.err.println("Attempting to create db " + f.getPath());
        if (debug) System.err.flush();
        try {
            open(f.getPath(), null, dbType, Db.DB_CREATE, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (debug) System.err.println("Db open");
        if (debug) System.err.flush();
    }

    public String toString() {
        return myFile.getAbsolutePath();
    }

    public boolean equals(Object o) {
        if (!(o instanceof CountedDb)) return false;
        CountedDb db = (CountedDb) o;
        return this.toString().equals(db.toString());
    }

    public byte[] get(int i) {
        System.err.println("CountedDb.get(" + i + ")");
        try {
            Dbt key = new Dbt();
            key.set_recno_key_data(i);
            Dbt value = new Dbt();
            get(null, key, value, 0);
            return value.get_data();
        } catch (Exception e) {
            System.out.println("Error in get(): " + e);
            return null;
        }
    }

    public byte[] get(byte[] b) {
        try {
            Dbt key = new Dbt(b);
            Dbt value = new Dbt();
            get(null, key, value, 0);
            return value.get_data();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void remove(int i) {
        try {
            Dbt key = new Dbt();
            key.set_recno_key_data(i);
            del(null, key, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(byte[] b) {
        try {
            Dbt key = new Dbt(b);
            del(null, key, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRec(int i, byte[] v) {
        Dbt key = new Dbt();
        key.set_recno_key_data(i);
        Dbt value = new Dbt(v);
        try {
            put(null, key, value, Db.DB_NOOVERWRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(int i, byte[] k) {
        Dbt key = new Dbt(k);
        byte[] b = Db3Util.intToBytes(i);
        Dbt value = new Dbt(b);
        try {
            put(null, key, value, Db.DB_NOOVERWRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(byte[] k, byte[] v) {
        Dbt key = new Dbt(k);
        Dbt value = new Dbt(v);
        try {
            put(null, key, value, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Enumeration keys() throws DbException {
        return new DbEnumeration(this);
    }

    public Enumeration elements() throws DbException {
        if (keyType == ALL) return new TripleEnumeration(this); else return null;
    }

    public Iterator iterator() throws DbException {
        return new TripleEnumeration(this);
    }

    private class DbEnumeration implements Enumeration {

        Dbc cursor;

        Dbt key = new Dbt();

        Dbt value = new Dbt();

        public DbEnumeration(Db db) throws DbException {
            cursor = db.cursor(null, 0);
            fetchNext();
        }

        public boolean hasMoreElements() {
            return key != null;
        }

        public Object nextElement() {
            if (key == null) return null;
            try {
                fetchNext();
            } catch (Exception e) {
                e.printStackTrace();
                key = null;
                return null;
            }
            return key.get_data();
        }

        public void fetchNext() throws DbException {
            int result = cursor.get(key, value, Db.DB_NEXT);
            if (result != 0) {
                key = null;
                return;
            }
        }
    }
}
