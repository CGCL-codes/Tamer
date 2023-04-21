package de.psychomatic.mp3db.gui.utils.database;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import de.psychomatic.mp3db.core.dblayer.dao.GenericDAO;
import de.psychomatic.mp3db.core.dblayer.utils.DBIf;
import de.psychomatic.mp3db.core.dblayer.utils.Transaction;
import de.psychomatic.mp3db.gui.Main;
import de.psychomatic.mp3db.gui.utils.database.adapter.AbstractModifier;
import de.psychomatic.mp3db.gui.utils.database.adapter.DatabaseModifyException;
import de.psychomatic.mp3db.gui.utils.database.adapter.HSQLModifier;
import de.psychomatic.mp3db.gui.utils.database.adapter.MSSQLModifier;
import de.psychomatic.mp3db.gui.utils.database.adapter.MySQLModifier;
import de.psychomatic.mp3db.gui.utils.database.adapter.OracleModifier;
import de.psychomatic.mp3db.gui.utils.database.adapter.PostgresModifier;

public class DatabaseDefinitionFactory {

    private static final String CREATE_FOREIGN_KEY = "create.fkey.";

    private static final String CREATE_TABLE = "create.table.";

    private static final String DROP_FOREIGN_KEY = "drop.fkey.";

    private static final String DROP_TABLE = "drop.table.";

    public static final String FK_COVERITEM_ALBUM = "coveritem_album";

    public static final String FK_MEDIAFILE_ALBUM = "mediafile_album";

    public static final String FK_MEDIAFILE_CD = "mediafile_cd";

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(DatabaseDefinitionFactory.class);

    public static final String TB_ALBUM = "album";

    public static final String TB_CD = "cd";

    public static final String TB_COVERITEM = "coveritem";

    public static final String TB_MEDIAFILE = "mediafile";

    public static final String CREATE_IDTABLE = "create.idtable";

    private final DBIf _db;

    private final AbstractModifier _modifier;

    private final Properties _props;

    public DatabaseDefinitionFactory(final DBIf db, final String adapter) throws IOException {
        _db = db;
        LOG.debug("Loading adapter: " + adapter);
        final URL url = getClass().getClassLoader().getResource("adapter/" + adapter + ".properties");
        _props = new Properties();
        _props.load(url.openStream());
        if (adapter.equals("mysql")) {
            _modifier = new MySQLModifier(this);
        } else if (adapter.equals("postgresql")) {
            _modifier = new PostgresModifier(this);
        } else if (adapter.equals("hypersonic")) {
            _modifier = new HSQLModifier(this);
        } else if (adapter.equals("oracle")) {
            _modifier = new OracleModifier(this);
        } else if (adapter.equals("mssql")) {
            _modifier = new MSSQLModifier(this);
        } else {
            _modifier = null;
        }
    }

    public List<Exception> clearTables() {
        final List<Exception> exceptions = new ArrayList<Exception>();
        exceptions.addAll(dropTables());
        exceptions.addAll(createTables());
        return exceptions;
    }

    public void createForeignKey(final String fkey) throws Exception {
        transactedExecuteSQL(_props.getProperty(CREATE_FOREIGN_KEY + fkey));
    }

    public void createTable(final String table) throws Exception {
        transactedExecuteSQL(_props.getProperty(CREATE_TABLE + table));
    }

    public List<Exception> createTables() {
        final List<Exception> exceptions = new ArrayList<Exception>();
        try {
            createTable(TB_CD);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            createTable(TB_MEDIAFILE);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            createTable(TB_ALBUM);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            createTable(TB_COVERITEM);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            createForeignKey(FK_COVERITEM_ALBUM);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            createForeignKey(FK_MEDIAFILE_ALBUM);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            createForeignKey(FK_MEDIAFILE_CD);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        initIdtable();
        return exceptions;
    }

    public void removeIdtable() {
        try {
            transactedExecuteSQL("DROP TABLE idtable");
        } catch (final Exception e) {
            LOG.error("removeIdtable()", e);
        }
    }

    public void initIdtable() {
        try {
            transactedExecuteSQL(_props.getProperty(CREATE_IDTABLE));
            transactedExecuteSQL("INSERT INTO idtable(idname, pkid) values ('dbversion', " + Main.ENGINEVERSION + ")");
            transactedExecuteSQL("INSERT INTO idtable(idname, pkid) values ('albumId', 0)");
            transactedExecuteSQL("INSERT INTO idtable(idname, pkid) values ('coveritemId', 0)");
            transactedExecuteSQL("INSERT INTO idtable(idname, pkid) values ('cdId', 0)");
            transactedExecuteSQL("INSERT INTO idtable(idname, pkid) values ('mediafileId', 0)");
        } catch (final Exception e) {
            LOG.error("initIdtable()", e);
        }
    }

    public void dropForeignKey(final String fkey) throws Exception {
        transactedExecuteSQL(_props.getProperty(DROP_FOREIGN_KEY + fkey));
    }

    public void dropTable(final String table) throws Exception {
        transactedExecuteSQL(_props.getProperty(DROP_TABLE + table));
    }

    public List<Exception> dropTables() {
        final List<Exception> exceptions = new ArrayList<Exception>();
        try {
            dropForeignKey(FK_COVERITEM_ALBUM);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            dropForeignKey(FK_MEDIAFILE_ALBUM);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            dropForeignKey(FK_MEDIAFILE_CD);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            dropTable(TB_CD);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            dropTable(TB_MEDIAFILE);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            dropTable(TB_ALBUM);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        try {
            dropTable(TB_COVERITEM);
        } catch (final Exception e) {
            exceptions.add(e);
        }
        removeIdtable();
        return exceptions;
    }

    public void modifyDatabase(final int baseVersion) throws DatabaseModifyException {
        if (_modifier != null) {
            _modifier.modifyDatabase(baseVersion);
        }
    }

    public int getDatabaseVersion() {
        final EntityManager em = GenericDAO.getEntityManager();
        int result;
        if (em != null) {
            try {
                final Query q = em.createNativeQuery("SELECT pkid FROM idtable WHERE idname = 'dbversion'");
                final List qres = q.getResultList();
                if (qres.size() == 1) {
                    final Object o = qres.get(0);
                    Number n;
                    if (o.getClass().isArray()) {
                        n = (Number) ((Object[]) o)[0];
                    } else if (o instanceof Collection) {
                        n = (Number) ((Collection) o).toArray()[0];
                    } else {
                        n = (Number) o;
                    }
                    result = n.intValue();
                } else {
                    result = 0;
                }
            } catch (final Exception e) {
                e.printStackTrace();
                result = getOlderDbVersion(em);
            }
            em.close();
        } else {
            result = 0;
        }
        return result;
    }

    private int getOlderDbVersion(final EntityManager em) {
        try {
            final Query q = em.createNativeQuery("SELECT id, title, artist, albumnr, cdnr, laenge, bitrate, groesse, path FROM mediafile WHERE id = 0");
            q.getResultList();
            return 1;
        } catch (final Exception e) {
            LOG.debug("getOlderDbVersion(EntityManager)", e);
        }
        try {
            final Query q = em.createNativeQuery("SELECT id, title, artist, albumnr, cdnr, playtime, bitrate, size, path FROM mediafile WHERE id = 0");
            q.getResultList();
            final Query q2 = em.createNativeQuery("SELECT aid, is_sampler, album, cover FROM album WHERE aid = 0");
            q2.getResultList();
            return 2;
        } catch (final Exception e) {
            LOG.debug("getOlderDbVersion(EntityManager)", e);
        }
        try {
            final Query q = em.createNativeQuery("SELECT id, title, artist, albumnr, cdnr, playtime, bitrate, size, path FROM mediafile WHERE id = 0");
            q.getResultList();
            final Query q2 = em.createNativeQuery("SELECT aid, is_sampler, album FROM album WHERE aid = 0");
            q2.getResultList();
            return 3;
        } catch (final Exception e) {
            LOG.debug("getOlderDbVersion(EntityManager)", e);
        }
        try {
            final Query q = em.createNativeQuery("SELECT mfid, title, artist, albumnr, cdnr, playtime, bitrate, filesize, path FROM mediafile WHERE mfid = 0");
            q.getResultList();
            final Query q2 = em.createNativeQuery("SELECT aid, album_type, album FROM album WHERE aid = 0");
            q2.getResultList();
            return 4;
        } catch (final Exception e) {
            LOG.debug("getOlderDbVersion(EntityManager)", e);
        }
        return 0;
    }

    public void transactedExecuteSQL(final String command) throws Exception {
        Transaction.startTransaction(this);
        try {
            final String[] sql = command.split(";");
            for (final String c : sql) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Execute: " + c);
                }
                _db.executeSQL(c);
            }
            Transaction.commitTransaction(this);
        } catch (final Exception e) {
            Transaction.rollbackTransation(this);
            throw e;
        }
    }
}
