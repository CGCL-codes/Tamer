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
public class EmployeeWorkshiftLogic {

    private static EmployeeWorkshiftLogic logic = null;

    private EmployeeWorkshiftLogic() {
    }

    public static EmployeeWorkshiftLogic getInstance() {
        if (logic == null) logic = new EmployeeWorkshiftLogic();
        return logic;
    }

    public ArrayList<HsHrEmployeeWorkshift> getEmployeeWorkshifts() {
        ArrayList<HsHrEmployeeWorkshift> arr = new ArrayList<HsHrEmployeeWorkshift>();
        try {
            List ls = HsHrEmployeeWorkshiftPeer.doSelect(new Criteria());
            for (int i = 0; i < ls.size(); i++) {
                HsHrEmployeeWorkshift h = (HsHrEmployeeWorkshift) ls.get(i);
                arr.add(h);
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public HsHrEmployeeWorkshift getEmployeeWorkshift(int workshiftId, int empNumber) {
        try {
            Criteria c = new Criteria();
            c.add(HsHrEmployeeWorkshiftPeer.WORKSHIFT_ID, (Object) workshiftId, SqlEnum.EQUAL);
            c.add(HsHrEmployeeWorkshiftPeer.EMP_NUMBER, (Object) empNumber, SqlEnum.EQUAL);
            List ls = HsHrEmployeeWorkshiftPeer.doSelect(c);
            for (Object h : ls) {
                HsHrEmployeeWorkshift hs = (HsHrEmployeeWorkshift) h;
                return hs;
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addEmployeeWorkshift(HsHrEmployeeWorkshift m) {
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

    public boolean delEmployeeWorkshift(int workshiftId, int empNumber) {
        try {
            Criteria c = new Criteria();
            c.add(HsHrEmployeeWorkshiftPeer.WORKSHIFT_ID, (Object) workshiftId, SqlEnum.EQUAL);
            c.add(HsHrEmployeeWorkshiftPeer.EMP_NUMBER, (Object) empNumber, SqlEnum.EQUAL);
            HsHrEmployeeWorkshiftPeer.doDelete(c);
            return true;
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return false;
    }
}
