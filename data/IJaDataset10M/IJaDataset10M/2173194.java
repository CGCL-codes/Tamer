package org.nescent.phenoscape.query;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.obd.query.impl.OBDSQLShard;

public class CounterForTaxaAndTaxaWithDistinctPhenotypes {

    private OBDSQLShard shard;

    private Connection conn;

    private final String driverName = "jdbc:postgresql://";

    private static final String CONNECTION_PROPERTIES_FILENAME = "connection.properties";

    private String getTaxaQuery = "SELECT DISTINCT taxon_nid FROM dw_taxon_phenotype_table";

    private String getPhenotypesForTaxonQuery = "SELECT phenotype_nid FROM dw_taxon_phenotype_table WHERE taxon_nid = ?";

    private String getDistinctPhenotypesForTaxonQuery = "SELECT DISTINCT phenotype_nid FROM dw_taxon_phenotype_table WHERE taxon_nid = ?";

    protected int taxonCt = 0, taxonWithDistinctPhenotypeCt = 0;

    public CounterForTaxaAndTaxaWithDistinctPhenotypes() {
        super();
        try {
            this.shard = new OBDSQLShard();
            final Properties props = new Properties();
            props.load(this.getClass().getResourceAsStream(CONNECTION_PROPERTIES_FILENAME));
            String dbHost = (String) props.get("dbHost");
            String db1 = driverName + dbHost + "/obdphenoscape_test";
            String uid = (String) props.get("uid");
            String pwd = (String) props.get("pwd");
            shard.connect(db1, uid, pwd);
            this.conn = shard.getConnection();
        } catch (SQLException e) {
            log().fatal("Error connecting to shard", e);
        } catch (ClassNotFoundException e) {
            log().fatal("Error connecting to shard", e);
        } catch (IOException e) {
            log().fatal("Error loading connection properties", e);
        }
    }

    public static void main(String[] args) {
        CounterForTaxaAndTaxaWithDistinctPhenotypes counter = new CounterForTaxaAndTaxaWithDistinctPhenotypes();
        try {
            counter.createDataStructureForTaxaAndPhenotypes();
            counter.printDataStructure();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDataStructureForTaxaAndPhenotypes() throws SQLException {
        int taxon_nid;
        int phenotypeCt, distinctPhenotypeCt;
        Statement stmt = conn.createStatement();
        ResultSet rs1 = stmt.executeQuery(this.getTaxaQuery);
        PreparedStatement ps1 = conn.prepareStatement(this.getPhenotypesForTaxonQuery);
        PreparedStatement ps2 = conn.prepareStatement(this.getDistinctPhenotypesForTaxonQuery);
        while (rs1.next()) {
            ++taxonCt;
            taxon_nid = rs1.getInt(1);
            ps1.setInt(1, taxon_nid);
            ps2.setInt(1, taxon_nid);
            phenotypeCt = 0;
            distinctPhenotypeCt = 0;
            ResultSet rs2 = ps1.executeQuery();
            while (rs2.next()) {
                ++phenotypeCt;
            }
            ResultSet rs3 = ps2.executeQuery();
            while (rs3.next()) {
                ++distinctPhenotypeCt;
            }
            if (distinctPhenotypeCt != phenotypeCt) {
                ++taxonWithDistinctPhenotypeCt;
            }
        }
    }

    private void printDataStructure() {
        System.out.println("Number of taxa with phenotypes : " + this.taxonCt);
        System.out.println("Number of taxa with distinct phenotypes: " + this.taxonWithDistinctPhenotypeCt);
    }

    private static Logger log() {
        return Logger.getLogger(Queries.class);
    }
}
