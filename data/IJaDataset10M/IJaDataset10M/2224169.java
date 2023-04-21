package net.cygeek.tech.client.logic;

import net.cygeek.tech.client.*;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.SqlEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * @Author: Thilina Hasantha
 */
public class EmployeeTimesheetPeriodLogic {

    private static EmployeeTimesheetPeriodLogic logic = null;

    private EmployeeTimesheetPeriodLogic() {
    }

    public static EmployeeTimesheetPeriodLogic getInstance() {
        if (logic == null) logic = new EmployeeTimesheetPeriodLogic();
        return logic;
    }

    public ArrayList<HsHrEmployeeTimesheetPeriod> getEmployeeTimesheetPeriods() {
        ArrayList<HsHrEmployeeTimesheetPeriod> arr = new ArrayList<HsHrEmployeeTimesheetPeriod>();
        try {
            List ls = HsHrEmployeeTimesheetPeriodPeer.doSelect(new Criteria());
            for (int i = 0; i < ls.size(); i++) {
                HsHrEmployeeTimesheetPeriod h = (HsHrEmployeeTimesheetPeriod) ls.get(i);
                arr.add(h);
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public HsHrEmployeeTimesheetPeriod getEmployeeTimesheetPeriod(int timesheetPeriodId, int employeeId) {
        try {
            Criteria c = new Criteria();
            c.add(HsHrEmployeeTimesheetPeriodPeer.TIMESHEET_PERIOD_ID, (Object) timesheetPeriodId, SqlEnum.EQUAL);
            c.add(HsHrEmployeeTimesheetPeriodPeer.EMPLOYEE_ID, (Object) employeeId, SqlEnum.EQUAL);
            List ls = HsHrEmployeeTimesheetPeriodPeer.doSelect(c);
            for (Object h : ls) {
                HsHrEmployeeTimesheetPeriod hs = (HsHrEmployeeTimesheetPeriod) h;
                return hs;
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addEmployeeTimesheetPeriod(HsHrEmployeeTimesheetPeriod m) {
        try {
            java.sql.Connection c = Torque.getConnection();
            m.save(c);
            Torque.closeConnection(c);
            return true;
        } catch (TorqueException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delEmployeeTimesheetPeriod(int timesheetPeriodId, int employeeId) {
        try {
            Criteria c = new Criteria();
            c.add(HsHrEmployeeTimesheetPeriodPeer.TIMESHEET_PERIOD_ID, (Object) timesheetPeriodId, SqlEnum.EQUAL);
            c.add(HsHrEmployeeTimesheetPeriodPeer.EMPLOYEE_ID, (Object) employeeId, SqlEnum.EQUAL);
            HsHrEmployeeTimesheetPeriodPeer.doDelete(c);
            return true;
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return false;
    }
}
