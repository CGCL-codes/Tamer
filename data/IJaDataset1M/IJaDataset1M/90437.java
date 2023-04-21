package ejb.bprocess.opac.xcql;

/**
 *
 * @author  Administrator
 */
public class Holidays {

    /** Creates a new instance of Holidays */
    public Holidays() {
    }

    public java.util.Vector getHolidays(String libraryId) {
        java.sql.Connection con = ejb.bprocess.util.DBConnector.getInstance().getDBConnection();
        java.util.Vector vechol = new java.util.Vector(1, 1);
        String year = "";
        try {
            java.sql.Statement stat = con.createStatement();
            java.sql.Timestamp currentDate = ejb.bprocess.util.Utility.getInstance().getTimestampWithTime();
            String fisQuery = "select fiscal_year from acc_fiscal_year where '" + currentDate + "'>=start_date    and '" + currentDate + "'<=end_date";
            java.sql.ResultSet rs = stat.executeQuery(fisQuery);
            while (rs.next()) {
                year = String.valueOf(rs.getInt(1));
            }
            stat.close();
            rs.close();
            System.out.println("year is " + year + "and libarary id" + libraryId);
            stat = con.createStatement();
            rs = stat.executeQuery("select holiday, note from adm_co_holiday where library_id='" + libraryId + "' and fiscal_year='" + year + "' order by holiday");
            while (rs.next()) {
                java.sql.Timestamp ts = rs.getTimestamp(1);
                String holi = rs.getString(2);
                Object[] obx = new Object[2];
                obx[0] = ts;
                obx[1] = holi;
                vechol.addElement(obx);
            }
            rs.close();
            stat.close();
            con.close();
            System.out.println("before " + vechol);
            java.util.Collections.sort(vechol, new HolidaySorter());
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        System.out.println(vechol);
        return vechol;
    }
}

class HolidaySorter implements java.util.Comparator {

    public int compare(Object o1, Object o2) {
        Object[] obx1 = (Object[]) o1;
        Object[] obx2 = (Object[]) o2;
        java.sql.Timestamp ts1 = (java.sql.Timestamp) obx1[0];
        java.sql.Timestamp ts2 = (java.sql.Timestamp) obx2[0];
        java.util.Date d1 = new java.util.Date(ts1.getTime());
        java.util.Date d2 = new java.util.Date(ts2.getTime());
        if (d1.after(d2)) return 1; else if (d1.before(d2)) return -1; else return 0;
    }
}
