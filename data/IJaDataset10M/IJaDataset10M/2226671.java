package org.opcda2out.output.database.upgrade.v2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.database.DatabaseManager;
import org.database.DbTable;
import org.opcda2out.exception.RecoverableInitializationException;
import org.opcda2out.output.database.nodes.PersistentCompositeNodeInfo;
import org.opcda2out.output.database.upgrade.DBConverter;
import org.opcda2out.output.database.upgrade.DBConvertionException;
import org.opcda2out.output.database.upgrade.v1.DBConverter_1_x;
import org.opcda2out.output.database.upgrade.v1.DatabaseStructureTransformerV1;
import org.opcda2out.output.database.upgrade.v1.DatabaseStructureTransformerV1.DatabaseSubVersion1;
import org.opcda2out.scripting.handlers.ScriptingEngineHandler;
import org.opcda2out.scripting.handlers.UnsupportedScriptingEngineHandler;
import org.openscada.opc.lib.common.ConnectionInformation;

/**
 *
 * @author Joao Leal
 */
public class DBConverter_2_0 extends DBConverter {

    private static MissingServerInfoProviderVerion_2_0 INFO_PROVIDER;

    private static final String CFG = "plantstreamer_cfg";

    private static final String CFG_ITEMS = "plantstreamer_cfg_item";

    private static final String CFG_COMP = "plantstreamer_cfg_comp";

    private static final String CFG_COMP_EL = "plantstreamer_cfg_comp_el";

    private static final String SERVER_POOL = "plantstreamer_server_pool";

    private static final String SERVERS = "plantstreamer_servers";

    private static final String VERSION = "plantstreamer_version";

    public static synchronized void setMissingInfoProvider(MissingServerInfoProviderVerion_2_0 infoProvider) {
        INFO_PROVIDER = infoProvider;
    }

    public static synchronized MissingServerInfoProviderVerion_2_0 getMissingInfoProvider() {
        return INFO_PROVIDER;
    }

    @Override
    protected void createTableStructure() {
        Map<String, Integer> columns = new HashMap<String, Integer>(3);
        columns.put("serv_id", Types.INTEGER);
        columns.put("description", Types.VARCHAR);
        columns.put("creation", Types.TIMESTAMP);
        addTable(SERVER_POOL, columns);
        columns = new HashMap<String, Integer>(7);
        columns.put("serv_id", Types.INTEGER);
        columns.put("pos", Types.INTEGER);
        columns.put("host", Types.VARCHAR);
        columns.put("domain", Types.VARCHAR);
        columns.put("progid", Types.VARCHAR);
        columns.put("username", Types.VARCHAR);
        columns.put("password", Types.VARCHAR);
        addTable(SERVERS, columns);
        columns = new HashMap<String, Integer>(6);
        columns.put("id", Types.INTEGER);
        columns.put("units", Types.VARCHAR);
        columns.put("description", Types.VARCHAR);
        columns.put("colname", Types.VARCHAR);
        columns.put("tablename", Types.VARCHAR);
        columns.put("save", Types.BOOLEAN);
        addTable(CFG, columns);
        columns = new HashMap<String, Integer>(5);
        columns.put("item_id", Types.INTEGER);
        columns.put("serv_id", Types.INTEGER);
        columns.put("opcid", Types.VARCHAR);
        columns.put("propertypath", Types.ARRAY);
        columns.put("arrayposition", Types.ARRAY);
        addTable(CFG_ITEMS, columns);
        columns = new HashMap<String, Integer>(6);
        columns.put("comp_id", Types.INTEGER);
        columns.put("serv_id", Types.INTEGER);
        columns.put("name", Types.VARCHAR);
        columns.put("language", Types.VARCHAR);
        columns.put("script", Types.VARCHAR);
        columns.put("datatype", Types.VARCHAR);
        addTable(CFG_COMP, columns);
        columns = new HashMap<String, Integer>(3);
        columns.put("comp_id", Types.INTEGER);
        columns.put("alias", Types.VARCHAR);
        columns.put("opcid", Types.VARCHAR);
        addTable(CFG_COMP_EL, columns);
        columns = new HashMap<String, Integer>(3);
        columns.put("t", Types.TIMESTAMP);
        columns.put("major", Types.INTEGER);
        columns.put("minor", Types.INTEGER);
        addTable(VERSION, columns);
    }

    @Override
    public void convertTo(DatabaseManager manager) throws SQLException, DBConvertionException {
        final Connection con = manager.getCon();
        final String schema = manager.conListInfo.getInfo().getSchema();
        String[] potentialTables = DBConverter_1_x.getUsedTableNames(con, schema);
        if (potentialTables.length == 0) {
            return;
        }
        final DatabaseStructureTransformerV1 trans = new DatabaseStructureTransformerV1();
        Map<String, DatabaseSubVersion1> tables = new HashMap<String, DatabaseSubVersion1>(potentialTables.length);
        try {
            for (String t : potentialTables) {
                DatabaseSubVersion1 v = trans.getTableStructureSubVersion(con, schema, t);
                if (v != null) {
                    tables.put(t, v);
                }
            }
        } catch (RecoverableInitializationException ex) {
            throw new DBConvertionException("Failed to determine the database structure version!", ex);
        }
        if (tables.isEmpty()) {
            return;
        }
        if (tables.size() == 1) {
            for (final Entry<String, DatabaseSubVersion1> e : tables.entrySet()) {
                final String table = e.getKey();
                trans.upgradeTable(con, table, e.getValue());
                upgradeSingleTable(con, table);
            }
        } else {
            MissingServerInfoProviderVerion_2_0 infoProvider = getMissingInfoProvider();
            if (infoProvider == null) {
                infoProvider = new CmdlnMissingInfoProviderVerion_2_0();
            }
            DbTable table2Move = infoProvider.getMultiTableUpgradeType(tables);
            if (table2Move == null) {
                upgradeMergeConfigurationTables(manager, trans, tables);
            } else {
                upgradeMoveTable(con, table2Move.getSchema(), table2Move.getName(), trans, tables.get(table2Move.getName()));
                manager.conListInfo.getInfo().setSchema(table2Move.getSchema());
                Logger.getLogger(DBConverter_2_0.class.getName()).log(Level.INFO, "The schema in the database connection information was automatically updated!");
            }
        }
    }

    private void createNewTables(Statement stmt) throws SQLException {
        stmt.execute("CREATE TABLE " + SERVER_POOL + "(" + "serv_id SERIAL PRIMARY KEY," + "description TEXT," + "creation TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP)");
        stmt.execute("CREATE TABLE " + SERVERS + "(" + "serv_id INTEGER REFERENCES " + SERVER_POOL + " (serv_id) ON UPDATE CASCADE NOT NULL," + "pos SERIAL NOT NULL," + "host TEXT NOT NULL DEFAULT 'localhost'," + "domain TEXT NOT NULL DEFAULT 'localhost'," + "progid TEXT NOT NULL," + "username TEXT," + "password TEXT," + "UNIQUE (serv_id, pos)," + "UNIQUE (host, domain, progid, username)," + "UNIQUE (serv_id, host, domain, progid)," + "PRIMARY KEY (serv_id, host, domain, progid, username))");
        stmt.execute("CREATE TABLE " + VERSION + "(" + "t TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP," + "major INTEGER NOT NULL," + "minor INTEGER NOT NULL," + " PRIMARY KEY (major, minor))");
    }

    private void upgradeSingleTable(Connection con, String table) throws SQLException, DBConvertionException {
        final String cfg = table + "_cfg";
        final String cfgItem = table + "_cfg_item";
        final String cfgComp = table + "_cfg_comp";
        final String cfgCompEl = table + "_cfg_comp_el";
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {
            stmt = con.createStatement();
            createNewTables(stmt);
            final Map<Integer, ConnectionInformation> serv_id2server = getMissingServerInfo(con, Collections.singletonList(table));
            stmt.execute("LOCK TABLE " + cfg);
            stmt.execute("LOCK TABLE " + cfgItem);
            stmt.execute("LOCK TABLE " + cfgComp);
            stmt.execute("LOCK TABLE " + cfgCompEl);
            stmt.execute("ALTER TABLE " + cfg + " RENAME TO " + CFG);
            stmt.execute("ALTER TABLE " + CFG + " ADD COLUMN id SERIAL NOT NULL");
            stmt.execute("ALTER TABLE " + CFG + " ADD COLUMN units TEXT");
            stmt.execute("ALTER TABLE " + CFG + " ADD COLUMN description TEXT");
            stmt.execute("ALTER TABLE " + CFG + " ALTER COLUMN tablename DROP NOT NULL");
            stmt.execute("ALTER TABLE " + CFG + " ADD UNIQUE (colname)");
            stmt.execute("ALTER TABLE " + CFG + " ADD UNIQUE (id, colname)");
            stmt.execute("ALTER TABLE " + CFG + " ADD CHECK (colname IS NULL = tablename IS NULL)");
            stmt.execute("UPDATE " + CFG + " SET units = c.units, description = c.description FROM " + cfgItem + " c WHERE c.colname = " + CFG + ".colname");
            stmt.execute("UPDATE " + CFG + " SET units = c.units, description = c.description FROM " + cfgComp + " c WHERE c.colname = " + CFG + ".colname");
            dropAllTableConstraints(con, cfgItem);
            stmt.execute("ALTER TABLE \"" + cfgItem + "\" DROP CONSTRAINT \"" + cfgItem + "_colname_fkey\"");
            stmt.execute("ALTER TABLE " + cfgItem + " RENAME TO " + CFG_ITEMS);
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " ADD COLUMN item_id INTEGER UNIQUE");
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " ADD COLUMN serv_id INTEGER");
            pstmt = con.prepareStatement("UPDATE " + CFG_ITEMS + " SET serv_id = ? WHERE server = ?");
            for (Entry<Integer, ConnectionInformation> e : serv_id2server.entrySet()) {
                pstmt.setInt(1, e.getKey());
                pstmt.setString(2, e.getValue().getProgId());
                pstmt.execute();
            }
            stmt.execute("UPDATE " + CFG_ITEMS + " SET item_id = c.id FROM " + CFG + " c WHERE " + CFG_ITEMS + ".colname = c.colname");
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " ALTER COLUMN item_id SET NOT NULL");
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " ALTER COLUMN serv_id SET NOT NULL");
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " DROP COLUMN colname");
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " DROP COLUMN server");
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " DROP COLUMN units");
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " DROP COLUMN description");
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " ADD PRIMARY KEY (serv_id, opcId, arrayposition)");
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " ADD FOREIGN KEY (serv_id) REFERENCES " + SERVER_POOL + " (serv_id) ON UPDATE CASCADE");
            dropAllTableConstraints(con, cfgCompEl);
            stmt.execute("ALTER TABLE \"" + cfgCompEl + "\" DROP CONSTRAINT \"" + cfgCompEl + "_colname_fkey\"");
            dropAllTableConstraints(con, cfgComp);
            stmt.execute("ALTER TABLE \"" + cfgComp + "\" DROP CONSTRAINT \"" + cfgComp + "_colname_fkey\"");
            stmt.execute("ALTER TABLE " + cfgComp + " RENAME TO " + CFG_COMP);
            stmt.execute("ALTER TABLE " + CFG_COMP + " ADD COLUMN comp_id INTEGER UNIQUE");
            stmt.execute("ALTER TABLE " + CFG_COMP + " ADD COLUMN serv_id INTEGER");
            pstmt = con.prepareStatement("UPDATE " + CFG_COMP + " SET serv_id = ? WHERE server = ?");
            for (Entry<Integer, ConnectionInformation> e : serv_id2server.entrySet()) {
                pstmt.setInt(1, e.getKey());
                pstmt.setString(2, e.getValue().getProgId());
                pstmt.execute();
            }
            stmt.execute("UPDATE " + CFG_COMP + " SET comp_id = c.id FROM " + CFG + " c WHERE " + CFG_COMP + ".colname = c.colname");
            stmt.execute("ALTER TABLE " + CFG_COMP + " ALTER COLUMN serv_id SET NOT NULL");
            stmt.execute("ALTER TABLE " + CFG_COMP + " ALTER COLUMN comp_id SET NOT NULL");
            stmt.execute("ALTER TABLE " + CFG_COMP + " DROP COLUMN server");
            stmt.execute("ALTER TABLE " + CFG_COMP + " DROP COLUMN units");
            stmt.execute("ALTER TABLE " + CFG_COMP + " DROP COLUMN description");
            stmt.execute("ALTER TABLE " + CFG_COMP + " ADD PRIMARY KEY (serv_id, name)");
            stmt.execute("ALTER TABLE " + CFG_COMP + " ADD FOREIGN KEY (serv_id) REFERENCES " + SERVER_POOL + " (serv_id) ON UPDATE CASCADE");
            stmt.execute("ALTER TABLE " + cfgCompEl + " RENAME TO " + CFG_COMP_EL);
            stmt.execute("ALTER TABLE " + CFG_COMP_EL + " ADD COLUMN comp_id INTEGER");
            stmt.execute("UPDATE " + CFG_COMP_EL + " SET comp_id = c.comp_id FROM " + CFG_COMP + " c WHERE " + CFG_COMP_EL + ".colname = c.colname");
            stmt.execute("ALTER TABLE " + CFG_COMP_EL + " ALTER COLUMN comp_id SET NOT NULL");
            stmt.execute("ALTER TABLE " + CFG_COMP_EL + " DROP COLUMN colname");
            stmt.execute("ALTER TABLE " + CFG_COMP_EL + " ADD PRIMARY KEY (comp_id, alias)");
            stmt.execute("ALTER TABLE " + CFG_COMP_EL + " ADD FOREIGN KEY (comp_id) REFERENCES " + CFG_COMP + " (comp_id) ON UPDATE CASCADE ON DELETE CASCADE");
            stmt.execute("ALTER TABLE " + CFG_COMP + " DROP COLUMN colname");
            String sql = "SELECT DISTINCT constraint_name FROM information_schema.constraint_column_usage" + " WHERE table_schema =  current_schema()" + " AND constraint_catalog = current_database()" + " AND table_name = '" + CFG + "' AND constraint_name ~ '_pkey'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            String pkey = rs.getString(1);
            rs.close();
            stmt.execute("ALTER TABLE \"" + CFG + "\" DROP CONSTRAINT \"" + pkey + "\"");
            stmt.execute("ALTER TABLE " + CFG + " ADD PRIMARY KEY (id)");
            stmt.execute("ALTER TABLE " + CFG + " ALTER COLUMN colname DROP NOT NULL");
            stmt.execute("ALTER TABLE " + CFG_ITEMS + " ADD FOREIGN KEY (item_id) REFERENCES " + CFG + " (id) ON UPDATE CASCADE ON DELETE CASCADE");
            stmt.execute("ALTER TABLE " + CFG_COMP + " ADD FOREIGN KEY (comp_id) REFERENCES " + CFG + " (id) ON UPDATE CASCADE ON DELETE CASCADE");
            stmt.execute("INSERT INTO " + VERSION + " (major, minor) VALUES (1,0)");
        } finally {
            DatabaseManager.closeStatements(stmt, pstmt);
        }
    }

    private void upgradeMoveTable(Connection con, String schema, String table, DatabaseStructureTransformerV1 trans, DatabaseSubVersion1 version) throws SQLException, DBConvertionException {
        final String cfg = table + "_cfg";
        final String cfgItem = table + "_cfg_item";
        final String cfgComp = table + "_cfg_comp";
        final String cfgCompEl = table + "_cfg_comp_el";
        trans.upgradeTable(con, table, version);
        version = DatabaseSubVersion1.v1_2;
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            schema = schema.replaceAll("\"", "\\\"");
            if (!DatabaseManager.getSchemas(con).contains(schema)) {
                stmt.execute("CREATE SCHEMA \"" + schema + "\"");
            }
            List<String> dataTables = new ArrayList<String>();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT tablename FROM " + cfg);
            while (rs.next()) {
                dataTables.add(rs.getString(1));
            }
            rs.close();
            stmt.execute("ALTER TABLE " + cfg + " SET SCHEMA \"" + schema + "\"");
            stmt.execute("ALTER TABLE " + cfgItem + " SET SCHEMA \"" + schema + "\"");
            stmt.execute("ALTER TABLE " + cfgComp + " SET SCHEMA \"" + schema + "\"");
            stmt.execute("ALTER TABLE " + cfgCompEl + " SET SCHEMA \"" + schema + "\"");
            for (String t : dataTables) {
                stmt.execute("ALTER TABLE " + t + " SET SCHEMA \"" + schema + "\"");
            }
            stmt.execute("SET search_path TO \"" + schema + "\"");
            upgradeSingleTable(con, table);
        } finally {
            DatabaseManager.closeStatement(stmt);
        }
    }

    private void upgradeMergeConfigurationTables(DatabaseManager manager, DatabaseStructureTransformerV1 trans, Map<String, DatabaseSubVersion1> tables) throws SQLException, DBConvertionException {
        final Connection con = manager.getCon();
        Statement stmt = null;
        PreparedStatement pstmt = null, pstmt2 = null;
        try {
            stmt = con.createStatement();
            List<String> tableList = new ArrayList<String>(tables.keySet());
            for (int i = 0; i < tableList.size(); i++) {
                for (int j = i + 1; j < tableList.size(); j++) {
                    String sql = "SELECT count(*) FROM " + tableList.get(i) + "_cfg_item t1 JOIN " + tableList.get(j) + "_cfg_item t2 ON" + " t1.server = t2.server" + " AND t1.opcid = t2.opcid" + " AND t1.arrayposition = t2.arrayposition";
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new DBConvertionException("The one or more OPC items are defined in several configuration tables. It is not possible to merge the tables.");
                    }
                }
            }
            createNewTables(stmt);
            stmt.execute("CREATE TABLE " + CFG + "(" + "id SERIAL UNIQUE NOT NULL," + "units TEXT," + "description TEXT," + "colname TEXT UNIQUE," + "tablename TEXT," + "save BOOLEAN NOT NULL," + " UNIQUE (id, colname)," + " PRIMARY KEY (id)," + " CHECK (colname IS NULL = tablename IS NULL))");
            stmt.execute("CREATE TABLE " + CFG_ITEMS + "(" + "item_id INTEGER UNIQUE NOT NULL," + "serv_id INTEGER REFERENCES " + SERVER_POOL + " (serv_id) ON UPDATE CASCADE NOT NULL," + "opcId TEXT NOT NULL," + "arrayPosition INTEGER[] NOT NULL," + "propertypath TEXT[]," + " PRIMARY KEY (serv_id, opcId, arrayposition)," + " FOREIGN KEY (item_id) REFERENCES " + CFG + " (id) ON DELETE CASCADE ON UPDATE CASCADE)");
            stmt.execute("CREATE TABLE " + CFG_COMP + "(" + "comp_id INTEGER UNIQUE NOT NULL," + "serv_id INTEGER REFERENCES " + SERVER_POOL + " (serv_id) ON UPDATE CASCADE NOT NULL," + "name TEXT NOT NULL," + "language TEXT NOT NULL," + "script TEXT NOT NULL," + "datatype TEXT NOT NULL," + " PRIMARY KEY (serv_id, name)," + " FOREIGN KEY (comp_id) REFERENCES " + CFG + " (id) ON DELETE CASCADE ON UPDATE CASCADE)");
            stmt.execute("CREATE TABLE " + CFG_COMP_EL + "(" + "comp_id INTEGER REFERENCES " + CFG_COMP + " (comp_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL," + "alias TEXT NOT NULL," + "opcId TEXT NOT NULL," + " PRIMARY KEY (comp_id, alias))");
            for (final Entry<String, DatabaseSubVersion1> e : tables.entrySet()) {
                trans.upgradeTable(con, e.getKey(), e.getValue());
            }
            Map<Integer, ConnectionInformation> serv_id2server = getMissingServerInfo(con, tables.keySet());
            stmt.execute("LOCK TABLE " + CFG);
            stmt.execute("LOCK TABLE " + CFG_ITEMS);
            stmt.execute("LOCK TABLE " + CFG_COMP);
            stmt.execute("LOCK TABLE " + CFG_COMP_EL);
            pstmt = con.prepareStatement("INSERT INTO " + CFG + " (colname, tablename, save) VALUES (?,?,?)");
            int colIndex = 0;
            for (String t : tables.keySet()) {
                Map<String, Set<String>> arrayCol2Tables = new HashMap<String, Set<String>>();
                Map<String, Map<String, String>> table2timeOld2newCol = new HashMap<String, Map<String, String>>();
                Map<String, Map<String, String>> table2dataOld2newCol = new HashMap<String, Map<String, String>>();
                final String cfg = t + "_cfg";
                final String cfgItem = t + "_cfg_item";
                final String cfgComp = t + "_cfg_comp";
                final String cfgCompEl = t + "_cfg_comp_el";
                ResultSet rs = stmt.executeQuery("SELECT colname, tablename, save FROM " + cfg);
                while (rs.next()) {
                    final String oldCol = rs.getString(1);
                    final String dataTable = rs.getString(2);
                    String newCol = "var" + colIndex++;
                    Map<String, String> oldTimeCol2new = table2timeOld2newCol.get(dataTable);
                    if (oldTimeCol2new == null) {
                        oldTimeCol2new = new HashMap<String, String>();
                        table2timeOld2newCol.put(dataTable, oldTimeCol2new);
                    }
                    Map<String, String> old2newCol = table2dataOld2newCol.get(dataTable);
                    if (old2newCol == null) {
                        old2newCol = new HashMap<String, String>();
                        table2dataOld2newCol.put(dataTable, old2newCol);
                    }
                    int pos = oldCol.indexOf("_");
                    if (pos != -1) {
                        String basecolname = oldCol.substring(0, pos);
                        Set<String> arrayTables = arrayCol2Tables.get(basecolname);
                        if (arrayTables == null || !arrayTables.contains(dataTable)) {
                            oldTimeCol2new.put("t_" + basecolname, "t_" + newCol);
                        }
                        newCol += oldCol.substring(pos, oldCol.length());
                        if (arrayTables == null) {
                            arrayTables = new HashSet<String>();
                            arrayCol2Tables.put(basecolname, arrayTables);
                        }
                        arrayTables.add(dataTable);
                    } else {
                        oldTimeCol2new.put("t_" + oldCol, "t_" + newCol);
                    }
                    old2newCol.put(oldCol, newCol);
                    pstmt.setString(1, newCol);
                    pstmt.setString(2, dataTable);
                    pstmt.setBoolean(3, rs.getBoolean(3));
                    pstmt.execute();
                }
                rs.close();
                for (Entry<String, Map<String, String>> e : table2timeOld2newCol.entrySet()) {
                    final String dataTable = e.getKey();
                    for (String oldCol : e.getValue().keySet()) {
                        stmt.execute("ALTER TABLE " + dataTable + " RENAME " + oldCol + " TO ___" + oldCol);
                    }
                    for (Entry<String, String> old2newCol : e.getValue().entrySet()) {
                        final String oldCol = old2newCol.getKey();
                        stmt.execute("ALTER TABLE " + dataTable + " RENAME ___" + oldCol + " TO " + old2newCol.getValue());
                    }
                }
                for (Entry<String, Map<String, String>> e : table2dataOld2newCol.entrySet()) {
                    final String dataTable = e.getKey();
                    for (String oldCol : e.getValue().keySet()) {
                        stmt.execute("ALTER TABLE " + dataTable + " RENAME " + oldCol + " TO ___" + oldCol);
                    }
                    for (Entry<String, String> old2newCol : e.getValue().entrySet()) {
                        final String oldCol = old2newCol.getKey();
                        stmt.execute("ALTER TABLE " + dataTable + " RENAME ___" + oldCol + " TO " + old2newCol.getValue());
                    }
                }
                stmt.execute("UPDATE " + cfg + " SET colname = '__' || colname");
                pstmt2 = con.prepareStatement("UPDATE " + cfg + " SET colname = ? WHERE colname = ?");
                for (Map<String, String> old2new : table2dataOld2newCol.values()) {
                    for (Entry<String, String> e : old2new.entrySet()) {
                        pstmt2.setString(1, e.getValue());
                        pstmt2.setString(2, "__" + e.getKey());
                        pstmt2.execute();
                    }
                }
                manipulateItemTable(con, cfgItem, serv_id2server);
                manipulateCompositeTable(con, cfgComp, cfgCompEl, serv_id2server);
            }
            pstmt.close();
            Map<Integer, Set<String>> serv_id2CompNames = getUsedCompositeNodeNames(con, tables.keySet());
            int count = 0;
            for (String t : tables.keySet()) {
                final String cfg = t + "_cfg";
                final String cfgItem = t + "_cfg_item";
                final String cfgComp = t + "_cfg_comp";
                final String cfgCompEl = t + "_cfg_comp_el";
                stmt.execute("UPDATE " + CFG + " SET units = c.units, description = c.description FROM " + cfgItem + " c WHERE c.colname = " + CFG + ".colname");
                stmt.execute("UPDATE " + CFG + " SET units = c.units, description = c.description FROM " + cfgComp + " c WHERE c.colname = " + CFG + ".colname");
                stmt.execute("INSERT INTO " + CFG_ITEMS + " (item_id, serv_id, opcId, arrayPosition, propertypath) " + " SELECT item_id, serv_id, opcId, arrayPosition, propertypath FROM " + cfgItem);
                handleCompositeNodeNameConflict(con, t, serv_id2server, serv_id2CompNames);
                stmt.execute("INSERT INTO " + CFG_COMP + " (comp_id, serv_id, name, language, script, datatype) " + " SELECT comp_id, serv_id, name, language, script, datatype FROM " + cfgComp);
                stmt.execute("INSERT INTO " + CFG_COMP_EL + " (comp_id ,alias, opcId) " + " SELECT comp_id, alias, opcId FROM " + cfgCompEl);
                stmt.execute("DROP TABLE " + cfgCompEl);
                stmt.execute("DROP TABLE " + cfgComp);
                stmt.execute("DROP TABLE " + cfgItem);
                stmt.execute("DROP TABLE " + cfg);
            }
            stmt.execute("INSERT INTO " + VERSION + " (major, minor) VALUES (1,0)");
        } finally {
            DatabaseManager.closeStatements(stmt, pstmt, pstmt2);
        }
    }

    private void manipulateItemTable(Connection con, String cfgItem, Map<Integer, ConnectionInformation> serv_id2server) throws SQLException {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute("ALTER TABLE " + cfgItem + " ADD COLUMN item_id INTEGER");
            stmt.execute("ALTER TABLE " + cfgItem + " ADD COLUMN serv_id INTEGER");
            pstmt = con.prepareStatement("UPDATE " + cfgItem + " SET serv_id = ? WHERE server = ?");
            for (Entry<Integer, ConnectionInformation> e : serv_id2server.entrySet()) {
                pstmt.setInt(1, e.getKey());
                pstmt.setString(2, e.getValue().getProgId());
                pstmt.execute();
            }
            stmt.execute("UPDATE " + cfgItem + " SET item_id = c.id FROM " + CFG + " c WHERE " + cfgItem + ".colname = c.colname");
            dropAllTableConstraints(con, cfgItem);
            stmt.execute("ALTER TABLE " + cfgItem + " DROP COLUMN server");
            stmt.execute("ALTER TABLE " + cfgItem + " ADD PRIMARY KEY (serv_id, opcId, arrayposition)");
            stmt.execute("ALTER TABLE " + cfgItem + " ALTER COLUMN serv_id SET NOT NULL");
            stmt.execute("ALTER TABLE " + cfgItem + " ALTER COLUMN item_id SET NOT NULL");
            stmt.execute("ALTER TABLE " + cfgItem + " ADD UNIQUE (item_id)");
        } finally {
            DatabaseManager.closeStatements(stmt, pstmt);
        }
    }

    private void manipulateCompositeTable(Connection con, String cfgComp, String cfgCompElTable, Map<Integer, ConnectionInformation> serv_id2server) throws SQLException {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute("ALTER TABLE " + cfgComp + " ADD COLUMN serv_id INTEGER");
            stmt.execute("ALTER TABLE " + cfgComp + " ADD COLUMN comp_id INTEGER");
            pstmt = con.prepareStatement("UPDATE " + cfgComp + " SET serv_id = ? WHERE server = ?");
            for (Entry<Integer, ConnectionInformation> e : serv_id2server.entrySet()) {
                pstmt.setInt(1, e.getKey());
                pstmt.setString(2, e.getValue().getProgId());
                pstmt.execute();
            }
            stmt.execute("UPDATE " + cfgComp + " SET comp_id = c.id FROM " + CFG + " c WHERE " + cfgComp + ".colname = c.colname");
            dropAllTableConstraints(con, cfgCompElTable);
            stmt.execute("ALTER TABLE " + cfgCompElTable + " DROP CONSTRAINT " + cfgCompElTable + "_colname_fkey");
            dropAllTableConstraints(con, cfgComp);
            stmt.execute("ALTER TABLE " + cfgComp + " ALTER COLUMN serv_id SET NOT NULL");
            stmt.execute("ALTER TABLE " + cfgComp + " ALTER COLUMN comp_id SET NOT NULL");
            stmt.execute("ALTER TABLE " + cfgComp + " ADD UNIQUE (comp_id)");
            stmt.execute("ALTER TABLE " + cfgComp + " DROP COLUMN server");
            stmt.execute("ALTER TABLE " + cfgComp + " ADD PRIMARY KEY (serv_id, name)");
            stmt.execute("ALTER TABLE " + cfgCompElTable + " ADD COLUMN comp_id INTEGER");
            stmt.execute("UPDATE " + cfgCompElTable + " SET comp_id = c.comp_id FROM " + cfgComp + " c WHERE " + cfgCompElTable + ".colname = c.colname");
            stmt.execute("ALTER TABLE " + cfgCompElTable + " ALTER COLUMN comp_id SET NOT NULL");
            stmt.execute("ALTER TABLE " + cfgCompElTable + " ADD FOREIGN KEY (comp_id) REFERENCES " + cfgComp + " (comp_id) ON UPDATE CASCADE ON DELETE SET NULL");
            stmt.execute("ALTER TABLE " + cfgCompElTable + " DROP COLUMN colname");
        } finally {
            DatabaseManager.closeStatements(stmt, pstmt);
        }
    }

    public static Map<Integer, Set<String>> getUsedCompositeNodeNames(Connection con, Set<String> tables) throws SQLException {
        Map<Integer, Set<String>> serv_id2CompNames = new HashMap<Integer, Set<String>>();
        StringBuilder sql = new StringBuilder();
        int i = 0;
        for (String t : tables) {
            if (i > 0) {
                sql.append(" UNION ");
            }
            sql.append("SELECT DISTINCT serv_id, name FROM ").append(t).append("_cfg_comp");
            i++;
        }
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                int serv_id = rs.getInt(1);
                String name = rs.getString(2);
                Set<String> names = serv_id2CompNames.get(serv_id);
                if (names == null) {
                    names = new HashSet<String>();
                    serv_id2CompNames.put(serv_id, names);
                }
                names.add(name);
            }
            rs.close();
        } finally {
            DatabaseManager.closeStatement(stmt);
        }
        return serv_id2CompNames;
    }

    private static void dropAllTableConstraints(Connection con, String table) throws SQLException {
        PreparedStatement pstmt = null;
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            pstmt = con.prepareStatement("SELECT DISTINCT constraint_name FROM information_schema.constraint_column_usage" + " WHERE table_schema = current_schema()" + " AND constraint_catalog = current_database()" + " AND table_name = ?");
            pstmt.setString(1, table);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                stmt.execute("ALTER TABLE \"" + table + "\" DROP CONSTRAINT \"" + rs.getString(1) + "\"");
            }
        } finally {
            DatabaseManager.closeStatements(stmt, pstmt);
        }
    }

    private Map<Integer, ConnectionInformation> getMissingServerInfo(Connection con, Collection<String> tableBaseNames) throws SQLException, DBConvertionException {
        final Set<String> progIds = new HashSet<String>();
        Map<Integer, ConnectionInformation> serv_id2server;
        StringBuilder sql = new StringBuilder();
        int i = 0;
        for (String t : tableBaseNames) {
            if (i > 0) {
                sql.append(" UNION ");
            }
            sql.append("SELECT DISTINCT server FROM ").append(t).append("_cfg_item");
            sql.append(" UNION SELECT DISTINCT server FROM ").append(t).append("_cfg_comp");
            i++;
        }
        PreparedStatement pstmt = null;
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                progIds.add(rs.getString(1));
            }
            rs.close();
            MissingServerInfoProviderVerion_2_0 infoProvider = getMissingInfoProvider();
            if (infoProvider == null) {
                infoProvider = new CmdlnMissingInfoProviderVerion_2_0();
            }
            serv_id2server = new HashMap<Integer, ConnectionInformation>(progIds.size());
            Map<String, ConnectionInformation> data = infoProvider.getServerInfo(progIds);
            if (data == null || data.size() != progIds.size()) {
                throw new DBConvertionException("Some servers do not have the required connection information");
            }
            for (String server : progIds) {
                if (data.get(server) == null) {
                    throw new DBConvertionException("There is no connection information for the server " + server);
                }
            }
            pstmt = stmt.getConnection().prepareStatement("INSERT INTO " + SERVERS + " (serv_id, host, domain, progid, username, password) VALUES (?,?,?,?,?,?)");
            for (String progId : progIds) {
                rs = stmt.executeQuery("INSERT INTO " + SERVER_POOL + " (creation) VALUES (CURRENT_TIMESTAMP) RETURNING serv_id");
                rs.next();
                final int serv_id = rs.getInt(1);
                rs.close();
                ConnectionInformation info = data.get(progId);
                pstmt.setInt(1, serv_id);
                pstmt.setString(2, info.getHost());
                pstmt.setString(3, info.getDomain());
                pstmt.setString(4, progId);
                if (info.getUser() != null) {
                    pstmt.setString(5, info.getUser());
                } else {
                    pstmt.setNull(5, Types.VARCHAR);
                }
                if (info.getPassword() != null) {
                    pstmt.setString(6, info.getPassword());
                } else {
                    pstmt.setNull(6, Types.VARCHAR);
                }
                pstmt.execute();
                serv_id2server.put(serv_id, info);
            }
        } finally {
            DatabaseManager.closeStatement(pstmt);
        }
        return serv_id2server;
    }

    private void handleCompositeNodeNameConflict(final Connection con, final String tableName, Map<Integer, ConnectionInformation> serv_id2server, Map<Integer, Set<String>> serv_id2CompNames) throws SQLException, DBConvertionException {
        final String cfgComp = tableName + "_cfg_comp";
        final String cfgCompEl = tableName + "_cfg_comp_el";
        Statement stmt = null;
        Map<Integer, Map<Integer, PersistentCompositeNodeInfo>> serv2conflictComp = new HashMap<Integer, Map<Integer, PersistentCompositeNodeInfo>>();
        PreparedStatement pstmt = null, pstmtEl = null;
        try {
            stmt = con.createStatement();
            pstmtEl = con.prepareStatement("SELECT alias, opcid FROM " + cfgCompEl + " WHERE comp_id = ? ");
            final ResultSet rs = stmt.executeQuery("SELECT o.serv_id, o.comp_id, o.name, o.language, o.script, o.datatype FROM " + CFG_COMP + " n" + " JOIN " + cfgComp + " o ON o.name = n.name AND o.serv_id = n.serv_id ORDER BY o.serv_id, o.name");
            while (rs.next()) {
                final PersistentCompositeNodeInfo comp = loadCompositeInfo(rs, pstmtEl);
                final int serv_id = rs.getInt(1);
                final int comp_id = rs.getInt(2);
                Map<Integer, PersistentCompositeNodeInfo> compId2Comp = serv2conflictComp.get(serv_id);
                if (compId2Comp == null) {
                    compId2Comp = new HashMap<Integer, PersistentCompositeNodeInfo>();
                    serv2conflictComp.put(serv_id, compId2Comp);
                }
                compId2Comp.put(comp_id, comp);
            }
            if (serv2conflictComp.isEmpty()) {
                return;
            }
            MissingServerInfoProviderVerion_2_0 infoProvider = getMissingInfoProvider();
            if (infoProvider == null) {
                infoProvider = new CmdlnMissingInfoProviderVerion_2_0();
            }
            Map<Integer, String> comp_id2NewName = infoProvider.getNewCompositeNodeNames(serv2conflictComp, tableName, serv_id2server, serv_id2CompNames);
            pstmt = con.prepareStatement("UPDATE " + cfgComp + " SET name = ? WHERE comp_id = ?");
            for (Entry<Integer, String> e : comp_id2NewName.entrySet()) {
                pstmt.setString(1, e.getValue());
                pstmt.setInt(2, e.getKey());
                pstmt.execute();
            }
        } finally {
            DatabaseManager.closeStatements(stmt, pstmtEl);
        }
    }

    private PersistentCompositeNodeInfo loadCompositeInfo(ResultSet rs, PreparedStatement pstmtEl) throws SQLException, DBConvertionException {
        final int comp_id = rs.getInt(2);
        final String name = rs.getString(3);
        final String lang = rs.getString(4);
        final String script = rs.getString(5);
        final String type = rs.getString(6);
        final ScriptingEngineHandler engineHandler = new UnsupportedScriptingEngineHandler(lang);
        final Class dataType = PersistentCompositeNodeInfo.parseDataType(type);
        if (dataType == null) {
            throw new DBConvertionException(MessageFormat.format("The_composite_item_{0}_has_an_invalid_datatype_{1}!", name, type));
        }
        final Map<String, String> alias2id = new HashMap<String, String>();
        pstmtEl.setInt(1, comp_id);
        ResultSet rsEl = pstmtEl.executeQuery();
        while (rsEl.next()) {
            final String alias = rsEl.getString(1);
            final String opcid = rsEl.getString(2);
            alias2id.put(alias, opcid);
        }
        rsEl.close();
        return new PersistentCompositeNodeInfo(name, true, alias2id, script, engineHandler, dataType);
    }
}
