package util;

import java.io.Serializable;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import util.Misc;

public class Summary implements Serializable {

    private static final long serialVersionUID = -3599879826884214321L;

    private Statement s;

    private PreparedStatement ps;

    private PreparedStatement ps1;

    @SuppressWarnings("unused")
    private String date;

    private String code;

    public String name = "", dept = "", desig = "", status = "", pic = "", type = "";

    public int total = 0, outgoing = 0, incoming = 0;

    public Summary(String date, Statement s) throws SQLException {
        this.s = s;
        this.date = Misc.getMysqlDateFormat(date);
        ps = s.getConnection().prepareStatement("select count(date) from call_det where date = ? and type = ? and co = ? and ext != -1");
        ps1 = s.getConnection().prepareStatement("select time_format(time, '%h:%i %p') from call_det where date = ? and type = ? and co = ? and ext != -1 order by time desc limit 1");
    }

    public String getShift() throws SQLException {
        ResultSet rs = s.executeQuery("select name from shifts where start_time <= curtime() and end_time>=curtime()");
        if (rs.first()) {
            return rs.getString(1);
        } else {
            return "";
        }
    }

    public String getCode() throws SQLException {
        ResultSet rs = s.executeQuery("select emp_code, status, type from attendance where last=1");
        if (rs.first()) {
            code = rs.getString(1);
            status = rs.getString(2);
            type = rs.getString(3);
            setValues();
            return code;
        } else {
            return "";
        }
    }

    public void setValues() throws SQLException {
        ResultSet rs = s.executeQuery("select name, dept, desig, picture from employee where code='" + code + "';");
        if (rs.first()) {
            name = rs.getString(1);
            dept = rs.getString(2);
            desig = rs.getString(3);
            pic = rs.getString(4);
            if (pic.equals("")) {
                pic = "na.jpg";
            }
        } else {
            name = "";
            dept = "";
            desig = "";
            pic = "";
        }
    }

    public void close() throws SQLException {
        ps.close();
        ps1.close();
        s.close();
    }
}
