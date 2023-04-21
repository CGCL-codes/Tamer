package org.nbrowse.dbloader;

import java.sql.SQLException;
import org.nbrowse.sql.NoResult;
import org.nbrowse.sql.SQL;

final class GnbInteraction {

    private String from, to, type, dataset;

    private static final String table = "gnb_interactions";

    private int edge_id;

    private static final String[] INS_COLS = new String[] { "from_node_primary_name", "to_node_primary_name", "edge_type", "edge_dataset", "taxon_id" };

    GnbInteraction(String f, String t, String ty, String ds) {
        from = f;
        to = t;
        type = ty;
        dataset = ds;
    }

    /** also sets edge_id if present as it queries for it */
    public boolean existInDb() throws SQLException {
        sql().select("edge_id").from(table);
        sql().whereQuote("from_node_primary_name", from).whereQuote("to_node_primary_name", to);
        sql().whereQuote("edge_type", type).whereQuote("edge_dataset", dataset);
        try {
            edge_id = sql().getSingleIntResult();
        } catch (NoResult e) {
            return false;
        }
        return true;
    }

    private String q(String s) {
        return "'" + s + "'";
    }

    public void insert() throws Exception {
        sql().prepInsertVals(insCols(), table, stringVals());
        sql().prepSetInt(getColNumOfFirstNonString(), taxon());
        edge_id = sql().executePrepUpdate();
    }

    private int taxon() {
        return LoaderConfig.inst().getTaxonId();
    }

    private String[] insCols() {
        return INS_COLS;
    }

    private String[] stringVals() {
        return new String[] { from, to, type, dataset };
    }

    private int getColNumOfFirstNonString() {
        return stringVals().length + 1;
    }

    private SQL sql() {
        return SQL.inst();
    }

    int getEdgeId() {
        return edge_id;
    }
}
