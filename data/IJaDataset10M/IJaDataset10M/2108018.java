package uima;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import document.lookup.TFIDFier;

public class CreateDocumentStatistics {

    protected Connection con;

    /**
	 * Opens the database
	 * 
	 */
    public void openDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://pong.llnl.gov/trec_filter_new", "dbuser", "dbpass");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected CreateDocumentStatistics() {
        openDatabase();
    }

    /**
	 * Compue the document frequencies from scratch
	 * 
	 * @param termType
	 *            type of terms to consider
	 * @param location
	 *            location of term (body or title)
	 * @param corpusDate
	 *            collection of documents published on or before corpusDate
	 * @return
	 */
    protected HashSet docs = new HashSet();

    protected TFIDFier computeFromScratch(String termType, String location, String corpusDate) {
        TFIDFier tv = new TFIDFier();
        ResultSet rs;
        try {
            String sql = null;
            if (corpusDate == null) sql = "SELECT k.docID, term FROM terms k"; else sql = "SELECT k.docID, term " + "FROM terms k, newsItem e " + "WHERE k.docID = e.docID AND " + "pubDate <= ? AND termType = ? AND location = ? " + "ORDER BY docID";
            PreparedStatement pstmt = con.prepareStatement(sql);
            if (corpusDate == null) {
            } else {
                pstmt.setString(1, corpusDate);
                pstmt.setString(2, termType);
                pstmt.setString(3, location);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                docs.add(rs.getInt("docID"));
                tv.addTerm(rs.getString("term"));
            }
            tv.setNumberOfDocuments(docs.size());
            rs.close();
            pstmt.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        tv.finishAddingTerms();
        store(tv, termType, location, corpusDate);
        return tv;
    }

    public static int MAX_RECORD = 10000;

    protected static String INSERT_SQL = "INSERT INTO documentfrequencies(term, termType, count,location) VALUES";

    /**
	 * Stores (caches) the document frequencies
	 * @param tv the TFIDFier (document frequency summary)
	 * @param termType type of term
	 * @param location location
	 * @param corpusDate date
	 */
    protected void store(TFIDFier tv, String termType, String location, String corpusDate) {
        StringBuffer sql = null;
        int i = 0;
        try {
            Statement stmt = con.createStatement();
            for (Iterator itr = tv.getTermCountMap().keySet().iterator(); itr.hasNext(); i++) {
                String term = (String) itr.next();
                Integer count = (Integer) tv.getTermCountMap().get(term);
                if (i % MAX_RECORD > 0) sql.append(","); else {
                    if (sql != null) stmt.execute(sql.toString());
                    sql = new StringBuffer(INSERT_SQL);
                }
                sql.append("(\"");
                sql.append(term + "\",\"");
                sql.append(termType + "\",");
                sql.append(count.toString() + ",\"");
                sql.append(location + "\"");
                sql.append(")");
            }
            if (sql != null) {
                stmt.execute(sql.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String termType[] = { "stems" };
        CreateDocumentStatistics stat = new CreateDocumentStatistics();
        for (int i = 0; i < termType.length; i++) stat.computeFromScratch(termType[i], "body", null);
    }
}
