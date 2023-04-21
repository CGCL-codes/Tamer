package com.explosion.datastream.processes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.explosion.datastream.gui.EXQLBaseTool;
import com.explosion.datastream.gui.dbbrowser.tree.ExqlTreeNode;
import com.explosion.expfmodules.rdbmsconn.dbom.DBEntity;
import com.explosion.expfmodules.rdbmsconn.dbom.utils.MetadataUtils;
import com.explosion.utilities.process.StackableSimpleProcess;
import com.explosion.utilities.process.threads.Finishable;

/**
 * @author Stephen Cowx
 * Date created:@14-Feb-2003
 */
public class RefreshCatalogsAndSchemasProcess extends StackableSimpleProcess {

    private static Logger log = LogManager.getLogger(RefreshCatalogsAndSchemasProcess.class);

    private Connection conn;

    private EXQLBaseTool tool;

    private ExqlTreeNode rootNode;

    /**
   * Constructor for GetDBCatalogs.
   * @param finishable
   */
    public RefreshCatalogsAndSchemasProcess(Finishable finishable, StackableSimpleProcess parentProcess, Connection conn, ExqlTreeNode rootNode, EXQLBaseTool tool) {
        super(finishable, parentProcess);
        this.conn = conn;
        this.tool = tool;
        this.rootNode = rootNode;
        this.setIsUserProcess(true);
    }

    public static void refresh(Connection conn, StackableSimpleProcess parentProcess, EXQLBaseTool tool, ExqlTreeNode rootNode) throws Exception {
        RefreshCatalogsAndSchemasProcess action = new RefreshCatalogsAndSchemasProcess(null, parentProcess, conn, rootNode, tool);
        rootNode.removeAllChildren();
        action.process();
    }

    /**
   * @see com.explosion.utilities.process.threads.SimpleProcess#process()
   */
    public void process() throws Exception {
        if (isStopped()) return;
        String schemaTerm = MetadataUtils.getDBMD(conn).getSchemaTerm();
        String catalogTerm = MetadataUtils.getDBMD(conn).getCatalogTerm();
        ResultSet set = null;
        ResultSet set2 = null;
        try {
            log.debug("About to fetch " + catalogTerm + "s.");
            set = MetadataUtils.getDBMD(conn).getCatalogs();
            if (isStopped()) return;
            addCatalogNodes(set, catalogTerm);
            log.debug("Finished fetching " + catalogTerm + "s.");
            log.debug("About to fetch " + schemaTerm + "s.");
            set2 = MetadataUtils.getDBMD(conn).getSchemas();
            if (isStopped()) return;
            addSchemaNodes(set2, schemaTerm);
        } catch (SQLException e) {
            log.debug("Resultset MetaData getCatalogs() has not been implemented.");
        } finally {
            try {
                if (set != null) set.close();
            } catch (Exception e) {
            }
            try {
                if (set2 != null) set2.close();
            } catch (Exception e) {
            }
        }
    }

    private void addSchemaNodes(ResultSet set, String schemaTerm) throws SQLException {
        while (set.next()) {
            if (isStopped()) return;
            String name = set.getString(1);
            log.debug("Adding " + schemaTerm + ": " + name);
            setStatusText("Adding " + schemaTerm + ": " + name);
            DBEntity schemaDescriptor = new DBEntity(null, name, name, DBEntity.TYPE_SCHEMA);
            ExqlTreeNode schemaNode = new ExqlTreeNode(schemaDescriptor);
            rootNode.add(schemaNode);
        }
    }

    public void addCatalogNodes(ResultSet set, String catalogTerm) throws SQLException {
        while (set.next()) {
            if (isStopped()) return;
            String name = set.getString(1);
            log.debug("Adding " + catalogTerm + ": " + name);
            DBEntity catalogDescriptor = new DBEntity(name, null, name, DBEntity.TYPE_CATALOG);
            setStatusText("Adding " + catalogTerm + ": " + name);
            ExqlTreeNode catalogNode = new ExqlTreeNode(catalogDescriptor);
            rootNode.add(catalogNode);
        }
    }
}
