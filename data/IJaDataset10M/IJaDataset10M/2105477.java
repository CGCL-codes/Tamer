package nu.lazy8.ledger.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import nu.lazy8.util.gen.SystemLog;

/**
 *  Description of the Class
 *
 * @author     Lazy Eight Data HB, Thomas Dilts
 * @created    den 5 mars 2002
 */
public class UniqNumGenerator {

    /**
   *  Constructor for the UniqNumGenerator object
   */
    public UniqNumGenerator() {
    }

    /**
   *  Description of the Method
   *
   * @param  NumbersName  Description of the Parameter
   * @param  minimum      Description of the Parameter
   * @param  maximum      Description of the Parameter
   * @param  compId       Description of the Parameter
   * @return              Description of the Return Value
   */
    public int ViewNextAvailableNumber(String NumbersName, int minimum, int maximum, Integer compId) {
        int lNewNumber = minimum;
        try {
            ResultSet resultSet;
            DataConnection dc = DataConnection.getInstance(null);
            if (dc == null || !dc.bIsConnectionMade) {
                return 0;
            }
            PreparedStatement readTable = dc.con.prepareStatement(dc.filterSQL("SELECT UniqName,CompId,LastNumber FROM UniqNum WHERE UniqName LIKE ? AND CompId=?"));
            readTable.setString(1, NumbersName);
            readTable.setInt(2, compId.intValue());
            resultSet = readTable.executeQuery();
            if (resultSet.next()) {
                int lNowNumber = resultSet.getInt(3);
                if ((lNowNumber + 1) <= maximum) {
                    lNowNumber++;
                } else {
                    lNowNumber = minimum;
                }
                lNewNumber = lNowNumber;
            } else {
            }
        } catch (Exception e) {
            SystemLog.ProblemPrintln("Error:" + e.getMessage());
        }
        return lNewNumber;
    }

    /**
   *  Description of the Method
   *
   * @param  NumbersName  Description of the Parameter
   * @param  minimum      Description of the Parameter
   * @param  maximum      Description of the Parameter
   * @param  compId       Description of the Parameter
   * @return              Description of the Return Value
   */
    public int GetUniqueNumber(String NumbersName, int minimum, int maximum, Integer compId) {
        int lNewNumber = ViewNextAvailableNumber(NumbersName, minimum, maximum, compId);
        try {
            DataConnection dc = DataConnection.getInstance(null);
            if (dc == null || !dc.bIsConnectionMade) {
                return 0;
            }
            if (lNewNumber == minimum) {
                PreparedStatement updateTable = dc.con.prepareStatement(dc.filterSQL("INSERT INTO UniqNum (UniqName,CompId,LastNumber) VALUES (?, ?, ?)"));
                updateTable.setString(1, NumbersName);
                updateTable.setInt(2, compId.intValue());
                updateTable.setInt(3, minimum);
                updateTable.executeUpdate();
            } else {
                PreparedStatement updateTable = dc.con.prepareStatement(dc.filterSQL("UPDATE UniqNum SET LastNumber = ? WHERE UniqName LIKE ? AND CompId=?"));
                updateTable.setInt(1, lNewNumber);
                updateTable.setString(2, NumbersName);
                updateTable.setInt(3, compId.intValue());
                updateTable.executeUpdate();
            }
        } catch (Exception e) {
            SystemLog.ProblemPrintln("Error:" + e.getMessage());
        }
        return lNewNumber;
    }
}
