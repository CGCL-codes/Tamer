package beans.miscellaneous;

import java.beans.*;

/**
 *
 * @author  Administrator
 */
public class Login extends Object implements java.io.Serializable {

    private static final String PROP_SAMPLE_PROPERTY = "SampleProperty";

    private String sampleProperty;

    private PropertyChangeSupport propertySupport;

    /** Creates new Login */
    public Login() {
        propertySupport = new PropertyChangeSupport(this);
    }

    public String getSampleProperty() {
        return sampleProperty;
    }

    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public java.util.Vector getLibraries(String networkLibraryId) {
        java.sql.Connection con = ejb.bprocess.util.DBConnector.getInstance().getDBConnection();
        java.util.Vector vresult = new java.util.Vector(1, 1);
        try {
            java.sql.Statement sta = con.createStatement();
            networkLibraryId = networkLibraryId.replaceAll("'", "''");
            String sql = "select Library_Id,Library_Name from Library where network_name='" + networkLibraryId + "'";
            System.out.println("query===" + sql);
            java.sql.ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                vresult.addElement(rs.getString(1));
                vresult.addElement(rs.getString(2));
            }
            rs.close();
            sta.close();
            con.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return vresult;
    }

    public java.util.Vector getUserDetails(String userid, String libraryid, String password) {
        java.util.Vector v1 = new java.util.Vector();
        System.out.println("entered to login page");
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
            System.out.println("date in login bean====" + date);
            if (libname.toLowerCase().trim().equals("Goa university library".toLowerCase())) {
                System.out.println("calling1");
                sta = con.createStatement();
                String sql = "select a.fname,a.mname,a.lname,b.Library_Name from Patron a, Library b where a.Library_Id=b.Library_Id and a.Patron_Id='" + userid + "' and a.Library_Id='" + libraryid + "' and a.USER_PASSWORD='" + password + "' and a.MEMBERSHIP_EXPIRY_DATE>= '" + t1 + "'  and a.MEMBERSHIP_START_DATE<='" + t1 + "'";
                System.out.println("query=" + sql);
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
                System.out.println("calling2");
                System.out.println("length of str=" + v1.size());
                if (v1.size() == 0) {
                    System.out.println("calling3");
                    sta = con.createStatement();
                    sql = "select a.fname,a.mname,a.lname,b.Library_Name,a.Patron_Id from Patron a, Library b where a.Library_Id=b.Library_Id and a.Patron_Id like '" + userid + "_' and a.Library_Id='" + libraryid + "' and a.USER_PASSWORD='" + password + "' and a.MEMBERSHIP_EXPIRY_DATE >= '" + t1 + "'  and a.MEMBERSHIP_START_DATE<='" + t1 + "'";
                    System.out.println("query=" + sql);
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
                    System.out.println("length of str=" + v1.size());
                }
                for (int i = 0; i < v1.size(); i++) {
                    System.out.println("psition=" + i + " value=" + v1.elementAt(i).toString());
                }
            } else {
                sta = con.createStatement();
                String sql = "select a.fname,a.mname,a.lname,b.Library_Name,a.Patron_Id from Patron a, Library b where a.Library_Id=b.Library_Id and a.Patron_Id='" + userid + "' and a.Library_Id='" + libraryid + "' and a.USER_PASSWORD='" + password + "' and a.MEMBERSHIP_EXPIRY_DATE >= '" + t1 + "'  and a.MEMBERSHIP_START_DATE <='" + t1 + "'";
                System.out.println("sql=====" + sql);
                rs = sta.executeQuery(sql);
                System.out.println(rs);
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
                for (int i = 0; i < v1.size(); i++) {
                    System.out.println("psition=" + i + " value=" + v1.elementAt(i).toString());
                }
            }
            con.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        System.out.println("str length in final=" + v1.size());
        for (int i = 0; i < v1.size(); i++) {
            System.out.println("psition=" + i + " value=" + v1.elementAt(i).toString());
        }
        return v1;
    }
}
