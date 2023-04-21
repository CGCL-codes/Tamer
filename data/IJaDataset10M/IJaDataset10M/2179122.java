package org.gbif.portal.harvest.statistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * A crawler that will loop over all the TC records and for now, just build a flat description of a 
 * taxonomy to investigate the memory it requires
 * 
 * @author trobertson
 */
public class TaxonStatistics {

    /**
	 * Cells
	 */
    protected Map<Integer, Integer[]> taxa = new HashMap<Integer, Integer[]>(10000);

    /**
	 * @param server DB server (e.g. Aenetus)
	 * @param user DB user
	 * @param password DB password
	 */
    protected void run(String server, String user, String password) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection(server, user, password);
            String query = "select " + "tc.id," + "tc.parent_concept_id," + "tc.kingdom_concept_id," + "tc.phylum_concept_id," + "tc.class_concept_id," + "tc.order_concept_id," + "tc.family_concept_id," + "tc.genus_concept_id," + "tc.species_concept_id," + "tc.rank " + "from taxon_concept tc where tc.data_resource_id=1";
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(query);
            int i = 0;
            long time = System.currentTimeMillis();
            long startTime = System.currentTimeMillis();
            while (rs.next()) {
                i++;
                Integer[] data = new Integer[9];
                if (rs.getObject("parent_concept_id") == null) data[0] = null; else data[0] = rs.getInt("parent_concept_id");
                if (rs.getObject("kingdom_concept_id") == null) data[1] = null; else data[1] = rs.getInt("kingdom_concept_id");
                if (rs.getObject("phylum_concept_id") == null) data[2] = null; else data[2] = rs.getInt("phylum_concept_id");
                if (rs.getObject("class_concept_id") == null) data[3] = null; else data[3] = rs.getInt("class_concept_id");
                if (rs.getObject("order_concept_id") == null) data[4] = null; else data[4] = rs.getInt("order_concept_id");
                if (rs.getObject("family_concept_id") == null) data[5] = null; else data[5] = rs.getInt("family_concept_id");
                if (rs.getObject("genus_concept_id") == null) data[6] = null; else data[6] = rs.getInt("genus_concept_id");
                if (rs.getObject("species_concept_id") == null) data[7] = null; else data[7] = rs.getInt("species_concept_id");
                if (rs.getObject("rank") == null) data[8] = null; else data[8] = rs.getInt("rank");
                taxa.put(rs.getInt("id"), data);
                int loopLog = 1000000;
                if (i % loopLog == 0) {
                    System.out.println(loopLog + " records returned in: " + (1 + (System.currentTimeMillis() - time) / 1000) + " secs. Total records returned: " + i);
                    time = System.currentTimeMillis();
                    logMemory();
                }
            }
            logMemory();
            System.out.println("All records returned in: " + (1 + (System.currentTimeMillis() - startTime) / 1000) + " secs. Total records returned: " + i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            ;
            try {
                stmt.close();
            } catch (Exception e) {
            }
            ;
            try {
                conn.close();
            } catch (Exception e) {
            }
            ;
        }
    }

    /**
	 * @throws Exception
	 */
    protected static Connection getConnection(String server, String username, String password) throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://" + server + "/portal";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    protected static void logMemory() {
        System.out.println("MaxMemory: " + Runtime.getRuntime().maxMemory());
        System.out.println("FreeMemory: " + Runtime.getRuntime().freeMemory());
        System.out.println("UsedMemory: " + Runtime.getRuntime().totalMemory());
    }

    /**
	 * @param args Requires server, user, password
	 */
    public static void main(String args[]) {
        if (args.length != 3) {
            System.out.print("Usage: OccurrenceStatistics dbServerName dbUser rbPassword");
            System.exit(1);
        }
        TaxonStatistics app = new TaxonStatistics();
        try {
            app.run(args[0], args[1], args[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
