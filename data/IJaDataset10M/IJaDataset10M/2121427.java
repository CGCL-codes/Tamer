package beans.miscellaneous;

/**
 *
 * @author  administrator
 */
public class NewGenLibLogIn implements beans.miscellaneous.OpacLogIn {

    /** Creates a new instance of NewGenLibLogIn */
    public NewGenLibLogIn() {
    }

    public java.util.Vector getUserDetails(String userid, String libraryid, String password) {
        java.util.Vector v1 = new java.util.Vector();
        try {
            String libname = "";
            java.sql.Connection con = ejb.bprocess.util.DBConnector.getInstance().getDBConnection();
            java.sql.Statement sta = con.createStatement();
            String one = "";
            one = "select Library_Name from Library where Library_Id='" + libraryid + "'";
            java.sql.ResultSet rs = sta.executeQuery(one);
            while (rs.next()) {
                libname = rs.getString(1);
            }
            rs.close();
            sta.close();
            java.sql.Timestamp t1 = ejb.bprocess.util.Utility.getInstance(null).getTimestamp();
            java.util.Calendar cl1 = java.util.Calendar.getInstance();
            cl1.setTimeInMillis(t1.getTime());
            int dd = cl1.get(java.util.Calendar.DATE);
            int mm = cl1.get(java.util.Calendar.MONTH) + 1;
            int yy = cl1.get(java.util.Calendar.YEAR);
            String date = String.valueOf(mm) + "/" + String.valueOf(dd) + "/" + String.valueOf(yy);
            if (libname.toLowerCase().trim().equals("Goa university library".toLowerCase())) {
                sta = con.createStatement();
                String sql = "select a.fname,a.mname,a.lname,b.Library_Name from Patron a, Library b where a.Library_Id=b.Library_Id and a.Patron_Id='" + userid + "' and a.Library_Id='" + libraryid + "' and a.USER_PASSWORD='" + password + "' and a.MEMBERSHIP_EXPIRY_DATE>= '" + t1 + "'  and a.MEMBERSHIP_START_DATE<='" + t1 + "'";
                rs = sta.executeQuery(sql);
                while (rs.next()) {
                    v1.addElement(userid);
                    v1.addElement(libraryid);
                    v1.addElement(rs.getString(4));
                    String name = "";
                    if (rs.getString(1) != null) {
                        name += rs.getString(1) + " ";
                        if (rs.getString(2) != null) name += rs.getString(2) + " ";
                        if (rs.getString(3) != null) name += rs.getString(3) + " ";
                        v1.addElement(name);
                    }
                }
                rs.close();
                sta.close();
                if (v1.size() == 0) {
                    sta = con.createStatement();
                    sql = "select a.fname,a.mname,a.lname,b.Library_Name,a.Patron_Id from Patron a, Library b where a.Library_Id=b.Library_Id and a.Patron_Id like '" + userid + "_' and a.Library_Id='" + libraryid + "' and a.USER_PASSWORD='" + password + "' and a.MEMBERSHIP_EXPIRY_DATE >= '" + t1 + "'  and a.MEMBERSHIP_START_DATE<='" + t1 + "'";
                    rs = sta.executeQuery(sql);
                    while (rs.next()) {
                        v1.addElement(rs.getString(5));
                        v1.addElement(libraryid);
                        v1.addElement(rs.getString(4));
                        String name = "";
                        if (rs.getString(1) != null) {
                            name += rs.getString(1) + " ";
                            if (rs.getString(2) != null) name += rs.getString(2) + " ";
                            if (rs.getString(3) != null) name += rs.getString(3) + " ";
                            v1.addElement(name);
                        }
                    }
                    rs.close();
                    sta.close();
                }
            } else {
                sta = con.createStatement();
                String sql = "select a.fname,a.mname,a.lname,b.Library_Name,a.Patron_Id from Patron a, Library b where a.Library_Id=b.Library_Id and (a.Patron_Id='" + userid + "' or a.email='" + userid + "') and a.Library_Id='" + libraryid + "' and a.USER_PASSWORD='" + password + "' and a.MEMBERSHIP_EXPIRY_DATE >= '" + t1 + "'  and a.MEMBERSHIP_START_DATE <='" + t1 + "'";
                rs = sta.executeQuery(sql);
                while (rs.next()) {
                    if (rs.getString(1) != null) {
                        v1.addElement(rs.getString(5));
                        v1.addElement(libraryid);
                        v1.addElement(rs.getString(4));
                        String name = "";
                        name += rs.getString(1) + " ";
                        if (rs.getString(2) != null) name += rs.getString(2) + " ";
                        if (rs.getString(3) != null) name += rs.getString(3) + " ";
                        v1.addElement(name);
                    }
                }
                rs.close();
                sta.close();
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v1;
    }

    public String getUserValidation(String userid, String libraryid, String password) {
        String s1 = "";
        ;
        boolean flag = false;
        try {
            String libname = "";
            java.sql.Connection con = ejb.bprocess.util.DBConnector.getInstance().getDBConnection();
            java.sql.Statement sta = con.createStatement();
            String one = "";
            one = "select Library_Name from Library where Library_Id='" + libraryid + "'";
            java.sql.ResultSet rs = sta.executeQuery(one);
            while (rs.next()) {
                libname = rs.getString(1);
            }
            rs.close();
            sta.close();
            java.sql.Timestamp t1 = ejb.bprocess.util.Utility.getInstance(null).getTimestampWithTime();
            java.util.Calendar cl1 = java.util.Calendar.getInstance();
            cl1.setTimeInMillis(t1.getTime());
            int dd = cl1.get(java.util.Calendar.DATE);
            int mm = cl1.get(java.util.Calendar.MONTH) + 1;
            int yy = cl1.get(java.util.Calendar.YEAR);
            String date = String.valueOf(mm) + "/" + String.valueOf(dd) + "/" + String.valueOf(yy);
            System.out.println("date in newgenlib " + date);
            System.out.println("date in newgenlib timestyap  " + t1.toString());
            String paswordencrypted = ejb.bprocess.util.EncryptionPassword.getInstance().encrypt(password);
            if (libname.toLowerCase().trim().equals("Goa university library".toLowerCase())) {
                sta = con.createStatement();
                String sql = "select a.fname,a.mname,a.lname,b.Library_Name,a.USER_PASSWORD from Patron a, Library b where a.Library_Id=b.Library_Id and a.Patron_Id='" + userid + "' and a.Library_Id='" + libraryid + "' and a.MEMBERSHIP_EXPIRY_DATE>= '" + t1 + "'  and a.MEMBERSHIP_START_DATE<='" + t1 + "'";
                rs = sta.executeQuery(sql);
                while (rs.next()) {
                    String pass12 = "";
                    pass12 = rs.getString(5);
                    if (pass12.trim().equals(password)) {
                        flag = true;
                    } else {
                        String paswordencrypted1 = ejb.bprocess.util.EncryptionPassword.getInstance().encrypt(pass12);
                        if (paswordencrypted.trim().equals(password)) {
                            flag = true;
                        }
                    }
                }
                rs.close();
                sta.close();
                if (!flag) {
                    sta = con.createStatement();
                    sql = "select a.fname,a.mname,a.lname,b.Library_Name,a.Patron_Id,a.USER_PASSWORD from Patron a, Library b where a.Library_Id=b.Library_Id and a.Patron_Id like '" + userid + "_' and a.Library_Id='" + libraryid + "' and a.MEMBERSHIP_EXPIRY_DATE >= '" + t1 + "'  and a.MEMBERSHIP_START_DATE<='" + t1 + "'";
                    rs = sta.executeQuery(sql);
                    while (rs.next()) {
                        String pass12 = "";
                        pass12 = rs.getString(6);
                        if (pass12.trim().equals(password)) {
                            flag = true;
                        } else {
                            String paswordencrypted1 = ejb.bprocess.util.EncryptionPassword.getInstance().encrypt(pass12);
                            if (paswordencrypted.trim().equals(password)) {
                                flag = true;
                            }
                        }
                    }
                    rs.close();
                    sta.close();
                }
            } else {
                sta = con.createStatement();
                String sql = "select a.fname,a.mname,a.lname,b.Library_Name,a.Patron_Id,a.USER_PASSWORD from Patron a, Library b where a.Library_Id=b.Library_Id and (a.Patron_Id='" + userid + "' or a.email='" + userid + "') and a.Library_Id='" + libraryid + "' and a.MEMBERSHIP_EXPIRY_DATE >= '" + t1 + "'  and a.MEMBERSHIP_START_DATE <='" + t1 + "'";
                rs = sta.executeQuery(sql);
                while (rs.next()) {
                    String pass12 = "";
                    pass12 = rs.getString(6);
                    if (pass12.trim().equals(password)) {
                        flag = true;
                    } else {
                        String paswordencrypted1 = ejb.bprocess.util.EncryptionPassword.getInstance().encrypt(pass12);
                        if (paswordencrypted.trim().equals(password)) {
                            flag = true;
                        }
                    }
                }
                rs.close();
                sta.close();
            }
            con.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        if (flag) s1 = "SUCCESS"; else s1 = "FAILURE";
        return s1;
    }
}
