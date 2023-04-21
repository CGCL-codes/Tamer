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
public class EmpDirectdebitLogic {

    private static EmpDirectdebitLogic logic = null;

    private EmpDirectdebitLogic() {
    }

    public static EmpDirectdebitLogic getInstance() {
        if (logic == null) logic = new EmpDirectdebitLogic();
        return logic;
    }

    public ArrayList<HsHrEmpDirectdebit> getEmpDirectdebits() {
        ArrayList<HsHrEmpDirectdebit> arr = new ArrayList<HsHrEmpDirectdebit>();
        try {
            List ls = HsHrEmpDirectdebitPeer.doSelect(new Criteria());
            for (int i = 0; i < ls.size(); i++) {
                HsHrEmpDirectdebit h = (HsHrEmpDirectdebit) ls.get(i);
                arr.add(h);
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public ArrayList<HsHrEmpDirectdebit> getEmpDirectdebits(int empNumber) {
        ArrayList<HsHrEmpDirectdebit> arr = new ArrayList<HsHrEmpDirectdebit>();
        try {
            Criteria c = new Criteria();
            c.add(HsHrEmpDirectdebitPeer.EMP_NUMBER, (Object) empNumber, SqlEnum.EQUAL);
            List ls = HsHrEmpDirectdebitPeer.doSelect(c);
            for (int i = 0; i < ls.size(); i++) {
                HsHrEmpDirectdebit h = (HsHrEmpDirectdebit) ls.get(i);
                arr.add(h);
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public HsHrEmpDirectdebit getEmpDirectdebit(Double ddSeqno, int empNumber) {
        try {
            Criteria c = new Criteria();
            c.add(HsHrEmpDirectdebitPeer.DD_SEQNO, (Object) ddSeqno, SqlEnum.EQUAL);
            c.add(HsHrEmpDirectdebitPeer.EMP_NUMBER, (Object) empNumber, SqlEnum.EQUAL);
            List ls = HsHrEmpDirectdebitPeer.doSelect(c);
            for (Object h : ls) {
                HsHrEmpDirectdebit hs = (HsHrEmpDirectdebit) h;
                return hs;
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addEmpDirectdebit(HsHrEmpDirectdebit m) {
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

    public boolean delEmpDirectdebit(Double ddSeqno, int empNumber) {
        try {
            Criteria c = new Criteria();
            c.add(HsHrEmpDirectdebitPeer.DD_SEQNO, (Object) ddSeqno, SqlEnum.EQUAL);
            c.add(HsHrEmpDirectdebitPeer.EMP_NUMBER, (Object) empNumber, SqlEnum.EQUAL);
            HsHrEmpDirectdebitPeer.doDelete(c);
            return true;
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return false;
    }
}
