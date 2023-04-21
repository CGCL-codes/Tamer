package thickclient.dataAccess;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SalesTransactionItemDAO extends BaseDAO {

    public static final String PKID = "pkid";

    public static final String TRANSACTION_ID = "transaction_id";

    public static final String ITEM_ID = "item_id";

    public static final String QTY = "qty";

    public static final String LOCATION_ID = "location_id";

    public static final String PRICE = "price";

    public static final String STATUS = "status";

    public static final String DATE_CREATED = "date_created";

    public static final String STATUS_PENDING = "pending";

    public static final String STATUS_PUSHED = "pushed";

    public static final String TABLENAME = "sales_transaction_item";

    public boolean insertObject(SalesTransactionItemObject valObj) {
        Connection conn = null;
        boolean result = false;
        try {
            conn = BaseDAO.getConnection();
            Statement s = conn.createStatement();
            String insertStatement = "insert into " + TABLENAME + " values (" + valObj.pkid + "," + valObj.transaction_id + "," + valObj.item_id + "," + valObj.qty + "," + valObj.location_id + "," + valObj.price + ",'" + valObj.status + "','" + new Timestamp(valObj.date_created).toString() + "')";
            System.out.println(insertStatement);
            s.execute(insertStatement);
        } catch (SQLException e) {
            result = true;
        } catch (Throwable e) {
            System.out.println("exception thrown:");
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(SalesTransactionIndexDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
