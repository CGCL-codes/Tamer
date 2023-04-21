package experiments;

import database.*;
import java.sql.*;

/**
 * Something to make a histogram of particle size.
 * 
 * @author smitht
 *
 */
public class SizeHistogrammer {

    private static final int collID = 839;

    public SizeHistogrammer() {
        super();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) throws SQLException {
        InfoWarehouse db = Database.getDatabase();
        if (!db.openConnection()) throw new RuntimeException();
        Connection conn = db.getCon();
        Statement s = conn.createStatement();
        s.execute("SELECT Value FROM TimeSeriesAtomInfoDense WHERE AtomID IN " + "( Select AtomID FROM InternalAtomOrder WHERE " + "CollectionID = " + collID + " )");
        ResultSet set = s.getResultSet();
        HistList histogram = new HistList(2f);
        while (set.next()) {
            histogram.addPeak(set.getFloat(1));
        }
        for (int i = 0; i < histogram.size(); i++) {
            System.out.println(histogram.getIndexMiddle(i) + "\t" + histogram.get(i));
            if (histogram.getIndexMiddle(i) > 50) break;
        }
        db.closeConnection();
    }
}
