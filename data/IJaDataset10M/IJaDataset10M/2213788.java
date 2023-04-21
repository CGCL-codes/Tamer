package org.joy.db;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.db.CompactConfig;
import com.sleepycat.db.Cursor;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.OperationStatus;
import com.sleepycat.db.SecondaryDatabase;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joy.db.Connection;

/**
 * 表示数据库数据和操作的中间层。作为数据的抽象
 * @author AC
 */
public abstract class Connection {

    private Vector<ResultReader> readerPool = new Vector<ResultReader>();

    private final Environment env;

    /**
     * 这个抽象层对应的底层数据库Database对象
     */
    protected Database db;

    private boolean freed;

    private Hashtable<String, SecondaryDatabase> secTable = new Hashtable<String, SecondaryDatabase>();

    protected void addSecDb(String name, SecondaryDatabase db) {
        secTable.put(name, db);
    }

    protected SecondaryDatabase getSecDb(String name) {
        return secTable.get(name);
    }

    public SecondaryConnection getSecondaryConn(final String name) throws DBException {
        SecondaryDatabase sDb = getSecDb(name);
        final Connection outter = this;
        SecondaryConnection conn = new SecondaryConnection(env, sDb) {

            @Override
            public String getDbName() {
                return name;
            }

            @Override
            public EntryBinding getKeyBinding() {
                try {
                    SecDbKeyCreator keyCreator = (SecDbKeyCreator) ((SecondaryDatabase) db).getSecondaryConfig().getKeyCreator();
                    return keyCreator.getSecKeyBinding();
                } catch (DatabaseException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }

            @Override
            public EntryBinding getValueBinding() {
                return outter.getValueBinding();
            }

            @Override
            public EntryBinding getPrimaryKeyBinding() {
                return outter.getKeyBinding();
            }
        };
        return conn;
    }

    /**
     * 创建一个数据库的抽象接口，如果该数据库不存在，则创建一个新的数据库
     * @param env 拥有该数据库的数据库环境
     * @param DBname 该数据库名字
     * @param dbConfig 打开该数据库所用的配置
     * @throws daphne.db.DBException 如果发生数据库错误，则抛出该异常
     */
    protected Connection(final Environment env, String DBname, DatabaseConfig dbConfig) throws DBException {
        this.env = env;
        try {
            if (env != null) {
                db = env.openDatabase(null, DBname, DBname, dbConfig);
            } else {
                db = new Database(DBname, DBname, dbConfig);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DBException("Can't open");
        }
    }

    protected Connection(Environment env, SecondaryDatabase db) {
        this.env = env;
        this.db = db;
    }

    /**
     * 关闭抽象层
     * @throws daphne.db.DBException 如果发生数据库错误，则抛出该异常
     */
    public void close() throws DBException {
        try {
            for (SecondaryDatabase secDb : secTable.values()) {
                secDb.close();
            }
            db.close();
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            throw new DBException("关闭错误");
        }
    }

    /**
     * 同步数据
     * @throws daphne.db.DBException 如果发生数据库错误，则抛出该异常
     */
    public void sync() throws DBException {
        try {
            db.sync();
            for (SecondaryDatabase secDb : secTable.values()) {
                secDb.sync();
            }
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            throw new DBException("同步错误");
        }
    }

    /**
     * 获取Database句柄
     * @return 返回数据库对象
     */
    public Database getDb() {
        return db;
    }

    /**
     * 是否被使用，在操作
     * @return 返回 该连接是否被使用
     */
    public boolean isFreed() {
        return freed;
    }

    /**
     * 设置该抽象层是否被使用
     */
    public void setFree() throws DBException {
        this.freed = true;
        for (ResultReader reader : readerPool) {
            reader.close();
        }
    }

    void setBusy() {
        this.freed = false;
    }

    /**
     * 获取该抽象层所代表的数据库的友好名字
     * @return 返回名字
     */
    public abstract String getDbName();

    /**
     * 获取该拥有抽象层所代表的数据库的数据库环境名
     * @return 返回数据库环境
     */
    public Environment getEnv() {
        return env;
    }

    /**
     * 预读数据库到内存中（尽可能多）
     * @throws daphne.db.DBException 如果发生数据库错误，则抛出该异常
     */
    public void preload() throws DBException {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取该抽象层所代表的数据库的键绑定
     * @return 返回该连接所代表的数据库键绑定
     */
    public abstract EntryBinding getKeyBinding();

    /**
     * 获取该抽象层所代表的数据库的值绑定
     * @return 返回值绑定
     */
    public abstract EntryBinding getValueBinding();

    protected void finalize() throws Throwable {
        super.finalize();
        setFree();
    }

    public Vector<ResultReader> getReaderPool() {
        return readerPool;
    }

    public Hashtable<String, SecondaryDatabase> getSecTable() {
        return secTable;
    }
}
