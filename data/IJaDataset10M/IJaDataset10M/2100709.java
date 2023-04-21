package utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import timer.ExecutionTimer;
import timer.TimerRecordFile;
import dbmanager.DBManager;
import dbmanager.GetIdName;

public class Addcode {

    public Addcode() {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        DBManager db = new DBManager();
        db.getConnect();
        GetIdName gin = new GetIdName(db);
        ResultSet rs = db.getSelect("select distinct pv from code where pv not in(select mid from code1)");
        try {
            while (rs.next()) {
                String mid = rs.getString("pv");
                String name = gin.getItem(mid);
                if (name.length() == 3) {
                    db.getInsert("insert into property_details (mid,pid,pv,vt,ref) values(" + mid + ",9,5853,'no$',0)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("utility", "Addcode", "Addcode", t.duration());
    }

    public static void main(String[] args) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        new Addcode();
    }
}
