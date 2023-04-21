package com.dl.ceorg.service;

import static util.Database.connectToDb;
import static util.FetchData.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import util.Database;
import com.dl.ceorg.model.Employee;
import com.dl.ceorg.model.RawData;
import com.dl.ceorg.util.DbUtils;
import com.dl.ceorg.util.MailUtil;

public class DataSynchronizer extends DbUtils implements Runnable {

    private static final String RAW_SQL = "select cardno, date, time, mid " + "from rawdata order by date,time limit 10";

    private static final String REMOVE_RAW_SQL = "delete from rawdata where cardno=? and time=?";

    private static final String EMPLOYEE_SQL = "select TIME_TO_SEC(start_time), TIME_TO_SEC(end_time), " + "start_time, end_time, TIME_TO_SEC(?) " + "from employee where code = ?";

    private static final String UNMARKED_EMPLOYEE_SQL = "select e.code, e.name, e.manager_id, e.email, m.name, m.email " + "from employee e, employee m " + "where e.manager_id = m.code " + "and e.code not in ( select emp_code from attendance where date=?)";

    private long defaultSleepTime = 200L;

    private MailUtil mail = MailUtil.getInstance();

    public DataSynchronizer() {
        System.out.println("Starting Synchronization ... ");
    }

    public void run() {
        int count = 0;
        while (true) {
            try {
                Thread.sleep(defaultSleepTime);
                List<RawData> list = getRawData();
                if (list != null && list.size() > 0) {
                    for (RawData rd : list) {
                        doProcess(rd);
                        System.out.println(rd);
                    }
                    removeRawData(list);
                }
                if (count++ % 5000 == 0) {
                    count = 0;
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private void sendNotif() {
        long start = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start);
        cal.set(Calendar.HOUR, 12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long diff = cal.getTimeInMillis();
        if ((start - diff) < 300000) {
            String today = sdf.format(new Date(start));
            List<Employee> empList = getUnMarkedData(today);
            for (Employee e : empList) {
                e.setToday(today);
                mail.sendMailAttNotMarked(e);
            }
        }
    }

    private void doProcess(RawData rd) throws SQLException {
        String type = getIntValue("select count(date) from attendance where emp_code='" + rd.getCardNo() + "' and date = '" + rd.getDate() + "'") % 2 == 0 ? "Time In" : "Time Out", status = "N/A";
        if ("Time In".equals(type)) {
            Employee emp = getEmpData(rd.getCardNo(), rd.getTime());
            if (emp == null) {
                System.out.println("Suspicious Employee: " + rd.getCardNo());
                mail.sendMailUnknown(rd);
                return;
            }
            if (emp.getCurTime() > emp.getStartTimeML()) {
                status = "Late Arrival";
            } else if (emp.getCurTime() < emp.getStartTimeML()) {
                status = "Early Arrival";
            } else if (emp.getCurTime() == emp.getStartTimeML()) {
                status = "On Time";
            }
        }
        String t = mysql_time.format(new Date(rd.getTime().getTime()));
        int count = getIntValue("select count(*) from attendance where date='" + rd.getDate() + "' and time='" + t + "' and emp_code='" + rd.getCardNo() + "'");
        if (count == 0) {
            executeUpdate("insert into attendance(date, time, emp_code, type, " + "status, remarks, last,location) values('" + rd.getDate() + "', '" + t + "' , '" + rd.getCardNo() + "', '" + type + "', '" + status + "', 'N/A','0', '" + rd.getMid() + "')");
        } else {
            System.out.print("Already Present ... ");
        }
    }

    public List<Employee> getUnMarkedData(String date) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Employee> empList = null;
        try {
            con = connectToDb();
            stmt = con.prepareStatement(UNMARKED_EMPLOYEE_SQL);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            empList = new ArrayList<Employee>();
            if (rs.next()) {
                empList.add(new Employee(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(rs, stmt, con);
        }
        return empList;
    }

    public Employee getEmpData(String code, Timestamp time) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Employee employee = null;
        try {
            con = connectToDb();
            stmt = con.prepareStatement(EMPLOYEE_SQL);
            stmt.setTimestamp(1, time);
            stmt.setString(2, code);
            rs = stmt.executeQuery();
            if (rs.next()) {
                employee = new Employee(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(rs, stmt, con);
        }
        return employee;
    }

    public List<RawData> getRawData() {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RawData> list = null;
        try {
            con = connectToDb();
            stmt = con.prepareStatement(RAW_SQL);
            rs = stmt.executeQuery();
            list = new ArrayList<RawData>();
            while (rs.next()) {
                list.add(new RawData(rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(rs, stmt, con);
        }
        return list;
    }

    public void removeRawData(List<RawData> list) {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = connectToDb();
            stmt = con.prepareStatement(REMOVE_RAW_SQL);
            for (RawData d : list) {
                stmt.setString(1, d.getCardNo());
                stmt.setTimestamp(2, d.getTime());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(null, stmt, con);
        }
    }

    public static void main(String[] args) {
        Database.init("jdbc:mysql://localhost:3306/warid", "root", "intello");
        Thread t = new Thread(new DataSynchronizer());
        t.start();
    }
}
