package org.fpdev.apps.rtemaster;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;
import org.fpdev.core.FPEngine;
import org.fpdev.core.data.LegacyDB;
import org.fpdev.core.data.LegacyDerbyDB;
import org.fpdev.core.data.LegacyMysqlDB;
import org.fpdev.core.basenet.BLink;
import org.fpdev.core.basenet.BLinkStreet;
import org.fpdev.core.basenet.BNode;
import org.fpdev.core.transit.Route;
import org.fpdev.core.transit.Station;
import org.fpdev.core.transit.SubRoute;

/**
 * The class that defines most database operations avialable to users in the
 * administration client. Many of these actions were created to facilitate 
 * migration from earlier db table schemas to newer ones, and are generally
 * not intended for day-to-day use. The actions typically <i>cannot</i> be 
 * undone.
 * 
 * <p>Most of the database operations are table-specific. These typically fall 
 * into one of four categories:
 * <ul>
 * <li>"<b>Create</b>" operations -- simply creates the specified table(s) in
 * the database with no attempt to populate table data
 * <li>"<b>Init</b>" operations -- creates and populates specified table(s), 
 * usually based on data currently in memory loaded through other means (e.g., 
 * links or nodes read from an external shapefile)
 * <li>"<b>Clear</b>" operations -- empties all records from the specified 
 * table(s)
 * <li>"<b>Drop</b>" operations -- drops the table(s), including all records, 
 * from the database completely.
 * </ul>
 * Note that the above actions have generally been added on an "as needed" 
 * basis; i.e. which of the four actions (if any) are provided for a particular
 * table will vary.
 * 
 * <p>In addition, some general database utilities are provided (e.g. migrating
 * between RDBSM platforms).
 * @author demory
 */
public class DatabaseOps {

    private RouteMaster ac_;

    private LegacyDB legDB_;

    /** Creates a new instance of DatabaseOps */
    public DatabaseOps(RouteMaster ac) {
        ac_ = ac;
        legDB_ = ac.getEngine().getDB();
    }

    public static boolean isDatabaseEvent(RMEvent e) {
        return e.getType() / 100 % 10 == 8;
    }

    public void handleDatabaseEvent(RMEvent e) {
        switch(e.getType()) {
            case EventTypes.DBMENU_CREATE_LOG_TABLES:
                ac_.getEngine().getDataPackage().getCoreDB().createQueryLogTables();
                ac_.msg("Created query log tables");
                break;
            case EventTypes.DBMENU_DROP_LOG_TABLES:
                ac_.getEngine().getDataPackage().getCoreDB().dropTable("tripqueries");
                ac_.getEngine().getDataPackage().getCoreDB().dropTable("locqueries");
                ac_.msg("Dropped query log tables");
                break;
            case EventTypes.DBMENU_CREATE_TRIPLINKS_TABLE:
                ac_.getEngine().getDataPackage().getCoreDB().createTripLinksTable();
                ac_.msg("Created trip links tables");
                break;
            case EventTypes.DBMENU_DROP_TRIPLINKS_TABLE:
                ac_.getEngine().getDataPackage().getCoreDB().dropTable("triplinks");
                ac_.msg("Dropped trip links table");
                break;
            case EventTypes.DBMENU_MIGRATE:
                migrateDB();
                break;
            case EventTypes.DBMENU_DELETE_SINGLE_LINK:
                String idStr = JOptionPane.showInputDialog("id:");
                if (idStr != null) {
                    legDB_.deleteLink(new Integer(idStr).intValue());
                    ac_.msg("deleted link");
                }
                break;
            case EventTypes.DBMENU_INIT_BASENET_TABLES:
                initBaseNetTables();
                break;
            case EventTypes.DBMENU_CLEAR_BASENET_TABLES:
                clearBaseNetTables();
                break;
            case EventTypes.DBMENU_INIT_NODES_TABLE:
                initNodesTable();
                break;
            case EventTypes.DBMENU_INIT_LINKS_TABLE:
                initLinksTable();
                break;
            case EventTypes.DBMENU_CLEAR_LINKS_TABLE:
                clearLinksTable();
                break;
            case EventTypes.DBMENU_INIT_ADDR_TABLE:
                initAddrTable();
                break;
            case EventTypes.DBMENU_INIT_ISECT_TABLE:
                initIsectTable();
                break;
            case EventTypes.DBMENU_CLEAR_ISECT_TABLE:
                clearIsectTable();
                break;
            case EventTypes.DBMENU_CLEAR_LANDMARKS_TABLE:
                clearLandmarksTable();
                break;
            case EventTypes.DBMENU_INIT_ELEV_TABLE:
                legDB_.createElevationTable();
                ac_.msg("Created elevation table");
                int c = 0;
                for (BLink link : ac_.getEngine().getBaseNet().getLinks()) {
                    if (link.getElevArray() != null && link.getElevArray().length > 0) {
                        legDB_.initElevInfo(link);
                        c++;
                        if (c % 1000 == 0) {
                            System.out.println("added elev info for " + c + "links");
                        }
                    }
                }
                System.out.println("added elev info for " + c + "links total");
                break;
            case EventTypes.DBMENU_DROP_ELEV_TABLE:
                legDB_.dropTable("elevation");
                ac_.msg("Dropped elevation table");
                break;
            case EventTypes.DBMENU_INIT_SCENLINKTYPES_TABLE:
                legDB_.createScenLinkTypesTable();
                ac_.msg("Created scenario-specific link types table");
                break;
            case EventTypes.DBMENU_DROP_SCENLINKTYPES_TABLE:
                legDB_.dropTable("scenlinktypes");
                ac_.msg("Dropped scenario-specific link types table");
                break;
            case EventTypes.DBMENU_CREATE_PING_TABLE:
                legDB_.createPingTable();
                ac_.msg("Created ping table");
                break;
            case EventTypes.DBMENU_DROP_PING_TABLE:
                legDB_.dropTable("ping");
                ac_.msg("Dropped ping table");
                break;
        }
    }

    public void initBaseNetTables() {
        initNodesTable();
        initLinksTable();
    }

    public void initNodesTable() {
        int i = 0;
        legDB_.dropTable("nodes");
        legDB_.createNodesTable();
        legDB_.setActiveTable("nodes");
        for (BNode node : ac_.getEngine().getBaseNet().getNodes()) {
            legDB_.initNode(node);
            i++;
            if (i % 1000 == 0) {
                System.out.println(i + " nodes processed");
            }
        }
        legDB_.flushInsertOps();
        System.out.println(i + " total nodes processed");
    }

    public void initLinksTable() {
        int i = 0;
        legDB_.setActiveTable("links");
        for (BLink link : ac_.getEngine().getBaseNet().getLinks()) {
            Integer id = new Integer(link.getID());
            legDB_.initLink(link);
            i++;
            if (i % 1000 == 0) {
                System.out.println(i + " links processed");
            }
        }
        legDB_.flushInsertOps();
    }

    public void clearBaseNetTables() {
        legDB_.clearBaseNetTables();
        System.out.println("Tables Cleared");
    }

    public void clearLinksTable() {
        legDB_.dropTable("links");
        legDB_.createLinksTable();
        System.out.println("Links Table Cleared");
    }

    public void initAddrTable() {
        System.out.println("Init DB Addr table");
        legDB_.dropTable("addrranges");
        legDB_.createAddrTable();
        legDB_.setActiveTable("addrranges");
        int i = 0;
        for (BLink link : ac_.getEngine().getBaseNet().getLinks()) {
            Integer id = new Integer(link.getID());
            if (link.getClassType() == BLink.CLASS_STREET) {
                legDB_.initAddrRange((BLinkStreet) link);
            }
            i++;
            if (i % 1000 == 0) {
                System.out.println(i + " links addr ranges processed");
            }
        }
        legDB_.flushInsertOps();
    }

    public void initIsectTable() {
        System.out.println("Init DB Addr table");
        legDB_.setActiveTable("isectkeys");
        ac_.getEngine().getLocations().buildIsectTable();
    }

    public void clearIsectTable() {
        legDB_.dropTable("isectkeys");
        legDB_.createIsectTable();
        System.out.println("IsectTable Table Cleared");
    }

    public void clearLandmarksTable() {
        legDB_.dropTable("landmarks");
        legDB_.createLandmarksTable();
        System.out.println("Landmarks Table Cleared");
    }

    public void initLinksOnRteNet() {
        Set<Integer> linkIDs = new HashSet<Integer>();
        Set<Integer> nodeIDs = new HashSet<Integer>();
        for (Route rte : ac_.getEngine().getRoutes().getCollection()) {
            for (SubRoute sub : rte.getSubRoutes()) {
                Iterator<BLink> links = sub.getPath().getLinks();
                while (links.hasNext()) {
                    BLink link = links.next();
                    linkIDs.add(link.getID());
                    nodeIDs.add(link.getFNodeID());
                    nodeIDs.add(link.getTNodeID());
                }
            }
        }
        for (Station sta : ac_.getEngine().getStations().getCollection()) {
            for (BNode snode : sta.getNodes()) nodeIDs.add(snode.getID());
            for (BLink link : sta.getLinks()) {
                linkIDs.add(link.getID());
                nodeIDs.add(link.getFNodeID());
                nodeIDs.add(link.getTNodeID());
            }
        }
        legDB_.initOnRteNet("nodes", nodeIDs.iterator());
        legDB_.initOnRteNet("links", linkIDs.iterator());
    }

    public void migrateDB() {
        if (legDB_.getRDBMS() == LegacyDB.DB_DERBY) {
            migrateDBToMySQL();
        } else {
            migrateDBToDerby();
        }
    }

    public void migrateDBToMySQL() {
        if (legDB_.getRDBMS() == LegacyDB.DB_MYSQL) {
            ac_.msg("Already using MySQL!");
            return;
        }
        ac_.msg("Migrating DB to MySQL");
        FPEngine engine = ac_.getEngine();
        String user = JOptionPane.showInputDialog("Username:");
        if (user == null) return;
        String pw = JOptionPane.showInputDialog("Password:");
        if (pw == null) return;
        LegacyDB mysql = new LegacyMysqlDB(engine, engine.getDataPackage(), user, pw);
        if (mysql.initialized()) {
            writeNewDB(mysql);
            System.out.println("Closed MySQL database");
        } else ac_.msg("Error: Could not initialize MySQL database");
    }

    public void migrateDBToDerby() {
        if (legDB_.getRDBMS() == LegacyDB.DB_DERBY) {
            ac_.msg("Already using Derby!");
            return;
        }
        ac_.msg("Migrating DB to Derby");
        LegacyDB derby = new LegacyDerbyDB(ac_.getEngine(), ac_.getEngine().getDataPackage());
        if (derby.initialized()) {
            writeNewDB(derby);
            System.out.println("Closed Derby database");
        } else ac_.msg("Error: Could not initialize Derby database");
    }

    public void writeNewDB(LegacyDB newDB) {
        newDB.clearBaseNetTables();
        System.out.println("Cleared tables");
        try {
            System.out.println("Processing landmarks..");
            newDB.setActiveTable("landmarks");
            ResultSet rs = legDB_.getAllRows("landmarks");
            int i = 0;
            while (rs.next()) {
                i++;
                if (i % 10000 == 0) {
                    System.out.println(i + " landmarks processed");
                }
                String name = rs.getString(1);
                String loc = rs.getString(2);
                String altnames = rs.getString(3);
                newDB.initLandmark(name, loc, altnames);
            }
            if (i > 0) {
                newDB.flushInsertOps();
            }
            rs.close();
            System.out.println("Finished processing " + i + " landmarks");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Processing nodes..");
            newDB.setActiveTable("nodes");
            ResultSet rs = legDB_.getAllNodes();
            int i = 0;
            while (rs.next()) {
                i++;
                if (i % 10000 == 0) {
                    System.out.println(i + " nodes processed");
                }
                newDB.initNode(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getShort(5), rs.getShort(6));
            }
            newDB.flushInsertOps();
            rs.close();
            System.out.println("Finished processing " + i + " nodes");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Processing links..");
            newDB.setActiveTable("links");
            ResultSet rs = legDB_.getAllLinks();
            int i = 0;
            while (rs.next()) {
                i++;
                if (i % 10000 == 0) {
                    System.out.println(i + " links processed");
                }
                int id = rs.getInt(1);
                int scenID = rs.getInt(2);
                int type = rs.getInt(3);
                int fNodeID = rs.getInt(4);
                int tNodeID = rs.getInt(5);
                String name = rs.getString(6);
                int len = (int) rs.getDouble(7);
                String tiles = rs.getString(8);
                short onRteNet = rs.getShort(9);
                int bbx1 = rs.getInt(10);
                int bby1 = rs.getInt(11);
                int bbx2 = rs.getInt(12);
                int bby2 = rs.getInt(13);
                newDB.initLink(id, scenID, type, fNodeID, tNodeID, name, len, tiles, onRteNet, bbx1, bby1, bbx2, bby2);
            }
            newDB.flushInsertOps();
            rs.close();
            System.out.println("Finished processing " + i + " links");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Processing shapepoints..");
            newDB.setActiveBlobTable("shapepoints");
            ResultSet rs = legDB_.getAllRows("shapepoints");
            int i = 0;
            while (rs.next()) {
                i++;
                if (i % 10000 == 0) {
                    System.out.println(i + " shppt blobs processed");
                }
                int id = rs.getInt(1);
                java.sql.Blob blob = rs.getBlob(2);
                newDB.initSPBlob(id, blob.getBytes(1, (int) blob.length()));
            }
            newDB.flushBlobInsertOps();
            rs.close();
            System.out.println("Finished processing " + i + " shppt blobs");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Processing addr ranges..");
            newDB.setActiveTable("addrranges");
            ResultSet rs = legDB_.getAllRows("addrranges");
            int i = 0;
            while (rs.next()) {
                i++;
                if (i % 10000 == 0) {
                    System.out.println(i + " addr ranges processed");
                }
                int id = rs.getInt(1);
                int fAddrL = rs.getInt(2);
                int fAddrR = rs.getInt(3);
                int tAddrL = rs.getInt(4);
                int tAddrR = rs.getInt(5);
                int zipL = rs.getInt(6);
                int zipR = rs.getInt(7);
                newDB.initAddrRange(id, fAddrL, fAddrR, tAddrL, tAddrR, zipL, zipR);
            }
            newDB.flushInsertOps();
            rs.close();
            System.out.println("Finished processing " + i + " addr ranges");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Processing elev data..");
            newDB.setActiveBlobTable("elevation");
            ResultSet rs = legDB_.getAllRows("elevation");
            int i = 0;
            while (rs.next()) {
                i++;
                if (i % 10000 == 0) {
                    System.out.println(i + " elev entries processed");
                }
                int id = rs.getInt(1);
                java.sql.Blob blob = rs.getBlob(2);
                float upslopefw = rs.getFloat(3);
                float upslopebw = rs.getFloat(4);
                newDB.initElevInfo(id, blob.getBytes(1, (int) blob.length()), upslopefw, upslopebw);
            }
            newDB.flushBlobInsertOps();
            rs.close();
            System.out.println("Finished processing " + i + " elev entries");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Processing scenlinktypes..");
            newDB.setActiveTable("scenlinktypes");
            ResultSet rs = legDB_.getAllRows("scenlinktypes");
            int i = 0;
            while (rs.next()) {
                i++;
                if (i % 10000 == 0) {
                    System.out.println(i + " scenlinktypes processed");
                }
                short scenID = rs.getShort(1);
                int linkID = rs.getInt(2);
                int newType = rs.getInt(3);
                newDB.initScenLinkType(scenID, linkID, newType);
            }
            rs.close();
            System.out.println("Finished processing " + i + " scenlinktypes");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        newDB.close();
    }
}
