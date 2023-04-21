package pub.beans;

import pub.db.*;
import java.sql.*;
import java.util.*;
import pub.utils.Log;

/**
 * Provides some statistics on the database (how many articles we
 * have, how many terms, etc.).
 *
 * See acoma.stanford.edu:~curator/SQL-Statements/sql_pub_leo for
 * details on the SQL queries performed here.
 */
public class StatisticsBean {

    private Map cachedValues;

    private Map cachedMapValues;

    private ConnectionFactory connFactory;

    public StatisticsBean(ConnectionFactory connFactory) {
        this.connFactory = connFactory;
        cachedValues = new HashMap();
        cachedMapValues = new HashMap();
    }

    /** Returns the number of nonobsolete articles */
    public String getCountArticles() {
        return selectSingleValue("select count(*) from pub_article where is_obsolete='n'");
    }

    public String getDateSynchronized(String table_name) {
        return selectSingleValue("select max(date_last_synchronized) from " + table_name);
    }

    /** Returns the number of nonobsolete hits */
    public String getCountHits() {
        return selectSingleValue("select count(*) from pub_hit where is_obsolete='n'");
    }

    /** Returns the number of nonobsolete genes */
    public String getCountGenes() {
        return selectSingleValue("select count(*) from pub_gene where is_obsolete='n'");
    }

    /** Returns the number of nonobsolete terms */
    public String getCountTerms() {
        return selectSingleValue("select count(*) from pub_term where is_obsolete='n' and (type != 'gene' or type IS NULL)");
    }

    /** Returns the number of unobsoleted go terms */
    public String getCountGoTerms() {
        return selectSingleValue("select count(*) from pub_term where pub_term.go_external_id like 'GO:%' and is_obsolete='n'");
    }

    /** Returns the number of annotations */
    public String getCountAnnotations() {
        return selectSingleValue("select count(*) from pub_termannotation where is_obsolete='n'");
    }

    /** Returns the number of temporary terms */
    public String getCountTemporaryTerms() {
        return selectSingleValue("select count(*) from pub_term where is_temporary='y' and is_obsolete='n'");
    }

    /**
     * Returns a histogram of how the articles are distributed within
     * the publication types.
     */
    public Map getPublicationTypeHistogram() {
        if (!cachedMapValues.containsKey("pubTypeHistogram")) {
            PubConnection conn = connFactory.getReadOnlyConnection();
            try {
                PreparedStatement stmt = conn.prepareStatement("select pub_source.type, count(*) as c " + " from pub_source, pub_article " + " where pub_article.pub_source_id = pub_source.id " + " and pub_article.is_obsolete='n' " + " and pub_source.is_obsolete = 'n' " + " group by pub_source.type ");
                try {
                    ResultSet rs = stmt.executeQuery();
                    Map m = new TreeMap();
                    while (rs.next()) {
                        m.put(rs.getString("type"), rs.getString("c"));
                    }
                    cachedMapValues.put("pubTypeHistogram", m);
                } finally {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                conn.freeConnection();
            }
        }
        return (Map) cachedMapValues.get("pubTypeHistogram");
    }

    /**
     * Given an SQL query, returns a single value for that query.
     */
    private String selectSingleValue(String query) {
        if (!cachedValues.containsKey(query)) {
            PubConnection conn = connFactory.getReadOnlyConnection();
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                try {
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        cachedValues.put(query, rs.getString(1));
                    }
                } finally {
                    stmt.close();
                }
            } catch (SQLException e) {
                Log.getLogger(this.getClass()).warn(e);
                return "";
            } finally {
                conn.freeConnection();
            }
        }
        return "" + cachedValues.get(query);
    }
}
