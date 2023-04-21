package org.maverickdbms.database.bdb;

import java.util.Hashtable;
import java.io.*;
import com.sleepycat.db.Db;
import com.sleepycat.db.DbEnv;
import com.sleepycat.db.DbException;
import org.maverickdbms.basic.mvConstants;
import org.maverickdbms.basic.mvConstantString;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.Program;
import org.maverickdbms.basic.List;
import org.maverickdbms.basic.Properties;
import org.maverickdbms.basic.MaverickString;
import org.maverickdbms.basic.Factory;
import org.maverickdbms.basic.Session;

public class bdbDataInterface {

    static final int PRINT_CHANNEL = -1;

    static final String ACCOUNTPATH = "org.maverickdbms.database.bdb.accountpath";

    static final char[] ESCAPED_CHARS = { '/', '?', '"', '%', '*', ':', '<', '|', '>', '\\' };

    static final String MANGLE_CHARS = new String(ESCAPED_CHARS);

    static final String REP_CHARS = "SQD%ACLVGB";

    private Factory factory;

    private Hashtable lists = new Hashtable();

    private String accountpath;

    private Session session;

    private DbEnv dbenv;

    public void close() {
    }

    public boolean containsList(mvConstantString name) {
        return lists.containsKey(name.toString());
    }

    public void createFile(mvConstantString type, mvConstantString name, mvConstantString[] fields) {
        boolean makedict = true;
        String dictname;
        String mname = mangle(name);
        dictname = "d_" + mname;
        if (type.equals("DICT")) {
            mname = dictname;
            makedict = false;
        } else {
            if (type.equals("DATA")) {
                makedict = false;
            }
        }
        try {
            Db db = new Db(dbenv, 0);
            db.open(null, mname, "", db.DB_HASH, (Db.DB_CREATE | Db.DB_EXCL), 0);
            if (makedict) {
                db.open(null, dictname, "", db.DB_HASH, (Db.DB_CREATE | Db.DB_EXCL), 0);
            }
        } catch (DbException dbe) {
            System.err.println("Database error: " + dbe.toString());
        } catch (FileNotFoundException fnf) {
            System.err.println("File not found error: " + fnf.toString());
        }
    }

    public void dropFile(mvConstantString type, mvConstantString name) {
        boolean dropdict = true;
        String mname = mangle(name);
        if (type.equals("DICT")) {
            mname = "d_" + mname;
            dropdict = false;
        } else {
            if (type.equals("DATA")) {
                dropdict = false;
            }
        }
        try {
            Db db = new Db(dbenv, 0);
            db.remove(mname, "", 0);
        } catch (DbException dbe) {
            System.err.println("Database error: " + dbe.toString());
        } catch (FileNotFoundException fnf) {
            System.err.println("File not found: " + fnf.toString());
        }
    }

    DbEnv getEnv() {
        return dbenv;
    }

    public List getList(mvConstantString name) {
        String n = name.toString();
        if (lists.containsKey(n)) {
            return (List) lists.get(n);
        }
        List l = new bdbList();
        lists.put(n, l);
        return l;
    }

    public boolean isClosed() {
        return false;
    }

    String mangle(mvConstantString iname) {
        int len = iname.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            char c = iname.charAt(i);
            int tpos = MANGLE_CHARS.indexOf(c);
            if (tpos > -1) {
                sb.append("%" + REP_CHARS.charAt(tpos));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public void init(Session session) {
        this.session = session;
        factory = session.getFactory();
        Properties prop = session.getProperties();
        accountpath = prop.get(ACCOUNTPATH);
        try {
            if (accountpath == null) {
                session.getChannel(PRINT_CHANNEL).PRINT(factory.getConstant("Account Path: "), false, session.getStatus());
                MaverickString s = factory.getString();
                session.getInputChannel().INPUT(s, false, session.getStatus());
                accountpath = s.toString();
                try {
                    File taccount = new File(accountpath);
                    boolean exists = taccount.exists();
                } catch (NullPointerException e) {
                    System.out.println("Caught open exception " + e);
                }
            }
            dbenv = new DbEnv(0);
            dbenv.open(accountpath, Db.DB_INIT_MPOOL, 0);
        } catch (FileNotFoundException fnf) {
            System.err.println("Account not found, creating..." + fnf.toString());
            try {
                dbenv.open(accountpath, Db.DB_INIT_MPOOL | Db.DB_CREATE, 0);
            } catch (DbException dbe2) {
                System.err.println("Database error: " + dbe2.toString());
            } catch (FileNotFoundException fnf2) {
                System.err.println("Could not create account." + fnf2.toString());
            }
        } catch (Exception e) {
            System.out.println("Caught open exception " + e);
        }
    }

    public mvConstantString OPEN(Program program, MaverickString var, mvConstantString type, mvConstantString name, int flags) {
        bdbFile file = new bdbFile(this, factory);
        mvConstantString retval = file.OPEN(type, name, flags, factory.getStatus());
        var.setFile(file);
        return retval;
    }

    public void CLEARSELECT() throws MaverickException {
        throw new MaverickException(0, "Sorry CLEARSELECT is not implemented yet");
    }

    public void RELEASE() throws MaverickException {
        throw new MaverickException(0, "Sorry RELEASE is not implemented yet");
    }
}
