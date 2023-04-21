package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.sourceforge.jtds.jdbc.ColInfo;
import net.sourceforge.jtds.jdbc.JtdsResultSet;
import net.sourceforge.jtds.jdbc.JtdsStatement;
import ATOFMS.ParticleInfo;
import analysis.BinnedPeakList;

/**
 * @author steinbel
 * Trying a cursor that will go both forward and backwards.  (Possibilities for
 * use in actual code if previous() works out - then implement other methods.)
 */
public class BothWayCursor implements CollectionCursor {

    ResultSet rs;

    /**
	 * @throws SQLException 
	 * 
	 */
    public BothWayCursor(Connection con, int collID) throws SQLException {
        Statement stmt = con.createStatement();
        rs = stmt.executeQuery("USE TestDB2 SELECT AtomID FROM AtomMembership WHERE CollectionID = " + collID);
        System.out.println(rs.getType() == ResultSet.TYPE_FORWARD_ONLY);
    }

    public boolean next() {
        try {
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ParticleInfo getCurrent() {
        return null;
    }

    public int getAtomID() {
        int atomID = -9999;
        try {
            atomID = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atomID;
    }

    public boolean previous() {
        try {
            rs.setFetchDirection(ResultSet.FETCH_REVERSE);
            return rs.previous();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close() {
    }

    public void reset() {
    }

    public ParticleInfo get(int i) throws NoSuchMethodException {
        return null;
    }

    public BinnedPeakList getPeakListfromAtomID(int id) {
        return null;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new CreateTestDatabase2();
        System.out.println("created db.");
        InfoWarehouse iw = Database.getDatabase("TestDB2");
        try {
            iw.getCon();
            iw.openConnection();
            BothWayCursor curs = new BothWayCursor(iw.getCon(), 4);
            while (curs.next()) System.out.println(curs.getAtomID());
            System.out.println("----------Now backwards---------");
            while (curs.previous()) System.out.println(curs.getAtomID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
