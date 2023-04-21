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
public class ConfigLogic {

    private static ConfigLogic logic = null;

    private ConfigLogic() {
    }

    public static ConfigLogic getInstance() {
        if (logic == null) logic = new ConfigLogic();
        return logic;
    }

    public ArrayList<HsHrConfig> getConfigs() {
        ArrayList<HsHrConfig> arr = new ArrayList<HsHrConfig>();
        try {
            List ls = HsHrConfigPeer.doSelect(new Criteria());
            for (int i = 0; i < ls.size(); i++) {
                HsHrConfig h = (HsHrConfig) ls.get(i);
                arr.add(h);
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public HsHrConfig getConfig(String key) {
        try {
            Criteria c = new Criteria();
            c.add(HsHrConfigPeer.KEY, (Object) key, SqlEnum.EQUAL);
            List ls = HsHrConfigPeer.doSelect(c);
            for (Object h : ls) {
                HsHrConfig hs = (HsHrConfig) h;
                return hs;
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addConfig(HsHrConfig m) {
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

    public boolean delConfig(String key) {
        try {
            Criteria c = new Criteria();
            c.add(HsHrConfigPeer.KEY, (Object) key, SqlEnum.EQUAL);
            HsHrConfigPeer.doDelete(c);
            return true;
        } catch (TorqueException e) {
            e.printStackTrace();
        }
        return false;
    }
}
