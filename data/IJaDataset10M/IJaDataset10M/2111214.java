package net.cygeek.tech.client.service;

import java.util.ArrayList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.cygeek.tech.client.logic.SkillLogic;
import net.cygeek.tech.client.HsHrSkill;
import net.cygeek.tech.client.data.Skill;
import java.util.Date;

/**
 * Author: Thilina Hasantha
 */
public class SkillServiceImpl extends RemoteServiceServlet implements SkillService {

    SkillLogic logic = SkillLogic.getInstance();

    public ArrayList getSkills() {
        ArrayList a = new ArrayList();
        ArrayList<HsHrSkill> k = logic.getSkills();
        for (HsHrSkill h : k) {
            a.add(Skill.getProxy(h));
        }
        return a;
    }

    public Boolean addSkill(Skill mSkill, boolean isNew) {
        HsHrSkill bean = Skill.getClass(mSkill);
        bean.setNew(isNew);
        return logic.addSkill(bean);
    }

    public Boolean deleteSkill(String skillCode) {
        return logic.delSkill(skillCode);
    }

    public Skill getSkill(String skillCode) {
        HsHrSkill k = logic.getSkill(skillCode);
        return Skill.getProxy(k);
    }
}
