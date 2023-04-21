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
public class NationalityLogic {

    private static NationalityLogic logic = null;

    private NationalityLogic() {
    }

    public static NationalityLogic getInstance() {
        if (logic == null) logic = new NationalityLogic();
        return logic;
    }

    public ArrayList<HsHrNationality> getNationalitys() {
        ArrayList<HsHrNationality> arr = new ArrayList<HsHrNationality>();
        try {
            List ls = HsHrNationalityPeer.doSelect(new Criteria());
            for (int i = 0; i < ls.size(); i++) {
                HsHrNationality h = (HsHrNationality) ls.get(i);
                arr.add(h);
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public HsHrNationality getNationality(String natCode) {
        try {
            Criteria c = new Criteria();
            c.add(HsHrNationalityPeer.NAT_CODE, (Object) natCode, SqlEnum.EQUAL);
            List ls = HsHrNationalityPeer.doSelect(c);
            for (Object h : ls) {
                HsHrNationality hs = (HsHrNationality) h;
                return hs;
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addNationality(HsHrNationality m) {
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

    public boolean delNationality(String natCode) {
        try {
            Criteria c = new Criteria();
            c.add(HsHrNationalityPeer.NAT_CODE, (Object) natCode, SqlEnum.EQUAL);
            HsHrNationalityPeer.doDelete(c);
            return true;
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return false;
    }
}
