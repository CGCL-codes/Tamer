package l1j.server.server;

import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.util.logging.Logger;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.L1DatabaseFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.serverpackets.S_SystemMessage;

public class SkillCheck {

    private static Logger _log = Logger.getLogger(SkillCheck.class.getName());

    private HashMap _SkillCheck = new HashMap();

    private static SkillCheck _instance;

    private SkillCheck() {
    }

    public static SkillCheck getInstance() {
        if (_instance == null) {
            _instance = new SkillCheck();
        }
        return _instance;
    }

    public boolean AddSkill(int objid, int skillId) {
        if (_SkillCheck.get(objid + skillId) == null) {
            _SkillCheck.put(objid + skillId, "");
        } else {
        }
        return false;
    }

    public void CheckSkill(L1PcInstance pc, int skillId) {
        if (_SkillCheck.get(pc.getId() + skillId) == null) {
            pc.setSkillCheck(1);
        }
    }

    public void DelSkill(int objid, int skillId) {
        if (_SkillCheck.get(objid + skillId) != null) {
            _SkillCheck.remove(objid + skillId);
        } else {
        }
    }

    public void QuitDelSkill(L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=? ");
            pstm.setInt(1, pc.getId());
            rs = pstm.executeQuery();
            while (rs.next()) {
                int skillId = rs.getInt("skill_id");
                if (_SkillCheck.get(pc.getId() + skillId) != null) {
                    _SkillCheck.remove(pc.getId() + skillId);
                } else {
                }
            }
        } catch (SQLException e) {
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
