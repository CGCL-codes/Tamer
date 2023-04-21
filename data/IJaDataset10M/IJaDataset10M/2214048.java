package ejb.objectmodel.sm;

/**
 *
 * @author  Administrator
 */
public class SM_BIND_LISTHandler {

    /** Creates a new instance of SM_BIND_LISTHandler */
    public SM_BIND_LISTHandler() {
    }

    public java.lang.String getListCount(java.lang.Integer subLibId, java.lang.Integer libId, java.lang.String regId) {
        java.lang.String count = "";
        java.sql.Connection con = null;
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;
        try {
            con = ejb.bprocess.util.DBConnector.getInstance().getDBConnection();
            stmt = con.createStatement();
            String sql = "select count(*) from SM_BIND_LIST where subscription_Library_Id= " + subLibId + " and library_Id = " + libId + " and registration_Id = " + regId;
            System.out.println("sql : " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                count = rs.getString(1);
            }
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (java.sql.SQLException ex) {
                ex.printStackTrace(System.out);
            } catch (java.lang.NullPointerException ex) {
                ex.printStackTrace(System.out);
            }
            return count;
        }
    }
}
