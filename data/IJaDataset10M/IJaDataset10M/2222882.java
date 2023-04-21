package de.psychomatic.mp3db.core.dblayer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import de.psychomatic.mp3db.utils.Config;

/**
 * @author Kykal
 */
public class DB {

    private static boolean _init = false;

    private static DB _instance;

    private static final Logger LOG = Logger.getLogger(DB.class);

    public static List checkDB() {
        return getInstance().checkTables();
    }

    public static List clearDB() {
        return getInstance().clearTables();
    }

    public static List<Exception> createDB() {
        return getInstance().createTables();
    }

    public static List dropDB() {
        return getInstance().dropTables();
    }

    private static DB getInstance() {
        if (_instance == null) {
            synchronized (DB.class) {
                if (_instance == null) {
                    _instance = new DB();
                }
            }
        }
        return _instance;
    }

    public static void initDB() throws IOException {
        getInstance().init();
    }

    public static boolean isInit() {
        return _init;
    }

    private Properties _props;

    private DB() {
    }

    private List<Exception> checkTables() {
        final List<Exception> exceptions = new ArrayList<Exception>();
        exceptions.addAll(executeSQL("mediafile.check"));
        exceptions.addAll(executeSQL("cd.check"));
        exceptions.addAll(executeSQL("album.check"));
        exceptions.addAll(executeSQL("coveritem.check"));
        return exceptions;
    }

    private List<Exception> clearTables() {
        final List<Exception> exceptions = new ArrayList<Exception>();
        exceptions.addAll(executeSQL("mediafile.clear"));
        exceptions.addAll(executeSQL("cd.clear"));
        exceptions.addAll(executeSQL("album.clear"));
        exceptions.addAll(executeSQL("coveritem.clear"));
        return exceptions;
    }

    private List<Exception> createTables() {
        final List<Exception> exceptions = new ArrayList<Exception>();
        exceptions.addAll(executeSQL("mediafile.create"));
        exceptions.addAll(executeSQL("cd.create"));
        exceptions.addAll(executeSQL("album.create"));
        exceptions.addAll(executeSQL("coveritem.create"));
        return exceptions;
    }

    private List<Exception> dropTables() {
        final List<Exception> exceptions = new ArrayList<Exception>();
        exceptions.addAll(executeSQL("mediafile.drop"));
        exceptions.addAll(executeSQL("cd.drop"));
        exceptions.addAll(executeSQL("album.drop"));
        exceptions.addAll(executeSQL("coveritem.drop"));
        return exceptions;
    }

    private List<Exception> executeSQL(final String property) {
        final List<Exception> errors = new ArrayList<Exception>();
        String sqlString = _props.getProperty(property);
        if (sqlString.endsWith(";")) {
            sqlString = sqlString.substring(0, sqlString.length() - 1);
        }
        final String[] sqls = sqlString.split(";");
        Connection con;
        try {
            con = Torque.getConnection();
            final Statement stmt = con.createStatement();
            String sql = null;
            for (final String element : sqls) {
                sql = element.trim();
                try {
                    LOG.trace("Execute: " + sql);
                    stmt.execute(sql);
                } catch (final SQLException e) {
                    LOG.error("Error in SQL", e);
                    if (!sql.toLowerCase().startsWith("drop")) {
                        errors.add(e);
                    }
                }
            }
            stmt.close();
            Torque.closeConnection(con);
        } catch (final TorqueException e) {
            LOG.error("executeSQL(String)", e);
            errors.add(e);
        } catch (final SQLException e) {
            LOG.error("executeSQL(String)", e);
            errors.add(e);
        }
        return errors;
    }

    private void init() throws IOException {
        if (Torque.isInit()) {
            if (!Config.isInternalDB()) {
                final String adapter = Torque.getConfiguration().getString("database.mp3db.adapter");
                if (adapter != null && adapter.trim().length() > 0) {
                    loadDBConfig(adapter);
                    try {
                        final Connection con = Torque.getConnection();
                        final Statement stmt = con.createStatement();
                        if (adapter.trim().toLowerCase().equals("mysql")) {
                            modifiyMySQL(stmt);
                        } else if (adapter.trim().toLowerCase().equals("postgresql")) {
                            modifyPostgreSQL(stmt);
                        } else if (adapter.trim().toLowerCase().equals("hypersonic")) {
                            modifyHypersonic(stmt);
                        }
                        stmt.close();
                        Torque.closeConnection(con);
                    } catch (final TorqueException e) {
                        LOG.error("init()", e);
                    } catch (final SQLException e) {
                        LOG.error("init()", e);
                    }
                }
            } else {
                loadDBConfig("hypersonic");
                try {
                    final Connection con = Torque.getConnection();
                    final Statement stmt = con.createStatement();
                    modifyHypersonic(stmt);
                    stmt.close();
                    Torque.closeConnection(con);
                } catch (final TorqueException e) {
                    LOG.error("init()", e);
                } catch (final SQLException e) {
                    LOG.error("init()", e);
                }
            }
        }
    }

    private void loadDBConfig(final String adapter) throws IOException {
        final URL url = getClass().getClassLoader().getResource("adapter/" + adapter + ".properties");
        _props = new Properties();
        _props.load(url.openStream());
        _init = true;
    }

    private void modifiyMSSQL(final Statement stmt) {
        try {
            stmt.execute("ALTER TABLE [album] ADD COLUMN [album_type] [smallint] NOT NULL DEFAULT (0)");
            stmt.execute("UPDATE [album] SET [album_type] = 1 WHERE [is_sampler] = 1");
            stmt.execute("ALTER TABLE [album] DROP COLUMN [is_sampler]");
        } catch (final SQLException se) {
            LOG.debug("Albumtype already added");
        }
    }

    /**
     * @param stmt
     */
    private void modifiyMySQL(final Statement stmt) {
        try {
            stmt.execute("SELECT groesse FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile CHANGE groesse size bigint(20)");
            stmt.execute("ALTER TABLE mediafile CHANGE laenge playtime int(11)");
        } catch (final SQLException se) {
            LOG.debug("Column already renamed or no table found");
        }
        try {
            stmt.execute("SELECT title_sndx FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile DROP COLUMN title_sndx");
        } catch (final SQLException se) {
            LOG.debug("Column already removed");
        }
        try {
            stmt.execute("SELECT artist_sndx FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile DROP COLUMN artist_sndx");
        } catch (final SQLException se) {
            LOG.debug("Column already removed");
        }
        try {
            stmt.execute("SELECT album_sndx FROM album WHERE aid = 0");
            stmt.execute("ALTER TABLE album DROP COLUMN album_sndx");
        } catch (final SQLException se) {
            LOG.debug("Column already removed");
        }
        try {
            stmt.execute("SELECT cid FROM covers WHERE cid = 0");
            executeSQL("coveritem.create");
            updateCoversTable(stmt, "SELECT album.aid, covers.front, covers.back, covers.inlay," + " covers.cd, covers.other FROM album, covers WHERE album.cover = covers.cid", "INSERT INTO coveritem (albumid, citype, cidata) VALUES (?, ?, ?)");
            stmt.execute("ALTER TABLE album DROP COLUMN cover");
            stmt.execute("DROP TABLE covers");
            stmt.execute("CREATE INDEX idx_mediafile_cdnr ON mediafile(cdnr)");
        } catch (final SQLException se) {
            LOG.debug("Covers already updated or no table found");
        }
        try {
            stmt.execute("SELECT id FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile CHANGE id mfid int(11) auto_increment");
            stmt.execute("ALTER TABLE mediafile CHANGE size filesize bigint(20)");
            stmt.execute("ALTER TABLE mediafile CHANGE title title varchar(256) NULL");
            stmt.execute("ALTER TABLE mediafile CHANGE artist artist varchar(256) NULL");
            stmt.execute("ALTER TABLE album CHANGE album album varchar(256) NOT NULL");
        } catch (final SQLException se) {
            LOG.debug("MFID column already updated");
        }
        try {
            stmt.execute("ALTER TABLE album ADD COLUMN album_type smallint default '0' NOT NULL AFTER aid");
            stmt.execute("UPDATE album SET album_type = 1 WHERE is_sampler = 1");
            stmt.execute("ALTER TABLE album DROP COLUMN is_sampler");
        } catch (final SQLException se) {
            LOG.debug("Albumtype already added");
        }
    }

    private void modifiyOracle(final Statement stmt) {
        try {
            stmt.execute("ALTER TABLE album ADD COLUMN album_type number(4) DEFAULT 0 NOT NULL");
            stmt.execute("UPDATE album SET album_type = 1 WHERE is_sampler = 1");
            stmt.execute("ALTER TABLE album DROP COLUMN is_sampler");
        } catch (final SQLException se) {
            LOG.debug("Albumtype already added");
        }
    }

    /**
     * @param stmt
     * @throws SQLException
     */
    private void modifyHypersonic(final Statement stmt) throws SQLException {
        boolean action = false;
        try {
            stmt.execute("SELECT title_sndx FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile DROP COLUMN title_sndx");
            action = true;
        } catch (final SQLException se) {
            LOG.debug("Column already removed");
        }
        try {
            stmt.execute("SELECT artist_sndx FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile DROP COLUMN artist_sndx");
            action = true;
        } catch (final SQLException se) {
            LOG.debug("Column already removed");
        }
        try {
            stmt.execute("SELECT album_sndx FROM album WHERE aid = 0");
            stmt.execute("ALTER TABLE album DROP COLUMN album_sndx");
            action = true;
        } catch (final SQLException se) {
            LOG.debug("Column already removed");
        }
        try {
            stmt.execute("SELECT count(*) FROM covers WHERE cid = 0");
            executeSQL("coveritem.create");
            updateCoversTable(stmt, "SELECT album.aid, covers.front, covers.back, covers.inlay," + " covers.cd, covers.other FROM album, covers WHERE album.cover = covers.cid", "INSERT INTO coveritem (albumid, citype, cidata) VALUES (?, ?, ?)");
            stmt.execute("ALTER TABLE album DROP COLUMN cover");
            stmt.execute("DROP TABLE covers");
            action = true;
            stmt.execute("CREATE INDEX idx_mediafile_cdnr ON mediafile(cdnr)");
        } catch (final SQLException se) {
            LOG.debug("Covers already updated or no table found");
        }
        try {
            stmt.execute("SELECT id FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile ALTER COLUMN id RENAME TO mfid");
            stmt.execute("ALTER TABLE mediafile ALTER COLUMN size RENAME TO filesize");
            stmt.execute("ALTER TABLE mediafile ALTER COLUMN artist varchar(256) NULL");
            stmt.execute("ALTER TABLE mediafile ALTER COLUMN title varchar(256) NULL");
            stmt.execute("ALTER TABLE album ALTER COLUMN album varchar(256) NOT NULL");
            action = true;
        } catch (final SQLException se) {
            LOG.debug("MFID column already updated");
        }
        try {
            stmt.execute("ALTER TABLE album ADD COLUMN album_type SMALLINT DEFAULT '0' NOT NULL BEFORE is_sample");
            stmt.execute("UPDATE album SET album_type = 1 WHERE is_sampler = 1");
            stmt.execute("ALTER TABLE album DROP COLUMN is_sampler");
            action = true;
        } catch (final SQLException se) {
            LOG.debug("Albumtype already added");
        }
        if (action) {
            stmt.execute("CHECKPOINT DEFRAG");
        }
    }

    /**
     * @param stmt
     */
    private void modifyPostgreSQL(final Statement stmt) {
        try {
            stmt.execute("SELECT groesse FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile RENAME COLUMN groesse TO size");
            stmt.execute("ALTER TABLE mediafile RENAME COLUMN laenge TO playtime");
        } catch (final SQLException se) {
            LOG.debug("Column already renamed or no table found");
        }
        try {
            stmt.execute("SELECT title_sndx FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile DROP COLUMN title_sndx");
        } catch (final SQLException se) {
            LOG.debug("Column already removed");
        }
        try {
            stmt.execute("SELECT artist_sndx FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile DROP COLUMN artist_sndx");
        } catch (final SQLException se) {
            LOG.debug("Column already removed");
        }
        try {
            stmt.execute("SELECT album_sndx FROM album WHERE aid = 0");
            stmt.execute("ALTER TABLE album DROP COLUMN album_sndx");
        } catch (final SQLException se) {
            LOG.debug("Column already removed");
        }
        try {
            stmt.execute("SELECT cid FROM covers WHERE cid = 0");
            executeSQL("coveritem.create");
            updateCoversTable(stmt, "SELECT album.aid, covers.front, covers.back, covers.inlay," + " covers.cd, covers.other FROM album, covers WHERE album.cover = covers.cid", "INSERT INTO coveritem (albumid, citype, cidata) VALUES (?, ?, ?)");
            stmt.execute("ALTER TABLE album DROP COLUMN cover");
            stmt.execute("DROP TABLE covers");
            stmt.execute("DROP SEQUENCE covers_seq");
            stmt.execute("CREATE INDEX idx_mediafile_cdnr ON mediafile(cdnr)");
        } catch (final SQLException se) {
            LOG.debug("Covers already updated or no table found");
        }
        try {
            stmt.execute("SELECT id FROM mediafile WHERE id = 0");
            stmt.execute("ALTER TABLE mediafile RENAME COLUMN id TO mfid");
            stmt.execute("ALTER TABLE mediafile RENAME COLUMN size TO filesize");
            stmt.execute("ALTER TABLE mediafile ALTER COLUMN title TYPE varchar(256)");
            stmt.execute("ALTER TABLE mediafile ALTER COLUMN title SET NULL");
            stmt.execute("ALTER TABLE mediafile ALTER COLUMN artist TYPE varchar(256)");
            stmt.execute("ALTER TABLE mediafile ALTER COLUMN artist SET NULL");
            stmt.execute("ALTER TABLE album ALTER COLUMN album TYPE varchar(256)");
        } catch (final SQLException se) {
            LOG.debug("MFID column already updated");
        }
        try {
            stmt.execute("ALTER TABLE album ADD COLUMN album_type smallint default '0' NOT NULL");
            stmt.execute("UPDATE album SET album_type = 1 WHERE is_sampler = 1");
            stmt.execute("ALTER TABLE album DROP COLUMN is_sampler");
        } catch (final SQLException se) {
            LOG.debug("Albumtype already added");
        }
    }

    private void updateCoversTable(final Statement stmt, final String query, final String prepString) throws SQLException {
        final ResultSet rst = stmt.executeQuery(query);
        final PreparedStatement prepst = stmt.getConnection().prepareStatement(prepString);
        while (rst.next()) {
            final int albumid = rst.getInt(1);
            byte[] result;
            for (int i = 2; i < 7; i++) {
                result = rst.getBytes(i);
                if (result != null && result.length > 0) {
                    prepst.setInt(1, albumid);
                    String name = null;
                    switch(i) {
                        case 2:
                            name = "Front";
                            break;
                        case 3:
                            name = "Back";
                            break;
                        case 4:
                            name = "Inlay";
                            break;
                        case 5:
                            name = "CD";
                            break;
                        case 6:
                            name = "Other";
                            break;
                        default:
                            break;
                    }
                    prepst.setString(2, name);
                    prepst.setBytes(3, result);
                    prepst.execute();
                }
            }
        }
        prepst.close();
        rst.close();
    }
}
