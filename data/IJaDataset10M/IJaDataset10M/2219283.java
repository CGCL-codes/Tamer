package tristero.search.dbm;

import java.io.*;
import java.util.*;
import com.sleepycat.db.*;

public class Db3List extends AbstractList {

    public static boolean debug = true;

    boolean expired = false;

    CountedDb db;

    CountedDb rec;

    String filename;

    int count = 1;

    public Db3List() {
        super();
    }

    public Db3List(String s) throws Exception {
        super();
        filename = s;
        if (debug) System.err.println("new db3list");
        if (debug) System.err.flush();
        File f = new File(filename + ".dbm");
        f.delete();
        try {
            db = new CountedDb(f, Db.DB_HASH, CountedDb.ALL);
            rec = new CountedDb(new File(filename + "-rec.dbm"), Db.DB_RECNO, CountedDb.ALL);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        if (debug) System.err.println("db: " + db);
        if (debug) System.err.println("rec: " + rec);
        if (debug) System.err.flush();
        Dbc cursor = rec.cursor(null, 0);
        Dbt key = new Dbt();
        Dbt value = new Dbt();
        cursor.get(key, value, Db.DB_LAST | Db.DB_GET_RECNO);
        byte[] b = value.get_data();
        if (b == null) count = 1; else count = Db3Util.bytesToInt(b);
        if (debug) System.out.println("SET COUNT TO (" + filename + "+count)");
    }

    public String getFilename() {
        return filename;
    }

    public void close() {
        try {
            if (db != null) db.close(0);
            db = null;
            if (rec != null) rec.close(0);
            rec = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void expire() {
        expired = true;
        if (size() == 0) {
            close();
            File f = new File(filename + ".dbm");
            f.delete();
            f = new File(filename + "-rec.dbm");
            f.delete();
        }
    }

    public int size() {
        if (debug) System.out.println("Db3List(" + filename + ").size()==" + (count - 1));
        if (debug) System.out.flush();
        return count - 1;
    }

    public Object get(int i) {
        System.err.println("Db3List(" + filename + ").get(" + (i + 1) + ")/" + size());
        byte[] b = rec.get(i + 1);
        if (i == size() - 1) if (expired) {
            if (debug) System.out.println("EXPIRING " + filename);
            if (debug) System.out.flush();
            close();
            File f = new File(filename + ".dbm");
            f.delete();
            f = new File(filename + "-rec.dbm");
            f.delete();
        }
        if (b != null) return unmarshal(b); else {
            if (debug) System.out.println("Db3List(" + filename + ")[" + i + "] not found.");
            if (debug) System.out.flush();
            return null;
        }
    }

    protected byte[] marshal(List l) {
        return Db3Util.marshal(l);
    }

    protected Object unmarshal(byte[] b) {
        return Db3Util.unmarshal(b);
    }

    public boolean add(Object o) {
        if (!(o instanceof List)) return false;
        List l = (List) o;
        if (debug) System.out.println("adding " + l);
        byte[] b = marshal(l);
        System.out.flush();
        System.err.println("~~~ " + filename + ".addRec(" + count + ")");
        rec.addRec(count, b);
        db.add(count, b);
        count++;
        return true;
    }

    public Object remove(int i) {
        byte[] b = rec.get(i + 1);
        rec.remove(i + 1);
        db.remove(b);
        return unmarshal(b);
    }

    public String toString() {
        return "Db3: " + db;
    }
}
