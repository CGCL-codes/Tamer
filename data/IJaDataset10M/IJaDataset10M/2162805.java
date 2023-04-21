package audit;

import java.sql.*;
import java.util.logging.*;

public class HostAUQuery {

    static final Logger logger = LogFormatter.make_logger(Driver.class.getPackage().getName(), false);

    private static final Level datd = Level.parse("DATD");

    private static final Level time = Level.parse("TIME");

    private ResultSet rs = null;

    public HostAUQuery(DBNetwork net_db, Connection conn, String network_db) {
        logger.info("\n===========================================================");
        logger.info("========== Constructor: \"HostAUQuery\" ==================");
        logger.info("========== Query 'pln_cm_db.archival_unit_status_table' and");
        logger.info("================ 'pln_cm_db.lockss_box_table' =============");
        logger.info("===========================================================");
        String q1_HA = "SELECT " + "au.pln_au_id, au.au_id, au.au_name, au.au_size, au.disk_usage, au.au_polls, au.au_status, au.pln_box_id, " + "au.last_poll, au.last_crawl_date, au.last_crawl_result, au.last_successful_crawl, " + " host.ip_address," + " CONCAT_WS( '_',host.ip_address, au.au_id ) as host_au_id " + " FROM " + network_db + ".archival_unit_status_table as au, " + network_db + ".lockss_box_table as host " + " WHERE au.pln_box_id = host.pln_box_id";
        this.rs = net_db.query(conn, q1_HA);
    }

    public ResultSet get_rs() {
        return rs;
    }
}
